<p align="center">
  <img width="460" height="300" src="resources/images/infinityclock.jpg" style="border-radius:20px;">
</p>

# Timing (core)

[![Clojars Project](https://img.shields.io/clojars/v/kovacnica/timing.svg)](https://clojars.org/kovacnica/timing)

Timing is small Clojure/script **zero dependency** time computation and
manipulation library. Library uses numeric representation of time to compute
Gregorian calendar years, months and so on. Timing calculates time with current
system offset by transforming local timestamp value to UTC value at given time
with function **time->value** that returns plain number of milliseconds. When
computation/manipulation is over numeric value of time should be transformed to
Date representation by caling **value->time**. For rest of core functions check
[API docs](http://gersak.github.io/timing/api/timing.core.html).

Usual workflow would be to transform java.util.Date or some other object to
numeric value with **time->value** protocol implementation and afterwards do the
computation in numeric domain. timing.core offers at par functions to
**clj(s)-time** only those functions work in numeric domain and on immutable
values. This means working with numbers instead of objects to compute values.
After computation is over if needed use **value->time** implementation of
__TimeValueProtocol__ to transform numeric values to _java.util.Date_
| _js/Date_, or leave them in numeric form if that fits you. Timing has nice macro
that transforms __Date__ instances to their value representations and afterwards
evaluates body -> **time-as-value**. Use it to reduce boilerplate when possible.



## Why?

* Immutability
* **Zero dependency**, only **clojure.core**
* Simple and familiar algebra in number domain (+,-,/,*,round-number,qout,mod)
* Powerfull core clojure concepts of sequences and operation on sequences
  (range, map, for, doseq, iterate) fit very nicely into this concept
* Multiple calendars (julian, gregorian, hebrew, islamic)
* Timezone handling through pure clojure data, converted directly from IANA TZ
  database and stored in vura.timezones.db namespace
* Both Clojure and Clojurescript library with same 99% of code


## How?
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

This is nice (and quick) function that doesn't just to rounding to 1 or 0 but to
any target number whatsoever. I.E.
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

Rounding strategy :floor and :ceil are selfexplanatory. :up and :down are
different in that :up will round number up if value is exactly half of
target-number and :down will round number down. Otherwise value will be rounded
to nearest value. I.E.

``` clojure

vura.core=> (time (round-number 182.625 0.25 :down))
"Elapsed time: 0.072 msecs"
182.5

vura.core=> (time (round-number 182.625 0.25 :up))
"Elapsed time: 0.063 msecs"
182.75



```

## Calendars and timezones (Credits)
**timing.core** internals are based on algorithms provided by 
awesome [Astronomy answers](https://www.aa.quae.nl/en/reken/juliaansedag.html)

Timezones are contained in **timing.timezones.db** namespace and are parsed from files
downloaded from [IANA - Time Zone Database](https://www.iana.org/time-zones)


## Intro

Values of timestamps are normalized to Greenwich Mean Time.

```clojure
(def millisecond 1)
(def second (* 1000 millisecond)
(def minute (* 60 second))
(def hour (* 60 minute))
(def day (* 24 hour))
(def week (* 7 day))
```

Time constructs defined this way can be easly added, subtracted, multiplied,
devided, round-numbered.

```clojure
(defn seconds
  "Function returns value of n seconds as number."
  [n]
  (* n second))

(defn second?
  "Returns which second in day does input value belongs to. For example
  for date 15.02.2015 it will return number 0"
  [value]
  (int (mod value 60)))

(defn midnight
  "Function calculates value of midnight for given value. For example
  if some date value is inputed it will round-number to the begining of
  that day."
  [value]
  (round-number value day :floor))

```
Yout get the idea...



## Time configuration
Usually when working with time context/location is very important. Following
dynamic variables are defined in _timing.core_ that affect computation of
_time->value_ and _value->time_ functions as well as _date_ function.

- \*timezone\* - specify what timezone will timing use to translate value from UTC to ->local timezone
- \*calendar\* - when constructing Date object which calendar is used to calculate year/month/day value
- \*weekend-days\* - What days are weekend-days?
- \*holiday?\* - Convinince function to check if certain day is holiday or not

Timing offers **with-time-configuration** macro that binds options map to above dynamic
variables and afterwards evaluates body. Nice example for this macro is function _teleport_
that transfers value from one timezone to another.

``` clojure
(defn teleport
  "Teleports value to different timezone."
  [value timezone]
  (let [d (value->time value)] ;; first cast value to Date for current *timezone*
    (with-time-configuration   ;; Then specify that following computation is in different timezone
      {:timezone timezone}
      (time->value d))))       ;; At last compute value for given date at that timezone
```
This function explains alot. Casting value to _Date_ is a way to hold current
timezone value and inject that value to different context provided by
_with-time-configuration_ macro. For more information go to wiki _Date is the
gate_ section. If there are still some questions left try to find them in _We
all live in England_.




## Day/Time context

I've used to have alot of problems working with calendar, calculating weekends,
working days and holidays, calculating daily wage or spent/unused vacation days.
Timing offers functions like _day-time-context_:

```clojure
(->
    (date 2018 12 25 0 0 0 0)
    time->value
    day-time-context)


{:day 2,
 :hour 0,
 :week 52,
 :weekend? false,
 :first-day-in-month? false,
 :second 0,
 :value 1545696000,
 :month 12,
 :year 2018,
 :millisecond 0,
 :holiday? false,
 :last-day-in-month? false,
 :day-in-month 25,
 :minute 0}
```

day-time-context functions can be mapped to any sequence of timing time values. So
it is possible to `(iterate (partial + (days 3.5)) (date->value (date 2018)))`
to get all dates with interval of 3.5 days till the end of time and afterwards
apply map day-context and take 20 days for example. 

When used inside of _with-time-configuration_ macro it will return context for
specified \*calendar\*/\*timezone\*. This results with easy switching of
year/month/day/holiday for given value/values from one calendar/timezone to
another.

## Calendar Frame

**day-time-context** function solves most of calculation challenges. Still there
are some use cases where it is usefull to have function that can return
day-context for whole month or year for given input value. Multimethod
**calendar-frame** provides implementations for **:year, :month** and **:week**
view for given value and can be extended to other frame types. This function
might be usefull in frontend for creating different UI components with OM,
Reagent, hx or some other Clojure/script frontend library.. Don't forget to use
**with-time-configuration** macro to put context on calendar-frame (to flag
holidays and weekend-days, as well as calendar).

## Don't forget about round-number

Timing returns Date representations from numeric values and all values can be
round-number(ed) so use that. Round values to `(days 3.5)` or `(hours 11)` or
maybe `(period {:week 2 :days 3})`

## Use clojure.core functions

**quot** is great way to calculate how long did some period(value) last in time units. For example
```clojure
(qout (period {:weeks 3 :hours 10 :minutes 11 :seconds 32821}) timing.core/hour) ;; 523

(qout (timing.core/interval (date 2018) (date 2019)) timing.core/minute) ;; 525600000
```
## License

Copyright © 2018 Robert Gersak

Released under the MIT license.
