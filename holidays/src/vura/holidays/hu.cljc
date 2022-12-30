;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.hu
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"10-23"
 {"type" "public",
  :name {:hu "1956-os forradalom", :en "National Day"}},
 "05-21"
 {"type" "observance",
  :name {:en "National Defense Day", :hu "Honvédelmi nap"}},
 "1st sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "01-01" {"type" "public", :name (partial n/get-name "01-01")},
 "04-16"
 {"type" "observance",
  :name
  {:en "Memorial Day for the Victims of the Holocaust",
   :hu "A holokauszt áldozatainak emléknapja"}},
 "02-01"
 {"type" "observance",
  :name
  {:hu "A köztársaság emléknapja",
   :en "Memorial Day of the Republic"}},
 "06-04"
 {"type" "observance",
  :name
  {:en "Day of National Unity", :hu "A nemzeti összetartozás napja"}},
 "11-01" {"type" "public", :name (partial n/get-name "11-01")},
 "02-25"
 {"type" "observance",
  :name
  {:hu "A kommunista diktatúrák áldozatainak emléknapja",
   :en "Memorial Day for the Victims of the Communist Dictatorships"}},
 "easter 50" {"type" "public", :name (partial n/get-name "easter 50")},
 "easter" {"type" "public", :name (partial n/get-name "easter")},
 "12-24" {"type" "optional", :name (partial n/get-name "12-24")},
 "10-06"
 {"type" "observance",
  :name
  {:en "Memorial Day for the Martyrs of Arad",
   :hu "Az aradi vértanúk emléknapja"}},
 "12-06" {"type" "observance", :name (partial n/get-name "12-06")},
 "08-20"
 {"type" "public",
  :name {:hu "Szent István ünnepe", :en "Saint Stephen's Day"}},
 "12-26" {"type" "public", :name (partial n/get-name "12-26")},
 "easter 49" {"type" "public", :name (partial n/get-name "easter 49")},
 "easter 1" {"type" "public", :name (partial n/get-name "easter 1")},
 "03-08" {"type" "observance", :name (partial n/get-name "03-08")},
 "05-01" {"type" "public", :name (partial n/get-name "05-01")},
 "12-31" {"type" "observance", :name (partial n/get-name "12-31")},
 "03-15"
 {"type" "public",
  :name {:hu "1948-as forradalom", :en "National Day"}},
 "12-25" {"type" "public", :name (partial n/get-name "12-25")},
 "06-19"
 {"type" "observance",
  :name
  {:en "Day of the Independent Hungary",
   :hu "A független Magyarország napja"}}}
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


(defmethod is-holiday? :hu
  [_ context]
  (holiday? context))