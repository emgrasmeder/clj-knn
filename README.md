# clj-knn

![image](https://user-images.githubusercontent.com/8107614/44235589-56593800-a1aa-11e8-8048-d5908c59a825.png)

Designed with making an API in mind, this simple library find the K Nearest Neighbors for associative arrays.
### What's it do?
Let's look at these passing tests! clj-knn takes an array of maps as training data and a test vector. It returns the `k` of the nearest training data records with appended distances.

```clojure
(import '[clj-knn.core :as clj-knn])

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

(deftest end-to-end-test
    (testing "should find the match with identical specs, id 1111"
      (let [test-vector {:id 1111 :feature1 "black" :feature2 "manual" :feature3 "earth" :feature4 "no"}]
        (is (= (assoc test-vector :distance 0) (first (clj-knn/knn test-vector training-data 1))))))
    (testing "should find the match with identical specs, id 1111"
      (let [test-vector {:id 1111 :feature1 "black" :feature2 "ONE DIFFERENCE" :feature3 "TWO DIFFERENCE" :feature4 "no"}]
        (is (= 2 (:distance (first (clj-knn/knn test-vector training-data 1))))))))
```



### Installation
1. `git clone https://github.com/emilyagras/clj-knn`
2. `cd clj-knn`
3. `lein test`
