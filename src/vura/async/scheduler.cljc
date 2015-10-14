(ns vura.async.scheduler
  (:require
    [dreamcatcher.core :refer [safe]]
    [vura.async.cron :refer [next-timestamp valid-timestamp?]]
    [vura.async.jobs :refer [start! stop! make-job
                             started? started-at?
                             finished? active?] :as j]
    [clojure.core.async :refer [go go-loop <! put! chan timeout alts! close!]]
    [clj-time.core :as t]
    [clj-time.local :refer (local-now)]
    [taoensso.timbre :as timbre :refer (info debug warn error)]))


;; Schedule definitions
(defprotocol SchedulerActions
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
                   (get-job [this job] (-> @schedule-data (get job) :job))
                   (get-jobs [this] (-> @schedule-data keys))
                   (get-schedule [this job] (-> @schedule-data (get job) :schedule))
                   (get-schedules [this] (let [jobs (-> @schedule-data keys)]
                                           (reduce merge (for [x jobs] (hash-map x (-> @schedule-data (get x) :schedule))))))
                   SchedulerActions
                   (add-job [this job-name job s] (when-not (-> @schedule-data (get job-name)) (swap! schedule-data assoc job-name {:schedule s :job job}) this))
                   (remove-job [this job-name] (do
                                                 (-> @schedule-data (get job-name) :job stop!)
                                                 (swap! schedule-data dissoc job-name)
                                                 this))
                   (replace-job [this job-name new-job s] (do
                                                            (remove-job this job-name)
                                                            (add-job this job-name new-job s)
                                                            this))
                   (reschedule-job [this job-name new-schedule] (do
                                                                  (swap! schedule-data assoc job-name {:schedule new-schedule :job (-> @schedule-data (get job-name) :job)})
                                                                  this)))]
    (doseq [x args] (apply add-job schedule x))
    schedule))

;; Immutable

(defprotocol DispatcherActions
  (start-dispatching! [this] "Function activates dispatcher.")
  (stop-dispatching! [this] "Function deactivates dispatcher.")
  (disable-dispatcher [this]))

(defn- period [a b]
  (t/in-millis (t/interval a b)))

(defn- wake-up-at? [schedule]
  (let [schedules (get-schedules schedule)
        timestamp (local-now)
        next-timestamps (for [x schedules] (next-timestamp timestamp (second x)))]
   (first (sort-by #(period timestamp %) next-timestamps))))

(defn- job-candidates? [schedule]
  (let [timestamp (local-now)]
    (remove nil? (for [x (get-schedules schedule)] (when (valid-timestamp? timestamp (second x)) (first x))))))


(defn make-dispatcher [schedule]
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
                   (let [candidates (-> schedule job-candidates?)
                         jobs (map #(get-job schedule %) candidates)
                         finished-jobs (filter #(and (finished? %) (started? %)) jobs)]
                     (doseq [x candidates]
                       (let [job (get-job schedule x)]
                         (cond
                           (finished? job) (do
                                             (info "Restarting job: " x)
                                             (start! job))
                           :else (do
                                   (info "Starting job new: " x)
                                   (start! job)))))
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


(def test-job (make-job
                [:telling (safe (println "Telling!"))
                 :throwning (safe (println "Throwing..."))]))

(def another-job1 (make-job
                    [:drinking (safe (println "job1 drinking"))
                     :going-home (safe (println "job1 going home"))]))

(def test-schedule (-> (make-schedule nil)
                       (add-job :test-job test-job "4/10 * * * * * *")
                       (add-job :another another-job1 "*/15 * * * * * *")))



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

  (def x (make-dispatcher s))

  (def test-schedule (-> (make-schedule nil)
                         (add-job :test-job test-job "* * * * * * *")
                         (add-job :another another-job1 "* 1 * * * * *"))))
