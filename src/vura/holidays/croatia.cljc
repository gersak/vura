(ns vura.holidays.croatia
  #?(:cljs [vura.holidays.macros :refer [static-holiday]])
  (:require
   [vura.holidays.core :as c]
   #?(:clj [vura.holidays.macros :refer [static-holiday]])
   [vura.core :as v]
   [vura.holidays.catholic :as catholic]))

(static-holiday praznik-rada? 5 1)
(static-holiday nova-godina? 1 1)
(static-holiday dan-antifasiticke-borbe? 6 22)
(static-holiday dan-drzavnosti? 6 25)
(static-holiday dan-pobjede-i-domovinske-zahvalnosti? 8 5)
(static-holiday dan-neovisnosti? 10 8)
(static-holiday sveti-stjepan? 12 26)

(def holiday->context
  {nova-godina? {:name "Nova Godina"
                 :working-day? false}
   catholic/epiphany? {:name "Sveta tri kralja"
                       :working-day? false}
   catholic/easter? {:name "Uskrs"
                     :working-day? false}
   catholic/easter-monday? {:name "Uskršnji ponedjeljak"
                            :working-day? false}
   praznik-rada? {:name "Praznik rada"
                  :working-day? false}
   catholic/corpus-christi? {:name "Tijelovo"
                             :working-day? false}
   dan-antifasiticke-borbe? {:name "Dan antifašističke borbe"
                             :working-day? false}
   dan-drzavnosti? {:name "Dan državnosti"
                    :working-day? false}
   dan-pobjede-i-domovinske-zahvalnosti? {:name "Dan pobjede i domovinske zahvalnosti"
                                          :working-day? false}
   catholic/assumption-of-the-blessed-virgin-mary? {:name "Velika Gospa"
                                                    :working-day? false}
   dan-neovisnosti? {:name "Dan neovisnosti"
                     :working-day? false}
   catholic/all-saints? {:name "Dan svih svetih"
                         :working-day? false}
   catholic/christmas? {:name "Božić"
                        :working-day? false}
   sveti-stjepan? {:name "Sveti Stjepan"
                   :working-day? false}})

(defn holiday? [{:keys [day-in-month month year] :as day-context}]
  (let [d (select-keys day-context [:day-in-month :month :year])]
    (boolean
     (some
      #(% day-context)
      (keys holiday->context)))))

(defmethod c/is-holiday? :country/croatia [_ day-context] (holiday? day-context))

(defmethod c/holiday-context-impelemntation :country/croatia [_ data]
  (let [d (-> data v/time->value v/day-context)]
    (some
     (fn [[f n]]
       (when (f d) n))
     holiday->context)))

(derive :locale/hr :country/croatia)
(derive :locale/hr c/locale)
(derive :country/croatia c/country)
(derive :vura.core.holiday/locale :vura.core/holiday)
