(defproject vura "0.5.0-b1"
  :description "Dreamcatcher is a realy small library that
               strives to simulate state machine behavior."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.taoensso/timbre "2.7.1"]
                 [org.roribib/dreamcatcher "1.0.2"]
                 [clj-time "0.6.0"]]
  :plugins [[lein-clojars "0.9.1"]
            [lein-ancient "0.5.4"]]
  :source-paths ["src"])
