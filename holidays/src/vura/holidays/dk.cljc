;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.dk
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"easter -48" {"type" "observance", :name {:da "Fastelavn"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "easter 39" {:name (partial n/get-name "easter 39")},
 "easter 50" {:name (partial n/get-name "easter 50")},
 "easter" {:name (partial n/get-name "easter")},
 "12-24"
 {"type" "observance",
  "note" "Shops are closed",
  :name (partial n/get-name "12-24")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "06-05"
 {"type" "observance",
  "note" "Shops are closed",
  :name (partial n/get-name "Constitution Day")},
 "12-26" {:name (partial n/get-name "12-26")},
 "easter 49" {:name (partial n/get-name "easter 49")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "05-01"
 {"type" "observance",
  "note" "Full holiday for blue collar workers",
  :name (partial n/get-name "05-01")},
 "easter -3" {:name (partial n/get-name "easter -3")},
 "easter 26"
 {:name
  {:da "Store Bededag", :en "Prayer Day", :de "Buß- und Bettag"}},
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


(defmethod is-holiday? :dk
  [_ context]
  (holiday? context))