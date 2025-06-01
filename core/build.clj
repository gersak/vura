(ns build
  (:require
    [clojure.tools.build.api :as b]))


(def lib 'com.github.gersak/timing.core)
(def version "0.5.7-SNAPSHOT")
(def basis (b/create-basis {:project "deps.edn"}))
(def target "target")

(defn pom [_]
  (b/write-pom {:lib lib
                :version version
                :basis basis
                :resource-dirs ["src" "../timezones/src"]
                :target "./"}))

(defn jar [_]
  (pom nil)
  (b/process {:command-args ["mvn" "jar"]}))

(defn deploy [_]
  (pom nil)
  (b/process {:command-args ["mvn" "deploy"]}))


