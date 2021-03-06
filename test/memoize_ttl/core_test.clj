(ns memoize-ttl.core-test
  (:require [clojure.test :refer :all]
            [memoize-ttl.core :refer :all])
  (:import [java.time Instant]))

(deftest memoize-ttl-test
  (testing "testing memoize-ttl"
    (let [f (memoize-ttl (fn []
                           (let [now (Instant/now)]
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
          (Thread/sleep 1000)))
      (is (= @changes 2)))))

(deftest memoize-ttl-parallel-start-test
  (testing "testing parallel execution at start"
    (is (every? (partial = 1) (doall (pmap (memoize-ttl (fn [x]
                                                          (print x)
                                                          x)) (repeat 10 1)))))
    (is (= "1" (with-out-str (doall (pmap (memoize-ttl (fn [x]
                                                         (print x)
                                                         x)) (repeat 10 1))))))))
