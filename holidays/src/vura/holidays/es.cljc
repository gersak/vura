;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.es
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {:name (partial n/get-name "12-08")},
 "08-15" {:name (partial n/get-name "08-15")},
 "1st sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "07-25"
 {"type" "observance",
  "note" "regional",
  :name {:es "Santiago Apostol"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "10-12" {:name {:es "Fiesta Nacional de España"}},
 "substitutes 01-06 if sunday then next monday"
 {"type" "observance",
  "substitute" true,
  :name (partial n/get-name "01-06")},
 "11-01" {:name (partial n/get-name "11-01")},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "substitutes 01-01 if sunday then next monday"
 {"type" "observance",
  "substitute" true,
  :name (partial n/get-name "01-01")},
 "03-19" {"type" "observance", :name (partial n/get-name "03-19")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "12-06" {:name {:es "Día de la Constitución Española"}},
 "easter 49"
 {"type" "observance", :name (partial n/get-name "easter 49")},
 "05-01" {:name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "substitutes 12-25 if sunday then next monday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "01-06" {:name (partial n/get-name "01-06")},
 "substitutes 12-08 if sunday then next monday"
 {"type" "observance",
  "substitute" true,
  :name (partial n/get-name "12-08")},
 "substitutes 11-01 if sunday then next monday"
 {"substitute" true,
  "type" "observance",
  :name (partial n/get-name "11-01")},
 "substitutes 12-06 if sunday then next monday"
 {"type" "observance",
  "substitute" true,
  :name {:es "Día de la Constitución Española"}},
 "substitutes 10-12 if sunday then next monday"
 {"substitute" true,
  "type" "observance",
  :name {:es "Fiesta Nacional de España"}},
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


(defmethod is-holiday? :es
  [_ context]
  (holiday? context))