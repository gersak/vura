(ns timing.holidays-edge-cases-test
  (:require
   [timing.core :as v]
   [clojure.test :refer [deftest is testing]]
   [timing.holiday.compiler :as c]
   [timing.holiday.util :refer [parse-definition]]))

(defn ->day-time-context
  "Helper to create day-time-context from year/month/day"
  [year month day]
  (-> (v/date year month day)
      v/time->value
      v/day-time-context))

;; =============================================================================
;; EDGE CASE TESTS FOR CONDITIONAL SUBSTITUTION RULES
;; =============================================================================

(deftest conditional-substitution-edge-cases
  (testing "New Year's Day with Sunday->Monday substitution across year boundary"
    (let [new-year-condition (c/compile-condition
                              (parse-definition "01-01 and if Sunday then next Monday"))]

      ;; 2023: Jan 1 is Sunday -> should substitute to Jan 2 (Monday)
      (is (true? (new-year-condition (->day-time-context 2023 1 1)))
          "Jan 1, 2023 (Sunday) should be holiday due to 'and' condition")
      (is (true? (new-year-condition (->day-time-context 2023 1 2)))
          "Jan 2, 2023 (Monday) should be holiday due to substitution")
      (is (nil? (new-year-condition (->day-time-context 2023 1 3)))
          "Jan 3, 2023 (Tuesday) should NOT be holiday")

      ;; 2028: Jan 1 is Saturday -> should be holiday but no substitution
      (is (true? (new-year-condition (->day-time-context 2028 1 1)))
          "Jan 1, 2028 (Saturday) should be holiday")
      (is (nil? (new-year-condition (->day-time-context 2028 1 2)))
          "Jan 2, 2028 (Sunday) should NOT be holiday")
      (is (nil? (new-year-condition (->day-time-context 2028 1 3)))
          "Jan 3, 2028 (Monday) should NOT be holiday")

      ;; 2024: Jan 1 is Monday -> should be holiday, no substitution
      (is (true? (new-year-condition (->day-time-context 2024 1 1)))
          "Jan 1, 2024 (Monday) should be holiday")
      (is (nil? (new-year-condition (->day-time-context 2024 1 2)))
          "Jan 2, 2024 (Tuesday) should NOT be holiday")))

  (testing "Multiple substitution rules - Saturday->Friday, Sunday->Monday"
    (let [multiple-substitution (c/compile-condition
                                 (parse-definition "01-01 and if Saturday then previous Friday if Sunday then next Monday"))]

      ;; 2022: Jan 1 is Saturday -> should substitute to Dec 31, 2021 (Friday)
      (is (true? (multiple-substitution (->day-time-context 2022 1 1)))
          "Jan 1, 2022 (Saturday) should be holiday")
      (is (true? (multiple-substitution (->day-time-context 2021 12 31)))
          "Dec 31, 2021 (Friday) should be holiday due to substitution")
      (is (nil? (multiple-substitution (->day-time-context 2022 1 2)))
          "Jan 2, 2022 (Sunday) should NOT be holiday")
      (is (nil? (multiple-substitution (->day-time-context 2022 1 3)))
          "Jan 3, 2022 (Monday) should NOT be holiday")

      ;; 2023: Jan 1 is Sunday -> should substitute to Jan 2 (Monday)  
      (is (true? (multiple-substitution (->day-time-context 2023 1 1)))
          "Jan 1, 2023 (Sunday) should be holiday")
      (is (true? (multiple-substitution (->day-time-context 2023 1 2)))
          "Jan 2, 2023 (Monday) should be holiday due to substitution")
      (is (nil? (multiple-substitution (->day-time-context 2022 12 31)))
          "Dec 31, 2022 (Saturday) should NOT be holiday when Jan 1 is Sunday"))))

;; =============================================================================
;; EDGE CASE TESTS FOR RELATIVE DATE CALCULATIONS  
;; =============================================================================

