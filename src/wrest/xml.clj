(ns wrest.xml
  (:refer-clojure :exclude [when])
  (:require [clojure.core :as c]
            [clojure.zip :as zip]
            [clojure.data.xml :as xml]
            [clojure.data.zip :as data-zip]
            [clojure.data.zip.xml :as xml-zip]))

(def parse-str xml/parse-str)

(def attr= xml-zip/attr=)

(def children data-zip/children)

(defn when [& predicate-path]
  (vec predicate-path))

(defn- xml-zip [el]
  (if (= (class el)
         clojure.data.xml.Element)
    (zip/xml-zip el)
    el))

(defn- extract-body [zip-fn el-sym path-transform]
  `(apply ~zip-fn (xml-zip ~el-sym) ~path-transform))

(defn- derive-fnames [fnames]
  (if (and (vector? fnames)
           (= 2 (count fnames)))
    fnames
    (let [fname fnames]
      [fnames (-> fnames (str "-from-many") symbol)])))

(defmacro defextract
  "This macro will define a pair of named functions as wrappers on xml-> and xml1->.

  By default `<name>' and `<name>-from-many', but if a vector of two
  symbols is passed as the first argument, then those symbols will be
  used.

  These functions correspond to using the xml1-> and xml-> functions
  respectively. The arg-vec parameter is used as the argument vector
  for both functions, and the first element is assumed to be the xml
  element that is being operated on. The body of the `defextract'
  should contain one expression that returns a sequence that
  represents the desired path through the xml document. This sequence
  will be applied as the final arguments to one of the xml*->
  functions."
  ([fname arg-vec path-transform]
   `(defextract ~fname "" ~arg-vec ~path-transform))
  ([fnames docstring arg-vec path-transform]
   (let [[fname many-fn-name] (derive-fnames fnames)
         xml-el-sym   (first arg-vec)]
     `(do
        (defn ~fname ~docstring ~arg-vec
          (c/when (seq ~xml-el-sym)
            ~(extract-body xml-zip/xml1-> xml-el-sym path-transform)))

        (defn ~many-fn-name ~docstring ~arg-vec
          (c/when (seq ~xml-el-sym)
            ~(extract-body xml-zip/xml->   xml-el-sym path-transform)))))))

(defextract extract-text [el & path]
  (concat path [xml-zip/text]))

(defextract extract-attribute [el & path]
  (let [path-to (butlast path)
        attr-name (last path)]
    (concat path-to [(xml-zip/attr attr-name)])))

(defextract [extract-element extract-elements]
  "Returns an XML Zipper pointing at the element specified.

  This value should not be used directly, but instead should be
  passed to further extract-* calls."
  [el & path] path)

(defn extract-text-from-children [el & path]
  (let [element (apply extract-element el path)]
    (if (seq element)
      (map #(extract-text %) (children element))
      [""])))
