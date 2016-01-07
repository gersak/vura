(defproject vura "0.5.1"
  :aot :all
  :description "Vura is tiny library that is intendend for task
               managment and scheduling"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [kovacnica/dreamcatcher "1.0.4"]
                 [org.clojure/core.async "0.2.374"]
                 [com.andrewmcveigh/cljs-time "0.3.14"]
                 [clj-time "0.11.0"]]
  :source-paths ["src"]
  :cljsbuild
  {:builds [{:source-paths ["src"]
             :compiler {:output-to "war/javascript/vura.js"}
             :optimizations :whitespace
             :pretty-print true}]})