(deftest relative-date-edge-cases
  (testing "Monday before June 1st - Memorial Day edge cases"
    (let [memorial-day (c/compile-before-after
                        (parse-definition "monday before 06-01"))]

      ;; 2025: June 1 is Sunday -> last Monday in May should be May 26
      (is (true? (memorial-day (->day-time-context 2025 5 26)))
          "May 26, 2025 should be Memorial Day (Monday before Jun 1 Sunday)")
      (is (nil? (memorial-day (->day-time-context 2025 5 19)))
          "May 19, 2025 should NOT be Memorial Day")
      (is (nil? (memorial-day (->day-time-context 2025 6 1)))
          "June 1, 2025 should NOT be Memorial Day")
      (is (nil? (memorial-day (->day-time-context 2025 6 2)))
          "June 2, 2025 should NOT be Memorial Day")

      ;; 2024: June 1 is Saturday -> last Monday in May should be May 27  
      (is (true? (memorial-day (->day-time-context 2024 5 27)))
          "May 27, 2024 should be Memorial Day (Monday before Jun 1 Saturday)")
      (is (nil? (memorial-day (->day-time-context 2024 5 20)))
          "May 20, 2024 should NOT be Memorial Day")

      ;; 2026: June 1 is Monday -> Memorial Day should be May 25 (Monday before)
      (is (true? (memorial-day (->day-time-context 2026 5 25)))
          "May 25, 2026 should be Memorial Day (Monday before Jun 1 Monday)")
      (is (nil? (memorial-day (->day-time-context 2026 6 1)))
          "June 1, 2026 (Monday) should NOT be Memorial Day")))

  (testing "Friday after 4th Thursday in November - Black Friday edge cases"
    (let [black-friday (c/compile-before-after
                        (parse-definition "friday after 4th thursday in November"))]

      ;; 2024: 4th Thursday is Nov 28 -> Black Friday is Nov 29
      (is (true? (black-friday (->day-time-context 2024 11 29)))
          "Nov 29, 2024 should be Black Friday")
      (is (nil? (black-friday (->day-time-context 2024 11 28)))
          "Nov 28, 2024 (Thanksgiving) should NOT be Black Friday")
      (is (nil? (black-friday (->day-time-context 2024 12 6)))
          "Dec 6, 2024 should NOT be Black Friday")

      ;; 2025: 4th Thursday is Nov 27 -> Black Friday is Nov 28  
      (is (true? (black-friday (->day-time-context 2025 11 28)))
          "Nov 28, 2025 should be Black Friday")
      (is (nil? (black-friday (->day-time-context 2025 11 21)))
          "Nov 21, 2025 should NOT be Black Friday")

      ;; Edge case: November has exactly 4 Thursdays when month starts on Friday
      ;; 2024 November starts on Friday, so 4th Thursday is on 28th
      (is (true? (black-friday (->day-time-context 2024 11 29)))
          "Nov 29, 2024 should be Black Friday (edge case: Nov starts Friday)"))))

;; =============================================================================
;; EDGE CASE TESTS FOR EASTER-BASED CALCULATIONS
;; =============================================================================

(deftest easter-edge-cases
  (testing "Easter calculation edge cases - early and late Easter"
    ;; Test years with early/late Easter to catch calculation errors
    (let [easter-fn (c/compile-easter {:offset 0})]

      ;; 2024: Easter is March 31 (early Easter)
      (is (true? (easter-fn (->day-time-context 2024 3 31)))
          "Easter 2024 should be March 31")
      (is (nil? (easter-fn (->day-time-context 2024 4 7)))
          "April 7, 2024 should NOT be Easter")

      ;; 2025: Easter is April 20 (late Easter)  
      (is (true? (easter-fn (->day-time-context 2025 4 20)))
          "Easter 2025 should be April 20")
      (is (nil? (easter-fn (->day-time-context 2025 3 20)))
          "March 20, 2025 should NOT be Easter")))

  (testing "Good Friday (Easter -2) edge cases"
    (let [good-friday (c/compile-easter {:offset -2})]

      ;; 2024: Easter is March 31 -> Good Friday is March 29
      (is (true? (good-friday (->day-time-context 2024 3 29)))
          "Good Friday 2024 should be March 29")
      (is (nil? (good-friday (->day-time-context 2024 3 31)))
          "March 31, 2024 (Easter) should NOT be Good Friday")

      ;; Edge case: Good Friday in February (very early Easter)
      ;; This tests cross-month calculations
      ;; Easter 2008 was March 23, so Good Friday was March 21
      (is (true? (good-friday (->day-time-context 2008 3 21)))
          "Good Friday 2008 should be March 21")))

  (testing "Orthodox Easter vs Catholic Easter differences"
    (let [catholic-easter (c/compile-easter {:offset 0})
          orthodox-easter (c/compile-orthodox {:offset 0})]

      ;; 2024: Catholic Easter March 31, Orthodox Easter May 5
      (is (true? (catholic-easter (->day-time-context 2024 3 31)))
          "Catholic Easter 2024 should be March 31")
      (is (true? (orthodox-easter (->day-time-context 2024 5 5)))
          "Orthodox Easter 2024 should be May 5")
      (is (nil? (orthodox-easter (->day-time-context 2024 3 31)))
          "March 31, 2024 should NOT be Orthodox Easter")
      (is (nil? (catholic-easter (->day-time-context 2024 5 5)))
          "May 5, 2024 should NOT be Catholic Easter"))))

