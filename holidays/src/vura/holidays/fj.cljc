;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.fj
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"09-07" {:name (partial n/get-name "Constitution Day")},
 "01-01" {:name (partial n/get-name "01-01")},
 "2023-11-12" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2022-10-24" {"_name" "Deepavali", :name {:en "Diwali"}},
 "easter -1" {:name (partial n/get-name "easter -1")},
 "2015-11-10" {"_name" "Deepavali", :name {:en "Diwali"}},
 "10-10 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Fiji Day"}},
 "12-25 if Sunday then next Monday"
 {:name (partial n/get-name "12-25")},
 "2017-10-19" {"_name" "Deepavali", :name {:en "Diwali"}},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "2024-11-01" {"_name" "Deepavali", :name {:en "Diwali"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2019-10-27" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2020-11-16"
 {"substitute" true, "_name" "Deepavali", :name {:en "Diwali"}},
 "2020-11-14" {"_name" "Deepavali", :name {:en "Diwali"}},
 "12-26 if Sunday then next Monday if Monday then next Tuesday"
 {:name (partial n/get-name "12-26")},
 "2019-10-28"
 {"substitute" true, "_name" "Deepavali", :name {:en "Diwali"}},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "2014-10-22" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2025-10-21" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2018-11-07" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2016-10-29" {"_name" "Deepavali", :name {:en "Diwali"}},
 "2023-11-13"
 {"substitute" true, "_name" "Deepavali", :name {:en "Diwali"}},
 "2021-11-04" {"_name" "Deepavali", :name {:en "Diwali"}},
 "12 Rabi al-awwal" {:name (partial n/get-name "12 Rabi al-awwal")}}
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


(defmethod is-holiday? :fj
  [_ context]
  (holiday? context))