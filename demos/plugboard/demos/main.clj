;; Copyright 2010 Malcolm Sparks.
;;
;; This file is part of Plugboard.
;;
;; Plugboard is free software: you can redistribute it and/or modify it under the
;; terms of the GNU Affero General Public License as published by the Free
;; Software Foundation, either version 3 of the License, or (at your option) any
;; later version.
;;
;; Plugboard is distributed in the hope that it will be useful but WITHOUT ANY
;; WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
;; A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
;; details.
;;
;; Please see the LICENSE file for a copy of the GNU Affero General Public License.

(ns plugboard.demos.main
  (:use compojure.core)
  (:require
   plugboard.demos.menu
   plugboard.demos.hello-world.configuration
   plugboard.demos.forms.configuration
   plugboard.demos.status-views.configuration
   plugboard.demos.basic-auth.configuration
   [compojure.route :as route]
   ))

(defn create-handler [plugboard]
  (fn [req]
    (plugboard.webfunction.response/get-response req plugboard)
    ))

(defroutes main-routes
  (GET "/" [] (fn [req] (plugboard.demos.menu/render-page)))
  (GET "/hello-world/*" []
       (create-handler (plugboard.demos.hello-world.configuration/create-plugboard)))
  (ANY "/forms/*" []
       (create-handler (plugboard.demos.forms.configuration/create-plugboard)))
  (GET "/status-views/*" []
       (create-handler (plugboard.demos.status-views.configuration/create-plugboard)))
  (GET "/basic-auth/*" []
       (create-handler (plugboard.demos.basic-auth.configuration/create-plugboard)))
  (route/not-found "<h1>Page not found</h1>"))


