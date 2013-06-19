(in-ns 'vura.jobs)

(def ^:dynamic *workers-number* 10)

(def |shared-workers| (Executors/newFixedThreadPool *workers-number*))

(defn shared-work 
  ([coll fun] (shared-work coll fun nil))
  ([coll fun timeout]
   (let [tasks (for [x coll] #(fun x))
         results (.invokeAll |shared-workers| tasks)]
     (for [x results]
       (.get x)))))

