(ns com.ewen.cle-usb-cljs.controller
  "Contains the functionalities that perform side effect action such as 
updating data or modifying the dom to switch the screen."
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [enfocus.core :as ef]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [com.ewen.cle-usb-cljs.model :refer [passwords]]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.passwords-scripts :as pwd-scripts]
            [com.ewen.cle-usb-cljs.edit-passwords-scripts :as 
             edit-pwd-scripts]
            [com.ewen.cle-usb-cljs.new-password-scripts :as 
             new-pwd-scripts])
  (:require-macros [enfocus.macros :as em]))






 







(def change-layout-events 
  "A flapjax event stream that fire the events used to switch the screen." 
  nil)

(defn init-change-layout-events []
  "Init the value of the change-layout-events Var be merging flapjax event streams 
from the different screens."
  (set! change-layout-events  
        (F/mergeE 
         (pwd-scripts/get-navigation-events)
         (edit-pwd-scripts/get-navigation-events)
         (edit-pwd-scripts/get-new-password-events)
         (new-pwd-scripts/get-navigation-events)))
  (F/mapE #(apply change-layout %) change-layout-events))










(defn prepare-layout-header []
  (em/at js/document 
         ["head .page-only-css"] (em/content nil)))


(defn layout-body [new-layout-body]
  (em/at js/document
         ["section"] (em/substitute new-layout-body)))

(defn build-layout [new-layout]
  (prepare-layout-header)
  (layout-body new-layout))

(defmulti change-layout (fn [old-layout action] 
                          [(keyword old-layout) (keyword action)]))

(defmethod change-layout [:passwords :navigation-forward] [old-layout action]
  (build-layout (:edit-passwords layouts)))

(defmethod change-layout [:edit-passwords :navigation-backward] [old-layout action]
  (build-layout (:passwords layouts)))

(defmethod change-layout [:edit-passwords :new-password] [old-layout action]
  (build-layout (:new-password layouts)))

(defmethod change-layout [:new-password :navigation-backward] [old-layout action]
  (build-layout (:edit-passwords layouts)))










(add-load-event
 #(do (build-layout (:passwords layouts))
      (init-change-layout-events)))





