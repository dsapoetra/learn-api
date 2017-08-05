(ns app.routes.home
  (:require [app.layout :as layout]
            [compojure.core :refer [defroutes GET PUT]]
            [ring.util.http-response :as response]
            [ring.util.response :as r]
            [app.logic :as logic]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))






(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
           (GET "/" [] (home-page))
           (PUT "/transactionservice/transaction/:transaction-id" [transaction-id] (fn [req]
                                                                                     (let [amount (get-in req [:params :amount])
                                                                                           types (get-in req [:params :type])
                                                                                           parent-id (get-in req [:params :parent-id])]
                                                                                       (do (logic/save-transaction transaction-id {:transaction-id transaction-id :amount amount :type types :parent-id parent-id})
                                                                                           (r/response {:status "ok" })))))
           (GET "/transactionservice/transaction/:transaction-id" [transaction-id] (r/response (logic/get-transaction transaction-id)))
           (GET "/transactionservice/types/:type" [type] (r/response (logic/get-transaction-id-by-type type)))
           (GET "/transactionservice/sum/:transaction-id" [transaction-id] (r/response (logic/get-sum-transaction-by-id transaction-id)))
           (GET "/about" [] (about-page)))

