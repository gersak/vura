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
        (throw (ex-info ("Out of bounds. Interval cannot be outside " [min- max-])
                        {:min min-
                         :max max-
                         :interval interval})))
      (if (= element "*") 
        (constantly true)
        (cond-> #{}
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
    (let [elements (mapv clojure.string/trim (clojure.string/split cron-record  #" +"))
          ; parts (map transform-interval (set-constraints (map cron-element-parserer elements)))
          constraints [[0 59]
                       [0 59]
                       [0 23]
                       [1 31]
                       [1 12]
                       [1 7]
                       [nil nil]]]
      ; (map #(vector %1 %2) elements constraints)
      (mapv cron-element-parserer elements constraints))))

(defn next-timestamp
  "Return next valid timestamp after input
  timestamp"
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
                          :when ((mapping 6) y) 
                          m (range 1 13)
                          :when (and
                                  (after-timestamp? y m)
                                  ((mapping 4) m)) 
                          d (range 1 (inc (core/days-in-month m (core/leap-year? y))))
                          :when (and
                                  (after-timestamp? y m d)
                                  ((mapping 5) (-> (core/date y m d) core/date->value core/day?))
                                  ((mapping 3) d)) 
                          h (range 24)
                          :when (and
                                  (after-timestamp? y m d h)
                                  ((mapping 2) h)) 
                          minutes (range 60)
                          :when (and
                                  (after-timestamp? y m d h minutes)
                                  ((mapping 1) minutes)) 
                          s (range 60)
                          :when (and
                                  (after-timestamp? y m d h minutes s)
                                  ((mapping 0) s))]
                      (core/date y m d h minutes s))]
    (first found-dates)))


(comment
  (next-timestamp (core/date 2018 2 9 15 50 30) "0,3,20/10 0 0 3-20/10 * * *"))
