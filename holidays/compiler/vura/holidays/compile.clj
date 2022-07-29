(ns vura.holidays.compile
  (:require
   [clojure.string :as str]
   [yaml.core :as yaml]
   [vura.timezones.db :refer [locales]]
   [vura.holidays.name :refer [get-name]]))


(defn read-locale-holidays
  [locale]
  (yaml/parse-string
   (slurp
    (format
     "https://raw.githubusercontent.com/commenthol/date-holidays/master/data/countries/%s.yaml"
     (str/upper-case (name locale))))
   :keywords false))


(defn get-holiday-days
  "Function task locale input and reads yaml file from date-holidays
  master branch. Result is parsed yaml data with path ['holidays' 'locale' 'days']"
  ([locale]
   (try
     (get-holiday-days (read-locale-holidays locale) locale)
     (catch Throwable _ nil)))
  ([yaml locale]
   (when yaml
     (get-in yaml ["holidays"
                   (str/upper-case (name locale))
                   "days"]))))



(defn get-holidays
  ([locale]
   (keys (get-holiday-days locale)))
  ([yaml locale]
   (keys (get-holiday-days yaml locale))))


(defn get-holiday-details
  ([locale holiday]
   (get-in (get-holiday-days locale) [holiday]))
  ([yaml locale holiday]
   (get-in (get-holiday-days yaml locale) [holiday])))


(defn get-holidays-identifiers
  ([locale]
   (remove nil?
           (for [val (vals (get-holiday-days locale))]
             (get-in val ["_name"]))))
  ([yaml locale]
   (remove nil?
           (for [val (vals (get-holiday-days yaml locale))]
             (get-in val ["_name"])))))


(defn get-holiday-types
  ([locale]
   (remove nil?
           (distinct
            (for [val (vals (get-holiday-days locale))]
              (get-in val ["type"])))))
  ([yaml locale]
   (remove nil?
           (distinct 
            (for [val (vals (get-holiday-days yaml locale))]
              (get-in val ["type"]))))))



(defn get-locale-types [locale]
  (try
    (set (map #(get % "type") (vals (get-holiday-days locale))))
    (catch Throwable _ #{})))


(defn get-all-types
  []
  (reduce into #{} (map get-locale-types (keys locales))))


(defn get-all-holidays
  []
  (reduce into (sorted-set) (map get-holidays (keys locales))))


(def holiday-types {nil "observance" "optional" "observence" "public" "Observance" "bank" "school"})


(comment
  (def locale :hr)
  (def locale :mk)
  (def locale :no)
  (def holiday "easter 1")
  (def holiday "12-25")
  (def holiday "05-30")
  (spit "all_holidays.edn" (with-out-str (clojure.pprint/pprint (get-all-holidays))))
  
  (keys locales)
  (keys (get-holiday-days :hr))
  (set (map #(get % "type") (vals (get-holiday-days :hr))))
  (read-holiday-names)
  ((vals (get-holiday-days :hr)))
  (slurp "https://raw.githubusercontent.com/commenthol/date-holidays/master/data/countries")
  (time (read-locale-holidays :hr))
  )
