(ns clj-knn.core-test
  (:require [clojure.test :refer :all]
            [bond.james :as bond]
            [clj-knn.core :as clj-knn]))

(deftest hamming-distance-test
  (testing "should calculate the hamming distance of two vectors"
    (let [vect1 {:same "a" :different-thing1 "b" :different-thing2 "c"}
          vect2 {:same "a" :different-thing1 "a" :different-thing2 "d"}]
      (is (= 2 (clj-knn/hamming-distance vect1 vect2)))))
  (testing "should work on test-vectors with mismatched fields"
    (let [vect1 {:a 1 :b 2}
          vect2 {:a 1 :c 2}]
      (is (= 2 (clj-knn/hamming-distance vect1 vect2)))))
  (testing "should work on test-vectors with missing fields"
    (let [vect1 {:aaa 1 :ddd 3}
          vect2 {:aaa 1 :bbb 2 :ddd 3}]
      (is (= 1 (clj-knn/hamming-distance vect1 vect2))))))

(deftest take-k-neighbors-test
  (testing "should return k neighbors sorted by score"
    (let [inputs [{:score 1} {:score 12} {:score 123} {:score 1234} {:score 12345}]]
      (is (= [{:score 1} {:score 12}] (clj-knn/take-k-neighbors 2 inputs))))))

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

(deftest tag-with-distance-test
  (testing "should return training data vector, tagged with distance from test-vector"
    (let [test-vector (first training-data)
          vector-from-training-data (second training-data)]
      (is (= (assoc vector-from-training-data :distance 2)
             (clj-knn/tag-with-distance test-vector vector-from-training-data))))))

(deftest rank-neighbors-test
  (testing "should return training data vectors, tagged with distances from test-vector"
    (let [test-vector {:id 1111 :feature1 "black" :feature2 "manual" :feature3 "earth" :feature4 "no"}]
      (is (every? #(not (nil? %)) (map :distance (clj-knn/rank-neighbors test-vector training-data))) ))))

(deftest end-to-end-test
  (testing "should find the match with identical specs, id 1111"
    (let [test-vector {:id 1111, :feature1 "black"
                       :feature2 "manual", :feature3, "earth" :feature4 "no"}]
      (is (= (assoc test-vector :distance 0) (first (clj-knn/knn test-vector training-data 1))))))
  (testing "should find the match with identical specs, id 1111"
    (let [test-vector {:id 1111, :feature1, "black" :feature2 "ONE DIFFERENCE"
                       :feature3 "TWO DIFFERENCE", :feature4 "no"}]
      (is (= 2 (:distance (first (clj-knn/knn test-vector training-data 1))))))))


(deftest flatten-map-test
  (testing "should flatten arbitrarily deeply nested maps"
    (binding [clj-knn/*opts* {:flatten true}]
      (let [input {:a {:b {:c {:d [1 2]}}} :b {:c 2}}
            expected-output {:a.b.c.d [1 2] :b.c 2}]
        (is (= expected-output (clj-knn/flatten-map input))))))
  (testing "clj-knn should flatten if {:flatten true}"
    (is (= [{:a.b 2 :distance 2}] (clj-knn/knn {:a 1} [{:a {:b 2}}] 1 {:flatten true}))))
  (testing "clj-knn should not flatten if :flatten is nil}"
    (is (= [{:a {:b 2} :distance 1}] (clj-knn/knn {:a 1} [{:a {:b 2}}] 1)))))

(deftest flatten-maps-test
  (binding [clj-knn/*opts* {:flatten true}]
    (testing "should flatten lists of maps"
      (let [input [{:a {:b 1}}
                   {:c {:d 1}}]
            expected-output [{:a.b 1}
                             {:c.d 1}]]
        (is (= expected-output (clj-knn/flatten-maps input)))))))
