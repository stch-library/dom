(ns stch.dom.protocol
  "Protocol implementations for
  NodeList and HTMLCollection.")

(extend-type js/NodeList
  ISeqable
  (-seq [nlist] (array-seq nlist 0))
  ICounted
  (-count [nlist] (alength nlist))
  IIndexed
  (-nth
   ([nlist n]
    (if (< n (alength nlist)) (aget nlist n)))
   ([nlist n not-found]
    (if (< n (alength nlist)) (aget nlist n)
      not-found)))
  ILookup
  (-lookup
   ([nlist k]
    (aget nlist k))
   ([nlist k not-found]
    (-nth nlist k not-found))))

(extend-type js/HTMLCollection
  ISeqable
  (-seq [nlist] (array-seq nlist 0))
  ICounted
  (-count [nlist] (alength nlist))
  IIndexed
  (-nth
   ([nlist n]
    (if (< n (alength nlist)) (aget nlist n)))
   ([nlist n not-found]
    (if (< n (alength nlist)) (aget nlist n)
      not-found)))
  ILookup
  (-lookup
   ([nlist k]
    (aget nlist k))
   ([nlist k not-found]
    (-nth nlist k not-found))))





