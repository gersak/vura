(ns vura.format
  (:require
    [clojure.set]
    [goog.i18n.NumberFormat]
    [goog.i18n.NumberFormatSymbols]
    [goog.i18n.DateTimeFormat]
    [goog.i18n.DateTimeSymbols]))


(def number-symbol-mapping
  (array-map
    "hr" goog.i18n.NumberFormatSymbols_hr
    "eu" goog.i18n.NumberFormatSymbols_eu
    "en_US" goog.i18n.NumberFormatSymbols_en_US
    "ru" goog.i18n.NumberFormatSymbols_ru
    "fr" goog.i18n.NumberFormatSymbols_fr
    "si" goog.i18n.NumberFormatSymbols_si
    "ja" goog.i18n.NumberFormatSymbols_ja
    "ms" goog.i18n.NumberFormatSymbols_ms
    "in" goog.i18n.NumberFormatSymbols_in))

(defn get-number-symbols
  "Supported localizations:
  * en_US
  * eu
  * ru
  * hr
  * si
  * ja
  * zh
  * in"
  [locale]
  (get number-symbol-mapping locale goog.i18n.NumberFormatSymbols_eu))

(def date-symbol-mapping
  {"hr" goog.i18n.DateTimeSymbols_hr
   "eu" goog.i18n.DateTimeSymbols_eu
   "en_US" goog.i18n.DateTimeSymbols_en_US
   "ru" goog.i18n.DateTimeSymbols_ru
   "fr" goog.i18n.DateTimeSymbols_fr
   "si" goog.i18n.DateTimeSymbols_si
   "ja" goog.i18n.DateTimeSymbols_ja
   "in" goog.i18n.DateTimeSymbols_in
   "zh" goog.i18n.DateTimeSymbols_zh})

(defn get-date-symbols
  "Supported localizations"
  [locale]
  (get date-symbol-mapping locale goog.i18n.DateTimeSymbols_eu))

(defn get-currency-code [locale]
  (.-DEF_CURRENCY_CODE (get-number-symbols locale)))

(defn get-currency-pattern [locale]
  (.-CURRENCY_PATTERN (get-number-symbols locale)))

(def currencies
  (distinct (mapv get-currency-code (keys number-symbol-mapping))))

(def locale->currency-mapping
  (reduce (fn [r locale] (assoc r locale (get-currency-code locale))) nil (keys number-symbol-mapping)))

(def currency->locale-mapping
  (clojure.set/map-invert locale->currency-mapping))

(defn set-number-symbols [locale]
  (set! (.-NumberFormatSymbols goog.i18n) (get-number-symbols locale)))

(defn get-number-group-separator []
  (let [number-symbols goog.i18n.NumberFormatSymbols]
    (.-GROUP_SEP number-symbols)))

(defn get-number-decimal-separator []
  (let [number-symbols goog.i18n.NumberFormatSymbols]
    (.-DECIMAL_SEP number-symbols)))

(defn get-number-separators []
  (let [number-symbols goog.i18n.NumberFormatSymbols]
    {:decimal (.-DECIMAL_SEP number-symbols)
     :group (.-GROUP_SEP number-symbols)}))

(defn get-number-formatter
  ([locale] (get-number-formatter locale :currency))
  ([locale type]
   (let [number-formatters {:currency 4
                            :decimal 1
                            :scientific 2
                            :percent 3
                            :compact-short 5
                            :compact-long 6}
         symbols (get-number-symbols locale)
         currency-code (get-currency-code locale)
         pattern (get-currency-pattern locale)
         formatter (goog.i18n.NumberFormat.
                     (get number-formatters type 4)
                     currency-code)]
     formatter)))

(defn currency-formatter
  "Returns currency formater for currency code."
  [currency-code]
  (let [locale (get currency->locale-mapping  currency-code "HRK")]
    (goog.i18n.NumberFormat.
      (get-currency-pattern locale)
      currency-code)))

(defn date-formatter
  ([locale] (date-formatter locale nil))
  ([locale type]
   (let [symbols (get-date-symbols locale)
         ;; pattern "d. MMMM, yyyy. HH:mm"
         pattern (or type 10)
         formatter (goog.i18n.DateTimeFormat.
                     pattern
                     (get-date-symbols locale))]
     formatter)))

(defn format
  [formatter input]
  (.format formatter input))

(defn parse
  [formatter input]
  (.parse formatter input))
