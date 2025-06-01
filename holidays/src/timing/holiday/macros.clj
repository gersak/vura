(ns timing.holiday.macros)

(defmacro static-holiday [name month day]
  `(defn ~name [{day-in-month# :day-in-month month# :month}]
     (and (= month# ~month) (= day-in-month# ~day))))
