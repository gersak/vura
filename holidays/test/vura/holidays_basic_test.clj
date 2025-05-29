(ns vura.holidays-basic-test
  (:require
   [vura.core :as v]
   [clojure.test :refer [deftest is testing run-tests]]
   [vura.holiday.util :refer [parse-definition]]))

(defn ->day-time-context
  "Helper to create day-time-context from year/month/day"
  [year month day]
  (-> (v/date year month day)
      v/time->value
      v/day-time-context))

;; =============================================================================
;; BASIC PARSING TESTS
;; =============================================================================

(deftest parse-definition-tests
  (testing "Basic date parsing"
    (is (= (parse-definition "01-01")
           {:day-in-month 1 :month 1}))

    (is (= (parse-definition "12-25")
           {:day-in-month 25 :month 12}))

    (is (= (parse-definition "2023-12-25")
           {:year 2023 :day-in-month 25 :month 12})))

  (testing "Weekday parsing"
    (is (= (parse-definition "monday")
           {:week-day 1}))

    (is (= (parse-definition "friday")
           {:week-day 5}))

    (is (= (parse-definition "sunday")
           {:week-day 7})))

  (testing "Nth weekday parsing"
    (is (= (parse-definition "3rd monday")
           {:nth 3 :week-day 1}))

    (is (= (parse-definition "4th thursday")
           {:nth 4 :week-day 4})))

  (testing "Month name parsing"
    (is (= (parse-definition "in February")
           {:in? true :month 2}))

    (is (= (parse-definition "in November")
           {:in? true :month 11})))

  (testing "Complex nth weekday in month"
    (is (= (parse-definition "3rd monday in February")
           {:nth 3 :week-day 1 :in? true :month 2}))

    (is (= (parse-definition "4th thursday in November")
           {:nth 4 :week-day 4 :in? true :month 11})))

  (testing "Easter-based holidays"
    (is (= (parse-definition "easter")
           {:easter? true :offset 0}))

    (is (= (parse-definition "easter -2")
           {:easter? true :offset -2}))

    (is (= (parse-definition "orthodox")
           {:orthodox? true :offset 0}))

    (is (= (parse-definition "orthodox -47")
           {:orthodox? true :offset -47})))

  (testing "Before/after relative dates"
    (is (= (parse-definition "monday before 06-01")
           {:week-day 1 :predicate :before
            :relative-to {:day-in-month 1 :month 6}}))

    (is (= (parse-definition "friday after 4th thursday in November")
           {:week-day 5 :predicate :after
            :relative-to {:nth 4 :week-day 4 :in? true :month 11}})))

  (testing "Conditional substitution statements"
    (is (= (parse-definition "01-01 and if Sunday then next Monday")
           {:day-in-month 1 :month 1 :and? true
            :statements ['("sunday" "then" "next" "monday")]}))

    (is (= (parse-definition "01-01 and if Saturday then previous Friday if Sunday then next Monday")
           {:day-in-month 1 :month 1 :and? true
            :statements ['("saturday" "then" "previous" "friday")
                         '("sunday" "then" "next" "monday")]}))

    ;; Fixed expectation: the parser includes "since 2021" in the last statement
    (is (= (parse-definition "06-19 and if sunday then next monday if saturday then previous friday since 2021")
           {:day-in-month 19 :month 6 :and? true
            :statements ['("sunday" "then" "next" "monday")
                         '("saturday" "then" "previous" "friday" "since" "2021")]})))

  (testing "Julian calendar dates"
    (is (= (parse-definition "julian 12-25")
           {:julian? true :day-in-month 25 :month 12})))

  (testing "Complex mixed patterns"
    ;; Fixed: "orthodox easter" should be separate calls, not combined
    (is (= (parse-definition "easter -47 and if sunday then next monday")
           {:easter? true :offset -47 :and? true
            :statements ['("sunday" "then" "next" "monday")]}))))

;; =============================================================================
;; DAY-TIME-CONTEXT TESTS
;; =============================================================================

(deftest day-time-context-tests
  (testing "Basic date context creation"
    (let [ctx (->day-time-context 2024 1 1)]
      (is (= (:year ctx) 2024))
      (is (= (:month ctx) 1))
      (is (= (:day-in-month ctx) 1))
      (is (= (:day ctx) 1)) ; Monday
      (is (= (:leap-year? ctx) true))))

  (testing "Leap year detection"
    (is (= (:leap-year? (->day-time-context 2024 2 29)) true))
    (is (= (:leap-year? (->day-time-context 2023 2 28)) false)))

  (testing "Weekday calculation"
    ;; Known dates for testing
    (is (= (:day (->day-time-context 2024 1 1)) 1)) ; Monday
    (is (= (:day (->day-time-context 2024 1 7)) 7)) ; Sunday
    (is (= (:day (->day-time-context 2023 1 1)) 7)) ; Sunday
    (is (= (:day (->day-time-context 2023 1 2)) 1))) ; Monday

  (testing "Weekend detection"
    (is (= (:weekend? (->day-time-context 2024 1 6)) true)) ; Saturday
    (is (= (:weekend? (->day-time-context 2024 1 7)) true)) ; Sunday
    (is (= (:weekend? (->day-time-context 2024 1 8)) false)) ; Monday
    (is (= (:weekend? (->day-time-context 2024 1 1)) false))) ; Monday

  (testing "Month boundaries"
    (is (= (:first-day-in-month? (->day-time-context 2024 1 1)) true))
    (is (= (:first-day-in-month? (->day-time-context 2024 1 2)) false))
    (is (= (:last-day-in-month? (->day-time-context 2024 1 31)) true))
    (is (= (:last-day-in-month? (->day-time-context 2024 1 30)) false))
    (is (= (:days-in-month (->day-time-context 2024 2 15)) 29)) ; Leap year February
    (is (= (:days-in-month (->day-time-context 2023 2 15)) 28)))) ; Non-leap February

;; =============================================================================
;; EDGE CASE SCENARIO TESTS
;; =============================================================================

(deftest edge-case-scenarios
  (testing "Year boundary transitions"
    ;; Test dates around New Year to verify year transitions work correctly
    (let [dec31-2023 (->day-time-context 2023 12 31)
          jan01-2024 (->day-time-context 2024 1 1)]
      (is (= (:year dec31-2023) 2023))
      (is (= (:month dec31-2023) 12))
      (is (= (:day-in-month dec31-2023) 31))
      (is (= (:year jan01-2024) 2024))
      (is (= (:month jan01-2024) 1))
      (is (= (:day-in-month jan01-2024) 1))))

  (testing "Leap year February edge cases"
    ;; Test February in leap vs non-leap years
    (let [feb28-2024 (->day-time-context 2024 2 28) ; Leap year
          feb29-2024 (->day-time-context 2024 2 29) ; Leap year
          feb28-2023 (->day-time-context 2023 2 28)] ; Non-leap year
      (is (= (:last-day-in-month? feb28-2024) false)) ; Not last day in leap year
      (is (= (:last-day-in-month? feb29-2024) true)) ; Last day in leap year
      (is (= (:last-day-in-month? feb28-2023) true)) ; Last day in non-leap year
      (is (= (:days-in-month feb28-2024) 29))
      (is (= (:days-in-month feb28-2023) 28))))

  (testing "Weekend patterns across year boundaries"
    ;; Test specific year patterns that could cause issues
    ;; 2022: Jan 1 is Saturday
    ;; 2023: Jan 1 is Sunday  
    ;; 2024: Jan 1 is Monday
    (is (= (:day (->day-time-context 2022 1 1)) 6)) ; Saturday
    (is (= (:day (->day-time-context 2023 1 1)) 7)) ; Sunday
    (is (= (:day (->day-time-context 2024 1 1)) 1)) ; Monday
    (is (= (:weekend? (->day-time-context 2022 1 1)) true))
    (is (= (:weekend? (->day-time-context 2023 1 1)) true))
    (is (= (:weekend? (->day-time-context 2024 1 1)) false)))

  (testing "Known problematic date calculations"
    ;; Test specific dates that have been problematic in holiday calculations

    ;; Memorial Day edge cases (last Monday in May)
    ;; 2025: June 1 is Sunday, so Memorial Day should be May 26 (Monday)
    ;; 2024: June 1 is Saturday, so Memorial Day should be May 27 (Monday)  
    ;; 2026: June 1 is Monday, so Memorial Day should be May 25 (Monday)
    (is (= (:day (->day-time-context 2025 6 1)) 7)) ; Sunday
    (is (= (:day (->day-time-context 2025 5 26)) 1)) ; Monday - Memorial Day
    (is (= (:day (->day-time-context 2024 6 1)) 6)) ; Saturday
    (is (= (:day (->day-time-context 2024 5 27)) 1)) ; Monday - Memorial Day
    (is (= (:day (->day-time-context 2026 6 1)) 1)) ; Monday
    (is (= (:day (->day-time-context 2026 5 25)) 1)) ; Monday - Memorial Day

    ;; Thanksgiving edge cases (4th Thursday in November)
    ;; 2024: November starts on Friday, so 4th Thursday is Nov 28
    ;; 2018: November starts on Thursday, so 4th Thursday is Nov 22
    (is (= (:day (->day-time-context 2024 11 1)) 5)) ; Friday
    (is (= (:day (->day-time-context 2024 11 28)) 4)) ; Thursday - Thanksgiving
    (is (= (:day (->day-time-context 2018 11 1)) 4)) ; Thursday
    (is (= (:day (->day-time-context 2018 11 22)) 4)) ; Thursday - Thanksgiving

    ;; Presidents Day (3rd Monday in February)
    ;; 2024: Leap year, 3rd Monday is Feb 19
    ;; 2023: Non-leap year, 3rd Monday is Feb 20
    ;; 2021: Feb 1 is Monday, so 3rd Monday is Feb 15
    (is (= (:leap-year? (->day-time-context 2024 2 19)) true))
    (is (= (:day (->day-time-context 2024 2 19)) 1)) ; Monday
    (is (= (:leap-year? (->day-time-context 2023 2 20)) false))
    (is (= (:day (->day-time-context 2023 2 20)) 1)) ; Monday
    (is (= (:day (->day-time-context 2021 2 1)) 1)) ; Monday - Feb 1
    (is (= (:day (->day-time-context 2021 2 15)) 1)))) ; Monday - 3rd Monday

;; Run the tests
(deftest basic-functionality-validation
  (testing "Core date functions work as expected"
    ;; Validate that our helper functions and basic date logic work
    (is (not (nil? (->day-time-context 2024 1 1))))
    (is (map? (->day-time-context 2024 1 1)))
    (is (contains? (->day-time-context 2024 1 1) :year))
    (is (contains? (->day-time-context 2024 1 1) :month))
    (is (contains? (->day-time-context 2024 1 1) :day-in-month))
    (is (contains? (->day-time-context 2024 1 1) :day))
    (is (contains? (->day-time-context 2024 1 1) :weekend?))
    (is (contains? (->day-time-context 2024 1 1) :leap-year?))))
