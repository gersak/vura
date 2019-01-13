(ns vura.cron
  (:require [vura.core :as core]))

(defn cron-element-parserer
  "Parses CRON like element. Elements are in form
  1-25/0
  1,5,40/10
  1,20,40
  20-40/5
  */2 etc."
  [element [min- max-]]
  (letfn [(parse-number [x]
            #?(:clj (Integer/valueOf x)
               :cljs (js/parseInt x)))]
    (let [[element interval] (clojure.string/split element #"/")
          interval (when interval (parse-number interval))
          fixed (mapv
                 parse-number
                 (remove
                  #(or
                    (re-find #"\d+-\d+" %)
                    (re-find #"\*" %))
                  (clojure.string/split element #",")))]
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
        (= element "*") (set (range min- (inc max-) interval))
        :else (cond-> #{}
                (seq (re-find #"-" element)) (into
                                              (let [[f l] (clojure.string/split  (re-find #"\d+-\d+" element) #"-")]
                                                (range
                                                 (parse-number f)
                                                 (inc (parse-number l)))))
                interval (into
                          (case fixed
                            ["*"] (range min- (inc max-) interval)
                            (reduce
                             concat
                             (map #(range %  (inc max-) interval) fixed))))
                true (into fixed))))))

(defn parse-cron-string
  "Parses CRON string e.g.

  \"0,3,20/20 0 0 3-20/10 * * *\"

  If record is not valid assertion will
  be thrown. Returned data is sequence
  of cron-mappings that define what time
  is valid to execute Job."
  [^String cron-record]
  (letfn [(transform-interval [x]
            (cond-> x
              (and
               (:interval x)
               (:fixed x)) ((fn [{:keys [interval fixed] :as x}]
                              (let [anchorns (conj (sorted-set fixed))]
                                (assoc x :sequence
                                       (apply sorted-set
                                              (reduce
                                               concat
                                               (map
                                                #(range %  (-> x :max inc) interval)
                                                anchorns)))))))
              (:range x) ((fn [{:keys [sequence]
                                :or {sequence (sorted-set)}
                                :as x}]
                            (let [[f l] (:range x)]
                              (assoc x :sequence (into sequence (range f (inc l)))))))
              true (dissoc :range :interval :fixed)))]
    (let [elements (mapv clojure.string/trim (clojure.string/split cron-record  #"\s+"))
          constraints [[0 59]
                       [0 59]
                       [0 23]
                       [1 31]
                       [1 12]
                       [1 7]
                       [nil nil]]]
      (mapv cron-element-parserer elements constraints))))

(defn valid-timestamp?
  "Given a timestamp and cron definition function returns true
   if timestamp satisfies cron definition."
  [timestamp cron-string]
  (let [tv (core/date->value timestamp)
        constraints (parse-cron-string cron-string)
        elements ((juxt
                   core/second?
                   core/minute?
                   core/hour?
                   core/month?
                   core/day-in-month?
                   core/day?
                   core/year?) tv)]
    ; (partition 2 (interleave constraints elements))
    (every?
     (fn [[validator value]] (validator value))
     (partition 2 (interleave constraints elements)))))


(defn days-in-month [year month]
  (let [leap-year?  (if ((comp not zero?) (mod year 4)) false
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


(defn next-timestamp
  "Return next valid timestamp after input timestamp. If there is no such timestamp,
   than nil is returned."
  [timestamp cron-string]
  (let [mapping (parse-cron-string cron-string)
        timestamp-value (core/date->value timestamp)
        timestamp-elements ((juxt
                             core/year?
                             core/month?
                             core/day-in-month?
                             core/hour?
                             core/minute?
                             core/second?) timestamp-value)
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
                            (get timestamp-min-values (count args))))
        found-dates (for [y (iterate inc (core/year? timestamp-value))
                          :while (>= y (first timestamp-elements))
                          :when (and
                                 (get mapping 6 (constantly true)) y)
                          m (range 1 13)
                          :when (and
                                 (after-timestamp? y m)
                                 ((get mapping 4 (constantly true)) m))
                          d (range 1 (inc (days-in-month y m)))
                          :when (and
                                 (after-timestamp? y m d)
                                 ((get mapping 5 (constantly true))
                                  (-> (core/date y m d) core/date->value core/day?))
                                 ((get mapping 3 (constantly true)) d))
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
                      (core/date y m d h minutes s))]
    (when-let [t (first found-dates)]
      (when (valid-timestamp? t cron-string)
        t))))

(comment
  (next-timestamp (core/date) "*/10")
  (next-timestamp (core/date) "0 0 0 1 1 * 2018")
  (parse-cron-string "*/10 *")
  (next-timestamp (core/date 2018 2 9 15 50 30) "*/10")
  (next-timestamp (core/date 2018 2 9 15 50 30) "15 10 13 29 2 4 *")
  (def timestamp (core/date 2018 2 9 15 50 0))
  (def cron-string "0 * * * * *")
  (def tv (core/date->value timestamp))
  (def constraints (parse-cron-string cron-string))
  (valid-timestamp? (core/date 2018 12 20 0 0 0) "0 * * * * * 2019"))
