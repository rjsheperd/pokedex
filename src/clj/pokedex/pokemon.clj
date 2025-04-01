(ns pokedex.pokemon
  (:require
    [pokedex.db :refer [datomic-conn start-pokedex-database]]
    [datomic.api :as d])
  (:gen-class))

(defn count-pokemon-by-type
  ([type]
   (let [count (d/q '[:find (count ?p-name) .
                      :where [?t :pokemon-type/name ?t-name]
                      [?p :pokemon/type ?t]
                      [?p :pokemon/name ?p-name]
                      :in $ ?t-name]
                    (d/db @datomic-conn) type)]
     {:Type type :Count count}
     )
   )
  ([type gen]
   (let [count (d/q '[:find (count ?p-name) .
                      :where [?t :pokemon-type/name ?t-name]
                      [?p :pokemon/type ?t]
                      [?p :pokemon/name ?p-name]
                      [?p :pokemon/gen ?gen]
                      :in $ ?t-name ?gen]
                    (d/db @datomic-conn) type gen)]
     {:Gen gen :Type type :Count count}
     )
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
  (let [gens  (get-generation-list)
        types (get-type-list)]
    (into []
          (for [gen gens
                type types]
            (count-pokemon-by-type type gen)))))

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