#!/bin/bash

PROJECT_PWD="$(pwd)"
SCRIPTS_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)"
PROJECT_ROOT="${PROJECT_ROOT:-"${SCRIPTS_DIR}/../../../"}"
PROJECT_SCRIPTS_ROOT="${PROJECT_SCRIPTS_ROOT:-"${PROJECT_ROOT}/scripts"}"
SERVER_ROOT="${SERVER_ROOT:-"${SCRIPTS_DIR}/.."}"

. "${PROJECT_SCRIPTS_ROOT}/utils.sh"

compose_envfile_export "${SERVER_ROOT}"

output_file="${1}"; shift

psql_dump() {
    PGPASSWORD="${DB_PASS}" pg_dump \
        --no-owner \
        --exclude-table-data="public.log_*" \
        --dbname="${DB_NAME}" \
        --host="${DB_DUMP_HOST:-"${DB_HOST}"}" \
        --port="${DB_DUMP_PORT:-"${DB_PORT}"}" \
        --username="${DB_USER}" \
        "$@"
}

if [ "$output_file" = "-" ]; then
    psql_dump
else
    psql_dump --file "${output_file}"
fi
