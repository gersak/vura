(ns vura.holidays.compiler
  (:require
   [clojure.set :as set]
   [vura.core :as v]
   [vura.holiday.catholic :as catholic]
   [vura.holiday.ortodox :as ortodox]
   [vura.holiday.util
    :refer [->day-time-context day-name->num]]))

(defn parse-statement
  "Converts a parsed statement sequence like ('saturday' 'then' 'previous' 'friday')
   to the expected map format {:today #{6}, :condition 'previous', :target 5}"
  [statement-seq]
  (let [[today-str _ condition-str target-str & extra] statement-seq
        today-day (day-name->num today-str)
        target-day (day-name->num target-str)]
    (cond-> {:today #{today-day}
             :condition condition-str
             :target target-day}
      (seq extra) (assoc :unknown (vec extra)))))

(defn compile-condition
  [{:keys [day-in-month
           month
           and?
           statements]}]
  ;; Transform parsed statements from text format to expected map format
  (let [transformed-statements (if (and statements (sequential? (first statements)))
                                 (map parse-statement statements)
                                 statements) ;; Already in correct format
        forbidden-days (if-some [pred (reduce set/union (map :today transformed-statements))]
                         pred
                         (constantly false))
        allowed-days (complement forbidden-days)
        valid-targets (set (map :target transformed-statements))]
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
       transformed-statements))))

(defn compile-static
  [{:keys [day-in-month
           month
           year]}]
  (fn [{d :day-in-month
        m :month
        y :year}]
    (when (and
           (= day-in-month d)
           (= month m)
           (or
            (nil? year)
            (= y year)))
      true)))

(defn compile-easter
  [{:keys [offset]}]
  (fn [{y :year
        value :value}]
    (let [easter (catholic/easter y)
          easter-d (:day-in-month easter)
          easter-m (:month easter)
          easter-value (:value (->day-time-context y easter-m easter-d))
          easter-offset-value (:value (v/day-time-context (+ easter-value (v/days offset))))]
      (when (= value easter-offset-value)
        true))))

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
        (when (= value (:value orthodox-offset-context))
          true)
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
      (when (and
             (= day-in-month d)
             (= month m))
        true))))

(defn compile-in-month
  [{:keys [nth
           week-day
           month]}]
  (fn [{d :day-in-month
        wd :day
        m :month}]
    (when (and
           (= m month)
           (= wd week-day)
           (>= d (+ (* (- nth 1) 7) 1))
           (<= d (* nth 7)))
      true)))

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
            (when (and
                   (= (:month rctx) rel-month)
                   (= (:day rctx) rel-week-day)
                   (<=
                    (inc (* 7 (dec rel-nth)))
                    (:day-in-month rctx)
                    (* 7 rel-nth)))
              true))
          ;; relative to static date
          (and rel-day-in-month rel-month)
          (let [target (as-> value t
                         (->nth t _nth)
                         (if (= predicate :before) (+ t v/week) t)
                         (v/day-time-context t))]
            (when (and
                   (= (:month target) rel-month)
                   (case predicate
                     :after (let [diff (- (:day-in-month target) rel-day-in-month)]
                              (and (>= diff 0) (< diff 7)))
                     :before (let [diff (- (:day-in-month target) rel-day-in-month)]
                               (and (>= diff 0) (< diff 7)))))
              true))
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
