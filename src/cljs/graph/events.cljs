(ns graph.events
  (:require [re-frame.core :as re-frame]
            [cljs.core.async :refer [<!]]
            [pokedex.client :as client]
            [clojure.edn :as edn])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; Initial default values
(re-frame/reg-event-db
  :graph/initialize-db
  (fn [_ _]
    {:all-type-counts []
     :selected-type "Normal"
     :type-options nil
     :single-type-results nil
     }))

(re-frame/reg-event-db
  :graph/set-type-options
  (fn [db [_ options]]
    (assoc db :type-options options)))

(re-frame/reg-event-db
  :graph/set-all-type-counts
  (fn [db [_ counts]]
    (assoc db :all-type-counts counts)))

(re-frame/reg-event-db
  :graph/set-selected-type
  (fn [db [_ new-type]]
    (assoc db :selected-type new-type)))

(re-frame/reg-event-db
  :graph/set-single-type-results
  (fn [db [_ results]]
    (assoc db :single-type-results results)))

(re-frame/reg-event-fx
  :graph/fetch-type-options
  (fn [{:keys [db]} _]
    (go (let [data (<! (client/fetch-type-list))]
          (when data
            (re-frame/dispatch [:graph/set-type-options data])

            (when-not (some #(= (:selected-type db) %) data)
              (re-frame/dispatch [:graph/set-selected-type (first data)])))))
    {}))

(re-frame/reg-event-fx
  :graph/fetch-all-type-counts
  (fn [{:keys [db]} _]
    (go (let [data (<! (client/fetch-all-type-counts))]
          (when data
            (re-frame/dispatch [:graph/set-all-type-counts data]))))
    {}))

(re-frame/reg-event-fx
  :graph/fetch-type-count
  (fn [{:keys [db]} _]
    (let [type (:selected-type db)]
      (go (let [data (<! (client/fetch-type-count type))]
            (when data
                (re-frame/dispatch [:graph/set-single-type-results data]))))
      {})))