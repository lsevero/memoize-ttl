(defproject lsevero/memoize-ttl "1.0.1"
  :description "A simple caching TTL strategy"
  :url "https://github.com/lsevero/memoize-ttl"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.2"]]
  :profiles {:dev {:plugins [[cider/cider-nrepl "0.22.3"]]
                   :global-vars {*warn-on-reflection* true}
                   :source-paths ["src" "test"]
                   :repl-options {:init-ns memoize-ttl.core}}}
  :source-paths ["src"]
  )
