(defproject vura "0.5.3"
  :description "Vura is tiny library that tackles time
               managment and scheduling"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [kovacnica/dreamcatcher "1.0.9-SNAPSHOT"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/tools.logging "0.4.0"]]
  :plugins [[lein-codox "0.10.7"]]
  :source-paths ["core/src" 
                 "cron/src" 
                 "timezones/generated" 
                 "holidays/src"])
