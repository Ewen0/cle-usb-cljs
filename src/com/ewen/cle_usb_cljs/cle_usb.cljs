(ns com.ewen.cle_usb_cljs
  (:require [cljs.core.async :as async]
            [domina.events :as events :refer [listen! unlisten! unlisten-by-key!]]
            [domina.css :refer [sel]]
            [domina :refer [single-node]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.style :as gstyle]
            [sablono.core :as html :refer-macros [html]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))




(defn password [password-map owner]
  (reify
    om/IRender
    (render [this]
      (html [:div.password
             [:p (:label password-map)]]))))

(defn password-list [password-vect owner]
  (reify
    om/IRenderState
    (render-state [this state]
      (html [:div#list-pwd
             (om/build-all password password-vect)]))))

(defn header [app owner]
  (reify
    om/IRender
    (render [this]
      (html [:div#action-bar
                   [:img#logo-action-bar
                    {:src "img/logo_action_bar.png"}]
                   [:img#action-bar-divider
                    {:src "img/action_bar_divider.png"}]
                   [:img#action-bar-title
                    {:src "img/action_bar_title.png"}]
                   [:div.dropdown.menu
                    [:button.navbar-toggle
                     {:data-toggle "dropdown"
                      :type "button"
                      :href "#"}
                     [:span.icon-bar]
                     [:span.icon-bar]
                     [:span.icon-bar]]
                    [:ul.dropdown-menu
                     {:role "menu"
                      :aria-labelledby "dLabel"}
                     [:li
                      [:a {:href "#"} "Home"]
                      [:a {:href "#"} "Add new password"]]]]]))))



(om/root header {}
         {:target (-> (sel "#header")
                      single-node)})

(def app-state
  (atom [{:id 1 :label "Password1"}
         {:id 2 :label "Password2"}]))

(om/root password-list app-state
         {:target (-> (sel "#app")
                      single-node)})

