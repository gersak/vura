;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.gh
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"03-06 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "Independence Day")},
 "05-01 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "12-26 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "1 Shawwal and if Saturday, Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "1 Shawwal")},
 "08-04 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name {:en "Founders Day"}},
 "01-01 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "01-07 since 2019" {:name (partial n/get-name "Constitution Day")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "09-21 and if Saturday, Sunday then next Monday"
 {"substitute" true, :name {:en "Kwame Nkrumah Memorial Day"}},
 "12-25 and if Saturday then next Monday if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "10 Dhu al-Hijjah and if Saturday, Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "10 Dhu al-Hijjah")},
 "1st Friday in December since 1988" {:name {:en "Farmers Day"}}}
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


(defmethod is-holiday? :gh
  [_ context]
  (holiday? context))