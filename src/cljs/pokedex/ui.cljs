(ns cljs.pokedex.ui
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [cljs.pokedex.client :refer [fetch-all-type-counts]]
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
                              (reset! app-state (js/JSON.parse data))) )}
    "Refresh Data"]])

(defn init-ui []
  ;; Fetch data when the UI initializes.
  (go (let [data (<! (fetch-all-type-counts))]
        (reset! app-state (js/JSON.parse data))))
  (dom/render [main-ui]
            (js/document.getElementById "app")))