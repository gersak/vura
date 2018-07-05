(ns vura.timezones
  (:refer-clojure :exclude [second])
  (:require
    [vura.timezones.db :refer [get-zone get-rule]]))


(defn get-timezone-attribute [value timezone attribute]
  (let [{:keys [current history]} (get-zone timezone)]
    (if (>= value (:from current))
      (get current attribute)
      (get
        (last
          (filter
            #(> (:until %) value)
            (reverse history)))
        attribute))))

(defn get-timezone-offset [value timezone]
  (get-timezone-attribute value timezone :offset))

(defn get-timezone-rule [value timezone]
  (when-let [rule (get-timezone-attribute value timezone :rule)]
    (case rule
      "-" nil
      rule)))


(comment
  (get-zone "Europe/Zagreb")
  (get-zone "Pacific/Apia")
  (time (get-zone "Pacific/Pago_Pago")))
