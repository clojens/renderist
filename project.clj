;; Copyright 2012 Julien Eluard
;; Updated 2013 Rob Jentzema
;;
;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.
;;
(defproject renderist "0.0.1"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.memoize "0.5.6"]
                 [org.clojure/tools.logging "0.2.6"]
                 [ring/ring-core "1.1.1"]
                 [ring-server "0.2.8"]
                 [compojure "1.1.5"]
                 [ring-json-response "0.2.0"]
                 [net.sourceforge.plantuml/plantuml "7947"]
                 [markdown-clj "0.9.29"]
                 [clj-time "0.5.1"]
                 [tentacles "0.2.6"]
                 [enlive "1.1.1"]
                 [environ "0.4.0"]
                 [thneed "1.0.0"]
                 [ring-mock "0.1.5"]]
  :main renderist.web
  :license {:name "GPL - v 3.0"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"
            :distribution :repo}
  :profiles {
    :production {
      :mirrors {
        #"central|clojars" "http://s3pository.herokuapp.com/clojure"
      }
    }
  }
  :min-lein-version "2.0.0")
