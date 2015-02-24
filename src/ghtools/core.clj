(ns ghtools.core
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as str]
            [ghtools.secrets :as secrets]
            ))

(def slack-api "https://slack.com/api/")

(defrecord Pull [repo-name url title])

(defn post-to-slack [channel-id message]
  (client/post (str slack-api "chat.postMessage")
               {:form-params
                {:channel channel-id
                 :token secrets/slack-token
                 :text message
                 :username "gitbot"
                 :icon_url "https://photos-3.dropbox.com/t/2/AADXShG2mVqbOgctemLe6chyUPEbnd6oMYRY20AjH74Mng/12/143180821/png/1024x768/3/1424750400/0/2/octocat.svg/CJWIo0QgASACIAMoASgC/7umcmmHRbGl6XN2sVWDIFYOnuE0isGmg8uzM5E-qN8Q"
                 }}))

(defn pull-init [pull-json-hash]
  (->Pull (get-in pull-json-hash ["head" "repo" "full_name"])
          (pull-json-hash "html_url")
          (pull-json-hash "title")))

(defn- fetch-json [url]
  (json/parse-string
   (:body (client/get url {:basic-auth [secrets/oauth-key "x-oauth-basic"] } ))))

(defn pulls
  "fetch pulls for this repo"
  [username repo]
  (fetch-json (str secrets/gh-api "/repos/" username "/" repo "/pulls")))

(defn html-string [pull]
  (str (.repo-name pull)  ": <a href='" (.url pull) "'>" (.title pull) "</a><br />"))

(defn markdown-string [pull]
  (str (.repo-name pull)  ": <" (.url pull) "|" (.title pull) ">\n"))

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
  (str "*Open Pull Requests*\n" (str/join (map markdown-format-pulls (map #(apply pulls %1) (split-names repos))))))
