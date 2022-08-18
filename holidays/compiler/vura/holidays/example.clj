(ns vura.holidays.example
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [clojure.test :refer [deftest is] :as test]
    [vura.holidays :as h]
    [vura.core :as v]
    [vura.holidays.catholic :as catholic]))


(defn holiday [day-context]
  (some
   (fn [[k v]] (when (k day-context) v))
   {catholic/easter? {:name "USKRS"}
    catholic/christmas? {:name "BOZIC"}
    (h/static-holiday 1 5) {:name "NEKI BLAGDAN"}
    (h/static-holiday 1 1) {:name "NEKI BLAGDAN2"}
    (h/static-holiday 8 10) {:name "NEKI BLAGDAN3"}
    (h/static-holiday 26 12) {:name "NEKI BLAGDAN4"}}))


(defmethod h/is-holiday? "HR" [_ day-context] (holiday day-context))


(defn ->day-time-context
  [year month day]
  (let [dt (-> (v/date year month day)
               v/time->value
               v/day-time-context)]
    dt))


(defn parse-if
  [text]
  (loop [[word & words] (str/split
                         (str/lower-case
                          (str/replace text #",\s+" ","))
                         #"\s+")
         result nil]
    ;(println "Word: " word)
    ;(println "Words: " words)
    ;(println "Result: " result)
    (if (nil? word) result
        (condp re-find word
          #"\d+-\d+"
          (recur words
                 (let [split-date (str/split word #"(-|#)")
                       day (Integer/parseInt (split-date 1))
                       month (Integer/parseInt (split-date 0))]
                   (assoc result
                          :day-in-month day
                          :month month)))
          ;;
          #"if$"
          (let [statement (take-while #(not= "if" %) words)]
            ;(println "STATEMENT:" statement)
            (recur
             (drop (count statement) words)
             (update result :statements (fnil conj []) statement)))
          ;;
          #"and$"
          (recur words (assoc result :and? true))
          ;;
          (recur
           words
           (update result :unknown (fnil conj []) word))))))


(defn day-name->num
  [day]
  ; (println "WTF: " day)
  (case day
    "monday" 1
    "tuesday" 2
    "wednesday" 3
    "thursday" 4
    "friday" 5
    "saturday" 6
    "sunday" 7
    nil))


(defn analyze-statement
  [[today _ condition target]]
  {:today (set (map day-name->num (str/split today #",")))
   :condition condition
   :target (day-name->num target)})


(defn transform-statement [parsed-result]
  (update parsed-result :statements
          (fn [statements]
            (map analyze-statement statements))))


(defn compile-holiday
  [{:keys [day-in-month
           month
           and?
           statements]}]
  ;; rezultat
  (let [forbidden-days (reduce set/union (map :today statements))
        allowed-days (complement forbidden-days)
        valid-targets (set (map :target statements))]
    (fn [{d :day-in-month
          wd :day
          m :month
          value :value}]
      (some
        (fn [{:keys [today condition target] :as statement}]
          (or
            (and
              (= month m)
              (= day-in-month d)
              (or (allowed-days wd) and?))
            (and
              ;; Check if current week day matches target from statment
              (contains? valid-targets wd)
              (case condition
                "next"
                (some
                  (fn [target]
                    ;; Current week day is in front of condition so find difference of current week day and today (statements)
                    ;; in days
                    (let [next-delta (if (< wd target)
                                       (+ (- 7 target) wd)
                                       (- wd target))
                          ;; subtract computed value from current date value and get day time context
                          {d :day-in-month m :month} (v/day-time-context (- value (v/days next-delta)))]
                      ;; Check if result day in month and month matches initial condition
                      (tap>
                        {:statement statement
                         :delta/next next-delta
                         :current/day wd
                         :target/day d
                         :condition/day day-in-month})
                      (and
                        (= month m)
                        (= d day-in-month))))
                  today)
                ;; Same as above with different delta computation
                "previous"
                (some
                  (fn [target]
                    (let [previous-delta (- target wd)
                          {d :day-in-month m :month} (v/day-time-context (+ value (v/days previous-delta)))]
                      (tap>
                        {:statement statement
                         :delta/previous previous-delta
                         :current/day wd
                         :target/day d
                         :condition/day day-in-month})
                      (and
                        (= month m)
                        (= day-in-month d))))
                  today)))))
        statements))))


(deftest new-year
  (let [new-year? (compile-holiday
                    {:day-in-month 1,
                     :month 1,
                     :and? true,
                     :statements
                     '({:today #{6}, :condition "previous", :target 5}
                       {:today #{7}, :condition "next", :target 1})})]
    (is (true? (new-year? (->day-time-context 2021 12 31))) "31.12.2021 is Friday and it should be holiday")
    (is (true? (new-year? (->day-time-context 2022 1 1))) "1.1.2022 is Sunday and it is holiday because of and condition")
    (is (nil? (new-year? (->day-time-context 2022 1 2))) "1.1.2022 is Sunday and it is holiday because of and condition")
    (is (nil? (new-year? (->day-time-context 2022 1 3))) "3.1.2022 is Monday and it is not holiday. Previous Friday was.")
    (is (nil? (new-year? (->day-time-context 2022 12 31))) "31.12.2022 is Saturday and it NOT holiday")
    (is (true? (new-year? (->day-time-context 2023 1 1))) "1.1.2023 is Sunday and it is holiday because of and condition")
    (is (true? (new-year? (->day-time-context 2023 1 2))) "2.1.2023 is Monday and it is not holiday. Previous Friday was.")
    (is (nil? (new-year? (->day-time-context 2023 1 3))) "3.1.2023 is Tuesday and it si NOT holiday")))


(comment
  (clojure-version)
  (test/run-test new-year)
  (add-tap println)
  (def holidays (clojure.edn/read-string (slurp "all_holidays.edn")))
  (def report (group-by vura.holidays.compile/analyze-holiday holidays))
  (def parsed-results (map parse-if (get report :static_condition)))
  (def transformed-statements (map transform-statement parsed-results))
  (def removed-unknown
    (remove #(or
              (some? (:unknown %))
              (empty? (:statements %)))
            transformed-statements))
  (def test-compile (compile-holiday (first removed-unknown)))
  (def new-year?
    (compile-holiday
     {:day-in-month 1,
      :month 1,
      :and? true,
      :statements
      '({:today #{6}, :condition "previous", :target 5}
        {:today #{7}, :condition "next", :target 1})}))
  (def boxing-day?
    (compile-holiday
     {:day-in-month 26,
      :month 12,
      :and? true,
      :statements
      '({:today #{6}, :condition "next", :target 1}
        {:today #{7}, :condition "next", :target 2})}))
  (def 
    (compile-holiday
     {:day-in-month 26,
      :month 1,
      :statements
      '({:today #{3 2}, :condition "previous", :target 1}
        {:today #{4 5}, :condition "next", :target 1})}))
  (def test-compile
    (compile-holiday
     {:day-in-month 21,
      :month 10,
      :statements
      '({:today #{7}, :condition "next", :target 1}
        {:today #{6}, :condition "previous", :target 5}
        {:today #{3 2}, :condition "previous", :target 1}
        {:today #{4}, :condition "next", :target 5})}))
  (def test-compile
    (compile-holiday
     {:day-in-month 6,
      :month 11,
      :statements '({:today #{4 6 3 2 5}, :condition "next", :target 1})}))
  ;; Holiday 04-25 and if Saturday,Sunday then next monday
  (test-compile (->day-time-context 2020 4 25))
  (test-compile (->day-time-context 2020 4 26))
  (test-compile (->day-time-context 2020 4 27))
  (test-compile (->day-time-context 2022 4 24))
  (test-compile (->day-time-context 2022 4 25))
  (test-compile (->day-time-context 2022 4 26))
  ;; Holiday 12-26 and if Saturday then next monday if Sunday then next tuesday
  (test-compile (->day-time-context 2022 12 26))
  (test-compile (->day-time-context 2023 12 26))
  (test-compile (->day-time-context 2021 12 26))
  (test-compile (->day-time-context 2021 12 27))
  (test-compile (->day-time-context 2021 12 28))
  (test-compile (->day-time-context 2021 12 29))
  (test-compile (->day-time-context 2020 12 26))
  (test-compile (->day-time-context 2020 12 27))
  (test-compile (->day-time-context 2020 12 28))
  (test-compile (->day-time-context 2020 12 29))
  ;; Holiday 01-26 if tuesday,wednsday then previous monday if thursday,friday then next monday
  (test-compile (->day-time-context 2025 01 24))
  (test-compile (->day-time-context 2025 01 25))
  (test-compile (->day-time-context 2025 01 26))
  (test-compile (->day-time-context 2025 01 27))
  (test-compile (->day-time-context 2025 01 28))
  (test-compile (->day-time-context 2024 01 26)) ;; true (vaća true a trebalo bi nil)
  (test-compile (->day-time-context 2024 01 27))
  (test-compile (->day-time-context 2024 01 28))
  (test-compile (->day-time-context 2024 01 29)) ;; true (samo ovo treba biti true)
  (test-compile (->day-time-context 2024 01 30))
  (test-compile (->day-time-context 2021 01 26)) ;; true (treba biti nil)
  (test-compile (->day-time-context 2021 01 25)) ;; true (samo ovo treba biti true)
  (test-compile (->day-time-context 2021 01 24))
  (test-compile (->day-time-context 2022 01 26)) ;; true (isti slučaj kao gore)
  (test-compile (->day-time-context 2022 01 25))
  (test-compile (->day-time-context 2022 01 24)) ;; true (samo ovo treba biti true)
  (test-compile (->day-time-context 2022 01 23))
  ;; Holiday 10-21 if sunday then next monday if saturday then previous friday
  ;; if tuesday,wednsday then previous monday if thursday then next friday
  (test-compile (->day-time-context 2022 10 20))
  (test-compile (->day-time-context 2022 10 21))
  (test-compile (->day-time-context 2022 10 22))
  (test-compile (->day-time-context 2023 10 21))
  (test-compile (->day-time-context 2023 10 20))
  (test-compile (->day-time-context 2023 10 22))
  (test-compile (->day-time-context 2023 10 23))
  (test-compile (->day-time-context 2021 10 15))
  (test-compile (->day-time-context 2021 10 20))
  (test-compile (->day-time-context 2021 10 21)) ;; true (ovdje isti slučaj kao gore)
  (test-compile (->day-time-context 2021 10 22)) ;; nil (ovo bi trebalo biti true (21. je thursday a ovo je next friday))
  (test-compile (->day-time-context 2021 10 23))
  (test-compile (->day-time-context 2021 10 29)) ;; true (ovo vraća true, a zapravo je drugi friday iza thursdaya)
  (test-compile (->day-time-context 2021 10 30))
  ;; Holiday 11-06 if tuseday,wedsnday,thursday,friday,saturday then next monday
  (test-compile (->day-time-context 2022 11 06)) ;; Za ovaj blagdan dobro radi "and" jer ima samo jedan statement
  (test-compile (->day-time-context 2022 11 07))
  (test-compile (->day-time-context 2023 11 06))
  (test-compile (->day-time-context 2024 11 06))
  (test-compile (->day-time-context 2024 11 07))
  (test-compile (->day-time-context 2024 11 8))
  (test-compile (->day-time-context 2024 11 9))
  (test-compile (->day-time-context 2024 11 10))
  (test-compile (->day-time-context 2024 11 11))
  (test-compile (->day-time-context 2026 11 06))
  (test-compile (->day-time-context 2026 11 07))
  (test-compile (->day-time-context 2026 11 8))
  (test-compile (->day-time-context 2026 11 9))
  (test-compile (->day-time-context 2026 11 10))

  (spit "transformed_statements.edn" (with-out-str (clojure.pprint/pprint removed-unknown)))
  (spit "transformed_statements.edn"
        (with-out-str
          (clojure.pprint/pprint removed-unknown)))
  (frequencies (map :unknown parsed-results))
  (spit "static_with_condition.edn"
        (with-out-str
          (clojure.pprint/pprint (:static_condition report))))
  (-> parsed-results first)
  (parse-if "01-01 and if sunday then next monday if saturday then previous friday")



  (defn holiday-2nd-monday-may
    "2nd monday in May"
    [{:keys [month day-in-month day]}]
    (and
     (= day 1)
     (= month 5)
     (and (> day-in-month 7) (< day-in-month 15))))
  )

(comment
  (holiday-2nd-monday-may (->day-time-context 2022 05 01))
  (holiday-2nd-monday-may (->day-time-context 2022 05 02))
  (holiday-2nd-monday-may (->day-time-context 2022 05 03))
  (holiday-2nd-monday-may (->day-time-context 2022 05 04))
  (holiday-2nd-monday-may (->day-time-context 2022 05 05))
  (holiday-2nd-monday-may (->day-time-context 2022 05 06))
  (holiday-2nd-monday-may (->day-time-context 2022 05 07))
  (holiday-2nd-monday-may (->day-time-context 2022 05 8))
  (holiday-2nd-monday-may (->day-time-context 2022 05 9))
  (holiday-2nd-monday-may (->day-time-context 2022 05 10))
  (holiday-2nd-monday-may (->day-time-context 2022 05 16)))



(defn holiday-3rd-monday-november
  "3rd monday in November"
  [{:keys [month day-in-month day]}]
  (and
   (= day 1)
   (= month 11)
   (and (> day-in-month 14) (< day-in-month 22))))

(comment
  (holiday-3rd-monday-november (->day-time-context 2022 11 01))
  (holiday-3rd-monday-november (->day-time-context 2022 11 02))
  (holiday-3rd-monday-november (->day-time-context 2022 11 03))
  (holiday-3rd-monday-november (->day-time-context 2022 11 8))
  (holiday-3rd-monday-november (->day-time-context 2022 11 9))
  (holiday-3rd-monday-november (->day-time-context 2022 11 10))
  (holiday-3rd-monday-november (->day-time-context 2022 11 15))
  (holiday-3rd-monday-november (->day-time-context 2022 11 16))
  (holiday-3rd-monday-november (->day-time-context 2022 11 17))
  (holiday-3rd-monday-november (->day-time-context 2022 11 20))
  (holiday-3rd-monday-november (->day-time-context 2022 11 21))
  (holiday-3rd-monday-november (->day-time-context 2022 11 22))
  (holiday-3rd-monday-november (->day-time-context 2022 12 19))
  (holiday-3rd-monday-november (->day-time-context 2026 11 15))
  (holiday-3rd-monday-november (->day-time-context 2026 11 16))
  (holiday-3rd-monday-november (->day-time-context 2026 11 23))
  (->day-time-context 2022 12 -32))



(defn holiday-2nd-sunday-before-12-25
  "2nd sunday before 12-25"
  [{:keys [month day-in-month day]}]
  (and
   (= day 7)
   (= month 12)
   (and (> day-in-month 7) (< day-in-month 15))))

(comment
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 01))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 02))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 03))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 04))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 10))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 11))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 16))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 17))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 18))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 19))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 25))
  (holiday-2nd-sunday-before-12-25 (->day-time-context 2022 12 26)))