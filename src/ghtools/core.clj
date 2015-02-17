(ns ghtools.core
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as str]
            ))

(defn html-format-pulls [pulls]
  (str/join (map (fn [pull]
         (let [repo-name (get-in pull ["head" "repo" "full_name"])
               pull-url (pull "url")
               pull-title (pull "title")
               ]
           (str repo-name ": <a href='" pull-url "'>" pull-title "</a><br />")))
   pulls)))
  

(defn fetch [url]
  (json/parse-string
   (:body (client/get url))))

(defn pulls
  "fetch pulls for this repo"
  [username repo]
  (fetch (str "https://api.github.com/repos/" username "/" repo "/pulls")))


(def test-url "https://api.github.com/repos/jwinter/test-repo/pulls")
(def sample-json (json/parse-string (:body (client/get test-url))))
