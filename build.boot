(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources/timezones"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [adzerk/boot-test "1.2.0" :scope "test"]
                  [pandeiro/boot-http "0.8.3" :scope "test"]
                  [org.clojure/tools.logging "0.5.0-alpha" :scope "test"]
                  [kovacnica/dreamcatcher "1.0.7-SNAPSHOT"]
                  [org.clojure/core.async "0.4.474"]
                  [org.clojure/clojurescript "1.10.238"]
                  [instaparse "1.4.9" :scope "test"]
                  [boot-codox "0.10.3" :scope "test"]])

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[pandeiro.boot-http :refer [serve]])
(require '[codox.boot :refer [codox]])
(require '[adzerk.boot-test :refer :all])

(def +version+ "0.5.3-SNAPSHOT")

(task-options!
  pom {:project 'kovacnica/vura
       :version +version+}
  jar {:manifest {"created-by" "Robert Gersak"}}
  codox {:name "vura"
         :description "Zero dependency micro library to compute and manipulate time"
         :version +version+
         :filter-namespaces #{'vura.core}})

(deftask dev []
  (comp
    (wait)
    (repl :server true :port 54321)))

(deftask build
  "Build vura and install localy"
  []
  (comp (pom) (jar) (install)))

(deftask deploy []
  (set-env! :resource-paths #{"src"})
  (comp 
    (pom)
    (jar)
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))


(deftask dev-test []
  (set-env! :source-paths #{"src" "test"})
  (comp 
    (watch)
    (repl :server true :port 54321)))

(deftask deploy-core []
  (set-env! 
    :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]]
    :resource-paths #{"src"})
  (comp
    (sift 
      :include #{#"vura/core.cljc"})
    (pom 
      :project 'kovacnica/vura.core
      :version +version+)
    (jar
      :manifest {"created-by" "Robert Gersak"
                 "version" +version+})
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
