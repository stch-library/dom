(defproject stch-library/dom "0.1.0"
  :description "DOM manipulation and event handling."
  :url "https://github.com/stch-library/dom"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]]
  :plugins [[lein-cljsbuild "1.0.2"]]
  :cljsbuild
  {:builds [{:id "browser-test"
             :source-paths ["src"]
             :compiler
             {:output-to "resources/js/browser-test.js"
              :optimizations :whitespace
              :pretty-print true}}]})
