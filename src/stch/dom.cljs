(ns stch.dom
  "DOM manipulation and event handling."
  (:require [goog.dom :as gdom]
            [goog.dom.classes :as gclasses]
            [clojure.string :as string]
            [stch.dom.protocol]
            [goog.userAgent :as gagent])
  (:refer-clojure :exclude [remove]))

(declare is-element? is-node?)

(defprotocol IDOM
  ; ids
  (get-id [el])
  (set-id [el id])
  (remove-id [el])
  ; classes
  (add-class [el cls])
  (get-classes [el])
  (set-classes [el cls])
  (update-classes [el f])
  (toggle-class [el cls])
  (swap-class [el from-cls to-cls])
  (enable-class [el cls enabled])
  (has-class? [el cls])
  (remove-class [el cls])
  (clear-classes [el])
  ; attributes
  (get-attr [el attr])
  (set-attr [el attr value])
  (has-attr? [el attr])
  (update-attr [el attr f])
  (remove-attr [el attr])
  ; style
  (get-style [el])
  (set-style [el styles])
  ; events
  (listen [el ev f] [el ev f cap])
  (unlisten [el ev f])
  (dispatch-event [el ev])
  ; value
  (get-val [el])
  (set-val [el value])
  (update-val [el f])
  (clear-val [el])
  ; inner html
  (get-html [el])
  (set-html [el html])
  (update-html [el f])
  (clear-html [el])
  ; outer html
  (outer-html [el])
  ; tag name
  (tag-name [el])
  ; text
  (get-text [el])
  (get-raw-text [el])
  (set-text [el text])
  (data [el])
  ; dataset
  (get-data [el attr])
  (set-data [el attr value])
  (update-data [el attr f])
  (remove-data [el attr])
  ; manipulation
  (append [el node])
  (prepend [el node])
  (before [el node])
  (after [el node])
  (remove [el])
  (remove-children [el])
  (swap [el node])
  (flatten-element [el])
  ; node retrieval
  (children [el])
  (first-child [el])
  (last-child [el])
  (nth-child [el n])
  (next-sibling [el])
  (previous-sibling [el])
  (next-node [el])
  (previous-node [el])
  (parent [el])
  (contains-node? [el node])
  (find-node [el pred])
  (find-nodes [el pred])
  (get-ancestor [el pred])
  (common-ancestor [el1 el2]))

