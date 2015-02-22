(ns ghtools.core-test
  (:require [clojure.test :refer :all]
            [ghtools.core :refer :all]
            [cheshire.core :as json]
            ))

(def test-url "https://api.github.com/repos/jwinter/test-repo/pulls")
(def sample-json (json/parse-string (slurp "./test/fixtures/pulls.json")))

(deftest testing-pulls
  (testing "pulls"
    (let [sample-pulls (pulls "jwinter" "test-repo")]
         (is (= 1 (count sample-pulls)))
         (is (= "https://api.github.com/repos/jwinter/test-repo/pulls/1"
                ((first sample-pulls) "url")))
         (is (= "jwinter/test-repo: <a href='https://github.com/jwinter/test-repo/pull/1'>More detail in the README</a><br />"
                (html-format-pulls sample-pulls)))
         (is (= "jwinter/test-repo: *More detail in the README* - https://github.com/jwinter/test-repo/pull/1\n"
                (markdown-format-pulls sample-pulls)))))
  (testing "open-pulls-from-repos"
    (is (= (open-pulls-from-repos ["jwinter/test-repo" "jwinter/second-test-repo"])
           (str
            "jwinter/test-repo: *More detail in the README* - "
            "https://github.com/jwinter/test-repo/pull/1\n"
            "jwinter/second-test-repo: *Add a README* - "
            "https://github.com/jwinter/second-test-repo/pull/1\n"))))
  )
