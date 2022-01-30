(ns build
  (:require
    [clojure.tools.build.api :as b]))


(def lib 'com.github.gersak/vura.core)
(def version "0.5.7-SNAPSHOT")
(def basis (b/create-basis {:project "deps.edn"}))

(defn pom [_]
  (b/write-pom {:lib lib
                :version version
                :basis basis
                :src-dirs ["src"]}))


(defn deploy [_]
  (pom nil)
  (b/process {:command-args ["mvn" "deploy"]}))


