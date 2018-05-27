# Vura

[![Clojars Project](https://img.shields.io/clojars/v/kovacnica/vura.core.svg)](https://clojars.org/kovacnica/vura.core)


Vura is small clojure/script **zero dependency** time computation and manipulation library. Library uses numeric representation of
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

As base unit of time **vura** uses **second**. This is not unusual from programmers perspective but not that unusual from everybody else that are using [SI](https://en.wikipedia.org/wiki/SI_base_unit). So bare with me it doesn't matter in the end anyway.

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

 .
 .
 .

(defn days 
  "Function returns value of n days as number."
  [n]
  (* n day))
.
.
.

(defn midnight 
  "Function calculates value of midnight for given value. For example
  if some date value is inputed it will round-number to the begining of
  that day."
  [value]
  (round-number value day :floor))

.
.
.

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

.
.
.


```

Yout get the idea...


## What else?


