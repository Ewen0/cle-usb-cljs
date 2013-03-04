(ns com.ewen.cle-usb-cljs.scripts
  (:require [com.ewen.utils-cljs.utils :refer [log space-to-dash]]
            [clojure.string :refer [upper-case]]
            [com.ewen.flapjax-cljs :as F-cljs]))

(defn canonicalize [in]
  (-> in (str) (space-to-dash) (upper-case)))

(defn get-events-with-value 
  ([dom-sel value event]
     (get-events-with-value 
       (aget (dom/getElementsByTagNameAndClass "html") 0) 
       dom-sel value event))
  ([layout dom-sel value event]
     (let [dom-elt (.querySelector layout dom-sel)
           events (F-cljs/extractEventE dom-elt event)
           Bvalue (F-cljs/constantB value)]
       (F-cljs/snapshotE events Bvalue))))

