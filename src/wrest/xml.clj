(ns wrest.xml
  (:refer-clojure :exclude [when])
  (:require [clojure.core :as c]
            [clojure.zip :as zip]
            [clojure.data.xml :as xml]
            [clojure.data.zip :as data-zip]
            [clojure.string :as string]
            [clojure.data.zip.xml :as xml-zip]))


(def attr= xml-zip/attr=)

(def children data-zip/children)

(defn- xml-zip [el]
  (if (= (class el)
         clojure.data.xml.Element)
    (zip/xml-zip el)
    el))

(defn- extract-text-with [xml-fn el & path]
  (c/when (seq el)
    (let [xml-path (concat path [xml-zip/text])]
      (apply xml-fn (xml-zip el) xml-path))))

(def extract-text
  (partial extract-text-with xml-zip/xml1->))

(def extract-text-from-many
  (partial extract-text-with xml-zip/xml->))

(defn- extract-attribute-with [xml-fn el & path]
  (c/when (seq el)
    (let [path-to (butlast path)
          attr-name (last path)
          xml-path (concat path-to [(xml-zip/attr attr-name)])]
      (apply xml-fn (xml-zip el) xml-path))))

(def extract-attribute
  (partial extract-attribute-with xml-zip/xml1->))

(def extract-attribute-from-many
  (partial extract-attribute-with xml-zip/xml->))

(defn when [& predicate-path]
  (vec predicate-path))

(defn extract-element
  "Returns an XML Zipper pointing at the element specified.

  This return should not be used directly, but instead should be
  passed to further extract-* function calls."
  [el & path]
  (apply xml-zip/xml1-> (xml-zip el) path))

(defn extract-elements
  "Returns an XML Zipper pointing at the elements specified.

  This return should not be used directly, but instead should be
  passed to further extract-* function calls."
  [el & path]
  (apply xml-zip/xml-> (xml-zip el) path))

(defn- escape-invalid-characters [ostensibly-correct-xml]
  (string/replace (string/replace ostensibly-correct-xml
                  #"&(?!(?:amp|quot|apos|lt|gt);)" "&amp;")
                  #"<phone:re:1>" ""))

(defn- parse-str [xml-str]
  (let [format-xml (escape-invalid-characters xml-str)]
    (xml/parse-str format-xml)))

(defn extract-text-from-children [el & path]
  (let [element (apply extract-element el path)]
    (if (seq element)
      (map #(extract-text %) (children element))
      [""])))
