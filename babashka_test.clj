#!/usr/bin/env bb

;; Babashka compatibility test for timing library
(require '[timing.core :as core]
         '[timing.adjusters :as adj]
         '[timing.cron :as cron])

(println "🟢 Babashka Timing Library Compatibility Test")
(println "============================================")

;; Test basic time operations
(println "\n📅 Basic Time Operations:")
(let [date-val (core/time->value (core/date 2024 6 1 12 30 0))]
  (println "Date value:" date-val)
  (println "Back to date:" (core/value->time date-val))
  (println "Day context:" (select-keys (core/day-time-context date-val)
                                       [:year :month :day-in-month :hour :minute])))

;; Test time arithmetic
(println "\n⏰ Time Arithmetic:")
(let [base-time (core/time->value (core/date 2024 6 1))
      plus-week (+ base-time (core/weeks 1))
      plus-hours (+ base-time (core/hours 5))]
  (println "Base time:" (core/value->time base-time))
  (println "Plus 1 week:" (core/value->time plus-week))
  (println "Plus 5 hours:" (core/value->time plus-hours)))

;; Test timezone operations
(println "\n🌍 Timezone Operations:")
(let [ny-time (core/time->value (core/date 2024 6 1 12 0 0))
      london-time (core/teleport ny-time "Europe/London")]
  (println "NY time (12:00):" (core/value->time ny-time))
  (println "London equivalent:" (core/value->time london-time))
  (println "Time difference (hours):" (/ (- london-time ny-time) (core/hours 1))))

;; Test adjusters
(println "\n🔧 Adjuster Operations:")
(let [friday (core/time->value (core/date 2024 3 15))] ; March 15, 2024 is Friday
  (println "Friday:" (core/value->time friday))
  (println "Next Monday:" (core/value->time (adj/next-day-of-week friday 1)))
  (println "Start of month:" (core/value->time (adj/start-of-month friday)))
  (println "End of month:" (core/value->time (adj/end-of-month friday)))
  (println "Next business day:" (core/value->time (adj/next-business-day friday))))

;; Test calendar operations
(println "\n📆 Calendar Operations:")
(let [date-val (core/time->value (core/date 2024 6 15))]
  (println "Day of week:" (core/day? date-val))
  (println "Weekend?:" (core/weekend? date-val))
  (println "Day in month:" (core/day-in-month? date-val))
  (println "Month:" (core/month? date-val))
  (println "Year:" (core/year? date-val)))

;; Test cron functionality
(println "\n⏳ Cron Operations:")
(let [base-time (core/time->value (core/date 2024 6 1 10 0 0))
      cron-expr "0 0 12 * * ?"
      next-run (cron/next-timestamp base-time cron-expr)]
  (println "Base time:" (core/value->time base-time))
  (println "Cron expression:" cron-expr)
  (println "Next execution:" (core/value->time next-run))
  (println "Valid now?:" (cron/valid-timestamp? next-run cron-expr)))

;; Test periods and intervals
(println "\n📊 Periods and Intervals:")
(let [start (core/time->value (core/date 2024 6 1))
      end (core/time->value (core/date 2024 6 30))
      period-days (/ (- end start) (core/days 1))]
  (println "June 2024 period:" period-days "days")
  (println "In milliseconds:" (- end start))
  (println "In hours:" (/ (- end start) (core/hours 1))))

(println "\n✅ All tests completed successfully!")
(println "🎉 Timing library is Babashka compatible!")
