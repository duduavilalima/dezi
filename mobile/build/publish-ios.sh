#!/bin/bash

set -eo pipefail

USAGE="\
USO:
    $0 <IOS_APP_FILE> [IOS_API_KEY_ID] [IOS_ISSUER_ID]

	IOS_APP_FILE é obrigatório.

	IOS_API_KEY_ID e IOS_ISSUER_ID podem ser omitidos se houverem variáveis
	de ambiente não-vazias com os seus respectivos nomes no ambiente.

	A chave de API com o ID indicado em IOS_API_KEY_ID deve estar presente
	na pasta \"~/.appstoreconnect/private_keys\".
"

if [ "$#" -lt 0 ]; then
	printf 'ERRO: Faltando caminho para o arquivo IPA.\n%s' "$USAGE" >&2
	exit 1
fi
IOS_APP_FILE="${1}"; shift

if [ -z "$IOS_API_KEY_ID" ]; then
	if [ "$#" -lt 0 ]; then
		printf 'ERRO: Faltando ID da chave de API.\n%s' "$USAGE" >&2
		exit 1
	fi
	IOS_API_KEY_ID="${1}"; shift
fi

if [ -z "$IOS_ISSUER_ID" ]; then
	if [ "$#" -lt 0 ]; then
		printf 'ERRO: Faltando ID do Issuer.\n%s' "$USAGE" >&2
		exit 1
	fi
	IOS_ISSUER_ID="${1}"; shift
fi

exec xcrun altool \
	--upload-app \
	--type ios \
	--file "$IOS_APP_FILE" \
	--apiKey "$IOS_API_KEY_ID" \
	--apiIssuer "$IOS_ISSUER_ID"