;; =============================================================================  
;; EDGE CASE TESTS FOR NTH WEEKDAY CALCULATIONS
;; =============================================================================

(deftest nth-weekday-edge-cases
  (testing "3rd Monday in February - Presidents Day edge cases"
    (let [presidents-day (c/compile-in-month
                          (parse-definition "3rd monday in February"))]

      ;; 2024: February has 29 days (leap year), 3rd Monday is Feb 19
      (is (true? (presidents-day (->day-time-context 2024 2 19)))
          "Feb 19, 2024 should be Presidents Day")
      (is (nil? (presidents-day (->day-time-context 2024 2 12)))
          "Feb 12, 2024 should NOT be Presidents Day")
      (is (nil? (presidents-day (->day-time-context 2024 2 26)))
          "Feb 26, 2024 should NOT be Presidents Day")

      ;; 2023: February has 28 days, 3rd Monday is Feb 20  
      (is (true? (presidents-day (->day-time-context 2023 2 20)))
          "Feb 20, 2023 should be Presidents Day")

      ;; Edge case: When February 1st is a Monday
      ;; 2021: Feb 1 is Monday -> 3rd Monday is Feb 15
      (is (true? (presidents-day (->day-time-context 2021 2 15)))
          "Feb 15, 2021 should be Presidents Day")))

  (testing "4th Thursday in November - Thanksgiving edge cases"
    (let [thanksgiving (c/compile-in-month
                        (parse-definition "4th thursday in November"))]

      ;; Edge case: November with exactly 4 Thursdays when month starts on Friday
      ;; 2024: November starts on Friday -> 4th Thursday is Nov 28
      (is (true? (thanksgiving (->day-time-context 2024 11 28)))
          "Nov 28, 2024 should be Thanksgiving")
      (is (nil? (thanksgiving (->day-time-context 2024 11 21)))
          "Nov 21, 2024 should NOT be Thanksgiving")

      ;; Edge case: November starting on Thursday
      ;; 2018: November starts on Thursday -> 4th Thursday is Nov 22  
      (is (true? (thanksgiving (->day-time-context 2018 11 22)))
          "Nov 22, 2018 should be Thanksgiving")

      ;; Verify there's no 5th Thursday confusion
      (is (nil? (thanksgiving (->day-time-context 2024 11 21)))
          "Nov 21, 2024 (3rd Thursday) should NOT be Thanksgiving")
      ;; Fixed: 2019 Nov 28 is actually the 4th Thursday, not 5th
      (is (true? (thanksgiving (->day-time-context 2019 11 28)))
          "Nov 28, 2019 (4th Thursday) should be Thanksgiving"))))

;; =============================================================================
;; EDGE CASE TESTS FOR COMPLEX MULTI-CONDITION RULES
;; =============================================================================

