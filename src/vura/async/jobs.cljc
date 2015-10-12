(ns vura.async.jobs
  #?(:cljs
      (:require-macros [dreamcatcher.core :refer defstm]
                       [cljs.core.async.macros :refer [go alt! go-loop mult mix admix]]))
  (:require
    [dreamcatcher.util :refer [has-transition?
                               get-transitions
                               get-transition
                               get-validators
                               get-states]]
    [dreamcatcher.util :refer :all]
    [dreamcatcher.async :refer :all]
    [dreamcatcher.core :refer :all]
    #?@(:clj [[clojure.core.async :refer [go go-loop chan >! <! alt!! put! close! take! mult mix admix]]
              [clj-time.core :as t]
              [clj-time.local :refer (local-now)]])
    #?@(:cljs [[cljs.core.async :refer [chan >! <! put! close! take!]]
               [cljs-time.core :as t]
               [cljs-time.local :refer (local-now)]])))

(def ^:private start-mark "*started-at*")
(def ^:private end-mark "*ended-at*")
(def ^:private running-mark "*running*")
(def ^:private disabled-mark "*disabled*")

;; Basic state machine for linear job execution

(def ^:private blank-job-machine
  (make-state-machine
    [::initialize ::start (fn [x] (assoc-in x [:data start-mark] (local-now)))
     ::start ::finished (fn [x] (-> x
                                (assoc-in [:data end-mark] (local-now))
                                (assoc-in [:data running-mark] false)))]))

;; Helper function for job phase buildup
(defn- get-next-phase [job phase]
  (-> job (get-transitions phase) keys first))

(defn- get-job-phases [job]
  (let [job (assoc job :state ::start)]
    (loop [phase ::start
           phases nil]
      (if (= phase ::finished) (-> phases reverse rest)
        (recur (get-next-phase job phase) (conj phases phase))))))

