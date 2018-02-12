(ns vura.core
  (:refer-clojure :exclude [second format])
  #?(:cljs 
     (:require 
       [[vura.format :refer [get-date-symbols]]]))
  #?(:clj
     (:import
       [java.time Instant LocalDateTime]
       [java.time.format DateTimeFormatter]
       [java.text DateFormat SimpleDateFormat]
       [java.util TimeZone Locale])))

(defn round-number
  "Function returns round whole number that is devidable by target-number.
   Rounding strategy can be specified in round-how? options:

   :floor
   :ceil
   :up
   :down"
  ([number] (round-number number 1))
  ([number target-number] (round-number number target-number :down))
  ([number target-number round-how?]
   (assert (pos? target-number) "Target number has to be positive.")
   (case target-number
     0 0
     (let [round-how? (keyword round-how?)
           diff (rem number target-number)
           base (if (>= target-number 1)
                  (* target-number (quot number target-number))
                  (- number diff))
           limit (* 0.5 0.5 target-number target-number)
           compare-fn (case round-how?
                        :floor (constantly false)
                        :ceil (constantly (not (zero? diff)))
                        :up <=
                        <)]
       ((if (pos? number) + -)
        base
        (if (compare-fn limit (* diff diff)) target-number 0))))))

(declare date->value)

(def ^:dynamic *weekend-days* #{6 7})
(def ^:dynamic *week-days* #{1 2 3 4 5})

(def second 1)
(def millisecond (/ second 1000))
(def microsecond (/ millisecond 1000))
(def nanosecond (/ microsecond 1000))
(def minute 60)
(def hour (* 60 minute))
(def day (* 24 hour))
(def week (* 7 day))

(def ^:dynamic *offset*
  #?(:cljs (.getTimezoneOffset (js/Date.))
     :clj (.getTimezoneOffset (java.util.Date.))))

(defn get-offset [date]
  #?(:cljs (.getTimezoneOffset date)
     :clj (.getTimezoneOffset date)))

(def ^:dynamic *locale* "en")
(def ^:dynamic *format* "d. MMMM',' yyyy. HH:mm")

(defn datetime-formatter
      ([] (datetime-formatter *format* *locale*))
      ([pattern] (datetime-formatter pattern *locale*))
      ([pattern locale]
        #?(:clj  (java.text.SimpleDateFormat.
                   pattern
                   (java.util.Locale. *locale*))
           :cljs (let [symbols (get-date-symbols locale)
                       formatter (goog.i18n.DateTimeFormat.
                                   pattern
                                   (get-date-symbols *locale*))]
                      formatter))))

(defn format 
  "Function calls format function of given formatter. Formatter itself
   can be crated through datetime-formatter function."
  [formatter date]
  (.format formatter date))

(def month-values
  {:january 1
   :february 2
   :march 3
   :april 4
   :may 5
   :june 6
   :july 7
   :august 8
   :september 9
   :october 10
   :november 11
   :december 12})

(def day-values
  {:monday 1
   :tuesday 2
   :wednesday 3
   :thursday 4
   :friday 5
   :saturday 6
   :sunday 7})

(declare value->date date->value)

(defn utc-date->value [date]
  (when date
    (/ (.getTime date) 1000)))

(defn value->utc-date [value]
  (when value
    (new
      #?(:clj java.util.Date
         :cljs js/Date)
      (long (* 1000 value)))))

(defn- <-local
  "Given a local timestamp value function normalizes datetime to Greenwich timezone value"
  ([value] (<-local value (get-offset (value->utc-date value))))
  ([value offset]
   (- value (* offset 60))))

(defn- ->local
  "Given a Greenwich timestamp value function normalizes datetime to local timezone value"
  ([value] (->local value (get-offset (value->utc-date value))))
  ([value offset]
   (+ value (* offset 60))))


(defprotocol LegacyDateProtocol
  (legacy-date [this] "Returns legacy Date."))

