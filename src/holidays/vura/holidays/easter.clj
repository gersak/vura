(ns vura.holidays.easter)

;; From 
;; https://github.com/eivindw/clj-easter-day/blob/master/src/easter_day.clj
;; https://en.wikipedia.org/wiki/Computus

(defn catholic-easter [{:keys [year] :as day-context}]
  (let [a (mod year 19)
        b (quot year 100)
        c (mod year 100)
        d (quot b 4)
        e (mod b 4)
        f (quot (+ b 8) 25)
        g (quot (+ (- b f) 1) 3)
        h (mod (+ (* 19 a) (- b d g) 15) 30)
        i (quot c 4)
        k (mod c 4)
        l (mod (- (+ 32 (* 2 e) (* 2 i)) h k) 7)
        m (quot (+ a (* 11 h) (* 22 l)) 451)
        n (quot (+ h (- l (* 7 m)) 114) 31)
        p (mod (+ h (- l (* 7 m)) 114) 31)]
    {:year year :month n :day (+ p 1)}))

(defn orthodox-easter [{:keys [year] :as day-context}]
  (let [a (mod year 4)
        b (mod year 7)
        c (mod year 19)
        d (mod (+ (* 19 c) 15) 30)
        e (mod (- (+ (* 2 a) (* 4 b) 34) d) 7)
        month (quot (+ d e 114) 31)
        day (+ 14 (mod (+ d e 114) 31))
        day-overload (quot day 30)]
    (if (pos? day-overload)
      {:year year :month (inc month) :day (mod day 30)}
      {:year year :month month :day day})))
