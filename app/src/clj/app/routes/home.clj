(ns app.routes.home
  (:require [app.layout :as layout]
            [compojure.core :refer [defroutes GET PUT]]
            [ring.util.http-response :as response]
            [ring.util.response :as r]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(def data (atom {}))

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



(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
           (GET "/" [] (home-page))
           (PUT "/transactionservice/transaction/:transaction-id" [transaction-id] (fn [req]
                                                                                     (let [amount (get-in req [:params :amount])
                                                                                           types (get-in req [:params :type])
                                                                                           parent-id (get-in req [:params :parent-id])]
                                                                                       (do (save-transaction transaction-id {:transaction-id transaction-id :amount amount :type types :parent-id parent-id})
                                                                                           (r/response {:status "ok" })))))
           (GET "/transactionservice/transaction/:transaction-id" [transaction-id] (r/response (get-transaction transaction-id)))
           (GET "/transactionservice/types/:type" [type] (r/response (get-transaction-id-by-type type)))
           (GET "/transactionservice/sum/:transaction-id" [transaction-id] (r/response (get-sum-transaction-by-id transaction-id)))
           (GET "/about" [] (about-page)))

