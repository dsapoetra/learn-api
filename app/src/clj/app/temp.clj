(ns app.temp)


(defmacro with-timeout [millis & body]
  `(let [future# (future ~@body)]
     (try
       (.get future# ~millis java.util.concurrent.TimeUnit/MILLISECONDS)
       (catch java.util.concurrent.TimeoutException x#
         (do
           (future-cancel future#)
           nil)))))

(defn kpk [x x1 y y1]
  "Trying when recursive literally has no basis, so limit with time, this one, 20s"
  (let [res (with-timeout 20000
                          (loop [x x x1 x1 y y y1 y1]
                            (if (= x y)
                              true
                              (recur (+ x x1) x1 (+ y y1) y1))))]
    (if (nil? res)
      false
      res)))


(defn count-occurence [s]
  (->>
    (partition-by identity s)
    (map #(do [(str (first %)) (count %)]))
    (flatten)
    (apply str)))

(defn calculate [x y]
  (try
    (map #(*' %1 %2) x y)
    (catch Exception e
      (prn "caught" e))))

;;In number 2, I don't know where transform function whereabout, thus, assuming that's the only code, it'll error. And as a clojure programmer, I don't know which class (or ns, in clojure) that "own" transform function. Should Java written like <Class>.<Method>?