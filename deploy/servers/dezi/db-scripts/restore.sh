#!/bin/bash

SCRIPTS_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)"
PROJECT_ROOT="${PROJECT_ROOT:-"${SCRIPTS_DIR}/../../../"}"
PROJECT_SCRIPTS_ROOT="${PROJECT_SCRIPTS_ROOT:-"${PROJECT_ROOT}/scripts"}"
SERVER_ROOT="${SERVER_ROOT:-"${SCRIPTS_DIR}/.."}"

. "${PROJECT_SCRIPTS_ROOT}/utils.sh"
compose_envfile_export "${SERVER_ROOT}"

dmp_file="${1}"; shift

do_psql() {
    PGPASSWORD="${DB_PASS}" psql \
        --var="DB_USER=${DB_USER}" \
        --var="DB_PASS=${DB_PASS}" \
        --var="DB_NAME=${DB_NAME}" \
        --host="${DB_DUMP_HOST:-"${DB_HOST}"}" \
        --port="${DB_DUMP_PORT:-"${DB_PORT}"}" \
        --username="${DB_USER}" \
        --file "${1:?Faltando nome do arquivo SQL.}" \
        "${DB_NAME}"
}

# Before
"${SCRIPTS_DIR}/recreate.sh"

# Do
log '*** INFO: [db:restore] Executando: "%s"' "$file"
do_psql "${dmp_file}"

# After (migrate)
"${SCRIPTS_DIR}/migrate.sh"

# After
SQL_ROOT="${SCRIPTS_DIR}/after-restore.sql.d"
sql_roots=( "${SQL_ROOT}" )
if [ ! -z "${PROJECT_ENVIRONMENT}" ]; then
    env_root="${SQL_ROOT}/${PROJECT_ENVIRONMENT}"
    if [ -e "$env_root" ]; then
        sql_roots+=( "${env_root}" )
    fi
fi

for sql_root in ${sql_roots}; do
    for file in $(find -L "${sql_root}" "${env_root}" -mindepth 1 -maxdepth 1 -type f -name '*.sql' | sort | uniq); do
        log '*** INFO: [db:restore:after] Executando: "%s"' "$file"
        do_psql "$file" || {
            die 'Falha ao executar arquivo SQL "%s"' "$file"
        }
    done
done
