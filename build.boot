(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [pandeiro/boot-http "0.8.3" :scope "test"]
                  [org.clojure/tools.logging "0.5.0-alpha" :scope "test"]
                  [org.clojure/clojure "1.8.0"]
                  [kovacnica/dreamcatcher "1.0.7-SNAPSHOT"]
                  [org.clojure/core.async "0.4.474"]
                  [org.clojure/clojurescript "1.10.238"]])

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[pandeiro.boot-http :refer [serve]])
; (require '[adzerk.boot-reload :refer [reload]])
; (require '[adzerk.bootlaces :refer :all])

(def +version+ "0.5.2-SNAPSHOT")

(task-options!
  pom {:project 'kovacnica/vura
       :version +version+}
  jar {:manifest {"created-by" "Robert Gersak"}})

(deftask dev []
  (comp
    (repl :server true :port 54321)
    (wait)))

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
