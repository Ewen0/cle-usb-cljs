(ns com.ewen.cle-usb-cljs.gen-css
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]
            [hiccup.def :refer [defelem]])
  (:import [javax.imageio ImageIO]
           [java.io File]))

(defn to-css-url
  "Wrap the provided string into a url(...) call. Usefull to build CSS properties.

Ex. `(to-css-url \"path\")`

-> \"url(path)\""
  [url]
  (if (nil? url) "none"
      (str "url(" url ")")))

(defn extract-css-rule 
  [key-val]
  (str (name (key key-val)) ":" (val key-val) ";"))

(defn css-rule 
"Generates a CSS rule as a string from an array containing a selector and a map of
CSS rules.

Ex : (css-rule [:#myId {:background-color \"black\"}])

-> \"#myId{background-color:black;}\" "
[rule]
  (let [sels (reverse (rest (reverse rule)))
        props (last rule)]
    (str (apply str (interpose " " (map name sels)))
         "{" (apply str (map extract-css-rule props)) "}")))

(defn css
"Generate a DOM element from a vector of CSS rules.

Ex : (.-outerHTML (css [:#myId {:background-image \"url(../img/image.jpeg)\"}][:.myClass {:position  \"relative\" :left \"50%\":margin-left \"-80px\"}]))

->`\"<style type=\"text/css\">#myId{background-image:url(../img/image.jpeg);}.myClass{left:50%;margin-left:-80px;position:relative;}</style>\"` "
  [& rules]
  (html [:style {:type "text/css"}
         (apply str (map css-rule rules))]))


(defn create-image [path]
  (let [img (ImageIO/read (File. (str "resources/public/css/" path)))]
    {:width (str (.getWidth img) "px") :height (str (.getHeight img) "px") :path path}))

(def css-images
  "Load the images used in this application as BufferedImages." 
  {:action-bar-bg (create-image "../img/action_bar.png")
   :logo-action-bar (create-image "../img/logo_action_bar.png")
   :action-bar-divider (create-image "../img/action_bar_divider.png")
   :action-bar-title (create-image "../img/action_bar_title.png")
   :navigation-forward (create-image "../img/1_navigation_forward.png")
   :navigation-back (create-image "../img/1_navigation_back.png")
   :section-divider (create-image "../img/section_divider.png")
   :section-body-divider (create-image "../img/section_body_divider.png")
   :spinner-bg (create-image "../img/spinner_bg.png")
   :input-text-bg (create-image "../img/input_text_bg.png")
   :input-text-active-bg (create-image "../img/input_text_active_bg.png")
   :button-bg (create-image "../img/button_bg.png")
   :button-disabled-bg (create-image "../img/button_disabled_bg.png")
   :button-pressed-bg (create-image "../img/button_pressed_bg.png")})

(def css-rules-common
  [["@font-face" {:font-family "roboto-black"
                  :src "url(\"/font/Roboto-Black.ttf\")"
                  }]
   ["@font-face" {:font-family "roboto-regular"
                  :src "url(\"/font/Roboto-Regular.ttf\")"
                  }]
   [:body {:-webkit-tap-highlight-color "rgba(255,255,255,0)"
           :font-family "roboto-regular"}]



   [:#action-bar {:background-image (to-css-url (:path (:action-bar-bg css-images)))
                  :background-size (str "100% " (:height (:action-bar-bg css-images)))
                  :background-repeat "no-repeat"
                  :width "100%"
                  :height (:height (:action-bar-bg css-images))}]
   [:#action-bar-title {:position "relative" :left "50%":margin-left "-80px"}]
   [:#action-bar-divider {:margin-left "2%"}]






   [:select 
    {:-webkit-appearance "none"
     :background "none"
     :border "none"
     :background-image (to-css-url (:path (:spinner-bg css-images)))
     :background-repeat "no-repeat"
     :background-position "bottom left"
     :width (:width (:spinner-bg css-images))
     :height "40px"
     :font-family "roboto-black"}]
   ["input[type=text], input[type=password]"
    {:-webkit-appearance "none"
     :background "none"
     :border "none"
     :background-image (to-css-url (:path (:input-text-bg css-images)))
     :background-repeat "no-repeat"
     :background-position "bottom left"
     :width (:width (:input-text-bg css-images))
     :height (:height (:input-text-bg css-images))
     :margin-top "5px"
     :font-family "roboto-black"}]
   ["input[type=text]:focus, input[type=password]:focus"
    {:-webkit-user-modify "read-write-plaintext-only"
     :-webkit-tap-highlight-color "rgba(0,0,0,0)"
     :background-image (to-css-url (:path (:input-text-active-bg css-images)))}]
   ["input[active=false], select[active=false]"
    {:display "none"}]
   ["input[type=button]"
    {:border "none"
     :background "none"
     :background-image (to-css-url (:path (:button-bg css-images)))
     :color "black"
     :width (:width (:button-bg css-images))
     :height "40px"}]
   ["div.action-buttons input[type=button]"
    {:margin-left "5%"}]
   ["input[type=button]:disabled"
    {:background-image (to-css-url (:path (:button-disabled-bg css-images)))
     :width (:width (:button-disabled-bg css-images))
     :color "rgba(0, 0, 0, 0.33)"}]
   ["input[type=button]:enabled:active"
    {:background-image (to-css-url (:path (:button-pressed-bg css-images)))
     :width (:width (:button-pressed-bg css-images))}]




   [:.list {:width "90%" :margin-left "5%"}]
   [:.navigation {:float "right"}]
   [:.section {:margin-left "auto" :margin-right "auto" :margin-top "30px" :width "90%"
               }]
   [:.section-header {:background-image (to-css-url (:path (:section-divider css-images)))
                      :background-position "bottom left"
                      :background-repeat "no-repeat"
                      :background-size (str "100% " (:height (:section-divider css-images)))
                      :width "100%"
                      :text-transform "uppercase"
                      :color "#999999"
                      :font-size "0.7em"
                      :font-family "roboto-black"}]
   [:.section-header :h2 {:margin-bottom "0px"}]
   [:.section-body {:background-image (to-css-url (:path (:section-body-divider css-images)))
                    :background-position "bottom left"
                    :background-color "#FFFFFF"
                    :background-repeat "no-repeat"
                    :background-size (str "100% " (:height (:section-body-divider css-images)))
                    :width "100%"
                    :text-transform "capitalize"
                    :padding-top "15px"
                    :padding-bottom "5px"}]
   [:.section-body 'p {:margin-top "0px"}]
   [:.section-body.active {:background-color "#33b5e5"}]
   [".section-body[draggable=\"draggable\"][is-enabled-drag=\"true\"]" {:position "absolute"}]



   [:#pwd-trash {:position "fixed"
                 :bottom "0"
                 :left "10%"
                 :width "80%" 
                 :background-color "#e6e6e6"}]
   ["#pwd-trash[enabled=\"true\"]" {:display "block"}]
   ["#pwd-trash[enabled=\"false\"]" {:display "none"}]
   [:#pwd-trash "#pwd-trash-logo" {:display "block" 
                                   :margin "auto"}]])

(def css-rules-passwords
  [])


#_(with-open [w (clojure.java.io/writer "resources/public/css/common.css")]
  (.write w (apply css css-rules-common)))

#_(with-open [w (clojure.java.io/writer "resources/public/css/passwords.css")]
  (.write w (apply css css-rules-passwords)))