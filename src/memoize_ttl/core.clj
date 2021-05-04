(ns memoize-ttl.core
  (:require [clojure.test :refer [is]]))


(defn memoize-ttl
  "Returns a memoized version of a referentially transparent function. The
  memoized version of the function keeps a cache of the mapping from arguments
  to results and, when calls with the same arguments are repeated often, has
  higher performance at the expense of higher memory use.
  
  The cache is invalidated after ttl seconds (defaults to 1 hour).
  TTL argument needs to be a positive number.
  If the function is called after the cache is invalidated the function will be called again and the cache will be updated.

  The timer used in this function is monotonic, it is always increasing and is not related to any notion of system or wall clock.

  Differences in successive calls that span greater than approximately 292 years (2^63 nanoseconds) will not correctly compute elapsed time due to numerical overflow. 
  "
  [f & {:keys [ttl] :or {ttl 3600}}]
  {:pre [(is (pos? ttl))]}
  (let [mem (atom (transient {}))]
    (fn [& args]
      (locking mem 
        (let [[_ {:keys [ret inst]} :as entry] (find @mem args)
              now (System/nanoTime)]
          (if (or (nil? entry)
                  (> (- now inst) (* 1000000000 ttl)))
            (let [ret (apply f args)]
              (swap! mem assoc! args {:ret ret
                                      :inst now})
              ret)
            ret))))))

(defn memoize-ttl-clean
  "Returns a vector of a memoized version of a referentially transparent function and a function to clean the cache.
  The memoized version of the function keeps a cache of the mapping from arguments
  to results and, when calls with the same arguments are repeated often, has
  higher performance at the expense of higher memory use.

  The cache is invalidated after ttl seconds (defaults to 1 hour).
  TTL argument needs to be a positive number.
  If the function is called after the cache is invalidated the function will be called again and the cache will be updated.

  The timer used in this function is monotonic, it is always increasing and is not related to any notion of system or wall clock.

  Differences in successive calls that span greater than approximately 292 years (2^63 nanoseconds) will not correctly compute elapsed time due to numerical overflow. 
  "
  [f & {:keys [ttl] :or {ttl 3600}}]
  {:pre [(is (pos? ttl))]}
  (let [mem (atom (transient {}))]
    [(fn [& args]
       (locking mem
         (let [[_ {:keys [ret inst]} :as entry] (find @mem args)
               now (System/nanoTime)]
           (if (or (nil? entry)
                   (> (- now inst) (* 1000000000 ttl)))
             (let [ret (apply f args)]
               (swap! mem assoc! args {:ret ret
                                       :inst now})
               ret)
             ret))))
     (fn [] (reset! mem (transient {})))
     ]))

