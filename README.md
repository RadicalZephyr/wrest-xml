# wrest-xml

This library provides a thin convenience wrapper on top of
`clojure.data.xml` and `clojure.data.zip.xml`.

Zippers are great, but they're kind of confusing, especially if you
practice print-line debugging. Dumping a zipper of a large XML
document to your console is... well, to put it mildly,
dissatisfying. In addition, since zippers are represented with a
Clojure vector, it can be hard to tell when you have a zipper in hand
if you (or your colleague) don't know much about them.

Enter wrest-xml (pronounced "wrestle" - because I can). Provides some
what I think are more expressive names for some common use-cases of
the `xml->` and `xml1->` and a shim record so you can treat zippers as
opaque data-structures. This way when your coworker picks up the
project in 3 months and in an effort to figure out what's going on
with your code throws in some `println`s they don't then try to
directly extract the data they want from the zipper...

## Usage

FIXME (Yeah, getting there...)

## License

Copyright © 2016 Geoff Shannon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
