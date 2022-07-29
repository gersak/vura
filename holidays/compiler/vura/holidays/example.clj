(ns vura.holidays.example
  (:require
    [vura.holidays :as h]
    [vura.holidays.catholic :as catholic]))



(defn holiday [day-context]
  (some
    (fn [[k v]] (when (k day-context) v))
    {catholic/easter? {:name "USKRS"}
     catholic/christmas? {:name "BOZIC"}
     (h/static-holiday 1 5) {:name "NEKI BLAGDAN"}
     (h/static-holiday 1 1) {:name "NEKI BLAGDAN2"}
     (h/static-holiday 8 10) {:name "NEKI BLAGDAN3"}
     (h/static-holiday 26 12) {:name "NEKI BLAGDAN4"}}))

(defmethod h/is-holiday? "HR" [_ day-context] (holiday day-context))


(comment
  (require '[vura.core :as v])
  (catholic/easter 2026)
  (-> (v/date 2022 12 26)
      v/time->value
      v/day-time-context
      holiday)
  (h/holiday? (v/date 2022 12 25) "HR")
  (h/holiday? (v/date 2022 12 24) "HR")
  (h/holiday? (v/date 2026 4 5) "HR")
  (h/holiday? (v/date 2026 4 5) "MK")
  (h/holiday? (v/date 2022 12 24) "HR")
  (h/holiday? (v/date 2022 12 26) "HR")
  )
