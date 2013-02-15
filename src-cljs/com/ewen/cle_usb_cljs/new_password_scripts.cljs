(ns com.ewen.cle-usb-cljs.new-password-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [domina :refer [nodes single-node attr set-attr!]]
            [domina.css :refer [sel]])
  (:require-macros [enfocus.macros :as em]))

(def layout (:new-password layouts))

(def switch-section-elt (.querySelector 
                            layout 
                            "#switch-section-selection"))

(def switch-section-E (F/extractValueE 
                       switch-section-elt))






(def is-active-existing-section-B 
  (F/startsWith  
   (F/mapE (partial = "Choose existing section") 
           switch-section-E) true))

(def is-active-new-section-B 
  (F/startsWith  
   (F/mapE (partial = "Create a new section")
           switch-section-E) false))

(def new-section-elt (.querySelector layout "#new-section"))

(def existing-section-elt (.querySelector layout "#already-existing-sections"))

(F/liftB set-attr! new-section-elt "active" is-active-new-section-B)

(F/liftB #(.setAttribute existing-section-elt %1 %2) "active" is-active-existing-section-B)








(defn- change-button-val []
  (cond 
   (= (attr switch-section-elt :value) 
      "Choose existing section")
   (set-attr! switch-section-elt :value "Create a new section")
   (= (attr switch-section-elt :value) 
      "Create a new section")
   (set-attr! switch-section-elt :value "Choose existing section")))

(F/mapE change-button-val (F/clicksE switch-section-elt))











(defn get-navigation-events []
  (scripts/get-events-with-value layout "#navigation-backward" 
    [:new-password :navigation-backward] "click"))

