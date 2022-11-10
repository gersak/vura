;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.ms
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"1st Monday in May" {:name (partial n/get-name "05-01")},
 "03-17 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name {:en "Saint Patrick's Day"}},
 "01-01 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "12-26 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "easter 50" {:name (partial n/get-name "easter 50")},
 "2nd Monday after 06-02" {:name {:en "Queen's Birthday Celebration"}},
 "08-01 if Saturday, Sunday then next Monday"
 {:name {:en "Emancipation Day"}},
 "12-31 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, "_name" "12-31", :name {:en "Festival Day"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "12-25 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")}}
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


(defmethod is-holiday? :ms
  [_ context]
  (holiday? context))