(defrecord Date [epoch-seconds]
  LegacyDateProtocol
  (legacy-date [_]
    #?(:clj (-> epoch-seconds (* 1000) long java.util.Date.)
       :cljs (js/Date. (-> epoch-seconds (* 1000) long js/Date.))))
  Object
  (toString [this]
    (.toString (legacy-date this))))


(defn seconds 
  "Function returns value of n seconds as number."
  [n]
  (* n second))

(defn minutes 
  "Function returns value of n minutes as number."
  [n]
  (* n minute))

(defn hours 
  "Function returns value of n hours as number."
  [n]
  (* n hour))

(defn days 
  "Function returns value of n days as number."
  [n]
  (* n day))

(defn weeks 
  "Function returns value of n weeks as number."
  [n]
  (* 7 n day))

(defn midnight 
  "Function calculates value of midnight for given value. For example
   if some date value is inputed it will round-number to the begining of
   that day."
  [^long value]
  (round-number value day :floor))

(defn leap-year? 
  "Calculates if date value belongs to year that is defined as leap year."
  [year]
  (cond
    (and (= 0 (mod year 100)) (= 0 (mod year 400))) true
    (and (= 0 (mod year 100))) false
    :else (= 0 (mod year 4))))

(defn days-in-month 
  "Mapping for months. Returns how much are there for input month according
   to Gregorian calendar."
  [month leap-year?]
  (case month
    1 31
    2 (if leap-year? 29 28)
    3 31
    4 30
    5 31
    6 30
    7 31
    8 31
    9 30
    10 31
    11 30
    12 31))

(def normal-year-seconds (* 365 day))
(def leap-year-seconds (* 366 day))

(defn- gregorian-year-period 
  [start-year end-year]
  (reduce
    (fn [r year]
      (+ r (if (leap-year? year)
             leap-year-seconds
             normal-year-seconds)))
    0
    (range start-year end-year)))

(def unix-epoch-year 1970)
(def unix-epoch-day 4)


