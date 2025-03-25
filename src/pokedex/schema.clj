(ns pokedex.schema)

(def schema
  [;; Pokemon
   {:db/ident       :pokemon/name
    :db/doc         "Pokemon's name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/form-name
    :db/doc         "Pokemon's name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/national-pokedex-id
    :db/doc         "Pokemon's National Pokedex Identifier"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/unique-id
    :db/doc         "Pokemon's Unique ID"
    :db/valueType   :db.type/double
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident       :pokemon/type
    :db/doc         "Pokemon's Type(s)"
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon/species
    :db/doc         "Pokemon's species"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/pokedex-entry
    :db/doc         "Pokemon's Pokedex entry"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/form-category
    :db/doc         "What category of form this Pokemon is."
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/category
    :db/doc         "What category of Pokemon this Pokemon is."
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/growth-rate
    :db/doc         "Pokemon's growth rate"
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/egg-groups
    :db/doc         "Pokemon's egg groups"
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon/base-hp
    :db/doc         "Pokemon's base hp stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-atk
    :db/doc         "Pokemon's base attack stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-def
    :db/doc         "Pokemon's base defense stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-spatk
    :db/doc         "Pokemon's base special attack stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-spdef
    :db/doc         "Pokemon's base special defense stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-spd
    :db/doc         "Pokemon's base speed stat"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/base-total
    :db/doc         "Pokemon's base stat total/sum"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/gen
    :db/doc         "Which generation the Pokemon is from."
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/evo-type
    :db/doc         "Pokemon Evolution label"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/evo-level
    :db/doc         "The level associated with the evolution"
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/evo-item
    :db/doc         "The item associated with the evolution"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident       :pokemon/evo-to
    :db/doc         "The Pokemon this Pokemon will evolve into."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}

   ;; Growth Rate
   {:db/ident       :pokemon-growth/name
    :db/doc         "Pokemon's growth rate name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   ;; Egg Group
   {:db/ident       :pokemon-egg-group/name
    :db/doc         "Pokemon's egg group name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   ;; Pokemon Types
   {:db/ident       :pokemon-type/name
    :db/doc         "Pokemon Type's name"
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   {:db/ident       :pokemon-type/super-effect-atk
    :db/doc         "Pokemon Types that this type is super-effective against."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/not-effect-atk
    :db/doc         "Pokemon Types that this type is not very effective against."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/no-effect-atk
    :db/doc         "Pokemon Types that take no damage from this type."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/super-effect-def
    :db/doc         "Pokemon Types that are super effective against this type."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/not-effect-def
    :db/doc         "Pokemon Types that are not very effective against this type."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident       :pokemon-type/no-effect-def
    :db/doc         "Pokemon Types that will do no damage to this type."
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many}])
