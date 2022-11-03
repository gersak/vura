(ns vura.holidays
  (:refer-clojure :exclude [name])
  (:require
    [vura.core
     :refer [day-time-context
             time->value]]))


(defn static-holiday
  "Given d as 'day' and m as month, function will generate function that accepts
  vura.core/day-context value and returns true if day and month match input value"
  [d m]
  (fn [{:keys [day-in-month month]}]
    (and (= month m) (= day-in-month d))))

(defn dispatch [dispatch _] dispatch)

; (def test-file "../date-holidays/data/holidays.json")
;; Holiday? - Date in calendar for given context country, or religion or locale
;; working-day? - true or false for given holiday and context
;; Holiday name - holiday in one context can have different name in other context
;;                How to form this relations?

; (defprotocol HolidayProtocol
;   (holiday? [this dispatch] "Returns true if this is holiday. Namely for extending Date and PersistentMap")
;   (holiday-context [this dispatch] "Returns holiday context for given dispatch parameter"))

; (def db (json/read-str (slurp test-file)))

(defmulti is-holiday?
  "Multimethod for extending holiday? function. 'dispatch' parameter is used to 
  dispatch to proper implementation of multimethod. In most cases it should be locale or country
  but it can as well be religion or culture dispatch (key)words or any other data type"
  dispatch)

(defmethod is-holiday? :default [dispatch _]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmulti is-working-day?
  dispatch)

(defmethod is-working-day? :default [dispatch _]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defn holiday? [date dispatch]
  (is-holiday? dispatch (-> date time->value day-time-context)))

(defn name [definition dispatch]
  (when-some [{f :name} definition]
    (when (fn? f)
      (f dispatch))))

(defmulti holiday-name-impl
  (fn
    ([dispatch _] [dispatch dispatch])
    ([dispatch _ translation] [dispatch translation])))

; (defn holiday-contexts [holiday & dispatches]
;   (reduce
;    (fn [r d]
;      (if-let [context (holiday-context holiday d)]
;        (assoc r d context)
;        r))
;    nil
;    dispatches))

(def locale ::locale)
(def religion ::religion)
(def country ::country)

(derive locale :vura.core/holiday)
(derive religion :vura.core/holiday)
(derive country :vura.core/holiday)
