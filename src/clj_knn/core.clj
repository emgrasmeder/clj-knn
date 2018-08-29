(ns clj-knn.core
  (:require [clojure.set :as set])
  (:gen-class))

(defn get-key
  [prefix key]
  (if (nil? prefix)
    key
    (str prefix "." key)))

(defn flatten-map-kvs
  ([map] (flatten-map-kvs map nil))
  ([map prefix]
   (reduce
     (fn [memo [k v]]
       (if (map? v)
         (concat memo (flatten-map-kvs v (get-key prefix (name k))))
         (conj memo [(get-key prefix (name k)) v])))
     [] map)))

(defn flatten-map [map]
  "Using an algorithm for map-flattening that I found at
  https://gist.github.com/sudodoki/023d5f08c2f847b072b652687fdb27f2

  Clojure walk adds computation complexity that probably is unnecessary"
  (clojure.walk/keywordize-keys (into {} (flatten-map-kvs map))))

(defn hamming-distance
  [map1 map2]
  (let [all-keys (set/union (set (keys map1)) (set (keys map2)))
        is-diff? (map (fn [k] (= (get map1 k) (get map2 k))) all-keys)]
    (count (filter #(not (true? %)) is-diff?))))

(defn take-k-neighbors
  [k neighbors]
  (take k (sort-by :distance neighbors)))

(defn tag-with-distance
  [test-vector vector-from-training-data]
  (assoc vector-from-training-data :distance (hamming-distance test-vector vector-from-training-data)))

(defn rank-neighbors
  [test-vector training-data]
  (map (partial tag-with-distance test-vector) training-data))

(defn knn
  [test-vector training-data k]
  (take-k-neighbors k (rank-neighbors test-vector training-data)))