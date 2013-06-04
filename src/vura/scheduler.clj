(ns vura.scheduler
  (:use vura.jobs
        vura.cron
        [clj-time.local :only (local-now)]
        [vura.cron :only (next-timestamp valid-timestamp?)])
  (:require [clj-time.core :as t]
            [taoensso.timbre :as timbre :refer (info debug warn)]))


;; Schedule definitions
(defprotocol SchedulerActions
  (add-job [this job-name job ^String schedule] "Function adds Job. to this schedule")
  (remove-job [this job-name] "Function removes Job. to this schedule")
  (replace-job [this job-name new-job ^String schedule] "Function replaces Job. to this schedule")
  (reschedule-job [this job-name ^String schedule] "Function reschedules Job. with new schedule"))

(defprotocol ScheduleInfo
  (get-job [this job] "Returns Job. instance")
  (get-jobs [this] "Get all scheduled jobs")
  (get-schedules [this] "List CRON schedules")
  (get-schedule [this job] "Returns CRON string for given job")
  (get-job-phases [this] "Get current job phases"))

;; Mutable
(defrecord Schedule [^clojure.lang.Atom schedule]
  ScheduleInfo
  (get-job [this job] (-> @schedule (get job) :job))
  (get-jobs [this] (-> @schedule keys))
  (get-schedule [this job] (-> @schedule (get job) :job))
  (get-schedules [this] (let [jobs (-> @schedule keys)]
                          (reduce merge (for [x jobs] (hash-map x (-> @schedule (get x) :schedule))))))
  (get-job-phases [this] (let [jobs (-> @schedule keys)]
                           (reduce merge (for [x jobs] (hash-map x (-> @schedule (get x) :job at-phase?))))))
  SchedulerActions
  (add-job [this job-name job s] (when-not (-> @schedule (get job-name)) (swap! schedule assoc job-name {:schedule s :job job}) this))
  (remove-job [this job-name] (do 
                                (-> @schedule (get job-name) :job stop!)
                                (swap! schedule dissoc job-name)
                                this))
  (replace-job [this job-name new-job s] (do
                                           (.remove-job this job-name)
                                           (.add-job this job-name new-job s)
                                           this))
  (reschedule-job [this job-name new-schedule] (do
                                                 (swap! schedule assoc job-name {:schedule new-schedule :job (-> @schedule (get job-name) :job)})
                                                 this)))

(defn make-schedule 
  "Functions returns Schedule instance. Jobs
  are supposed to be argument of 3ies. 
  
  job-name, Job., CRON-schedule"
  [& jobs]
  (let [args (vec (partition 3 jobs))
        schedule (Schedule. (atom nil))]
    (doseq [x args] (apply add-job schedule x))
    schedule))

;; Immutable
(defrecord StaticSchedule [schedule]
  ScheduleInfo
  (get-job [this job] (-> schedule (get job) :job))
  (get-jobs [this] (-> schedule keys))
  (get-schedule [this job] (-> schedule (get job) :job))
  (get-schedules [this] (let [jobs (-> schedule keys)]
                          (reduce merge (for [x jobs] (hash-map x (-> schedule (get x) :schedule))))))
  (get-job-phases [this] (let [jobs (-> schedule keys)]
                           (reduce merge (for [x jobs] (hash-map x (-> schedule (get x) :job at-phase?)))))))

(defmacro defschedule 
  "Returns instance of StaticSchedule"
  [schedule-name mappings]
  `(def ~schedule-name (StaticSchedule. @(:schedule (make-schedule ~@mappings)))))

(defprotocol DispatcherActions
  (start-dispatching! [this] "Function activates dispatcher.")
  (stop-dispatching! [this] "Function deactivates dispatcher."))

(defn- period [a b]
  (t/in-msecs (t/interval a b)))

(defn- wake-up-at? [schedule] 
  (let [schedules (get-schedules schedule)
        timestamp (local-now)
        next-timestamps (for [x schedules] (next-timestamp timestamp (second x)))]
   (first (sort-by #(period timestamp %) next-timestamps))))

(defn- job-candidates? [schedule] 
  (let [timestamp (local-now)]
    (remove nil? (for [x (get-schedules schedule)] (when (valid-timestamp? timestamp (second x)) (first x))))))

(defn dispatcher-life [instance]
  (Thread/sleep (period (local-now) (wake-up-at? (:schedule instance))))
  (if (:running instance)
    (let [candidates (-> instance :schedule job-candidates?)
          jobs (map #(get-job (-> instance :schedule) %) candidates)
          finished-jobs (filter #(and (finished? %) (started? %)) jobs)]
      (doseq [x candidates] (let [job (-> instance :schedule (get-job x))]
                              (cond
                                (and (finished? job) (started? job)) (do 
                                                                       (reset-job! job)
                                                                       (info "Restarting job: " x)
                                                                       (start! job))
                                (and (started? job) (-> job finished? not))  (warn "Job " x " hasn't yet finished! Current phase: " (at-phase? job) "  and started at: " (started-at? job))
                                :else (do
                                        (info "Starting job new: " x)
                                        (start! job)))))
      (send-off *agent* #'dispatcher-life)
      instance)
    instance))

(defrecord Dispatcher [^clojure.lang.Agent dispatcher]
  DispatcherActions
  (start-dispatching! [this] (do 
                               (info "Starting dispatcher!")
                               (send-off dispatcher #(assoc % :running true))
                               (await dispatcher)
                               (send-off dispatcher dispatcher-life)
                               nil))
  (stop-dispatching! [this] (do 
                              (info "Stoping dispatcher!")
                              (send-off dispatcher #(assoc % :running false))
                              (await dispatcher)
                              nil)))

(defn make-dispatcher [schedule]
  (Dispatcher. (agent {:schedule schedule :running true})))




;; Testing
(defjob another-job1 [:drinking (safe (println "job1 drinking"))
                      :going-home (safe (println "job1 going home")) (wait-for 15000)])

(defjob suicide-job [:buying-rope (safe (println "Suicide is buying a rope! Watch out!"))
                     :suicide (safe (println "Last goodbay!"))])

(defjob test-job [:test1 (safe (println "Testis 1"))
                  ;:start-tests (start-jobs another-job1 suicide-job)
                  :test2 (safe (println "Testis 2")) (wait-for 3000) 
                  ;:stop-tests (stop-jobs another-job1 suicide-job) (wait-for 3000 #(-> another-job1 (after-phase? :drinking)))
                  :test3 (safe (println "Testis 3"))])



(defschedule s [:t test-job "5 * * * * * *"
                :a another-job1 "*/10 * * * * * *"
                :s suicide-job "*/4 * * * * * *"])

(def x (make-dispatcher s))

;;(def test-schedule (-> (Schedule. (atom nil)) (add-job :test-job test-job "* * * * * * *") (add-job :another another-job1 "* 1 * * * * *")))
