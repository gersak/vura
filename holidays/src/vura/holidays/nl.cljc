;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.nl
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"01-01" {:name (partial n/get-name "01-01")},
 "easter 39" {:name (partial n/get-name "easter 39")},
 "3rd sunday in June"
 {"type" "observance", :name (partial n/get-name "Fathers Day")},
 "12-15"
 {"type" "observance",
  :name {:nl "Koninkrijksdag", :en "Kingdom Day"}},
 "05-05" {"type" "observance", :name {:nl "Bevrijdingsdag"}},
 "easter 50" {:name (partial n/get-name "easter 50")},
 "easter" {:name (partial n/get-name "easter")},
 "05-04"
 {"type" "observance", :name {:nl "Nationale Dodenherdenking"}},
 "12-05"
 {"type" "observance",
  :name {:nl "Sinterklaasavond", :en "St Nicholas' Eve"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "12-26" {:name (partial n/get-name "12-26")},
 "easter 49" {:name (partial n/get-name "easter 49")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "12-31" {"type" "bank", :name (partial n/get-name "12-31")},
 "3rd tuesday in September"
 {"note" "Scholen in Den Haag geven meestal 1 dag vrij",
  "type" "observance",
  :name {:nl "Prinsjesdag"}},
 "11-11" {"type" "observance", :name (partial n/get-name "11-11")},
 "12-25" {:name (partial n/get-name "12-25")},
 "04-27 if sunday then previous saturday" {:name {:nl "Koningsdag"}}}
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


(defmethod is-holiday? :nl
  [_ context]
  (holiday? context))