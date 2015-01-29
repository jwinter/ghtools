(ns ghtools.core
  (:require [clojure.tools.namespace.repl :refer [refresh]]))

(defn fetch [url]
  "")

(defn pulls
  "fetch pulls for this repo"
  [api-url username repo]
  (fetch (str api-url username repo)))

