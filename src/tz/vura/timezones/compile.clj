(ns vura.timezones.compile
  (:require
    [clojure.java.io :as io]
    [vura.timezones.parser :as parser]
    [me.raynes.fs :as fs]
    [me.raynes.fs.compression 
     :refer [gunzip untar]]))



(def uri "https://data.iana.org/time-zones/tzdata-latest.tar.gz")


(defn download 
  "Function downloads latest tzdata file and returns file reference"
  []
  (let [f (fs/temp-file "timezones" ".tar.gz")]
    (with-open [i (io/input-stream (java.net.URI. uri))
                o (io/output-stream f)]
      (io/copy i o))
    f))

(defn extract
  [f]
  (let [f' (fs/temp-file "timezones" ".tar")
        f'' (fs/temp-dir "timezones")] 
    (gunzip f f')
    (untar f' f'')
    (fs/delete f)
    (fs/delete f')
    f''))



(defn -main []
  (let [tz-dir (extract (download))]
    (binding [parser/*tz-dir* (str tz-dir "/")]
      #_(parser/create-locale-data)
      (parser/create-timezone-data))))


(comment
  (def x 
    (binding [parser/*tz-dir* (str tz-dir "/")]
      #_(parser/create-locale-data)
      (parser/create-timezone-data)))
  (binding [parser/*tz-dir* (str tz-dir "/")]
    ; (parser/extract-zones (parser/read-zone :europe))
    ; (parser/extract-rules (parser/read-zone :europe))
    (parser/extract-data (parser/read-zone :europe))
    ))
