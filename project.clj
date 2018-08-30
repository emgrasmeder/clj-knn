(defproject clj-knn "0.1.0-SNAPSHOT"
  :description "K-Nearest Neighbors in Clojure for json/edn -like data"
  :url "http://github.com/emilyagras/clj-knn"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [circleci/bond "0.3.1"]]
  :main ^:skip-aot clj-knn.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
