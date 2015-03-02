(ns ghtools.core-test
  (:require [clojure.test :refer :all]
            [ghtools.core :refer :all]
            [cheshire.core :as json]
            ))

(ns ghtools.core)
(def sample-json (json/parse-string (slurp "./test/fixtures/pulls.json")))
(def second-sample-json (json/parse-string (slurp "./test/fixtures/pulls2.json")))

; stubbing fetch-json so that we don't have hit the network
(defn- fetch-json [url]
  (if (re-find #"second" url)
    second-sample-json
    sample-json))

(ns ghtools.core-test)
(def test-url "https://api.github.com/repos/jwinter/test-repo/pulls")

(deftest testing-pulls
  (testing "pulls"
    (let [sample-pulls (pulls "jwinter" "test-repo")]
         (is (= 1 (count sample-pulls)))
         (is (= "https://api.github.com/repos/jwinter/test-repo/pulls/1"
                ((first sample-pulls) "url")))
         (is (= "jwinter/test-repo [jwinter]: <a href='https://github.com/jwinter/test-repo/pull/1'>More detail in the README</a><br />"
                (html-format-pulls sample-pulls)))
         (is (= "jwinter/test-repo [jwinter]: <https://github.com/jwinter/test-repo/pull/1|More detail in the README>\n"
                (slack-format-pulls sample-pulls)))))
  (testing "open-pulls-from-repos"
    (is (= (open-pulls-from-repos ["jwinter/test-repo" "jwinter/second-test-repo"])
           (str
            "*Open Pull Requests*\n"
            "jwinter/test-repo [jwinter]: <https://github.com/jwinter/test-repo/pull/1|More detail in the README>\n"
            "jwinter/second-test-repo [jwinter]: <https://github.com/jwinter/second-test-repo/pull/1|Add a README>\n" )))))


