;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.ve
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {"type" "observance", :name (partial n/get-name "12-08")},
 "easter -48" {:name {:en "Carnival", :es "Carnaval"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "10-12"
 {:name
  {:en "Day of Indigenous Resistance",
   :es "Día de la resistencia indígena"}},
 "easter -47" {:name (partial n/get-name "easter -47")},
 "07-24"
 {:name
  {:en "Birthday of Simón Bolívar", :es "Natalicio de Simón Bolívar"}},
 "11-01" {"type" "observance", :name (partial n/get-name "11-01")},
 "08-03"
 {"type" "observance",
  :name {:en "Flag Day", :es "Día de la Bandera"}},
 "09-11"
 {"type" "observance",
  :name
  {:en "Our Lady of Coromoto",
   :es "Día de Nuestra Señora de Coromoto"}},
 "easter" {:name (partial n/get-name "easter")},
 "06-24"
 {:name
  {:en "Battle of Carabobo",
   :es "Aniversario de la Batalla de Carabobo"}},
 "12-24" {:name (partial n/get-name "12-24")},
 "easter -6 P7D"
 {"type" "observance", :name {:en "Holy Week", :es "Semana Santa"}},
 "07-05" {:name (partial n/get-name "Independence Day")},
 "03-19" {"type" "observance", :name (partial n/get-name "03-19")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "12-10"
 {"type" "observance",
  :name
  {:en "Venezuelan Air Force Day", :es "Día de la Aviación Nacional"}},
 "04-19"
 {:name
  {:en "Declaration of Independence",
   :es "Declaración de la Independencia"}},
 "01-15"
 {"type" "optional",
  "note" "teachers only",
  :name {:es "Día del Maestro", :en "Teacher's Day"}},
 "11-02" {"type" "observance", :name (partial n/get-name "11-02")},
 "05-01" {:name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "01-06" {"type" "observance", :name (partial n/get-name "01-06")},
 "12-31" {:name (partial n/get-name "12-31")},
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


(defmethod is-holiday? :ve
  [_ context]
  (holiday? context))