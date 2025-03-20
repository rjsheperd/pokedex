(ns pokedex.core
  (:require
   [pokedex.db :refer [connect! ->datomic-uri datomic-conn]]
   [pokedex.schema :refer [schema]]
   [datomic.api :as d]
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.set :as set])
  (:gen-class))

(defn init-basic-pokemon-fields []
  ;; Insert Pokemon Types
  (d/transact @datomic-conn
              [{:db/id             -1
                :pokemon-type/name "Normal"
                :pokemon-type/not-effect-atk [-13,-17]
                :pokemon-type/no-effect-atk [-14]
                :pokemon-type/super-effect-def [-7]
                :pokemon-type/no-effect-def [-14]}
               {:db/id             -2
                :pokemon-type/name "Fire"
                :pokemon-type/super-effect-atk [-5,-6,-12,-17]
                :pokemon-type/not-effect-atk [-2,-3,-13,-15]
                :pokemon-type/super-effect-def [-3,-9,-13]
                :pokemon-type/not-effect-def [-2,-5,-6,-12,-17,-18]}
               {:db/id             -3
                :pokemon-type/name "Water"
                :pokemon-type/super-effect-atk [-2,-9,-13]
                :pokemon-type/not-effect-atk [-3,-5,-15]
                :pokemon-type/super-effect-def [-4,-5]
                :pokemon-type/not-effect-def [-2,-3,-6,-17]}
               {:db/id             -4
                :pokemon-type/name "Electric"
                :pokemon-type/super-effect-atk [-3,-10]
                :pokemon-type/not-effect-atk [-4,-5,-15]
                :pokemon-type/no-effect-atk [-9]
                :pokemon-type/super-effect-def [-9]
                :pokemon-type/not-effect-def [-4,-10,-17]}
               {:db/id             -5
                :pokemon-type/name "Grass"
                :pokemon-type/super-effect-atk [-3,-9,-13]
                :pokemon-type/not-effect-atk [-2,-5,-8,-10,-12,-15,-17]
                :pokemon-type/super-effect-def [-2,-6,-8,-10,-12]
                :pokemon-type/not-effect-def [-3,-4,-5,-9]}
               {:db/id             -6
                :pokemon-type/name "Ice"}
               {:db/id             -7
                :pokemon-type/name "Fighting"}
               {:db/id             -8
                :pokemon-type/name "Poison"
                :pokemon-type/super-effect-atk [-5,-18]
                :pokemon-type/not-effect-atk [-8,-9,-13,-14]
                :pokemon-type/no-effect-atk [-17]
                :pokemon-type/super-effect-def [-9,-11]
                :pokemon-type/not-effect-def [-5,-7,-8,-12,-18]}
               {:db/id             -9
                :pokemon-type/name "Ground"}
               {:db/id             -10
                :pokemon-type/name "Flying"}
               {:db/id             -11
                :pokemon-type/name "Psychic"}
               {:db/id             -12
                :pokemon-type/name "Bug"}
               {:db/id             -13
                :pokemon-type/name "Rock"}
               {:db/id             -14
                :pokemon-type/name "Ghost"}
               {:db/id             -15
                :pokemon-type/name "Dragon"}
               {:db/id             -16
                :pokemon-type/name "Dark"}
               {:db/id             -17
                :pokemon-type/name "Steel"}
               {:db/id             -18
                :pokemon-type/name "Fairy"}
               ])

  ;; Insert Pokemon growth rates
  (d/transact @datomic-conn
              [{:pokemon-growth/name "Erratic"}
               {:pokemon-growth/name "Fast"}
               {:pokemon-growth/name "Medium Fast"}
               {:pokemon-growth/name "Medium Slow"}
               {:pokemon-growth/name "Slow"}
               {:pokemon-growth/name "Fluctuating"}
               ])

  ;; Insert Pokemon egg groups
  (d/transact @datomic-conn
              [{:pokemon-egg-group/name "Amorphous"}
               {:pokemon-egg-group/name "Bug"}
               {:pokemon-egg-group/name "Dragon"}
               {:pokemon-egg-group/name "Fairy"}
               {:pokemon-egg-group/name "Field"}
               {:pokemon-egg-group/name "Flying"}
               {:pokemon-egg-group/name "Grass"}
               {:pokemon-egg-group/name "Human-Like"}
               {:pokemon-egg-group/name "Mineral"}
               {:pokemon-egg-group/name "Monster"}
               {:pokemon-egg-group/name "Water1"}
               {:pokemon-egg-group/name "Water2"}
               {:pokemon-egg-group/name "Water3"}
               {:pokemon-egg-group/name "Ditto"}
               {:pokemon-egg-group/name "Undiscovered"}
               ])
  )

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))

(defn transform-pokemon [pokemon]
  (-> pokemon
      (set/rename-keys {:name :pokemon/name
                        :nat_id :pokemon/national-pokedex-id
                        :classification :pokemon/species
                        :hp :pokemon/base-hp
                        :atk :pokemon/base-atk
                        :def :pokemon/base-def
                        :sp_atk :pokemon/base-spatk
                        :sp_def :pokemon/base-spdef
                        :speed :pokemon/base-spd
                        :base_total :pokemon/base-total
                        :gen :pokemon/gen})

      (update :pokemon/national-pokedex-id parse-long)
      (update :pokemon/base-hp parse-long)
      (update :pokemon/base-atk parse-long)
      (update :pokemon/base-def parse-long)
      (update :pokemon/base-spatk parse-long)
      (update :pokemon/base-spdef parse-long)
      (update :pokemon/base-spd parse-long)
      (update :pokemon/base-total parse-long)
      (update :pokemon/gen parse-long)

      (assoc :pokemon/type
             (if (clojure.string/blank? (:type2 pokemon))
               [[:pokemon-type/name (:type1 pokemon)]]
               [[:pokemon-type/name (:type1 pokemon)]
                [:pokemon-type/name (:type2 pokemon)]]
               )
             )

      (assoc :pokemon/growth-rate [:pokemon-growth/name (:xp_growth pokemon)])

      (dissoc :type1 :type2 :xp_growth :base_happiness :gen :capture_rate :base_egg_steps)
      )
  )

(defn populate-pokemon-from-csv []
  (with-open [reader (io/reader (io/resource "Pokemon.csv"))]
    (->> reader
         (csv/read-csv)
         (doall)
         (csv-data->maps)
         (map transform-pokemon)
         (d/transact @datomic-conn)
         )
    ))

(defn start-pokedex-database []
  ;; Connect to Datomic
  (connect! (->datomic-uri {:project "pokedex"}))

  ;; Transact the schema
  (d/transact @datomic-conn schema)

  (init-basic-pokemon-fields)
  (populate-pokemon-from-csv)
  )

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
(defn get-pokemon-by-type-and-gen []
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

  (count-pokemon-by-type "Water 3")
  (get-pokemon-by-type-and-gen)

  ;; Pull and map how some outputs are displayed
  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/growth-rate [:pokemon-growth/name]
               :pokemon/evo-to [:pokemon/name]
               :pokemon/egg-groups [:pokemon-egg-group/name]
               :pokemon/type [:pokemon-type/name]}]
          [:pokemon/name "Bulbasaur"])
  )

(defn -main []
  (println "hello world"))
