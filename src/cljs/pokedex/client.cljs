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