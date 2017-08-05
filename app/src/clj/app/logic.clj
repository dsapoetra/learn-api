(ns app.logic
  (:require
    [app.db :refer [data]]))

(defn save-transaction [id body]
  (swap! data assoc id body))

(defn get-transaction [id]
  (dissoc (get @data id) :transaction-id))

(defn get-transaction-id-by-type [type]
  (let [data-processed (vals @data)]
    (->> (filter #(= type (:type %)) data-processed)
         (map #(:transaction-id %)))))

(defn get-sum-transaction-by-id [id]
  {:sum (+
          (:amount (get @data id )) (->> (vals @data)
                                         (filter #(= (read-string id) (:parent-id %)))
                                         (map #(:amount %))
                                         (apply +')))})
