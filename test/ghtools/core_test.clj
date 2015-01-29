(ns ghtools.core-test
  (:require [clojure.test :refer :all]
            [ghtools.core :refer :all]))

(deftest testing-pulls
  (testing "pulls"
    (is (= ""
           (pulls "" 'b' 'c)
           ))
    (is true)
    ))
