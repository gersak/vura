;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.vc
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"01-01 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "12-25 and if Sunday then next Tuesday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "easter 50" {:name (partial n/get-name "easter 50")},
 "1st Monday in July"
 {"disable" ["2021-06-05"],
  "enable" ["2021-09-06"],
  :name {:en "Carnival Monday"}},
 "easter" {"type" "observance", :name (partial n/get-name "easter")},
 "12-26 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "12-26")},
 "10-27 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "Independence Day")},
 "08-01 and if Sunday then next Monday"
 {"substitute" true, :name {:en "Emancipation Day"}},
 "easter -2" {:name (partial n/get-name "easter -2")},
 "03-14 and if Sunday then next Monday"
 {"substitute" true, :name {:en "National Hero′s Day"}},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "05-01 and if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "Tuesday after 1st Monday in July"
 {"disable" ["2021-06-06"],
  "enable" ["2021-09-07"],
  :name {:en "Carnival Tuesday"}}}
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


(defmethod is-holiday? :vc
  [_ context]
  (holiday? context))