#!/bin/sh

set -e

. "$(dirname "$0")/conf.sh"

printf 'Entrando em "%s"...\n' "${PROJECT_ROOT}/build"
cd "${PROJECT_ROOT}/build"
rm -rf "target/pwa"
mkdir -p "target/pwa"

printf '*** INSTALANDO DEPENDÊNCIAS\n' >&2
NODE_DOCKER bash -c "npm rebuild node-sass && npm i"

printf '*** EXECUTANDO TSLINT\n' >&2
NODE_DOCKER bash -c "npm run lint"

# Compilar o projeto
for environment_name in staging staging-hotfix demo production; do
	printf '*** COMPILANDO PWA PARA O AMBIENTE "%s" ***\n' "${environment_name}" >&2

	rm -rf ../www
	NODE_DOCKER bash -c "ENVIRONMENT_NAME='${environment_name}' npm run build:pwa -- --prod --generateSourceMap false --noLint"
	mkdir -p "target/pwa/dist"
	mv ../www "target/pwa/dist/${environment_name}"
done

# Criar TGZ para uso no build da imagem Docker
tar --create --file target/dezi-mobile.txz --xz \
	-C "${PROJECT_ROOT}" server.js \
	-C "${PROJECT_ROOT}/build" package.json package-lock.json \
	-C "${PROJECT_ROOT}/build/target/pwa" dist

# Cleanup, para não poluir demais o cache do Gitlab CI
rm -rf "target/pwa"
