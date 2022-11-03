(ns vura.holidays.compiler
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
          #"\d+-\d+-\d+"
          (recur words
                 (let [split-date (str/split word #"(-|#)")
                       year (Integer/parseInt (split-date 0))
                       month (Integer/parseInt (split-date 1))
                       day (Integer/parseInt (split-date 2))]
                   (assoc result
                          :year year
                          :month month
                          :day-in-month day)))
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


(defn analyze-statement
  [[today _ condition target]]
  {:today (set (map day-name->num (str/split today #",")))
   :condition condition
   :target (day-name->num target)})


(defn transform-statement [parsed-result]
  (update parsed-result :statements
          (fn [statements]
            (map analyze-statement statements))))


(defn compile-condition
  [{:keys [day-in-month
           month
           and?
           statements]}]
  ;; rezultat
  (let [forbidden-days (if-some [pred (reduce set/union (map :today statements))]
                         pred
                         (constantly false))
        allowed-days (complement forbidden-days)
        valid-targets (set (map :target statements))]
    (println "forbidden days: " forbidden-days)
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


(defn compile-static
  [{:keys [day-in-month
           month
           year]}]
  (fn [{d :day-in-month
        m :month
        y :year}]
    (and
     (= day-in-month d)
     (= month m)
     (or
      (nil? year)
      (= y year)))))


(defn compile-easter
  [{:keys [offset]}]
  (fn [{y :year
        value :value}]
    (let [easter (catholic/easter y)
          easter-d (:day-in-month easter)
          easter-m (:month easter)
          easter-value (:value (->day-time-context y easter-m easter-d))
          easter-offset-value (:value (v/day-time-context (+ easter-value (v/days offset))))]
      (= value easter-offset-value))))


(defn compile-orthodox
  [{:keys [offset
           and?
           statements]}]
  (fn [{d :day-in-month
        m :month
        y :year
        value :value}]
    (let [orthodox (ortodox/orthodox-easter {:year y})
          orthodox-d (:day-in-month orthodox)
          orthodox-m (:month orthodox)
          orthodox-value (:value (->day-time-context y orthodox-m orthodox-d))
          orthodox-offset-context (v/day-time-context (+ orthodox-value (v/days offset)))]
      (if (empty? statements)
        (= value (:value orthodox-offset-context))
        ((compile-condition {:day-in-month (:day-in-month orthodox-offset-context)
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


(defn compile-in-month
  [{:keys [nth
           week-day
           month]}]
  (fn [{d :day-in-month
        wd :day
        m :month}]
    (and
     (= m month)
     (= wd week-day)
     (>= d (+ (* (- nth 1) 7) 1))
     (<= d (* nth 7)))))


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
               ;;
                (and first? after?)
                value
               ;;
                :else
                (+ value (v/weeks (if before?
                                    (dec _nth)
                                    (- (dec _nth))))))))]
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
          (let [target (as-> value t
                         (->nth t _nth)
                         (if (= predicate :before) (+ t v/week) t)
                         (v/day-time-context t))]
            ;(println "Target: " target)
            ;(println "Days: " (:day-in-month target) rel-day-in-month)
            ;(println "sfajio" [predicate
            ;                   (let [diff (- (:day-in-month target) rel-day-in-month)]
            ;                     (println diff))])
            (println "DAYS: " [(:month target) (:day-in-month target)] [rel-month rel-day-in-month])
            (println "DIFF: " (- (:day-in-month target) rel-day-in-month))
            (and
             (= (:month target) rel-month)
             (case predicate
               :after (let [diff (- (:day-in-month target) rel-day-in-month)]
                        (and (>= diff 0) (< diff 7)))
               :before (let [diff (- (:day-in-month target) rel-day-in-month)]
                         (and (>=  diff 0) (< diff 7))))))
          ;; Otherwise check based on relative week-day
          :else (throw (ex-info "Unknown definition" definition)))))))


;; :julian
;; :static
;; :orthodox
;; :easter
;; :condition
;; :before-after
;; :nth

(defn compile-type [definition]
  (condp #(contains? %2 %1) definition
    :julian?
    (compile-julian definition)
    ;;
    :easter?
    (compile-easter definition)
    ;;
    :orthodox?
    (compile-orthodox definition)
    ;;
    :statements
    (compile-condition definition)
    ;;
    :predicate
    (compile-before-after definition)
    ;;
    :in?
    (compile-in-month definition)
    ;;
    :unknown
    (constantly false)
    ;;
    (compile-static definition)))


(comment
  (map parse-definition (keys (c/get-holiday-days :hr)))
  (map compile-type (map parse-definition (keys (c/get-holiday-days :hr))))

  (def definition (parse-definition "12-25"))
  (def pred (compile-type definition))

  (def holiday-mapping
    (reduce-kv
     (fn [result definition name-mapping]
       (assoc result
              (compile-type (parse-definition definition))
              name-mapping))
     nil
     (c/get-holiday-days :hr)))


  (def pred (compile-static definition))
  (def value (->day-time-context 2022 12 25))
  (def value (->day-time-context 2022 5 1))
  (def value (->day-time-context 2022 11 18))

  (some
   (fn [[pred naming]]
     (println "PREDI: " pred)
     (println "NAMING: " naming)
     (when (pred value)
       naming))
   holiday-mapping))


;; Ovak bi trebao izleda file


(def holidays
  {"01-01" {"_name" "01-01", "type" "public"},
   "01-06" {"_name" "01-06", "type" "public"},
   "easter -47" {"_name" "easter -47", "type" "observance"},
   "easter" {"_name" "easter", "type" "public"},
   "easter 1" {"_name" "easter 1", "type" "public"},
   "easter 60" {"_name" "easter 60", "type" "public"},
   "03-08" {"_name" "03-08", "type" "observance"},
   "05-01" {"_name" "05-01", "type" "public"},
   "05-30" {"name" {"hr" "Dan državnosti", "en" "National Day"}, "type" "public", "active" [{"from" "2020-01-01"}]},
   "2nd sunday in May" {"_name" "Mothers Day", "type" "observance"},
   "06-22" {"name" {"hr" "Dan antifašističke borbe", "en" "Anti-Fascist Struggle Day"}, "type" "public"},
   "06-25 #1" {"name" {"hr" "Dan državnosti", "en" "Statehood Day"}, "type" "public", "active" [{"to" "2020-01-01"}]},
   "06-25"
   {"name" {"hr" "Dan neovisnosti", "en" "Independence Day"}, "type" "observance", "active" [{"from" "2020-01-01"}]},
   "08-05"
   {"name"
    {"hr" "Dan pobjede i domovinske zahvalnosti i Dan hrvatskih branitelja",
     "en" "Victory and Homeland Thanksgiving Day and the Day of Croatian defenders"},
    "type" "public"},
   "08-15" {"_name" "08-15", "type" "public"},
   "10-08 #1"
   {"name" {"hr" "Dan neovisnosti", "en" "Independence Day"}, "type" "public", "active" [{"to" "2020-01-01"}]},
   "10-08"
   {"name" {"hr" "Dan Hrvatskoga sabora", "en" "Day of the Croatian Parliament"},
    "type" "observence",
    "active" [{"from" "2020-01-01"}]},
   "11-01" {"_name" "11-01", "type" "public"},
   "11-18 #1"
   {"name" {"hr" "Dan sjećanja na žrtvu Vukovara i Škabrnje", "en" "Remembrance Day"},
    "type" "observance",
    "active" [{"to" "2020-01-01"}]},
   "11-18"
   {"name"
    {"hr" "Dan sjećanja na žrtve Domovinskog rata i Dan sjećanja na žrtvu Vukovara i Škabrnje", "en" "Remembrance Day"},
    "type" "public",
    "active" [{"from" "2020-01-01"}]},
   "12-25" {"_name" "12-25", "type" "public"},
   "12-26" {"_name" "12-26", "type" "public"},
   "orthodox"
   {"_name" "orthodox",
    "type" "optional",
    "note" "Orthodox believers (legally defined as Christians who follow the Julian Calender)"},
   "orthodox 1"
   {"_name" "orthodox 1",
    "type" "optional",
    "note" "Orthodox believers (legally defined as Christians who follow the Julian Calender)"},
   "julian 12-25"
   {"_name" "julian 12-25",
    "type" "optional",
    "note" "Orthodox believers (legally defined as Christians who follow the Julian Calender)"},
   "10 Dhu al-Hijjah" {"_name" "10 Dhu al-Hijjah", "type" "optional", "note" "Muslim believers"},
   "1 Shawwal" {"_name" "1 Shawwal", "type" "optional", "note" "Muslim believers"},
   "1 Tishrei" {"_name" "10 Tishrei", "type" "optional", "note" "Jewish believers"},
   "10 Tishrei" {"_name" "10 Tishrei", "type" "optional", "note" "Jewish believers"}})

(def locale-holiday-mapping
  (reduce-kv
   (fn [result definition name-mapping]
     (assoc result
            (compile-type (parse-definition definition))
            name-mapping))
   nil
   holidays))


(defn holiday?
  [context]
  (some
   (fn [[pred naming]]
     (when (pred context)
       naming))
   locale-holiday-mapping))


(comment
  (time (locale-holiday-mapping :hr))
  (time (holiday? (->day-time-context 2022 11 18))))



(comment
  (declare third-sunday-before-12-25 before-after-tests)
  (test/run-test before-after-tests)
  (third-sunday-before-12-25 (->day-time-context 2022 12 4))
  (third-sunday-before-12-25 (->day-time-context 2022 12 11))
  (def third-sunday-before-12-25
    (compile-before-after {:nth 3,
                           :week-day 7,
                           :predicate :before,
                           :relative-to {:day-in-month 25, :month 12}})))



(deftest before-after-tests
  (let [monday-before-september (compile-before-after {:week-day 1,
                                                       :predicate :before,
                                                       :relative-to {:month 9}})
        third-monday-after-12-25 (compile-before-after {:nth 3,
                                                        :week-day 1,
                                                        :predicate :after,
                                                        :relative-to {:day-in-month 25, :month 12}})
        second-monday-before-first-tuesday-in-january (compile-before-after {:nth 2,
                                                                             :week-day 1,
                                                                             :predicate :before,
                                                                             :relative-to {:nth 1, :week-day 2, :in? true, :month 1}})
        first-monday-before-06-01 (compile-before-after {:nth 1,
                                                         :week-day 1,
                                                         :predicate :before,
                                                         :relative-to {:day-in-month 1, :month 6}})
        first-monday-after-10-12 (compile-before-after {:week-day 1,
                                                        :predicate :after,
                                                        :relative-to {:day-in-month 12, :month 10}})
        third-saturday-after-06-01 (compile-before-after {:nth 3,
                                                          :week-day 6,
                                                          :predicate :after,
                                                          :relative-to {:day-in-month 1, :month 6}})
        third-sunday-before-12-25 (compile-before-after {:nth 3,
                                                         :week-day 7,
                                                         :predicate :before,
                                                         :relative-to {:day-in-month 25, :month 12}})
        saturday-after-10-21 (compile-before-after {:week-day 6,
                                                    :predicate :after,
                                                    :relative-to {:day-in-month 21, :month 10}})
        friday-after-4th-thursday-in-november (compile-before-after {:week-day 5,
                                                                     :predicate :after,
                                                                     :relative-to {:nth 4, :week-day 4, :in? true, :month 11}})
        monday-after-07-01 (compile-before-after {:week-day 1,
                                                  :predicate :after,
                                                  :relative-to {:day-in-month 1, :month 7}})
        first-sunday-before-12-25 (compile-before-after {:nth 1,
                                                         :week-day 7,
                                                         :predicate :before,
                                                         :relative-to {:day-in-month 25, :month 12}})
        monday-before-06-20 (compile-before-after {:week-day 1,
                                                   :predicate :before,
                                                   :relative-to {:day-in-month 20, :month 6}})
        tuesday-after-2nd-monday-in-august (compile-before-after {:week-day 2,
                                                                  :predicate :after,
                                                                  :relative-to {:nth 2, :week-day 1, :in? true, :month 8}})
        tuesday-after-1st-monday-in-november (compile-before-after {:week-day 2,
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


(comment
  (test/run-test before-after-tests))


(deftest new-year
  (let [new-year? (compile-condition
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
  (let [holiday-04-25? (compile-condition
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
  (let [holiday-10-21? (compile-condition
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


  ;; "01-01" "01-06" "easter -47" "easter" "easter 1" "easter 60" "03-08" "05-01" "05-30"
  ;; "2nd sunday in May" "06-22" "06-25 #1" "06-25" "08-05" "08-15" "10-08 #1" "10-08"
  ;; "11-01" "11-18 #1" "11-18" "12-25" "12-26" "orthodox" "orthodox 1"
  ;; "julian 12-25" "10 Dhu al-Hijjah" "1 Shawwal" "1 Tishrei" "10 Tishrei"

  (def stat-test1 (compile-static (parse-definition "01-01")))
  (stat-test1 (->day-time-context 2022 1 1))
  (stat-test1 (->day-time-context 2021 12 31))
  (stat-test1 (->day-time-context 2022 1 2))
  (stat-test1 (->day-time-context 2022 4 4))
  (stat-test1 (->day-time-context 2023 1 1))
  (def stat-test2 (compile-static (parse-definition "01-06")))
  (stat-test2 (->day-time-context 2022 1 6))
  (stat-test2 (->day-time-context 2022 1 5))
  (stat-test2 (->day-time-context 2022 1 7))
  (stat-test2 (->day-time-context 2023 1 6))
  (def easter-test1 (compile-easter (parse-definition "easter")))
  (easter-test1 (->day-time-context 2022 4 4))
  (def easter-test2 (compile-easter (parse-definition "easter -47")))
  (easter-test2 (->day-time-context 2022 4 4))
  (def nth-day-in-month-test1 (compile-in-month (parse-definition "2nd sunday in May")))
  (nth-day-in-month-test1 (->day-time-context 2022 5 1))
  (nth-day-in-month-test1 (->day-time-context 2022 5 7))
  (nth-day-in-month-test1 (->day-time-context 2022 5 8))
  (nth-day-in-month-test1 (->day-time-context 2022 5 15))
  (def orthodox-test1 (compile-orthodox (parse-definition "orthodox")))
  (orthodox-test1 (->day-time-context 2022 1 5))
  (orthodox-test1 (->day-time-context 2022 4 24))
  (orthodox-test1 (->day-time-context 2022 4 26))
  (def orthodox-test2 (compile-orthodox (parse-definition "orthodox 1")))
  (orthodox-test2 (->day-time-context 2022 1 5))
  (orthodox-test2 (->day-time-context 2022 4 24))
  (orthodox-test2 (->day-time-context 2022 4 25))
  (orthodox-test2 (->day-time-context 2023 4 16))
  (orthodox-test2 (->day-time-context 2023 4 17))
  (def julian-test1 (compile-julian (parse-definition "julian 12-25")))
  (julian-test1 (->day-time-context 2022 1 6))
  (julian-test1 (->day-time-context 2022 1 7))
  (julian-test1 (->day-time-context 2022 1 8))


  (def holidays (clojure.edn/read-string (slurp "all_holidays.edn")))
  (def report (group-by vura.holidays.compile/analyze-holiday holidays))
  (def parsed-results (map parse-definition (get report :static_condition)))
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
  (spit "transformed_statements.edn"
        (with-out-str
          (clojure.pprint/pprint removed-unknown)))
  (frequencies (map :unknown parsed-results))
  (spit "static_with_condition.edn"
        (with-out-str
          (clojure.pprint/pprint (:static_condition report))))

  (def test-compile (compile-condition (first removed-unknown)))
  (def new-year?
    (compile-condition
     {:day-in-month 1,
      :month 1,
      :and? true,
      :statements
      '({:today #{6}, :condition "previous", :target 5}
        {:today #{7}, :condition "next", :target 1})}))
  (def boxing-day?
    (compile-condition
     {:day-in-month 26,
      :month 12,
      :and? true,
      :statements
      '({:today #{6}, :condition "next", :target 1}
        {:today #{7}, :condition "next", :target 2})}))
  (def t
    (compile-condition
     {:day-in-month 26,
      :month 1,
      :statements
      '({:today #{3 2}, :condition "previous", :target 1}
        {:today #{4 5}, :condition "next", :target 1})}))
  (def test-compile
    (compile-condition
     {:day-in-month 21,
      :month 10,
      :statements
      '({:today #{7}, :condition "next", :target 1}
        {:today #{6}, :condition "previous", :target 5}
        {:today #{3 2}, :condition "previous", :target 1}
        {:today #{4}, :condition "next", :target 5})}))
  (def test-compile
    (compile-condition
     {:day-in-month 6,
      :month 11,
      :statements '({:today #{4 6 3 2 5}, :condition "next", :target 1})})))

(comment
  ((:name (mk/holiday? (->day-time-context 2023 1 1))) :hr)
  ((:name (hr/holiday? (->day-time-context 2022 11 18))) :hr)
  (mk/holiday? (->day-time-context 2023 1 1))
  (mk/holiday? (->day-time-context 2022 1 7))
  (mk/holiday? (->day-time-context 2022 2 23))
  (mk/holiday? (->day-time-context 2022 12 25))
  (hr/holiday? (->day-time-context 2022 12 25))
  (hr/holiday? (->day-time-context 2022 11 18))
  (hr/holiday? (->day-time-context 2022 11 1))
  (hr/holiday? (->day-time-context 2022 2 23))
  (hr/holiday? (->day-time-context 2022 4 17))
  )