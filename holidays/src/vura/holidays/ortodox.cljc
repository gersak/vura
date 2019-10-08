(ns vura.holidays.ortodox
  #?(:cljs [vura.holidays.macros :refer [static-holiday]])
  (:require
   [vura.core :as vura]
   [vura.holidays.core :as c]
   #?(:clj [vura.holidays.macros :refer [static-holiday]])))

(defn orthodox-easter [{:keys [year] :as day-context}]
  (let [a (mod year 4)
        b (mod year 7)
        c (mod year 19)
        d (mod (+ (* 19 c) 15) 30)
        e (mod (- (+ (* 2 a) (* 4 b) 34) d) 7)
        month (quot (+ d e 114) 31)
        day (+ 14 (mod (+ d e 114) 31))
        day-overload (quot day 30)]
    (if (pos? day-overload)
      {:year year :month (inc month) :day (mod day 30)}
      {:year year :month month :day day})))
