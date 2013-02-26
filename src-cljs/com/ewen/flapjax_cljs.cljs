(ns com.ewen.flapjax-cljs
  (:require [goog.dom :as dom]))



(defmulti B->Node (comp type js/valueNow))

(defmethod B->Node js/DocumentFragment [nodeB]
  (when-not (-> nodeB (js/valueNow) (.-firstChild)) 
    (throw "Parameter is empty"))
  (when (-> nodeB (js/valueNow) (.-childNodes) (.-length) (> 1)) 
    (throw "The provided parameter must have exactly 1 child node")) 
  (let [childs (fn [node] (-> node (.-firstChild) (.-childNodes)))
        rootNode (-> nodeB (js/valueNow) (.-firstChild) (.cloneNode true))
        updateFn (fn [param] 
                   (do (dom/removeChildren rootNode)
                       (dorun 
                        (map 
                         #(dom/appendChild rootNode (.cloneNode % true)) 
                         (childs param)))))]
    (js/liftB updateFn nodeB)
    rootNode))

(defmulti extractValueB type)

(defmethod extractValueB :default [in-obj]
  (js/extractValueB in-obj))

;; Implement `extractValueB` for clojurescript Atoms. We want the function to be memoized
;; so it seems we cannot implement it directly with `defmethod` (maybe it is possible?).
;; Instead we modify the `extractValueB` javascript object to add the function.

(def extractValueB-Atom
  "We want the function memoized so the beaviour returned is created only once. Otherwise, 
problems occur ..."
  (memoize 
   (fn [atom-in] 
     (let [receiv (js/receiverE)] 
       (add-watch atom-in :get-behaviour 
                  (fn [k r o n] (js/sendEvent receiv n)))
       (js/startsWith receiv @atom-in)))))

(swap! (.-method-table extractValueB) 
       #(assoc % cljs.core/Atom 
               extractValueB-Atom))