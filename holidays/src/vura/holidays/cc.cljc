;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.cc
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"1 Shawwal and if sunday then next monday"
 {"substitute" true,
  "_name" "1 Shawwal",
  :name {:en "Hari Raya Puasa"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "12-25 and if sunday then next tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "01-26" {:name {:en "Australia Day"}},
 "03-20" {:name {:en "Labour Day"}},
 "chinese 01-0-01 and if sunday then next tuesday if saturday then next monday"
 {"substitute" true, :name {:en "Chinese New Year"}},
 "1 Muharram and if sunday then next monday"
 {"substitute" true, :name (partial n/get-name "1 Muharram")},
 "12-26 and if sunday then next monday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "12 Rabi al-awwal and if sunday then next monday"
 {"substitute" true,
  "_name" "12 Rabi al-awwal",
  :name {:en "Hari Maulaud Nabi"}},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "10 Dhu al-Hijjah and if sunday then next monday"
 {"substitute" true,
  "_name" "10 Dhu al-Hijjah",
  :name {:en "Hari Raya Haji"}},
 "04-06" {:name {:en "Self Determination Day"}},
 "04-25" {:name {:en "Anzac Day"}},
 "chinese 01-0-02 and if sunday then next tuesday if saturday then next monday"
 {"substitute" true, :name {:en "Chinese New Year (2nd Day)"}}}
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


(defmethod is-holiday? :cc
  [_ context]
  (holiday? context))