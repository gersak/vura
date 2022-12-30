;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.sm
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"12-08" {:name (partial n/get-name "12-08")},
 "08-15" {:name (partial n/get-name "08-15")},
 "easter 60" {:name (partial n/get-name "easter 60")},
 "01-01" {:name (partial n/get-name "01-01")},
 "11-01" {"_name" "11-01", :name {:it "Tutti i Santi"}},
 "03-25"
 {:name
  {:it "Anniversario dell'Arengo", :en "Anniversary of the Arengo"}},
 "easter" {:name (partial n/get-name "easter")},
 "10-01"
 {:name {:it "Cerimonia di investitura dei Capitani Reggenti"}},
 "2nd sunday in May"
 {"type" "observance", :name (partial n/get-name "Mothers Day")},
 "12-26" {:name (partial n/get-name "12-26")},
 "easter 1" {:name (partial n/get-name "easter 1")},
 "02-05"
 {:name {:it "Festa di Sant’Agata", :en "Feast of Saint Agatha"}},
 "11-02"
 {:name
  {:it "Commemorazione dei defunti",
   :en "Commemoration of the deceased"}},
 "04-01"
 {:name
  {:it "Cerimonia di investitura dei Capitani Reggenti",
   :en "Inauguration Ceremony"}},
 "05-01" {:name (partial n/get-name "05-01")},
 "09-03"
 {:name
  {:it "Festa di San Marino e di Fondazione della Repubblica",
   :en "The Feast of San Marino and the Republic"}},
 "01-06" {"_name" "01-06", :name {:it "Epifania"}},
 "12-31" {"type" "optional", :name (partial n/get-name "12-31")},
 "07-28"
 {:name
  {:it "Anniversario della caduta del Fascismo e Festa della Libertà",
   :en "Liberation from Fascism"}},
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


(defmethod is-holiday? :sm
  [_ context]
  (holiday? context))