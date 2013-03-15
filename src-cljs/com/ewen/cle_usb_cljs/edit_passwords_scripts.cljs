(ns com.ewen.cle-usb-cljs.edit-passwords-scripts
  (:require [com.ewen.utils-cljs.utils :refer [log]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [com.ewen.cle-usb-cljs.model :refer [passwords]]
            [com.ewen.flapjax-cljs :as F-cljs]
            [domina :refer [nodes single-node attr remove-class! add-class!]]
            [domina.css :refer [sel]]))

(def layout (:edit-passwords layouts))

(defn get-navigation-events []
  (scripts/get-events-with-value layout "#navigation-backward" 
    [:edit-passwords :navigation-backward] "touchend"))

(defn get-new-password-events []
  (scripts/get-events-with-value layout "#new-password" 
    [:edit-passwords :new-password] "click"))





(defn on-event-map-fn [fn event seq-dom]
  (doseq [elt seq-dom]
    (-> elt
        (F-cljs/extractEventE event)
        (F-cljs/snapshotE (F-cljs/constantB elt))
        (#(F-cljs/mapE fn %)))))


(defn make-section-active [section]
  (add-class! section "active"))

(defn make-section-inactive [section]
  (remove-class! section "active"))

(F-cljs/liftB
 #(on-event-map-fn make-section-active "touchstart" (.querySelectorAll layout ".section-body"))
 (F-cljs/extractValueB passwords))

(F-cljs/liftB
 #(on-event-map-fn make-section-inactive "touchend" (.querySelectorAll layout ".section-body"))
 (F-cljs/extractValueB passwords))
