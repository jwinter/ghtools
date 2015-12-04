(ns ghtools.http-test
  (:require [clojure.test :refer :all]
            [ghtools.http :refer :all]
            [cheshire.core :as json]
            [schema.test :as st]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            ))
(use-fixtures :once st/validate-schemas)

(deftest testing-http-handler
  (testing "handler"
    (is (= 1 2))
    (is (= 1 1))
    ))
