;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.co
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {:name (partial n/get-name "12-08")},
 "monday after 06-29" {:name (partial n/get-name "06-29")},
 "01-01" {:name (partial n/get-name "01-01")},
 "08-07" {:name {:en "Battle of Boyacá", :es "Batalla de Boyacá"}},
 "easter 71"
 {:name {:es "Sagrado Corazón de Jesús", :en "Sacred Heart"}},
 "monday after 10-12"
 {:name {:es "Día de la Raza", :en "Columbus Day"}},
 "monday after 03-19" {:name (partial n/get-name "03-19")},
 "easter 43" {:name (partial n/get-name "easter 39")},
 "monday after 08-15" {:name (partial n/get-name "08-15")},
 "easter" {:name (partial n/get-name "easter")},
 "easter 64" {:name (partial n/get-name "easter 60")},
 "07-20" {:name (partial n/get-name "Independence Day")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "monday after 11-11"
 {:name
  {:es "Independencia de Cartagena", :en "Independence of Cartagena"}},
 "05-01" {:name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "1st monday in November" {:name (partial n/get-name "11-01")},
 "easter -7"
 {"type" "observance", :name (partial n/get-name "easter -7")},
 "12-25" {:name (partial n/get-name "12-25")},
 "monday after 01-06" {:name (partial n/get-name "01-06")}}
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


(defmethod is-holiday? :co
  [_ context]
  (holiday? context))