(ns vura.holidays.catholic
  #?(:cljs [vura.holidays.macros :refer [static-holiday]])
  (:require
   [vura.core :as vura]
   [vura.holidays.core :as c]
   #?(:clj [vura.holidays.macros :refer [static-holiday]])))

(static-holiday epiphany? 1 6)

(def easter
  (memoize
   (fn [year]
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
       {:year year :month n :day-in-month (+ p 1)}))))

(defn easter? [{:keys [year] :as day-context}]
  (= (easter year) (select-keys day-context [:year :month :day-in-month])))

(def easter+
  (memoize
   (fn [year delta]
     (let [easter-day-context (easter year)
           context (+ (vura/context->value easter-day-context) delta)]
       {:year year
        :month (vura/month? context)
        :day-in-month (vura/day-in-month? context)}))))

(defn mardi-gras [{:keys [year]}]
  (let [ash-week (iterate (partial + vura/day) (vura/context->value (easter+ year (vura/weeks -7))))
        corpus-christi (first
                        (filter
                         (comp #{2} vura/day?)
                         ash-week))]
    {:year year
     :month (vura/month? corpus-christi)
     :day-in-month (vura/day-in-month? corpus-christi)}))

(defn mardi-gras? [day-context]
  (= (mardi-gras day-context) (select-keys day-context [:year :month :day-in-month])))

(defn ash-wednesday [{:keys [year]}]
  (let [ash-week (iterate (partial + vura/day) (vura/context->value (easter+ year (vura/weeks -7))))
        corpus-christi (first
                        (filter
                         (comp #{3} vura/day?)
                         ash-week))]
    {:year year
     :month (vura/month? corpus-christi)
     :day-in-month (vura/day-in-month? corpus-christi)}))

(defn ash-wednesday? [day-context]
  (= (ash-wednesday day-context) (select-keys day-context [:year :month :day-in-month])))

(defn trinity-sunday [year]
  (easter+ year (vura/weeks 8)))

(defn trinity-sunday? [{:keys [year] :as day-context}]
  (= (trinity-sunday year) (select-keys day-context [:year :month :day-in-month])))

(defn corpus-christi [year]
  (let [trinity-sunday-context (trinity-sunday year)
        trinity-week (iterate (partial + vura/day) (vura/context->value trinity-sunday-context))
        corpus-christi (first
                        (filter
                         (comp #{4} vura/day?)
                         trinity-week))]
    {:year year
     :month (vura/month? corpus-christi)
     :day-in-month (vura/day-in-month? corpus-christi)}))

(defn corpus-christi?
  [{:keys [year] :as day-context}]
  (= (corpus-christi year) (select-keys day-context [:year :month :day-in-month])))

(defn palm-sunday? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/weeks -1)) (select-keys day-context [:year :month :day-in-month])))

(defn moundy-thursday? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/days -3)) (select-keys day-context [:year :month :day-in-month])))

(defn good-friday? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/days -2)) (select-keys day-context [:year :month :day-in-month])))

(defn easter-sunday? [{:keys [year] :as day-context}]
  (= (easter+ year -1) (select-keys day-context [:year :month :day-in-month])))

(defn easter-monday? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/days 1)) (select-keys day-context [:year :month :day-in-month])))

(defn ascension-of-jesus? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/days 39)) (select-keys day-context [:year :month :day-in-month])))

(defn pentecost? [{:keys [year] :as day-context}]
  (= (easter+ year (vura/days 49)) (select-keys day-context [:year :month :day-in-month])))

(static-holiday assumption-of-the-blessed-virgin-mary? 8 15)
(static-holiday all-saints? 11 1)
(static-holiday christmas? 12 25)

(def holiday->context
  {easter? {:name "Easter"}
   epiphany? {:name "Epiphany"}
   mardi-gras? {:name "Mardi Gras"}
   ash-wednesday? {:name "Ash Wednesday"}
   palm-sunday? {:name "Palm Sunday"}
   moundy-thursday? {:name "Moundy Thursday"}
   good-friday? {:name "Good Friday"}
   easter-monday? {:name "Easter Monday"}
   corpus-christi? {:name "Corpus Christi"}
   assumption-of-the-blessed-virgin-mary? {:name "Assumption of the Blessed Virgin Mary"}
   ascension-of-jesus? {:name "Ascension of Jesus"}
   pentecost? {:name "Pentecost"}
   all-saints? {:name "All Saint's Day"}
   christmas? {:name "Christmas"}})

(defmethod c/is-holiday? :religion/christian [_ day-context]
  (let [d (select-keys day-context [:day-in-month :month :year])]
    (boolean
     (some
      #(% day-context)
      (keys holiday->context)))))

(defmethod c/holiday-name-impl [:country/croatia :country/croatia])

; (defmethod c/holiday-context-impelemntation :religion/christian [_ data]
;   (let [d (-> data vura/time->value vura/day-context)]
;     (some
;      (fn [[f n]]
;        (when (f d) n))
;      holiday->context)))
