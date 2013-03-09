(ns com.ewen.cle-usb-cljs.model
  "Contains the data used by the GUI."
  (:require [com.ewen.utils-cljs.utils :refer [log add-load-event]]
            [cljs.reader :refer [read-string]])
  (:require-macros [enfocus.macros :as em]))

(defrecord Passwords [])

(def passwords-str "{:SECTION1 {:password1 {:id \"1\" :logo \"\"}} :SECTION2 {:password2 {:id \"2\" :logo \"\"} :password3 {:id \"3\" :logo \"\"}}}")

(def passwords
  "The passwords displayed to the user. A password is a map that contains a `:id`
and a `:logo` entry"
   (atom (map->Passwords (read-string passwords-str))))





(defn get-sections [pwds]
"Returns a list of the existing section names.
Ex. (get-sections @passwords)

-> (\"SECTION1\" \"SECTION2\")"
  (map (comp name first) pwds))

(defn get-pwd-labels [pwds section]
  "Returns a list of the existing password labels for a given section
Ex. (get-pwd-labels @passwords \"SECTION2\")

-> (\"password2\" \"password3\")"
  (keys ((keyword section) pwds)))

(defn add-password [section pwd-label]
  (swap! passwords #(update-in % [(keyword section)] assoc (keyword pwd-label) {:id "1" :logo ""})))

(defn rem-password [section pwd-label]
    (swap! passwords #(update-in % [(keyword section)] 
                                 dissoc (keyword pwd-label)))
    (swap! passwords #(->> % (filter (comp (comp not empty?) second))
                           (into {}) (map->Passwords))))







(comment "Test purpose only, to be removed")
#_(swap! passwords #(assoc % :SECTION1 [{:title "password2", :logo ""}]))



