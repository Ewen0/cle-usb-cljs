(ns flapjax.core
  (:require [goog.dom :as dom]))

 

(declare receiverE sendEvent)

 

(def EventStream js/EventStream)

(def Behavior js/Behavior)

 

;;;;;;;;;;;;;;;;;;;;;;;;;;; UTILITY FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(def startsWith js/startsWith)

 

(def changes js/changes)

 

;;;;;;;;;;;;;;;;;;;;;;;;; EVENT STREAM FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(def oneE js/oneE)

(def zeroE js/zeroE)

(def mapE js/mapE)

(def mergeE js/mergeE)

 

(def switchE js/switchE)

 

(def filterE js/filterE)

 

(def constantE js/constantE)

 

(def collectE js/collectE)

 

(def notE js/notE)

 

(def filterRepeatsE js/filterRepeatsE)

(def ifE js/ifE)

 

(def receiverE js/receiverE)

 

(def sendEvent js/sendEvent)

 

(def snapshotE js/snapshotE)

 

(def onceE js/onceE)

 

(def skipFirstE js/skipFirstE)

 

(def delayE js/delayE)

 

(def blindE js/blindE)

 

(def calmE js/calmE)

 

(def timerE js/timerE)

(def extractEventE js/extractEventE)

(def clicksE js/clicksE)

 

;;;;;;;;;;;;;;;;;;;;;;;;;;; BEHAVIOR FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 

(def constantB js/constantB)

 

(def delayB js/delayB)

 

(def valueNow js/valueNow)

 

(def switchB js/switchB)

 

(def andB js/andB)

(def orB js/orB)

 

(def notB js/notB)

 

(def liftB js/liftB)

(def condB js/condB)

 

(def ifB js/ifB)

 

(def timerB js/timerB)

 

(def blindB js/blindB)

 

(def calmB js/calmB)





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