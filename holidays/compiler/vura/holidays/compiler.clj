(ns vura.holidays.compiler
  (:require
    [clojure.string :as str]
    [yaml.core :as yaml]))


(defn read-holiday-names
  []
  (yaml/parse-string
    (slurp "https://raw.githubusercontent.com/commenthol/date-holidays/master/data/names.yaml")
    :keywords false))


(defn read-locale-holidays
  [locale]
  (yaml/parse-string
    (slurp
      (format
        "https://raw.githubusercontent.com/commenthol/date-holidays/master/data/countries/%s.yaml"
        (str/upper-case (name locale))))
    :keywords false))


(comment
  (time (read-locale-holidays :mk)))
