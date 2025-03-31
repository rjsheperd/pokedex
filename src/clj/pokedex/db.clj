(ns pokedex.db
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.set :as set]
    [datomic.api :as d]
    [pokedex.schema :refer [schema]]))

;;; Connection

(defonce ^{:doc "Datomic Connection"} datomic-conn (atom nil))

;;; Helpers

(defn ->datomic-uri
  "Returns a Datomic Connection URI from a set of configuration parameters, which inclues:
  - `project`  [Required] - Project name within Datomic.
  - `dbtype`   [Optional, Default: 'postgresql'] - Type of DB for JDBC connection.
  - `host`     [Optional, Default: 'localhost'] - Host of JDBC connection.
  - `port`     [Optional, Default: 5432] - Port of JDBC connection.
  - `dbname`   [Optional, Default: 'datomic'] - Database name to connect to.
  - `user`     [Optional, Default: 'datomic'] - Username to the JDBC connection.
  - `password` [Optional, Default: 'datomic'] - Password to the JDBC connection."
  [{:keys [project dbtype host port dbname user password]
    :or   {dbtype   "postgresql"
           host     "localhost"
           port     5432
           dbname   "datomic"
           user     "datomic"
           password "datomic"}}]

  (format "datomic:sql://%s?jdbc:%s://%s:%d/%s?user=%s&password=%s"
          project
          dbtype
          host
          port
          dbname
          user
          password))

(defn connect! [datomic-uri]
  (d/create-database datomic-uri)
  (reset! datomic-conn (d/connect datomic-uri)))

(defn clear-database [datomic-uri]
  (d/delete-database datomic-uri))

;;; Pokedex Start Up

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
      (set/rename-keys {:number          :pokemon/national-pokedex-id
                        :detailed_number :pokemon/unique-id
                        :name            :pokemon/name
                        :form_name       :pokemon/form-name
                        :form_category   :pokemon/form-category
                        :category        :pokemon/category
                        :hp              :pokemon/base-hp
                        :attack          :pokemon/base-atk
                        :defense         :pokemon/base-def
                        :sp_atk          :pokemon/base-spatk
                        :sp_def          :pokemon/base-spdef
                        :speed           :pokemon/base-spd
                        :total           :pokemon/base-total
                        :gen             :pokemon/gen})

      (update :pokemon/national-pokedex-id parse-long)
      (update :pokemon/unique-id parse-double)
      (update :pokemon/base-hp parse-long)
      (update :pokemon/base-atk parse-long)
      (update :pokemon/base-def parse-long)
      (update :pokemon/base-spatk parse-long)
      (update :pokemon/base-spdef parse-long)
      (update :pokemon/base-spd parse-long)
      (update :pokemon/base-total parse-long)
      (update :pokemon/gen parse-long)

      ;(assoc :pokemon/name
      ;       (cond
      ;         (or (nil? (:pokemon/form-category pokemon)) (clojure.string/blank? (:pokemon/form-category pokemon))) (:name pokemon)
      ;         (or (= (:pokemon/form-category pokemon) "Mega") (= (:pokemon/form-category pokemon) "Regional")) (:form_name pokemon)
      ;         :else (str (:name pokemon) " (" (:form_name pokemon) ")")
      ;         )
      ;       )

      (assoc :pokemon/type
             (if (clojure.string/blank? (:type_2 pokemon))
               [[:pokemon-type/name (:type_1 pokemon)]]
               [[:pokemon-type/name (:type_1 pokemon)]
                [:pokemon-type/name (:type_2 pokemon)]]
               )
             )

      ;(assoc :pokemon/growth-rate [:pokemon-growth/name (:xp_growth pokemon)])

      (dissoc :name :form_name :type_1 :type_2 :gen :hgt_ft :hgt_m :wgt_lbs :wgt_kg :bmi :evolves_to :evo_family :stage)
      )
  )

(defn populate-pokemon-from-csv []
  (with-open [reader (io/reader (io/resource "PokemonDatabase.csv"))]
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
  (clear-database (->datomic-uri {:project "pokedex"}))
  (connect! (->datomic-uri {:project "pokedex"}))

  ;; Transact the schema
  (d/transact @datomic-conn schema)

  (init-basic-pokemon-fields)
  (populate-pokemon-from-csv)
  )