(deftest complex-multi-condition-edge-cases
  (testing "Juneteenth with complex substitution rules"
    ;; "06-19 and if sunday then next monday if saturday then previous friday since 2021"
    (let [juneteenth (c/compile-condition
                      (parse-definition "06-19 and if sunday then next monday if saturday then previous friday since 2021"))]

      ;; 2022: June 19 is Sunday -> should substitute to June 20 (Monday)
      (is (true? (juneteenth (->day-time-context 2022 6 19)))
          "June 19, 2022 (Sunday) should be Juneteenth")
      (is (true? (juneteenth (->day-time-context 2022 6 20)))
          "June 20, 2022 (Monday) should be Juneteenth due to substitution")
      (is (nil? (juneteenth (->day-time-context 2022 6 18)))
          "June 18, 2022 should NOT be Juneteenth")
      (is (nil? (juneteenth (->day-time-context 2022 6 21)))
          "June 21, 2022 should NOT be Juneteenth")

      ;; 2021: June 19 is Saturday -> should substitute to June 18 (Friday)
      (is (true? (juneteenth (->day-time-context 2021 6 19)))
          "June 19, 2021 (Saturday) should be Juneteenth")
      (is (true? (juneteenth (->day-time-context 2021 6 18)))
          "June 18, 2021 (Friday) should be Juneteenth due to substitution")
      (is (nil? (juneteenth (->day-time-context 2021 6 20)))
          "June 20, 2021 should NOT be Juneteenth")
      (is (nil? (juneteenth (->day-time-context 2021 6 21)))
          "June 21, 2021 should NOT be Juneteenth")

      ;; 2023: June 19 is Monday -> should be holiday, no substitution
      (is (true? (juneteenth (->day-time-context 2023 6 19)))
          "June 19, 2023 (Monday) should be Juneteenth")
      (is (nil? (juneteenth (->day-time-context 2023 6 18)))
          "June 18, 2023 should NOT be Juneteenth")
      (is (nil? (juneteenth (->day-time-context 2023 6 20)))
          "June 20, 2023 should NOT be Juneteenth")))

  (testing "Veterans Day with bank holiday substitution"
    ;; "substitutes 11-11 if sunday then next monday if saturday then previous friday"
    (let [veterans-day (c/compile-condition
                        (parse-definition "11-11 and if sunday then next monday if saturday then previous friday"))]

      ;; 2023: Nov 11 is Saturday -> should substitute to Nov 10 (Friday)
      (is (true? (veterans-day (->day-time-context 2023 11 11)))
          "Nov 11, 2023 (Saturday) should be Veterans Day")
      (is (true? (veterans-day (->day-time-context 2023 11 10)))
          "Nov 10, 2023 (Friday) should be Veterans Day due to substitution")
      (is (nil? (veterans-day (->day-time-context 2023 11 12)))
          "Nov 12, 2023 should NOT be Veterans Day")
      (is (nil? (veterans-day (->day-time-context 2023 11 13)))
          "Nov 13, 2023 should NOT be Veterans Day")

      ;; 2024: Nov 11 is Monday -> should be holiday, no substitution
      (is (true? (veterans-day (->day-time-context 2024 11 11)))
          "Nov 11, 2024 (Monday) should be Veterans Day")
      (is (nil? (veterans-day (->day-time-context 2024 11 10)))
          "Nov 10, 2024 should NOT be Veterans Day")
      (is (nil? (veterans-day (->day-time-context 2024 11 12)))
          "Nov 12, 2024 should NOT be Veterans Day"))))

;; =============================================================================
;; EDGE CASE TESTS FOR YEAR BOUNDARY TRANSITIONS
;; =============================================================================

