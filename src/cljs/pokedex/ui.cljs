(ns pokedex.ui
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [clojure.edn :as edn]
            [pokedex.client :refer [fetch-all-type-counts fetch-type-list fetch-type-count]]
            [cljs.pprint :as pprint]
            [cljs.core.async :refer [<!]]
            [oz.core :as oz])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; Define an atom to hold the fetched data.
(defonce app-state (r/atom []))

(defonce selected-type (r/atom "Normal"))
(defonce type-options (r/atom []))
(defonce type-results (r/atom []))

(defn type-bar-chart-spec [data]
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :data {:values [data]}
   :mark "bar"
   :encoding {:x {:field "Type"}
              :y {:field "Count" :type "quantitative"}
              :color
              {:field "Type"
               :scale
               {:range
                ["#AABB22" "#775544" "#7766EE" "#FFCC33" "#EE99EE" "#BB5544"
                 "#FF4422" "#8899FF" "#6666BB" "#77CC55" "#DDBB55" "#66CCFF"
                 "#AAAA99" "#AA5599" "#FF5599" "#BBAA66" "#AAAABB" "#3399FF"]
                :domain
                ["Bug" "Dark" "Dragon" "Electric" "Fairy" "Fighting" "Fire"
                 "Flying" "Ghost" "Grass" "Ground" "Ice" "Normal" "Poison"
                 "Psychic" "Rock" "Steel" "Water"]}}}})

(defn multi-bar-chart-spec [data]
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :data {:values data}
   :mark "bar"
   :encoding {:x {:field "Type"}
              :y {:field "Count" :type "quantitative"}
              :facet {:field "Gen" :type "ordinal" :columns 3}
              :color
              {:field "Type"
               :scale
               {:range
                ["#AABB22" "#775544" "#7766EE" "#FFCC33" "#EE99EE" "#BB5544"
                 "#FF4422" "#8899FF" "#6666BB" "#77CC55" "#DDBB55" "#66CCFF"
                 "#AAAA99" "#AA5599" "#FF5599" "#BBAA66" "#AAAABB" "#3399FF"]
                :domain
                ["Bug" "Dark" "Dragon" "Electric" "Fairy" "Fighting" "Fire"
                 "Flying" "Ghost" "Grass" "Ground" "Ice" "Normal" "Poison"
                 "Psychic" "Rock" "Steel" "Water"]}}}})

(defn main-ui []
  [:div {:style {:display "flex"
                 :flexDirection "column"}}
   [:div
    [:h1 "Pokemon Counts by Generation and Type"]
    [:div [:button {:on-click #(go (let [data (<! (fetch-all-type-counts))]
                                     (reset! app-state (edn/read-string data))) )}
                                     "Refresh Data"]]
    [:div
     ;[:pre (with-out-str (pprint/pprint @app-state))]
     [oz/vega-lite (multi-bar-chart-spec @app-state)]]
    ]
   [:div
    [:h1 "Pokemon Counts by Type"]
    [:div [:select {:value @selected-type
              :on-change #(reset! selected-type (-> % .-target .-value))
              :style {:marginBottom "10px"}}
           (for [t @type-options] ^{:key t} [:option {:value t} t])]

     [:button {:on-click #(go (let [result (<! (fetch-type-count @selected-type))]
                                (reset! type-results (edn/read-string result))))}
                                "Query by Single Type"]]
    [:div
     [oz/vega-lite (type-bar-chart-spec @type-results)]]
   ]])

(defn init []
  ;; Fetch data when the UI initializes.
  (go (let [data (<! (fetch-type-list))
            types (edn/read-string data)]
        (reset! type-options types)
        ;; If the currently selected type is not in the new list, update it.
        (when-not (some #(= @selected-type %) types)
          (reset! selected-type (first types)))))

  (go (let [data (<! (fetch-all-type-counts))]
        (reset! app-state (edn/read-string data))))
  (dom/render [main-ui]
              (.getElementById js/document "app")))