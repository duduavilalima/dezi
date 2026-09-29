#!/bin/bash

log() {
    local msg="$1"; shift
    printf "$msg\n" "$@" >&2
}

bye() {
    [ "$#" -gt 0 ] && log "$@"
    exit
}

die() {
    local code="$?";
    [ "$code" -eq 0 ] && code=1
    log "$@"
    exit "$code"
}

reverse() {
    local IFS
    local elem
    local input
    local output
    local sep
    input="$1"; shift
    IFS=":"; for elem in $input; do
        output="${elem}${sep}${output}"
        sep=":"
    done
    printf '%s\n' "$output"
}

compose_envfile_export() {
	local varname
	local dir
	local file
	local IFS
	unset IFS

	dir="${1:-$(pwd)}"
	file="${dir}/.env"

	# Exporta todas as variáveis do .env para poderem ser usadas nos scripts
	source <(sed 's/\([A-Z_]*\)=\(.*\)/\1="\2"/g' "${file}")
	for varname in $(grep -v -e "^#" -e '^\s*$' "${file}" | cut -d= -f1 | tr '\n' ' '); do
		export "$varname"
	done

	# Exporta o ID do usuário
	export USER_ID=$UID
}

sample_name() {
	local from
	local from_basename
	local to_basename

	from="${1}"; shift
	from_basename="$(basename "$from")"
	to_basename="$(basename "$from" .sample)"
	if [ -z "$from" ] || [ "$from_basename" = "$to_basename" ]; then
		log '*** ERROR: Arquivo "%s" não é um template!' "$from"
		return 1
	fi

	printf '%s\n' "$from"
}

sample_instance_name() {
	local from_dirname
	local from_basename
	local from="${1}"; shift
	from="$(sample_name "${from}")" || return $?
	from_dirname="$(dirname "$from")" || return $?
	from_basename="$(basename "$from" .sample)" || return $?
	printf '%s\n' "${from_dirname}/${from_basename}" || return "$?"
}

sample_backup_name() {
	local from
	local to

	from="${1}"; shift
	to="$(sample_instance_name "${from}")" || return $?
	printf '%s\n' "${to}.${NOW:?Faltando slug de timestamp.}.sample.backup"
}

sample_init() {
	local sample_file

	for sample_file in $(sample_find "$@"); do
		sample_initfile "$sample_file"
	done || return $?
}

sample_clean() {
	local sample_file
	local instance_file

	for sample_file in $(sample_rfind "$@"); do
		instance_file="$(sample_instance_name "$sample_file")"
		log '*** INFO: Apagando arquivo instanciado: "%s"!' "$instance_file"
		rm -f "$instance_file" || log '*** WARN: Não foi possível excluir arquivo instanciado "%s"!' "$instance_file"
	done || return $?
}

sample_initfile() {
	local from
	local to
	local from_file
	local from_pattern
	local from_order
	local envname
	local bkp
	local count
	local from_alternatives

	local envname="${PROJECT_ENVIRONMENT:?Faltando ENV-VAR PROJECT_ENVIRONMENT.}"
	from_order=(
		'env.'"$envname"'.pre*'
		'env.'"$envname"'.main*'
		'env.'"$envname"'.post*'
	)
	declare -A from_alternatives=(
		['env.'"$envname"'.pre*']="default.pre*"
		['env.'"$envname"'.main*']="default.main*"
		['env.'"$envname"'.post*']="default.post*"
	)

	from="$1"
	to="$(sample_instance_name "$from")" || return $?
	bkp="$(sample_backup_name "$from")" || return $?

	if [ -e "$to" ]; then
		if [ "$from" -nt "$to" ]; then
			log '*** INFO: Arquivo "%s" já existe, mas template "%s" é mais novo. Fazendo backup em "%s"...' "$to" "$from" "$bkp"
			log '*** WARN: Revise as configurações em "%s"!' "$to" # TODO COLOCAR EM VERMELHO
			cp -f "$to" "$bkp" || {
				log '*** ERROR: Não foi possível realizar o backup!'
				return 1
			}
		else
			log '*** INFO: Arquivo "%s" já existe.' "$to"
			return 0
		fi
	fi

	log '*** INFO: Criando arquivo "%s" a partir do template "%s"...' "$to" "$from"

	if [ -d "$from" ]; then
		{ rm -f "$to" && touch "$to"; } || die '*** ERROR: Não foi possível criar o arquivo "%s"!' "$to"

		for from_pattern in ${from_order[@]}; do
			log '*** DEBUG: >>> Padrão: "%s"' "$from_pattern"

			count="0"
			for from_file in $(find "$from" -mindepth 1 -maxdepth 1 -name "$from_pattern" -type f | sort); do
				count=$((count+1))
				log '*** INFO: >>> Padrão: "%s"; Copiando: "%s" -> "%s"' "$from_pattern" "$from" "$to"
				cat "$from_file" >> "$to" || {
					rm -f "$to" || true
					log '*** ERROR: Não foi possível concatenar o arquivo de template "%s" ao arquivo de destino "%s"!' "$from_file" "$to"
					return 1
				}
			done

			if [ "$count" -eq 0 ]; then
				from_pattern="${from_alternatives["$from_pattern"]}"
				log '*** DEBUG: >>> Padrão: "%s"' "$from_pattern"

				for from_file in $(find "${from}/" -mindepth 1 -maxdepth 1 -name "$from_pattern" -type f | sort); do
					log '*** INFO: >>> Copiando (fallback): "%s" -> "%s"' "$from" "$to"
					cat "$from_file" >> "$to" || {
						rm -f "$to" || true
						log '*** ERROR: Não foi possível concatenar o arquivo de template "%s" ao arquivo de destino "%s"!' "$from_file" "$to"
						return 1
					}
				done
			fi
		done
	else
		cp -f "$from" "$to" || {
			log '*** ERROR: Não foi possível substituir o arquivo "%s"!' "$to"
			return 1
		}
	fi

	if [ "$(basename "$to")" = ".env" ]; then
		printf 'ENVIRONMENT_NAME=%s\n' "$PROJECT_ENVIRONMENT" >> "$to"
	fi
}

