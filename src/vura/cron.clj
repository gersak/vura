(ns vura.cron
  (:use dreamcatcher.core
        [clj-time.local :only (local-now to-local-date-time)])
  (:require [clj-time.core :as t]
            [clj-time.format :as tf]))

(defn cron-element-parserer 
  "Parses CRON like element. Elements are in form
  1-25/0
  1,5,40/10
  1,20,40
  20-40/5 
  */2 etc."
  [element]
  (let [[element interval] (clojure.string/split element #"/")
        temp (cond
               (= "*" element) nil 
               (seq (re-find #"-" element)) {:range (map #(Integer/valueOf %) (clojure.string/split element #"-"))}
               (seq (re-find #"," element)) {:sequence (apply sorted-set (map #(Integer/valueOf %) (clojure.string/split element #",")))}
               :else {:fixed (Integer/valueOf element)})]
    (if interval 
      (assoc temp :interval (Integer/valueOf interval))
      temp)))

(defn- set-constraints [cron-mapping]
  (let [trans [{:min 0 :max 59} ;; Seconds
               {:min 0 :max 59} ;; Minutes
               {:min 0 :max 23} ;; Hours
               {:min 1 :max 31} ;; Day of the month
               {:min 1 :max 12} ;; Month
               {:min 1 :max 7}  ;; Day of the week
               {:min 0 :max 4000}]
        prepared (partition 2 (interleave cron-mapping trans))]
    (map #(apply merge %) prepared)))

(defn- validate-range [x] (when (:range x)
                            (and (>= (-> x :range first) (:min x)) (<= (-> x :range second) (:max x)))))

(defn- validate-interval [x] (when (:interval x)
                               (and (<= (:interval x) (- (:max x) (:min x))))))

(defn- validate-sequence [x] (when (:sequence x)
                               (and (every? #(>= % (:min x)) (:sequence x)) (every? #(<= % (:max x)) (:sequence x)))))

(defn- validate-fixed [x] (when (:fixed x) (and (>= (:fixed x) (:min x)) (<= (:fixed x) (:max x)))))

(def ^:private validators [validate-range validate-sequence validate-fixed validate-interval])

(defn- validate-cron-mapping [x]
  (doseq [v validators] (assert (not (false? (v x))) (str (or (:range x) (:fixed x) (:sequence x) (:interval x)) " is out of range. [:min :max] = " [(:min x) (:max x)]))))

(defn parse-cron-string 
  "Parses CRON string e.g.

  \"0,3,20/20 0 0 3-20/10 * * *\" 

  If record is not valid assertion will
  be thrown. Returned data is sequence
  of cron-mappings that define what time
  is valid to execute Job."
  [^String cron-record]
  (let [elements (map clojure.string/trim (clojure.string/split cron-record  #" +"))
        transform-interval (fn [x] (if-let [interval (:interval x)]
                                     (dissoc (assoc x :sequence (apply sorted-set (range (or (:fixed x) 0) (-> x :max inc) interval))) :fixed :interval)
                                     x))
        parts (map transform-interval (set-constraints (map cron-element-parserer elements)))]
    (assert (and (= (count parts) 7)) "Wrong number of elements passed in. Cron schedule has 7 elements.")
    (doseq [x parts] (validate-cron-mapping x))
    (vec parts)))

(defn joda->cron [t]
  [(t/second t) (t/minute t) (t/hour t) (t/day t) (t/month t) (t/day-of-week t) (t/year t)])

(defn cron->joda [c]
  (let [tc (reverse c) 
        tc (cons (first tc) (subvec (vec tc) 2 7))
        t (to-local-date-time (apply t/date-time tc))]
    (assert (= (t/day-of-week t) (c 5)) "Cron day of the week is not valid!")
    t))


(defn- valid-element? [element {:keys [fixed range sequence] :as mapping}]
  (let [fixed? (when fixed 
                 (if (= element fixed) :fixed))
        in-range? (when range 
                    (if (<= (first range) (second range))
                      (when (and (>= element (first range)) (<= element (second range))) :range)
                      (when (or (>= element (first range)) (<= element (second range))) :range)))
        belongs? (when sequence 
                   (if (get sequence element) :sequence))
        valid? (if (every? nil? [fixed range sequence]) :any)]
    (or fixed? belongs? in-range? valid?)))

(defn valid-timestamp? [timestamp cron-string]
  "Returns true if timestamp is valid."
  (let [mapping (parse-cron-string cron-string)
        cron (joda->cron timestamp)
        evaluated-elements (map #(apply valid-element? %) (partition 2 (interleave cron mapping)))]
    (not-any? nil? evaluated-elements)))

(defn next-timestamp
  "Return next valid timestamp after input 
  timestamp"
  [timestamp cron-string]
  (let [mapping (parse-cron-string cron-string)
        current-cron (replace (joda->cron timestamp) [6 4 3 2 1 0])
        day-of-the-week-mapping (nth mapping 5)
        day-time-mapping (replace mapping [6 4 3 2 1 0])
        found-dates (for [y (range (current-cron 0) 4000) 
                          :when (valid-element? y (day-time-mapping 0))
                          m (range 1 13)
                          :when (valid-element? m (day-time-mapping 1))
                          d (range 1 (inc (t/number-of-days-in-the-month (t/date-time y m)))) 
                          :when (and  
                                  (valid-element? d (day-time-mapping 2))
                                  (valid-element? (t/day-of-week (t/date-time y m d)) day-of-the-week-mapping)
                                  (not (t/before? (t/date-time y m d) (apply t/date-time (take 3 current-cron)))))
                          h (range 0 24) 
                          :when (valid-element? h (day-time-mapping 3)) 
                          minutes (range 0 60) 
                          :when (valid-element? minutes (day-time-mapping 4)) 
                          s (range 0 60) 
                          :when (and  (valid-element? s (day-time-mapping 5)) (t/after? (to-local-date-time (t/date-time y m d h minutes s)) timestamp))]
                      (list y m d h minutes s))
        found-date (first found-dates)]
    (when found-date (to-local-date-time (apply t/date-time found-date)))))
