;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.pl
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"08-15" {:name (partial n/get-name "08-15")},
 "easter 60" {:name (partial n/get-name "easter 60")},
 "01-01" {:name (partial n/get-name "01-01")},
 "11-01" {:name (partial n/get-name "11-01")},
 "05-26"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "easter" {:name (partial n/get-name "easter")},
 "05-03"
 {:name
  {:pl "Święto Narodowe Trzeciego Maja", :en "Constitution Day"}},
 "12-26" {:name (partial n/get-name "12-26")},
 "easter 49" {:name (partial n/get-name "easter 49")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "05-01"
 {"_name" "05-01", :name {:pl "Święto Państwowe; Święto Pracy"}},
 "01-06" {:name (partial n/get-name "01-06")},
 "11-11" {:name (partial n/get-name "Independence Day")},
 "12-25" {:name (partial n/get-name "12-25")}}
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


(defmethod is-holiday? :pl
  [_ context]
  (holiday? context))