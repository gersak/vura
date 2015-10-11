(defproject vura "0.5.0-b3"
  :aot :all
  :description "Vura is tiny library that is intendend for task
               managment and scheduling"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.taoensso/timbre "4.1.4"]
                 [kovacnica/dreamcatcher "1.0.4"]
                 [clj-time "0.11.0"]]
  :plugins [[lein-clojars "0.9.1"]
            [lein-ancient "0.5.4"]]
  :source-paths ["src"])
