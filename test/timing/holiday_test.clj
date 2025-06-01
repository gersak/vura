(ns timing.holiday-test
  "Tests for holiday functionality - tests that verify the holiday API works"
  (:require [clojure.test :refer :all]
            timing.holiday.all
            [timing.core :as v]
            [timing.holiday :as h]))

(deftest holiday-api-structure
  "Test basic holiday API structure and error handling"
  (testing "Unknown country dispatch throws exception"
    (is (thrown? Exception (h/? :unknown-country (v/date 2023 7 4)))
        "Unknown country should throw exception with descriptive message"))

  (testing "Holiday dispatch function exists"
    (is (fn? h/dispatch) "Holiday dispatch function should exist")
    (is (fn? h/?) "Holiday ? function should exist"))

  (testing "Holiday multimethod is-holiday? exists"
    (is (var? #'h/is-holiday?) "is-holiday? multimethod should exist")))

(deftest holiday-context-creation
  "Test that holiday context is created correctly"
  (testing "Day time context creation"
    (let [test-date (v/date 2023 7 4)
          context (v/day-time-context (v/time->value test-date))]
      (is (map? context) "Context should be a map")
      (is (contains? context :year) "Context should contain year")
      (is (contains? context :month) "Context should contain month")
      (is (contains? context :day-in-month) "Context should contain day-in-month")
      (is (= 2023 (:year context)) "Year should be 2023")
      (is (= 7 (:month context)) "Month should be 7")
      (is (= 4 (:day-in-month context)) "Day in month should be 4")))

  (testing "Context with timezone configuration"
    (v/with-time-configuration {:timezone "America/New_York"}
      (let [context (v/day-time-context (v/time->value (v/date 2023 7 4 12 0 0)))]
        (is (map? context) "Context should be a map in timezone configuration")
        (is (= 2023 (:year context)) "Year should be preserved")))))

(deftest holiday-integration-with-time-context
  "Test holiday integration with time context and configuration"
  (testing "Holiday function integration with context"
    (let [test-fn (fn [_] false)] ; Mock holiday function
      (v/with-time-configuration {:holiday? test-fn}
        (let [context (v/day-time-context (v/time->value (v/date 2023 7 4)))]
          (is (contains? context :holiday?) "Context should contain holiday flag")
          (is (false? (:holiday? context)) "Holiday function should be called")))))

  (testing "Weekend and holiday configuration together"
    (let [mock-holiday-fn (fn [_] true)] ; Always returns true for testing
      (v/with-time-configuration {:timezone "America/New_York"
                                  :weekend-days #{6 7}
                                  :holiday? mock-holiday-fn}
        (let [context (v/day-time-context (v/time->value (v/date 2023 7 1)))] ; Saturday
          (is (:weekend? context) "Should detect weekend")
          (is (:holiday? context) "Should detect holiday with mock function"))))))

(deftest date-and-time-utilities
  "Test date and time utilities work with holiday system"
  (testing "Date creation and conversion"
    (let [test-date (v/date 2023 12 25)
          time-value (v/time->value test-date)
          back-to-date (v/value->time time-value)]
      (is (inst? test-date) "Date should be an instance")
      (is (number? time-value) "Time value should be numeric")
      (is (inst? back-to-date) "Converted back should be an instance")))

  (testing "Calendar context extraction"
    (let [christmas (v/date 2023 12 25)
          context (v/day-time-context (v/time->value christmas))]
      (is (= 25 (:day-in-month context)) "Should extract day-in-month correctly")
      (is (= 12 (:month context)) "Should extract month correctly")
      (is (= 2023 (:year context)) "Should extract year correctly")
      (is (number? (:days-in-year context)) "Should calculate days in year")
      (is (number? (:week context)) "Should calculate week of year"))))

(deftest timezone-handling
  "Test timezone handling in holiday context"
  (testing "Different timezones produce correct contexts"
    (v/with-time-configuration {:timezone "Europe/London"}
      (let [context (v/day-time-context (v/time->value (v/date 2023 6 15 12 0 0)))]
        (is (= 2023 (:year context)) "Year should be correct in London timezone")
        (is (= 6 (:month context)) "Month should be correct in London timezone")))

    (v/with-time-configuration {:timezone "America/Los_Angeles"}
      (let [context (v/day-time-context (v/time->value (v/date 2023 6 15 12 0 0)))]
        (is (= 2023 (:year context)) "Year should be correct in LA timezone")
        (is (= 6 (:month context)) "Month should be correct in LA timezone"))))

  (testing "Timezone teleportation"
    (let [ny-time (v/time->value (v/date 2023 6 15 12 0 0))
          london-time (v/teleport ny-time "Europe/London")]
      (is (number? london-time) "Teleported time should be numeric")
      (is (not= ny-time london-time) "Times should be different after teleport"))))

(deftest weekend-detection
  "Test weekend detection works correctly"
  (testing "Weekend configuration and detection"
    (v/with-time-configuration {:weekend-days #{6 7}} ; Saturday, Sunday
      (let [monday-context (v/day-time-context (v/time->value (v/date 2023 6 19))) ; Monday
            saturday-context (v/day-time-context (v/time->value (v/date 2023 6 17)))] ; Saturday
        (is (not (:weekend? monday-context)) "Monday should not be weekend")
        (is (:weekend? saturday-context) "Saturday should be weekend"))))

  (testing "Custom weekend days"
    (v/with-time-configuration {:weekend-days #{5 6}} ; Friday, Saturday
      (let [friday-context (v/day-time-context (v/time->value (v/date 2023 6 16))) ; Friday
            sunday-context (v/day-time-context (v/time->value (v/date 2023 6 18)))] ; Sunday
        (is (:weekend? friday-context) "Friday should be weekend with custom config")
        (is (not (:weekend? sunday-context)) "Sunday should not be weekend with custom config")))))

(deftest actual-holiday-testing
  "Test actual country-specific holidays now that compiler is available"
  (testing "US holidays"
    (is (h/? :us (v/date 2023 7 4)) "July 4th 2023 should be a US holiday (Independence Day)")
    (is (h/? :us (v/date 2023 12 25)) "December 25th 2023 should be a US holiday (Christmas)")
    (is (h/? :us (v/date 2023 1 1)) "January 1st 2023 should be a US holiday (New Year's Day)")
    (is (not (h/? :us (v/date 2023 3 15))) "March 15th should not be a US holiday")
    (is (not (h/? :us (v/date 2023 8 20))) "August 20th should not be a US holiday"))

  (testing "UK holidays"
    (is (h/? :gb (v/date 2023 12 25)) "Christmas should be a UK holiday")
    (is (h/? :gb (v/date 2023 12 26)) "Boxing Day should be a UK holiday")
    (is (h/? :gb (v/date 2023 1 1)) "New Year's Day should be a UK holiday"))

  (testing "German holidays"
    (is (h/? :de (v/date 2023 12 25)) "Christmas should be a German holiday")
    (is (h/? :de (v/date 2023 1 1)) "New Year's Day should be a German holiday"))

  (testing "Cross-country comparison"
    (let [july4 (v/date 2023 7 4)]
      (is (h/? :us july4) "July 4th should be US holiday")
      (is (not (h/? :gb july4)) "July 4th should NOT be UK holiday")
      (is (not (h/? :de july4)) "July 4th should NOT be German holiday")))

  (testing "Basic holiday integration"
    ;; Test the basic holiday functionality without complex time configuration
    (let [july4-holiday (h/? :us (v/date 2023 7 4))
          july5-holiday (h/? :us (v/date 2023 7 5))]
      (is (some? july4-holiday) "July 4th should return holiday data")
      (is (nil? july5-holiday) "July 5th should not be a holiday"))

    ;; Test with different countries
    (let [christmas-us (h/? :us (v/date 2023 12 25))
          christmas-gb (h/? :gb (v/date 2023 12 25))
          christmas-de (h/? :de (v/date 2023 12 25))]
      (is (some? christmas-us) "Christmas should be US holiday")
      (is (some? christmas-gb) "Christmas should be UK holiday")
      (is (some? christmas-de) "Christmas should be German holiday"))))

(deftest holiday-time-configuration-integration
  "Test proper holiday integration with time configuration"
  (testing "Simple holiday function integration"
    ;; Create a simple wrapper that works with the context format
    (letfn [(us-holiday-checker [context]
              (let [date (v/value->time (:value context))]
                (boolean (h/? :us date))))]
      (v/with-time-configuration {:holiday? us-holiday-checker}
        (let [july4-context (v/day-time-context (v/time->value (v/date 2023 7 4)))
              july5-context (v/day-time-context (v/time->value (v/date 2023 7 5)))]
          (is (:holiday? july4-context) "July 4th should be marked as holiday")
          (is (not (:holiday? july5-context)) "July 5th should not be marked as holiday"))))))

(deftest tax-day-fix-verification
  "Test that Tax Day parsing works correctly with comma-separated days"
  (testing "Tax Day parsing with complex conditions"
    ;; The rule: "04-15 if friday then next monday if saturday,sunday then next tuesday"
    ;; When April 15 falls on weekend, it moves to the following Monday

    ;; 2023: April 15 was Saturday, so Tax Day is April 17 (Monday)
    (let [tax-day-2023 (h/? :us (v/date 2023 4 17)) ; Correct date: April 17, not 15
          tax-day-2024 (h/? :us (v/date 2024 4 15))] ; April 15, 2024 was Monday

      (is (some? tax-day-2023) "Tax Day 2023 should be recognized on April 17")
      (is (some? tax-day-2024) "Tax Day 2024 should be recognized on April 15")

      ;; Use direct name access since h/name function has issues
      (is (= "Tax Day" (get-in tax-day-2023 [:name :en])) "2023 should be Tax Day")
      (is (= "Tax Day" (get-in tax-day-2024 [:name :en])) "2024 should be Tax Day")

      ;; Verify that April 15, 2023 (Saturday) is NOT Tax Day
      (let [not-tax-day-2023 (h/? :us (v/date 2023 4 15))]
        (is (nil? not-tax-day-2023) "April 15, 2023 (Saturday) should NOT be Tax Day"))))

  (testing "Tax Day weekend shifting rules"
    ;; Test the rule logic across multiple years
    (let [test-cases {2020 {:date [4 15] :expected true} ; Weekday
                      2021 {:date [4 15] :expected true} ; Weekday  
                      2022 {:date [4 18] :expected true} ; 4/15 was Friday -> 4/18 Monday
                      2023 {:date [4 17] :expected true} ; 4/15 was Saturday -> 4/17 Monday
                      2024 {:date [4 15] :expected true} ; Weekday
                      2025 {:date [4 15] :expected true}}] ; Weekday

      (doseq [[year {:keys [date expected]}] test-cases]
        (let [test-date (apply v/date year date)
              tax-day-result (h/? :us test-date)]
          (if expected
            (do
              (is (some? tax-day-result) (str "Tax Day should exist for " year))
              (when tax-day-result
                (is (= "Tax Day" (get-in tax-day-result [:name :en]))
                    (str "Should be Tax Day for " year))))
            (is (nil? tax-day-result) (str "Should NOT be Tax Day for " year))))))))
