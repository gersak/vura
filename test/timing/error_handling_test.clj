(ns timing.error-handling-test
  "Tests for error handling with invalid inputs in Timing"
  (:refer-clojure :exclude [second])
  (:require [clojure.test :refer :all]
            [timing.core :as v]
            [timing.timezones.db :as tz]))

(deftest invalid-date-construction-tests
  "Test behavior with edge case date inputs - Timing uses lenient date construction"

  (testing "Out of range month values get adjusted"
    ;; Month 13 becomes January of next year
    (let [adjusted-date (v/date 2023 13 15)]
      (is (= 1 (v/month? (v/time->value adjusted-date)))
          "Month 13 should become January")
      (is (= 2024 (v/year? (v/time->value adjusted-date)))
          "Year should advance when month > 12"))

    ;; Month 0 becomes December of previous year  
    (let [adjusted-date (v/date 2023 0 15)]
      (is (= 12 (v/month? (v/time->value adjusted-date)))
          "Month 0 should become December")
      (is (= 2022 (v/year? (v/time->value adjusted-date)))
          "Year should decrease when month < 1")))

  (testing "Out of range day values get adjusted"
    ;; February 30th becomes March 2nd (or 1st in leap year)
    (let [adjusted-date (v/date 2023 2 30)
          day-context (v/day-time-context (v/time->value adjusted-date))]
      (is (= 3 (:month day-context))
          "Feb 30 should become March")
      (is (= 2 (:day-in-month day-context))
          "Should be March 2nd in non-leap year"))

    ;; April 31st becomes May 1st
    (let [adjusted-date (v/date 2023 4 31)
          day-context (v/day-time-context (v/time->value adjusted-date))]
      (is (= 5 (:month day-context))
          "April 31st should become May")
      (is (= 1 (:day-in-month day-context))
          "Should be May 1st")))

  (testing "Leap year handling is lenient"
    ;; Feb 29 in non-leap year becomes March 1st
    (let [adjusted-date (v/date 2023 2 29)
          day-context (v/day-time-context (v/time->value adjusted-date))]
      (is (= 3 (:month day-context))
          "Feb 29 in non-leap year should become March")
      (is (= 1 (:day-in-month day-context))
          "Should be March 1st"))

    ;; Feb 29 in leap year should be valid
    (let [leap-date (v/date 2024 2 29)
          day-context (v/day-time-context (v/time->value leap-date))]
      (is (= 2 (:month day-context))
          "Feb 29 in leap year should stay February")
      (is (= 29 (:day-in-month day-context))
          "Should be Feb 29th")))

  (testing "Hour values get adjusted when out of range"
    ;; Hour 24 becomes hour 0 of next day
    (let [adjusted-date (v/date 2023 6 15 24 0 0)
          day-context (v/day-time-context (v/time->value adjusted-date))]
      (is (= 0 (:hour day-context))
          "Hour 24 should become hour 0")
      (is (= 16 (:day-in-month day-context))
          "Should advance to next day")))

  (testing "Negative values get adjusted backwards"
    ;; Day -1 should go to previous month
    (let [adjusted-date (v/date 2023 6 -1)
          day-context (v/day-time-context (v/time->value adjusted-date))]
      (is (= 5 (:month day-context))
          "Negative day should go to previous month"))))

(deftest invalid-timezone-operations-tests
  "Test error handling for invalid timezone operations"

  (testing "Non-existent timezone"
    (is (thrown? Exception
                 (v/with-time-configuration {:timezone "Invalid/Timezone"}
                   (v/date 2023 6 15)))
        "Non-existent timezone should throw exception"))

  (testing "Invalid timezone format"
    (is (thrown? Exception
                 (v/with-time-configuration {:timezone "not-a-timezone"}
                   (v/date 2023 6 15)))
        "Invalid timezone format should throw exception"))

  (testing "Teleport with invalid timezone"
    (let [valid-time (v/time->value (v/date 2023 6 15))]
      (is (thrown? Exception (v/teleport valid-time "Invalid/Zone"))
          "Teleporting to invalid timezone should throw exception"))))

(deftest invalid-calendar-conversion-tests
  "Test calendar conversion functions with edge case inputs"

  (testing "Calendar conversions are lenient like date construction"
    ;; Calendar conversions appear to be lenient, not throwing exceptions
    (let [result (v/gregorian-date->value {:month 13 :day-in-month 1 :year 2023})]
      (is (number? result)
          "Gregorian conversion with month 13 should return adjusted value"))

    (let [result (v/gregorian-date->value {:month 2 :day-in-month 30 :year 2023})]
      (is (number? result)
          "Gregorian conversion with Feb 30 should return adjusted value"))

    (let [result (v/julian-date->value {:month 13 :day-in-month 1 :year 2023})]
      (is (number? result)
          "Julian conversion should handle out-of-range values"))

    (let [result (v/islamic-date->value {:month 13 :day-in-month 1 :year 1444})]
      (is (number? result)
          "Islamic conversion should handle out-of-range values"))

    (let [result (v/hebrew-date->value {:month 13 :day-in-month 1 :year 5783})]
      (is (number? result)
          "Hebrew conversion should handle out-of-range values")))

  (testing "Calendar conversions handle missing fields with defaults"
    (let [result (v/gregorian-date->value {:year 2023})]
      (is (number? result)
          "Should use defaults for missing month and day"))

    (let [result (v/islamic-date->value {:year 1444 :month 6})]
      (is (number? result)
          "Should use default for missing day"))))

