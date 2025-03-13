(ns pokedex.db
  (:require [datomic.api :as d]))

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