(extend-protocol IDOM
  nil
  (get-id [el])
  (set-id [el id])
  (remove-id [el])
  ; classes
  (add-class [el cls])
  (get-classes [el])
  (set-classes [el cls])
  (update-classes [el f])
  (toggle-class [el cls])
  (swap-class [el from-cls to-cls])
  (enable-class [el cls enabled])
  (has-class? [el cls])
  (remove-class [el cls])
  (clear-classes [el])
  ; attributes
  (get-attr [el attr])
  (set-attr [el attr value])
  (has-attr? [el attr])
  (update-attr [el attr f])
  (remove-attr [el attr])
  ; style
  (get-style [el])
  (set-style [el styles])
  ; events
  (listen [el ev f])
  (listen [el ev f cap])
  (unlisten [el ev f])
  (dispatch-event [el ev])
  ; value
  (get-val [el])
  (set-val [el value])
  (update-val [el f])
  (clear-val [el])
  ; inner html
  (get-html [el])
  (set-html [el html])
  (update-html [el f])
  (clear-html [el])
  ; outer html
  (outer-html [el])
  ; tag name
  (tag-name [el])
  ; text
  (get-text [el])
  (get-raw-text [el])
  (set-text [el text])
  (data [el])
  ; dataset
  (get-data [el attr])
  (set-data [el attr value])
  (update-data [el attr f])
  (remove-data [el attr])
  ; manipulation
  (append [el node])
  (prepend [el node])
  (before [el node])
  (after [el node])
  (remove [el])
  (remove-children [el])
  (swap [el node])
  (flatten-element [el])
  ; node retrieval
  (children [el])
  (first-child [el])
  (last-child [el])
  (nth-child [el n])
  (next-sibling [el])
  (previous-sibling [el])
  (next-node [el])
  (previous-node [el])
  (parent [el])
  (contains-node? [el node])
  (find-node [el pred])
  (find-nodes [el pred])
  (get-ancestor [el pred])
  (common-ancestor [el1 el2])

  js/Node
  ; text
  (data [el]
    (.-data el))
  ; events
  (listen [el ev f]
    (.addEventListener el (name ev) f)
    el)
  (listen [el ev f cap]
    (.addEventListener el (name ev) f cap)
    el)
  (unlisten [el ev f]
    (.removeEventListener el (name ev) f)
    el)
  (dispatch-event [el ev]
    (.dispatchEvent el (js/CustomEvent. ev)))
  ; node retrieval
  (children [el]
    (gdom/getChildren el))
  (first-child [el]
    (gdom/getFirstElementChild el))
  (last-child [el]
    (gdom/getLastElementChild el))
  (nth-child [el n]
    (get (children el) n))
  (next-sibling [el]
    (gdom/getNextElementSibling el))
  (previous-sibling [el]
    (gdom/getPreviousElementSibling el))
  (next-node [el]
    (gdom/getNextNode el))
  (previous-node [el]
    (gdom/getPreviousNode el))
  (parent [el]
    (gdom/getParentElement el))
  (contains-node? [el node]
    (gdom/contains el node))
  (find-node [el pred]
    (gdom/findNode el pred))
  (find-nodes [el pred]
    (lazy-seq (gdom/findNodes el pred)))
  (get-ancestor [el pred]
    (gdom/getAncestor el pred))
  (common-ancestor [el1 el2]
    (gdom/findCommonAncestor el1 el2))
  ; manipulation
  (append [el node]
    (gdom/appendChild el node)
    el)
  (prepend [el node]
    (gdom/insertChildAt el node 0)
    el)
  (before [el node]
    (gdom/insertSiblingBefore node el)
    el)
  (after [el node]
    (gdom/insertSiblingAfter node el)
    el)
  (remove [el]
    (gdom/removeNode el)
    el)
  (remove-children [el]
    (gdom/removeChildren el)
    el)
  (swap [el node]
    (gdom/replaceNode node el)
    node)
  (flatten-element [el]
    (gdom/flattenElement el))

  js/Element
  ; ids
  (get-id [el]
    (.-id el))
  (set-id [el id]
    (set! (.-id el) (name id))
    el)
  (remove-id [el]
    (set! (.-id el) nil)
    el)
  ; classes
  (add-class [el cls]
    (gclasses/add el (name cls))
    el)
  (get-classes [el]
    (let [cls (.-className el)]
      (when-not (string/blank? cls)
        (-> cls
            (string/split #"\s+")
            (set)))))
  (set-classes [el cls]
    (let [c (cond
              (string? cls) cls

              (keyword? cls) (name cls)

              (seqable? cls)
              (string/join \space (map name cls)))]
    (gclasses/set el c))
    el)
  (update-classes [el f]
    (set-classes el (f (get-classes el)))
    el)
  (toggle-class [el cls]
    (gclasses/toggle el (name cls))
    el)
  (swap-class [el from-cls to-cls]
    (gclasses/swap el (name from-cls) (name to-cls))
    el)
  (enable-class [el cls enabled]
    (gclasses/enable el (name cls) enabled)
    el)
  (has-class? [el cls]
    (gclasses/has el (name cls)))
  (remove-class [el cls]
    (gclasses/remove el (name cls))
    el)
  (clear-classes [el]
    (gclasses/set el "")
    el)
  ; attributes
  (get-attr [el attr]
    (.getAttribute el (name attr)))
  (set-attr [el attr value]
    (.setAttribute el (name attr) value)
    el)
  (has-attr? [el attr]
    (.hasAttribute el (name attr)))
  (update-attr [el attr f]
    (set-attr el attr (f (get-attr el attr)))
    el)
  (remove-attr [el attr]
    (.removeAttribute el (name attr))
    el)
  ; style
  (get-style [el]
    (when-let [style-str (get-attr el "style")]
      (let [styles (-> style-str
                       (string/split #";"))]
        (->> styles
             (clojure.core/remove string/blank?)
             (map #(string/split % #":"))
             (map (fn [[k v]] [(keyword k) (string/triml v)]))
             (into {})))))
  (set-style [el styles]
    (letfn [(sf [[k v]] (str (name k) ":" v))]
      (cond (string? styles)
            (set! (.-style el) styles)

            (map? styles)
            (set! (.-style el)
                  (string/join ";" (map sf styles))))))
  ; value
  (get-val [el]
    (.-value el))
  (set-val [el value]
    (set! (.-value el) value)
    el)
  (clear-val [el]
    (set! (.-value el) "")
    el)
  (update-val [el f]
    (set-val el (f (get-val el)))
    el)
  ; inner html
  (get-html [el]
    (.-innerHTML el))
  (set-html [el html]
    (cond
      (string? html)
      (set! (.-innerHTML el) html)

      (or (is-node? html)
          (is-element? html))
      (-> el (remove-children)
             (append html)))
    el)
  (update-html [el f]
    (set-html el (f (get-html el)))
    el)
  (clear-html [el]
    (set! (.-innerHTML el) "")
    el)
  ; outer html
  (outer-html [el]
    (.-outerHTML el))
  ; tag name
  (tag-name [el]
    (string/lower-case (.-tagName el)))
  ; text
  (get-text [el]
    (gdom/getTextContent el))
  (get-raw-text [el]
    (gdom/getRawTextContent el))
  (set-text [el text]
    (gdom/setTextContent el text))
  ; dataset
  (get-data [el attr]
    (if gagent/IE
      (get-attr el (str "data-" (name attr)))
      (aget (.-dataset el) (name attr))))
  (set-data [el attr value]
    (if gagent/IE
      (set-attr el (str "data-" (name attr)) value)
      (aset (.-dataset el) (name attr) value))
    el)
  (update-data [el attr f]
    (set-data el attr (f (get-data el attr)))
    el)
  (remove-data [el attr]
    (if gagent/IE
      (remove-attr el (str "data-" (name attr)))
      (js-delete (.-dataset el) (name attr)))
    el)

  LazySeq
  ; ids
  (get-id [els]
    (for [el els]
      (get-id el)))
  (set-id [els id]
    (doseq [el els]
      (set-id el id))
    els)
  (remove-id [els]
    (doseq [el els]
      (remove-id el))
    els)
  ; classes
  (add-class [els cls]
    (doseq [el els]
      (add-class el cls))
    els)
  (get-classes [els]
    (for [el els]
      (get-classes el)))
  (set-classes [els cls]
    (doseq [el els]
      (set-classes el cls))
    els)
  (update-classes [els f]
    (doseq [el els]
      (update-classes el f))
    els)
  (toggle-class [els cls]
    (doseq [el els]
      (toggle-class el cls))
    els)
  (swap-class [els from-cls to-cls]
    (doseq [el els]
      (swap-class el from-cls to-cls))
    els)
  (enable-class [els cls enabled]
    (doseq [el els]
      (enable-class el cls enabled))
    els)
  (has-class? [els cls]
    (for [el els]
      (has-class? el cls)))
  (remove-class [els cls]
    (doseq [el els]
      (remove-class el cls))
    els)
  ; attributes
  (get-attr [els attr]
    (for [el els]
      (get-attr el attr)))
  (set-attr [els attr value]
    (doseq [el els]
      (set-attr el attr value))
    els)
  (has-attr? [els attr]
    (for [el els]
      (has-attr? el attr)))
  (update-attr [els attr f]
    (doseq [el els]
      (update-attr el attr f))
    els)
  (remove-attr [els attr]
    (doseq [el els]
      (remove-attr el attr))
    els)
  ; style
  (get-style [els]
    (for [el els]
      (get-style el)))
  (set-style [els styles]
    (doseq [el els]
      (set-style el styles))
    els)
  ; events
  (listen [els ev f]
    (doseq [el els]
      (listen el ev f))
    els)
  (listen [els ev f cap]
    (doseq [el els]
      (listen el ev f cap))
    els)
  (unlisten [els ev f]
    (doseq [el els]
      (unlisten el ev f))
    els)
  (dispatch-event [els ev]
    (doseq [el els]
      (dispatch-event el ev))
    els)
  ; value
  (get-val [els]
    (for [el els]
      (get-val el)))
  (set-val [els value]
    (doseq [el els]
      (set-val el value))
    els)
  (update-val [els f]
    (doseq [el els]
      (update-val el f))
    els)
  (clear-val [els]
    (doseq [el els]
      (clear-val el))
    els)
  ; inner html
  (get-html [els]
    (for [el els]
      (get-html el)))
  (set-html [els html]
    (doseq [el els]
      (set-html el html))
    els)
  (update-html [els f]
    (doseq [el els]
      (update-html el f))
    els)
  (clear-html [els]
    (doseq [el els]
      (clear-html el))
    els)
  ; outer html
  (outer-html [els]
    (for [el els]
      (outer-html el)))
  ; tag name
  (tag-name [els]
    (for [el els]
      (tag-name el)))
  ; text
  (get-text [els]
    (for [el els]
      (get-text el)))
  (get-raw-text [els]
    (for [el els]
      (get-raw-text el)))
  (set-text [els text]
    (doseq [el els]
      (set-text el text))
    els)
  (data [els]
    (for [el els]
      (data el)))
  ; dataset
  (get-data [els attr]
    (for [el els]
      (get-data el attr)))
  (set-data [els attr value]
    (doseq [el els]
      (set-data el attr value))
    els)
  (update-data [els attr f]
    (doseq [el els]
      (update-data el attr f))
    els)
  (remove-data [els attr]
    (doseq [el els]
      (remove-data el attr))
    els)
  ; manipulation
  (append [els node]
    (doseq [el els]
      (append el node))
    els)
  (prepend [els node]
    (doseq [el els]
      (prepend el node))
    els)
  (before [els node]
    (doseq [el els]
      (before el node))
    els)
  (after [els node]
    (doseq [el els]
      (after el node))
    els)
  (remove [els]
    (doseq [el els]
      (remove el))
    els)
  (remove-children [els]
    (doseq [el els]
      (remove-children el))
    els)
  (swap [els node]
    (doseq [el els]
      (swap el node))
    els)
  (flatten-element [els]
    (doseq [el els]
      (flatten-element el)))
  ; node retrieval
  (children [els]
    (for [el els]
      (children el)))
  (first-child [els]
    (for [el els]
      (first-child el)))
  (last-child [els]
    (for [el els]
      (last-child el)))
  (nth-child [els n]
    (for [el els]
      (nth-child el n)))
  (next-sibling [els]
    (for [el els]
      (next-sibling el)))
  (previous-sibling [els]
    (for [el els]
      (previous-sibling el)))
  (next-node [els]
    (for [el els]
      (next-node el)))
  (previous-node [els]
    (for [el els]
      (previous-node el)))
  (parent [els]
    (for [el els]
      (parent el)))
  (contains-node? [els node]
    (for [el els]
      (contains-node? el node)))
  (find-node [els pred]
    (for [el els]
      (find-node el pred)))
  (find-nodes [els pred]
    (for [el els]
      (find-nodes el pred)))
  (get-ancestor [els pred]
    (for [el els]
      (get-ancestor el pred))))

; Type predicates

(defn ^boolean is-node? [el]
  (gdom/isNodeLike el))

(defn ^boolean is-element? [el]
  (gdom/isElement el))

(defn ^boolean is-window? [el]
  (gdom/isWindow el))

; Scrolling

(defn scroll [y-coord]
  (js/window.scroll 0 y-coord))

(defn scroll-top []
  (scroll 0))

(defn scroll-bottom []
  (scroll js/document.body.scrollHeight))

; Events

(defn ready
  "Fire f when the DOM is ready"
  [f]
  (listen js/document :DOMContentLoaded f))

(defn hash-change [f]
  (set! js/document.body.onhashchange f))

; Target

(defn current-target [e]
  (.-currentTarget e))

(defn target [e]
  (.-target e))

; Dimensions

(defn viewport-size []
  (gdom/getViewportSize))

(defn document-height []
  (gdom/getDocumentHeight))

(defn page-scroll []
  (gdom/getPageScroll))

(defn document-scroll []
  (gdom/getDocumentScroll))

; Conversion

(defn html->docfrag
  "Convert a string of html to a document fragment."
  [html]
  (gdom/htmlToDocumentFragment html))

; Form

(defn get-radio
  "Get the value of a group of radio buttons."
  [els]
  (-> (filter #(.-checked %) els)
      first
      get-val))

(defn check [el]
  (set! (.-checked el) "checked"))

(defn set-radio
  "Set the value for one or more radio buttons."
  [els v]
  (-> (filter #(= (get-val %) v) els)
      first
      check))

(defn get-checkbox
  "Get the value(s) of one or more checkboxes.
  Returns a set."
  [& els]
  (-> (filter #(.-checked %) els)
      get-val
      set))

; Element querying

(defn el-or-ancestor
  "Select the first element that returns true for pred.
  Start with el and traverse up the DOM if that returns false."
  [el pred]
  (if (pred el) el
    (get-ancestor el pred)))

(defn by-id
  "Retrieve the element with the corresponding id."
  [id]
  (.getElementById js/document (name id)))

(defn sel
  "Query using CSS selectors.
  Returns a LazySeq of nodes."
  ([selectors]
   (sel js/document selectors))
  ([el selectors]
   (when el
     (lazy-seq (.querySelectorAll el (name selectors))))))

(defn sel1
  "Query using CSS selectors.
  Returns the first matched element."
  ([selectors]
   (.querySelector js/document selectors))
  ([el selectors]
   (when el
     (.querySelector el (name selectors)))))