;; Lazy sequence of all future years to come according to Gregorian calendar
(def ^{:doc "Definition of future years. Lazy sequence of all future years from
             unix-epoch-year according to Gregorian calendar"}
  future-years 
  (iterate
    (fn [{:keys [year seconds]}]
      {:year (inc year)
       :seconds (if (leap-year? year)
                  (+ seconds leap-year-seconds)
                  (+ seconds normal-year-seconds))})
    {:year 1970
     :seconds 0}))

(def ^{:doc "Definition of past years. Lazy sequence of all past years
             according to Gregorian calendar from unix-epoch-year"} 
  past-years 
  (iterate
    (fn [{:keys [year seconds]}]
      {:year (dec year)
       :seconds (if (leap-year? (dec year))
                  (- seconds leap-year-seconds)
                  (- seconds normal-year-seconds))})
    {:year 1970
     :seconds 0}))

(defn year-day-mapping
  "Calculates day mapping. Keys are days values are months. Function will return map
   where keys are days in year from 1-36[56] and month as value for that day."
  [leap-year?]
  (let [days (range 1 366)]
    (loop [d days
           m 1
           r {}]
      (if (empty? d) r
        (let [days-count (days-in-month m leap-year?)
              month-days (take days-count d)]
          (recur
            (drop days-count d)
            (inc m)
            (merge
              r
              (zipmap month-days (repeat m)))))))))

(def ^{:doc "Year day to month mapping calculated during compilation with year-day-mapping function."} 
  normal-year-day-mapping
  (year-day-mapping false))

(def ^{:doc "Year day to month mapping calculated during compilation with year-day-mapping function for leap year."}
  leap-year-day-mapping
  (year-day-mapping true))

(defn- find-year
  "Finds a year for given value. Returns
  {:year x
   :seconds x}
  for given value where seconds is number of seconds of start of the year
  relative to unix-epoch-year"
  [value]
  (if (pos? value)
    (last (take-while #(<= (:seconds %) value) future-years))
    (first (drop-while #(> (:seconds %) value) past-years))))

(defn year?
  "For given value year? returns year that value belogs to."
  [value]
  (if (pos? value)
    ((comp :year) (find-year value))
    ((comp :year) (find-year value))))

(defn day-in-year?
  "Returns day in year period (1 - 366)"
  [value]
  (let [{year :year year-start :seconds} (find-year value)
        relative-day (quot (- value year-start) day)]
    relative-day))



(defn first-day-in-month
  "Returns first day for given month in range of days 1-366 for leap-year?"
  ([month] (first-day-in-month month false))
  ([month leap-year?]
   (apply min
     (keep
       (fn [[d m]] (when (= m month) d))
       (if leap-year?
         leap-year-day-mapping
         normal-year-day-mapping)))))


(defn day-in-month? 
  "Returns which day (Gregorian) in month input value belongs to. For example
   for date 15.02.2015 it will return number 15"
  [^long value]
  (let [{year :year year-start :seconds} (find-year value)
        relative-day (inc (quot (- value year-start) day))
        leap-year? (leap-year? year)
        month (get
                (if leap-year?
                  leap-year-day-mapping
                  normal-year-day-mapping)
                relative-day)]
    (+ 1 (- relative-day (first-day-in-month month leap-year?)))))


(defn month? 
  "Returns which month (Gregorian) does input value belongs to. For example
   for date 15.02.2015 it will return number 2"
  [^long value]
  (let [{year :year year-start :seconds} (find-year value)
        relative-day (inc (quot (- value year-start) day))]
    (get
      (if (leap-year? year)
        leap-year-day-mapping
        normal-year-day-mapping)
      relative-day)))

(defn day? 
  "Returns which day in week does input value belongs to. For example
   for date 15.02.2015 it will return number 7"
  [^long value]
  (inc
    (mod
      (+
       (if (neg? *offset*) 3 4)
       (/ (round-number value day :floor) day))
      7)))

(defn hour? 
  "Returns which hour in day does input value belongs to. For example
   for date 15.02.2015 it will return number 0"
  [^long value]
  (int
    (mod
      (/ (round-number value hour :floor) hour)
      24)))

(defn minute? 
  "Returns which hour in day does input value belongs to. For example
   for date 15.02.2015 it will return number 0"
  [^long value]
  (int
    (mod
      (/ (round-number value minute :floor) minute)
      60)))

(defn second? 
  "Returns which second in day does input value belongs to. For example
   for date 15.02.2015 it will return number 0"
  [^long value]
  (int
    (mod value 60)))

(defn millisecond? 
  "Returns which millisecond in day does input value belongs to. For example
   for date 15.02.2015 it will return number 0"
  [^long value]
  (int
    (*
      1000
      (mod
        value
        1))))

(defn week-in-year?
  "Returns which week in year does input value belongs to. For example
   for date 15.02.2015 it will return number 6"
  [value]
  (let [{year :year year-start :seconds} (find-year value)
        value (midnight value)
        first-monday (first
                       (filter
                         #(#{1} (day? %))
                         (iterate (partial + day) year-start)))
        time-difference (- value first-monday)
        week-in-year (round-number time-difference week :floor)]
    ;; If year startsh with Thursday, Friday, or any above
    (if (neg? time-difference)
      0
      (+ (/ week-in-year week) 1))))


(defmulti calendar-frame 
  "Returns sequence of days for given value that are contained in that month. List is consisted
of keys:

   value
   day
   week
   month
   day-in-month
   
   for Gregorian calendar."
  (fn [value frame-type & options] frame-type))

(defmethod calendar-frame :month [value _]
  (let [year (year? value)
        month (month? value) 
        leap-year (leap-year? year)
        current-day-in-month (day-in-month? value)
        current-day (midnight value)
        first-day (- 
                    current-day 
                    (days (dec current-day-in-month)))]

    (map
      #(zipmap
         [:value :day :week :month :day-in-month]
         ((juxt identity day? week-in-year? month? day-in-month?) %))
      (range 
        first-day 
        (+ first-day (days (days-in-month month leap-year)))
        day))))

(defmethod calendar-frame "month" [value _]
  (calendar-frame value :month))

(defmethod calendar-frame :week [value _]
  (let [w (week-in-year? value)]
    ;; TODO fix-this to support border weeks
    (filter
      (comp #{w} :week)
      (calendar-frame value :month))))

(defmethod calendar-frame "week" [value _]
  (calendar-frame value :week))


(defn date
  "Constructs new Date object.
  Months: 1-12
  Days: 1-7 (1 is Monday)"
  ([] #?(:cljs (js/Date.)
         :clj (java.util.Date.)))
  ([year] (date year 1 ))
  ([year month] (date year month 1))
  ([year month day] (date year month day 0))
  ([year month day hour] (date year month day hour 0))
  ([year month day hour minute] (date year month day hour minute 0))
  ([year month day hour minute second] (date year month day hour minute second 0))
  ([year month day' hour' minute' second' millisecond']
   (let [leap-year? (leap-year? year)]
     (assert (and
               (< 0 month)
               (> 13 month)) "Month should be in range 1-12")
     (assert (not (or (neg? day') (zero? day'))) "There is no 0 or negative day in month.")
     (assert (and
               (<= 0 hour')
               (> 24 hour')) "Hour should be in range 0-23")
     (assert (and
               (<= 0 minute')
               (> 60 minute')) "Minute should be in range 0-59")
     (assert (and
               (<= 0 second')
               (> 60 second')) "Second should be in range 0-59")
     (assert (and
               (< 0 day')
               (<= day' (days-in-month month leap-year?)))
       (str  "Day " day' " is out of range: 1-" (days-in-month month leap-year?) " for year " year))
     (let [years-period (if (>= year unix-epoch-year)
                          (gregorian-year-period unix-epoch-year year)
                          (* -1 (gregorian-year-period year unix-epoch-year)))
           months-period (reduce + 0
                           (map
                             #(* day (days-in-month % leap-year?))
                             (range 1 month)))
           seconds (reduce
                     +
                     0
                     [years-period
                      months-period
                      (* day (dec day'))
                      (* hour' hour)
                      (* minute' minute)
                      (* second' second)
                      (* millisecond' millisecond)
                      (* minute *offset*)])]
       #?(:clj (java.util.Date. (long (* 1000 seconds)))
          :cljs (js/Date. (long (* 1000 seconds))))))))

(defn period
  "Returns number of seconds of all input arguments added together"
  ([{:keys [weeks days hours seconds minutes milliseconds]
     :or {weeks 0
          days 0
          hours 0
          seconds 0
          minutes 0
          milliseconds 0}}]
   (reduce + 0
           [(* 7 day weeks)
            (* day days)
            (* hour hours)
            (* seconds second)
            (* minutes minute)
            (* milliseconds millisecond)])))

(defn value->date
  "Returns Date instance for value in seconds for current. Function first
  transforms value to local *offset* value."
  ([value]
   (new
     #?(:clj java.util.Date
        :cljs js/Date)
     (long (* 1000 (->local value))))))

(defn date->value
  "Returns value of Date instance in seconds. Value is localized to *offset*"
  ([t]
   (when t
     (<-local
       (/ (.getTime t) 1000)))))



(defn intervals
  "Given sequence of timestamps (Date) values returns period values between each timestamp
   value in milliseconds"
  [& timestamps]
  (assert 
    (every? (partial instance? #?(:clj java.util.Date :cljs js/Date)) timestamps) 
    (str "Wrong input value."))
  (let [timestamps' (map date->value timestamps)
        t1 (rest timestamps')
        t2 (butlast timestamps')]
    (map (partial * 1000) (map - t1 t2))))

(defn interval
  "Returns period of time value in milliseconds between start and end. Input values
   are supposed to be Date."
  [start end]
  (first (intervals start end)))


#?(:clj 
   (defmacro with-time-configuration [{:keys [offset 
                                              weekend-days
                                              week-days
                                              locale
                                              format]
                                       :or {offset *offset*
                                            weekend-days *weekend-days*
                                            week-days *weekend-days*
                                            locale *locale*
                                            format *format*}}
                                      & body]
     `(binding [*offset* offset
                *weekend-days* weekend-days
                *week-days* week-days
                *locale* locale
                *format* format]
        ~@body)))


