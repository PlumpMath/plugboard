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

(defproject plugboard "1.8.3"
  :description "A library to promote correct HTTP semantics for Clojure web applications built on Compojure"
  :url "http://github.com/malcolmsparks/plugboard"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :license {:name "AGPLv3"}

  :dev-dependencies [
                     [swank-clojure "1.2.1"]
                     [compojure "0.6.0"]
                     [ring/ring-core "0.3.5"]
                     [ring/ring-jetty-adapter "0.3.5"]
                     [clout "0.4.0"]
                     [clj-http "0.1.1"]
                     [autodoc "0.7.1" :exclusions [org.clojure/clojure org.clojure/clojure-contrib]]])
