;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.za
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-16" {:name {:en "Day of Reconciliation"}},
 "08-09" {:name {:en "National Women's Day"}},
 "04-27" {:name {:en "Freedom Day"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "substitutes 12-16 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "07-18" {"type" "observance", :name {:en "Nelson Mandela Day"}},
 "substitutes 06-16 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "3rd sunday in June"
 {"type" "observance", :name (partial n/get-name "Fathers Day")},
 "substitutes 05-01 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "substitutes 01-01 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "substitutes 04-27 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "substitutes 08-09 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "substitutes 03-21 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "06-16" {:name {:en "Youth Day"}},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "12-26" {"_name" "12-26", :name {:en "Day of Goodwill"}},
 "easter 1" {"_name" "easter 1", :name {:en "Family Day"}},
 "05-01" {"_name" "05-01", :name {:en "Workers' Day"}},
 "09-24" {:name {:en "Heritage Day"}},
 "03-21" {:name {:en "Human Rights Day"}},
 "substitutes 09-24 if sunday then next monday"
 {:name {:en "Public Holiday"}},
 "substitutes 12-26 if sunday then next monday"
 {:name {:en "Public Holiday"}},
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


(defmethod is-holiday? :za
  [_ context]
  (holiday? context))