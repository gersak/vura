(ns vura.holidays.test
  (:require
   [vura.core :as vura]
   [vura.holidays :as h]
   vura.holidays.compiler
   vura.holidays.mk
   vura.holidays.hr))


(h/holiday? (vura/date 2023 12 25) :hr)
(h/name (h/holiday? (vura/date 2023 12 25) :hr) :en)