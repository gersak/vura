;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.rs
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"10 Dhu al-Hijjah"
 {"type" "optional",
  "note" "Muslim believers",
  :name (partial n/get-name "10 Dhu al-Hijjah")},
 "05-02 if sunday then next monday"
 {:name (partial n/get-name "05-01")},
 "04-22"
 {"type" "observance",
  :name
  {:sr "Дан сећања на жртве холокауста",
   :en "Holocaust Remembrance Day"}},
 "05-09"
 {"type" "observance", :name {:sr "Дан победе", :en "Victory Day"}},
 "02-16 if sunday then next monday"
 {:name {:sr "Дан државности Србије", :en "Statehood Day"}},
 "easter"
 {"type" "optional",
  "note" "Catholic believers",
  :name (partial n/get-name "easter")},
 "01-02 if sunday then next monday"
 {:name (partial n/get-name "01-01")},
 "02-15 if sunday then next tuesday"
 {:name {:sr "Дан државности Србије", :en "Statehood Day"}},
 "easter -2"
 {"type" "optional",
  "note" "Catholic believers",
  :name (partial n/get-name "easter -2")},
 "1 Shawwal"
 {"type" "optional",
  "note" "Muslim believers",
  :name (partial n/get-name "1 Shawwal")},
 "05-01 if sunday then next tuesday"
 {:name (partial n/get-name "05-01")},
 "easter 1"
 {"type" "optional",
  "note" "Catholic believers",
  :name (partial n/get-name "easter 1")},
 "orthodox -2" {:name (partial n/get-name "orthodox -2")},
 "julian 12-25" {:name (partial n/get-name "julian 12-25")},
 "julian 01-14"
 {"type" "observance", :name {:sr "Свети Сава", :en "Saint Sava Day"}},
 "11-11 if sunday then next monday"
 {:name {:sr "Дан примирја", :en "Armistice Day"}},
 "01-01 if sunday then next tuesday"
 {:name (partial n/get-name "01-01")},
 "julian 06-15"
 {"type" "observance", :name {:sr "Видовдан", :en "Saint Vitus Day"}},
 "orthodox" {:name (partial n/get-name "orthodox")},
 "10 Tishrei"
 {"type" "optional",
  "note" "Jewish believers",
  :name (partial n/get-name "10 Tishrei")},
 "orthodox 1" {:name (partial n/get-name "orthodox 1")},
 "10-21"
 {"type" "observance",
  :name
  {:sr "Дан сећања на српске жртве у Другом светском рату",
   :en "World War II Serbian Victims Remembrance Day"}},
 "12-25"
 {"type" "optional",
  "note" "Catholic believers",
  :name (partial n/get-name "12-25")}}
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


(defmethod is-holiday? :rs
  [_ context]
  (holiday? context))