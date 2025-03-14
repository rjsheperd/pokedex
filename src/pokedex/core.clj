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

  ;; Query the types
  (d/q '[:find ?t ?name
         :where [?t :pokemon-type/name ?name]]
       (d/db @datomic-conn))

  ;; Insert a Pokemon
  (d/transact @datomic-conn
              [{:db/id        -1
                :pokemon/name "Bulbasaur"
                :pokemon/national-pokedex-id 1
                :pokemon/type [[:pokemon-type/name "Grass"]
                               [:pokemon-type/name "Poison"]]
                :pokemon/species "Seed"
                :pokemon/pokedex-entry "For some time after its birth, it grows by taking nourishment from the seed on its back."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Grass"]
                                     [:pokemon-egg-group/name "Monster"]]
                :pokemon/base-hp 45
                :pokemon/base-atk 49
                :pokemon/base-def 49
                :pokemon/base-spatk 65
                :pokemon/base-spdef 65
                :pokemon/base-spd 45
                :pokemon/base-total 318
                :pokemon/evo-type "Level"
                :pokemon/evo-level 16
                :pokemon/evo-to -2}
               {:db/id        -2
                :pokemon/name "Ivysaur"
                :pokemon/national-pokedex-id 2
                :pokemon/type [[:pokemon-type/name "Grass"]
                               [:pokemon-type/name "Poison"]]
                :pokemon/species "Seed"
                :pokemon/pokedex-entry "When the bud on its back starts swelling, a sweet aroma wafts to indicate the flower’s coming bloom."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Grass"]
                                     [:pokemon-egg-group/name "Monster"]]
                :pokemon/base-hp 60
                :pokemon/base-atk 62
                :pokemon/base-def 63
                :pokemon/base-spatk 80
                :pokemon/base-spdef 80
                :pokemon/base-spd 60
                :pokemon/base-total 405}
               {:db/id        -3
                :pokemon/name "Charmander"
                :pokemon/national-pokedex-id 4
                :pokemon/type [[:pokemon-type/name "Fire"]]
                :pokemon/species "Lizard"
                :pokemon/pokedex-entry "The fire on the tip of its tail is a measure of its life. If the Pokémon is healthy, its tail burns intensely."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Dragon"]
                                     [:pokemon-egg-group/name "Monster"]]
                :pokemon/base-hp 39
                :pokemon/base-atk 52
                :pokemon/base-def 43
                :pokemon/base-spatk 60
                :pokemon/base-spdef 50
                :pokemon/base-spd 65
                :pokemon/base-total 309
                :pokemon/evo-type "Level"
                :pokemon/evo-level 16
                :pokemon/evo-to -4}
               {:db/id        -4
                :pokemon/name "Charmeleon"
                :pokemon/national-pokedex-id 5
                :pokemon/type [[:pokemon-type/name "Fire"]]
                :pokemon/species "Flame"
                :pokemon/pokedex-entry "In the rocky mountains where Charmeleon live, their fiery tails shine at night like stars."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Dragon"]
                                     [:pokemon-egg-group/name "Monster"]]
                :pokemon/base-hp 58
                :pokemon/base-atk 64
                :pokemon/base-def 58
                :pokemon/base-spatk 80
                :pokemon/base-spdef 65
                :pokemon/base-spd 80
                :pokemon/base-total 405}
               {:db/id        -5
                :pokemon/name "Squirtle"
                :pokemon/national-pokedex-id 7
                :pokemon/type [[:pokemon-type/name "Water"]]
                :pokemon/species "Tiny Turtle"
                :pokemon/pokedex-entry "It hides in its shell to protect itself, then strikes back with spouts of water at every opportunity."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Monster"]
                                     [:pokemon-egg-group/name "Water1"]]
                :pokemon/base-hp 44
                :pokemon/base-atk 48
                :pokemon/base-def 65
                :pokemon/base-spatk 50
                :pokemon/base-spdef 64
                :pokemon/base-spd 43
                :pokemon/base-total 314
                :pokemon/evo-type "Level"
                :pokemon/evo-level 16
                :pokemon/evo-to -6}
               {:db/id        -6
                :pokemon/name "Wartortle"
                :pokemon/national-pokedex-id 8
                :pokemon/type [[:pokemon-type/name "Water"]]
                :pokemon/species "Turtle"
                :pokemon/pokedex-entry "It is said to live 10,000 years. Its furry tail is popular as a symbol of longevity."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Slow"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Monster"]
                                     [:pokemon-egg-group/name "Water1"]]
                :pokemon/base-hp 59
                :pokemon/base-atk 63
                :pokemon/base-def 80
                :pokemon/base-spatk 65
                :pokemon/base-spdef 80
                :pokemon/base-spd 58
                :pokemon/base-total 405}
               {:db/id        -7
                :pokemon/name "Pikachu"
                :pokemon/national-pokedex-id 25
                :pokemon/type [[:pokemon-type/name "Electric"]]
                :pokemon/species "Mouse"
                :pokemon/pokedex-entry "Possesses cheek sacs in which it stores electricity. This clever forest-dweller roasts tough berries with an electric shock before consuming them."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Fast"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Fairy"]
                                     [:pokemon-egg-group/name "Field"]]
                :pokemon/base-hp 35
                :pokemon/base-atk 55
                :pokemon/base-def 40
                :pokemon/base-spatk 50
                :pokemon/base-spdef 50
                :pokemon/base-spd 90
                :pokemon/base-total 320
                :pokemon/evo-type "Stone"
                :pokemon/evo-item "Thunder Stone"
                :pokemon/evo-to -8}
               {:db/id        -8
                :pokemon/name "Raichu"
                :pokemon/national-pokedex-id 26
                :pokemon/type [[:pokemon-type/name "Electric"]]
                :pokemon/species "Mouse"
                :pokemon/pokedex-entry "It can loose 100,000-volt bursts of electricity, instantly downing foes several times its size."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Fast"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Fairy"]
                                     [:pokemon-egg-group/name "Field"]]
                :pokemon/base-hp 60
                :pokemon/base-atk 90
                :pokemon/base-def 55
                :pokemon/base-spatk 90
                :pokemon/base-spdef 80
                :pokemon/base-spd 110
                :pokemon/base-total 485}
               {:db/id        -9
                :pokemon/name "Eevee"
                :pokemon/national-pokedex-id 133
                :pokemon/type [[:pokemon-type/name "Normal"]]
                :pokemon/species "Evolution"
                :pokemon/pokedex-entry "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment."
                :pokemon/growth-rate [:pokemon-growth/name "Medium Fast"]
                :pokemon/egg-groups [[:pokemon-egg-group/name "Field"]]
                :pokemon/base-hp 55
                :pokemon/base-atk 55
                :pokemon/base-def 50
                :pokemon/base-spatk 45
                :pokemon/base-spdef 65
                :pokemon/base-spd 55
                :pokemon/base-total 325}])

  ;; Query for Pokemon
  (d/q '[:find [?p-name ...]
         :where [?p :pokemon/base-hp ?v]
         [?p :pokemon/name ?p-name]
         :in $ ?v]
       (d/db @datomic-conn) 60)

  ;; Join
  (def grass-pokemon
    (d/q '[:find [?p-name ...]
           :where [?t :pokemon-type/name ?t-name]
           [?p :pokemon/type ?t]
           [?p :pokemon/name ?p-name]
           :in $ ?t-name]
         (d/db @datomic-conn) "Grass"))

  (d/q '[:find [?p-name ...]
         :where [?set-name :pokemon-type/name ?t-name]
         [?t :pokemon-type/super-effect-atk ?set-name]
         [?p :pokemon/type ?t]
         [?p :pokemon/name ?p-name]
         :in $ [?t-name ...]]
       (d/db @datomic-conn) ["Ground" "Rock"])

  (d/q '[:find (min ?stat)
         :where [_ :pokemon/base-total ?stat]]
       (d/db @datomic-conn))

  ;; Pull
  (d/pull (d/db @datomic-conn)
          '[* {:pokemon/growth-rate [:pokemon-growth/name]
               :pokemon/evo-to [:pokemon/name]
               :pokemon/egg-groups [:pokemon-egg-group/name]
               :pokemon/type [:pokemon-type/name]}]
          [:pokemon/name "Bulbasaur"])

  )


(defn -main []
  (println "hello world"))
