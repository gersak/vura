;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.md
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"01-01" {:name (partial n/get-name "01-01")},
 "05-09" {:name {:ro "Ziua Victoriei", :en "Victory Day"}},
 "orthodox 8" {:name {:ro "Paştele Blăjinilor", :en "Memorial Day"}},
 "08-27" {:name (partial n/get-name "Independence Day")},
 "08-31" {:name {:ro "Limba noastră", :en "National Language Day"}},
 "03-08" {:name (partial n/get-name "03-08")},
 "05-01" {:name (partial n/get-name "05-01")},
 "1st saturday in October P2D"
 {"type" "observance",
  "active" [{"from" 2013}],
  :name {:ro "Ziua vinului", :en "Wine Day"}},
 "orthodox" {:name (partial n/get-name "easter")},
 "julian 12-25 P2D"
 {"_name" "julian 12-25", :name {:ro "Craciun pe Rit Vechi"}},
 "orthodox 1" {:name (partial n/get-name "easter 1")},
 "12-25" {"_name" "12-25", :name {:ro "Craciun pe stil Nou"}}}
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


(defmethod is-holiday? :md
  [_ context]
  (holiday? context))