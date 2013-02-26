(ns com.ewen.cle-usb-cljs.passwords-scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [com.ewen.cle-usb-cljs.model :refer [passwords]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [com.ewen.flapjax-cljs :as F-cljs]
            [domina :refer [nodes single-node attr set-attr! remove-attr! add-class! remove-class!]]
            [domina.css :refer [sel]]))

(def layout (:passwords layouts))

#_(defn bind-list-touch []
  (let [sections (sel ".section-body")]
    (js/mapE #(add-class! (single-node sections) "active") (js/extractEventE (single-node sections) "touchstart"))))











(defn drag-E [elt]
  (let [event-X-pos (fn [event] (.-pageX (.item (.-changedTouches event) 0)))
        event-Y-pos (fn [event] (.-pageY (.item (.-changedTouches event) 0)))
        moveEE 
        (js/mapE 
         (fn [td] 
           (.preventDefault td)
           (js/mapE 
            (fn [tm] 
              (.preventDefault tm)
              {:drag elt :left (.-pageX (.item (.-changedTouches tm) 0)) :top (.-pageY (.item (.-changedTouches tm) 0))})
            (js/calmE (js/extractEventE elt "touchmove") (js/constantB 5))))
         (js/extractEventE elt "touchstart"))
        dropEE
        (js/mapE (fn [tu] 
                  (.preventDefault tu)
                  (js/oneE {:drop elt :left (event-X-pos tu) :top (event-Y-pos tu)}))
                (js/extractEventE elt "touchend"))]
    (js/switchE (js/mergeE moveEE dropEE))))

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
  (-> (drag-E drag-elt) (js/filterE (fn [p] (:drag p)))
      (js/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))

(defn drop-E [drag-elt drop-elt]
  (-> (drag-E drag-elt) (js/filterE (fn [p] (:drop p)))
      (js/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))

(defn drag-start-E [drag-events]
  (js/filterE (js/filterRepeatsE drag-events) (fn [p] (:drag p))))

(defn drag-stop-E [drag-events]
  (js/filterE drag-events (fn [p] (:drop p))))

(def remove-pwd-E (js/receiverE))

(js/liftB 
 (fn [] 
   (let [draggable-elts (.querySelectorAll layout "div.draggable")] 
     (doseq [elt draggable-elts]
       (let [elt-pos (fn [touch-pos] 
                       (- touch-pos (/ (.-offsetHeight elt) 2)))]
         (js/mapE #(do (add-class! elt "is-enabled-drag")
                      (add-class! (.querySelector layout "#pwd-trash") "enabled")) 
                 (-> elt (drag-E) (drag-start-E)))
         (comment "Be carefull of the order of functions call when applying side effects functions to same events")
         (js/mapE #(remove-class! (.querySelector layout "#pwd-trash") "over") (drag-E elt))
         (js/mapE #(add-class! (.querySelector layout "#pwd-trash") "over") 
                 (over-E elt (.querySelector layout "#pwd-trash")))
         (js/mapE #(when (js/confirm 
                         (str "Really delete password \"" 
                              (.-innerHTML (.querySelector elt "p")) 
                              "\"?")) 
                    (js/sendEvent remove-pwd-E 
                                 [(scripts/canonicalize (attr elt "section")) (.-innerHTML (.querySelector elt "p"))])) 
                 (drop-E elt (.querySelector layout "#pwd-trash")))
         (js/mapE #(do (remove-class! elt "is-enabled-drag")
                      (remove-class! (.querySelector layout "#pwd-trash") "enabled")) 
                 (-> elt (drag-E) (drag-stop-E)))
         (js/insertValueE (js/mapE (fn [p] (-> p (:top) (elt-pos))) (drag-E elt)) 
                         elt "style" "top")))))
 (F-cljs/extractValueB passwords))












(defn get-navigation-events []  
  (scripts/get-events-with-value layout "#navigation-forward" 
    [:passwords :navigation-forward] "touchend"))

 








