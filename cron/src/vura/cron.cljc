(ns vura.cron
  (:require
   [clojure.string :as str]
   [vura.core :as core]))

(def ^:dynamic *now*
  {:day 1
   :millisecond 0
   :second 0
   :minute 0
   :hour 0
   :day-in-month 1
   :month 1
   :year 0
   :week 1})

(defn cron-element-parserer
  "Parses CRON like element. Elements are in form
  1-25/0
  1,5,40/10
  1,20,40
  20-40/5
  */2 etc."
  [element [min- max- at]]
  (letfn [(normalize [x]
            (if-not (= at :day) x
                    (let [x (str/lower-case x)]
                      (case x
                        ("mon" "monday") 1
                        ("tue" "tuesday") 2
                        ("wed" "wednesday") 3
                        ("thu" "thursday") 4
                        ("fri" "friday") 5
                        ("sat" "saturday") 6
                        ("sun" "sunday") 7
                        x))))
          (parse-number [x]
            #?(:clj (Integer/valueOf (normalize x))
               :cljs (js/parseInt (normalize x))))]
    (let [[element interval] (str/split element #"/")
          current (get *now* at)
          interval (when interval (parse-number interval))
          fixed (filter
                 #(if (and min- max-)
                    (<= min- % max-)
                    true)
                 (mapv
                  parse-number
                  (remove
                   #(or
                     (re-find #"\d+-\d+" %)
                     (re-find #"\*" %)
                     (= % "?"))
                   (str/split element #","))))]
      (when (and
             interval
             (pos? interval)
             (or
              (> interval max-)
              (< interval min-)
              (zero? interval)))
        (throw (ex-info (str "Out of bounds. Interval cannot be outside " [min- max-])
                        {:min min-
                         :max max-
                         :interval interval})))
      (cond
        (and (= element "*") (not interval)) (constantly true)
        (= element "?") (constantly true)
        (= element "*") (set (range min- (inc max-) interval))
        :else (let [maybe-fixed (cond-> #{}
                                  (seq (re-find #"-" element)) (into
                                                                (let [[f l] (str/split (re-find #"\d+-\d+" element) #"-")]
                                                                  (range
                                                                   (parse-number f)
                                                                   (inc (parse-number l)))))
                                  interval (into
                                            (case fixed
                                              ["*"] (range min- (inc max-) interval)
                                              (reduce
                                               concat
                                               (map #(range % (inc max-) interval) fixed))))
                                  true (into fixed))]
                (if (empty? maybe-fixed) (constantly true) maybe-fixed))))))

(defn parse-cron-string
  "Parses CRON string e.g.

  \"0,3,20/20 0 0 3-20/10 * * *\"

  If record is not valid assertion will
  be thrown. Returned data is sequence
  of cron-mappings that define what time
  is valid to execute Job."
  [^String cron-record]
  (when (or (nil? cron-record) (str/blank? cron-record))
    (throw (ex-info "Cron expression cannot be empty or nil" {:expression cron-record})))

  (let [elements (mapv str/trim (str/split cron-record #"\s+"))]
    (when (< (count elements) 6)
      (throw (ex-info "Cron expression must have at least 6 fields"
                      {:expression cron-record :field-count (count elements)})))

    (let [constraints [[0 59 :second]
                       [0 59 :minute]
                       [0 23 :hour]
                       [1 31 :day-in-month]
                       [1 12 :month]
                       [1 7 :day]
                       [nil nil]]]

      ;; Pre-validate raw values before parsing
      (doseq [[element [min-val max-val field-name]] (map vector elements constraints)]
        (when (and min-val max-val)
          ;; Check for obvious out-of-range values in the raw element
          (when-let [numbers (re-seq #"\d+" element)]
            (doseq [num-str numbers]
              (let [num (Integer/parseInt num-str)]
                (when (or (< num min-val) (> num max-val))
                  (throw (ex-info (str "Value " num " is out of range for " field-name " (valid range: " min-val "-" max-val ")")
                                  {:field field-name :value num :min min-val :max max-val :expression cron-record}))))))))

      (mapv cron-element-parserer elements constraints))))

(defn valid-timestamp?
  "Given a timestamp and cron definition function returns true
   if timestamp satisfies cron definition."
  [timestamp cron-string]
  (let [tv (if (number? timestamp)
             timestamp
             (core/date->value timestamp))
        {:keys [year month day-in-month hour minute second] :as now} (core/day-time-context tv)
        elements [second minute hour day-in-month month (core/day? tv)]
        constraints (binding [*now* now] (parse-cron-string cron-string))]
    ; (partition 2 (interleave constraints elements))
    (every?
     (fn [[validator value]] (validator value))
     (partition 2 (interleave constraints elements)))))

(defn days-in-month [year month]
  (let [leap-year? (if ((comp not zero?) (mod year 4)) false
                       (if ((comp not zero?) (mod year 100)) true
                           (if ((comp not zero?) (mod year 400)) false
                               true)))]
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
      12 31)))

(defn future-timestamps
  [timestamp cron-string]
  (let [timestamp-value (if (number? timestamp)
                          timestamp
                          (core/date->value timestamp))
        {:keys [year month day-in-month hour minute second] :as now} (core/day-time-context timestamp-value)
        mapping (binding [*now* now] (parse-cron-string cron-string))
        timestamp-elements [year month day-in-month hour minute second]
        timestamp-min-values (reduce
                              (fn [r v]
                                (let [v' (core/date->value
                                          (apply core/date (take v timestamp-elements)))]
                                  (conj r v')))
                              []
                              (range 7))
        after-timestamp? (fn [& args]
                           (>=
                            (core/date->value (apply core/date args))
                            (get timestamp-min-values (count args))))]
    (for [y (iterate inc year)
          :while (>= y year)
          :when (and y (get mapping 6 (constantly true)))
          m (range 1 13)
          :when (and
                 (after-timestamp? y m)
                 ((get mapping 4 (constantly true)) m))
          d (range 1 (inc (days-in-month y m)))
          ; :when (and
          ;         (after-timestamp? y m d)
          ;         ((get mapping 5 (constantly true))
          ;          (-> (core/date y m d) core/date->value core/day?))
          ;         ((get mapping 3 (constantly true)) d))
          :when (and
                 (after-timestamp? y m d)
                 (if-let [f (get mapping 5)]
                   (-> (core/date y m d) core/date->value core/day? f)
                   true)
                 (if-let [f (get mapping 3)] (f d) true))
          h (range 24)
          :when (and
                 (after-timestamp? y m d h)
                 ((get mapping 2 (constantly true)) h))
          minutes (range 60)
          :when (and
                 (after-timestamp? y m d h minutes)
                 ((get mapping 1 (constantly true)) minutes))
          s (range 60)
          :when (and
                 (after-timestamp? y m d h minutes s)
                 (>
                  (core/date->value (core/date y m d h minutes s))
                  (+ timestamp-value core/second))
                 ((get mapping 0 (constantly true)) s))]
      (core/date->value (core/date y m d h minutes s)))))

(defn next-timestamp
  "Return next valid timestamp after input timestamp. If there is no such timestamp,
  than nil is returned."
  [timestamp cron-string]
  (first (future-timestamps timestamp cron-string)))

(comment
  (time (take 10 (future-timestamps (core/date) "* */10")))
  (time (take 10 (future-timestamps (core/date) "0 */30")))
  (next-timestamp (core/date) "*/10")
  (next-timestamp (core/date) "0 0 0 1 1 * 2018")
  (next-timestamp (core/date) "0 15 9 * * TUE")
  (binding [*now* (-> (core/date) core/time->value core/day-time-context)]
    (parse-cron-string "0 21/3 * * * *"))
  (next-timestamp (core/date 2018 2 9 15 50 30) "*/10")
  (next-timestamp (core/date 2018 2 9 15 50 30) "15 10 13 29 2 4 *")
  (def timestamp (core/date 2018 2 9 15 50 0))
  (def cron-string "0 * * * * *")
  (def tv (core/date->value timestamp))
  (def constraints (parse-cron-string cron-string))
  (valid-timestamp? (core/date 2018 12 20 0 0 0) "0 * * * * * 2019"))
