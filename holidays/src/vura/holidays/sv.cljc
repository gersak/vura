;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.sv
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"08-06" {:name {:es "Celebración del Divino Salvador del Mundo"}},
 "01-01" {"type" "observance", :name (partial n/get-name "01-01")},
 "09-15" {:name (partial n/get-name "Independence Day")},
 "easter -1" {:name (partial n/get-name "easter -1")},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "01-16"
 {"type" "observance", :name {:es "Firma de los Acuerdos de Paz"}},
 "05-10"
 {"type" "optional",
  "note" "Public Offices only",
  :name (partial n/get-name "Mothers Day")},
 "03-08" {"type" "observance", :name {:es "Día de la Mujer"}},
 "11-02" {:name (partial n/get-name "11-02")},
 "05-01" {:name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "05-07"
 {"type" "observance",
  :name {:en "Soldier's Day", :es "Día del Soldado"}},
 "06-22"
 {"type" "optional",
  "note" "Dayoff for teachers only",
  :name {:es "Día del Maestro"}},
 "12-25" {:name (partial n/get-name "12-25")},
 "06-17"
 {"type" "observance", :name (partial n/get-name "Fathers Day")}}
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


(defmethod is-holiday? :sv
  [_ context]
  (holiday? context))