(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources/timezones"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [adzerk/boot-test "1.2.0" :scope "test"]
                  [pandeiro/boot-http "0.8.3" :scope "test"]
                  [cljfmt/cljfmt "0.6.0" :scope "test"]
                  [org.clojure/tools.logging "0.5.0-alpha" :scope "test"]
                  [org.clojure/data.json "0.2.6"]
                  [kovacnica/dreamcatcher "1.0.7"]
                  [org.clojure/core.async "0.4.474"]
                  [instaparse "1.4.9" :scope "test"]
                  [boot-codox "0.10.5" :scope "test"]
                  ;; Clojurescript
                  [org.clojure/clojurescript "1.10.339" :scope "test"]
                  [adzerk/boot-cljs-repl     "0.3.3" :scope "test"]
                  [com.cemerick/piggieback   "0.2.2" :scope "test"]
                  [weasel                    "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl   "0.2.13"]])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[pandeiro.boot-http :refer [serve]]
         '[codox.boot :refer [codox]]
         '[adzerk.boot-test :refer :all]
         '[cljfmt.core])

(def +version+ "0.5.3-SNAPSHOT")

(task-options!
  pom {:project 'kovacnica/vura
       :version +version+}
  jar {:manifest {"created-by" "Robert Gersak"}}
  codox {:name "vura"
         :description "Zero dependency micro library to compute and manipulate time"
         :version +version+
         :filter-namespaces #{'vura.core
                              'vura.timezones.db}})



(deftask build
  "Build vura and install localy"
  []
  (set-env! 
    :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]]
    :resource-paths #{"src"})
  (comp (pom) (jar) (install)))

(deftask deploy []
  (set-env! :resource-paths #{"src"})
  (comp
    (pom)
    (jar)
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))

(deftask deploy-core []
  (set-env! 
    :dependencies '[]
    :resource-paths #{"src"})
  (comp
    (sift 
      :include #{#"vura/core.cljc"
                 #"vura/timezones/db.cljc"})
    (pom 
      :project 'kovacnica/vura.core
      :version +version+)
    (jar
      :manifest {"created-by" "Robert Gersak"
                 "version" +version+})
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))

(deftask dev []
  (comp
    (wait)
    (repl :server true :port 54321)))

(deftask dev-cljs []
  (comp
    (watch)
    (cljs-repl 
      :nrepl-opts {:port 54321})
    (cljs)
    (target :dir #{"target"})))

(deftask dev-test []
  (set-env! :source-paths #{"src" "test"})
  (comp 
    (watch)
    ; (test)
    (repl :server true :port 54321)))