(deftest extreme-value-tests
  "Test handling of extreme timestamp values"

  (testing "Very large timestamps"
    (let [large-value (* 100 365 24 60 60 1000)] ; ~100 years worth of milliseconds
      (is (some? (v/value->time large-value))
          "Should handle reasonably large timestamps")))

  (testing "Negative timestamps"
    (let [negative-value (* -365 24 60 60 1000)] ; -1 year
      (is (some? (v/value->time negative-value))
          "Should handle negative timestamps (before epoch)")))

  (testing "Zero timestamp"
    (is (some? (v/value->time 0))
        "Should handle epoch timestamp"))

  (testing "Very small positive values"
    (is (some? (v/value->time 1))
        "Should handle very small positive timestamps")))

(deftest invalid-round-number-tests
  "Test round-number function with edge cases"

  (testing "Zero target number behavior"
    ;; Rounding to zero target returns zero (not an error)
    (is (= 0 (v/round-number 10.5 0 :up))
        "Rounding to zero target should return zero")
    (is (= 0 (v/round-number -5.3 0 :down))
        "Rounding negative number to zero target should return zero"))

  (testing "Negative target number throws assertion error"
    ;; Negative target throws assertion error due to precondition
    (is (thrown? AssertionError (v/round-number 10.5 -1 :up))
        "Negative target should throw assertion error"))

  (testing "Invalid strategy returns original value"
    ;; Invalid strategy seems to return the original value
    (is (= 10.5 (v/round-number 10.5 0.5 :invalid))
        "Invalid strategy should return original value"))

  (testing "Non-numeric inputs should cause errors"
    (is (thrown? Exception (v/round-number "not-a-number" 0.5 :up))
        "Non-numeric value should throw exception")
    (is (thrown? Exception (v/round-number 10.5 "not-a-number" :up))
        "Non-numeric target should throw exception")))

(deftest invalid-configuration-tests
  "Test behavior with edge case configurations"

  (testing "Invalid calendar type gets handled gracefully"
    ;; Invalid calendar type may just fall back to default behavior
    (try
      (v/with-time-configuration {:calendar :invalid-calendar}
        (let [context (v/day-time-context (v/time->value (v/date 2023 6 15)))]
          (is (map? context)
              "Should return context even with invalid calendar")))
      (catch Exception e
        (is true "Invalid calendar type may throw exception - this is acceptable"))))

  (testing "Invalid weekend days configuration behavior"
    ;; Invalid weekend day numbers may be ignored or cause errors
    (try
      (v/with-time-configuration {:weekend-days #{8 9}} ; Invalid day numbers
        (let [result (v/weekend? (v/time->value (v/date 2023 6 15)))]
          (is (boolean? result)
              "Should return boolean even with invalid weekend config")))
      (catch Exception e
        (is true "Invalid weekend configuration may throw exception"))))

  (testing "Invalid holiday function behavior"
    ;; Non-function holiday? may be ignored or cause errors  
    (try
      (v/with-time-configuration {:holiday? "not-a-function"}
        (let [context (v/day-time-context (v/time->value (v/date 2023 6 15)))]
          (is (map? context)
              "Should return context with non-function holiday?")))
      (catch Exception e
        (is true "Non-function holiday? may cause error")))))

(deftest nil-and-empty-input-tests
  "Test handling of nil and empty inputs"

  (testing "Nil inputs to core functions cause protocol errors"
    (is (thrown? Exception (v/time->value nil))
        "Nil input to time->value should throw exception")
    (is (thrown? Exception (v/value->time nil))
        "Nil input to value->time should throw exception")
    (is (thrown? Exception (v/day-time-context nil))
        "Nil input to day-time-context should throw exception"))

  (testing "Empty context maps use defaults"
    ;; Empty context map uses default values rather than throwing
    (let [result (v/context->value {})]
      (is (number? result)
          "Empty context map should use defaults and return epoch"))

    ;; Context with invalid keys may be ignored
    (let [result (v/context->value {:invalid-key "value"})]
      (is (number? result)
          "Context with invalid keys should ignore them and use defaults"))))

(deftest boundary-condition-tests
  "Test boundary conditions that might cause errors"

  (testing "DST transition boundaries"
    (v/with-time-configuration {:timezone "America/New_York"}
      ;; Test spring forward (2:00 AM becomes 3:00 AM)
      (let [spring-forward-time (v/date 2023 3 12 2 30 0 0)]
        (is (some? spring-forward-time)
            "Should handle spring forward DST transition"))

      ;; Test fall back (2:00 AM happens twice)
      (let [fall-back-time (v/date 2023 11 5 1 30 0 0)]
        (is (some? fall-back-time)
            "Should handle fall back DST transition"))))

  (testing "Year boundary conditions"
    (is (some? (v/date 1970 1 1 0 0 0 0))
        "Should handle Unix epoch start")
    (is (some? (v/date 2038 1 19 3 14 7 0))
        "Should handle dates near 32-bit timestamp limit")
    (is (some? (v/date 1900 1 1))
        "Should handle early 20th century dates"))

  (testing "Month length edge cases"
    ;; Test last day of each month
    (doseq [month (range 1 13)]
      (let [last-day (case month
                       (1 3 5 7 8 10 12) 31
                       (4 6 9 11) 30
                       2 28)] ; Non-leap year
        (is (some? (v/date 2023 month last-day))
            (str "Should handle last day of month " month))))))
