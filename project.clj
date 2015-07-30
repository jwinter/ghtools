(defproject ghtools "0.1.0-SNAPSHOT"
  :description "Github tools"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [cheshire "5.4.0"]
                 [clj-http "1.0.1"]
                 [prismatic/schema "0.4.0"]]
  :main ghtools.runner
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.7"]
                                  [org.clojure/test.check "0.7.0"]]
                   :source-paths ["dev"]}})
