(ns timing.holidays-test
  (:require
   [timing.core :as v]
   [clojure.test
    :refer [deftest is]]
   [timing.holiday.compiler :as c]
   [timing.holiday.util :refer [parse-definition]]))

(defn ->day-time-context
  [year month day]
  (let [dt (-> (v/date year month day)
               v/time->value
               v/day-time-context)]
    dt))

(deftest holiday-definition-parsing
  (is (= (parse-definition "orthodox")
         {:orthodox? true, :offset 0}))
  (is (= (parse-definition "orthodox -47")
         {:orthodox? true, :offset -47}))
  (is (= (parse-definition "orthodox and if sunday then next monday")
         {:orthodox? true, :offset 0, :and? true, :statements ['("sunday" "then" "next" "monday")]}))
  (is (= (parse-definition "easter")
         {:easter? true, :offset 0}))
  (is (= (parse-definition "easter 26")
         {:easter? true, :offset 26}))
  (is (= (parse-definition "easter -2 prior to 2019")
         {:easter? true, :offset -2, :unknown ["prior" "to" "2019"]}))
  (is (= (parse-definition "easter -6 P7D") {:easter? true, :offset -6, :period {:days 7, :hours nil, :minutes nil}}))
  (is (= (parse-definition "julian 06-15")
         {:julian? true, :day-in-month 15, :month 6}))
  (is (= (parse-definition "julian 12-25 and if saturday, sunday then next monday")
         {:julian? true, :day-in-month 25, :month 12, :and? true, :statements ['("saturday,sunday" "then" "next" "monday")]}))
  (is (= (parse-definition "julian 12-25 P2D") {:julian? true, :day-in-month 25, :month 12, :period {:days 2, :hours nil, :minutes nil}})))

(deftest before-after-tests
  (let [monday-before-september (c/compile-before-after {:week-day 1,
                                                         :predicate :before,
                                                         :relative-to {:month 9}})
        third-monday-after-12-25 (c/compile-before-after {:nth 3,
                                                          :week-day 1,
                                                          :predicate :after,
                                                          :relative-to {:day-in-month 25, :month 12}})
        second-monday-before-first-tuesday-in-january (c/compile-before-after {:nth 2,
                                                                               :week-day 1,
                                                                               :predicate :before,
                                                                               :relative-to {:nth 1, :week-day 2, :in? true, :month 1}})
        first-monday-before-06-01 (c/compile-before-after {:nth 1,
                                                           :week-day 1,
                                                           :predicate :before,
                                                           :relative-to {:day-in-month 1, :month 6}})
        first-monday-after-10-12 (c/compile-before-after {:week-day 1,
                                                          :predicate :after,
                                                          :relative-to {:day-in-month 12, :month 10}})
        third-saturday-after-06-01 (c/compile-before-after {:nth 3,
                                                            :week-day 6,
                                                            :predicate :after,
                                                            :relative-to {:day-in-month 1, :month 6}})
        third-sunday-before-12-25 (c/compile-before-after {:nth 3,
                                                           :week-day 7,
                                                           :predicate :before,
                                                           :relative-to {:day-in-month 25, :month 12}})
        saturday-after-10-21 (c/compile-before-after {:week-day 6,
                                                      :predicate :after,
                                                      :relative-to {:day-in-month 21, :month 10}})
        friday-after-4th-thursday-in-november (c/compile-before-after {:week-day 5,
                                                                       :predicate :after,
                                                                       :relative-to {:nth 4, :week-day 4, :in? true, :month 11}})
        monday-after-07-01 (c/compile-before-after {:week-day 1,
                                                    :predicate :after,
                                                    :relative-to {:day-in-month 1, :month 7}})
        first-sunday-before-12-25 (c/compile-before-after {:nth 1,
                                                           :week-day 7,
                                                           :predicate :before,
                                                           :relative-to {:day-in-month 25, :month 12}})
        monday-before-06-20 (c/compile-before-after {:week-day 1,
                                                     :predicate :before,
                                                     :relative-to {:day-in-month 20, :month 6}})
        tuesday-after-2nd-monday-in-august (c/compile-before-after {:week-day 2,
                                                                    :predicate :after,
                                                                    :relative-to {:nth 2, :week-day 1, :in? true, :month 8}})
        tuesday-after-1st-monday-in-november (c/compile-before-after {:week-day 2,
                                                                      :predicate :after,
                                                                      :relative-to {:nth 1, :week-day 1, :in? true, :month 11},
                                                                      :unknown ["every" "4" "years" "since" "1848"]})]
    (is (not (true? (monday-before-september (->day-time-context 2022 9 1)))))
    (is (not (true? (monday-before-september (->day-time-context 2022 8 31)))))
    (is (not (true? (monday-before-september (->day-time-context 2022 8 30)))))
    (is (true? (monday-before-september (->day-time-context 2022 8 29))))
    (is (not (true? (monday-before-september (->day-time-context 2022 8 28)))))
    (is (not (true? (monday-before-september (->day-time-context 2022 9 5)))))
    (is (not (true? (monday-before-september (->day-time-context 2023 9 5)))))
    (is (not (true? (monday-before-september (->day-time-context 2023 9 1)))))
    (is (not (true? (monday-before-september (->day-time-context 2023 9 4)))))
    (is (not (true? (monday-before-september (->day-time-context 2023 8 29)))))
    (is (true? (monday-before-september (->day-time-context 2023 8 28))))
    (is (not (true? (monday-before-september (->day-time-context 2023 8 21)))))
    (is (not (true? (monday-before-september (->day-time-context 2026 9 7)))))
    (is (not (true? (monday-before-september (->day-time-context 2026 9 1)))))
    (is (true? (monday-before-september (->day-time-context 2026 8 31))))
    (is (not (true? (monday-before-september (->day-time-context 2026 8 24)))))

    (is (not (true? (third-monday-after-12-25 (->day-time-context 2022 12 25)))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2022 12 26)))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2023 1 2)))))
    (is (true? (third-monday-after-12-25 (->day-time-context 2023 1 9))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2023 1 16)))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2026 12 25)))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2026 12 28)))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2027 1 4)))))
    (is (true? (third-monday-after-12-25 (->day-time-context 2027 1 11))))
    (is (not (true? (third-monday-after-12-25 (->day-time-context 2027 1 18)))))

    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2022 1 1)))))
    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2022 1 4)))))
    (is (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2021 12 27))))
    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2022 12 20)))))
    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2025 1 1)))))
    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2025 1 6)))))
    (is (not (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2025 1 7)))))
    (is (true? (second-monday-before-first-tuesday-in-january (->day-time-context 2024 12 30))))

    (is (not (true? (first-monday-before-06-01 (->day-time-context 2022 06 1)))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2022 05 31)))))
    (is (true? (first-monday-before-06-01 (->day-time-context 2022 05 30))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2022 05 29)))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2019 06 01)))))
    (is (true? (first-monday-before-06-01 (->day-time-context 2019 05 27))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2019 05 20)))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2019 06 03)))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2027 06 01)))))
    (is (true? (first-monday-before-06-01 (->day-time-context 2027 05 31))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2027 05 24)))))
    (is (not (true? (first-monday-before-06-01 (->day-time-context 2027 06 07)))))

    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 12)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 13)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 14)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 15)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 16)))))
    (is (true? (first-monday-after-10-12 (->day-time-context 2022 10 17))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2022 10 18)))))
    (is (true? (first-monday-after-10-12 (->day-time-context 2020 10 12))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2020 10 13)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2020 10 11)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2020 10 19)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2020 10 5)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2028 10 12)))))
    (is (true? (first-monday-after-10-12 (->day-time-context 2028 10 16))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2028 10 9)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2028 10 23)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2028 10 11)))))
    (is (not (true? (first-monday-after-10-12 (->day-time-context 2028 10 15)))))

    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2022 06 01)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2022 06 02)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2022 06 04)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2022 06 11)))))
    (is (true? (third-saturday-after-06-01 (->day-time-context 2022 06 18))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2022 06 25)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2024 06 1)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2024 06 8)))))
    (is (true? (third-saturday-after-06-01 (->day-time-context 2024 06 15))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2024 06 22)))))
    (is (not (true? (third-saturday-after-06-01 (->day-time-context 2024 06 29)))))

    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2022 12 25)))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2022 12 18)))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2022 12 11)))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2022 12 3)))))
    (is (true? (third-sunday-before-12-25 (->day-time-context 2022 12 4))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2022 12 5)))))
    (is (true? (third-sunday-before-12-25 (->day-time-context 2023 12 10))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2025 12 25)))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2025 12 21)))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2025 12 14)))))
    (is (true? (third-sunday-before-12-25 (->day-time-context 2025 12 7))))
    (is (not (true? (third-sunday-before-12-25 (->day-time-context 2025 11 30)))))

    (is (not (true? (saturday-after-10-21 (->day-time-context 2022 10 21)))))
    (is (true? (saturday-after-10-21 (->day-time-context 2022 10 22))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2022 10 29)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2022 10 15)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2025 10 21)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2025 10 22)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2025 10 23)))))
    (is (true? (saturday-after-10-21 (->day-time-context 2025 10 25))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2025 11 1)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2025 10 18)))))
    (is (true? (saturday-after-10-21 (->day-time-context 2028 10 21))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2028 10 28)))))
    (is (not (true? (saturday-after-10-21 (->day-time-context 2028 10 14)))))

    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2022 11 24)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2022 11 25))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2022 12 2)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2022 11 18)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2024 11 28)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2024 11 29))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2024 12 6)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2024 11 22)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2018 11 22)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2018 11 23))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2018 11 30)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2018 11 16)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2019 11 28)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2019 11 29))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2019 11 22)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2019 12 6)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2020 11 26)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2020 11 27))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2020 11 20)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2020 12 4)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2021 11 25)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2021 11 26))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2021 11 19)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2021 12 3)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2023 11 23)))))
    (is (true? (friday-after-4th-thursday-in-november (->day-time-context 2023 11 24))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2023 11 17)))))
    (is (not (true? (friday-after-4th-thursday-in-november (->day-time-context 2023 12 1)))))

    (is (not (true? (monday-after-07-01 (->day-time-context 2022 7 1)))))
    (is (true? (monday-after-07-01 (->day-time-context 2022 7 4))))
    (is (not (true? (monday-after-07-01 (->day-time-context 2022 7 11)))))
    (is (true? (monday-after-07-01 (->day-time-context 2024 7 1))))
    (is (not (true? (monday-after-07-01 (->day-time-context 2024 7 8)))))
    (is (not (true? (monday-after-07-01 (->day-time-context 2029 7 1)))))
    (is (true? (monday-after-07-01 (->day-time-context 2029 7 2))))
    (is (not (true? (monday-after-07-01 (->day-time-context 2029 7 9)))))

    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2022 12 25)))))
    (is (true? (first-sunday-before-12-25 (->day-time-context 2022 12 18))))
    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2022 12 11)))))
    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2024 12 25)))))
    (is (true? (first-sunday-before-12-25 (->day-time-context 2024 12 22))))
    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2024 12 15)))))
    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2019 12 25)))))
    (is (true? (first-sunday-before-12-25 (->day-time-context 2019 12 22))))
    (is (not (true? (first-sunday-before-12-25 (->day-time-context 2019 12 15)))))

    (is (not (true? (monday-before-06-20 (->day-time-context 2022 6 20)))))
    (is (true? (monday-before-06-20 (->day-time-context 2022 6 13))))
    (is (not (true? (monday-before-06-20 (->day-time-context 2025 6 20)))))
    (is (true? (monday-before-06-20 (->day-time-context 2025 6 16))))
    (is (not (true? (monday-before-06-20 (->day-time-context 2025 6 9)))))
    (is (not (true? (monday-before-06-20 (->day-time-context 2019 6 20)))))
    (is (true? (monday-before-06-20 (->day-time-context 2019 6 17))))
    (is (not (true? (monday-before-06-20 (->day-time-context 2019 6 10)))))
    (is (not (true? (monday-before-06-20 (->day-time-context 2019 6 24)))))

    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2019 8 12)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2019 8 13))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2019 8 6)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2019 8 20)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2019 8 27)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2020 8 10)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2020 8 11))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2020 8 4)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2020 8 18)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2020 8 25)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2021 8 9)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2021 8 10))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2021 8 3)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2021 8 17)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2021 8 24)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2022 8 8)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2022 8 9))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2022 8 2)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2022 8 16)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2022 8 23)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2023 8 14)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2023 8 15))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2023 8 8)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2023 8 22)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2023 8 29)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2024 8 12)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2024 8 13))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2024 8 6)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2024 8 20)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2024 8 27)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2025 8 11)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2025 8 12))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2025 8 5)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2025 8 19)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2025 8 26)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2026 8 10)))))
    (is (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2026 8 11))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2026 8 4)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2026 8 18)))))
    (is (not (true? (tuesday-after-2nd-monday-in-august (->day-time-context 2026 8 25)))))

    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2021 11 1)))))
    (is (true? (tuesday-after-1st-monday-in-november (->day-time-context 2021 11 2))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2021 11 9)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2021 11 16)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2021 10 26)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2022 11 1)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2022 11 7)))))
    (is (true? (tuesday-after-1st-monday-in-november (->day-time-context 2022 11 8))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2022 11 15)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2022 10 25)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 11 1)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 11 6)))))
    (is (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 11 7))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 11 14)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 11 21)))))
    (is (not (true? (tuesday-after-1st-monday-in-november (->day-time-context 2023 10 31)))))))

