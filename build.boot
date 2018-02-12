(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                  [adzerk/boot-reload "0.4.2" :scope "test"]
                  [adzerk/bootlaces "0.1.13" :scope "test"]
                  [pandeiro/boot-http "0.7.3" :scope "test"]
                  [org.clojure/tools.logging "0.4.0" :scope "test"]
                  [org.clojure/clojure "1.8.0"]
                  [kovacnica/dreamcatcher "1.0.4"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                  [org.clojure/clojurescript "1.7.145"]])

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[pandeiro.boot-http :refer [serve]])
; (require '[adzerk.boot-reload :refer [reload]])
; (require '[adzerk.bootlaces :refer :all])

(def +version+ "0.5.3-SNAPSHOT")

(task-options!
  push {:repo-map 
        {:url "https://clojars.org/kovacnica/vura"}}
  pom {:project 'kovacnica/dreamcatcher
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
  (set-env! 
    :resource-paths #{"src-cljc"})
  (comp 
    (build)
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