(deftest year-boundary-edge-cases
  (testing "New Year's Day Saturday->Friday substitution crossing year boundary"
    (let [new-year-saturday-friday (c/compile-condition
                                    (parse-definition "01-01 and if Saturday then previous Friday"))]

      ;; 2022: Jan 1 is Saturday -> should substitute to Dec 31, 2021 (Friday)
      (is (true? (new-year-saturday-friday (->day-time-context 2022 1 1)))
          "Jan 1, 2022 (Saturday) should be New Year's Day")
      (is (true? (new-year-saturday-friday (->day-time-context 2021 12 31)))
          "Dec 31, 2021 (Friday) should be New Year's Day due to substitution")
      (is (nil? (new-year-saturday-friday (->day-time-context 2022 1 2)))
          "Jan 2, 2022 should NOT be New Year's Day")
      (is (nil? (new-year-saturday-friday (->day-time-context 2022 1 3)))
          "Jan 3, 2022 should NOT be New Year's Day")))

  (testing "December 31st with Sunday->Monday substitution crossing year boundary"
    (let [new-years-eve-substitution (c/compile-condition
                                      (parse-definition "12-31 and if Sunday then next Monday"))]

      ;; 2023: Dec 31 is Sunday -> should substitute to Jan 1, 2024 (Monday)
      (is (true? (new-years-eve-substitution (->day-time-context 2023 12 31)))
          "Dec 31, 2023 (Sunday) should be holiday")
      (is (true? (new-years-eve-substitution (->day-time-context 2024 1 1)))
          "Jan 1, 2024 (Monday) should be holiday due to substitution")
      (is (nil? (new-years-eve-substitution (->day-time-context 2024 1 2)))
          "Jan 2, 2024 should NOT be holiday"))))

;; =============================================================================
;; EDGE CASE TESTS FOR LEAP YEAR HANDLING
;; =============================================================================

(deftest leap-year-edge-cases
  (testing "February 29th in leap years"
    (let [leap-day (c/compile-static
                    (parse-definition "02-29"))]

      ;; 2024: Leap year -> Feb 29 should exist
      (is (true? (leap-day (->day-time-context 2024 2 29)))
          "Feb 29, 2024 should be valid in leap year")

      ;; 2023: Not leap year -> Feb 29 doesn't exist (this might throw or return nil)
      ;; We should test how the system handles invalid dates
      ;; Note: This test might need adjustment based on how invalid dates are handled
      ))

  (testing "February holidays in leap vs non-leap years"
    (let [presidents-day (c/compile-in-month
                          (parse-definition "3rd monday in February"))]

      ;; Test that Presidents Day calculation works correctly in both leap and non-leap years
      ;; 2024: Leap year, 3rd Monday should be Feb 19
      (is (true? (presidents-day (->day-time-context 2024 2 19)))
          "Presidents Day 2024 should be Feb 19 (leap year)")

      ;; 2023: Non-leap year, 3rd Monday should be Feb 20
      (is (true? (presidents-day (->day-time-context 2023 2 20)))
          "Presidents Day 2023 should be Feb 20 (non-leap year)"))))

;; =============================================================================
;; INTEGRATION TESTS WITH REAL HOLIDAY DEFINITIONS
;; =============================================================================

(deftest integration-edge-cases
  (testing "Real US holiday edge cases"
    ;; Load actual US holiday definitions and test edge cases
    ;; This requires the generated US holidays to be available
    ))

(deftest parse-definition-edge-cases
  (testing "Complex parsing edge cases"
    ;; Fixed: "easter -47" not "orthodox easter -47"
    (is (= (parse-definition "easter -47 and if sunday then next monday")
           {:easter? true :offset -47 :and? true
            :statements [["sunday" "then" "next" "monday"]]}))

    (is (= (parse-definition "3rd monday in February and if saturday then previous friday")
           {:nth 3 :week-day 1 :in? true :month 2 :and? true
            :statements [["saturday" "then" "previous" "friday"]]}))

    (is (= (parse-definition "friday after 4th thursday in November")
           {:predicate :after :week-day 5
            :relative-to {:nth 4 :week-day 4 :in? true :month 11}}))))

 ;; =============================================================================
;; EDGE CASE TESTS FOR PERIOD HOLIDAYS (ISO 8601 DURATION)
;; =============================================================================

