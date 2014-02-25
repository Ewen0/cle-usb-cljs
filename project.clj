(defproject com.ewen.cle-usb-cljs "0.1.0-SNAPSHOT"
  :description "cle-usb client interface"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :test-paths ["test" "test-server"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [domina "1.0.2"]
                 [om "0.5.0"]
                 [compojure "1.1.3"]
                 [ring-serve "0.1.2"]
                 [ring/ring-devel "1.1.6" :scope "test"]
                 [ring/ring-core "1.1.6"]
                 [sablono "0.2.6"]
                 [ewen/dragdrop "0.1.0-SNAPSHOT"]]
  :dev-dependencies [[lein-cljsbuild "1.0.1"]]
  :plugins [[lein-cljsbuild "1.0.1"]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                        :output-to "resources/public/cljs/cle-usb.js"
                        :output-dir "resources/public/cljs/"
                        :optimizations :none
                        :source-map true}}]})
