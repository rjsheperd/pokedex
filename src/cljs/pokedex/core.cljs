(ns pokedex.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as dom]
            [graph.events]    ;; load graph events
            [graph.subs]      ;; load graph subscriptions
            [graph.view :refer [graph-view]]))

(defn init []
  (re-frame/dispatch-sync [:graph/initialize-db])
  (re-frame/dispatch [:graph/fetch-type-options])
  (re-frame/dispatch [:graph/fetch-all-type-counts])
  (dom/render [graph-view]
              (.getElementById js/document "app")))