(defn- get-previous-phase [job phase]
  (or (first (filter #(has-transition? job % phase) (get-job-phases job))) ::start))


;; Job definition manipulation
(defn add-phase
  "Functions adds phase to the end
  of the phase chain that completes
  job"
  ([job new-phase [function validator]]
   (assert (not-any? #(= % new-phase) (get-states @job)) "Phase already defined")
   (let [last-phase (first (filter #(has-transition? @job % ::finished) (get-states @job)))]
     (remove-transition job last-phase ::finished)
     (remove-validator job last-phase ::finished)
     (add-state job new-phase)
     (add-transition job last-phase new-phase function)
     (when validator (add-validator job last-phase new-phase validator))
     (add-transition job new-phase ::finished (fn [x] (-> x
                                                          (assoc-in [:data end-mark] (local-now))
                                                          (assoc-in [:data running-mark] false))))
     job)))

(defn insert-phase
  "Inserts new phase after at-phase"
  ([job new-phase at-phase function validator]
   (assert (not-any? #(= % new-phase) (get-states @job)) "Phase already defined")
   (let [next-phase (get-next-phase @job at-phase)
         transition (get-transition @job at-phase next-phase)
         p-validator (first (get-validators @job at-phase))]
     (remove-transition job at-phase next-phase)
     (remove-validator job at-phase next-phase)
     (add-state job new-phase)
     (add-transition job at-phase new-phase function)
     (add-transition job new-phase next-phase transition)
     (when validator (add-validator job new-phase next-phase validator))
     (when p-validator (add-validator job at-phase new-phase validator))
     job)))

(defn remove-phase
  "Removes phase from job"
  [^clojure.lang.Atom job phase]
  (assert (some #(= % phase) (get-states @job)) "Phase is not defined for this job")
  (let [next-phase (get-next-phase @job phase)
        previous-phase (get-previous-phase @job phase)
        tp (get-transition @job phase next-phase)
        vp (first (get-validators @job phase))]
    (remove-transition job previous-phase phase)
    (remove-validator job previous-phase phase)
    (remove-state job phase)
    (add-transition job previous-phase next-phase tp)
    (add-validator job previous-phase next-phase vp)
    job))


;; Following... Job domain definition

(defprotocol JobInfo
  (at-phase? [this] "Returns current phase that job-agent is working on")
  (before-phase? [this phase] "Returns boolean true if current phase that job-agent is working on is before input phase")
  (after-phase? [this phase] "Returns boolean true if current phase that job-agent is working on is before input phase")
  (get-phases [this] "Lists all Job phases")
  (started-at? [this] "Returns org.joda.time.DateTime timestamp")
  (ended-at? [this] "Returns org.joda.time.DateTime timestamp")
  (started? [thsi] "Returns true if job is not in phase start")
  (finished? [this] "Returns true is job is in ::finished state")
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
  (let [job (atom blank-job-machine)]
    (loop [phases phases]
      (if (empty? phases)
        @job
        (recur
          (do
            (add-phase job (first phases) (take-while fn? (rest phases)))
            (drop-while fn? (rest phases))))))))


(defn make-job [phases]
  (let [job-data (atom nil)
        ground-channel (chan 1)
        end-collector (mix ground-channel)
        state-machine-shell (make-job-shell phases)
        state-machine-instance (wrap-async-machine state-machine-shell)]
    ;; Really ground this channel
    (mult ground-channel)
    (admix
      end-collector
      (suck state-machine-instance
            ::start
            1
            (map
              (with-stm x
                (swap! job-data assoc start-mark (get-in x [:data start-mark]))
                true))))
    (admix
      end-collector
      (suck state-machine-instance
            ::finished
            1
            (map
              (with-stm x
                (swap! job-data assoc end-mark (get-in x [:data end-mark]))
                (swap! job-data assoc running-mark false)
                true))))
    (doseq [s (keys (state-channels? state-machine-instance))]
      (admix
        end-collector
        (suck state-machine-instance
              s
              1
              (map (with-stm x
                     (swap! job-data assoc ::at-phase s)
                     true)))))
    (reify
      JobInfo
      #_(at-phase? [this] (get @job-data ::at-phase))
      #_(before-phase? [this phase] (when (at-phase? this)
                                    (<
                                     (.indexOf (get-phases this) (at-phase? this))
                                     (.indexOf (get-phases this) phase))))
      #_(after-phase? [this phase] (when (at-phase? this)
                                   (>
                                    (.indexOf (get-phases this) (at-phase? this))
                                    (.indexOf (get-phases this) phase))))
      (get-phases [this]
        (let [job (assoc state-machine-shell :state ::initialize)]
          (loop [phase ::initialize phases nil]
            (if (= phase ::finished) (-> phases (conj ::finished) reverse)
              (recur (get-next-phase job phase) (conj phases phase))))))
      (started-at? [this] (get @job-data start-mark))
      (ended-at? [this] (get @job-data end-mark))
      (started? [this] (-> this started-at? boolean))
      (finished? [this] (-> this ended-at? boolean))
      (active? [this] (get @job-data running-mark))
      (duration? [this] (when (finished? this)
                          (-> (t/interval (started-at? this) (ended-at? this)) t/in-millis)))
      (in-error? [this] nil)
      JobActions
      (start! [this data]
        (if (get @job-data disabled-mark)
          (throw (Exception. (str "Job was stopped at phase: " (at-phase? this))))
          (if (and (not (finished? this)) (active? this))
            (throw (Exception. (str "Job hasn't finished jet! Currently running transition to phase: "
                                    (nth (vec (get-phases this)) (inc (.indexOf (get-phases this) (at-phase? this)))))))
            (do
              (swap! job-data assoc running-mark true start-mark nil end-mark nil)
              (inject state-machine-instance ::initialize data)))))
      (start! [this] (start! this nil))
      (stop! [this] (do
                      (swap! job-data assoc running-mark false disabled-mark true)
                      (disable state-machine-instance))))))


;; Intended for static job definition
(def test-job (make-job [:test1 (safe  (println "Testis 1")) #_(fn [_] (Thread/sleep 2000) true)
                         :test2 (safe  (println "Testis 2")) #_(fn [_] (Thread/sleep 5000) true)
                         :test3 (safe  (println "Testis 3")) #_(fn [_] (Thread/sleep 10000) true)]))

(def t (make-job-shell
         [:test1 (safe  (println "Testis 1")) #_(fn [_] (Thread/sleep 2000) true)
          :test2 (safe  (println "Testis 2")) #_(fn [_] (Thread/sleep 5000) true)
          :test3 (safe  (println "Testis 3")) #_(fn [_] (Thread/sleep 10000) true)]))
