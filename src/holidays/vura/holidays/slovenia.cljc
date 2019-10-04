(ns vura.holidays.slovenia
  #?(:cljs [vura.holidays.macros :refer [static-holiday]])
  (:require
   [vura.holidays.core :as c]
   #?(:clj [vura.holidays.macros :refer [static-holiday]])
   [vura.core :as v]
   [vura.holidays.catholic :as catholic]))

(static-holiday novo-leto? 1 1)
(static-holiday novo-leto2? 1 2)
(static-holiday presernov-dan? 2 8)
(static-holiday dan-boja-proti-okupatorju? 4 27)
(static-holiday dan-dela? 5 1)
(static-holiday dan-dela2? 5 2)
(static-holiday dan-drzavnosti? 6 25)
(static-holiday dan-reformacije? 10 31)
(static-holiday dan-samostojnosti-in-enotnosti? 12 26)

(def holiday->context
  {novo-leto? {:name "Novo leto"
               :translation/en "New Year"
               :working-day? false}
   novo-leto2? {:name "Novo leto (2)"
                :translation/en "New Year"
                :working-day? false}
   catholic/easter? {:name "Velika noč"
                     :working-day? false}
   catholic/easter-monday? {:name "Velikonočni ponedeljek"
                            :working-day? false}
   dan-boja-proti-okupatorju? {:name "Dan boja proti okupatorju"
                               :working-day? false}
   dan-dela? {:name "Dan dela"
              :working-day? false}
   dan-dela2? {:name "Dan dela (2)"
               :working-day? false}
   dan-drzavnosti? {:name "Dan državnosti"
                    :working-day? false}
   catholic/assumption-of-the-blessed-virgin-mary? {:name "Marijino vnebovzetje"
                                                    :working-day? false}
   dan-reformacije? {:name "Dan reformacije"
                     :working-day? false}
   catholic/all-saints? {:name "Dan spomina na mrtve"
                         :working-day? false}
   catholic/christmas? {:name "Božič"
                        :working-day? false}
   dan-samostojnosti-in-enotnosti? {:name "Dan samostojnosti in enotnosti"
                                    :working-day? false}})

(defn holiday? [{:keys [day-in-month month year] :as day-context}]
  (let [d (select-keys day-context [:day-in-month :month :year])]
    (boolean
     (some
      #(% day-context)
      (keys holiday->context)))))

(defmethod c/is-holiday? :country/slovenia [_ day-context] (holiday? day-context))

; (defmethod c/holiday-context-impelemntation :country/slovenia [_ data]
;   (let [d (-> data v/time->value v/day-context)]
;     (some
;      (fn [[f n]]
;        (when (f d) n))
;      holiday->context)))

(derive :locale/si :country/slovenia)
