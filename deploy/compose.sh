#!/bin/bash

export PROJECT_PWD="$(pwd)"

export PROJECT_ROOT="$(cd "$(dirname "${0}")"; pwd)"
export PROJECT_NAME="dezi"
export PROJECT_SERVERS_ROOT="${PROJECT_ROOT}/servers"
export PROJECT_SCRIPTS_ROOT="${PROJECT_ROOT}/scripts"
export PROJECT_CONFIGS_ROOT="${PROJECT_ROOT}/conf"
export PROJECT_CONFIGFILE="${PROJECT_ROOT}/.compose.conf"

declare PROJECT_SERVERS
declare -A PROJECT_ENVIRONMENT_SERVERS
declare -A PROJECT_SERVER_LOCATION
declare -A PROJECT_ENVIRONMENT_SERVER_OPTIONS

[ -e "$PROJECT_CONFIGFILE" ] && . "${PROJECT_CONFIGFILE}"
[ -z "$PROJECT_ENVIRONMENT" ] && PROJECT_ENVIRONMENT="none"
export PROJECT_ENVIRONMENT

. "${PROJECT_ROOT}/scripts/utils.sh"

declare -xr NOW_FORMAT="%Y-%m-%dT%H%M%S%z"
declare -xr NOW="$(date "+${NOW_FORMAT}")"

declare -Axr PROJECT_ENVIRONMENTS=(
    ["prod"]="production"
    ["production"]="production"
    ["demo"]="demo"
    ["staging"]="staging"
    ["staging-hotfix"]="staging-hotfix"
)

declare -Axr PROJECT_ENVIRONMENT_SERVERS=(
    ["production"]="${PROJECT_ENVIRONMENT_SERVERS["production"]:-"deziproxy:dezi"}"
    ["demo"]="${PROJECT_ENVIRONMENT_SERVERS["demo"]:-"deziproxy:dezi"}"
    ["staging"]="${PROJECT_ENVIRONMENT_SERVERS["staging"]:-"dezi"}"
    ["staging-hotfix"]="${PROJECT_ENVIRONMENT_SERVERS["staging-hotfix"]:-"dezi"}"
)

declare -Axr PROJECT_SERVER_LOCATION

declare -Axr PROJECT_ENVIRONMENT_SERVER_OPTIONS=(
    ["*:up"]="${PROJECT_ENVIRONMENT_SERVER_OPTIONS["*:up"]:-"-d"}"
)

conf_exec() {
    local command_name
    local command_function
    local conf_name
    local conf_dir
    local IFS
    unset IFS

    command_name="${1}"; shift
    conf_name="${1}"; shift
    conf_dir="${PROJECT_CONFIGS_ROOT}/${conf_name}"
    command_function="conf_do_${command_name}"

    (
        set -e
        cd "$conf_dir"
        export CONFIG_ROOT="$conf_dir"
        export CONFIG_NAME="$conf_name"

        log '*** INFO: Executando comando "%s" para a pasta de configuração "%s", opções: %s' "$command_name" "$CONFIG_NAME" "$*"

        if [ -f ./compose.rc ] && [ -x ./compose.rc ]; then
            ./compose.rc "$command_name" "$@"
        elif [ "$(type -t "${command_function}")" = "function" ]; then
            "$command_function" "$@"
        else
            case "$command_name" in
            setup) sample_init "$CONFIG_ROOT" ;;
            clean) sample_clean "$CONFIG_ROOT" ;;
            *) die '*** ERROR: Comando "%s" não implementado!' "$command_name" ;;
            esac
        fi
    ) || die
}

server_exec() {
    local command_name
    local server_name
    local server_dir
    local server_options
    local command_function
    local IFS
    unset IFS

    command_name="${1}"; shift
    server_name="${1}"; shift
    server_options="${PROJECT_ENVIRONMENT_SERVER_OPTIONS["${PROJECT_ENVIRONMENT}:${command_name}"]}"
    [ -z "$server_options" ] && server_options="${PROJECT_ENVIRONMENT_SERVER_OPTIONS["*:${command_name}"]}"
    command_function="server_do_${command_name}"

    server_dir="$( cd "${PROJECT_SERVERS_ROOT}"; cd "${PROJECT_SERVER_LOCATION["${server_name}"]:-"${server_name}"}"; pwd -P )"

    (
        set -e
        cd "$server_dir"
        export SERVER_ROOT="$server_dir"
        export SERVER_NAME="$server_name"
        export SERVER_OPTIONS="$server_options"

        log '*** INFO: Executando comando "%s" para o servidor "%s", opções: %s %s' "$command_name" "$SERVER_NAME" "$SERVER_OPTIONS" "$*"

        if [ -f ./compose.rc ] && [ -x ./compose.rc ]; then
            ./compose.rc "$command_name" "$@"
        elif [ "$(type -t "${command_function}")" = "function" ]; then
            "$command_function" "$@"
        else
            case "$command_name" in
            up|down|pull|logs|exec) echo docker-compose "$command_name" $SERVER_OPTIONS "$@"; docker-compose "$command_name" $SERVER_OPTIONS "$@" ;;
            db-dump|dump) pgsql_dump "$@" ;;
            db-restore|restore) pgsql_restore "$@" ;;
            db-migrate|migrate) pgsql_migrate "$@" ;;
            setup) sample_init "$SERVER_ROOT" ;;
            clean) sample_clean "$SERVER_ROOT" ;;
            *) die '*** ERROR: Comando "%s" não implementado!' "$command_name" ;;
            esac
        fi
    ) || die
}

