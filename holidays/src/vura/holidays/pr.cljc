;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.pr
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"07-25"
 {:name
  {:es "Constitución de Puerto Rico",
   :en "Puerto Rico Constitution Day"}},
 "3rd Monday in July"
 {:name
  {:es "Natalicio de Don Luis Muñoz Rivera",
   :en "Birthday of Don Luis Muñoz Rivera"}},
 "04-16"
 {:name
  {:es "Natalicio de José de Diego", :en "Birthday of José de Diego"}},
 "03-22"
 {:name
  {:es "Día de la Abolición de Esclavitud", :en "Emancipation Day"}},
 "07-27"
 {:name
  {:es "Natalicio de Dr. José Celso Barbosa",
   :en "Birthday of Dr. José Celso Barbosa"}},
 "11-19"
 {:name
  {:es "Día del Descubrimiento de Puerto Rico",
   :en "Discovery of Puerto Rico"}},
 "02-18"
 {:name
  {:es "Natalicio de Luis Muñoz Marín",
   :en "Birthday of Luis Muñoz Marín"}},
 "2nd Monday in January"
 {:name
  {:es "Natalicio de Eugenio María de Hostos",
   :en "Birthday of Eugenio María de Hostos"}},
 "01-06" {:name (partial n/get-name "01-06")},
 "03-02 since 2017"
 {:name
  {:es "Día de la Ciudadanía Americana",
   :en "American Citizenship Day"}}}
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


(defmethod is-holiday? :pr
  [_ context]
  (holiday? context))