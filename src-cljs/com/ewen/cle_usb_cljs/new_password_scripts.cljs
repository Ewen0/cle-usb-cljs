(ns com.ewen.cle-usb-cljs.new-password-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [com.ewen.cle-usb-cljs.model :refer [passwords get-pwd-labels]]
            [domina :refer [nodes single-node attr set-attr! remove-attr!]]
            [domina.css :refer [sel]])
  (:require-macros [enfocus.macros :as em]))

(def layout (:new-password layouts))








(def validate-button-elt (.querySelector 
                            layout 
                            "#new-password-button"))

(def switch-section-elt (.querySelector 
                            layout 
                            "#switch-section-selection"))

(def validate-button-E (js/extractValueE 
                        validate-button-elt))

(def switch-section-E (js/extractValueE 
                       switch-section-elt))






(def is-active-existing-section-B 
  (js/startsWith  
   (js/mapE (partial = "Choose existing section") 
           switch-section-E) true))

(def is-active-new-section-B 
  (js/startsWith  
   (js/mapE (partial = "Create a new section")
           switch-section-E) false))

(def new-section-elt (.querySelector layout "#new-section"))

(def existing-section-elt (.querySelector layout "#already-existing-sections"))

(js/liftB set-attr! new-section-elt "active" is-active-new-section-B)

(js/liftB #(.setAttribute existing-section-elt %1 %2) "active" is-active-existing-section-B)







(def section-name-B
  (js/liftB scripts/canonicalize 
           (js/ifB is-active-existing-section-B 
                  (js/extractValueB (.querySelector layout "#already-existing-sections"))
                  (js/extractValueB (.querySelector layout "#new-section")))))

(def pwd-label-B (js/extractValueB (.querySelector layout "#password-label")))
(def pwd-val-B (js/extractValueB (.querySelector layout "#password-value")))

(def enable-validate-button-B
  (js/liftB #(->> % (some empty?) (not)) (js/liftB vector section-name-B pwd-label-B pwd-val-B)))

(js/liftB #(if % 
            (remove-attr! validate-button-elt "disabled") 
            (set-attr! validate-button-elt "disabled" "disabled")) 
         enable-validate-button-B)











(defn- change-button-val []
  (cond 
   (= (attr switch-section-elt :value) 
      "Choose existing section")
   (set-attr! switch-section-elt :value "Create a new section")
   (= (attr switch-section-elt :value) 
      "Create a new section")
   (set-attr! switch-section-elt :value "Choose existing section")))

(js/mapE change-button-val (js/clicksE switch-section-elt))








(defn- validation [section pwd-label]
  (let [section (scripts/canonicalize section)
        pwd-label (scripts/canonicalize pwd-label)
        valid (every? (partial not= pwd-label) 
                      (->> (get-pwd-labels @passwords section) (map scripts/canonicalize)))]
    (if valid
      [true]
      [false "Password already exists !"])))

(defn validation-filter [section pwd-label pwd-value]
    (let [valid (validation section pwd-label)]
       (if (first valid)
         [section pwd-label pwd-value]
         valid)))




(def new-pwd-data-E 
  (js/snapshotE validate-button-E 
               (js/liftB vector section-name-B pwd-label-B pwd-val-B)))

(def validated-new-pwd-data-E 
  (js/mapE #(apply validation-filter %) new-pwd-data-E))

















(defn get-navigation-events []
  (scripts/get-events-with-value layout "#navigation-backward" 
    [:new-password :navigation-backward] "touchend"))

