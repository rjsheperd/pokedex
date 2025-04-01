(ns pokedex.server
  (:require
    [ring.adapter.jetty :as jetty]
    [compojure.core     :as comp]
    [compojure.route    :as route]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [clojure.pprint     :as pprint]
    [pokedex.pokemon    :refer [count-pokemon-by-type get-all-type-counts get-type-list]])
  (:gen-class))

(defonce server (atom nil))

(comp/defroutes routes
    (comp/GET "/" []
      {:status 200
       :body "<h1>Homepage</h1>
              <ul>
                <li><a href=\"/echo\">Echo request</a></li>
              </ul>"
       :headers {"Content-Type" "text/html; charset=UTF-8"}})
    (comp/GET "/api/q-type-count" req
      (let [{:keys [type gen]} (:params req)
            result (if (= gen nil)
              (count-pokemon-by-type type)
              (count-pokemon-by-type type (parse-long gen)))]
        (println result)
        {:status 200
         :body (str result)
         :headers {"Content-Type" "text/plain"}}))
    (comp/GET "/api/all-type-counts" []
      {:status 200
       :body (str (get-all-type-counts))
       :headers {"Content-Type" "text/plain"}})
    (comp/GET "/api/get-types" []
      {:status 200
       :body (str (get-type-list))
       :headers {"Content-Type" "text/plain"}})
    (comp/ANY "/echo" req
      {:status 200
       :body (with-out-str (pprint/pprint req))  ;; use the req binding
       :headers {"Content-Type" "text/plain"}})
    (route/not-found {:status 404
                      :body "Not found."
                      :headers {"Content-Type" "text/plain"}}))

(def app
  (-> (fn [req] (routes req))
      wrap-keyword-params
      wrap-params
      ))

(defn start-server! []
  (reset! server
          (jetty/run-jetty (fn [req] (app req))
                           {:port 9020
                            :join? false}))) ;; don't block the main thread

(defn stop-server! []
  (when-some [s @server] ;; check if there is an object in the atom
    (.stop s)            ;; call the .stop method
    (reset! server nil)));; overwrite the atom with nil

(comment
  (stop-server!)
  (start-server!)
  )

(defn -main []
  (start-server!)
  (print "Started server on port 3001."))