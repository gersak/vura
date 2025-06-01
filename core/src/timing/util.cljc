(ns timing.util
  "Utility functions for the Timing library, including calendar printing."
  (:require [timing.core :as core]))

(defn print-calendar
  "Prints a formatted calendar for the given month and year.
  
  Options:
  - :first-day-of-week - Day to start week with (1=Monday, 7=Sunday), default 1
  - :day-width - Width of each day cell, default 3
  - :show-week-numbers - Whether to show week numbers, default false
  
  Example:
  (print-calendar 2023 12)
  (print-calendar 2023 12 {:first-day-of-week 7 :show-week-numbers true})"
  ([year month] (print-calendar year month {}))
  ([year month {:keys [first-day-of-week day-width show-week-numbers]
                :or {first-day-of-week 1 day-width 3 show-week-numbers false}}]
   (let [date-value (core/time->value (core/date year month 1))
         month-data (core/calendar-frame date-value :month)
         day-names (if (= first-day-of-week 7)
                     ["Sun" "Mon" "Tue" "Wed" "Thu" "Fri" "Sat"]
                     ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"])
         month-names ["" "January" "February" "March" "April" "May" "June"
                      "July" "August" "September" "October" "November" "December"]
         month-name (month-names month)

         ;; Adjust day numbers for different week start
         adjust-day (fn [day]
                      (if (= first-day-of-week 7)
                        (if (= day 7) 0 day)
                        (dec day)))

         ;; Group days into weeks
         weeks (loop [days month-data
                      current-week []
                      weeks []
                      week-start-day (adjust-day (:day (first month-data)))]
                 (if (empty? days)
                   (if (seq current-week)
                     (conj weeks current-week)
                     weeks)
                   (let [day (first days)
                         day-pos (adjust-day (:day day))]
                     (if (and (seq current-week) (= day-pos 0))
                       (recur (rest days)
                              [day]
                              (conj weeks current-week)
                              0)
                       (recur (rest days)
                              (conj current-week day)
                              weeks
                              week-start-day)))))

         ;; Add padding to first week if needed
         first-week (first weeks)
         first-day-pos (when (seq first-week)
                         (adjust-day (:day (first first-week))))
         padded-first-week (if (and first-week (> first-day-pos 0))
                             (concat (repeat first-day-pos nil) first-week)
                             first-week)

         ;; Add padding to last week if needed  
         last-week (last weeks)
         last-day-pos (when (seq last-week)
                        (adjust-day (:day (last last-week))))
         padded-last-week (if (and last-week (< last-day-pos 6))
                            (concat last-week (repeat (- 6 last-day-pos) nil))
                            last-week)

         ;; Replace first and last weeks with padded versions
         all-weeks (if (= (count weeks) 1)
                     [(concat (repeat first-day-pos nil)
                              first-week
                              (repeat (- 6 last-day-pos) nil))]
                     (concat [padded-first-week]
                             (rest (butlast weeks))
                             [padded-last-week]))

         ;; Formatting functions
         pad-cell (fn [text width]
                    (let [text-str (str text)
                          padding (max 0 (- width (count text-str)))
                          left-pad (quot padding 2)
                          right-pad (- padding left-pad)]
                      (str (apply str (repeat left-pad " "))
                           text-str
                           (apply str (repeat right-pad " ")))))

         separator-line (str "+"
                             (when show-week-numbers
                               (str (apply str (repeat 4 "-")) "+"))
                             (apply str (repeat 7 (str (apply str (repeat day-width "-")) "+"))))

         header-line (str "|"
                          (when show-week-numbers
                            (str (pad-cell "Wk" 4) "|"))
                          (apply str (map #(str (pad-cell % day-width) "|") day-names)))]

     ;; Print the calendar
     (println)
     (println (str "                " month-name " " year))
     (println separator-line)
     (println header-line)
     (println separator-line)

     (doseq [week all-weeks]
       (let [week-num (when (and show-week-numbers (seq week))
                        (:week (first (filter identity week))))
             week-line (str "|"
                            (when show-week-numbers
                              (str (pad-cell (or week-num "") 4) "|"))
                            (apply str
                                   (map (fn [day]
                                          (str (pad-cell
                                                (if day (:day-in-month day) "")
                                                day-width)
                                               "|"))
                                        week)))]
         (println week-line)))

     (println separator-line)
     (println))))

(defn print-year-calendar
  "Prints calendars for all 12 months of the given year.
  
  Options are passed through to print-calendar.
  
  Example:
  (print-year-calendar 2024)
  (print-year-calendar 2024 {:first-day-of-week 7})"
  ([year] (print-year-calendar year {}))
  ([year options]
   (println (str "\n" (apply str (repeat 20 " ")) year "\n"))
   (doseq [month (range 1 13)]
     (print-calendar year month options))))

(comment
  (timing.util/print-calendar 2027 4)
  (print-year-calendar 2025))
