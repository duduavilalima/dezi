#!/bin/bash

SCRIPTS_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)"
PROJECT_ROOT="${PROJECT_ROOT:-"${SCRIPTS_DIR}/../../../"}"
PROJECT_SCRIPTS_ROOT="${PROJECT_SCRIPTS_ROOT:-"${PROJECT_ROOT}/scripts"}"
SERVER_ROOT="${SERVER_ROOT:-"${SCRIPTS_DIR}/.."}"

. "${PROJECT_SCRIPTS_ROOT}/utils.sh"

compose_envfile_export "${SERVER_ROOT}"

do_psql() {
    PGPASSWORD="${DB_ADMIN_PASS}" psql \
        --var="DB_USER=${DB_USER}" \
        --var="DB_PASS=${DB_PASS}" \
        --var="DB_NAME=${DB_NAME}" \
        --host="${DB_DUMP_HOST:-"${DB_HOST}"}" \
        --port="${DB_DUMP_PORT:-"${DB_PORT}"}" \
        --username="${DB_ADMIN_USER}" \
        --dbname "postgres" \
        "$@"
}

# Dear, PostgreSQL... Why no CREATE ROLE IF NOT EXISTS?!
{ create_user_script="$(cat)"; } <<-EOF
DO \$do\$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_roles
        WHERE  rolname = '${DB_USER}'
    ) THEN
        CREATE ROLE "${DB_USER}" LOGIN PASSWORD '${DB_PASS}';
    END IF;
END
\$do\$;
EOF

log '*** INFO: [db:recreate:user] Garantindo que o usuário "%s" exista...' "${DB_USER}"
printf '%s\n' "$create_user_script" | do_psql --file -

for file in $(find "${SCRIPTS_DIR}/recreate.sql.d" -type f -name '*.sql' | sort); do
    log '*** INFO: [db:recreate] Executando: "%s"' "$file"
    do_psql --file "$file" || die 'Falha ao executar arquivo SQL "%s"' "$file"
done
