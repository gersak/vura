(ns vura.holidays.test
  (:require
   [vura.core :as vura]
   [vura.holidays :as h]
   vura.holidays.compiler
   vura.holidays.mk
   vura.holidays.hr))


(h/holiday? (vura/date 2023 12 25) :hr)
(h/name (h/holiday? (vura/date 2023 12 25) :hr) :hr)
(h/holiday? (vura/date 2023 12 25) :us)
(h/name (h/holiday? (vura/date 2023 12 25) :us) :hr)
(h/name (h/holiday? (vura/date 2022 11 8) :us) :en)
(h/holiday? (vura/date 2023 11 7) :us)
(h/holiday? (vura/date 2023 1 1) :tf)