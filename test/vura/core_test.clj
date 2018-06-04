(ns vura.core-test
  (:refer-clojure :exclude [second])
  (:use clojure.test
        vura.core)
  (:import
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
          (with-time-configuration {:offset 0} (value->date (date->value (date 2018))))
          (with-time-configuration {:offset -300} (value->date (date->value (date 2018)))))
        (= 
          (with-time-configuration {:offset 0} (date->value (date 2018)))
          (with-time-configuration {:offset -300} (date->value (date 2018))))) "Offset affects value! This shouldn't happen"))
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
      (is (= value 1527724740))
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
      (is (= 2 day) "Wrong day for ")))
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
                                          :offset -180
                                          :holiday? (fn [{:keys [day-in-month month]}]
                                                      (boolean 
                                                        (and
                                                          (#{5} month)
                                                          (#{30} day-in-month))))}
                                         (day-time-context 
                                           (date->value (date 2018 5 30 23 59 0 0))))]
      (is (= value 1527724740))
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
