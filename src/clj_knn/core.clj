(ns clj-knn.core
  (:gen-class))

(defn hamming-distance
  [vec1 vec2]
  (count (filter true? (map
                         (partial reduce not=)
                            (map vector (vals (sort vec1)) (vals (sort vec2)))))))

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