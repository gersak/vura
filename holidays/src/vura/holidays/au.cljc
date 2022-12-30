;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.au
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-26 and if saturday then next monday if sunday then next tuesday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "01-26 if saturday,sunday then next monday"
 {"substitute" true, :name {:en "Australia Day"}},
 "easter -1" {:name (partial n/get-name "easter -1")},
 "easter" {:name (partial n/get-name "easter")},
 "1st sunday in September"
 {"type" "observance", :name (partial n/get-name "Fathers Day")},
 "01-01 and if saturday,sunday then next monday"
 {:name (partial n/get-name "01-01")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "12-25 and if saturday then next monday if sunday then next tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "04-25" {:name {:en "Anzac Day"}},
 "2022-09-22"
 {:name {:en "National Day of Mourning for Queen Elizabeth II"}}}
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


(defmethod is-holiday? :au
  [_ context]
  (holiday? context))