server_list_percmd() {
    local servers
    local command_name
    local IFS
    unset IFS

    command_name="${1}"; shift

    servers="${PROJECT_SERVERS:-"${PROJECT_ENVIRONMENT_SERVERS["$PROJECT_ENVIRONMENT"]}"}"
    case "$command_name" in
    down|clean) servers="$(reverse "$servers")" ;;
    esac

    printf '%s\n' "$servers"
}

main() {
    local proceed
    local command_name
    local environment_name
    local servers
    local answer
    local server
    local service
    local config_id
    local IFS
    unset IFS

    command_name="${1:?Faltando comando.}"; shift
    servers="$(server_list_percmd "$command_name")"

    case "$command_name" in
    # ALIASES
    #
    destroy)
        read -n 1 -p 'Esta é uma operação DESTRUTIVA. Todos os dados serão apagados. Tem certeza? [y/N] ' answer
        echo
        if [ "$answer" = "y" ]; then
            exec "$0" down --volumes "$@"
        else
            die
        fi
        ;;

    redeploy)
        exec "$0" up --force-recreate --remove-orphans "$@"
        ;;

    server-bash)
        server="${1:?Faltando nome do servidor.}"; shift
        service="${1:?Faltando nome do serviço.}"; shift
        server_exec "exec" "$server" "$service" "bash" "$@" || die
        ;;

    # COMANDOS DE DATABASE
    #

    db-dump|dump)
        server_exec "$command_name" "dezi" "$@" || die
        ;;

    db-restore|restore)
        server_exec "$command_name" "dezi" "$@" || die
        ;;

    db-migrate|migrate)
        server_exec "$command_name" "dezi" "$@" || die
        ;;

    # COMANDOS
    #
    up|down|pull|logs)
        if [ ! -e "$PROJECT_CONFIGFILE" ]; then
            die '*** ERROR: Projeto ainda não está configurado!'
        fi

        IFS=":"; for server in $servers; do
            server_exec "$command_name" "$server" "$@" || die
        done

        bye
        ;;

    server-logs)
        server="${1:?Faltando nome do servidor.}"; shift
        server_exec "logs" "$server" "$@" || die
        ;;

    server-exec)
        server="${1:?Faltando nome do servidor.}"; shift
        server_exec "exec" "$server" "$@" || die
        ;;

    setup)
        if [ -e "$PROJECT_CONFIGFILE" ]; then
            bye '*** INFO: Projeto já está configurado para o ambiente "%s".' "$PROJECT_ENVIRONMENT"
        fi

        if [ "$#" -eq 0 ]; then
            die 'Faltando nome de ambiente.'
        else
            environment_name="${1}"; shift
            log '*** INFO: Configurando ambiente: "%s"' "$environment_name"
        fi

        if [ -z "${PROJECT_ENVIRONMENTS["$environment_name"]}" ]; then
            die '*** ERROR: Ambiente "%s" inválido.' "$environment_name"
        else
            PROJECT_ENVIRONMENT="${PROJECT_ENVIRONMENTS["$environment_name"]}"
        fi

        unset IFS; for config_id in $(cd "${PROJECT_CONFIGS_ROOT}"; find "." -mindepth 1 -maxdepth 1 -type d | sed "s|^\./||"); do
            conf_exec "$command_name" "$config_id" "$@"
        done

        servers="$(server_list_percmd "$command_name")"
        IFS=":"; for server in $servers; do
            server_exec "$command_name" "$server" "$@"
        done

        log '*** INFO: Criando arquivo de configuração: "%s"' "$PROJECT_CONFIGFILE"
        (
            set -e
            touch "$PROJECT_CONFIGFILE"
            printf '%s\n' "export PROJECT_ENVIRONMENT=\"$PROJECT_ENVIRONMENT\"" >> "$PROJECT_CONFIGFILE"
            printf '%s\n' "export PROJECT_HOST_IPS=\"$(hostname --ip-address)\"" >> "$PROJECT_CONFIGFILE"
            printf '%s\n' "export PROJECT_HOST_IP=\"$(hostname --ip-address | cut -d' ' -f1)\"" >> "$PROJECT_CONFIGFILE"
            printf '%s\n' "export PROJECT_SERVERS=\"${PROJECT_ENVIRONMENT_SERVERS["$PROJECT_ENVIRONMENT"]}\"" >> "$PROJECT_CONFIGFILE"
        ) || {
            rm -f "$PROJECT_CONFIGFILE"
            die '*** ERROR: Não foi possível configurar o ambiente!'
        }

        bye '*** INFO: Ambiente configurado com sucesso.'
        ;;

    clean)
        if [ ! -e "$PROJECT_CONFIGFILE" ]; then
            bye '*** INFO: Projeto não está configurado no momento.'
        fi

        proceed="0"

        IFS=":"; for server in $servers; do
            (
                cd "${PROJECT_SERVERS_ROOT}/${server}"
                if [ "$(docker-compose ps -q | wc -l)" -eq 0 ]; then
                    case "${1}" in
                    -f|--force) ;;
                    *) proceed="1"  ;;
                    esac
                fi
            )
        done

        if [ "$proceed" -ne 0 ]; then
            die '*** ERROR: Operação de limpeza abortada. Execute `"%s" down` e tente novamente.' "$0"
        fi

        unset IFS; for config_id in $(cd "${PROJECT_CONFIGS_ROOT}"; find "." -mindepth 1 -maxdepth 1 -type d | sed "s|^\./||"); do
            conf_exec "$command_name" "$config_id" "$@"
        done

        IFS=":"; for server in $servers; do
            server_exec "$command_name" "$server" "$@" || die
        done
        rm -f "$PROJECT_CONFIGFILE"
        ;;

    *)
        die '*** ERROR: Comando desconhecido: "%s".' "$command_name"
        ;;
    esac
}

main "$@"
