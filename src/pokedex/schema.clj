(ns pokedex.schema)

(def schema
  [;; Pokemon
   {:db/ident       :pokemon/name
    :db/doc         "Pokemon's name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident       :pokemon/national-pokedex-id
    :db/doc         "Pokemon's National Pokedex Identifier"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident       :pokemon/type
    :db/doc         "Pokemon's Type(s)"
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   ;; Evolutions

   ;; Pokemon Types
   {:db/ident       :pokemon-type/name
    :db/doc         "Pokemon Type's name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident       :pokemon-type/not-effective
    :db/doc         "Pokemon Type's that are 'Not-Effective' (50% Damage)."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/no-effect
    :db/doc         "Pokemon Type's that not take damage."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/super-effect
    :db/doc         "Pokemon Type's that are Super Effective."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/immune
    :db/doc         "Pokemon Type's that will not do damage."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}])
