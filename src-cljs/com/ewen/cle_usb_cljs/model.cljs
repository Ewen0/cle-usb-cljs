(ns com.ewen.cle-usb-cljs.model
  "Contains the data used by the GUI."
  (:require [com.ewen.cle-usb-cljs.utils :refer [log add-load-event]]
            [cljs.reader :refer [read-string]])
  (:require-macros [enfocus.macros :as em]))

(defrecord Passwords [])

(def passwords-str "{:SECTION1 [{:title \"password1\" :logo \"\"}] :SECTION2 [{:title \"password2\" :logo \"\"}{:title \"password3\" :logo \"\"}]}")

(def passwords 
  "The passwords displayed to the user. A password is a map that contains a `:title`, a `:section`
and a `:logo` entry"
   (atom (map->Passwords (read-string passwords-str))))

#_(add-watch passwords 
           :remove-empty-sections
           (fn [k r o n] 
             #_(let [filtered-pwds (->> n (filter (comp (comp not empty?) second))
                                      (into {}) (map->Passwords))]
               (log n) (log filtered-pwds)
               (when (not= n filtered-pwds) (reset! passwords filtered-pwds)))
(reset! passwords (map->Passwords {}))))




(defn get-sections [pwds]
"Returns a list of the existing section names.
Ex. (get-sections @passwords)

-> (\"SECTION1\" \"SECTION2\")"
  (map (comp name first) pwds))

(defn get-pwd-labels [pwds section]
  "Returns a list of the existing password labels for a given section
Ex. (get-pwd-labels @passwords \"SECTION2\")

-> (\"password2\" \"password3\")"
  (map :title ((keyword section) pwds)))

(defn add-password [section pwd-label]
  (swap! passwords #(update-in % [(keyword section)] conj {:title (str pwd-label), :logo ""})))

(defn rem-password [section pwd-label]
  (swap! passwords #(update-in % [(keyword section)] 
                               (fn [coll pred] (filterv pred coll)) 
                               (fn [map] (not= pwd-label (:title map)))))
  (swap! passwords #(->> % (filter (comp (comp not empty?) second))
                        (into {}) (map->Passwords))))







(comment "Test purpose only, to be removed")
#_(swap! passwords #(assoc % :SECTION1 [{:title "password2", :logo ""}]))



