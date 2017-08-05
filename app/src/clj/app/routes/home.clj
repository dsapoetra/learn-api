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
  (get @data id))

(defn get-transaction-by-type [])

(defn get-sum-transaction-by-id [])



(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
           (GET "/" [] (home-page))
           (PUT "/transactionservice/transaction/:transaction-id" [transaction-id] (fn [req]
                                                                                     (let [amount (get-in req [:params :amount])
                                                                                           types (get-in req [:params :type])
                                                                                           parent-id (get-in req [:params :parent-id])]
                                                                                       (save-transaction transaction-id {:amount amount :type types :parent-id parent-id}))))
           (GET "/transactionservice/transaction/:transaction-id" [transaction-id] (r/response (str (get-transaction transaction-id))))
           (GET "/transactionservice/types/:type" [] ())
           (GET " /transactionservice/sum/:transaction-id" [] ())
           (GET "/about" [] (about-page)))

