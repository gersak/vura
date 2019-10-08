(set-env!
  :source-paths #{"src/core"}
  :dependencies '[[boot-codox "0.10.5" :scope "test"]])

(require '[codox.boot :refer [codox]])

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

(deftask deploy []
  (set-env! 
    :dependencies '[]
    :resource-paths #{"src/generated" "src/core"})
  (comp
    (pom
      :project 'kovacnica/vura.core
      :version +version+)
    (jar)
    (push :repo "clojars"
          :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
