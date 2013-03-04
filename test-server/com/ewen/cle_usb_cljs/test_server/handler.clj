(ns com.ewen.cle-usb-cljs.test-server.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.reload :refer [wrap-reload]]
            [com.ewen.cle-usb-cljs.test-server.templates :refer [index-page]]
            [ring.middleware.refresh :refer [wrap-refresh]])
  (:import [java.io.File]))


(defroutes app-routes
  (GET "/" [] (index-page (java.io.File. "resources/public/bootstrap.html")))
  (route/files "/" {:root "resources/public"})
  (route/files "/css" {:root "resources/public/css"})
  (route/not-found "Not Found"))

(def app
  (-> app-routes (wrap-refresh ["resources/public/js/cljs.js" "resources/public/passwords.html"]) (handler/site)))

#_(.. Thread (currentThread) (getContextClassLoader) (getResourceAsStream "public/index.html"))