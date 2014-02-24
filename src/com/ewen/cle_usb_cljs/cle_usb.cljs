(ns com.ewen.cle_usb_cljs
  (:require [cljs.core.async :as async]
            [domina.events :as events :refer [listen! unlisten! unlisten-by-key!]]
            [domina.css :refer [sel]]
            [domina :refer [single-node]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.style :as gstyle])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))


(def app-state
  (atom {}))

(om/root (fn [app owner]
           (dom/section nil
                        (dom/div #js {:id "action-bar"}
                                 (dom/img #js {:id "logo-action-bar"
                                               :src "img/logo_action_bar.png"})
                                 (dom/img #js {:id "action-bar-divider"
                                               :src "img/action_bar_divider.png"})
                                 (dom/img #js {:id "action-bar-title"
                                               :src "img/action_bar_title.png"})
                                 (dom/div #js {:className "dropdown menu"}
                                          (dom/button #js {:className "navbar-toggle"
                                                           :data-toggle "dropdown"
                                                           :type "button"
                                                           :href "#"}
                                                      (dom/span #js {:className "icon-bar"})
                                                      (dom/span #js {:className "icon-bar"})
                                                      (dom/span #js {:className "icon-bar"}))
                                          (dom/ul #js {:className "dropdown-menu"
                                                       :role "menu"
                                                       :aria-labelledby "dLabel"}
                                                  (dom/li #js {} (dom/a #js {:href "#"} "e"))))
                                 #_(dom/img #js {:id "navigation-forward"
                                                 :className "menu"
                                                 :src "img/1_navigation_forward.png"} nil))))
         app-state
         {:target (-> (sel "#app") single-node)})

