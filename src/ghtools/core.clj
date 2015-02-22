(ns ghtools.core
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as str]
            ))

(defrecord Pull [repo-name url title])

(defn pull-init [pull-json-hash]
  (->Pull (get-in pull-json-hash ["head" "repo" "full_name"])
          (pull-json-hash "html_url")
          (pull-json-hash "title")))

(defn- fetch-json [url]
  (json/parse-string
   (:body (client/get url))))

(defn pulls
  "fetch pulls for this repo"
  [username repo]
  (fetch-json (str "https://api.github.com/repos/" username "/" repo "/pulls")))

(defn html-string [pull]
  (str (.repo-name pull)  ": <a href='" (.url pull) "'>" (.title pull) "</a><br />"))

(defn markdown-string [pull]
  (str (.repo-name pull)  ": *" (.title pull) "* - " (.url pull) "\n"))

(defn html-format-pulls [pulls]
  (str/join
   (map (fn [pull]
          (html-string (pull-init pull)))
        pulls)))

(defn markdown-format-pulls [pulls]
  (str/join
   (map (fn [pull]
          (markdown-string (pull-init pull)))
        pulls)))

(defn split-names [repos]
  "Takes a list of repos and returns all the open pulls in markdown-format"
  (map #(str/split %1 #"\/") repos))

(defn open-pulls-from-repos [repos]
  (str/join (map markdown-format-pulls (map #(apply pulls %1) (split-names repos)))))
