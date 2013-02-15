(ns com.ewen.cle-usb-cljs.gen-templates
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]
            [hiccup.def :refer [defelem]]))

(defelem action-bar [& forms] [:div#action-bar forms])

(defn pwd-tml [] (html5 
                  [:body 
                   [:section
                    (action-bar        
                     [:img#logo-action-bar {:src "img/logo_action_bar.png"}]
                     [:img#action-bar-divider {:src "img/action_bar_divider.png"}]
                     [:img#action-bar-title {:src "img/action_bar_title.png"}]
                     [:img#navigation-forward.navigation {:src "img/1_navigation_forward.png"}])
                    [:div#list-pwd.list
                     [:div.section 
                      [:div.section-header [:h2]]
                      [:div.section-body [:p] [:img.section-body-logo]]]]]]))

(defn edit-pwd-tml [] (html5 [:body   
                              [:section                            
                               (action-bar        
                                [:img#logo-action-bar {:src "img/logo_action_bar.png"}]
                                [:img#action-bar-divider {:src "img/action_bar_divider.png"}]
                                [:img#action-bar-title {:src "img/action_bar_title.png"}]
                                [:img#navigation-backward.navigation {:src "img/1_navigation_back.png"}])
                               [:div#list-actions.list
                                [:div#edit-passwords.section
                                 [:div.section-header [:h2 "Edit passwords"]]
                                 [:div#new-password.section-body [:p "New password"] [:img.section-body-logo]]]]]]))

(defn new-pwd-tml [] (html5 [:body   
                             [:section                            
                              (action-bar        
                               [:img#logo-action-bar {:src "img/logo_action_bar.png"}]
                               [:img#action-bar-divider {:src "img/action_bar_divider.png"}]
                               [:img#action-bar-title {:src "img/action_bar_title.png"}]
                               [:img#navigation-backward.navigation {:src "img/1_navigation_back.png"}])
                              [:div#list-actions.list
                               [:div#section-wrapper.section
                                [:div.section-header [:h2 "Section"]]
                                [:select#already-existing-sections [:option "example-option"]]
                                [:input#new-section {:type "text" :placeholder "Section"}]
                                [:br]
                                [:input#switch-section-selection {:type "button"
                                                                  :value "Create a new section"}]]
                               [:div#password-label-wrapper.section
                                [:div.section-header [:h2 "Password label"]]
                                [:input#password-label {:type "text" :placeholder "Password label"}]]
                               [:div#password-value-wrapper.section
                                [:div.section-header [:h2 "Password"]]
                                [:input#password-value {:type "password" :placeholder "Password value"}]]
                               [:div.action-buttons
                                [:input#new-password-button {:type "button" 
                                                             :value "Validate" 
                                                             :disabled "disabled"}]]
                               [:p#err-msg]]]]))



#_(with-open [w (clojure.java.io/writer  "resources/public/passwords.html")]
    (.write w (pwd-tml)))

#_(with-open [w (clojure.java.io/writer  "resources/public/edit-passwords.html")]
    (.write w (edit-pwd-tml)))

#_(with-open [w (clojure.java.io/writer  "resources/public/new-password.html")]
    (.write w (new-pwd-tml)))