(ns com.ewen.cle-usb-cljs.passwords-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [domina :refer [nodes single-node attr]]
            [domina.css :refer [sel]]))

(def layout (:passwords layouts))

#_(defn bind-list-touch []
  (let [sections (sel ".section-body")]
    (F/mapE #(add-class! (single-node sections) "active") (F/extractEventE (single-node sections) "touchstart"))))


(defn get-navigation-events []
  (scripts/get-events-with-value layout "#navigation-forward" 
    [:passwords :navigation-forward] "click"))

 








