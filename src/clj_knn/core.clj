(ns clj-knn.core
  (:require [clojure.set :as set])
  (:gen-class))

(defn hamming-distance
  [map1 map2]
  (let [all-keys (set/union (set (keys map1)) (set (keys map2)))
        is-diff? (map (fn [k] (= (get map1 k) (get map2 k))) all-keys)]
    (count (filter #(not (true? %)) is-diff?))))

(defn take-k-neighbors
  [k neighbors]
  (take k (sort-by :distance neighbors)))

(defn tag-with-distance
  [test-vector vector-from-training-data ]
  (assoc vector-from-training-data :distance (hamming-distance test-vector vector-from-training-data)))

(defn rank-neighbors
  [test-vector training-data]
  (map (partial tag-with-distance test-vector) training-data))

(defn knn
  [test-vector training-data k]
  (take-k-neighbors k (rank-neighbors test-vector training-data)))