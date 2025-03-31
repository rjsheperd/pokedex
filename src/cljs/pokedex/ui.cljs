(ns pokedex.ui
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [clojure.edn :as edn]
            [pokedex.client :refer [fetch-all-type-counts]]
            [cljs.pprint :as pprint]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; Define an atom to hold the fetched data.
(defonce app-state (r/atom {}))

(defn main-ui []
  [:div
   [:h1 "All Type Counts"]
   ;; Display the data from the app-state atom.
   [:pre (with-out-str (pprint/pprint @app-state))]
   [:button {:on-click #(go (let [data (<! (fetch-all-type-counts))]
                              (reset! app-state (edn/read-string data))) )}
    "Refresh Data"]])

(defn init []
  ;; Fetch data when the UI initializes.
  (go (let [data (<! (fetch-all-type-counts))]
        (reset! app-state (edn/read-string data))))
  (dom/render [main-ui]
              (.getElementById js/document "app")))