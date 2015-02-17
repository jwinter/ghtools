(ns ghtools.core-test
  (:require [clojure.test :refer :all]
            [ghtools.core :refer :all]))

(def test-url "https://api.github.com/repos/jwinter/test-repo/pulls")
(def sample-json (json/parse-string (:body (client/get test-url))))


(deftest testing-pulls
  (testing "pulls"
    (let [sample-pulls (pulls "jwinter" "test-repo")]
         (is (= 1 (count sample-pulls)))
         (is (= "https://api.github.com/repos/jwinter/test-repo/pulls/1"
                ((first sample-pulls) "url")))
         (is (= "jwinter/test-repo: <a href='https://api.github.com/repos/jwinter/test-repo/pulls/1'>More detail in the README</a><br />"
                (html-format-pulls sample-pulls)))
         (is true)
    )))
