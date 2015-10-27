(ns ghtools.core
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as str]
            [schema.core :as s]
            [ghtools.secrets :as secrets]
            ))

(def slack-api "https://slack.com/api/")

(s/defrecord Pull
  [repo-name :- s/Str
   url  :- s/Str
   title :- s/Str
   user  :- s/Str
   created_at :- s/Str])

(s/defn pull-init :- Pull
  [pull-json-hash]
  (Pull. (get-in pull-json-hash ["head" "repo" "full_name"])
         (pull-json-hash "html_url")
         (pull-json-hash "title")
         (get-in pull-json-hash ["user" "login"])
         (pull-json-hash "created_at")
         ))

(defn post-to-slack [channel-id message]
  (client/post (str slack-api "chat.postMessage")
               {:form-params
                {:channel channel-id
                 :token secrets/slack-token
                 :text message
                 :username "gitbot"
                 :icon_url "https://photos-3.dropbox.com/t/2/AADXShG2mVqbOgctemLe6chyUPEbnd6oMYRY20AjH74Mng/12/143180821/png/1024x768/3/1424750400/0/2/octocat.svg/CJWIo0QgASACIAMoASgC/7umcmmHRbGl6XN2sVWDIFYOnuE0isGmg8uzM5E-qN8Q"
                 }}))

(defn- fetch-json [url]
  (json/parse-string
   (:body (client/get url {:basic-auth [secrets/oauth-key "x-oauth-basic"] } ))))

(defn pulls
  "fetch pulls for this repo"
  [username repo]
  (fetch-json (str secrets/gh-api "/repos/" username "/" repo "/pulls")))

(defn html-string [pull]
  (str (.repo-name pull)
       " [" (.user pull) "]: <a href='" (.url pull) "'>" (.title pull) "</a><br />"))

(defn slack-string [pull]
  (str (.repo-name pull)
       " [" (.user pull) "]: <" (.url pull) "|" (.title pull) ">\n"))

(defn html-format-pulls [pulls]
  (str/join
   (map (fn [pull]
          (html-string (pull-init pull)))
        pulls)))

(defn slack-format-pulls [pulls]
  (str/join
   (map slack-string pulls)))

(defn split-names [repos]
  (map #(str/split %1 #"\/") repos))

(defn all-pulls [repos]
  (->>
   (split-names repos) ; returns pairs of repo names
   (map #(apply pulls %1)) ; fetches JSON hash for each repo
   flatten ; flatten the array of repos of arrays of PRs
   (map pull-init)
   (sort-by #(.created_at %1))))

(defn open-pulls-from-repos
  "Takes a list of repos and returns all the open pulls in slack-format"
  [repos]
  (str "*Open Pull Requests*\n"
       (str/join
        (slack-format-pulls (all-pulls repos)))))
