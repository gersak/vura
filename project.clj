(defproject vura "0.5.1"
  ;:aot :all
  :repl-options {:init-ns vura.async.jobs}
  :description "Vura is tiny library that is intendend for task
               managment and scheduling"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.taoensso/timbre "4.1.4"]
                 [kovacnica/dreamcatcher "1.0.4"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.andrewmcveigh/cljs-time "0.3.14"]
                 [clj-time "0.11.0"]]
  :source-paths ["src"])
