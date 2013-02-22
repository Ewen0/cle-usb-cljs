(ns flapjax.core
  (:require [goog.dom :as dom]))

 

(declare receiverE sendE)

 

(def EventStream js/EventStream)

(def Behavior js/Behavior)

 

;;;;;;;;;;;;;;;;;;;;;;;;;;; UTILITY FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(defn startsWith

[streamE v]

(.startsWith streamE v))

 

(defn changes

[sourceB]

(.changes sourceB))

 

;;;;;;;;;;;;;;;;;;;;;;;;; EVENT STREAM FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(def oneE js/oneE)

(def zeroE js/zeroE)

(def mapE js/mapE)

(def mergeE js/mergeE)

 

(defn switchE

[streamE]

(.switchE streamE))

 

(defn filterE

[streamE pred]

(.filterE streamE pred))

 

(defn constantE

[streamE v]

(.constantE streamE v))

 

(defn collectE

[streamE init f]

(.collectE streamE init f))

 

(defn notE

[streamE]

(.notE streamE))

 

(defn filterRepeatsE

[streamE]

(.filterRepeatsE streamE))

(def ifE js/ifE)

 

(def receiverE js/receiverE)

(def sendEvent js/sendEvent)

 

(defn sendE

[streamE v]

(.sendEvent streamE v))

 

(def snapshotE js/snapshotE)

 

(defn onceE

[streamE]

(.onceE streamE))

 

(defn skipFirstE

[streamE]

(.skipFirstE streamE))

 

(defn delayE

[streamE intervalB]

(.delayE streamE intervalB))

 

(defn blindE

[streamE intervalB]

(.blindE streamE intervalB))

 

(defn calmE

[streamE intervalB]

(.calmE streamE intervalB))

 

(def timerE js/timerE)

(def extractEventE js/extractEventE)

(def clicksE js/clicksE)

 

;;;;;;;;;;;;;;;;;;;;;;;;;;; BEHAVIOR FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(def constantB js/constantB)

 

(defn delayB

[sourceB intervalB]

(.delayB sourceB intervalB))

 

(defn valueNow

[sourceB]

(.valueNow sourceB))

 

(defn switchB

[sourceBB]

(.switchB sourceBB))

 

(def andB js/andB)

(def orB js/orB)

 

(defn notB

[valueB]

(.notB valueB))

 

(def liftB js/liftB)

(def condB js/condB)

 

(defn ifB

[predicateB consequentB alternativeB]

(.ifB predicateB consequentB alternativeB))

 

(def timerB js/timerB)

 

(defn blindB

[sourceB intervalB]

(.blindB sourceB intervalB))

 

(defn calmB

  [sourceB intervalB]

  (.calmB sourceB intervalB))





(def insertDomB js/insertDomB)
(def insertValueB js/insertValueB)

(def insertValueE js/insertValueE)

(def TEXT2 js/TEXT2)

(def DIV js/DIV)





(defn B->Node [nodeB]
  (let [rootNode (.cloneNode (valueNow (liftB #(.-firstChild %) nodeB)))
        childNodesNow (valueNow (liftB #(.-childNodes (.-firstChild %)) nodeB))
        updateFn (fn [param] 
                   (do (dom/removeChildren rootNode)
                       (dorun (map 
                               #(dom/appendChild rootNode (.cloneNode % true)) 
                               (valueNow (liftB #(.-childNodes (.-firstChild %)) param))))))]
    (dorun (map #(dom/appendChild rootNode (.cloneNode % true)) childNodesNow))
    (liftB updateFn nodeB)
    rootNode))

(def extractValueE js/extractValueE)

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
     (let [receiv (receiverE)] 
       (add-watch atom-in :get-behaviour 
                  (fn [k r o n] (sendEvent receiv n)))
       (startsWith receiv @atom-in)))))

(swap! (.-method-table extractValueB) 
       #(assoc % cljs.core/Atom 
               extractValueB-Atom))