(deftest period-holiday-edge-cases
  (testing "P2DT - 2 day period starting from specific date"
    (let [new-year-period (c/compile-type
                           (parse-definition "01-01 P2DT"))]

      ;; 2024: New Year period should cover Jan 1-2
      (is (true? (new-year-period (->day-time-context 2024 1 1)))
          "Jan 1, 2024 should be within New Year period")
      (is (true? (new-year-period (->day-time-context 2024 1 2)))
          "Jan 2, 2024 should be within New Year period")
      (is (nil? (new-year-period (->day-time-context 2024 1 3)))
          "Jan 3, 2024 should NOT be within New Year period")
      (is (nil? (new-year-period (->day-time-context 2023 12 31)))
          "Dec 31, 2023 should NOT be within New Year period")

      ;; Test different years
      (is (true? (new-year-period (->day-time-context 2025 1 1)))
          "Jan 1, 2025 should be within New Year period")
      (is (true? (new-year-period (->day-time-context 2025 1 2)))
          "Jan 2, 2025 should be within New Year period")))

  (testing "P2DT0H0M - 2 day period with explicit hours/minutes"
    (let [complex-period (c/compile-type
                          (parse-definition "06-01 P2DT0H0M"))]

      ;; Children's Day period June 1-2
      (is (true? (complex-period (->day-time-context 2024 6 1)))
          "June 1, 2024 should be within Children's Day period")
      (is (true? (complex-period (->day-time-context 2024 6 2)))
          "June 2, 2024 should be within Children's Day period")
      (is (nil? (complex-period (->day-time-context 2024 6 3)))
          "June 3, 2024 should NOT be within Children's Day period")
      (is (nil? (complex-period (->day-time-context 2024 5 31)))
          "May 31, 2024 should NOT be within Children's Day period")))

  (testing "Period holidays across month boundaries"
    (let [month-boundary-period (c/compile-type
                                 (parse-definition "01-31 P2DT"))]

      ;; Period starting Jan 31 should cover Jan 31 - Feb 1  
      (is (true? (month-boundary-period (->day-time-context 2024 1 31)))
          "Jan 31, 2024 should be within period")
      (is (true? (month-boundary-period (->day-time-context 2024 2 1)))
          "Feb 1, 2024 should be within period (crosses month boundary)")
      (is (nil? (month-boundary-period (->day-time-context 2024 2 2)))
          "Feb 2, 2024 should NOT be within period")))

  (testing "Period holidays across year boundaries"
    (let [year-boundary-period (c/compile-type
                                (parse-definition "12-31 P2DT"))]

      ;; Period starting Dec 31 should cover Dec 31 - Jan 1 next year
      (is (true? (year-boundary-period (->day-time-context 2023 12 31)))
          "Dec 31, 2023 should be within period")
      (is (true? (year-boundary-period (->day-time-context 2024 1 1)))
          "Jan 1, 2024 should be within period (crosses year boundary)")
      (is (nil? (year-boundary-period (->day-time-context 2024 1 2)))
          "Jan 2, 2024 should NOT be within period")))

  (testing "Leap year handling with periods"
    (let [leap-day-period (c/compile-type
                           (parse-definition "02-28 P2DT"))]

      ;; 2024 is a leap year - period should be Feb 28-29
      (is (true? (leap-day-period (->day-time-context 2024 2 28)))
          "Feb 28, 2024 (leap year) should be within period")
      (is (true? (leap-day-period (->day-time-context 2024 2 29)))
          "Feb 29, 2024 (leap day) should be within period")
      (is (nil? (leap-day-period (->day-time-context 2024 3 1)))
          "Mar 1, 2024 should NOT be within period")

      ;; 2023 is not a leap year - period should be Feb 28 - Mar 1
      (is (true? (leap-day-period (->day-time-context 2023 2 28)))
          "Feb 28, 2023 (non-leap year) should be within period")
      (is (true? (leap-day-period (->day-time-context 2023 3 1)))
          "Mar 1, 2023 should be within period (no leap day)")
      (is (nil? (leap-day-period (->day-time-context 2023 3 2)))
          "Mar 2, 2023 should NOT be within period")))

  (testing "Period parsing edge cases"
    ;; Test various ISO 8601 duration formats
    (is (= (parse-definition "01-01 P2DT")
           {:day-in-month 1 :month 1 :period {:days 2 :hours nil :minutes nil}})
        "P2DT should parse correctly")

    (is (= (parse-definition "01-01 P3DT2H5M")
           {:day-in-month 1 :month 1 :period {:days 3 :hours 2 :minutes 5}})
        "P3DT2H5M should parse correctly")

    (is (= (parse-definition "01-01 P1DT0H0M")
           {:day-in-month 1 :month 1 :period {:days 1 :hours 0 :minutes 0}})
        "P1DT0H0M should parse correctly")))

 ;; =============================================================================
