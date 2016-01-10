(ns vura.async.scheduler
  #?(:cljs (:require-macros [dreamcatcher.core :refer [safe]]
                            [cljs.core.async.macros :refer [go go-loop]]))
  (:require
    #?(:clj [dreamcatcher.core :refer [safe]])
    [vura.async.cron :refer [next-timestamp valid-timestamp?]]
    [vura.async.jobs :refer [start! stop! make-job
                             started? started-at?
                             finished? active?] :as j]
    #?(:clj [clojure.core.async :refer [go go-loop <! put! chan timeout alts! close!] :as async]
            :cljs [cljs.core.async :refer [<! put! chan timeout alts! close!] :as async])
    #?(:clj [clj-time.core :as t]
            :cljs [cljs-time.core :as t])
    #?(:clj [clj-time.local :refer (local-now)]
            :cljs [cljs-time.local :refer (local-now)])))


(def ^:dynamic *scheduler-exception-fn* (fn [job-name job-definition exception]
                                          (println (.getMessage exception))))


;; Schedule definitions
(defprotocol ScheduleActions
  (add-job [this job-name job ^String schedule] "Function adds Job. to this schedule")
  (remove-job [this job-name] "Function removes Job. form this schedule")
  (replace-job [this job-name new-job ^String schedule] "Function replaces Job. to this schedule")
  (reschedule-job [this job-name ^String schedule] "Function reschedules Job. with new schedule"))

(defprotocol ScheduleInfo
  (get-job [this job-name] "Returns Job. instance of job-name")
  (get-jobs [this] "Get all scheduled jobs names")
  (get-schedules [this] "List CRON schedules")
  (get-schedule [this job-name] "Returns CRON string for given job-name"))


(defn make-schedule
  "Functions returns reify instance. Jobs
  are supposed to be argument of 3ies.

  job-name, Job., CRON-schedule"
  [& jobs]
  (let [args (vec (partition 3 jobs))
        schedule-data (atom nil)
        schedule (reify
                   ScheduleInfo
                   (get-job [this job] (-> @schedule-data (get job) ::job))
                   (get-jobs [this] (-> @schedule-data keys))
                   (get-schedule [this job] (-> @schedule-data (get job) ::schedule))
                   (get-schedules [this] (let [jobs (-> @schedule-data keys)]
                                           (reduce merge (for [x jobs] (hash-map x (-> @schedule-data (get x) ::schedule))))))
                   ScheduleActions
                   (add-job [this job-name job s] (when-not (-> @schedule-data (get job-name)) (swap! schedule-data assoc job-name {::schedule s ::job job}) this))
                   (remove-job [this job-name] (swap! schedule-data dissoc job-name))
                   (replace-job [this job-name new-job s] (do
                                                            (remove-job this job-name)
                                                            (add-job this job-name new-job s)
                                                            this))
                   (reschedule-job [this job-name new-schedule] (do
                                                                  (swap! schedule-data assoc job-name {::schedule new-schedule ::job (-> @schedule-data (get job-name) ::job)})
                                                                  this)))]
    (doseq [[_ job :as x] args]
      (do
        (assert (satisfies? j/JobInfo job) "Every instance of jobs has to implement JobInfo protocol.")
        (assert (satisfies? j/JobActions job) "Every instance of jobs has to implement JobActions protocol.")
        (apply add-job schedule x)))
    schedule))

;; Immutable

(defprotocol DispatcherActions
  (start-dispatching! [this] "Function activates dispatcher. Returns true if successfull or false if not.")
  (stop-dispatching! [this] "Function deactivates dispatcher. Always returns true")
  (disable-dispatcher [this] "Function disables dispatcher, stopping it from executing permenantly. Eaven if start-dispatching! is ran."))

(defn- period [a b]
  (t/in-millis (t/interval a b)))

