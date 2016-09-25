# wrest-xml

This library provides a thin convenience wrapper on top of
`clojure.data.xml` and `clojure.data.zip.xml`.

Zippers are great, but they're kind of confusing, especially if you
practice print-line debugging. Dumping a zipper of a large XML
document to your console is... well, to put it mildly,
dissatisfying. In addition, since zippers are represented with a
Clojure vector, it can be hard to tell when you have a zipper in hand
if you're not familiar with this detail of their representation.

Enter wrest-xml (pronounced "wrestle" - because I can and working with
XML always feels like wrestling to me). Provides what I think are more
expressive names for some common use-cases of the `xml->` and `xml1->`
fns and (eventually) a shim record so you can treat zippers as opaque
data-structures. This way when your coworker picks up the project in 3
months and in an effort to figure out what's going on with your code
throws in some `println`s they don't then try to directly extract the
data they want from the zipper...

## Usage

``` clojure
[wrest-xml "0.1.0"]
```

Then check out the [specs](/blob/master/spec/wrest/xml_spec.clj) for
some example usage.

## License

Copyright © 2016 Geoff Shannon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