sample_find() { _sample_find "f,d" "$@"; }
sample_rfind() { _sample_find "d,f" "$@"; }
_sample_find() {
	local sample_file
	local maxdepth
	local search_root
	local order
	local file_type

	order="${1:?Faltando ordem de tipos de arquivo.}"; shift
	search_root="${1:?Faltando raíz para a busca de arquivos de exemplo.}"; shift

	if [ "$#" -gt 0 ]; then
		maxdepth="-maxdepth $1"; shift
	fi

	IFS=","; for file_type in ${order}; do
		for sample_file in $(find "$search_root" -mindepth 1 $maxdepth -name '*.sample' -type "${file_type}"); do
			printf '%s\n' "$sample_file"
		done
	done
}

pgsql_decompress() {
	local file="${1}"; shift
	local xz_output_name="$(basename "$file" '.xz')"
	local file_type="$(file --brief --mime-type "$file")"

	if [ "$file_type" = "application/x-xz" ] && [ ! "${xz_output_name}" = "$file" ]; then
		output="$(dirname "$file")/${xz_output_name}"
		unxz -cd "$file" > "${output}"
		[ -e "$output" ] || die '*** ERROR: Não foi possível encontrar o arquivo descompactado "%s"!' "$xz_output_name"
		printf '%s\n' "$output"
	else
		die '*** ERROR: Tipo de arquivo de dump desconhecido.'
	fi
}

pgsql_compress() {
	local input="${1}"; shift
	local output="${1}"; shift
	{ cat "$input" | xz -2qc; } > "$output"
}

pgsql_restore() {
	local input="${1?:Faltando nome do arquivo de dump.}"; shift
	local dump_file

	(
		cd "${PROJECT_PWD}" || die
		[ -e "${SERVER_ROOT}/db-scripts/restore.sh" ] || die '*** ERROR: Falta script de restore.'
		dump_file="$(pgsql_decompress "${input}")" || die
		"${SERVER_ROOT}/db-scripts/restore.sh" "${dump_file}" || {
			rm -f "$dump_file"
			die
		}
		rm -f "$dump_file"
	) || die '*** ERROR: Não foi possível realizar o restore!'
}

pgsql_dump() {
	local output

	if [ "$#" -gt 0 ]; then
		output="${1}"; shift
	else
		output="./${SERVER_NAME}-${PROJECT_ENVIRONMENT}.${NOW}.nolog.dmp.xz"
	fi

	output="$(cd "${PROJECT_PWD}"; cd "$(dirname "$output")"; pwd)/$(basename "$output")"

	(
		set -e
		cd "${PROJECT_PWD}"
		[ -e "${SERVER_ROOT}/db-scripts/dump.sh" ] || die '*** ERROR: Falta script de dump.'
		"${SERVER_ROOT}/db-scripts/dump.sh" - | pgsql_compress - "$output"
		printf '%s\n' "$output"
	) || die '*** ERROR: Não foi possível realizar o dump!'
}

pgsql_migrate() {
	(
		cd "${PROJECT_PWD}" || die
		[ -e "${SERVER_ROOT}/db-scripts/migrate.sh" ] || die '*** ERROR: Falta script de migração.'
		"${SERVER_ROOT}/db-scripts/migrate.sh" "${dump_file}"
	) || die '*** ERROR: Não foi possível realizar a migração!'
}
