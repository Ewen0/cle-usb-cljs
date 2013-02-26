(ns com.ewen.cle-usb-cljs.templates
  (:require [net.cgrand.enlive-html :as enlive]))

(defn index-page [in]
  (apply str (enlive/emit* 
              (enlive/at (enlive/html-resource in) []))))