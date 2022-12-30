;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.my
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"2030-10-25" {:name (partial n/get-name "Deepavali")},
 "2026-05-31" {:name (partial n/get-name "Vesak")},
 "substitutes 09-16 if Sunday then next Monday"
 {"substitute" true, :name {:en "Malaysia Day", :ms "Hari Malaysia"}},
 "10 Dhu al-Hijjah"
 {"disable" ["2022-07-09"],
  "enable" ["2022-07-10"],
  :name (partial n/get-name "10 Dhu al-Hijjah")},
 "2015-06-02" {:name (partial n/get-name "Vesak")},
 "2020-05-07" {:name (partial n/get-name "Vesak")},
 "2017-10-18" {:name (partial n/get-name "Deepavali")},
 "substitutes 05-01 if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "05-01")},
 "01-01" {:name (partial n/get-name "01-01")},
 "2023-11-12" {:name (partial n/get-name "Deepavali")},
 "substitutes 12-25 if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "12-25")},
 "17 Ramadan"
 {"disable" ["2022-04-18"],
  "enable" ["2022-04-19"],
  :name (partial n/get-name "17 Ramadan")},
 "2023-05-04" {:name (partial n/get-name "Vesak")},
 "2012-05-06" {:name (partial n/get-name "Vesak")},
 "2024-10-31" {:name (partial n/get-name "Deepavali")},
 "2022-10-24" {:name (partial n/get-name "Deepavali")},
 "2018-11-06" {:name (partial n/get-name "Deepavali")},
 "substitutes chinese 01-0-01 if Sunday then next Monday"
 {"substitute" true,
  :name {:en "Chinese New Year", :ms "Tahun Baru Cina"}},
 "2028-11-15" {:name (partial n/get-name "Deepavali")},
 "2027-10-28" {:name (partial n/get-name "Deepavali")},
 "09-16" {:name {:en "Malaysia Day", :ms "Hari Malaysia"}},
 "2025-05-12" {:name (partial n/get-name "Vesak")},
 "2016-05-22" {:name (partial n/get-name "Vesak")},
 "2026-11-09" {:name (partial n/get-name "Deepavali")},
 "1 Muharram" {:name (partial n/get-name "1 Muharram")},
 "chinese 01-0-01"
 {:name {:en "Chinese New Year", :ms "Tahun Baru Cina"}},
 "substitutes 01-01 if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "01-01")},
 "2018-05-29" {:name (partial n/get-name "Vesak")},
 "2029-11-05" {:name (partial n/get-name "Deepavali")},
 "1st Monday in June"
 {:name
  {:en "Yang di-Pertuan Agong's Birthday",
   :ms "Hari Keputeraan Yang di-Pertuan Agong"}},
 "substitutes chinese 01-0-02 if Sunday then next Monday"
 {"substitute" true,
  :name {:en "Chinese New Year", :ms "Tahun Baru Cina"}},
 "substitutes 08-31 if Sunday then next Monday"
 {"substitute" true, :name (partial n/get-name "Independence Day")},
 "1 Shawwal"
 {"disable" ["2022-05-02"],
  "enable" ["2022-05-03"],
  :name (partial n/get-name "1 Shawwal")},
 "2019-10-27" {:name (partial n/get-name "Deepavali")},
 "2022-05-16" {"substitute" true, :name (partial n/get-name "Vesak")},
 "2014-05-15" {:name (partial n/get-name "Vesak")},
 "2017-05-10" {:name (partial n/get-name "Vesak")},
 "2022-05-15" {:name (partial n/get-name "Vesak")},
 "2020-11-14" {:name (partial n/get-name "Deepavali")},
 "2 Shawwal"
 {"disable" ["2022-05-03"],
  "enable" ["2022-05-04"],
  :name (partial n/get-name "1 Shawwal")},
 "2011-05-17" {:name (partial n/get-name "Vesak")},
 "chinese 01-0-02"
 {:name {:en "Chinese New Year", :ms "Tahun Baru Cina"}},
 "2019-10-28"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "08-31" {:name (partial n/get-name "Independence Day")},
 "05-01" {:name (partial n/get-name "05-01")},
 "2021-05-26" {:name (partial n/get-name "Vesak")},
 "2025-10-20" {:name (partial n/get-name "Deepavali")},
 "2016-10-29" {:name (partial n/get-name "Deepavali")},
 "2023-11-13"
 {"substitute" true, :name (partial n/get-name "Deepavali")},
 "2010-05-28" {:name (partial n/get-name "Vesak")},
 "2019-05-19" {:name (partial n/get-name "Vesak")},
 "2021-11-04" {:name (partial n/get-name "Deepavali")},
 "12 Rabi al-awwal"
 {"disable" ["2022-10-08"],
  "enable" ["2022-10-09"],
  :name (partial n/get-name "12 Rabi al-awwal")},
 "12-25" {:name (partial n/get-name "12-25")},
 "2013-05-25" {:name (partial n/get-name "Vesak")},
 "2024-05-22" {:name (partial n/get-name "Vesak")}}
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


(defmethod is-holiday? :my
  [_ context]
  (holiday? context))