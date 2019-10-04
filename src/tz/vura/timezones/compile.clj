(ns vura.timezones.compile
  (:require
    [clojure.java.io :as io]
    [me.raynes.fs :as fs]
    ))



(def uri "https://data.iana.org/time-zones/tzdata-latest.tar.gz")


(defn download []
  (let [f (fs/temp-file "timezones" ".tar.gz")]
    (with-open [i (io/input-stream (java.net.URI. uri))
                o (io/output-stream f)]
      (io/copy i o))))
