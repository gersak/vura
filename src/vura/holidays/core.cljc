(ns vura.holidays.core
  (:require
   [clojure.data.json :as json]
   [vura.core
    :refer [day-time-context
            time->value]]))

(defn dispatch [dispatch _] dispatch)

(def test-file "../date-holidays/data/holidays.json")
;; Holiday? - Date in calendar for given context country, or religion or locale
;; working-day? - true or false for given holiday and context
;; Holiday name - holiday in one context can have different name in other context
;;                How to form this relations?

; (defprotocol HolidayProtocol
;   (holiday? [this dispatch] "Returns true if this is holiday. Namely for extending Date and PersistentMap")
;   (holiday-context [this dispatch] "Returns holiday context for given dispatch parameter"))

(def db (json/read-str (slurp test-file)))

(defmulti is-holiday?
  "Multimethod for extending holiday? function. 'dispatch' parameter is used to 
  dispatch to proper implementation of multimethod. In most cases it should be locale or country
  but it can as well be religion or culture dispatch (key)words or any other data type"
  dispatch)

(defmethod is-holiday? :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defmulti is-working-day?
  dispatch)

(defmethod is-working-day? :default [dispatch day-context]
  (throw (Exception. (str "Unkonwn dispatch " (pr-str dispatch) ". Are you sure that target multimethod implementation is loaded(required)?"))))

(defn holiday? [data dispatch]
  (is-holiday? dispatch (-> data time->value day-time-context)))

(defmulti holiday-name-impl
  (fn
    ([dispatch data] [dispatch dispatch])
    ([dispatch data translation] [dispatch translation])))

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