(deftest condition-tests
  (let [new-year? (c/compile-condition
                   {:day-in-month 1,
                    :month 1,
                    :and? true,
                    :statements
                    '({:today #{6}, :condition "previous", :target 5}
                      {:today #{7}, :condition "next", :target 1})})
        holiday-04-25? (c/compile-condition
                        {:day-in-month 25,
                         :month 4,
                         :and? true,
                         :statements '({:today #{7 6}, :condition "next", :target 1})})
        holiday-10-21? (c/compile-condition
                        {:day-in-month 21,
                         :month 10,
                         :statements
                         '({:today #{7}, :condition "next", :target 1}
                           {:today #{6}, :condition "previous", :target 5}
                           {:today #{3 2}, :condition "previous", :target 1}
                           {:today #{4}, :condition "next", :target 5})})]
    (is (true? (new-year? (->day-time-context 2021 12 31))) "31.12.2021 is Friday and it should be holiday")
    (is (true? (new-year? (->day-time-context 2022 1 1))) "1.1.2022 is Sunday and it is holiday because of and condition")
    (is (nil? (new-year? (->day-time-context 2022 1 2))) "1.1.2022 is Sunday and it is holiday because of and condition")
    (is (nil? (new-year? (->day-time-context 2022 1 3))) "3.1.2022 is Monday and it is not holiday. Previous Friday was.")
    (is (nil? (new-year? (->day-time-context 2022 12 31))) "31.12.2022 is Saturday and it NOT holiday")
    (is (true? (new-year? (->day-time-context 2023 1 1))) "1.1.2023 is Sunday and it is holiday because of and condition")
    (is (true? (new-year? (->day-time-context 2023 1 2))) "2.1.2023 is Monday and it is not holiday. Previous Friday was.")
    (is (nil? (new-year? (->day-time-context 2023 1 3))) "3.1.2023 is Tuesday and it si NOT holiday")

    (is (true? (holiday-04-25? (->day-time-context 2020 4 25))) "25.4.2020 is Saturday and is holiday because of and condition")
    (is (nil? (holiday-04-25? (->day-time-context 2020 4 26))) "26.4.2020 is Sunday and is not a holiday")
    (is (true? (holiday-04-25? (->day-time-context 2020 4 27))) "27.4.2020 is Monday and is holiday because of statement")
    (is (true? (holiday-04-25? (->day-time-context 2022 4 25))) "25.4.2021 is Monday and is holiday")
    (is (nil? (holiday-04-25? (->day-time-context 2022 4 26))) "26.4.2021 is Tuesday and not a holiday")

    (is (nil? (holiday-10-21? (->day-time-context 2021 10 21))) "21.10.2021 is Thursday and not a holiday because of statement")
    (is (true? (holiday-10-21? (->day-time-context 2021 10 22))) "22.10.2021 is Friday and should be holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2021 10 23))) "23.10.2021 is Saturday and not a holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2021 10 29))) "29.10.2021 is Friday and not a holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2023 10 21))) "21.10.2021 is Saturday and not a holiday because of statement")
    (is (nil? (holiday-10-21? (->day-time-context 2023 10 22))) "22.10.2021 is Sunday and not a holiday")
    (is (true? (holiday-10-21? (->day-time-context 2023 10 20))) "20.10.2021 is Friday and should be a holiday")))