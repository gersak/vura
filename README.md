<p align="center">
  <img width="460" height="300" src="resources/images/infinityclock.jpg" style="border-radius:20px;">
</p>

# Timing ⏰

[![Clojars Project](https://img.shields.io/clojars/v/kovacnica/timing.svg)](https://clojars.org/gersak/timing)

**The ultimate zero-dependency Clojure/ClojureScript time library that thinks in numbers.**

Timing is a comprehensive time computation and manipulation library that revolutionizes how you 
work with dates and time. By working in the **numeric domain** first, Timing unlocks the full 
power of functional programming for temporal operations while maintaining complete calendar accuracy.

## 🚀 Why Timing?

### **Zero Dependencies, Maximum Power**
- **Pure Clojure/ClojureScript** - No external dependencies except `clojure.core`
- **Cross-platform** - Identical behavior on JVM and JavaScript engines
- **Immutable** - All operations return new values, never mutate existing ones
- **Calendar** - 
- **Holidays** - 

### **Numeric Domain Philosophy**
```clojure
;; Work with time as numbers (milliseconds since epoch)
(def now (time->value (date 2024 6 15 14 30 0)))  ; => 1718461800000
(def later (+ now (days 7) (hours 3)))            ; Simple arithmetic!
(value->time later)                               ; Back to Date when needed
```

### **Functional Programming Superpowers**
Timing leverages Clojure's sequence operations naturally:
```clojure
;; Generate quarterly dates for 2024
(->> (range 0 12 3)
       (map #(add-months (core/time->value (core/date 2024 1 1)) %))
       (map core/value->time))

;; All business days in Q1 2024  
(def q1 (-> (core/date 2024 1 15) core/time->value))
  (->> (business-days-in-range (start-of-quarter q1) (end-of-quarter q1))
     (take 10)
     (map core/value->time))
```

### **Multiple Calendar Systems**
- **Gregorian** (default) - Modern Western calendar
- **Julian** - Historical calendar system  
- **Hebrew** - Jewish calendar with proper leap year handling
- **Islamic** - Lunar calendar support

### **Smart Period Arithmetic**
```clojure
;; Fixed-length periods (traditional)
(+ today (days 30) (hours 8))

;; Variable-length periods (NEW!)
(-> today
    (add-months 3)    ; Handles month lengths properly
    (add-years 2)     ; Handles leap years automatically  
    (+ (days 15)))    ; Mix with fixed periods
```

## ⚡ Quick Start

### Installation
```clojure
;; deps.edn
{:deps {dev.gersak/timing {:mvn/version "0.7.0"}}}

;; Leiningen  
[dev.gersak/timing "0.7.0"]
```

### Basic Usage
```clojure
(require '[timing.core :as t])

;; Create dates
(def birthday (t/date 1990 5 15))
(def now (t/date))

;; Convert to numeric domain for computation
(def age-ms (- (t/time->value now) (t/time->value birthday)))
(def age-days (/ age-ms t/day))

;; Time arithmetic  
(def next-week (+ (t/time->value now) (t/days 7)))
(def next-month (t/add-months (t/time->value now) 1))

;; Convert back to dates
(t/value->time next-week)
(t/value->time next-month)
```

## 🎯 Core Features

### **1. Precision Time Units**
```clojure
;; All time units as precise numbers
t/millisecond  ; => 1
t/second      ; => 1000
t/minute      ; => 60000
t/hour        ; => 3600000
t/day         ; => 86400000
t/week        ; => 604800000

;; Helper functions
(t/days 7)      ; => 604800000
(t/hours 3)     ; => 10800000  
(t/minutes 45)  ; => 2700000
```

### **2. Smart Period Arithmetic**
```clojure
;; NEW: Variable-length periods with edge case handling
(t/add-months (t/date 2024 1 31) 1)  ; => Feb 29, 2024 (leap year!)
(t/add-months (t/date 2023 1 31) 1)  ; => Feb 28, 2023 (not leap year)
(t/add-years (t/date 2024 2 29) 1)   ; => Feb 28, 2025 (Feb 29 doesn't exist)

;; Chain operations naturally
(-> (t/date 2024 1 15)
    (t/add-months 6)
    (t/add-years 2)  
    (+ (t/days 10))
    (+ (t/hours 8)))
```

### **3. Powerful Rounding & Alignment**
```clojure
;; Round to any precision
(t/round-number 182.8137 0.25 :up)    ; => 183.0
(t/round-number 182.8137 0.25 :down)  ; => 182.75

;; Align to time boundaries
(t/midnight value)           ; Round to start of day
(t/round-number value t/hour :floor)  ; Round to start of hour
```

### **4. Rich Time Context**
```clojure
(t/day-time-context (t/time->value (t/date 2024 6 15)))
; => {:day 6, :hour 0, :week 24, :weekend? true, :month 6, :year 2024,
;     :day-in-month 15, :holiday? false, :first-day-in-month? false, ...}
```

### **5. Calendar Frame Generation**
```clojure
;; Get all days in a month (perfect for UI calendars)
(t/calendar-frame (t/date 2024 6 1) :month)

;; Get all days in a year
(t/calendar-frame (t/date 2024 1 1) :year)

;; Get week view
(t/calendar-frame (t/date 2024 6 15) :week)
```

## 🛠️ Advanced Features

### **Temporal Adjusters**
```clojure
(require '[timing.adjusters :as adj])

;; Navigate to specific days
(adj/next-day-of-week today 1)        ; Next Monday
(adj/first-day-of-month-on-day-of-week today 5)  ; First Friday of month
(adj/last-day-of-month-on-day-of-week today 5)   ; Last Friday of month
(adj/nth-day-of-month-on-day-of-week today 2 3)  ; 3rd Tuesday of month

;; Period boundaries
(adj/start-of-week today)             ; Start of current week
(adj/end-of-month today)              ; End of current month  
(adj/start-of-quarter today)          ; Start of current quarter
(adj/end-of-year today)               ; End of current year

;; Business day operations
(adj/next-business-day today)         ; Skip weekends
(adj/add-business-days today 5)       ; Add 5 business days
(adj/business-days-in-range start end) ; All business days in range
```

### **Calendar Printing**
```clojure
(require '[timing.util :as util])

;; Print beautiful ASCII calendars
(util/print-calendar 2024 6)
; =>                 June 2024
;    +---+---+---+---+---+---+---+
;    |Mon|Tue|Wed|Thu|Fri|Sat|Sun|
;    +---+---+---+---+---+---+---+
;    |   |   |   |   |   | 1 | 2 |
;    | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
;    |10 |11 |12 |13 |14 |15 |16 |
;    +---+---+---+---+---+---+---+

;; Customizable options
(util/print-calendar 2024 6 {:first-day-of-week 7    ; Sunday first
                              :show-week-numbers true ; Show week numbers
                              :day-width 4})          ; Wider cells

;; Print entire year
(util/print-year-calendar 2024)
```

### **Timezone & Configuration**
```clojure
;; Dynamic timezone context
(t/with-time-configuration {:timezone "America/New_York"}
  (t/day-time-context (t/time->value (t/date 2024 6 15))))

;; Teleport between timezones
(def ny-time (t/time->value (t/date 2024 6 15 12 0 0)))
(def london-time (t/teleport ny-time "Europe/London"))

;; Custom weekend days and holidays
(t/with-time-configuration {:weekend-days #{5 6}      ; Fri/Sat weekend
                            :holiday? my-holiday-fn}   ; Custom holiday logic
  (t/weekend? some-date))
```

### **Multiple Calendar Systems**
```clojure
;; Switch calendar systems dynamically
(let [now (t/date)]
  (println
   (t/with-time-configuration {:calendar :hebrew}
     (select-keys (t/day-time-context (t/time->value now)) [:year :month :day-in-month])))
  (println
   (t/with-time-configuration {:calendar :islamic}
     (select-keys (t/day-time-context (t/time->value now)) [:year :month :day-in-month]))))

;; All calendars: :gregorian, :julian, :hebrew, :islamic
```

### **Holiday Integration**
```clojure
(require '[timing.holiday :as holiday])
(require '[timing.holiday.all]) ;; Add all holiday implementations

;; Check holidays by country
(holiday/? :us (t/time->value (t/date 2024 7 4)))     ; => true (Independence Day)
(holiday/? :us (t/time->value (t/date 2024 12 25)))   ; => true (Christmas)
(holiday/? :us (t/time->value (t/date 2024 1 1)))     ; => true (New Year's Day)

;; Get holiday name
(holiday/name (holiday/? :us (t/time->value (t/date 2024 7 4))) :en)  ; => "Independence Day"

;; ~200 countries supported!
```

### **Cron Scheduling**
```clojure
(require '[timing.cron :as cron])

;; Parse and work with cron expressions
(cron/next-timestamp (t/time->value (t/date 2024 6 15)) "0 0 12 * * ?")
; => Next occurrence of daily noon

(cron/valid-timestamp? (t/time->value (t/date 2024 6 15 12 0 0)) "0 0 12 * * ?")
; => true (matches cron pattern)

;; Generate future execution times
(take 5 (cron/future-timestamps start-time "0 0 9 * * MON"))
; => Next 5 Monday mornings at 9 AM
```

## 💡 Real-World Examples

### **Business Date Calculations**
```clojure
;; Add 30 business days to today
(def deadline 
  (adj/add-business-days (t/time->value (t/date)) 30))

;; Find all month-end Fridays in 2024
(def month-end-fridays
  (->> (range 1 13)
       (map #(t/time->value (t/date 2024 % 1)))
       (map #(adj/last-day-of-month-on-day-of-week % 5))))

;; Calculate working days between two dates
(def working-days
  (count (adj/business-days-in-range start-date end-date)))
```

### **Recurring Event Generation**
```clojure
;; Every 2nd Tuesday for next 6 months
(def bi-weekly-meetings
  (->> (adj/every-nth-day-of-week today 2 2)  ; Every 2nd Tuesday
       (take-while #(< % (t/add-months today 6)))
       (take 12)))

;; Quarterly board meetings (last Friday of quarter)
(def quarterly-meetings
  (->> [3 6 9 12]  ; End of quarters
       (map #(t/time->value (t/date 2024 % 1)))
       (map adj/end-of-month)
       (map #(adj/last-day-of-month-on-day-of-week % 5))))
```

### **Financial Calculations**
```clojure
;; Monthly payment dates (15th of each month)
(def payment-dates-2024
  (->> (range 1 13)
       (map #(t/time->value (t/date 2024 % 15)))
       (map #(if (adj/weekend? %) 
               (adj/previous-business-day %)  ; Move to Friday if weekend
               %))))

;; Quarter-end reporting dates
(def quarter-ends
  (->> (range 2024 2027)
       (mapcat #(map (fn [q] (adj/end-of-quarter 
                             (t/time->value (t/date % (* q 3) 1)))) 
                     [1 2 3 4]))))
```

## 🔧 Architecture

### **Modular Design**
```
timing/
├── core/        # Core time computation (timing.core)
├── timezones/   # IANA timezone database (timing.timezones)  
├── holidays/    # Country-specific holidays (timing.holiday)
├── cron/        # Cron scheduling (timing.cron)
└── util/        # Utility functions (timing.util, timing.adjusters)
```

### **Key Design Principles**

1. **Numeric Domain First** - All computation in milliseconds
2. **Immutable Values** - Never mutate, always return new values  
3. **Functional Composition** - Everything chains naturally
4. **Zero Dependencies** - Pure Clojure/ClojureScript
5. **Cross-Platform** - Identical behavior everywhere

### **Performance Characteristics**
- **Fast** - Numeric arithmetic is extremely efficient
- **Memory Efficient** - Work with primitives, not objects
- **GC Friendly** - Minimal object allocation during computation
- **Lazy** - Sequence operations are lazy by default

## 🆚 Comparison with Other Libraries

### **vs. Joda-Time/Java 8 Time API**
| Feature | Timing | Joda-Time/Java 8 |
|---------|--------|------------------|
| Dependencies | Zero | Heavy |
| Cross-platform | ✅ Clojure/ClojureScript | ❌ JVM only |
| Immutability | ✅ Native | ✅ Yes |
| Functional Style | ✅ Natural | ⚠️ Object-oriented |
| Calendar Systems | ✅ 4 built-in | ✅ Many |
| Parsing/Formatting | ⚠️ Host platform | ✅ Extensive |
| Numeric Domain | ✅ Core philosophy | ❌ Object-based |

### **vs. clj-time**
| Feature | Timing | clj-time |
|---------|--------|----------|
| Dependencies | Zero | Joda-Time |
| ClojureScript | ✅ Native | ❌ Limited |
| Learning Curve | Gentle | Steep |
| Time Zones | ✅ IANA DB | ✅ Joda zones |
| Holiday Support | ✅ 200 countries | ❌ None |
| Cron Support | ✅ Built-in | ❌ None |

## 📚 Documentation

## 🎨 Usage Patterns

### **Functional Pipeline Style**
```clojure
(->> employees
     (map :hire-date)
     (map t/time->value)  
     (map #(t/add-years % 1))         ; One year anniversary
     (map #(adj/next-day-of-week % 5)) ; Move to Friday
     (map t/value->time))             ; Back to dates
```

### **Threading Macro Style**  
```clojure
(-> (t/date 2024 1 1)
    t/time->value
    (t/add-months 6)
    (adj/start-of-quarter)
    (adj/next-business-day)
    t/value->time)
```

### **Sequence Generation**
```clojure
;; Generate all Mondays in 2024
(take-while #(< % (t/time->value (t/date 2025 1 1)))
            (adj/every-nth-day-of-week (t/time->value (t/date 2024 1 1)) 1 1))

;; All business days in a month
(adj/business-days-in-range (adj/start-of-month today) (adj/end-of-month today))
```

## ⚡ Performance Tips

1. **Stay in Numeric Domain** - Minimize conversions to/from Date objects
2. **Use Lazy Sequences** - Let Clojure's laziness work for you
3. **Batch Operations** - Process collections functionally
4. **Cache Computations** - Store frequently used values

```clojure
;; Good: Stay numeric  
(map #(+ % (t/days 1)) timestamps)

;; Less efficient: Convert back and forth
(map #(t/value->time (+ (t/time->value %) (t/days 1))) dates)
```

## 🚀 Getting Started

1. **Add Timing to your project**
2. **Start with basic date arithmetic**
3. **Explore calendar frames for UI components**
4. **Add temporal adjusters for complex logic**
5. **Use holidays and timezones as needed**

```clojure
;; Your first Timing program
(require '[timing.core :as t])

(def today (t/time->value (t/date)))
(def next-friday
  (-> today
      (t/midnight)
      (next-day-of-week 5)
      (+ (t/hours 17))))  ; 5 PM

(println "Next Friday at 5 PM:" (t/value->time next-friday))
```

## 📜 License

Copyright © 2018 Robert Gersak

Released under the MIT license.

---

**Built with ❤️ for the Clojure community**

*Timing: Because time is too important to be left to objects.*
