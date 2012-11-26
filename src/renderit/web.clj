(ns renderit.web
    (:use [ring.server.standalone])
    (:require [clj-time.format :as f]
              [clojure.java.io :as io]
              [compojure.core :as c]
              [compojure.route :as r]
              [markdown :as md]
              [net.cgrand.enlive-html :as h]
              [renderit.api :as a]
              [renderit.gist :as g]
              [renderit.plantuml :as p]
              [ring.util.codec :as codec])
    (:import java.util.Locale))

(def formatter (f/with-locale (f/formatter "MMM dd, yyyy") Locale/US)) ;default to UTC time zone

(h/deftemplate blank "public/blank.html" [string]
  [:div#content] (h/content string))

(h/defsnippet gist-snippet "public/templates/diagram.html" [:body :> h/any-node] [{:keys [id description] date :created_at {author :login} :user} readme files]
  [:span (h/nth-child 1)] (h/do->
                            (h/set-attr :href (str "/" author))
                            (h/content author))
  [:span (h/nth-child 2)] (h/do->
                            (h/set-attr :href (str "https://gist.github.com/" id))
                            (h/content (str "#" id)))
  [:h1] (h/content description)
  [:aside#date] (h/content (f/unparse formatter (f/parse date)))
  [:pre#readme] (h/html-content (md/md-to-html-string readme))
  [:section] (h/clone-for [file files]
               [:section#id] (h/set-attr :id (:filename (val file)))
               [:h2 :a] (h/content (:filename (val file)))
               [:h2 :a] (h/set-attr :href (str "https://gist.github.com/" id "#file_" (g/file-name-to-file-id (:filename (val file)))))
               [:div.diagram :img] (h/set-attr :src (str "data:image/png;base64," (codec/base64-encode (p/render (:content (val file)) "png"))))
               [:pre.source] (h/content (:content (val file)))
               [:code] (h/content (str "<img alt=\"" (:filename (val file)) "\" src=\"http://renderit.herokuapp.com/api/" id "/" (:filename (val file)) ".png\" />"))))

(defn load-page [page]
  (slurp (io/resource (str "public/" page)) :encoding "UTF-8"))

(def page-index (load-page "index.html"))
(def page-author (load-page "author.html"))
(def page-404 (load-page "404.html"))

(defn page-gist [id]
  (if-let [gist (g/get-gist id)]
    (apply str (blank (gist-snippet gist (g/extract-file gist "Readme.md") (:files gist))))
    page-404))

(c/defroutes all-routes
  (c/context "/api" [] a/routes)
  (c/GET "/" [] page-index)
  (c/GET "/:id" [id] (page-gist id))
  (c/GET "/author/:id" [id] page-author)
  (r/resources "/")
  (r/not-found page-404))

(defn -main []
  (System/setProperty "java.awt.headless" "true")
  (serve all-routes {:open-browser? false}))
