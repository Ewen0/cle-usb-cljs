(ns com.ewen.cle-usb-cljs.model
  "Contains the data used by the GUI."
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [cljs.reader :refer [read-string]])
  (:require-macros [enfocus.macros :as em]))

(defrecord Passwords [])

(def passwords-str "{:section1 [{:title \"password1\" :logo \"\"}] :section2 [{:title \"password2\" :logo \"\"}{:title \"password3\" :logo \"\"}]}")

(def passwords 
  "The passwords displayed to the user. A password is a map that contains a `:title`, a `:section`
and a `:logo` entry"
  (atom (map->Passwords (read-string passwords-str))))


(defn get-sections [pwds]
"Returns a list of the existing section names.
Ex. (get-sections @passwords)

-> (\"section1\" \"section2\")"
  (map (comp name first) pwds))







(comment "Test purpose only, to be removed")
#_(swap! passwords #(assoc % :section1 [{:title "password2", :logo ""}]))



