;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.cl
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {:name (partial n/get-name "12-08")},
 "05-21" {:name {:en "Navy Day", :es "Día de las Glorias Navales"}},
 "08-15" {:name (partial n/get-name "08-15")},
 "01-01" {:name (partial n/get-name "01-01")},
 "06-29" {:name (partial n/get-name "06-29")},
 "07-16"
 {:name {:en "Our Lady of Mount Carmel", :es "Virgen del Carmen"}},
 "11-01" {:name (partial n/get-name "11-01")},
 "easter" {:name (partial n/get-name "easter")},
 "10-31 if wednesday then next friday if tuesday then previous friday"
 {:name (partial n/get-name "Reformation Day")},
 "10-12 if tuesday, wednesday, thursday, friday then previous monday"
 {:name {:en "Columbus Day", :es "Encuentro de Dos Mundos"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2022-09-16" {:name {:es "Fiestas Patrias", :en "National Holiday"}},
 "05-01" {:name (partial n/get-name "05-01")},
 "09-18" {:name {:en "National holiday", :es "Fiestas Patrias"}},
 "12-31" {"type" "bank", :name (partial n/get-name "12-31")},
 "09-19"
 {:name {:en "Army Day", :es "Día de las Glorias del Ejército"}},
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


(defmethod is-holiday? :cl
  [_ context]
  (holiday? context))