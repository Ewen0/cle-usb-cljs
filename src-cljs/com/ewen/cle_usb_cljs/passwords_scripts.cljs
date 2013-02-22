(ns com.ewen.cle-usb-cljs.passwords-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [domina :refer [nodes single-node attr set-attr! remove-attr!]]
            [domina.css :refer [sel]]))

(def layout (:passwords layouts))

#_(defn bind-list-touch []
  (let [sections (sel ".section-body")]
    (F/mapE #(add-class! (single-node sections) "active") (F/extractEventE (single-node sections) "touchstart"))))









(def draggable-elts (.querySelectorAll layout "div[draggable=\"draggable\"]"))

(defn drag-E [elt]
  (let [moveEE 
        (F/mapE 
         (fn [td] 
           (.preventDefault td)
           (F/mapE 
            (fn [tm] 
              (.preventDefault tm)
              {:drag elt :left (.-pageX (.item (.-changedTouches tm) 0)) :top (.-pageY (.item (.-changedTouches tm) 0))})
            (F/calmE (F/extractEventE elt "touchmove") (F/constantB 5))))
         (F/extractEventE elt "touchstart"))
        dropEE
        (F/mapE (fn [tu] 
                  (.preventDefault tu)
                  (F/oneE {:drop elt :left (.-pageX tu) :top (.-pageY tu)}))
                (F/extractEventE elt "touchend"))]
    (F/switchE (F/mergeE moveEE dropEE))))

(defn drag-start-E [drag-events]
  (F/filterE (F/filterRepeatsE drag-events) (fn [p] (:drag p))))

(defn drag-stop-E [drag-events]
  (F/filterE drag-events (fn [p] (:drop p))))

(doseq [elt draggable-elts]
  (let [elt-pos (fn [touch-pos] 
                  (- touch-pos (/ (.-offsetHeight elt) 2)))]
    (F/mapE #(do (set-attr! elt "is-enabled-drag" "true")
                 (set-attr! (.querySelector layout "#pwd-trash") "enabled" "true")) 
            (-> elt (drag-E) (drag-start-E)))
    (F/mapE #(do (remove-attr! elt "is-enabled-drag")
                 (set-attr! (.querySelector layout "#pwd-trash") "enabled" "false")) 
            (-> elt (drag-E) (drag-stop-E)))
    (F/insertValueE (F/mapE (fn [p] (-> p (:top) (elt-pos))) (drag-E elt)) 
                    elt "style" "top")))












(defn get-navigation-events []  
  (scripts/get-events-with-value layout "#navigation-forward" 
    [:passwords :navigation-forward] "touchend"))

 








