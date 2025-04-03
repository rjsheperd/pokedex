(ns graph.view
  (:require [re-frame.core :as re-frame]
            [oz.core :as oz]
            [cljs.pprint :as pprint]))

;; Vega-Lite spec for the multi-bar chart (all generations).
(defn multi-bar-chart-spec [data]
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :data {:values data}
   :mark "bar"
   :encoding {:x {:field "Type"}
              :y {:field "Count" :type "quantitative"}
              :facet {:field "Gen" :type "ordinal" :columns 3}
              :color {:field "Type"
                      :scale {:range ["#AABB22" "#775544" "#7766EE" "#FFCC33" "#EE99EE" "#BB5544"
                                      "#FF4422" "#8899FF" "#6666BB" "#77CC55" "#DDBB55" "#66CCFF"
                                      "#AAAA99" "#AA5599" "#FF5599" "#BBAA66" "#AAAABB" "#3399FF"]
                              :domain ["Bug" "Dark" "Dragon" "Electric" "Fairy" "Fighting" "Fire"
                                       "Flying" "Ghost" "Grass" "Ground" "Ice" "Normal" "Poison"
                                       "Psychic" "Rock" "Steel" "Water"]}}}})

;; Vega-Lite spec for the single-type chart.
(defn type-bar-chart-spec [data]
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :data {:values data}
   :mark "bar"
   :encoding {:x {:field "Type"}
              :y {:field "Count" :type "quantitative"}
              :color {:field "Type"
                      :scale {:range ["#AABB22" "#775544" "#7766EE" "#FFCC33" "#EE99EE" "#BB5544"
                                      "#FF4422" "#8899FF" "#6666BB" "#77CC55" "#DDBB55" "#66CCFF"
                                      "#AAAA99" "#AA5599" "#FF5599" "#BBAA66" "#AAAABB" "#3399FF"]
                              :domain ["Bug" "Dark" "Dragon" "Electric" "Fairy" "Fighting" "Fire"
                                       "Flying" "Ghost" "Grass" "Ground" "Ice" "Normal" "Poison"
                                       "Psychic" "Rock" "Steel" "Water"]}}}})

(defn graph-view []
  (let [selected-type       (re-frame/subscribe [:graph/selected-type])
        type-options        (re-frame/subscribe [:graph/type-options])
        all-type-counts     (re-frame/subscribe [:graph/all-type-counts])
        single-type-results (re-frame/subscribe [:graph/single-type-results])]

        [:div {:style {:display "flex" :flexDirection "column"}}
         ;; Multi-bar chart section.
         [:div
          [:h1 "Pokemon Counts by Generation and Type"]
          [:button {:on-click #(re-frame/dispatch [:graph/fetch-all-type-counts])}
           "Refresh All Type Counts"]

          (if (seq @all-type-counts)
            [oz/vega-lite (multi-bar-chart-spec @all-type-counts)]
            [:div "Loading graph data..."]
            )
          ]

         ;; Single-type chart section.
         [:div
          [:h1 "Pokemon Counts by Type"]
          [:select {:value (or @selected-type "")
                    :on-change #(re-frame/dispatch [:graph/set-selected-type (-> % .-target .-value)])
                    :style {:marginBottom "10px"}}
           (if (seq @type-options)
             (for [t @type-options]
               ^{:key t} [:option {:value t} t])
             [:option {:value "Loading"} "Loading..."])]

          [:button {:on-click #(re-frame/dispatch [:graph/fetch-type-count])}
           "Query by Single Type"]

          (if (seq @single-type-results)
            [oz/vega-lite (type-bar-chart-spec @single-type-results)]
            [:div "Loading graph data..."]
            )
          ]]
        ))
