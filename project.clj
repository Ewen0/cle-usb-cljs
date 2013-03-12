(defproject com.ewen.cle-usb-cljs "0.1.0-SNAPSHOT"
  :description "cle-usb client interface"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :test-paths ["test" "test-server"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1586"]
                 [com.cemerick/piggieback "0.0.4" :scope "test"]
                 [compojure "1.1.3"]
                 [ring-serve "0.1.2"]
                 [ring/ring-devel "1.1.6" :scope "test"]
                 [ring/ring-core "1.1.6"]
                 [hiccup "1.0.2"]
                 [enlive "1.0.1"]
                 [ring-refresh "0.1.1" :scope "test"]
                 [enfocus "1.0.1-SNAPSHOT"]
                 [domina "1.0.1"]
                 [com.ewen.flapjax-cljs "1.0.1-SNAPSHOT"]
                 [com.ewen.utils-cljs "1.0.0-RELEASE"]]
  :dev-dependencies [[lein-cljsbuild "0.3.0"]
                     [lein-marginalia "0.7.1"]]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-marginalia "0.7.1"]
            [lein-deps-tree "0.1.2" 
             :exclusions [org.clojure/clojure com.cemerick/pomegranate]]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src-cljs" "browser-repl"]
                        :compiler {:output-to "resources/public/js/cljs.js"
                                   :optimizations :simple
                                   :pretty-print true}}
                       {:id "mobile"
                        :source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/cljs.js"
                                   :optimizations :advanced
                                   :externs ["resources/public/js/android_externs.js"]
                                   :pretty-print false}}
                       {:id "mobile-debug"
                        :source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/cljs.js"
                                   :optimizations :simple
                                   :pretty-print true
                                   :externs ["resources/public/js/android_externs.js"]}}]}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]})
