(ns ghtools.runner
  (:require [ghtools.core :refer :all]
            [ghtools.secrets :as secrets]))

(defn -main []
  (let [msg (open-pulls-from-repos secrets/repos)]
    (post-to-slack secrets/channel-id msg)))
