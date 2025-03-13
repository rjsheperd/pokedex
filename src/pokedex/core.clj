(ns pokedex.core
  (:require
   [pokedex.db :refer [connect! ->datomic-uri datomic-conn]]
   [pokedex.schema :refer [schema]]
   [datomic.api :as d])
  (:gen-class))

(comment
  ;; Connect to Datomic
  (connect! (->datomic-uri {:project "pokedex"}))

  ;; Transact the schema
  (d/transact @datomic-conn schema)

  ;; Insert a Pokemon Types
  (d/transact @datomic-conn
              [{:db/id             -1
                :pokemon-type/name "Grass"}
               {:db/id                     -2
                :pokemon-type/name         "Fire"
                :pokemon-type/super-effect [-1]}])

  ;; Query the types
  (d/q '[:find ?t ?name
         :where [?t :pokemon-type/name ?name]]
       (d/db @datomic-conn))

  ;; Insert a Pokemon
  (d/transact @datomic-conn [{:pokemon/name "Bulbasaur"}])
  (d/transact @datomic-conn [{:pokemon/name "Squirtle"}])
  (d/transact @datomic-conn [{:pokemon/name "Charmander"}])

  ;; Query for Pokemon
  (d/q '[:find ?t ?name
         :where [?t :pokemon/name ?name]]
       (d/db @datomic-conn))

  (def bulbasaur 
    (d/q '[:find ?t .
           :where [?t :pokemon/name ?name]
           :in $ ?name]
         (d/db @datomic-conn) "Bulbasaur"))

  (def grass
    (d/q '[:find ?t .
           :where [?t :pokemon-type/name ?name]
           :in $ ?name]
         (d/db @datomic-conn) "Grass"))

  ;; Update
  (d/transact @datomic-conn [{:db/id bulbasaur :pokemon/type grass}])

  ;; Join
  (def grass-pokemon
    (d/q '[:find ?p-name .
           :where [?t :pokemon-type/name ?t-name]
           [?p :pokemon/type ?t]
           [?p :pokemon/name ?p-name]
           :in $ ?t-name]
         (d/db @datomic-conn) "Grass"))

  ;; Pull
  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/type [:pokemon-type/name]}]
          [:pokemon/name "Bulbasaur"])

  )


(defn -main []
  (println "hello world"))
