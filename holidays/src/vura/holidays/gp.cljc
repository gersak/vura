;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.gp
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"easter -2" {:name (partial n/get-name "easter -2")},
 "05-27" {:name (partial n/get-name "Abolition of Slavery")},
 "07-21"
 {:name {:fr "Jour de Victor Shoelcher", :en "Victor Shoelcher Day"}}}
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


(defmethod is-holiday? :gp
  [_ context]
  (holiday? context))