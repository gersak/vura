(ns vura.core
  #?(:cljs 
     (:require-macros [vura.core :refer [with-time-configuration]]))
  (:require
   [vura.timezones.db
    :refer [get-timezone get-rule]]
   #?(:cljs [goog.object]))
  (:refer-clojure :exclude [second]))

(def get-locale-timezone vura.timezones.db/get-locale-timezone)
(def get-timezone-locale vura.timezones.db/get-timezone-locale)

(declare utc-date hours minutes calendar-frame month?)

(defn round-number
  "Function returns round number that is devidable by target-number.
  Rounding strategy can be specified in round-how? options:

   :floor
   :ceil
   :up
   :down
  
  Rounding number strategy is symetric to 0. This means that :ceil will round
  negative numbers to lower target-number. I.E (round-number -9.5 1 :ceil) would return -10.
  Rounding happens in absolute domain and sign is inserted afterwards.
   
  Negative target-numbers are not supported. Can't reason about that yet. Maybe for imaginary numbers?"
  ([number] (round-number number 1))
  ([number target-number] (round-number number target-number :down))
  ([number target-number round-how] (round-number number target-number round-how false))
  ([number target-number round-how? symetric?]
   {:pre [(or
           (zero? target-number)
           (pos? target-number))]}
   (letfn [(normalize-number [x]
             #?(:clj (if (some #(% target-number) [float? double?])
                       (bigdec x)
                       x)
                :cljs x))]
     (case target-number
       0 0
       ;; First normalize numbers to floating point or integer
       (let [number (normalize-number number)
             target-number (normalize-number target-number)
             round-how? (keyword round-how?)
             diff (rem number target-number)
             base (if (>= target-number 1)
                    (* target-number (quot number target-number))
                    (- number diff))
             limit (* 0.25 target-number target-number)
             compare-fn (if (and (not symetric?) (neg? number))
                          (case round-how?
                            :floor (constantly (not (zero? diff)))
                            :ceil (constantly false)
                            :up <
                            <=)
                          (case round-how?
                            :floor (constantly false)
                            :ceil (constantly (not (zero? diff)))
                            :up <=
                            <))
             result ((if (pos? number) + -)
                     base
                     (if (compare-fn limit (* diff diff)) target-number 0))]
         #?(:clj
            (if (some decimal? [target-number number])
              (double result)
              result)
            :cljs result))))))

(def
  ^{:dynamic true
    :doc "Influences time->value and value->time functions as well as all '?' functions.
         Valid values: 

         :vura.core/gregorian
         :vura.core/julian"}
  *calendar* ::gregorian)

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
  nil)

(def millisecond "1" 1)
(def microsecond  "1.0E-3" (/ millisecond 1000))
(def nanosecond  "1.0E-6" (/ microsecond 1000))
(def second "1000" (* 1000 millisecond))
(def minute  "60" (* 60 second))
(def hour  "3600" (* 60 minute))
(def day "86400" (* 24 hour))
(def week "604800" (* 7 day))

(defn ^:no-doc
  system-timezone []
  #?(:clj (.. (java.util.TimeZone/getDefault) (getID))
     :cljs (goog.object/get (.resolvedOptions (js/Intl.DateTimeFormat)) "timeZone")))

