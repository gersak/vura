(ns vura.holidays.example
  (:require
   [clojure.string :as str]
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
  (fn [{d :day-in-month
        wd :day
        m :month
        value :value}]
    (some
      (fn [{:keys [today condition target]}]
        ; (println "Current day: " [d m wd])
        ; (println "Static: " (dissoc record :statements))
        ; (println "Statement: " [today condition target])
        (or
          (and
            (= month m)
            (= day-in-month d)
            (or (not (today wd)) and?))
          (and
            ;; Check if current week day matches target from statment
            (= wd target)
            (case condition 
              "next"
              (some
                (fn [target]
                  ;; Current week day is in front of condition so find difference of current week day and today (statements)
                  ;; in days
                  (let [next-delta (+ (- 7 target) wd)
                        ;; subtract computed value from current date value and get day time context
                        {d :day-in-month m :month} (v/day-time-context (- value (v/days next-delta)))]
                    ;; Check if result day in month and month matches initial condition
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
                    (and
                      (= month m)
                      (= day-in-month d))))
                today)))))
      statements)))


(comment
  (list 1 2 3)
  '(1 2 3)
  (test-compile (->day-time-context 2020 4 25))
  (test-compile (->day-time-context 2020 4 26))
  (test-compile (->day-time-context 2020 4 27))
  (test-compile (->day-time-context 2021 12 31))
  (test-compile (->day-time-context 2022 1 1))
  (test-compile (->day-time-context 2022 1 2))
  (test-compile (->day-time-context 2023 1 2))
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
  (def test-compile
    (compile-holiday
      {:day-in-month 1,
       :month 1,
       :and? true,
       :statements
       '({:today #{6}, :condition "previous", :target 5}
        {:today #{7}, :condition "next", :target 1})}))
  (spit "transformed_statements.edn"
        (with-out-str
          (clojure.pprint/pprint removed-unknown)))
  (frequencies (map :unknown parsed-results))
  (spit "static_with_condition.edn"
        (with-out-str
          (clojure.pprint/pprint (:static_condition report))))
  (-> parsed-results first)
  (parse-if "01-01 and if sunday then next monday if saturday then previous friday")
  (holiday-01-26-if (->day-time-context 2022 01 26))
  (holiday-01-26-if (->day-time-context 2022 01 25))
  (holiday-01-26-if (->day-time-context 2022 01 24))
  (holiday-01-26-if (->day-time-context 2022 01 27))
  (holiday-01-26-if (->day-time-context 2022 01 28))
  (holiday-01-26-if (->day-time-context 2022 01 29))
  (holiday-01-26-if (->day-time-context 2022 01 30))

  (holiday-01-26-if (->day-time-context 2023 01 26))
  (holiday-01-26-if (->day-time-context 2023 01 25))
  (holiday-01-26-if (->day-time-context 2023 01 24))
  (holiday-01-26-if (->day-time-context 2023 01 23))
  (holiday-01-26-if (->day-time-context 2023 01 27))
  (holiday-01-26-if (->day-time-context 2023 01 28))
  (holiday-01-26-if (->day-time-context 2023 01 29))
  (holiday-01-26-if (->day-time-context 2023 01 30)))



(defn holiday-2nd-monday-may
  "2nd monday in May"
  [{:keys [month day-in-month day]}]
  (and
   (= day 1)
   (= month 5)
   (and (> day-in-month 7) (< day-in-month 15))))

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