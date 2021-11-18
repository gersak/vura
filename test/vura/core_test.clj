(ns vura.core-test
  (:refer-clojure :exclude [second])
  (:use clojure.test
        vura.core)
  (:require 
    [vura.timezones.db :as db]
    [clojure.data :as data])
  (:import
    [java.time ZonedDateTime ZoneId]))

(def positive-number (rand 100000))
(def negative-number (rand -10000))


(deftest test-round-zero
  (is (every? zero? (map #(round-number positive-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value")
  (is (every? zero? (map #(round-number negative-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value"))

(deftest test-round-number
  (testing "Rounding numbers? Aye"
    (are [x y] (== (round-number 10.75 0.5 x) y)
         :ceil 11.0
         :floor 10.5
         :up 11.0
         :down 10.5)
    (are [x y] (== (round-number 10.76 0.5 x) y)
         :ceil 11.0
         :floor 10.5
         :up 11.0
         :down 11.0)
    (are [x y] (== (round-number -10.75 0.5 x) y)
         :ceil -10.5
         :floor -11
         :up -10.5
         :down -11)
    (are [x y] (== (round-number -10.76 0.5 x) y)
         :ceil -10.5
         :floor -11
         :up -11.0
         :down -11.0)
    (is (== (round-number 10.12345681928 0.5) 10.0) "Rounding failed")
    ;; Think about this JS/Java Clojure implementation limitation
    ; (is (== (round-number 10.12345681928 0.1) 10.1) "Rounding failed")
    (is (== (round-number 122 3) 123) "Rounding failed")
    (is (== (round-number 10.12345681928 0.125) 10.125) "Rounding failed")))


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
    (is (= 23 (hour? (time->value (date 2018 3 25 23 59 59 999)))))
    (is (= 59 (minute? (time->value (date 2018 3 25 23 59 59 999)))))
    (is (= 59 (second? (time->value (date 2018 3 25 23 59 59 999)))))
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
    (with-time-configuration
      {:timezone "Europe/Zagreb"}
      (let [[summer-before summer-at summer-after] [(date 2018 3 25 1 59 59 999)
                                                    (date 2018 3 25 2)
                                                    (date 2018 3 25 3)]
            [winter-before winter-at] [(date 2018 10 28 2 59 59 999)
                                       (date 2018 10 28 3)]]

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

        (is 
          (= 
            (java.util.Date/from (.toInstant (ZonedDateTime/of 2018 10 28 2 59 0 0 (ZoneId/of "Europe/Zagreb"))))
            (date 2018 10 28 2 59 0 0)))

        (is 
          (= 
            (java.util.Date/from (.toInstant (ZonedDateTime/of 2018 10 28 3 0 0 0 (ZoneId/of "Europe/Zagreb"))))
            (date 2018 10 28 3 0 0 0)))))))


(deftest Teleportation
  (let [target (date 2018 7 6 12)
        t1 (with-time-configuration
             {:timezone "Europe/Zagreb"}
             target)
        t2 (with-time-configuration
             {:timezone "Asia/Tokyo"}
             target)
        t3 (with-time-configuration
             {:timezone "America/New_York"}
             target)]
    (is (= t1 t2 t3) "Every time is same time")
    (is
      (= 
        {:value 1530903600000, :hour 19, :minute 0, :second 0, :millisecond 0}
        (with-time-configuration 
          {:timezone "Asia/Tokyo"}
          (select-keys (day-time-context (date->value target)) [:value :hour :minute :second :millisecond]))
        (select-keys 
          (day-time-context
            (teleport
              (date->value target)
              "Asia/Tokyo"))
          [:value :hour :minute :second :millisecond])))
    (is
      (=
       {:value 1530856800000, :hour 6, :minute 0, :second 0, :millisecond 0}
       (with-time-configuration
         {:timezone "America/New_York"}
         (select-keys (day-time-context (date->value target)) [:value :hour :minute :second :millisecond]))
       (select-keys
         (day-time-context
           (teleport 
             (date->value target)
             "America/New_York"))
         [:value :hour :minute :second :millisecond])))))


(deftest CalendarAlgorithms
  "Testing calendar algorithms"
  (let [value (-> (date 1900 1 1) time->value)
        context {:year 1900
                 :month 1
                 :day 1}]
    (is (== value (-> value value->gregorian-date gregorian-date->value)) "Gregorian algorithm is not bidirectional")
    (is (== value (-> value value->julian-date julian-date->value)) "Julian algorithm is not bidirectional")
    (is (== value (-> value value->islamic-date islamic-date->value)) "Islamic algorithm is not bidirectional")
    (is (== value (-> value value->hebrew-date hebrew-date->value)) "Hebrew algorithm is not bidirectional")))


(deftest JulianCalendar
  (with-time-configuration {:calendar :julian}
    (let [value (-> (date 1900 2 1) time->value)
          {:keys [day day-in-month week
                  year month days-in-month]
           :as context} (day-time-context value)]
      (is (== year 1900) "Wrong year")
      (is (== month 2) "Wrong month")
      (is (== day-in-month 1) "Wrong date")
      (is (== day 2) "Wrong day")
      (is (== week 6) "Wrong week")
      (is (== days-in-month 29) "Wrong leap year count"))))


(deftest GregorianCalendar
  (with-time-configuration {:calendar :gregorian}
    (let [value (-> (date 1900 2 1) time->value)
          {:keys [day day-in-month week
                  year month days-in-month]
           :as context} (day-time-context value)]
      (is (== year 1900) "Wrong year")
      (is (== month 2) "Wrong month")
      (is (== day-in-month 1) "Wrong date")
      (is (== day 4) "Wrong day")
      (is (== week 5) "Wrong week")
      (is (== days-in-month 28) "Wrong leap year count"))))


(deftest IslamicCalendar
  (let [value (-> (date 1900 1 1) time->value)
        months {1 "Muharram"
                2 "Safar"
                3 "Rabi al-Awwal"
                4 "Rabi al Thani"
                5 "Jumada al-Ula"
                6 "Jumada al-Akhirah"
                7 "Rajab"
                8 "Sha'ban"
                9 "Ramadan"
                10 "Shawwal"
                11 "Zulqiddah"
                12 "Zulhijjah"}]
    (with-time-configuration {:calendar :islamic}
      (let [{:keys [day day-in-month week
                    year month days-in-month]
             :as context} (day-time-context value)
            context (assoc context :month/name (months month))]
        (is (== year 1317) "Wrong year")
        (is (== month 8) "Wrong month")
        (is (== day-in-month 28) "Wrong date")
        (is (== day 1) "Wrong day")
        (is (== week 35) "Wrong week")
        (is (== days-in-month 29) "Wrong number of days in month")))))


(deftest HebrewCalendar
  (let [value (-> (date 1948 1 1) time->value)]
    (with-time-configuration {:calendar :hebrew}
      (let [{:keys [day day-in-month week
                    year month days-in-month]
             :as context} (day-time-context value)]
        ; (week-in-year? value)
        (println context)
        (is (== year 5708) "Wrong year")
        (is (== month 10) "Wrong month")
        (is (== day-in-month 19) "Wrong date")
        (is (== day 4) "Wrong day")
        ; (is (== week 35) "Wrong week")
        (is (== days-in-month 29) "Wrong number of days in month")))))


(deftest BidirectionalContext
  (let [gregorian-context {:year 1900 :month 1 :day-in-month 1}
        julian-context {:year 1900 :month 1 :day-in-month 1}
        islamic-context {:year 1317 :month 8 :day-in-month 28}
        hebrew-context {:year 5708 :month 10 :day-in-month 19}]
    (with-time-configuration {:calendar :gregorian} 
      (is (= gregorian-context 
             (select-keys 
               (-> 
                 gregorian-context 
                 context->value 
                 day-time-context) 
               [:year :month :day-in-month]))))
    (with-time-configuration {:calendar :julian} 
      (is (= julian-context 
             (select-keys 
               (-> 
                 julian-context 
                 context->value 
                 day-time-context) 
               [:year :month :day-in-month]))))
    (with-time-configuration {:calendar :islamic}
      (is (= islamic-context 
             (select-keys 
               (-> 
                 islamic-context 
                 context->value 
                 day-time-context) 
               [:year :month :day-in-month]))))
    (with-time-configuration {:calendar :hebrew}
      (is (= hebrew-context 
             (select-keys 
               (-> 
                 hebrew-context 
                 context->value 
                 day-time-context) 
               [:year :month :day-in-month]))))))

; (deftest TimezoneCoverage
;   (let [java-zones (set (TimeZone/getAvailableIDs))
;         vura-zones (set (keys (:zones db/db)))
;         [java vura _] (data/diff java-zones vura-zones)]
;     (sort java)))
