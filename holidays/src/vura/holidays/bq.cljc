;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.bq
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"04-27"
 {:name {:pap "Aña di Rey", :nl "Koningsdag", :en "Kings Day"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "easter 39" {:name (partial n/get-name "easter 39")},
 "3rd sunday in June"
 {"type" "observance", :name (partial n/get-name "Fathers Day")},
 "12-15"
 {:name
  {:pap "Dia di Reino", :nl "Koninkrijksdag", :en "Kingdom Day"}},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "12-05"
 {"type" "observance",
  :name {:nl "Sinterklaasavond", :en "St. Nicholas' Eve"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "05-01 if sunday then next monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "12-26" {:name (partial n/get-name "12-26")},
 "easter 1" {:name (partial n/get-name "easter 1")},
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


(defmethod is-holiday? :bq
  [_ context]
  (holiday? context))