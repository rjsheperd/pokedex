(ns pokedex.client
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn fetch-all-type-counts []
  (go
    (let [response (<! (http/get "/api/all-type-counts" {:with-credentials? false}))]
      (if (= 200 (:status response))
        (:body response)
        (do (println "Error fetching all type counts:" response)
            nil)))))

(defn fetch-type-list []
  (go
    (let [response (<! (http/get "/api/get-types" {:with-credentials? false}))]
      (if (= 200 (:status response))
        (:body response)
        (do (println "Error fetching types:" response)
            nil)))))

(defn fetch-type-count
  ([type]
   (go
     (let [response (<! (http/get "/api/q-type-count" {:with-credentials? false
                                                       :query-params {:type type}}))]
       (if (= 200 (:status response))
         (:body response)
         (do (println "Error fetching type count:" response)
             0)))))
  ([type gen]
   (go
     (let [response (<! (http/get "/api/q-type-count" {:with-credentials? false
                                                       :query-params {:type type :gen gen}}))]
       (if (= 200 (:status response))
         (:body response)
         (do (println "Error fetching type count:" response)
             0))))))