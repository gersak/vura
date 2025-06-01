(ns timing.core.hebrew)


(defn- ^:no-doc hebrew-c1 [x]
  (round-number
    (/ (inc (* 235 x)) 19)
    1 :floor))

(defn- ^:no-doc hebrew-q [x]
  (round-number
    (/ (hebrew-c1 x) 1095)
    1 :floor))

(defn- ^:no-doc hebrew-r [x] (mod (hebrew-c1 x) 1095))

(defn- ^:no-doc hebrew-v1 [x]
  (+
   (* 29 (hebrew-c1 x))
   (round-number (/ (+ 12084 (* 13753 (hebrew-c1 x))) 25920) 1 :floor)))

(defn- ^:no-doc hebrew-v2 [x]
  (+
   (hebrew-v1 x)
   (mod
     (round-number
       (/ (* 6 (mod (hebrew-v1 x) 7)) 7)
       1 :floor)
     2)))

(defn- ^:no-doc hebrew-L2 [x] (- (hebrew-v2 (inc x)) (hebrew-v2 x)))

(defn- ^:no-doc hebrew-v3 [x]
  (* 2
     (mod
       (round-number (/ (+ (hebrew-L2 x) 19) 15) 1 :floor)
       2)))

(defn- ^:no-doc hebrew-v4 [x]
  (mod
    (round-number (/ (+ 7 (hebrew-L2 (dec x))) 15) 1 :floor)
    2))

(defn- ^:no-doc hebrew-c2 [x] (+ (hebrew-v2 x) (hebrew-v3 x) (hebrew-v4 x)))
(defn- ^:no-doc hebrew-L [x] (- (hebrew-c2 (inc x)) (hebrew-c2 x)))
(defn- ^:no-doc hebrew-c8 [x] (mod (round-number (/ (+ 7 (hebrew-L x)) 2) 1 :floor) 15))
(defn- ^:no-doc hebrew-c9 [x] (* -1 (mod (round-number (/ (- 385 (hebrew-L x)) 2)  1 :floor) 15)))

(defn- ^:no-doc hebrew-c3 [x m]
  (+
   (round-number
     (/ (+ 7 (* 384 m)) 13)
     1 :floor)
   (*
    (hebrew-c8 x)
    (round-number (/ (+ m 4) 12) 1 :floor))
   (*
    (hebrew-c9 x)
    (round-number (/ (+ m 3) 12) 1 :floor))))

(defn- ^:no-doc hebrew-c4 [x m] (+ (hebrew-c2 x) (hebrew-c3 x m)))


(defn- ^:no-doc hebrew-date->value
  [{:keys [day-in-month month year]
    :or {month 1 day-in-month 1}}]
  (let [c0 (round-number
             (/ (- 13 month) 7)
             1 :floor)
        x1 (+ c0 (dec year))
        z4 (dec day-in-month)
        J (+
           347821
           (hebrew-c4 x1 (dec month))
           z4)]
    (jcdn->value J)))
;;
(defn ^:no-doc value->hebrew-date
  [value]
  (let [J (value->jcdn value)
        y4 (- J 347821)
        q (round-number (/ y4 1447) 1 :floor)
        r (mod y4 1447)
        y1' (+ (* 49 q) (round-number (/ (+ (* 23 q) (* 25920 r) 13835)
                                         765433)
                                      1 :floor))
        gama1 (inc y1')
        epsilon1 (round-number (/ (+ (* 19 gama1) 17)
                                  235)
                               1 :floor)
        u1 (- gama1 (round-number (/ (inc (* 235 epsilon1)) 19) 1 :floor))
        c41' (hebrew-c4 epsilon1 u1)
        ro1 (- y4 c41')
        gama2 (+ gama1 (round-number (/ ro1 33) 1 :floor))
        epsilon2 (round-number (/ (+ (* 19 gama2) 17)
                                  235)
                               1 :floor)
        u2 (- gama2 (round-number (/ (inc (* 235 epsilon2)) 19) 1 :floor))
        c42' (hebrew-c4 epsilon2 u2)
        ro2 (- y4 c42')
        gama3 (+ gama2 (round-number (/ ro2 33) 1 :floor))
        epsilon3 (round-number (/ (+ (* 19 gama3) 17)
                                  235)
                               1 :floor)
        u3 (- gama3 (round-number (/ (inc (* 235 epsilon3)) 19) 1 :floor))
        z4 (- y4 (hebrew-c4 epsilon3 u3))
        c (round-number (/ (- 12 u3) 7) 1 :floor)
        j (long (- (inc epsilon3) c))
        m (long (inc u3))
        d (long (inc z4))
        year-length (long (hebrew-L epsilon3))
        month-mapping (case year-length
                        353 (zipmap (range 1 13)
                                    [30 29 30 29 30 29 30 29 29 29 30 29])
                        354 (zipmap (range 1 13)
                                    [30 29 30 29 30 29 30 29 30 29 30 29])
                        355 (zipmap (range 1 13)
                                    [30 29 30 29 30 29 30 30 30 29 30 29])
                        383 (zipmap (range 1 14)
                                    [30 29 30 29 30 29 30 30 30 29 30 30 29])
                        384 (zipmap (range 1 13)
                                    [30 29 30 29 30 29 30 29 30 29 30 30 29])
                        385 (zipmap (range 1 13)
                                    [30 29 30 29 30 29 30 30 30 29 30 30 29]))
        last-day (get month-mapping m)]
    {:year j
     :month m
     :day-in-month d
     :year-lenght year-length
     :days-in-month last-day
     :days-in-year year-length
     :first-day-in-month? (= 1 d)
     :last-day-in-month? (= last-day d)}))
