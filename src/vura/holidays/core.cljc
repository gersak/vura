(ns vura.holidays.core
  (:require
   [vura.core :refer [day-context
                      date->value]]))

(defprotocol HolidayProtocol
  (holiday? [this dispatch] "Returns true if this is holiday. Namely for extending Date and PersistentMap"))

(defmulti is-holiday?
  "Multimethod for extending holiday? function. 'dispatch' parameter is used to 
  dispatch to proper implementation of multimethod. In most cases it should be locale
  but it can as well be religion or culture dispatch (key)words or any other data type"
  (fn [dispatch day-context] dispatch))

(defmethod is-holiday? :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmulti holiday-name
  "Function returns holiday name for input day-context based on dispatch value. For more info on dispatch look at 'vura.holidays.core/holiday?'"
  (fn [dispatch data]
    {:pre [(holiday? data dispatch)]}
    dispatch))

(defn holiday-context [day-context & dispatches])

#?(:clj
   (extend-protocol HolidayProtocol
     java.lang.Long
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.lang.Integer
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.lang.Number
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.lang.Float
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.lang.Double
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.math.BigInteger
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     java.math.BigDecimal
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     clojure.lang.BigInt
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     clojure.lang.Ratio
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     clojure.lang.ASeq
     (holiday? [this dispatch] (map (partial is-holiday? dispatch) this))
     (holiday? [this dispatch] (map (partial is-holiday? dispatch) this))
     clojure.lang.APersistentMap
     (holiday? [this dispatch] (is-holiday? dispatch this))
     clojure.lang.APersistentVector
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     clojure.lang.APersistentSet
     (holiday? [this dispatch] (set (map (partial is-holiday? dispatch) this)))
     (holiday? [this dispatch] (set (map (partial is-holiday? dispatch) this)))
     java.util.Date
     (holiday? [this dispatch]
       (println "DISPAtCH: " dispatch)
       ((comp (partial is-holiday? dispatch) day-context date->value) this))
     java.time.Instant
     (holiday? [this dispatch]
       ((comp (partial is-holiday? dispatch) day-context date->value)
        (java.util.Date/from this))))
   :cljs
   (extend-protocol TimeValueProtocol
     number
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     js/Date
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context date->value) this))
     cljs.core/PersistentMap
     (holiday? [this dispatch] (is-holiday? dispatch this))
     cljs.core/PersistentVector
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     cljs.core/PersistentHashSet
     (holiday? [this dispatch] (set (map (partial is-holiday? dispatch) this)))
     (holiday? [this dispatch] (set (map (partial is-holiday? dispatch) this)))))