;; EDGE CASE TESTS FOR ISLAMIC CALENDAR HOLIDAYS
;; =============================================================================

(deftest islamic-calendar-edge-cases
  (testing "Islamic calendar date parsing"
    ;; Test basic Islamic calendar date parsing
    (is (= (parse-definition "10 Muharram")
           {:islamic? true :day-in-month 10 :month 1})
        "10 Muharram should parse correctly")

    (is (= (parse-definition "1 Shawwal")
           {:islamic? true :day-in-month 1 :month 10})
        "1 Shawwal should parse correctly")

    (is (= (parse-definition "21 Ramadan")
           {:islamic? true :day-in-month 21 :month 9})
        "21 Ramadan should parse correctly")

    (is (= (parse-definition "12 Dhu al-Hijjah")
           {:islamic? true :day-in-month 12 :month 12})
        "12 Dhu al-Hijjah should parse correctly"))

  (testing "Islamic calendar holidays - major celebrations"
    (let [ashura (c/compile-type (parse-definition "10 Muharram"))
          eid-fitr (c/compile-type (parse-definition "1 Shawwal"))
          eid-adha (c/compile-type (parse-definition "10 Dhu al-Hijjah"))]

      ;; Test known Islamic dates (approximate Gregorian equivalents)
      ;; Note: Islamic calendar is lunar, so dates shift each year

      ;; Test Ashura (10 Muharram) - July 17, 2024 = 10 Muharram 1446
      (let [ashura-ctx (->day-time-context 2024 7 17)]
        (is (true? (ashura ashura-ctx))
            "10 Muharram should be recognized as Ashura"))

      ;; Test Eid Fitr (1 Shawwal) - April 10, 2024 = 1 Shawwal 1445
      (let [eid-fitr-ctx (->day-time-context 2024 4 10)]
        (is (true? (eid-fitr eid-fitr-ctx))
            "1 Shawwal should be recognized as Eid Fitr"))

      ;; Test Eid Adha (10 Dhu al-Hijjah) - June 17, 2024 = 10 Dhu al-Hijjah 1445
      (let [eid-adha-ctx (->day-time-context 2024 6 17)]
        (is (true? (eid-adha eid-adha-ctx))
            "10 Dhu al-Hijjah should be recognized as Eid Adha"))))

  (testing "Islamic calendar year boundaries"
    ;; Islamic calendar has 354-355 days, so it shifts relative to Gregorian
    (let [muharram-1 (c/compile-type (parse-definition "1 Muharram"))]

    ;; 1 Muharram is Islamic New Year - July 8, 2024 = 1 Muharram 1446
      (let [islamic-new-year-ctx (->day-time-context 2024 7 8)]
        (is (true? (muharram-1 islamic-new-year-ctx))
            "1 Muharram should be recognized as Islamic New Year"))))

  (testing "Islamic month name variations"
    ;; Test different spellings/variations of Islamic month names
    (is (= (parse-definition "15 Jumada al-awwal")
           {:islamic? true :day-in-month 15 :month 5})
        "Jumada al-awwal should parse correctly")

    (is (= (parse-definition "27 Rajab")
           {:islamic? true :day-in-month 27 :month 7})
        "Rajab should parse correctly")

    (is (= (parse-definition "15 Sha'ban")
           {:islamic? true :day-in-month 15 :month 8})
        "Sha'ban should parse correctly"))

  (testing "Islamic calendar with other date patterns"
    ;; Test Islamic dates combined with other patterns (should not interfere)
    (is (= (parse-definition "julian 01-01")
           {:julian? true :day-in-month 1 :month 1})
        "Julian dates should still work with Islamic support")

    (is (= (parse-definition "01-01 P2DT")
           {:day-in-month 1 :month 1 :period {:days 2 :hours nil :minutes nil}})
        "Period dates should still work with Islamic support")))
