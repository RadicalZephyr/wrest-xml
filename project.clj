(defproject wrest-xml "0.1.1"
  :description "Get just exactly the data you need from those pesky XML documents."
  :url "https://github.com/RadicalZephyr/wrest-xml"
  :scm {:name "git"
        :url "https://github.com/RadicalZephyr/wrest-xml"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :deploy-repositories [["releases" :clojars]]

  :test-paths ["spec"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.2"]]

  :profiles {:dev {:dependencies [[speclj "3.3.2"]]
                   :plugins [[speclj "3.3.2"]]}})
