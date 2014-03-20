# stch.dom

ClojureScript library for DOM manipulation and event handling.

## Installation

Add the following to your project dependencies:

```clojure
[stch-library/dom "0.1.0"]
```

## How to use

tch.dom is designed to be used like jQuery.  All calls are chainable were applicable.

Much like jQuery, the following pattern emerges:

1. Query for an element (using by-id, sel, or sel1).
2. Chain calls to get/manipulate the element(s) or add/remove event listeners to the element(s).

For the purposes of this documentation we'll classify all fns as either query or element fns.

Some query fns return more than one element, and some return just one.  Sometimes a query function might return nil, in the case where no elements were found.  stch.dom is designed to handle all three scenarios identically.

```clojure
(ns my.ns
  (:require [stch.dom :as dom])

(-> (dom/by-id "page-title"
    (dom/set-html "New Title")
    (dom/listen :click (fn [e])))

(-> (dom/sel ".link")
    (dom/add-class "highlight")
    (dom/remove-class "muted"))

(-> (dom/by-id "not-found-id"
    (dom/set-html "Something Different"))
```

The element fns can be divided into the following categories:

1. ids
2. classes
3. attributes
4. style
5. events
6. value
7. inner html
8. outer html
9. tag name
10. text
11. dataset
12. manipulation
13. node retrieval

Many of these categories have the same fn prefixes: get, set, update, clear.  For example, in the classes category we have get-classes, set-classes, update-classes, clear-classes.  The names are designed to be self-explanatory.  As a result, there is no specific documentation on each element fn.

Has been tested in IE9 and above.