(def
  ^{:dynamic true
    :doc "Variable that is used in function get-dst-offset. ->local, <-local and day? funcitons are affected
   when changing value of this variable. I.E. (binding [*timezone* :hr] ...) would make all computations
   in that time zone (offset)."}
  *timezone*
  (system-timezone))

(def
  ^{:dynamic true
    :doc "Variable that is used in function get-dst-offset. ->local, <-local and day? funcitons are affected
   when changing value of this variable. I.E. (binding [*offset* (hours -2)] ...) would make all computations
   in that offset from UTC."}
  *offset* nil)

(defn unknown-holiday [_] false)

(defmacro with-time-configuration
  "Utility macro to put context frame on computation scope. Specify:

  :holiday?     - (fn [day-context] true | false)
  :timezone     - timezone-name
  :weekend-days - (fn [number] true | false)
  :calendar     - :vura.core/gregorian :vura.core/julian"
  [{:keys [timezone
           holiday?
           offset
           calendar
           weekend-days]
    :or {weekend-days vura.core/*weekend-days*
         timezone (vura.core/system-timezone)
         holiday? *holiday?*
         calendar :vura.core/gregorian
         offset nil}}
   & body]
  ;;`(let [] ~@body)
  `(binding [vura.core/*timezone* ~timezone
             vura.core/*offset* ~offset
             vura.core/*weekend-days* ~weekend-days
             vura.core/*holiday?* ~holiday?
             vura.core/*calendar* ~calendar]
     ~@body))

;; TODO - enable this to conform with history rules not just current one
;; This is + on lib size and speed. I've compiled TZ with history and it
;; is about 0.5 Mb in size. That is alot for CLJS not that much for CLJ
;; For now SUPPORT ONLY LATEST(CURRENT) TZ DB (72 KB)


(defn- get-timezone-attribute [timezone attribute]
  (get (get-timezone timezone) attribute))

(defn- get-timezone-offset [timezone]
  (get-timezone-attribute timezone :offset))

(defn- get-timezone-rule [timezone]
  (when-let [rule (get-timezone-attribute timezone :rule)]
    (when-let [rule-name (case rule
                           "-" nil
                           rule)]
      (get-rule rule-name))))

(defn value->utc-date
  "Casts value to UTC value of Date object"
  [value]
  #?(:cljs (js/Date. value)
     :clj (java.util.Date. value)))

(defn date->utc-value [date]
  "Returns number of milliseconds from UNIX epoch"
  (when-let [t (.getTime date)]
    t
    #_(case *calendar*
        ::gregorian t
      ;; Add 13 days becaouse gregorian is refrence calendar
      ;; So if *calendar* = ::julian, add 13 days to get real value
        ::julian (+ t (days 13)))))

(defn ^:no-doc until-value
  [{:keys [year month day floating-day] :as until
    :or {day 1
         month 1}}]
  ;; Find calendar frame for this month
  (when until
    (let [days-mapping
          {"Mon" 1
           "Tue" 2
           "Wed" 3
           "Thu" 4
           "Fri" 5
           "Sat" 6
           "Sun" 7}
          {:keys [hour minute]
           :or {hour 0 minute 0}} (:time until)
          frame (->
                 (utc-date year month)
                 date->utc-value
                 (calendar-frame :month))]
      (if floating-day
        (if (clojure.string/starts-with? floating-day "last")
          ;; Floating day is last something
          (let [day' (days-mapping (subs floating-day 4))
                value (:value
                       (last
                        (filter
                         #(= day' (:day %))
                         frame)))]
            value
            (+ value (minutes minute) (hours hour)))
          ;; Floating day is higher than
          (let [day' (days-mapping (subs floating-day 0 3))
                operator (case (subs floating-day 3 5)
                           ">=" >=)
                day-in-month' #?(:clj (Integer/parseInt (subs floating-day 5))
                                 :cljs (js/parseInt (subs floating-day 5)))
                value (:value
                       (first
                        (filter
                         #(and
                           (= day' (:day %))
                           (operator (:day-in-month %) day-in-month'))
                         frame)))]
            (+ value (hours hour) (minutes minute))))
        (date->utc-value (utc-date year month day hour minute))))))

(defn get-dst-offset
  "Returns DST offset for UTC input value"
  [value]
  (letfn [(utc-rule? [rule] (if-not (= "s" (-> rule :time :time-suffix)) true false))]
    (if (nil? *timezone*)
      (throw (ex-info "No timezone defined" {:value value}))
      (if-let [{dst-rule :daylight-savings
                s-rule :standard
                ;; TODO - if this is going to expand to other rules in history
                ;;  than get-timezone-rule should be function of two arguments
                ;; *timezone* and value
                ;; THIS IS ONLY PLACE REQUIRED FOR EXPANDING TO FULL TZ SUPPORT
                :as rule} (get-timezone-rule *timezone*)]
        (let [month (binding [*offset* 0] (month? value))]
          (if-not dst-rule 0
                  (if (< (:month dst-rule) (:month s-rule))
              ;; Northen hemisphere
                    (cond
                ;; Standard time use timezone offset
                      (and
                       (< month (:month s-rule))
                       (> month (:month dst-rule))) (:save dst-rule)
                      (or
                       (< month (:month dst-rule))
                       (> month (:month s-rule))) 0
                      :else
                      (let [month-frame (calendar-frame value "month")
                            save-light? (= month (:month dst-rule))
                            limit (+
                                   (if-not save-light?
                                     (:save dst-rule)
                                     0)
                                   (binding [*offset* 0]
                                     (until-value
                                      (assoc
                                       (if save-light?
                                         dst-rule
                                         s-rule)
                                       :year (:year (first month-frame))))))]
                        (if ((if save-light? < >=) value limit)
                          0
                          (:save dst-rule))))
              ;; Southern hemisphere
                    (cond
                ;; Standard time use timezone offset
                      (and
                       (> month (:month s-rule))
                       (< month (:month dst-rule))) 0
                      (or
                       (> month (:month dst-rule))
                       (< month (:month s-rule))) (:save dst-rule)
                      :else
                      (let [month-frame (calendar-frame value "month")
                            save-light? (= month (:month dst-rule))
                            limit (+
                                   (if-not save-light?
                                     (:save dst-rule)
                                     0)
                                   (binding [*offset* 0]
                                     (until-value
                                      (assoc
                                       (if save-light?
                                         dst-rule
                                         s-rule)
                                       :year (:year (first month-frame))))))]
                        (if ((if save-light? < >=) value limit)
                          0
                          (:save dst-rule)))))))
        0))))

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

(defn- ^:no-doc <-local
  "Given a local timestamp value function normalizes datetime to Greenwich timezone value"
  ([value] (<-local
            value
            (if (number? *offset*) *offset*
                (if (string? *timezone*)
                  (let [tz-offset (get-timezone-offset *timezone*)]
                    (+
                     ;; move to standard time
                     (get-dst-offset value)
                     tz-offset))
                 ;; Fallback to deprecated getTimeZoneOffset
                  (* -1 (minutes (.getTimezoneOffset (value->utc-date value))))))))
  ([value offset] (+ value offset)))

(defn- ^:no-doc ->local
  "Given a Greenwich timestamp value function normalizes datetime to local timezone value"
  ([value] (->local
            value
            (if (number? *offset*) *offset*
                (if (string? *timezone*)
                  (let [tz-offset (get-timezone-offset *timezone*)]
                    (+
                   ;; remove timzone offset to move to standard time 
                     (get-dst-offset (- value tz-offset))
                     tz-offset))
                ;; Fallback to deprecated getTimeZoneOffset
                  (* -1 (minutes (.getTimezoneOffset (value->utc-date value))))))))
  ([value offset]
   (- value offset)))

(defn milliseconds
  "Function returns value of n seconds as number."
  [n]
  (* n millisecond))

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
  (if ((comp not zero?) (mod year 4)) false
      (if ((comp not zero?) (mod year 100)) true
          (if (and
               (case *calendar*
                 ::gregorian true
                 ::julian false)
               ((comp not zero?) (mod year 400))) false
              true))))

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

(def ^:no-doc normal-year-value (* 365 day))
(def ^:no-doc leap-year-value (* 366 day))

(defn- year-period
  [start-year end-year]
  (reduce
   (fn [r year]
     (+ r (if (leap-year? year)
            leap-year-value
            normal-year-value)))
   0
   (range start-year end-year)))



(def ^:no-doc unix-epoch-year 1970)
(def ^:no-doc unix-epoch-day 4)
(def ^:no-doc julian-day-epoch
  (with-time-configuration
    {:offset 0}
    (- (days 2440588))))





;; Credits 
;; Algorithms taken from awesome Astronomy Answers page
;; https://www.aa.quae.nl/en/reken/juliaansedag.html


(defn value->jcdn
  "For given value returns which Julian Day value belongs to"
  [value]
  (/ (- (round-number value day :floor) julian-day-epoch) day))

(defn jcdn->value [jcdn]
  (+ (days jcdn) julian-day-epoch))


(defn jcdn->calendar [jcdn]
  )

(defn- value->gregorian-calendar [value]
  (letfn [(div [x y]
            [(quot x y)
             (mod x y)])]
    (let [J (value->jcdn value)
          [x3 r3] (div (- (* 4 J) 6884477) 146097)
          [x2 r2] (div
                   (+
                    (* 100 (round-number (/ r3 4) 1 :floor))
                    99)
                   36525)
          [x1 r1] (div
                   (+ (* 5 (round-number (/ r2 100) 1 :floor)) 2)
                   153)
          d (inc (round-number (/ r1 5) 1 :floor))
          c0 (round-number (/ (+ x1 2) 12) 1 :floor)]
      {:day-in-month (long d)
       :month (long (+ x1 (* -12 c0) 3))
       :year (long (+ (* 100 x3) x2 c0))})))

(defn- value->julian-calendar [value]
  (let [J (value->jcdn value)
        y2 (- J 1721118)
        k2 (+ (* 4 y2) 3)
        k1 (+ (* 5 (round-number (/ (mod k2 1461) 4) 1 :floor)) 2)
        x1 (round-number (/ k1 153) 1 :floor)
        c0 (round-number (/ (+ x1 2) 12) 1 :floor)
        j (+ (round-number (/ k2 1461) 1 :floor) c0)
        m (+ x1 (* c0 -12) 3)
        d (inc (round-number (/ (mod k1 153) 5) 1 :floor))]
    {:day-in-month (long d)
     :month (long m)
     :year (long j)}))

(defn- value->islamic-calendar [value]
  (let [J (value->jcdn value)
        k2 (+ 15 (* 30 (- J 1948440)))
        k1 (+ 5 (* 11 (round-number (/ (mod k2 10631) 30) 1 :floor)))
        j (inc (round-number (/ k2 10631) 1 :floor))
        m (inc (round-number (/ k1 325) 1 :floor))
        d (inc (round-number (/ (mod k1 325) 11) 1 :floor))]
    {:day-in-month (long d) :month (long m) :year (long j)}))


(defn- islamic-calendar->value [{:keys [day-in-month month year]}]
  )

(letfn [(c1 [x]
          (round-number
           (/ (inc (* 235 x)) 19)
           1 :floor))
        (q [x]
           (round-number
            (/ (c1 x) 1095)
            1 :floor))
        (r [x] (mod (c1 x) 1095))
        (v1 [x]
            (+
             (* 29 (c1 x))
             (round-number (/ (+ 12084 (* 13753 (c1 x))) 25920) 1 :floor)))
        (v2 [x]
            (+
             (v1 x)
             (mod
              (round-number
               (/ (* 6 (mod (v1 x) 7)) 7)
               1 :floor)
              2)))
        (L2 [x] (- (v2 (inc x)) (v2 x)))
        (v3 [x]
            (* 2
               (mod
                (round-number (/ (+ (L2 x) 19) 15) 1 :floor)
                2)))
        (v4 [x]
            (mod
             (round-number (/ (+ 7 (L2 (dec x))) 15) 1 :floor)
             2))
        (c2 [x] (+ (v2 x) (v3 x) (v4 x)))
        (L [x] (- (c2 (inc x)) (c2 x)))
        (c8 [x] (mod (round-number (/ (+ 7 (L x)) 2) 1 :floor) 15))
        (c9 [x] (* -1 (mod (round-number (/ (- 385 (L x)) 2)  1 :floor) 15)))
        (c3 [x m]
            (+
             (round-number
              (/ (+ 7 (* 384 m)) 13)
              1 :floor)
             (*
              (c8 x)
              (round-number (/ (+ m 4) 12) 1 :floor))
             (*
              (c9 x)
              (round-number (/ (+ m 3) 12) 1 :floor))))
        (c4 [x m] (+ (c2 x) (c3 x m)))]
  (defn- hebrew-date->value [{:keys [year month day-in-month]}]
    (let [c0 (round-number
              (/ (- 13 month) 7)
              1 :floor)
          x1 (+ c0 (dec year))
          z4 (dec day-in-month)
          J (+
             347821
             (c4 x1 (dec month))
             z4)]
      (jcdn->value J)))
  (defn- value->hebrew-calendar [value]
    (let [J (value->jcdn value)
          y4 (- J 347821)
          q (round-number (/ y4 1447) 1 :floor)
          r (mod y4 1447)
          y1' (+ (* 49 q) (round-number (/ (+ (* 23 q) (* 25920 r) 13835)
                                           765433)
                                        1 :floor))
          gama1 (inc y1')
          epsilon1 (round-number (/ (+ (* 19 gama1) 17)
                                    235)
                                 1 :floor)
          u1 (- gama1 (round-number (/ (inc (* 235 epsilon1)) 19) 1 :floor))
          c41' (c4 epsilon1 u1)
          ro1 (- y4 c41')
          gama2 (+ gama1 (round-number (/ ro1 33) 1 :floor))
          epsilon2 (round-number (/ (+ (* 19 gama2) 17)
                                    235)
                                 1 :floor)
          u2 (- gama2 (round-number (/ (inc (* 235 epsilon2)) 19) 1 :floor))
          c42' (c4 epsilon2 u2)
          ro2 (- y4 c42')
          gama3 (+ gama2 (round-number (/ ro2 33) 1 :floor))
          epsilon3 (round-number (/ (+ (* 19 gama3) 17)
                                    235)
                                 1 :floor)
          u3 (- gama3 (round-number (/ (inc (* 235 epsilon3)) 19) 1 :floor))
          z4 (- y4 (c4 epsilon3 u3))
          c (round-number (/ (- 12 u3) 7) 1 :floor)
          j (long (- (inc epsilon3) c))
          m (long (inc u3))
          d (long (inc z4))
          year-length (long (L epsilon3))
          month-mapping (case year-length
                          353 (zipmap (range 1 13)
                                      [30 29 30 29 30 29 30 29 29 29 30 29])
                          354 (zipmap (range 1 13)
                                      [30 29 30 29 30 29 30 29 30 29 30 29])
                          355 (zipmap (range 1 13)
                                      [30 29 30 29 30 29 30 30 30 29 30 29])
                          383 (zipmap (range 1 14)
                                      [30 29 30 29 30 29 30 30 30 29 30 30 29])
                          384 (zipmap (range 1 13)
                                      [30 29 30 29 30 29 30 29 30 29 30 30 29])
                          386 (zipmap (range 1 13)
                                      [30 29 30 29 30 29 30 30 30 29 30 30 29]))
          last-day (get month-mapping m)]
      {:year j
       :month m
       :day-in-month d
       :year-lenght year-length
       :month-length last-day
       :first-day-in-month? (= 1 d)
       :last-day-in-month? (= last-day d)})))

(def
  ^{:doc "Definition of future years. Lazy sequence of all future years from
  unix-epoch-year according to Gregorian calendar"}
  gregorian-future-years
  (iterate
   (fn [{:keys [year value]}]
     {:year (inc year)
      :value (binding [*calendar* ::gregorian]
               (if (leap-year? year)
                 (+ value leap-year-value)
                 (+ value normal-year-value)))})
   {:year 1970
    :value 0}))

(def
  ^{:doc "Definition of past years. Lazy sequence of all past years
  according to Gregorian calendar from unix-epoch-year"}
  gregorian-past-years
  (iterate
   (fn [{:keys [year value]}]
     {:year (dec year)
      :value (binding [*calendar* ::gregorian]
               (if (leap-year? (dec year))
                 (- value leap-year-value)
                 (- value normal-year-value)))})
   {:year 1970
    :value 0}))

(def
  ^{:doc "Definition of future years. Lazy sequence of all future years from
  unix-epoch-year according to Gregorian calendar"}
  julian-future-years
  (iterate
   (fn [{:keys [year value]}]
     {:year (inc year)
      :value (binding  [*calendar* ::julian]
               (if (leap-year? year)
                 (+ value leap-year-value)
                 (+ value normal-year-value)))})
   {:year 1970
    :value 0}))

(def
  ^{:doc "Definition of past years. Lazy sequence of all past years
  according to Gregorian calendar from unix-epoch-year"}
  julian-past-years
  (iterate
   (fn [{:keys [year value]}]
     {:year (dec year)
      :value (binding [*calendar* ::julian]
               (if (leap-year? (dec year))
                 (- value leap-year-value)
                 (- value normal-year-value)))})
   {:year 1970
    :value 0}))

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
   :milliseconds x}
  for given value where seconds is number of seconds of start of the year
  relative to unix-epoch-year"
  [value]
  (loop [position 0]
    (let [[future-years past-years] (case *calendar*
                                      ::gregorian [gregorian-future-years gregorian-past-years]
                                      ::julian [julian-future-years julian-past-years])
          {:keys [year]
           value' :value
           :as target} (if (pos? value)
                         (nth future-years position)
                         (nth past-years position))
          diff (if (pos? value)
                 (- value value')
                 (- value' value))
          step (/ diff normal-year-value 2)]
      (if (zero? diff) target
          (if (pos? diff)
            (if (<
                 diff
                 (if (leap-year? year)
                   leap-year-value
                   normal-year-value))
              (if (pos? value)
                target
                (nth (if (pos? value) future-years past-years) (inc position)))
              (recur (round-number (+ position step) 1 (if (pos? diff) :ceil :floor))))
            (recur (round-number (+ position step) 1 (if (pos? diff) :ceil :floor))))))))

(defn millisecond?
  "Returns which millisecond in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (mod value 1000))

(defn second?
  "Returns which second in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (long
   (mod
    (/ (round-number value second :floor) second)
    60)))

(defn minute?
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (long
   (mod
    (/ (round-number value minute :floor) minute)
    60)))

(defn hour?
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (long
   (mod
    (/ (round-number value hour :floor) hour)
    24)))

(defn day?
  "Returns which day in week does input value belongs to. For example
   for date 15.02.2015 it will return number 7"
  [value]
  (let [move-days (/ (round-number value day (if (neg? value) :ceil :floor)) day)]
    (inc (mod (+ 3 move-days) 7))))

(defn weekend?
  "Returns true if value in seconds belongs to *weekend-days*"
  [value]
  (boolean (*weekend-days* (day? value))))

(defn year?
  "For given value year? returns year that value belogs to."
  [value]
  ((comp :year) (*find-year* value)))

(defn day-in-year?
  "Returns day in year period (1 - 366)"
  [value]
  (let [{year :year year-start :value} (*find-year* value)
        relative-day (long (quot (- value year-start) day))]
    relative-day))

(defn week-in-year?
  "Returns which week in year does input value belongs to. For example
  for date 15.02.2015 it will return number 6"
  [value]
  (let [{year :year year-start :value} (*find-year* value)
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
      (long (+ (/ week-in-year week) 1)))))

(defn- normalize-calendar-value [value]
  (+ value
     (case *calendar*
       ::gregorian 0
       ::julian (days -13))))

(defn month?
  "Returns which month does input value belongs to. For example
  for date 15.02.2015 it will return number 2"
  [value]
  (let [value (normalize-calendar-value value)
        {year :year year-start :value} (*find-year* value)
        relative-day (long (inc (quot (- value year-start) day)))]
    (get
     (if (leap-year? year)
       leap-year-day-mapping
       normal-year-day-mapping)
     relative-day)))

(defn first-day-in-month
  "Returns first day for given month in range of days 1-366 for leap-year?"
  ([month] (first-day-in-month month false))
  ([month leap-year?]
   (apply
    min
    (keep
     (fn [[d m]] (when (= m month) d))
     (if leap-year?
       leap-year-day-mapping
       normal-year-day-mapping)))))

(defn day-in-month?
  "Returns which day (Gregorian) in month input value belongs to. For example
  for date 15.02.2015 it will return number 15"
  [value]
  (let [value (normalize-calendar-value value)
        {year :year year-start :value} (*find-year* value)
        relative-day (long (inc (quot (- value year-start) day)))
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
        days-in-month' (days-in-month (month? value) (leap-year? (year? value)))]
    (= days-in-month' day-in-month)))

(defn utc-date-value
  "Constructs new Date object.
  Months: 1-12
  Days: 1-7 (1 is Monday)"
  ([] (date->utc-value
       #?(:cljs (js/Date.)
          :clj (java.util.Date.))))
  ([year] (utc-date-value year 1))
  ([year month] (utc-date-value year month 1))
  ([year month day] (utc-date-value year month day 0))
  ([year month day hour] (utc-date-value year month day hour 0))
  ([year month day hour minute] (utc-date-value year month day hour minute 0))
  ([year month day hour minute second] (utc-date-value year month day hour minute second 0))
  ([year month day' hour' minute' second' millisecond']
   (let [leap-year? (leap-year? year)]
     (assert (and
              (< 0 month)
              (> 13 month)) "Month should be in range 1-12")
     (assert (not (or (neg? day') (zero? day'))) "There is no 0 or negative day in month.")
     (assert (and
              (<= 0 hour')
              (> 25 hour')) "Hour should be in range 0-24")
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
                          (year-period unix-epoch-year year)
                          (* -1 (year-period year unix-epoch-year)))
           months-period (reduce + 0
                                 (map
                                  #(* day (days-in-month % leap-year?))
                                  (range 1 month)))
           date-value (reduce
                       +
                       0
                       ; (case *calendar*
                       ;   ::gregorian 0
                       ;   ::julian (days -13))
                       [years-period
                        months-period
                        (days (dec day'))
                        (hours hour')
                        (minutes minute')
                        (seconds second')
                        (milliseconds millisecond')])]
       date-value))))

(def utc-date (comp value->utc-date utc-date-value))

(defn date
  "Constructs new Date object.
  Month:       1-12
  Day:         Depends on month
  Hour:        0-24
  Minute:      0-59
  Second:      0-59
  Milliseconds: 0-999"
  ([]
   (utc-date))
  ([year] (date year 1))
  ([year month] (date year month 1))
  ([year month day] (date year month day 0))
  ([year month day hour] (date year month day hour 0))
  ([year month day hour minute] (date year month day hour minute 0))
  ([year month day hour minute second] (date year month day hour minute second 0))
  ([year month day' hour' minute' second' millisecond']
   (let [value (utc-date-value year month day' hour' minute' second' millisecond')]
     (value->utc-date (->local value)))))

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
            (* second seconds)
            (* minutes minute)
            (* milliseconds millisecond)])))

(defn period?
  "Returns duration of for input value in form of map with keys:

   :weeks
   :days
   :hours
   :minutes
   :seconds
   :milliseconds"
  [value]
  (letfn [(round-period [value limit]
            (let [r (round-number value limit :floor)]
              [(- value r) (int (quot r limit))]))]
    (let [[r w] (round-period value week)
          [r d] (round-period r day)
          [r h] (round-period r hour)
          [r m] (round-period r minute)
          [r s] (round-period r second)
          [r ms] (round-period r millisecond)]
      {:weeks w :hours h :days d :minutes m :seconds s :milliseconds ms})))

(defn value->date
  "Returns Date instance for value in seconds for current. Function first
  transforms value to local *timezone* value."
  ([value]
   (let [value (case *calendar*
                 ::gregorian (->local value)
                 ;; While *calendar* is bound to ::julian
                 ;; keep values Dates in Julian domain
                 ::julian (->local (- value (days 13))))]
     (new
      #?(:clj java.util.Date
         :cljs js/Date)
      (long value)))))

(defn date->value
  "Returns value of Date instance in seconds. Value is localized to offset"
  ([t]
   (when t
     (let [v (<-local (date->utc-value t))]
       (case *calendar*
         ::gregorian v
         ::julian (+ v (days 13)))))))

(defn context->value [{:keys [year
                              month
                              day-in-month
                              hour
                              minute
                              second
                              millisecond]
                       :or {year 0
                            month 1
                            day-in-month 1
                            hour 0
                            minute 0
                            second 0
                            millisecond 0}}]
  (date->value (date year month day-in-month hour minute second millisecond)))

(defprotocol TimeValueProtocol
  (time->value [this] "Return numeric value for given object.")
  (value->time [this] "Returns Date for given value.")
  ; (time-travel [this destination] "Travels in time in current zone to given date. Remaining in that zone(place).")
)

#?(:clj
   (extend-protocol TimeValueProtocol
     java.lang.Long
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.lang.Integer
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.lang.Number
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.lang.Float
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.lang.Double
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.math.BigInteger
     (value->time [this] (value->date this))
     (time->value [this] this)
     java.math.BigDecimal
     (value->time [this] (value->date this))
     (time->value [this] this)
     clojure.lang.BigInt
     (value->time [this] (value->date this))
     (time->value [this] this)
     clojure.lang.Ratio
     (value->time [this] (value->date this))
     (time->value [this] this)
     clojure.lang.ASeq
     (value->time [this] (map value->time this))
     (time->value [this] (map time->value this))
     clojure.lang.APersistentMap
     (time->value [this] (context->value this))
     clojure.lang.APersistentVector
     (value->time [this] (mapv value->date this))
     (time->value [this] (mapv time->value this))
     clojure.lang.APersistentSet
     (value->time [this] (set (map value->date this)))
     (time->value [this] (set (map time->value this)))

     java.util.Date
     (time->value [this] (date->value this))
     (value->time [this] this)
     java.time.Instant
     (value->time [this] (java.util.Date/from this))
     (time->value [this] (date->value (java.util.Date/from this))))
   :cljs
   (extend-protocol TimeValueProtocol
     number
     (value->time [this] (value->date this))
     (time->value [this] this)
     js/Date
     (time->value [this] (date->value this))
     (value->time [this] this)
     cljs.core/PersistentArrayMap
     (value->time [this] this)
     (time->value [this] (context->value this))
     cljs.core/PersistentHashMap
     (value->time [this] this)
     (time->value [this] (context->value this))
     cljs.core/PersistentVector
     (value->time [this] (mapv value->time this))
     (time->value [this] (mapv time->value this))
     cljs.core/PersistentHashSet
     (value->time [this] (set (map value->time this)))
     (time->value [this] (set (map time->value this)))))

(defn intervals
  "Given sequence of timestamps (Date) values returns period values between each timestamp
  value in milliseconds"
  [& timestamps]
  (assert
   (every? #(satisfies? TimeValueProtocol %) timestamps)
   (str "Wrong input value."))
  (let [timestamps' (map time->value timestamps)
        t1 (rest timestamps')
        t2 (butlast timestamps')]
    (map - t1 t2)))

(defn interval
  "Returns period of time value in milliseconds between start and end. Input values
  are supposed to be Date."
  [start end]
  (first (intervals start end)))



(defn teleport
  "Teleports value to different timezone."
  [value timezone]
  (let [d (value->time value)]
    (with-time-configuration
      {:timezone timezone}
      (time->value d))))


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
    :year
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
        leap? (leap-year? year)
        first-day (time->value (date year 1 1))]
    (loop [cd first-day
           m 1
           r []]
      (if (> m 12) r
          (recur
           (+ cd (days (days-in-month m leap?)))
           (inc m)
           (concat r (calendar-frame cd :month)))))))

(defmethod calendar-frame "year" [value _]
  (calendar-frame value :year))

(defmethod calendar-frame :month [value _]
  (let [year (year? value)
        leap-year (leap-year? year)
        month (month? value)
        month-days (days-in-month month leap-year)
        current-day-in-month (day-in-month? value)
        current-day (midnight value)
        first-week (week-in-year? value)
        first-day (-
                   current-day
                   (days (dec current-day-in-month)))
        first-day-in-week (day? first-day)]
    (for [d (range month-days)
          :let [v (+ first-day (days d))
                day (mod (+ first-day-in-week d) 7)
                day (if (zero? day) 7 day)
                week' (+
                       first-week
                       (quot
                        (dec (+ d first-day-in-week))
                        7))]]

      {:value v
       :month month
       :year year
       :week week'
       :day day
       :day-in-month (inc d)
       :first-day-in-month? (= (inc d) 1)
       :last-day-in-month? (= d (dec month-days))
       :weekend (boolean (*weekend-days* day))})))

(defmethod calendar-frame "month" [value _]
  (calendar-frame value :month))

(defmethod calendar-frame :week [value _]
  (let [w (week-in-year? value)]
    (filter
     (comp #{w} :week)
     (calendar-frame value :month))))

(defmethod calendar-frame "week" [value _]
  (calendar-frame value :week))

#?(:clj
   (defmacro time-as-value
     "bindings => [name (time->value x) ...]

     Similar to let or binding. Casts all bound symbol values with
     function time->value. Then evaluates body."
     [bindings & body]
     (assert (vector? bindings) "Bindings should be vector")
     (assert (even? (count bindings)) "Odd count of bindings. Try to even binding count.")
     (cond
       (= (count bindings) 0) `(do ~@body)
       (symbol? (bindings 0)) (let [bindings (reduce
                                              (fn [r [s v]]
                                                (conj r s (list 'vura.core/time->value v)))
                                              []
                                              (partition 2 bindings))]
                                `(let ~bindings
                                   ~@body))
       :else (throw
              #?(:clj
                 (IllegalArgumentException.
                  "time-as-value only allows Symbols in bindings")
                 :cljs
                 (js/Error.

                  "time-as-value allows only Symbols in bindings"))))))

(comment
  (def date1 (with-time-configuration {:calendar ::julian} (time->value (date 1582 10 5))))
  (def date2 (time->value (date 1582 10 15)))
  (= date1 date2))

; (def synodic-month-value
;   (period
;    {:days 29
;     :hours 12
;     :minutes 44
;     :seconds 2.8016}))

; (def ^:private new-moon-reference
;   (date->utc-value (utc-date 1999 8 11 13 8)))

; (defn moon-phase? [value]
;   (let [distance (- value new-moon-reference)
;         new-moon-value (round-number
;                         distance
;                         synodic-month-value
;                         (if (pos? distance) :floor :ceil))
;         phase-value (/ synodic-month-value 4)
;         phase-start (+ new-moon-reference new-moon-value)]
;     [new-moon-reference new-moon-value]
;     (value->date (long (+ new-moon-reference new-moon-value)))
;     #_(mod (int (round-number (/ phase-start phase-value) 1)) 4)))
