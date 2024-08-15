#!/usr/bin/bash
#
# Script to run in the PostgreSQL container to initialize the data.
# Only runs when the data directory is empty, so adding to this file will have no effect on an already-initialized
# data server.
#
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER keycloak;
	CREATE DATABASE keycloak;
	COMMENT ON DATABASE keycloak IS 'Working data for KeyCloak.';
	GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL
