(ns vura.holidays.example
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :refer [deftest is] :as test]
   [vura.holidays :as h]
   [vura.core :as v]
   [vura.holidays.catholic :as catholic]
   [vura.holidays.ortodox :as ortodox]))


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


(defn month-name->num
  [month]
  (case month
    "january" 1
    "february" 2
    "march" 3
    "april" 4
    "may" 5
    "june" 6
    "july" 7
    "august" 8
    "september" 9
    "october" 10
    "november" 11
    "december" 12
    nil))


(defn parse-definition
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
          #"orthodox"
          (let [[offset-word & words] words
                offset-number (if (contains? #{"and" nil} offset-word)
                                0
                                (Integer/parseInt offset-word))]
            (recur (if (= offset-word "and")
                     (cons offset-word words)
                     words)
                   (assoc result
                          :orthodox? true
                          :offset offset-number)))
          ;;
          #"easter"
          (let [[offset-word & words] words
                offset-number (if (contains? #{"and" nil} offset-word)
                                0
                                (Integer/parseInt offset-word))]

            (recur (if (= offset-word "and")
                     (cons offset-word words)
                     words)
                   (assoc result
                          :easter? true
                          :offset offset-number)))
          ;;
          #"julian"
          (recur words
                 (assoc result
                        :julian? true))
          ;;
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
          #"\d[a-z]{2}"
          (recur words (assoc result :nth (Integer/parseInt ((str/split word #"") 0))))
          ;;
          #"(monday|tuesday|wednesday|thursday|friday|saturday|sunday)"
          (recur words (assoc result :week-day (day-name->num word)))
          ;;
          #"^in$"
          (let [[target-month & words] words
                target-month-number (month-name->num target-month)
                results (if (nil? target-month-number) ;; ????
                          (assoc result
                                 :unknown [target-month])
                          (assoc result
                                 :in? true
                                 :month target-month-number))]
            (recur words results))
          ;;
          #"(before|after)"
          (let [predicate (if (= word "before") :before :after)
                temp (take-while #(nil? (re-find #"(\d+-\d+|january|february|march|april|may|june|july|august|september|october|november|december)" %)) words)
                n (+ (count temp) 1)
                what (take n words)]
            (recur (drop (count what) words)
                   (assoc result
                          :predicate predicate
                          :relative-to (parse-definition (str/join " " what)))))
          ;;
          #"(january|february|march|april|may|june|july|august|september|october|november|december)"
          (recur words (assoc result :month (month-name->num word)))
          ;;
          (recur
           words
           (update result :unknown (fnil conj []) word))))))

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
                   {::statement statement
                    ::delta next-delta
                    ::current wd
                    ::target d
                    ::condition day-in-month})
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
                   {::statement statement
                    ::delta previous-delta
                    ::current wd
                    ::target d
                    ::condition day-in-month})
                  (and
                   (= month m)
                   (= day-in-month d))))
              today)))))
       statements))))


(defn compile-easter
  [offset]
  (fn [{y :year
        value :value}]
    (let [easter (catholic/easter y)
          easter-d (:day-in-month easter)
          easter-m (:month easter)
          easter-value (:value (->day-time-context y easter-m easter-d))
          easter-offset-value (:value (v/day-time-context (+ easter-value (v/days offset))))]
      (= value easter-offset-value))))


(defn orthodox-easter [year]
  (let [a (mod year 4)
        b (mod year 7)
        c (mod year 19)
        d (mod (+ (* 19 c) 15) 30)
        e (mod (- (+ (* 2 a) (* 4 b) 34) d) 7)
        month (quot (+ d e 114) 31)
        day (+ 14 (mod (+ d e 114) 31))
        day-overload (quot day 30)]
    (if (pos? day-overload)
      {:year year :month (inc month) :day-in-month (mod day 30)}
      {:year year :month month :day-in-month day})))


(defn compile-orthodox
  [{:keys [offset
           and?
           statements]}]
  (fn [{d :day-in-month
        m :month
        y :year
        value :value}]
    (let [orthodox (orthodox-easter y)
          orthodox-d (:day-in-month orthodox)
          orthodox-m (:month orthodox)
          orthodox-value (:value (->day-time-context y orthodox-m orthodox-d))
          orthodox-offset-context (v/day-time-context (+ orthodox-value (v/days offset)))]
      (if (empty? statements)
        (= value (:value orthodox-offset-context))
        ((compile-holiday {:day-in-month (:day-in-month orthodox-offset-context)
                           :month (:month orthodox-offset-context)
                           :and? and?
                           :statements statements}) (->day-time-context y m d))))))

(comment
  (def orthodox-8 (compile-orthodox {:orthodox? true, :offset 8, :statements ()}))
  (orthodox-8 (->day-time-context 2023 4 16))
  (orthodox-8 (->day-time-context 2023 4 23))
  (orthodox-8 (->day-time-context 2023 4 24))
  (orthodox-8 (->day-time-context 2023 4 25))
  (def orthodox-0
    (compile-orthodox
     {:orthodox? true, :offset 0, :statements ()}))
  (orthodox-0 (->day-time-context 2023 4 15))
  (orthodox-0 (->day-time-context 2023 4 16))
  (orthodox-0 (->day-time-context 2023 4 17))

  (def orthodox-0-if
    (compile-orthodox
     {:orthodox? true, :offset 0, :and? true, :statements '({:today #{7}, :condition "next", :target 1})}))
  (orthodox-0-if (->day-time-context 2023 4 15))
  (orthodox-0-if (->day-time-context 2023 4 16))
  (orthodox-0-if (->day-time-context 2023 4 17))
  (orthodox-0-if (->day-time-context 2023 4 18)))

#_(defn compile-julian
    [definition]
    (println "Cale`ndar: " v/*calendar*)
    (let [{:keys [day-in-month
                  month
                  year]}
          (->
           (v/with-time-configuration
             {:calendar :julian}
             (v/context->value definition))
           v/day-time-context)]
      (println "ZAPRAVO GADAM: " [month day-in-month year])
      (fn [{d :day-in-month
            m :month
            y :year
            value :value}]
        (let [julian-context (v/value->julian-date value)
              julian-m (:month julian-context)
              julian-d (:day-in-month julian-context)]
          (and
           (= day-in-month julian-d)
           (= month julian-m))))))

(defn compile-julian
  [{:keys [day-in-month
           month]}]
  (fn [{value :value}]
    (let [{d :day-in-month
           m :month}
          (v/with-time-configuration
            {:calendar :julian}
            (v/day-time-context value))]
      (and
       (= day-in-month d)
       (= month m)))))


; (defn compile-in-month
;   [{:keys [nth
;            week-day
;            month]}]
;   (fn [{d :day-in-month
;         wd :day
;         m :month}]
;     (and
;      (= m month)
;      (= wd week-day)
;      (>= d (+ (* (- nth 1) 7) 1))
;      (<= d (* nth 7)))))


; (defn get-nth-week-day-in-month
;   [year month week-day nth]
;   (some
;    (fn [day-in-month]
;      (let [context (->day-time-context year month day-in-month)
;            wd (:day context)
;            m (:month context)]
;        (if (and (= wd week-day) (= month m))
;          context
;          nil)))
;    (range (+ (* (- nth 1) 7) 1) (* nth 7))))


(comment
  (def day-in-month 1)
  (def month 9)
  (def year 2022)
  (def value (:value (->day-time-context year month day-in-month)))
  (def predicate :before)
  (def predicate :after)
  (nth
   (filter
      ;; ovdje ide uvjet... Tj. uvjet za filter
    (fn [{:keys [day]}] (= day 1))
    (map
     v/day-time-context
     (iterate
      #((case predicate
          :before -
          :after +)
        % v/day)
      value)))
   3)
  (vura/date)
  (v/day-time-context (first-week-day 1 :after value)))


(defn first-week-day
  [week-day predicate value]
  (when-some [{:keys [value]} (first
                               (filter
                                (fn [{:keys [day]}] (= day week-day))
                                (map
                                 (fn [value]
                                   {:day (v/day? value)
                                    :value value})
                                 (iterate
                                  #((case predicate
                                      :before -
                                      :after +)
                                    % v/day)
                                  value))))]
    value))


(defn same-day?
  [a b]
  (let [ks [:day-in-month
            :month
            :year]]
    (= (select-keys a ks) (select-keys b ks))))


(defn compile-before-after
  [{:keys [predicate]
    _week-day :week-day
    _nth :nth
    {rel-month :month
     rel-nth :nth
     rel-week-day :week-day
     rel-day-in-month :day-in-month
     in? :in?
     :or {rel-day-in-month 1}} :relative-to
    :or {_nth 1
         rel-nth 1}
    :as definition}]
  (letfn [(->nth
           [value _nth]
           (let [before? (= :before predicate)
                 after? (not before?)
                 first? (= _nth 1)]
             (cond
               (and first? before?)
               (+ value v/week)
               ;;
               (and first? after?)
               value
               ;;
               :else
               (+ value (v/weeks (if before?
                                   (* -1 (dec _nth))
                                   (dec _nth)))))))]
    (fn [{:keys [value]
          week-day :day}]
      ;; Only if weekday matches
      (when (= week-day _week-day)
        ;; If specific day-in-month is present
        (cond
          ;; relative to nth day in month
          in?
          (let [inverse-predicate (case predicate :before :after :before)
                relative-value (first-week-day _week-day inverse-predicate value)
                target-value (->nth relative-value _nth)
                relative-target-value (first-week-day rel-week-day inverse-predicate target-value)
                rctx (v/day-time-context relative-target-value)]
            (println "REL: " rel-nth)
            (println "Relative context: " rctx)
            (and
             (= (:month rctx) rel-month)
             (= (:day rctx) rel-week-day)
             (<=
              (inc (* 7 (dec rel-nth)))
              (:day-in-month rctx)
              (* 7 rel-nth))))
          ;; relative to static date
          (and rel-day-in-month rel-month)
          (let [target (v/day-time-context (->nth value _nth))]
            (println "Target: " target)
            (println "Days: " (:day-in-month target) rel-day-in-month)
            (println "sfajio" [predicate (<= (- (:day-in-month target) rel-day-in-month) 7)])
            (and
             (= (:month target) rel-month)
             (case predicate
               :after (let [diff (- (:day-in-month target) rel-day-in-month)]
                        (and (pos? diff) (<= diff 7)))
               :before (let [diff (- (:day-in-month target) rel-day-in-month)]
                         (and (pos? diff) (<= diff 7))))))
          ;; Otherwise check based on relative week-day
          :else (throw (ex-info "Unknown definition" definition)))))))


(comment
  (def monday-before-september
    (compile-before-after {:week-day 1, :predicate :before, :relative-to {:month 9}}))
  (monday-before-september (->day-time-context 2022 9 1))
  (monday-before-september (->day-time-context 2022 8 31))
  (monday-before-september (->day-time-context 2022 8 30))
  (monday-before-september (->day-time-context 2022 8 29)) ;; error!
  (monday-before-september (->day-time-context 2022 8 28))

  (def monday-before-september
    (compile-before-after {:week-day 1, :predicate :before, :relative-to {:month 9 :day-in-month 1}}))
  (monday-before-september (->day-time-context 2022 9 1))
  (monday-before-september (->day-time-context 2022 8 31))
  (monday-before-september (->day-time-context 2022 8 30))
  (monday-before-september (->day-time-context 2022 8 29)) ;; error!
  (monday-before-september (->day-time-context 2022 9 5))
  (monday-before-september (->day-time-context 2022 8 28))

  (def third-monday-after-12-25
    (compile-before-after {:nth 3,
                           :week-day 1,
                           :predicate :after,
                           :relative-to {:day-in-month 25, :month 12}}))
  (third-monday-after-12-25 (->day-time-context 2022 12 25)) ;; ne radi
  (third-monday-after-12-25 (->day-time-context 2022 12 26))
  (third-monday-after-12-25 (->day-time-context 2023 1 2))
  (third-monday-after-12-25 (->day-time-context 2023 1 9))
  (third-monday-after-12-25 (->day-time-context 2023 1 16))

  (def second-monday-before-first-tuesday-in-january
    (compile-before-after {:nth 2,
                           :week-day 1,
                           :predicate :before,
                           :relative-to {:nth 1, :week-day 2, :in? true, :month 1}}))
  (second-monday-before-first-tuesday-in-january (->day-time-context 2022 1 1)) ;; ne radi
  (second-monday-before-first-tuesday-in-january (->day-time-context 2022 1 4))
  (second-monday-before-first-tuesday-in-january (->day-time-context 2021 12 27))
  (second-monday-before-first-tuesday-in-january (->day-time-context 2021 12 20)))


(comment
  (ortodox/orthodox-easter {:year 2022})
  (def first-monday-before-06-01
    (compile-before-after {:nth 1,
                           :week-day 1,
                           :predicate :before,
                           :relative-to {:day-in-month 1, :month 6}}))
  (first-monday-before-06-01 (->day-time-context 2022 06 1))
  (first-monday-before-06-01 (->day-time-context 2022 05 31))
  (first-monday-before-06-01 (->day-time-context 2022 05 30))
  (first-monday-before-06-01 (->day-time-context 2022 05 29))

  (def first-monday-after-10-12
    (compile-before-after {:week-day 1,
                           :predicate :after,
                           :relative-to {:day-in-month 12, :month 10}}))
  (first-monday-after-10-12 (->day-time-context 2022 10 12))
  (first-monday-after-10-12 (->day-time-context 2022 10 13))
  (first-monday-after-10-12 (->day-time-context 2022 10 14))
  (first-monday-after-10-12 (->day-time-context 2022 10 15))
  (first-monday-after-10-12 (->day-time-context 2022 10 16))
  (first-monday-after-10-12 (->day-time-context 2022 10 17))

  (def third-saturday-after-06-01
    (compile-before-after {:nth 3,
                           :week-day 6,
                           :predicate :after,
                           :relative-to {:day-in-month 1, :month 6}}))
  (third-saturday-after-06-01 (->day-time-context 2022 06 01))
  (third-saturday-after-06-01 (->day-time-context 2022 06 02))
  (third-saturday-after-06-01 (->day-time-context 2022 06 04))
  (third-saturday-after-06-01 (->day-time-context 2022 06 11))
  (third-saturday-after-06-01 (->day-time-context 2022 06 18)) ;; krivo!
  (third-saturday-after-06-01 (->day-time-context 2022 06 25)) ;; !!!!

  (def third-sunday-before-12-25
    (compile-before-after {:nth 3,
                           :week-day 7,
                           :predicate :before,
                           :relative-to {:day-in-month 25, :month 12}}))
  (third-sunday-before-12-25 (->day-time-context 2022 12 25))
  (third-sunday-before-12-25 (->day-time-context 2022 12 18))
  (third-sunday-before-12-25 (->day-time-context 2022 12 11))
  (time (third-sunday-before-12-25 (->day-time-context 2022 12 3)))
  (time (third-sunday-before-12-25 (->day-time-context 2022 12 4)))
  (time (third-sunday-before-12-25 (->day-time-context 2022 12 5)))







  (def first-monday-in-august
    (compile-before-after {:nth 1, :week-day 1, :in? true, :month 8}))
  (first-monday-in-august (->day-time-context 2022 8 1)) ;; error
  (first-monday-in-august (->day-time-context 2021 8 1))
  (first-monday-in-august (->day-time-context 2021 8 2)))


(comment
  (v/day-time-context (first-week-day 7 :after (-> (v/date 2022 12 25) v/time->value)))
  (def first-wednesday-after-first-monday-in-august
    (compile-before-after
     {:week-day 3,
      :predicate :after,
      :relative-to {:nth 1, :week-day 1, :in? true, :month 8}}))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2022 7 31))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2022 8 1))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2022 8 2))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2022 8 3))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2023 8 4))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2023 8 1))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2023 8 8))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2023 8 9))
  (first-wednesday-after-first-monday-in-august (->day-time-context 2023 8 10))




  (def christmas?
    (compile-julian
     {:julian? true,
      :day-in-month 25,
      :month 12, :and? true,
      :statements ['("sunday" "then" "next" "monday")]}))
  (christmas? (->day-time-context 2022 1 7))
  (christmas? (->day-time-context 2022 12 25))
  (christmas? (->day-time-context 2022 1 6))
  (christmas? (->day-time-context 2022 1 8))

  (def is-julian-6-15
    (compile-julian
     {:julian? true,
      :day-in-month 15,
      :month 6}))
  (is-julian-6-15 (->day-time-context 2022 6 28)))


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


(deftest holiday-04-25
  (let [holiday-04-25? (compile-holiday
                        {:day-in-month 25,
                         :month 4,
                         :and? true,
                         :statements '({:today #{7 6}, :condition "next", :target 1})})]
    (is (true? (holiday-04-25? (->day-time-context 2020 4 25))) "25.4.2020 is Saturday and is holiday because of and condition")
    (is (nil? (holiday-04-25? (->day-time-context 2020 4 26))) "26.4.2020 is Sunday and is not a holiday")
    (is (true? (holiday-04-25? (->day-time-context 2020 4 27))) "27.4.2020 is Monday and is holiday because of statement")
    (is (true? (holiday-04-25? (->day-time-context 2022 4 25))) "25.4.2021 is Monday and is holiday")
    (is (nil? (holiday-04-25? (->day-time-context 2022 4 26))) "26.4.2021 is Tuesday and not a holiday")))


(deftest holiday-10-21
  (let [holiday-10-21? (compile-holiday
                        {:day-in-month 21,
                         :month 10,
                         :statements
                         '({:today #{7}, :condition "next", :target 1}
                           {:today #{6}, :condition "previous", :target 5}
                           {:today #{3 2}, :condition "previous", :target 1}
                           {:today #{4}, :condition "next", :target 5})})]
    (is (nil? (holiday-10-21? (->day-time-context 2021 10 21))) "21.10.2021 is Thursday and not a holiday because of statement")
    (is (true? (holiday-10-21? (->day-time-context 2021 10 22))) "22.10.2021 is Friday and should be holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2021 10 23))) "23.10.2021 is Saturday and not a holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2021 10 29))) "29.10.2021 is Friday and not a holiday")
    (is (nil? (holiday-10-21? (->day-time-context 2023 10 21))) "21.10.2021 is Saturday and not a holiday because of statement")
    (is (nil? (holiday-10-21? (->day-time-context 2023 10 22))) "22.10.2021 is Sunday and not a holiday")
    (is (true? (holiday-10-21? (->day-time-context 2023 10 20))) "20.10.2021 is Friday and should be a holiday")))


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
  (is (= (parse-definition "easter -6 P7D")
         {:easter? true, :offset -6, :unknown ["p7d"]}))
  (is (= (parse-definition "julian 06-15")
         {:julian? true, :day-in-month 15, :month 6}))
  (is (= (parse-definition "julian 12-25 and if saturday, sunday then next monday")
         {:julian? true, :day-in-month 25, :month 12, :and? true, :statements ['("saturday,sunday" "then" "next" "monday")]}))
  (is (= (parse-definition "julian 12-25 P2D")
         {:julian? true, :day-in-month 25, :month 12, :unknown ["p2d"]})))


(comment
  (clojure-version)
  (test/run-test new-year)
  (test/run-test holiday-definition-parsing)
  (test/run-tests)
  (add-tap println)
  (def easter-4 (compile-easter 4))
  (easter-4 (->day-time-context 2022 4 21))

  (catholic/easter 2023)
  (orthodox-easter 2023)
  (->day-time-context 2023 4 9)
  (v/value->julian-date ((->day-time-context 2022 1 7) :value))
  (v/julian-date->value 2021)
  (v/with-time-configuration
    {:calendar :julian}
    (def julian-compile-test
      (compile-julian
       {:julian? true,
        :day-in-month 25,
        :month 12, :and? true,
        :statements ['("sunday" "then" "next" "monday")]})))
  (def julian-compile-test
    (compile-julian
     {:julian? true,
      :day-in-month 25,
      :month 12, :and? true,
      :statements ['("sunday" "then" "next" "monday")]}))
  (julian-compile-test (->day-time-context 2024 1 8))
  (v/with-time-configuration
    {:calendar :julian}
    (->
     (v/date 2022 12 25)
     v/time->value
     v/day-time-context))

  (->
   (v/with-time-configuration
     {:calendar :julian}
     (v/date 2022 12 25))
   v/time->value
   v/day-time-context)


  (def holidays (clojure.edn/read-string (slurp "all_holidays.edn")))
  (def report (group-by vura.holidays.compile/analyze-holiday holidays))
  (def parsed-results (map parse-if (get report :static_condition)))
  (def transformed-statements (map transform-statement parsed-results))
  (def removed-unknown
    (remove #(or
              (some? (:unknown %))
              (empty? (:statements %)))
            transformed-statements))
  (map transform-statement (map parse-definition (get report :orthodox)))
  (map parse-definition (get report :dayofweek_in_month))
  (map parse-definition (get report :dayofweek_before_after))
  (spit "parsed_before_after.edn" (with-out-str (clojure.pprint/pprint (map parse-definition (get report :dayofweek_before_after)))))
  (spit "raw_before_after.edn" (with-out-str (clojure.pprint/pprint (get report :dayofweek_before_after))))
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
     (and (> day-in-month 7) (< day-in-month 15)))))

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