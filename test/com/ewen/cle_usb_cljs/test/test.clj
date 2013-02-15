(ns com.ewen.cle-usb-cljs.test.test
  (:require [com.ewen.cle-usb-cljs.test.handler :refer [app]]
            [ring.util.serve :refer [serve-headless stop-server]]
            [cljs.repl.browser]))

(serve-headless app)

(stop-server)

;Starts the browser connected REPL
(cemerick.piggieback/cljs-repl
  :repl-env (doto (cljs.repl.browser/repl-env :port 9000)
              cljs.repl/-setup))