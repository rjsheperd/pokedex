(ns graph.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :graph/all-type-counts
  (fn [db _]
    (:all-type-counts db)))

(re-frame/reg-sub
  :graph/selected-type
  (fn [db _]
    (:selected-type db)))

(re-frame/reg-sub
  :graph/type-options
  (fn [db _]
    (:type-options db)))

(re-frame/reg-sub
  :graph/single-type-results
  (fn [db _]
    (:single-type-results db)))