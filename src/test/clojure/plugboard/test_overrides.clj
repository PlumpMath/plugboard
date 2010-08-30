(ns plugboard.test-overrides
  (:use
   clojure.test
   )
  (:require [plugboard.plugboard :as plugboard]
            [plugboard.configurations :as configs])
  )

(defn insert-path [state]
  [false (merge {plugboard/path "a/b/index.html"} state)]
  )

(defn resource-exists [state]
  (if (= "a/b/index.html" (get state plugboard/path))
    [true state]
    [false state]
  ))

(deftest path-insertion
  (is (= 404 (plugboard/get-status
              configs/default-decision-map
              {:request {:request-method :get}})))
  (is (= 200 (plugboard/get-status
              (configs/override-default-decision-map {:B3 insert-path :C7 resource-exists})
              {:request {:request-method :get}})))
    )



