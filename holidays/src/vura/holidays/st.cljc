;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.st
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"01-01 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "02-03 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true,
  :name {:en "Martyrs' Day", :pt "Dia dos Mártires"}},
 "05-01 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "07-12 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "Independence Day")},
 "09-06 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true,
  :name {:en "Armed Forces Day", :pt "Dia das Forças Armadas"}},
 "09-30 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true,
  :name {:en "Agricultural Reform Day", :pt "Dia da Reforma Agrária"}},
 "12-21 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true,
  :name {:en "São Tomé Day", :pt "Dia de São Tomé"}},
 "12-25 and if Saturday then previous Friday if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "12-25")}}
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


(defmethod is-holiday? :st
  [_ context]
  (holiday? context))