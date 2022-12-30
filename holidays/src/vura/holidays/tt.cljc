;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.tt
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"2030-10-25" {:name (partial n/get-name "Deepavali")},
 "09-24 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Republic Day"}},
 "01-01 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "easter 60" {:name (partial n/get-name "easter 60")},
 "2028-10-17" {:name (partial n/get-name "Deepavali")},
 "2012-11-13" {:name (partial n/get-name "Deepavali")},
 "12-25 and if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "2023-11-12" {:name (partial n/get-name "Deepavali")},
 "2024-10-31" {:name (partial n/get-name "Deepavali")},
 "2015-11-11" {:name (partial n/get-name "Deepavali")},
 "2022-10-24" {:name (partial n/get-name "Deepavali")},
 "2013-11-04"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "08-31 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "Independence Day")},
 "2027-10-28" {:name (partial n/get-name "Deepavali")},
 "2017-10-19" {:name (partial n/get-name "Deepavali")},
 "easter" {:name (partial n/get-name "easter")},
 "03-30 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Spiritual Baptist Liberation Day"}},
 "12-26 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "2026-11-09"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "2029-11-05" {:name (partial n/get-name "Deepavali")},
 "08-01 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Emancipation Day"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2019-10-27" {:name (partial n/get-name "Deepavali")},
 "2020-11-14" {:name (partial n/get-name "Deepavali")},
 "2013-11-03" {:name (partial n/get-name "Deepavali")},
 "2019-10-28"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "easter 1"
 {"type" "observance", :name (partial n/get-name "easter 1")},
 "1 Shawwal and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "1 Shawwal")},
 "05-30 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Indian Arrival Day"}},
 "06-19 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "2018-11-07" {:name (partial n/get-name "Deepavali")},
 "2025-10-20" {:name (partial n/get-name "Deepavali")},
 "2016-10-29" {:name (partial n/get-name "Deepavali")},
 "2023-11-13"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "2014-10-23" {:name (partial n/get-name "Deepavali")},
 "2021-11-04" {:name (partial n/get-name "Deepavali")},
 "2026-11-08" {:name (partial n/get-name "Deepavali")}}
)


(def locale-holiday-mapping
  (reduce-kv
    (fn [result definition name-mapping]
      (assoc result
             (compiler/compile-type (compiler/parse-definition definition))
             name-mapping))
    nil
    holidays))


(defn holiday?
  [context]
  (some
   (fn [[pred naming]]
     (when (pred context)
       naming))
   locale-holiday-mapping))


(defmethod is-holiday? :tt
  [_ context]
  (holiday? context))