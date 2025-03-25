(ns clj.pokedex.pokemon
  (:require
    [clj.pokedex.db :refer [datomic-conn start-pokedex-database]]
    [datomic.api :as d])
  (:gen-class))

(defn count-pokemon-by-type
  ([type]
   (d/q '[:find (count ?p-name) .
          :where [?t :pokemon-type/name ?t-name]
          [?p :pokemon/type ?t]
          [?p :pokemon/name ?p-name]
          :in $ ?t-name]
        (d/db @datomic-conn) type)
   )
  ([type gen]
   (d/q '[:find (count ?p-name) .
          :where [?t :pokemon-type/name ?t-name]
          [?p :pokemon/type ?t]
          [?p :pokemon/name ?p-name]
          [?p :pokemon/gen ?gen]
          :in $ ?t-name ?gen]
        (d/db @datomic-conn) type gen)
   )
  )

(defn get-pokemon-by-name [name]
  (d/q '[:find (pull ?p [*])
         :where [?p :pokemon/name ?p-name]
         :in $ ?p-name]
       (d/db @datomic-conn) name)
  )

(defn get-generation-list []
  (d/q '[:find (distinct ?gen) .
         :where [_ :pokemon/gen ?gen]]
       (d/db @datomic-conn))
  )

(defn get-type-list []
  (d/q '[:find (distinct ?t-name) .
         :where [?t :pokemon-type/name ?t-name]
         [_ :pokemon/type ?t]]
       (d/db @datomic-conn))
  )

;; Dummy function that returns sample data until proper calling and mapping of
;; `count-pokemon-by-type` is implemented.
(defn get-all-type-counts []
  [{:gen 1 :type "Normal" :count 22}
   {:gen 1 :type "Fire" :count 13}
   {:gen 1 :type "Water" :count 32}

   {:gen 2 :type "Normal" :count 15}
   {:gen 2 :type "Fire" :count 10}
   {:gen 2 :type "Water" :count 18}

   {:gen 3 :type "Normal" :count 18}
   {:gen 3 :type "Fire" :count 6}
   {:gen 3 :type "Water" :count 28}]
  )

(comment
  (start-pokedex-database)

  (count-pokemon-by-type "Water" 3)
  (get-all-type-counts)
  (get-generation-list)

  (get-pokemon-by-name "Charizard")

  ;; Pull and map how some outputs are displayed
  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/growth-rate [:pokemon-growth/name]
               :pokemon/egg-groups [:pokemon-egg-group/name]
               :pokemon/type [:pokemon-type/name]}]
          [:pokemon/unique-id 6.1])

  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/growth-rate [:pokemon-growth/name]
               :pokemon/egg-groups [:pokemon-egg-group/name]
               :pokemon/type [:pokemon-type/name]}]
          [:pokemon/unique-id 6.1])
  )