(ns com.ewen.cle-usb-cljs.scripts
  (:require [com.ewen.cle-usb-cljs.utils :refer 
             [log add-load-event space-to-dash]]
            [clojure.string :refer [upper-case]]
            [flapjax.core :as F]))

(defn canonicalize [in]
  (-> in (str) (space-to-dash) (upper-case)))

(defn get-events-with-value 
  ([dom-sel value event]
     (get-events-with-value 
       (aget (dom/getElementsByTagNameAndClass "html") 0) 
       dom-sel value event))
  ([layout dom-sel value event]
     (let [dom-elt (.querySelector layout dom-sel)
           events (F/extractEventE dom-elt event)
           Bvalue (F/constantB value)]
       (F/snapshotE events Bvalue))))

