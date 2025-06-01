(ns vura.calendar-frame-test
  "Comprehensive tests for calendar frame functions"
  (:refer-clojure :exclude [second])
  (:require [clojure.test :refer :all]
            [vura.core :as v]))

(deftest basic-calendar-frame-tests
  "Test basic functionality of calendar frame functions"

  (testing "Month frame generation"
    (let [test-date (v/time->value (v/date 2023 6 15)) ; June 15, 2023 (Thursday)
          month-frame (v/calendar-frame test-date :month)]

      (is (= 30 (count month-frame))
          "June should have 30 days")

      (is (every? #(= 6 (:month %)) month-frame)
          "All days should be in June")

      (is (every? #(= 2023 (:year %)) month-frame)
          "All days should be in 2023")

      (is (= (range 1 31) (map :day-in-month month-frame))
          "Days should be numbered 1-30")

      ;; Test first and last day flags
      (is (:first-day-in-month? (first month-frame))
          "First day should be marked as first-day-in-month")

      (is (:last-day-in-month? (last month-frame))
          "Last day should be marked as last-day-in-month")

      (is (not (:first-day-in-month? (nth month-frame 1)))
          "Second day should not be marked as first-day-in-month")

      (is (not (:last-day-in-month? (nth month-frame 28)))
          "29th day should not be marked as last-day-in-month")))

  (testing "Week frame generation"
    (let [test-date (v/time->value (v/date 2023 6 15)) ; June 15, 2023 (Thursday)
          week-frame (v/calendar-frame test-date :week)]

      (is (= 7 (count week-frame))
          "Week should have 7 days")

      ;; Week should start with Monday (day 1) and end with Sunday (day 7)
      (let [days (map #(v/day? (:value %)) week-frame)]
        (is (= [1 2 3 4 5 6 7] days)
            "Week should run Monday (1) through Sunday (7)"))))

  (testing "Year frame generation - basic properties"
    (let [test-date (v/time->value (v/date 2023 6 15))
          year-frame (v/calendar-frame test-date :year)]

      (is (> (count year-frame) 300)
          "Year should have more than 300 days")

      (is (< (count year-frame) 400)
          "Year should have less than 400 days")

      (is (every? #(= 2023 (:year %)) year-frame)
          "All days should be in 2023")

      ;; Test that multiple months are represented
      (let [months (set (map :month year-frame))]
        (is (> (count months) 8)
            "Should have multiple months represented"))))

  (testing "String variants of frame types"
    (let [test-date (v/time->value (v/date 2023 6 15))]

      (is (= (v/calendar-frame test-date :month)
             (v/calendar-frame test-date "month"))
          "String 'month' should work same as keyword :month")

      (is (= (v/calendar-frame test-date :week)
             (v/calendar-frame test-date "week"))
          "String 'week' should work same as keyword :week")

      (is (= (v/calendar-frame test-date :year)
             (v/calendar-frame test-date "year"))
          "String 'year' should work same as keyword :year"))))

(deftest leap-year-calendar-frame-tests
  "Test calendar frames with leap years"

  (testing "Leap year February"
    (let [leap-feb (v/time->value (v/date 2024 2 15)) ; February 2024 (leap year)
          feb-frame (v/calendar-frame leap-feb :month)]

      (is (= 29 (count feb-frame))
          "February 2024 should have 29 days")

      (is (:last-day-in-month? (last feb-frame))
          "29th should be marked as last day")

      (is (= 29 (:day-in-month (last feb-frame)))
          "Last day should be day 29")))

  (testing "Non-leap year February"
    (let [non-leap-feb (v/time->value (v/date 2023 2 15)) ; February 2023 (non-leap year)
          feb-frame (v/calendar-frame non-leap-feb :month)]

      (is (= 28 (count feb-frame))
          "February 2023 should have 28 days")

      (is (= 28 (:day-in-month (last feb-frame)))
          "Last day should be day 28")))

  (testing "Leap year February vs non-leap year February"
    (let [leap-feb (v/time->value (v/date 2024 2 15))
          non-leap-feb (v/time->value (v/date 2023 2 15))
          leap-frame (v/calendar-frame leap-feb :month)
          non-leap-frame (v/calendar-frame non-leap-feb :month)]

      (is (= 29 (count leap-frame))
          "Leap year February should have 29 days")

      (is (= 28 (count non-leap-frame))
          "Non-leap year February should have 28 days")

      (is (> (count leap-frame) (count non-leap-frame))
          "Leap year February should have more days than non-leap year February"))))

(deftest month-boundary-tests
  "Test calendar frames at month boundaries"

  (testing "End of month boundaries"
    (doseq [month [1 3 5 7 8 10 12]] ; 31-day months
      (let [last-day (v/time->value (v/date 2023 month 31))
            month-frame (v/calendar-frame last-day :month)]

        (is (= 31 (count month-frame))
            (str "Month " month " should have 31 days"))

        (is (:last-day-in-month? (last month-frame))
            (str "Last day of month " month " should be marked correctly")))))

  (testing "30-day months"
    (doseq [month [4 6 9 11]] ; 30-day months
      (let [mid-month (v/time->value (v/date 2023 month 15))
            month-frame (v/calendar-frame mid-month :month)]

        (is (= 30 (count month-frame))
            (str "Month " month " should have 30 days")))))

  (testing "First day of month"
    (let [first-jan (v/time->value (v/date 2023 1 1))
          jan-frame (v/calendar-frame first-jan :month)]

      (is (:first-day-in-month? (first jan-frame))
          "January 1st should be marked as first day")

      (is (= 1 (:day-in-month (first jan-frame)))
          "First day should have day-in-month = 1"))))

(deftest week-numbering-tests
  "Test week numbering in calendar frames"

  (testing "Week numbers in January"
    (let [jan-first (v/time->value (v/date 2023 1 1)) ; January 1, 2023 was a Sunday
          jan-frame (v/calendar-frame jan-first :month)
          week-numbers (map :week jan-frame)]

      (is (every? number? week-numbers)
          "All week numbers should be numeric")

      (is (every? pos? week-numbers)
          "All week numbers should be positive")))

  (testing "Week consistency within month"
    (let [mid-june (v/time->value (v/date 2023 6 15))
          june-frame (v/calendar-frame mid-june :month)]

      ;; Days in the same week should have the same week number
      (let [grouped-by-week (group-by :week june-frame)]
        (doseq [[week-num days] grouped-by-week]
          (is (<= (count days) 7)
              (str "Week " week-num " should not have more than 7 days in this month"))))))

  (testing "Week frame day ordering"
    (let [test-date (v/time->value (v/date 2023 6 15)) ; Thursday
          week-frame (v/calendar-frame test-date :week)
          day-numbers (map #(v/day? (:value %)) week-frame)]

      (is (= [1 2 3 4 5 6 7] day-numbers)
          "Week should start Monday (1) and end Sunday (7)"))))

(deftest weekend-flag-tests
  "Test weekend flagging in calendar frames"

  (testing "Default weekend configuration"
    (let [test-date (v/time->value (v/date 2023 6 15))
          month-frame (v/calendar-frame test-date :month)
          weekend-days (filter :weekend month-frame)]

      ;; Count weekend days (should be Saturdays and Sundays)
      (is (> (count weekend-days) 0)
          "Should have some weekend days")

      ;; All weekend days should be Saturday (6) or Sunday (7)
      (let [weekend-day-numbers (map #(v/day? (:value %)) weekend-days)]
        (is (every? #{6 7} weekend-day-numbers)
            "Weekend days should only be Saturday (6) or Sunday (7)"))))

  (testing "Custom weekend configuration"
    (v/with-time-configuration {:weekend-days #{5 6}} ; Friday and Saturday
      (let [test-date (v/time->value (v/date 2023 6 15))
            month-frame (v/calendar-frame test-date :month)
            weekend-days (filter :weekend month-frame)
            weekend-day-numbers (map #(v/day? (:value %)) weekend-days)]

        (is (every? #{5 6} weekend-day-numbers)
            "Custom weekend should be Friday (5) and Saturday (6)")))))

(deftest year-boundary-edge-cases
  "Test calendar frames at year boundaries"

  (testing "New Year's Day"
    (let [new-years (v/time->value (v/date 2023 1 1))
          week-frame (v/calendar-frame new-years :week)]

      (is (= 7 (count week-frame))
          "Week containing New Year's should have 7 days")

      ;; The week might span across years
      (let [years (set (map #(v/year? (:value %)) week-frame))]
        (is (<= (count years) 2)
            "Week should span at most 2 years"))))

  (testing "New Year's Eve"
    (let [new-years-eve (v/time->value (v/date 2023 12 31))
          week-frame (v/calendar-frame new-years-eve :week)]

      (is (= 7 (count week-frame))
          "Week containing New Year's Eve should have 7 days")

      ;; The week might span into next year
      (let [years (set (map #(v/year? (:value %)) week-frame))]
        (is (<= (count years) 2)
            "Week should span at most 2 years"))))

  (testing "December month frame"
    (let [december (v/time->value (v/date 2023 12 15))
          dec-frame (v/calendar-frame december :month)]

      (is (= 31 (count dec-frame))
          "December should have 31 days")

      (is (every? #(= 12 (:month %)) dec-frame)
          "All days should be in December")

      (is (every? #(= 2023 (:year %)) dec-frame)
          "All days should be in 2023"))))

(deftest calendar-frame-consistency-tests
  "Test consistency between different calendar frame types"

  (testing "Month frame values match day-time-context"
    (let [test-date (v/time->value (v/date 2023 6 15))
          month-frame (v/calendar-frame test-date :month)]

      (doseq [day-info month-frame]
        (let [day-context (v/day-time-context (:value day-info))]

          (is (= (:month day-info) (:month day-context))
              "Month should match day-time-context")

          (is (= (:year day-info) (:year day-context))
              "Year should match day-time-context")

          (is (= (:day-in-month day-info) (:day-in-month day-context))
              "Day-in-month should match day-time-context")

          (is (= (:day day-info) (:day day-context))
              "Day-of-week should match day-time-context")))))

  (testing "Week frame values match day-time-context"
    (let [test-date (v/time->value (v/date 2023 6 15))
          week-frame (v/calendar-frame test-date :week)]

      (doseq [day-context week-frame]
        (let [expected-context (v/day-time-context (:value day-context))]

          (is (= (:value day-context) (:value expected-context))
              "Values should match")

          (is (= (:day expected-context) (v/day? (:value day-context)))
              "Day-of-week should be consistent")))))

  (testing "Month frames have expected structure"
    (let [test-date (v/time->value (v/date 2023 6 15))
          month-frame (v/calendar-frame test-date :month)]

      ;; Verify basic structure is correct for June
      (is (= 30 (count month-frame))
          "June should have 30 days")

      (is (every? #(= 6 (:month %)) month-frame)
          "All days should be in June")

      (is (= (range 1 31) (map :day-in-month month-frame))
          "Days should be numbered consecutively 1-30"))))

(deftest calendar-frame-performance-tests
  "Test performance characteristics of calendar frame generation"

  (testing "Month frame generation performance"
    (let [test-date (v/time->value (v/date 2023 6 15))
          start-time (System/nanoTime)]

      (dotimes [_ 100]
        (v/calendar-frame test-date :month))

      (let [end-time (System/nanoTime)
            duration-ms (/ (- end-time start-time) 1000000.0)]

        (is (< duration-ms 500)
            (str "100 month frame generations should take less than 500ms, took " duration-ms "ms")))))

  (testing "Year frame generation performance"
    (let [test-date (v/time->value (v/date 2023 6 15))
          start-time (System/nanoTime)]

      (dotimes [_ 5]
        (v/calendar-frame test-date :year))

      (let [end-time (System/nanoTime)
            duration-ms (/ (- end-time start-time) 1000000.0)]

        (is (< duration-ms 1000)
            (str "5 year frame generations should take less than 1000ms, took " duration-ms "ms"))))))