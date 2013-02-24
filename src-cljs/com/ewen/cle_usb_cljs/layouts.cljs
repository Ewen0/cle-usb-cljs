(ns com.ewen.cle-usb-cljs.layouts
  "Define the layouts of the pages of the GUI."
  (:require [enfocus.core :as ef]
            [flapjax.core :as F]
            [com.ewen.cle-usb-cljs.model :refer 
             [passwords get-sections]]
            [goog.dom :as dom])
  (:require-macros [enfocus.macros :as em]))






(comment "This is a bug fix for dom manipulation of <select> DOM nodes. 
This is not needed in more recent version of domina, but we don't
have control over the domina version since it is imported through enfocus.")
(set! domina/array-like? (fn array-like?
                           [obj]
                           (and obj ;; is not nil
                                (not (.-name obj)) ;; is not an element (i.e, <select>)
                                (.-length obj))))








;; ## Passwords page template

(em/defsnippet section-body :compiled "resources/public/passwords.html" [".section-body"]
  [] [])

(em/defsnippet section-body :compiled "resources/public/passwords.html" [".section-body"]
  [{pwd-label :title logo-path :logo}]
  ["p"] (em/prepend pwd-label)
  [".section-body-logo"] (em/set-attr "src" logo-path))

(em/defsnippet section :compiled "resources/public/passwords.html" [".section"]
  [section-name section-pwds]
  [".section-header > h2:first-of-type"] (em/content (name section-name))
  [".section"] (em/set-attr :id (name section-name))
  [".section-body"] (em/substitute (map section-body section-pwds))
  [".section-body"] (em/set-attr :section (name section-name)))

(em/defsnippet list-pwd :compiled "resources/public/passwords.html" ["#list-pwd"] [passwords]
  ["#list-pwd"] (em/content (map #(apply section %) passwords)))

(em/deftemplate template-passwords :compiled "resources/public/passwords.html" [passwords]
  ["#list-pwd"] (em/substitute (F/B->Node (F/liftB list-pwd passwords))))







;; ## Edit-passwords page template

(em/deftemplate template-edit-passwords :compiled "resources/public/edit-passwords.html" [])








;; ## New-password page template

(em/defsnippet section-opt :compiled "resources/public/new-password.html" ["#already-existing-sections option:first-of-type"] 
  [section-name]
  ["option"] (em/content (name section-name)))

(em/defsnippet list-sections-opt :compiled "resources/public/new-password.html" ["#already-existing-sections"] [passwords]
  ["#already-existing-sections"] (em/content (map section-opt (get-sections passwords))))

(em/deftemplate template-new-password :compiled "resources/public/new-password.html" [passwords]
  ["#already-existing-sections"] 
  (em/substitute (F/B->Node (F/liftB list-sections-opt passwords))))














(defn- tml-frag->node [frag]
  "Convert a template represented as a fragment into a DOM node.
We use `(second (.-childNodes frag))` because `(first (.-childNodes frag))` does not
contain anything interesting."
  (second (.-childNodes frag)))



(def layouts 
  "Layouts are created once during application bootstrapping and stored in this map.
We don't need to rebuild layouts after data update since data are manipulated as
\"time varying values\"" 
  {:passwords (tml-frag->node 
               (template-passwords 
                (F/extractValueB passwords))) 
   :edit-passwords (tml-frag->node
                    (template-edit-passwords))
   :new-password (tml-frag->node
                  (template-new-password
                   (F/extractValueB passwords)))})









