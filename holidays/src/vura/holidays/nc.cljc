;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.nc
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"01-01" {:name {:fr "Jour de l'an"}},
 "09-24"
 {:name {:fr "Fête de la Citonneyeté", :en "New Caledonia Day"}}}
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


(defmethod is-holiday? :nc
  [_ context]
  (holiday? context))