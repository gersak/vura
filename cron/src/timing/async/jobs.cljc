(ns timing.async.jobs
  #?(:cljs
     (:require-macros 
       [cljs.core.async.macros 
        :refer [go go-loop]]))
  (:require
    [timing.core :as core]
    [dreamcatcher.async 
     :refer [wrap-async-machine 
             suck 
             inject 
             disable]]
    [dreamcatcher.core :as d]
    #?@(:clj [[clojure.core.async :refer [go go-loop chan <! mult mix admix timeout]]])
    #?@(:cljs [[clojure.core.async :refer [chan <! mult mix admix timeout]]])))


(def start-state ::start)
(def start-state ::end)
(def ^:private start-mark ::started)
(def ^:private error-mark ::error)
(def ^:private end-mark ::ended)
(def ^:private running-mark ::running?)
(def ^:private disabled-mark ::disabled?)


;; Helper function for job phase buildup
(defn get-next-phase [job phase]
  (-> job (d/get-transitions phase) keys first))


(defn _get-phases [machine]
  (loop [phase ::initialize phases nil]
    (if (= phase ::end) (-> phases (conj ::end) reverse)
      (recur (get-next-phase machine phase) (conj phases phase)))))


(defn get-job-phases [job]
  (loop [phase ::start
         phases nil]
    (if (= phase ::end) (-> phases reverse rest)
      (recur (get-next-phase job phase) (conj phases phase)))))


(defn get-previous-phase [job phase]
  (or 
    (first 
      (filter 
        #(d/has-transition? job % phase)
        (get-job-phases job))) 
    ::start))


;; Following... Job domain definition

(defprotocol JobInfo
  (at-phase? [this] "Returns current phase that job-agent is working on")
  (before-phase? [this phase] "Returns boolean true if current phase that job-agent is working on is before input phase")
  (after-phase? [this phase] "Returns boolean true if current phase that job-agent is working on is before input phase")
  (get-phases [this] "Lists all Job phases")
  (started-at? [this] "Returns org.joda.time.DateTime timestamp")
  (ended-at? [this] "Returns org.joda.time.DateTime timestamp")
  (started? [thsi] "Returns true if job is not in phase start")
  (finished? [this] "Returns true is job is in ::end state")
  (active? [this] "Returns true if job is running")
  (duration? [this] "Returns duration of job in milliseconds")
  (in-error? [this] "Returns error exception if it happend. Otherwise nil"))

(defprotocol JobActions
  (start! [this] [this data] "Starts Job. That is sends-off job-agent to do the job. If Job
  was previously stoped than it continues from last phase.")
  (stop! [this] "Stops running Job. Job will allways try to complete current phase.
  If validator doesn't allow execution to continue than Job is stoped
  at current phase."))


(defn make-job-shell [phases]
  (loop [[phase & rest-of-phases :as phases] phases
         previous-phase ::start
         machine {:transitions [::initialize ::start  identity
                                d/any-state ::start (fn [x] 
                                                    (d/update-data! x assoc start-mark (core/date)))
                                d/any-state ::end (fn [x] 
                                                       (d/update-data! x merge {end-mark (core/date)
                                                                              running-mark false}))]}]
    (if-not (some? phase) 
      (update machine :transitions 
              concat [previous-phase ::end identity])
      (recur
        (drop-while fn? rest-of-phases)
        phase
        (let [[transition validator] (take-while fn? rest-of-phases)]
          (cond-> (update machine :transitions concat [previous-phase phase transition])
            (fn? validator) (update :validators concat [previous-phase phase validator])))))))


(defn make-job [phases]
  (let [job-data (atom nil)
        ;; Create \"collector\" that will monitor start and end state 
        ground-channel (chan 1)
        end-collector (mix ground-channel)
        state-machine-shell (d/make-state-machine
                              (update
                                (make-job-shell phases) :transitions concat
                                [d/any-state d/any-state
                                 (fn [previous instance]
                                   ; (println "@Phase: " (state previous))
                                   ; (println "Next Phase: " (state instance))
                                   (swap! job-data assoc ::at-phase (d/state instance)))]))
        ; state-machine-shell (make-job-shell phases)
        state-machine-instance (wrap-async-machine 
                                 state-machine-shell 
                                 :exception-fn (fn [e] 
                                                 (swap! job-data assoc error-mark e)
                                                 (.printStackTrace e)
                                                 nil))
        phases (_get-phases state-machine-shell)]
    ;; Really ground this channel
    (mult ground-channel)
    (admix
      end-collector
      (suck state-machine-instance
            ::start
            1
            (map (fn [instance]
                   (swap! job-data assoc start-mark (get (d/data instance) start-mark))
                   (swap! job-data assoc running-mark true)
                   instance))))
    (admix
      end-collector
      (suck state-machine-instance
            ::end
            1
            (map (fn [instance]
                   (swap! job-data assoc end-mark (get (d/data instance) end-mark))
                   (swap! job-data assoc running-mark false)
                   instance))))
    (reify
      JobInfo
      (at-phase? [this] (get @job-data ::at-phase))
      (before-phase? [this phase] 
        (let [i (.getIndexOf phases phase)]
          (when (= -1 i) (throw 
                           (ex-info 
                             "Phase not definied in job" 
                             state-machine-shell)))
          (< i (.getIndexOf phases (at-phase? this)))))
      (after-phase? [this phase] 
        (let [i (.getIndexOf phases phase)]
          (when (= -1 i) (throw 
                           (ex-info
                             "Phase not definied in job" 
                             state-machine-shell)))
          (> i (.getIndexOf phases (at-phase? this)))))
      (get-phases [this] phases)
      (started-at? [this] (get @job-data start-mark))
      (ended-at? [this] (get @job-data end-mark))
      (started? [this] (-> this started-at? boolean))
      (finished? [this] (-> this ended-at? boolean))
      (active? [this] (get @job-data running-mark))
      (duration? [this] (when (finished? this)
                          (core/interval (started-at? this) (ended-at? this))))
      (in-error? [this] (get @job-data error-mark))
      JobActions
      (start! [this data]
        (condp #(get %2 %1) @job-data
          ;;
          disabled-mark 
          #?(:clj (throw (Exception. (str "Job was stopped...")))
             :cljs (throw (js/Error (str "Job was stopped..."))))
          ;;
          (and (not (finished? this)) (active? this))
          #?(:clj (throw (Exception. (str "Job hasn't finished jet!")))
             :cljs (throw (js/Error (str "Job hasn't finished jet!"))))
          ;;
          error-mark
          #?(:clj (throw (Exception. "Exception happend and job cannot continue."))
             :cljs (throw (js/Error "Exception happend and job cannot continue.")))
          ;;
          (do
            (swap! job-data assoc running-mark true start-mark nil end-mark nil)
            (inject state-machine-instance ::initialize data))))
      (start! [this] (start! this nil))
      (stop! [this] (do
                      (swap! job-data assoc running-mark false disabled-mark true)
                      (disable state-machine-instance))))))
