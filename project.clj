(defproject vura "0.5.0-b2"
  :aot :all
  :description "Dreamcatcher is a realy small library that
               strives to simulate state machine behavior."
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [org.roribib/dreamcatcher "1.0.2"]
                 [clj-time "0.8.0"]]
  :plugins [[lein-clojars "0.9.1"]
            [lein-ancient "0.5.4"]]
  :source-paths ["src"])
