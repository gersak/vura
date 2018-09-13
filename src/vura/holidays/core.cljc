(ns vura.holidays.core
  (:require
   [vura.core :refer [day-context
                      date->value]]))

(defn dispatch [dispatch _] dispatch)

(defprotocol HolidayProtocol
  (holiday? [this dispatch] "Returns true if this is holiday. Namely for extending Date and PersistentMap")
  (holiday-context [this dispatch] "Returns holiday context for given dispatch parameter"))

(defmulti is-holiday?
  "Multimethod for extending holiday? function. 'dispatch' parameter is used to 
  dispatch to proper implementation of multimethod. In most cases it should be locale
  but it can as well be religion or culture dispatch (key)words or any other data type"
  dispatch)

(defmethod is-holiday? :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmulti is-working-day?
  dispatch)

(defmethod is-working-day? :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmethod holiday-context-impelemntation :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmulti holiday-name-implementation dispatch)

(defmethod holiday-name-implementation :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

#?(:clj
   (extend-protocol HolidayProtocol
     java.lang.Long
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.lang.Integer
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.lang.Number
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.lang.Float
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.lang.Double
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.math.BigInteger
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     java.math.BigDecimal
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     clojure.lang.BigInt
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     clojure.lang.Ratio
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     clojure.lang.ASeq
     (holiday? [this dispatch] (map (partial is-holiday? dispatch) this))
     (holiday-context [this dispatch] (map (partial holiday-context-impelemntation dispatch) this))
     clojure.lang.APersistentMap
     (holiday? [this dispatch] (is-holiday? dispatch this))
     (holiday-context [this dispatch] (holiday-context-impelemntation dispatch this))
     clojure.lang.APersistentVector
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     (holiday-context [this dispatch] (mapv (partial holiday-context-impelemntation dispatch) this))
     clojure.lang.APersistentSet
     (holiday-context [this dispatch] (set (map (partial holiday-context-impelemntation dispatch) this)))
     java.util.Date
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context date->value) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context date->value) this))
     java.time.Instant
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context date->value) (java.util.Date/from this)))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context date->value) (java.util.Date/from this))))
   :cljs
   (extend-protocol TimeValueProtocol
     number
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context) this))
     js/Date
     (holiday? [this dispatch] ((comp (partial is-holiday? dispatch) day-context date->value) this))
     (holiday-context [this dispatch] ((comp (partial holiday-context-impelemntation dispatch) day-context date->value) this))
     cljs.core/PersistentMap
     (holiday? [this dispatch] (is-holiday? dispatch this))
     (holiday-context [this dispatch] (holiday-context-impelemntation dispatch this))
     cljs.core/PersistentVector
     (holiday? [this dispatch] (mapv (partial is-holiday? dispatch) this))
     (holiday-context [this dispatch] (mapv (partial holiday-context-impelemntation dispatch) this))
     cljs.core/PersistentHashSet
     (holiday? [this dispatch] (set (map (partial is-holiday? dispatch) this)))
     (holiday-context [this dispatch] (set (map (partial holiday-context-impelemntation dispatch) this)))))

(defn holiday-contexts [holiday & dispatches]
  (reduce
   (fn [r d]
     (if-let [context (holiday-context holiday d)]
       (assoc r d context)
       r))
   nil
   dispatches))

(def locale ::locale)
(def religion ::religion)
(def country ::country)

(derive locale :vura.core/holiday)
(derive religion :vura.core/holiday)
(derive country :vura.core/holiday)
