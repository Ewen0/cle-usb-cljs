(defproject com.ewen.cle-usb-cljs "0.1.0-SNAPSHOT"
  :description "cle-usb client interface"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.cemerick/piggieback "0.0.2"]
                 [compojure "1.1.3"]
                 [ring-serve "0.1.2"]
                 [ring/ring-devel "1.1.6"]
                 [ring/ring-core "1.1.6"]
                 [hiccup "1.0.2"]
                 [enlive "1.0.1"]
                 [ring-refresh "0.1.1"]
                 [enfocus "1.0.0-SNAPSHOT"]
                 [domina "1.0.0"]]
  :dev-dependencies [[lein-cljsbuild "0.2.9"]
                     [lein-marginalia "0.7.1"]]
  :plugins [[lein-cljsbuild "0.2.9"]
            [lein-marginalia "0.7.1"]
            [lein-deps-tree "0.1.2" 
             :exclusions [org.clojure/clojure com.cemerick/pomegranate]]]
  :cljsbuild {:builds [{:id "dev"
                        :source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/cljs.js"
                                   :optimizations :simple
                                   :pretty-print true
                                   :externs ["resources/public/js/flapjax-externs.js"]}}
                       {:id "mobile"
                        :source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/cljs-mobile.js"
                                   :optimizations :advanced
                                   :externs ["resources/public/js/android_externs.js"
                                             "resources/public/js/flapjax-externs.js"]
                                   :pretty-print false}}
                       {:id "mobile-debug"
                        :source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/cljs-mobile.js"
                                   :optimizations :simple
                                   :pretty-print true
                                   :externs ["resources/public/js/android_externs.js"
                                             "resources/public/js/flapjax-externs.js"]}}]}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]})
