(ns vura.holidays.compiler
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [vura.core :as v]
   [vura.holidays.catholic :as catholic]
   [vura.holidays.ortodox :as ortodox])
  #?(:clj
     (:import java.lang.Integer)))


(defn parse-int [x]
  #?(:clj (Integer/parseInt x)
     :cljs (js/parseInt x)))

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
                                (parse-int offset-word))]
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
                                (parse-int offset-word))]

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
                       year (parse-int (split-date 0))
                       month (parse-int (split-date 1))
                       day (parse-int (split-date 2))]
                   (assoc result
                          :year year
                          :month month
                          :day-in-month day)))
          ;;
          #"\d+-\d+"
          (recur words
                 (let [split-date (str/split word #"(-|#)")
                       day (parse-int (split-date 1))
                       month (parse-int (split-date 0))]
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
          (recur words (assoc result :nth (parse-int ((str/split word #"") 0))))
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


(defn compile-condition
  [{:keys [day-in-month
           month
           and?
           statements]}]
  (let [forbidden-days (if-some [pred (reduce set/union (map :today statements))]
                         pred
                         (constantly false))
        allowed-days (complement forbidden-days)
        valid-targets (set (map :target statements))]
    (fn [{d :day-in-month
          wd :day
          m :month
          value :value}]
      (some
       (fn [{:keys [today condition] :as statement}]
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
            (and
             (= (:month target) rel-month)
             (case predicate
               :after (let [diff (- (:day-in-month target) rel-day-in-month)]
                        (and (>= diff 0) (< diff 7)))
               :before (let [diff (- (:day-in-month target) rel-day-in-month)]
                         (and (>=  diff 0) (< diff 7))))))
          ;; Otherwise check based on relative week-day
          :else (throw (ex-info "Unknown definition" definition)))))))


;; Holiday types :julian :static :orthodox :easter :condition :before-after :nth

(defn compile-type [definition]
  (condp #(contains? %2 %1) definition
    :unknown
    (constantly false)
    ;;
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
    (compile-static definition)))
