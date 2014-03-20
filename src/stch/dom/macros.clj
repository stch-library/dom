(ns stch.dom.macros
  "DOM macros.")

(defmacro ready
  "Execute forms when DOM is ready."
  [& forms]
  `(stch.dom.ready (fn [] ~@forms)))
