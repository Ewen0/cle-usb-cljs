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

(om/root app-state
         (fn [app owner]
           (dom/section nil
            (dom/div #js {:id "action-bar"}
                    (dom/img #js {:id "logo-action-bar"
                             :src "img/logo_action_bar.png"})
                    (dom/img #js {:id "action-bar-divider"
                             :src "img/action_bar_divider.png"})
                    (dom/img #js {:id "action-bar-title"
                             :src "img/action_bar_title.png"})
                    (dom/img #js {:id "navigation-forward"
                             :className "navigation"
                             :src "img/1_navigation_forward.png"} nil))))
         (-> (sel "#app") single-node))

