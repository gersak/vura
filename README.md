# Vura CORE

[![Clojars Project](https://img.shields.io/clojars/v/kovacnica/vura.core.svg)](https://clojars.org/kovacnica/vura.core)


Vura is small clojure/script **ZERO DEPENDENCY** time computation and manipulation library. Library uses numeric representation of
time to compute Gregorian calendar years, months and so on. Library calculates time with system offset by transforming local 
timestamp value to UTC value at given time with function **date->value** after which time is just number. When computation/manipulation
is over numeric value of time should be transformed to Date representation by caling **value->date**.


Heart of this library is round-number function.

``` clojure
(defn round-number
  "Function returns round whole number that is devidable by target-number.
  Rounding strategy can be specified in round-how? options:

   :floor
   :ceil
   :up
   :down"
  ([number] (round-number number 1))
  ([number target-number] (round-number number target-number :down))
  ([number target-number round-how?]
   (assert (pos? target-number) "Target number has to be positive.")
   (case target-number
     0 0
     (let [round-how? (keyword round-how?)
           diff (rem number target-number)
           base (if (>= target-number 1)
                  (* target-number (quot number target-number))
                  (- number diff))
           limit (* 0.25 target-number target-number)
           compare-fn (case round-how?
                        :floor (constantly false)
                        :ceil (constantly (not (zero? diff)))
                        :up <=
                        <)]
       ((if (pos? number) + -)
        base
        (if (compare-fn limit (* diff diff)) target-number 0))))))
```

This is nice (and quick) function that doesn't just to rounding to 1 or 0 but to any target number whatsoever. I.E.
``` clojure

vura.core=> (time (round-number 182.8137172 0.25 :up))
"Elapsed time: 0.062 msecs"
182.75

vura.core=> (time (round-number 182.8137172 0.25 :down))
"Elapsed time: 0.123 msecs"
182.75

vura.core=> (time (round-number 182.8137172 0.25 :ceil))
"Elapsed time: 0.075 msecs"
183.0

vura.core=> (time (round-number 182.8137172 0.25 :floor))
"Elapsed time: 0.062 msecs"
182.75


```

Rounding strategy :floor and :ceil are selfexplanatory. :up and :down are differnt in that :up will round number up if value is exactly half of target-number and :down will round number down. Otherwise value will be rounded to nearest value. I.E.

``` clojure

vura.core=> (time (round-number 182.625 0.25 :down))
"Elapsed time: 0.072 msecs"
182.5

vura.core=> (time (round-number 182.625 0.25 :up))
"Elapsed time: 0.063 msecs"
182.75


```


## Choices and assumptions

Values of timestamps are normalized to Greenwich Mean Time. As base unit of time **vura** uses **second**. This is very unusual from programmers perspective but not that unusual for anyone else that is using [SI](https://en.wikipedia.org/wiki/SI_base_unit). So bare with me it doesn't matter in the end anyway.

```clojure
(def second 1)
(def millisecond (/ second 1000))
(def microsecond (/ millisecond 1000))
(def nanosecond (/ microsecond 1000))
(def minute 60)
(def hour (* 60 minute))
(def day (* 24 hour))
(def week (* 7 day))
```

Time constructs defined this way can be easly added, subtracted, multiplied, devided, round-numbered.

```
(defn seconds 
  "Function returns value of n seconds as number."
  [n]
  (* n second))


(defn days 
  "Function returns value of n days as number."
  [n]
  (* n day))

(defn midnight 
  "Function calculates value of midnight for given value. For example
  if some date value is inputed it will round-number to the begining of
  that day."
  [value]
  (round-number value day :floor))

(defn second? 
  "Returns which second in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int (mod value 60)))

(defn minute? 
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int
    (mod
      (/ (round-number value minute :floor) minute)
      60)))

(defn hour? 
  "Returns which hour in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int
    (mod
      (/ (round-number value hour :floor) hour)
      24)))

```

Yout get the idea...


## What else?

How about... I've used to have alot of problems working with calendar, calculating weekends, working days and holidays, calculating daily wage spent/unused vacation days. Vura offers functions like:

* weekend?
* day-in-year?
* week-in-year?
* first-day-in-month?
* day-in-month?
* last-day-in-month?
* day-context
* time-context
* day-time-context


And most important of them all is *with-time-configuration* macro.

```clojure
(require '[vura.core :refer [date day day-context with-time-configuration date->value]])


(def hr-holidays 
    #{[1 1]
      [6 1]
      [1 5]
      [22 6]
      [25 6]
      [5 8]
      [15 8]
      [8 10]
      [1 11]
      [25 12]
      [26 12]})

(def vacation-start (date 2030 6 15 8 0 0))

(with-time-configuration
    {:weekend-days #{5 6 7}
     :week-days #{1 2 3 4}
     :holiday? (fn [{:keys [day-in-month month]}]
                 (boolean (hr-holidays [day-in-month month])))}
    (->>
      (iterate (partial + day) (date->value vacation-start))
      (take 20)
      ;; Be carefull if this is not realized in with-time-configuration
      ;; configuration bindings won't work. Use mapv instead map
      (mapv day-context)
      (remove :holiday?)
      (remove :weekend?)
      count
      time))

"Elapsed time: 6.397 msecs"
11

vura.core=>  

```

What happend here? Lets say that we want to go on vacation from 15 of June for 20 days. There are some holidays in June as
well the fact that we are living in socially advanced culture with 3 day weekend that is Friday, Saturday and Sunday. 
So how many days did we actually spent?


**with-time-configuration** macro allows us to put time values in context. Vura has dynamic variables ***wekend-days*, *week-days*
*holiday?*, *offset*** that can be used to put values in context. For example if we are interesed for sequence of values what is theirs **day-context** with configuration definition above `(mapv day-context)`  will return vector of maps with following keys:

* value
* day
* week
* month
* year
* day-in-month
* weekend?
* holiday?
* first-day-in-month?
* last-day-in-month?


After that everything else is simple. We just remove :holiday? and :weekend? and we are left with only spent days. That is 11 days.


## Don't be shy with round-number

Let's play a while. We'll try to round-number on couple of examples.


```clojure
(def some-day (date 2030 6 15 8 15 20 300))
  (def some-day-value (date->value some-day))
  (def other-day-value (-> some-day 
                           date->value 
                           (+ (period {:weeks 26 
                                       :days 3 
                                       :hours 82 
                                       :minutes 5000 
                                       :seconds 1000 
                                       :milliseconds -800}))))
  ;; 3848643839/2

  (def other-day (value->date other-day-value)) ;; #inst "2030-12-24T04:51:59.500-00:00"

  (def day-difference (- other-day-value some-day-value)) ;; 82900996/5

  ;; Lets round how many hours have passed with rounding strategy :ceil
  ;; that will round number up even if millisecond has passed in current hour

  (round-number (/ day-difference hour) 1 :ceil) ;; 4606N

  (with-time-configuration {:offset 0}  
    (-> other-day-value (round-number (hours 6) :ceil) value->date)) ;; #inst "2030-12-24T06:00:00.000-00:00"

  (with-time-configuration {:offset -240}  
    (-> 
      other-day-value                 ;; 3848643839/2
      (round-number (hours 2) :floor) ;; 1924315200N
      value->date                     ;; #inst "2030-12-24T03:00:00.000-00:00"
      get-offset))                    ;; -240

 
```