(defn wake-up-at? [schedule]
  (let [schedules (get-schedules schedule)
        timestamp (local-now)
        next-timestamps (for [[job-name job] schedules] (next-timestamp timestamp job))]
    (first (sort-by #(period timestamp %) next-timestamps))))

(defn job-candidates? [schedule]
  (let [timestamp (local-now)
        candidates (remove nil? (for [[job-name job] (get-schedules schedule)] (when (valid-timestamp? timestamp job) job-name))) ]
    candidates))


(defn make-dispatcher [schedule]
  (assert (satisfies? ScheduleInfo schedule) "Input data doesn't implement ScheduleInfo")
  (let [dispatch? (atom false)
        control-channel (chan)]
    ;; Dispatcher life cycle
    (go-loop []
             (let [[control-data] (alts! [control-channel (go (<! (timeout (period (local-now) (wake-up-at?  schedule)))) ::TIMEOUT)])]
               (when-not (nil? control-data)
                 (if-not @dispatch?
                   ;; If dispatcher is disabled and timeout occured
                   ;; wait for ::START
                   (if (= ::START (<! control-channel))
                     (do
                       (reset! dispatch? true)
                       (recur))
                     (recur))
                   ;; If dispatcher is running
                   (let [candidates (-> schedule job-candidates?)]
                     (doseq [x candidates]
                       (try
                         (start! (get-job schedule x))
                         #?(:clj
                             (catch Exception e (*scheduler-exception-fn* x (get-job schedule x) e))
                             :cljs
                             (catch :default e (*scheduler-exception-fn* x (get-job schedule x) e)))))
                     (recur))))))
    (reify
      DispatcherActions
      (start-dispatching! [this]
        (do
          (reset! dispatch? true)
          (put! control-channel ::START)))
      (stop-dispatching! [this]
        (do
          (reset! dispatch? false)
          true))
      (disable-dispatcher [_] (close! control-channel)))))


(comment
  (def test-job (make-job
                  [:telling (safe
                              (println (local-now))
                              (println "Telling!"))
                   :throwning (safe (println "Throwing..."))]))

  (def another-job1 (make-job
                      [:drinking (safe
                                   (println (local-now))
                                   (println  "job1 drinking"))
                       :going-home (safe (println "job1 going home"))]))

  #?(:clj
      (def long-job (make-job
                      [:phuba (safe
                                (println (local-now))
                                (println "Going from phuba!")
                                (async/<!! (timeout 2000)))
                       :letovanic (safe
                                    (println "paryting in Letovanic")
                                    (async/<!! (timeout 5000)))])))


(def test-schedule (make-schedule
                     :test-job test-job "4/10 * * * * * *"
                     #?@(:clj [:long-job long-job "*/2 * * * * * *"])
                     :another another-job1 "*/15 * * * * * *"))

(def t (make-schedule
         :t test-job "0 0/20 * * * * *"))

(def suicide-job (make-job
                   [:buying-rope (safe (println "@" (local-now)) (println "Suicide is buying a rope! Watch out!"))
                    :suicide (safe (println "Last goodbay!"))])))


(comment

  (defschedule test-schedule
    [:test-job test-job "0/10 * * * * * *"])

  (def test-dispatcher (make-dispatcher test-schedule))


  (defjob another-job1 [:drinking (safe (println "job1 drinking"))
                        :going-home (safe (println "job1 going home")) (wait-for 1000)])

  (defjob suicide-job [:buying-rope (safe (println "Suicide is buying a rope! Watch out!"))
                       :suicide (safe (println "Last goodbay!"))])

  (defjob test-job [:test1 (safe (println "Testis 1"))
                    :test2 (safe (println "Testis 2")) (wait-for 3000)
                    :test3 (safe (println "Testis 3"))])



  (defschedule s [:t test-job "5 * * * * * *"
                  :a another-job1 "*/10 * * * * * *"
                  :s suicide-job "*/4 * * * * * *"])

  (defschedule t [:t test-job "0 0/20 * * * * *"])

  (def x (make-dispatcher s))

  (def test-schedule (make-schedule
                           (:test-job test-job "* * * * * * *")
                           (:another another-job1 "* 1 * * * * *"))))
