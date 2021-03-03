# memoize-ttl

A `clojure.core/memoize` version with a time-to-live strategy.
It mimics the interface of memoize and it is much more simplistic than [core.cache](https://github.com/clojure/core.cache).
If you need more control on the cache, consider switching to core.cache instead.

This library has no dependencies (besides clojure).
## Usage

Just pass the function you want to memoize to `memoize-ttl`, this function also receives a :ttl optional argument in seconds (defaults to 1 hour).

```clojure
(require '[memoize-ttl.core :refer [memoize-ttl]])

(let [f (memoize-ttl (fn []
                           (let [now (java.time.Instant/now)]
                             (println "executing function f at: " now)
                             now)) :ttl 10)
          changes (atom 0)
          last-ret (atom nil)]
      (doseq [i (range 20)]
        (let [ret (f)]
          (println "f returns: " ret)
          (when (not= ret @last-ret)
            (do (swap! changes inc)
                (reset! last-ret ret)))
          (Thread/sleep 1000))))
```

We also provides the `memoize-ttl-clean` function which returns a vector of the memoized function and a function to wipe the entire cache.

## License

Copyright © 2021 Lucas Severo.

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
