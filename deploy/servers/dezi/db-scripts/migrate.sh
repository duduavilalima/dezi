#!/bin/bash

SCRIPTS_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)"
PROJECT_ROOT="${PROJECT_ROOT:-"${SCRIPTS_DIR}/../../../"}"
PROJECT_SCRIPTS_ROOT="${PROJECT_SCRIPTS_ROOT:-"${PROJECT_ROOT}/scripts"}"
SERVER_ROOT="${SERVER_ROOT:-"${SCRIPTS_DIR}/.."}"

. "${PROJECT_SCRIPTS_ROOT}/utils.sh"
compose_envfile_export "${SERVER_ROOT}"

image_tag="${DEZI_DOCKER_TAG:-"${DEZI_VERSION_SERVICES}"}"
image_name="docker.fasolti.com.br/dezi-db-migration"
image="${image_name}:${image_tag}"
url="${DEZI_DB_URL:-"${DB_URL:-"jdbc:postgresql://${DB_DUMP_HOST}:${DB_DUMP_PORT}/${DB_NAME}"}"}"
username="${DEZI_DB_USER:-"${DB_USER}"}"
password="${DEZI_DB_PASS:-"${DB_PASS}"}"
db_link="${DEZI_DB_LINK:-"${DB_LINK}"}"
migrate_opts="${DEZI_DB_MIGRATE_OPTS:-"${DB_MIGRATE_OPTS}"}"

if ! docker search "$image_name" | tail -n +2 | cut -d' ' -f1 | grep -F "${image}" >/dev/null; then
    echo -e "*** ERROR: Imagem de migração '${image}' não existe."
    echo -e "    Talvez seja necessária intervenção manual para migrar o BD!"
    exit 1
else
    (
        set -e
        echo -e '*** Informações de migração de BD:' >&2
        docker run --rm ${db_link} "${image}" "-url=${url}" "-user=${username}" "-password=${password}" "-table=schema_version" ${migrate_opts} info
        echo -e '*** Efetuando migração de BD...' >&2
        docker run --rm ${db_link} "${image}" "-url=${url}" "-user=${username}" "-password=${password}" "-table=schema_version" ${migrate_opts} migrate
    ) || {
        echo -e "*** ERROR: Migração do BD usando a imagem '${image}' falhou!"
    }
fi
