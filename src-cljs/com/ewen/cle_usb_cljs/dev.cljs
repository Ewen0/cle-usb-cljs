(ns com.ewen.cle-usb-cljs.dev
  (:require [clojure.browser.repl] 
            [com.ewen.cle-usb-cljs.utils :refer [add-load-event]]))

(def connect-repl
  "Connects to the browser connected REPL"
  #(clojure.browser.repl/connect "http://localhost:9000/repl"))


