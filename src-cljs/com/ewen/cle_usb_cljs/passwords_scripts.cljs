(ns com.ewen.cle-usb-cljs.passwords-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.model :refer [passwords]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [domina :refer [nodes single-node attr set-attr! remove-attr! add-class! remove-class!]]
            [domina.css :refer [sel]]))

(def layout (:passwords layouts))

#_(defn bind-list-touch []
  (let [sections (sel ".section-body")]
    (F/mapE #(add-class! (single-node sections) "active") (F/extractEventE (single-node sections) "touchstart"))))











(defn drag-E [elt]
  (let [event-X-pos (fn [event] (.-pageX (.item (.-changedTouches event) 0)))
        event-Y-pos (fn [event] (.-pageY (.item (.-changedTouches event) 0)))
        moveEE 
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
                  (F/oneE {:drop elt :left (event-X-pos tu) :top (event-Y-pos tu)}))
                (F/extractEventE elt "touchend"))]
    (F/switchE (F/mergeE moveEE dropEE))))

(defn- pos-in-segment? [pos start-seg stop-seg]
  (and (> pos start-seg) (< pos stop-seg)))

(defn- pos-over-elt? [[left-off top-off] elt]
  (let [left-off-elt (.-offsetLeft elt)
        top-off-elt (.-offsetTop elt)
        over-elt-horiz? (pos-in-segment? 
                         left-off left-off-elt 
                         (+ left-off-elt (.-offsetWidth elt)))
        over-elt-vert? (pos-in-segment? 
                        top-off top-off-elt 
                        (+ top-off-elt (.-offsetHeight elt)))]
    (and over-elt-horiz? over-elt-vert?)))

(defn over-E [drag-elt drop-elt]
  (-> (drag-E drag-elt) (F/filterE (fn [p] (:drag p)))
      (F/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))

(defn drop-E [drag-elt drop-elt]
  (-> (drag-E drag-elt) (F/filterE (fn [p] (:drop p)))
      (F/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))

(defn drag-start-E [drag-events]
  (F/filterE (F/filterRepeatsE drag-events) (fn [p] (:drag p))))

(defn drag-stop-E [drag-events]
  (F/filterE drag-events (fn [p] (:drop p))))

(def remove-pwd-E (F/receiverE))

(F/liftB 
 (fn [] 
   (let [draggable-elts (.querySelectorAll layout "div.draggable")] 
     (doseq [elt draggable-elts]
       (let [elt-pos (fn [touch-pos] 
                       (- touch-pos (/ (.-offsetHeight elt) 2)))]
         (F/mapE #(do (add-class! elt "is-enabled-drag")
                      (add-class! (.querySelector layout "#pwd-trash") "enabled")) 
                 (-> elt (drag-E) (drag-start-E)))
         (comment "Be carefull of the order of functions call when applying side effects functions to same events")
         (F/mapE #(remove-class! (.querySelector layout "#pwd-trash") "over") (drag-E elt))
         (F/mapE #(add-class! (.querySelector layout "#pwd-trash") "over") 
                 (over-E elt (.querySelector layout "#pwd-trash")))
         (F/mapE #(when (js/confirm 
                         (str "Really delete password \"" 
                              (.-innerHTML (.querySelector elt "p")) 
                              "\"?")) 
                    (F/sendEvent remove-pwd-E 
                                 [(scripts/canonicalize (attr elt "section")) (.-innerHTML (.querySelector elt "p"))])) 
                 (drop-E elt (.querySelector layout "#pwd-trash")))
         (F/mapE #(do (remove-class! elt "is-enabled-drag")
                      (remove-class! (.querySelector layout "#pwd-trash") "enabled")) 
                 (-> elt (drag-E) (drag-stop-E)))
         (F/insertValueE (F/mapE (fn [p] (-> p (:top) (elt-pos))) (drag-E elt)) 
                         elt "style" "top")))))
 (F/extractValueB passwords))












(defn get-navigation-events []  
  (scripts/get-events-with-value layout "#navigation-forward" 
    [:passwords :navigation-forward] "touchend"))

 








