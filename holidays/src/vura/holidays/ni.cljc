;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.ni
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {:name (partial n/get-name "12-08")},
 "06-01" {:name {:en "Children's Day", :es "Día del niño"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "10-12"
 {:name
  {:en "Indigenous Resistance Day",
   :es "Día de la resistencia indígena"}},
 "09-15" {:name (partial n/get-name "Independence Day")},
 "09-14"
 {:name {:en "Battle of San Jacinto", :es "Batalla de San Jacinto"}},
 "12-24" {"type" "observance", :name (partial n/get-name "12-24")},
 "07-19"
 {:name
  {:en "Revolution Day", :es "Triunfo de la Revolución Popular"}},
 "06-23"
 {"_name" "Fathers Day",
  "type" "observance",
  :name {:es "Día del padre nicaragüense"}},
 "05-30"
 {"_name" "Mothers Day",
  "type" "observance",
  :name {:es "Día de la madre nicaragüense"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "05-01" {:name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "01-18"
 {"type" "observance",
  :name
  {:en "Birthday of Rubén Darío", :es "Natalicio de Rubén Darío"}},
 "12-31" {"type" "observance", :name (partial n/get-name "12-31")},
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


(defmethod is-holiday? :ni
  [_ context]
  (holiday? context))