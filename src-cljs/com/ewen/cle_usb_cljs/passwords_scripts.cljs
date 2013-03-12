(ns com.ewen.cle-usb-cljs.passwords-scripts
  (:require [com.ewen.utils-cljs.utils :refer [log add-load-event]]
            [com.ewen.cle-usb-cljs.model :refer [passwords]]
            [com.ewen.cle-usb-cljs.scripts :as scripts]
            [com.ewen.cle-usb-cljs.layouts :refer [layouts]]
            [com.ewen.flapjax-cljs :as F-cljs]
            [domina :refer [nodes single-node attr set-attr! 
                            remove-attr! add-class! 
                            remove-class! has-class?]]
            [domina.css :refer [sel]]))

(def layout (:passwords layouts))




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













(defn drag-E [elt]
  (let [event-X-pos (fn [event] (.-pageX (.item (.-changedTouches event) 0)))
        event-Y-pos (fn [event] (.-pageY (.item (.-changedTouches event) 0)))
        dropEE
        (F-cljs/mapE (fn [tu] 
                       (.preventDefault tu)
                       (F-cljs/oneE {:drop elt :left (event-X-pos tu) :top (event-Y-pos tu)}))
                     (F-cljs/extractEventE elt "touchend"))
        false-E (-> dropEE (F-cljs/constantE false))
        moveEE 
        (F-cljs/mapE 
         (fn [ts] 
           (.preventDefault ts)
           (F-cljs/mergeE 
            (F-cljs/oneE {:handle elt :left (event-X-pos ts) :top (event-Y-pos ts)})
            (F-cljs/mapE
             (fn [tm] 
               (.preventDefault tm)
               {:drag elt :left (.-pageX (.item (.-changedTouches tm) 0)) :top (.-pageY (.item (.-changedTouches tm) 0))})
             (-> false-E (F-cljs/startsWith elt) (F-cljs/extractEventE "touchmove")))))
         (F-cljs/extractEventE elt "touchstart"))]
    (F-cljs/switchE (F-cljs/mergeE moveEE dropEE))))

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
  (-> (drag-E drag-elt) (F-cljs/filterE (fn [p] (:drag p)))
      (F-cljs/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))

(defn drop-E [drag-elt drop-elt]
  (-> (drag-E drag-elt) (F-cljs/filterE (fn [p] (:drop p)))
      (F-cljs/filterE (fn [p] (pos-over-elt? [(:left p) (:top p)] drop-elt)))))


(defn drag-start-E [drag-events]
  (let [handle-E (-> drag-events  
                     (F-cljs/filterE 
                      (fn [p] (:handle p))))
        long-press-E (F-cljs/mapE (fn [] (-> :handle-delayed (F-cljs/oneE) (F-cljs/delayE (F-cljs/constantB 1000)))) handle-E)
        handle-stop-E (F-cljs/filterE drag-events
                                      (fn [p] (and (:drop p) (-> (:drop p) 
                                                                 (has-class? "is-enabled-drag") 
                                                                 (not)))))
        handle-stop-E (F-cljs/mapE (fn [] (F-cljs/oneE :handle-stop)) handle-stop-E)]
    (F-cljs/filterE (F-cljs/switchE (F-cljs/mergeE long-press-E handle-stop-E)) (fn [e] (= :handle-delayed e)))))


(defn drag-stop-E [drag-events]
  (F-cljs/filterE drag-events (fn [p] (:drop p))))

(def remove-pwd-E (F-cljs/receiverE))

(F-cljs/liftB 
 (fn [] 
   (let [draggable-elts (.querySelectorAll layout "div.draggable")] 
     (doseq [elt draggable-elts]
       (let [elt-pos (fn [touch-pos] 
                       (- touch-pos (/ (.-offsetHeight elt) 2)))]
         (F-cljs/mapE #(do (add-class! elt "is-enabled-drag")
                           (add-class! (.querySelector layout "#pwd-trash") "enabled")) 
                      (-> elt (drag-E) (drag-start-E)))
         (comment "Be carefull of the order of functions call when applying side effects functions to same events")
         (F-cljs/mapE #(remove-class! (.querySelector layout "#pwd-trash") "over") (drag-E elt))
         (F-cljs/mapE #(add-class! (.querySelector layout "#pwd-trash") "over") 
                      (over-E elt (.querySelector layout "#pwd-trash")))
         (F-cljs/mapE #(when (js/confirm 
                              (str "Really delete password \"" 
                                   (.-innerHTML (.querySelector elt "p")) 
                                   "\"?")) 
                         (F-cljs/sendEvent remove-pwd-E 
                                           [(scripts/canonicalize (attr elt "section")) (.-innerHTML (.querySelector elt "p"))])) 
                      (drop-E elt (.querySelector layout "#pwd-trash")))
         (F-cljs/mapE #(do (remove-class! elt "is-enabled-drag")
                           (remove-class! (.querySelector layout "#pwd-trash") "enabled")) 
                      (-> elt (drag-E) (drag-stop-E)))
         (F-cljs/insertValueE (F-cljs/mapE (fn [p] (-> p (:top) (elt-pos))) (drag-E elt)) 
                              elt "style" "top")))))
 (F-cljs/extractValueB passwords))












(defn get-navigation-events []  
  (scripts/get-events-with-value layout "#navigation-forward" 
    [:passwords :navigation-forward] "touchend"))

 




