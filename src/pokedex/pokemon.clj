(ns pokedex.pokemon
  (:require
    [pokedex.db :refer [datomic-conn start-pokedex-database]]
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

;; Dummy function that returns sample data until proper calling and mapping of
;; `count-pokemon-by-type` is implemented.
(defn get-all-type-counts []
  [[1 {"Normal" 22
       "Fire" 13
       "Water" 32}]
   [2 {"Normal" 15
       "Fire" 10
       "Water" 18}]
   [3 {"Normal" 18
       "Fire" 6
       "Water" 28}]]
  )

(comment
  (start-pokedex-database)

  (count-pokemon-by-type "Water" 3)
  (get-all-type-counts)

  ;; Pull and map how some outputs are displayed
  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/growth-rate [:pokemon-growth/name]
               :pokemon/evo-to [:pokemon/name]
               :pokemon/egg-groups [:pokemon-egg-group/name]
               :pokemon/type [:pokemon-type/name]}]
          [:pokemon/name "Bulbasaur"])
  )