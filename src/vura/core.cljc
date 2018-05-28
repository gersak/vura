(ns vura.core
  (:refer-clojure :exclude [second]))

(declare date->value)

(defn round-number
  "Function returns round number that is devidable by target-number.
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
           limit (* 0.25 target-number target-number)
           compare-fn (case round-how?
                        :floor (constantly false)
                        :ceil (constantly (not (zero? diff)))
                        :up <=
                        <)]
       ((if (pos? number) + -)
        base
        (if (compare-fn limit (* diff diff)) target-number 0))))))

(def 
  ^{:dynamic true
    :doc "This variable can be used to customize weekend? function to
  return true or false for day-context. Function should accept one
  argument that is supposed to be day [1-7] and return true or false."} 
  *weekend-days* #{6 7})

(def 
  ^{:dynamic true
    :doc "This variable is supposed to be used through with-time-configuration
  macro. Specify function that calculates if given day-context is
  holiday or not. Look @ \"day-context\" Returns boolean"} 
  *holiday?* 
  (constantly false))

(def second "1" 1)
(def millisecond "0.001" (/ second 1000))
(def microsecond  "1.0E-6" (/ millisecond 1000))
(def nanosecond  "1.0E-9" (/ microsecond 1000))
(def minute  "60" 60)
(def hour  "3600" (* 60 minute))
(def day "86400" (* 24 hour))
(def week "604800" (* 7 day))

(def 
 ^{:dynamic true
   :doc "Variable that is used in function get-offset. ->local, <-local and day? funcitons are affected
   when changing value of this variable. I.E. (binding [*offset* -60] ...) would make all computations
   in that time zone."} 
  *offset* nil)

(defn get-offset 
  "Funciton returns time zone for input Date object"
  [date]
  (or *offset* (.getTimezoneOffset date)))

(def ^:no-doc month-values
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

(def ^:no-doc day-values
  {:monday 1
   :tuesday 2
   :wednesday 3
   :thursday 4
   :friday 5
   :saturday 6
   :sunday 7})

(declare value->date date->value)

(defn ^:no-doc utc-date->value [date]
  (when date
    (/ (.getTime date) 1000)))

(defn ^:no-doc value->utc-date [value]
  (when value
    (new
      #?(:clj java.util.Date
         :cljs js/Date)
      (long (* 1000 value)))))

(defn- ^:no-doc <-local
  "Given a local timestamp value function normalizes datetime to Greenwich timezone value"
  ([value] (<-local value (get-offset (value->utc-date value))))
  ([value offset]
   (- value (* offset 60))))

(defn- ^:no-doc ->local
  "Given a Greenwich timestamp value function normalizes datetime to local timezone value"
  ([value] (->local value (get-offset (value->utc-date value))))
  ([value offset]
   (+ value (* offset 60))))

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
  [value]
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

(def ^:no-doc normal-year-seconds (* 365 day))
(def ^:no-doc leap-year-seconds (* 366 day))

(defn- gregorian-year-period 
  [start-year end-year]
  (reduce
    (fn [r year]
      (+ r (if (leap-year? year)
             leap-year-seconds
             normal-year-seconds)))
    0
    (range start-year end-year)))

(def ^:no-doc unix-epoch-year 1970)
(def ^:no-doc unix-epoch-day 4)


;; Lazy sequence of all future years to come according to Gregorian calendar
(def 
  ^{:doc "Definition of future years. Lazy sequence of all future years from
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

(def 
  ^{:doc "Definition of past years. Lazy sequence of all past years
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
  (let [days (range 1 (if leap-year? 367 366))]
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

(defn ^:dynamic *find-year*
  "Finds a year for given value. Returns
  {:year x
   :seconds x}
  for given value where seconds is number of seconds of start of the year
  relative to unix-epoch-year"
  [value]
  (loop [position 0]
    (let [{:keys [year seconds]
           :as target} (if (pos? value) 
                         (nth future-years position)
                         (nth past-years position))
          diff (if (pos? value)
                 (- value seconds)
                 (- seconds value))
          step (/ diff normal-year-seconds)]
      (if (zero? diff) target
        (if (pos? diff)
          (if (< 
                diff 
                (if (leap-year? year) 
                  leap-year-seconds
                  normal-year-seconds))
            (if (pos? value) 
              target 
              (nth (if (pos? value) future-years past-years) (inc position)))
            (recur (round-number (+ position step) 1 (if (pos? diff) :ceil :floor))))
          (recur (round-number (+ position step) 1 (if (pos? diff) :ceil :floor))))))))

(defn millisecond? 
  "Returns which millisecond in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int (* 1000 (mod value 1))))

(defn second? 
  "Returns which second in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int (mod value 60)))

(defn minute? 
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int
    (mod
      (/ (round-number value minute :floor) minute)
      60)))

(defn hour? 
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int
    (mod
      (/ (round-number value hour :floor) hour)
      24)))

(defn day? 
  "Returns which day in week does input value belongs to. For example
  for date 15.02.2015 it will return number 7"
  [value]
  (int
    (inc
      (mod
        (+
         (if ((comp neg? get-offset value->date) value) 3 4)
         (/ (round-number value day :floor) day))
        7))))

(defn weekend? 
  "Returns true if value in seconds belongs to *weekend-days*"
  [value]
  (boolean (*weekend-days* (day? value))))

(defn day-in-year?
  "Returns day in year period (1 - 366)"
  [value]
  (let [{year :year year-start :seconds} (*find-year* value)
        relative-day (int (quot (- value year-start) day))]
    relative-day))


(defn week-in-year?
  "Returns which week in year does input value belongs to. For example
  for date 15.02.2015 it will return number 6"
  [value]
  (let [{year :year year-start :seconds} (*find-year* value)
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
      (int (+ (/ week-in-year week) 1)))))


(defn month? 
  "Returns which month (Gregorian) does input value belongs to. For example
  for date 15.02.2015 it will return number 2"
  [value]
  (let [{year :year year-start :seconds} (*find-year* value)
        relative-day (int (inc (quot (- value year-start) day)))]
    (get
      (if (leap-year? year)
        leap-year-day-mapping
        normal-year-day-mapping)
      relative-day)))


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
  [value]
  (let [{year :year year-start :seconds} (*find-year* value)
        relative-day (int (inc (quot (- value year-start) day)))
        leap-year? (leap-year? year)
        month (get
                (if leap-year?
                  leap-year-day-mapping
                  normal-year-day-mapping)
                relative-day)]
    (when (nil? month)
      (throw
        (ex-info "Month can't be nil"
                 {:value value
                  :leap-year leap-year?
                  :relative-day relative-day})))
    (+ 1 (- relative-day (first-day-in-month month leap-year?)))))

(defn first-day-in-month? 
  "Returns true if value in seconds belongs to first day in month."
  [value]
  (= 1 (day-in-month? value)))


(defn last-day-in-month?
  "Returns true if value in seconds belongs to last day in month."
  [value]
  (let [day-in-month (day-in-month? value)
        days-in-month' (days-in-month (month? value) (leap-year? value))]
    (= days-in-month' day-in-month)))

(defn year?
  "For given value year? returns year that value belogs to."
  [value]
  ((comp :year) (*find-year* value)))

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
                      (* millisecond' millisecond)])]
       #?(:clj (java.util.Date. (long (* 1000 (->local seconds))))
          :cljs (js/Date. (long (* 1000 (->local seconds)))))))))

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
  "Returns value of Date instance in seconds. Value is localized to offset"
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
   (defmacro with-time-configuration 
     "Utility macro to put frame on computation scope. Specify:
      
    * offset       - +/- number
    * holiday?     - (fn [day-context] true | false)
    * weekend-days - (fn [number] true | false)"
     [{:keys [offset
              holiday?
              weekend-days]
       :or {weekend-days *weekend-days*
            holiday? (fn [_] false)
            offset nil}}
      & body]
     `(binding [vura.core/*offset* ~offset
                vura.core/*weekend-days* ~weekend-days
                vura.core/*holiday?* ~holiday?]
        ~@body)))


;; TIME FRAMES

(defmulti calendar-frame 
  "Returns sequence of days for given value that are contained in that frame-type. List is consisted
  of keys:

   :value
   :day
   :week
   :month
   :day-in-month
   :weekend?
   :holiday?
   :first-day-in-month?
   :last-day-in-month?

   for Gregorian calendar. Frame types can be extened by implementing different calendar-frame functions.
   Vura supports calendar-frames for:
    * year
    * month
    * week"
  (fn [value frame-type & options] frame-type))

(defn day-context 
  "Returns day context for given value in Gregorian calendar. 

  Returnes hash-map with keys: 
    :value
    :day
    :week
    :month
    :day-in-month
    :weekend?
    :holiday?
    :first-day-in-month?
    :last-day-in-month?"
  [value]
  (binding [*find-year* (memoize *find-year*)] 
    (let [context (zipmap
                    [:value :day :week :month :year :day-in-month :weekend? 
                     :last-day-in-month? :first-day-in-month?]
                    ((juxt identity day? week-in-year? month? year? day-in-month? 
                           weekend? last-day-in-month? first-day-in-month?) value))]
      (if (fn? *holiday?*) 
        (assoc context :holiday? (*holiday?* context))
        context))))

(defn time-context 
  "Similar to day-context only for time values [millisecond, second, minute, hour]"
  [value]
  (binding [*find-year* (memoize *find-year*)] 
    (zipmap
      [:value :hour :minute :second :millisecond]
      ((juxt identity hour? minute? second? millisecond?) value))))

(defn day-time-context 
  "Composition of time-context and day-context"
  [value]
  (merge (day-context value) (time-context value)))

(defmethod calendar-frame :year [value _]
  (let [year (year? value)
        first-day (date->value (date year 1 1))]
    (map
      day-context
      (range first-day (+ first-day (days (if (leap-year? year) 367 366))) day))))

(defmethod calendar-frame "year" [value _]
  (calendar-frame value :year))

(defmethod calendar-frame :month [value _]
  (let [year (year? value)
        leap-year (leap-year? value)
        month (month? value) 
        current-day-in-month (day-in-month? value)
        current-day (midnight value)
        first-day (- 
                    current-day 
                    (days (dec current-day-in-month)))]

    (map
      day-context
      (range 
        first-day 
        (+ first-day (days (days-in-month month leap-year)))
        day))))

(defmethod calendar-frame "month" [value _]
  (calendar-frame value :month))

(defmethod calendar-frame :week [value _]
  (let [w (week-in-year? value)]
    (filter
      (comp #{w} :week)
      (calendar-frame value :month))))

(defmethod calendar-frame "week" [value _]
  (calendar-frame value :week))


(comment
  (def hr-holidays 
    #{[1 1]
      [6 1]
      [1 5]
      [22 6]
      [25 6]
      [5 8]
      [15 8]
      [8 10]
      [1 11]
      [25 12]
      [26 12]})

  (def vacation-start (date 2030 6 15 8 0 0))

  (with-time-configuration
    {:weekend-days #{5 6 7}
     :week-days #{1 2 3 4}
     :holiday? (fn [{:keys [day-in-month month]}]
                 (boolean (hr-holidays [day-in-month month])))}
    (->>
      (iterate (partial + day) (date->value vacation-start))
      (take 20)
      ;; Be carefull if this is not realized in with-time-configuration
      ;; configuration bindings won't work. Use mapv instead map
      (map day-context)
      (remove :holiday?)
      (remove :weekend?)
      count
      time))

  (->
    (date 2018 12 25 0 0 0 0)
    date->value
    day-time-context)

  (map day-context (take 20 (iterate (partial + (days 3.5)) (date->value (date 2018)))))

  (def some-day (date 2030 6 15 8 15 20 300))
  (def some-day-value (date->value some-day))
  (def other-day-value (-> some-day 
                           date->value 
                           (+ (period {:weeks 26 
                                       :days 3 
                                       :hours 82 
                                       :minutes 5000 
                                       :seconds 1000 
                                       :milliseconds -800}))))
  ;; 3848643839/2

  (def other-day (value->date other-day-value)) ;; #inst "2030-12-24T04:51:59.500-00:00"

  (def day-difference (- other-day-value some-day-value)) ;; 82900996/5

  ;; Lets round how many hours have passed with rounding strategy :ceil
  ;; that will round number up even if millisecond has passed in current hour

  (round-number (/ day-difference hour) 1 :ceil) ;; 4606N

  (with-time-configuration {:offset 0}  
    (-> other-day-value (round-number (hours 6) :ceil) value->date)) ;; #inst "2030-12-24T06:00:00.000-00:00"

  (with-time-configuration {:offset -240}  
    (-> 
      other-day-value                 ;; 3848643839/2
      (round-number (hours 2) :floor) ;; 1924315200N
      value->date                     ;; #inst "2030-12-24T03:00:00.000-00:00"
      get-offset))                    ;; -240

)
