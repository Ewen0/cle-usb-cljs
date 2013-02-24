(ns com.ewen.cle-usb-cljs.handlers
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [com.ewen.cle-usb-cljs.templates :refer [index-page]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:import [java.io.File]))

(defroutes app-routes
  (GET "/" [] (index-page (java.io.File. "resources/public/bootstrap.html")))
  (route/files "/" {:root "resources/public"})
  (route/files "/css" {:root "resources/public/css"})
  (route/not-found "Not Found"))

(def app
  (-> app-routes (handler/site)))

(defn -main [port]
  (run-jetty app {:port (Integer. port)}))