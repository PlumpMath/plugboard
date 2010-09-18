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

(ns plugboard.webfunction.plugboards
  (:require [plugboard.webfunction.webfunction :as web]
            plugboard.webfunction.selectors
            [clojure.contrib.condition :as condition]
            [plugboard.core.plugboard :as plugboard]
            clojure.contrib.base64
            )
  )

(def ^{:private true} _wn)
(def
 ^{:doc "A vector of namespaces that contain web functions."}
 web-namespaces (var _wn))

(def ^{:private true} _newlocation)
(def
 ^{:doc "The location of the newly appended resource. Used for redirects."}
 new-location (var _newlocation))

(defn webfn-matches-path? [path webfn]
  (let [p (get (meta webfn) web/path)]
    (cond
     (fn? p) (not (nil? (p path)))
     (string? p) (= p path)
     :otherwise false))
  )

(defn get-matching-webfunctions-for-path [path web-namespaces]
  (mapcat
   (fn [web-ns]
     (filter #(webfn-matches-path? path %)
             (plugboard.webfunction.selectors/get-web-functions
              web-ns)))
   web-namespaces)
  )

(defn web-function-resources [namespaces]
  {
   :init (fn [state]
           (-> state
               (assoc web-namespaces namespaces)
               (assoc plugboard/path (get-in state [:request :route-params "*"]))
               ))
   :C7 ; Resource exists?
   (fn [state dlg] 
     [(not (empty? (get-matching-webfunctions-for-path (get state plugboard/path) (get state web-namespaces)))) state]
     )
   })

(defn set-header [state name value]
  (update-in state [:response :headers] (fn [old] (assoc old name value)))
  )

(defn welcome-page [path]
  {:D4 (fn [state dlg] (= \/ (last (get-in state [:request :uri]))))
   :D5 (fn [state dlg] [true
                    (let [location (str (get-in state [:request :uri]) path)]
                      (set-header state "Location" location))])})

;; Plug in an appender capable of appending a resource.  The appender must
;; append the resource and return the new location as a uri string (which may
;; depend itself on the client's preferred media type
(defn redirecting-appender [f]
  {
   :L13 (fn [state dlg]
          [true (assoc state new-location (f state))])
   :M13 (fn [state dlg] (contains? state new-location))
   ;; For this type of appender we want to do always redirect to new-location.
   :M14 (fn [state dlg] [true (set-header state "Location" (get state new-location))])
   }
  )

;; The auth string arrives as "Basic user:password" (where user:password is base64 encorded
(defn compare-secret [expected auth-string]
  (let [actual (second (re-seq #"[\w=]+" auth-string))]
    (= expected actual)
    ))

(defn basic-authentication [realm requires-auth-fn user password]
  {:B4
   (let [encoded (clojure.contrib.base64/encode-str (str user ":" password))]
     (fn [state dlg]
       (if (requires-auth-fn (get state :request))
         (let [res
               (and
                (contains? (get-in state [:request :headers]) "authorization")
                (compare-secret encoded (get-in state [:request :headers "authorization"])))]
           (if res true 
               [false (plugboard.webfunction.plugboards/set-header state "WWW-Authenticate" (format "Basic realm=\"%s\"" realm))])
           )
         true
         )))})
