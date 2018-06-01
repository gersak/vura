(ns vura.core-test
  (:refer-clojure :exclude [second])
  (:use clojure.test
        vura.core))

(def positive-number (rand 100000))
(def negative-number (rand -10000))


(deftest test-round-zero
  (is (every? zero? (map #(round-number positive-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value")
  (is (every? zero? (map #(round-number negative-number 0 %) [:up :down :ceil :floor])) "Failed rounding to zero value"))

(deftest test-round-number
  (are [x y] (= (round-number 10.75 0.5 x) y)
       :ceil 11.0
       :floor 10.5
       :up 11.0
       :down 10.5)
  (are [x y] (= (round-number 10.76 0.5 x) y)
       :ceil 11.0
       :floor 10.5
       :up 11.0
       :down 11.0)
  (are [x y] (= (round-number -10.75 0.5 x) y)
       :ceil -11.0
       :floor -10.5
       :up -11.0
       :down -10.5)
  (are [x y] (= (round-number -10.76 0.5 x) y)
       :ceil -11.0
       :floor -10.5
       :up -11.0
       :down -11.0)
  (is (= (round-number 10.12345681928 0.5) 10.0) "Rounding failed")
  (is (= (round-number 10.12345681928 0.1) 10.1) "Rounding failed")
  (is (= (round-number 122 3) 123) "Rounding failed")
  (is (= (round-number 10.12345681928 0.125) 10.125) "Rounding failed")
  )
