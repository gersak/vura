(ns vura.core-test
  (:refer-clojure :exclude [second])
  (:use clojure.test
        vura.core)
  (:import
    [java.time ZonedDateTime LocalDateTime ZoneId]
    [java.util Calendar]))

(def positive-number (rand 100000))
(def negative-number (rand -10000))

(defn java-day [timestamp]
  (let [c (Calendar/getInstance)
        _ (.setTime c timestamp)]
    (.get c Calendar/DAY_OF_WEEK)))


(deftest test-round-zero
  (is (every? zero? (map #(round-number positive-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value")
  (is (every? zero? (map #(round-number negative-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value"))

(deftest test-round-number
  (are [x y] (= (round-number 10.75 0.5 x) y)
       :ceil 11.0
       :floor 10.5
       :up 11.0
       :down 10.5)
  (are [x y] (= (round-number 10.76 0.5 x) y)
       :ceil 11.0
       :floor 10.5
       :up 11.0
       :down 11.0)
  (are [x y] (= (round-number -10.75 0.5 x) y)
       :ceil -11.0
       :floor -10.5
       :up -11.0
       :down -10.5)
  (are [x y] (= (round-number -10.76 0.5 x) y)
       :ceil -11.0
       :floor -10.5
       :up -11.0
       :down -11.0)
  (is (= (round-number 10.12345681928 0.5) 10.0) "Rounding failed")
  (is (= (round-number 10.12345681928 0.1) 10.1) "Rounding failed")
  (is (= (round-number 122 3) 123) "Rounding failed")
  (is (= (round-number 10.12345681928 0.125) 10.125) "Rounding failed"))


(deftest vura-core
  (testing "Offset computation" 
    (is 
      (and
        (not=
          (with-time-configuration {:timezone "Europe/Zagreb"} (value->date (date->value (date 2018))))
          (with-time-configuration {:timezone "America/Detroit"} (value->date (date->value (date 2018)))))
        (= 
          (with-time-configuration {:timezone "Europe/Zagreb"} (date->value (date 2018)))
          (with-time-configuration {:timezone "America/Detroit"} (date->value (date 2018))))) "Offset affects value! This shouldn't happen"))
  (testing "Testing ? functions"
   (let [test-date (date->value (date 2018 5 30 23 59 0 0))]
    (is (= 2018 (year? test-date)) "Wrong year computed")
    (is (= 5 (month? test-date)) "Wrong month computed")
    (is (= 30 (day-in-month? test-date)) "Wrong day-in-month computed")
    (is (= 3 (day? test-date)) "Wrong day in week computed")
    (is (= 23 (hour? test-date)) "Wrong hour computed")
    (is (= 59 (minute? test-date)) "Wrong minute computed")
    (is (= 0 (second? test-date)) "Wrong second computed")))
  (testing "First last day in month"
    (is (first-day-in-month? (date->value (date 2018 1 1))))
    (is (last-day-in-month? (date->value (date 2018 2 28))))
    (is (false? (last-day-in-month? (date->value (date 2020 2 28)))))
    (is (last-day-in-month? (date->value (date 2020 2 29)))))
  (testing "Testing day-time-context" 
    (let [{:keys [value
                  day
                  week
                  year
                  day-in-month
                  month
                  second
                  hour
                  minute
                  millisecond
                  weekend?
                  holiday?
                  first-day-in-month?
                  last-day-in-month?]} (day-time-context (date->value (date 2018 5 30 23 59 0 0)))]
      (is (= value 1527724740000))
      (is (= day 3))
      (is (= month 5))
      (is (= year 2018))
      (is (= day-in-month 30))
      (is (= hour 23))
      (is (= minute 59))
      (is (= second 0))
      (is (= millisecond 0))
      (is (false? weekend?))
      (is (false? holiday?))
      (is (false? first-day-in-month?))
      (is (false? last-day-in-month?)))
    (let [{:keys [day
                  year
                  day-in-month]} (day-time-context (time->value (date 1900 2 28)))]
      (is (= 3 day) "Wrong day for ")))
  (testing "Testing day-time-context with-time-configuration" 
    (let [{:keys [value
                  day
                  week
                  year
                  day-in-month
                  month
                  second
                  hour
                  minute
                  millisecond
                  weekend?
                  holiday?
                  first-day-in-month?
                  last-day-in-month?]} (with-time-configuration
                                         {:weekend-days #{3}
                                          :timezone "Europe/Zagreb"
                                          :holiday? (fn [{:keys [day-in-month month]}]
                                                      (boolean 
                                                        (and
                                                          (#{5} month)
                                                          (#{30} day-in-month))))}
                                         (day-time-context 
                                           (date->value (date 2018 5 30 23 59 0 0))))]
      (is (= value 1527724740000))
      (is (= day 3))
      (is (= week 22))
      (is (= month 5))
      (is (= year 2018))
      (is (= day-in-month 30))
      (is (= hour 23))
      (is (= minute 59))
      (is (= second 0))
      (is (= millisecond 0))
      (is (true? weekend?))
      (is (true? holiday?))
      (is (false? first-day-in-month?))
      (is (false? last-day-in-month?))))
  (testing "period"
    (let [p (period? (- (date->value (date 2019 1 10 23 10 11 12)) (date->value (date 2018))))
          p' (period p)]
      (is (= p (period? p'))  "period and period? not bidirectional"))))


(deftest TimezoneNormalization
  (testing "Day Time Savings - Only system default timezone"
    (is (= 7 (day? (time->value (date 2018 3 25)))))
    (is (= 7 (day? (time->value (date 2018 3 25 3)))))
    (is (= 25 (day-in-month? (time->value (date 2018 3 25 23 59 59 999)))))
    (is (= 23 (hour? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 59 (minute? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 59 (second? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 999 (millisecond? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 6 (day? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 24 (day-in-month? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 6 (day? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 82 (day-in-year? (time->value (date 2018 3 24 23 59 59 999)))))
    (is (= 83 (day-in-year? (time->value (date 2018 3 25 0 0 1))))))
  
  (testing "Timezones"
    (let [value (with-time-configuration {:timezone "Europe/Zagreb"} 
                  (->
                    (date 2018 3 24 23 59 59 999)
                    time->value))
          minus-wtc-value (with-time-configuration 
                            {:timezone "Asia/Tokyo"}
                            (->
                              (date 2018 3 24 23 59 59 999)
                              time->value))
          plus-wtc-value (with-time-configuration 
                           {:timezone "America/Detroit"}
                           (->
                             (date 2018 3 24 23 59 59 999)
                             time->value))
          value' (time->value
                   (with-time-configuration {:offset 0} 
                     (date 2018 3 24 23 59 59 999)))
          minus-value (time->value
                        (with-time-configuration 
                          {:timezone "Asia/Tokyo"}
                          (date 2018 3 24 23 59 59 999)))
          plus-value (time->value 
                       (with-time-configuration 
                         {:timezone "America/Detroit"}
                         (date 2018 3 24 23 59 59 999)))]
      (is (= minus-wtc-value value plus-wtc-value) "Midnight value should be same for all offsets inside with-time-configuration scope")
      (is (< minus-value value' plus-value) "When time->value is used out of wtc scope *offset* fallbacks to system offset if not nested no other *offset* is bound."))))


(deftest LeapTests
  (testing  "Leap times in Northern hemisphere"
    (let [[summer-before summer-at summer-after] (with-time-configuration
                                                   {:timezone "Europe/Zagreb"}
                                                   [(date 2018 3 25 1 59 59 999)
                                                    (date 2018 3 25 2)
                                                    (date 2018 3 25 3)])
          [winter-before winter-at] (with-time-configuration
                                      {:timezone "Europe/Zagreb"}
                                      [(date 2018 10 28 2 59 59 999)
                                       (date 2018 10 28 3)])]

      (is (= summer-before (-> summer-before date->value value->date)) "date->value->date")
      (is (= summer-at (-> summer-at date->value value->date)) "date->value->date")
      (is (= winter-before (-> winter-before date->value value->date)) "date->value->date")
      (is (= winter-at (-> winter-at date->value value->date)) "date->value->date")
      (is (= 
            (java.util.Date/from (.toInstant (ZonedDateTime/of 2018 3 25 3 0 0 0 (ZoneId/of "Europe/Zagreb"))))
            (date 2018 3 25 3 0 0 0)))
      (is (= 
            (java.util.Date/from (.toInstant (ZonedDateTime/of 2018 3 25 1 59 0 0 (ZoneId/of "Europe/Zagreb"))))
            (date 2018 3 25 1 59 0 0)))
      ; (is (= (get-offset summer-before false) -3600000) "Spring leap wrong before value")
      ; (is (= (get-offset summer-at false) -7200000) "Spring leap wrong at value")
      ; (is (= (get-offset winter-at false) -3600000) "Spring leap wrong before value")
      ; (is (= (get-offset winter-before false) -7200000) "Spring leap wrong at value")
      )))
