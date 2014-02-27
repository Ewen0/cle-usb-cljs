(ns com.ewen.cle_usb_cljs
  (:require [cljs.core.async :as async]
            [domina.events :as events :refer [listen! unlisten! unlisten-by-key!]]
            [domina.css :refer [sel]]
            [domina :refer [single-node]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.style :as gstyle]
            [sablono.core :as html :refer-macros [html]]
            [clojure.string :refer [upper-case]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [ewen.async-plus.macros :as async+m]))


(enable-console-print!)


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
                      [:a {:href "#"
                           :on-click #(async/put!
                                      (async/muxch* (om/get-shared owner :menu-events))
                                      {:event-name :home})}
                       "Home"]
                      [:a {:href "#"
                           :on-click #(async/put!
                                       (async/muxch* (om/get-shared owner :menu-events))
                                       {:event-name :add-new-password})}
                       "Add new password"]]]]]))))

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


(defn enable-button? [passwords {:keys [pwd-label pwd-value]}]
  (let [passwords (map #(-> % :label str upper-case) passwords)]
    (if (or (empty? pwd-label)
            (empty? pwd-value)
            (some #(= % (-> pwd-label str upper-case))
                  passwords))
      false true)))


(defn handle-label-change [e owner passwords {:keys [pwd-label] :as state}]
  (om/set-state! owner :pwd-label (.. e -target -value))
  (if (enable-button? passwords (om/get-state owner))
    (om/set-state! owner :enable-button true)
    (om/set-state! owner :enable-button false)))

(defn handle-value-change [e owner passwords {:keys [pwd-value] :as state}]
  (om/set-state! owner :pwd-value (.. e -target -value))
  (if (enable-button? passwords (om/get-state owner))
    (om/set-state! owner :enable-button true)
    (om/set-state! owner :enable-button false)))

(defn assoc-if [pred map key val]
  (if pred (assoc map key val)
    map))


(defn new-password [password-vect owner]
  (reify
    om/IInitState
    (init-state [_]
      {:pwd-label ""
       :pwd-value ""
       :enable-button false})
    om/IRenderState
    (render-state [this state]
                  (html [:div
                         [:div#password-label-wrapper.section
                          [:div.section-header [:h2 "Password label"]]
                          [:input#password-label {:placeholder "Password label"
                                                  :type "text"
                                                  :value (:pwd-label state)
                                                  :onChange #(handle-label-change % owner @password-vect state)}]]
                         [:div#password-value-wrapper.section
                          [:div.section-header
                           [:h2 "Password value"]]
                          [:input#password-value {:placeholder "Password value"
                                                  :type "password"
                                                  :value (:pwd-value state)
                                                  :onChange #(handle-value-change % owner @password-vect state)}]]
                         [:div.action-buttons [:input#new-password-button
                                               (assoc-if (not (:enable-button state))
                                                         {:type "button"
                                                          :value "Validate"}
                                                         :disabled "disabled")]]
                         [:p#err-msg]]))))






(def app-state
  (atom [{:id 1 :label "Password1"}
         {:id 2 :label "Password2"}]))

(def menu-events (async/mult (async/chan)))
(def new-pwd-events (async/mult (async/chan)))




(defn home-view []
  (om/root header {}
           {:target (-> (sel "#header") single-node)
            :shared {:menu-events menu-events}})

  (om/root password-list app-state
           {:target (-> (sel "#app") single-node)}))

(defn new-password-view []
  (om/root header {}
           {:target (-> (sel "#header") single-node)
            :shared {:menu-events menu-events}})

  (om/root new-password
           app-state
           {:target (-> (sel "#app") single-node)
            :shared {:new-pwd-events menu-events}}))









(defmulti handle-menu-event #(:event-name %))

(defmethod handle-menu-event :home []
  (home-view))

(defmethod handle-menu-event :add-new-password []
  (new-password-view))








(async+m/go-loop [menu-ch menu-events]
                 (when-let [menu-event (async/<! menu-ch)]
                   (handle-menu-event menu-event)
                   (recur menu-ch)))

(home-view)

