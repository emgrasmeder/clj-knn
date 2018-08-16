(ns clj-knn.core-test
  (:require [clojure.test :refer :all]
            [clj-knn.core :as clj-knn]))

(deftest hamming-distance-test
  (testing "should calculate the euclidean distance of two vectors"
    (let [vect1 {:same "a" :different-thing1 "b" :different-thing2 "c"}
          vect2 {:same "a" :different-thing1 "a" :different-thing2 "d"}]
      (is (= 2 (clj-knn/hamming-distance vect1 vect2))))))

(deftest take-k-neighbors-test
  (testing "should return k neighbors sorted by score"
    (let [inputs [{:score 1} {:score 12} {:score 123} {:score 1234} {:score 12345}]]
      (is (= [{:score 1} {:score 12}] (clj-knn/take-k-neighbors 2 inputs))))))

(deftest tag-with-distance-test
  (testing "should return training data vector, tagged with distance from test-vector"
    (let [test-vector {:same "a" :different-thing1 "b" :different-thing2 "c"}
          vector-from-training-data {:same "a" :different-thing1 "a" :different-thing2 "d"}]
      (is (= (assoc vector-from-training-data :distance 2)
             (clj-knn/tag-with-distance test-vector vector-from-training-data))))))

(def training-data
  [{:id 1111 :feature1 "black" :feature2 "manual" :feature3 "earth" :feature4 "no"}
   {:id 1112 :feature1 "black" :feature2 "automatic" :feature3 "earth" :feature4 "no"}
   {:id 1114 :feature1 "green" :feature2 "manual" :feature3 "wind" :feature4 "yes"}
   {:id 1113 :feature1 "yellow" :feature2 "automatic" :feature3 "earth" :feature4 "no"}
   {:id 1118 :feature1 "blue" :feature2 "manual" :feature3 "fire" :feature4 "yes"}
   {:id 1119 :feature1 "red" :feature2 "manual" :feature3 "water" :feature4 "yes"}
   {:id 1116 :feature1 "red" :feature2 "automatic" :feature3 "wind" :feature4 "yes"}
   {:id 1117 :feature1 "white" :feature2 "autonomous" :feature3 "wind" :feature4 "no"}
   {:id 1115 :feature1 "green" :feature2 "manual" :feature3 "wind" :feature4 "no"}
   {:id 1120 :feature1 "red" :feature2 "automatic" :feature3 "fire" :feature4 "no"}])

(deftest rank-neighbors-test
  (testing "should return training data vectors, tagged with distances from test-vector"
    (let [test-vector {:id 1111 :feature1 "black" :feature2 "manual" :feature3 "earth" :feature4 "no"}]
      (is (every? #(not (nil? %)) (map :distance (clj-knn/rank-neighbors test-vector training-data))) ))))

(deftest end-to-end-test
    (testing "should find the match with identical specs, id 1111")
    (let [test-vector {:id 1111 :feature1 "black" :feature2 "manual" :feature3 "earth" :feature4 "no"}]
      (is (= (assoc test-vector :distance 0) (first (clj-knn/knn test-vector training-data 1))))))

