(ns wrest.xml-spec
  (:require [wrest.xml :as sut]
            [clojure.data.xml :as xml]
            [speclj.core :refer :all]))

(def xml
  (xml/parse-str
   "<TopLevel>
       <FirstChild attr=\"Foo\">
           <GrandChild1>Grand1</GrandChild1>
           <GrandChild2>Grand2</GrandChild2>
       </FirstChild>
       <ManyType many-attr=\"4\">1</ManyType>
       <ManyType many-attr=\"3\">2</ManyType>
       <OtherTag />
       <ManyType many-attr=\"2\">3</ManyType>
       <ManyType many-attr=\"1\">4</ManyType>
       <RandomOtherTag>Hello</RandomOtherTag>
    </TopLevel>"))

(def nested-xml
  (xml/parse-str
    "<TopLevel>
      <FirstChild> I am the first child
        <NestedChild>I am a nested child</NestedChild>
      </FirstChild>
    </TopLevel>"))

(def alt-xml
  (xml/parse-str
   "<TopLevel2>
    </TopLevel2>"))

(def multi-level-xml
  (xml/parse-str
   "<MultiTopLevel>
       <Child number=\"1\">
           <GrandChild>Bar</GrandChild>
           <GrandChild>Baz</GrandChild>
       </Child>
       <Child number=\"2\">
           <GrandChild>Zip</GrandChild>
           <GrandChild>Zop</GrandChild>
       </Child>
    </MultiTopLevel>"))

(describe "extract-text"
  (it "gets one text element"
    (should= "1"
             (sut/extract-text xml :ManyType)))

  (it "gets nested elements"
    (should= "I am a nested child"
             (sut/extract-text nested-xml :FirstChild :NestedChild)))

  (it "does nothing if xml is nil"
    (should= nil
             (sut/extract-text nil :ManyType))))

(describe "extract-text-from-many"
  (it "gets the contents of many text elements"
    (should= ["1" "2" "3" "4"]
             (sut/extract-text-from-many xml :ManyType))))

(describe "extract-attribute"
  (it "gets the contents of an attribute"
    (should= "Foo"
             (sut/extract-attribute xml :FirstChild :attr)))

  (it "gets the contents of an attribute"
    (should= nil
             (sut/extract-attribute nil :FirstChild :attr))))

(describe "extract-attribute-from-many"
  (it "gets the contents of an attribute"
    (should= ["4" "3" "2" "1"]
             (sut/extract-attribute-from-many xml :ManyType :many-attr))))

(describe "extract-text-from-children"
  (it "gets the text from all children elements"
    (should= ["Grand1Grand2" "1" "2" "" "3" "4" "Hello"]
             (sut/extract-text-from-children xml)))

  (it "returns an empty string if the element does not exist"
    (should= [""]
             (sut/extract-text-from-children xml :BogusNode))))

(describe "extract-element"
  (it "returns an 'intermediate value'"
    (let [first-child (sut/extract-element xml :FirstChild)]
      (should= "Grand1" (sut/extract-text first-child :GrandChild1))
      (should= "Grand2" (sut/extract-text first-child :GrandChild2)))))

(describe "extract-elements"
  (it "returns a lazy seq of 'intermediate values'"
    (let [children (sut/extract-elements multi-level-xml :Child)]
      (should= ["1" "2"]
               (map #(sut/extract-attribute % :number)
                    children))
      (should= [["Bar" "Baz"] ["Zip" "Zop"]]
               (map #(sut/extract-text-from-many % :GrandChild)
                    children)))))
