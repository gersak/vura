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

(def holiday->name
  {nova-godina? "Nova Godina"
   catholic/epiphany? "Sveta tri kralja"
   catholic/easter? "Uskrs"
   catholic/easter-monday? "Uskršnji ponedjeljak"
   praznik-rada? "Praznik rada"
   catholic/corpus-christi? "Tijelovo"
   dan-antifasiticke-borbe? "Dan antifašističke borbe"
   dan-drzavnosti? "Dan državnosti"
   dan-pobjede-i-domovinske-zahvalnosti? "Dan pobjede i domovinske zahvalnosti"
   catholic/assumption-of-the-blessed-virgin-mary? "Velika Gospa"
   dan-neovisnosti? "Dan neovisnosti"
   catholic/all-saints? "Dan svih svetih"
   catholic/christmas? "Božić"
   sveti-stjepan? "Sveti Stjepan"})

(defn holiday? [{:keys [day-in-month month year] :as day-context}]
  (let [d (select-keys day-context [:day-in-month :month :year])]
    (boolean
     (some
      #(% day-context)
      (keys holiday->name)))))

(defmethod c/is-holiday? :country/croatia [_ day-context] (holiday? day-context))

(defmethod c/holiday-name :country/croatia [_ data]
  (let [d (-> data v/time->value v/day-context)]
    (some
     (fn [[f n]]
       (when (f d) n))
     holiday->name)))

(derive :locale/hr :country/croatia)
