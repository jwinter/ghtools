(ns ghtools.http
  (:require [ghtools.core :refer :all]
            [ghtools.secrets :as secrets]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "hello"})

;; ghtools.http> (use 'ring.adapter.jetty)
;; nil
;; ghtools.http> (run-jetty ghtools.http/handler {:port 3002})


;; (defn hello-slack [msg]
;;   (post-to-slack secrets/channel-id msg))

