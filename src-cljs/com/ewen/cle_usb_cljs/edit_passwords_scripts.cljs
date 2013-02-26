(ns com.ewen.cle-usb-cljs.edit-passwords-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer 
             [log add-load-event]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [domina :refer [nodes single-node attr]]
            [domina.css :refer [sel]]))

(def layout (:edit-passwords layouts))

(defn get-navigation-events []
  (scripts/get-events-with-value layout "#navigation-backward" 
    [:edit-passwords :navigation-backward] "touchend"))

(defn get-new-password-events []
  (scripts/get-events-with-value layout "#new-password" 
    [:edit-passwords :new-password] "click"))

