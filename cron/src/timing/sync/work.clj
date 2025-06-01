(in-ns 'timing.jobs)

(defmacro shared-work-template
  [coll fun threads]
  `(let [tasks# (for [x# ~coll] (fn [] (~fun x#)))
         pool# (Executors/newFixedThreadPool ~threads)
         results# (.invokeAll pool# tasks#)
         final-result# (mapv #(.get %) results#)]
     (.shutdown pool#)
     final-result#))

(defn shared-work
  ([coll fun] (shared-work coll fun 10))
  ([coll fun threads] (shared-work-template coll fun threads)))
