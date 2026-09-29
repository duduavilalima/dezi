#!/usr/bin/env bash

set -e

. ./conf.sh

export DEZI_CI=true

if [[ -z "${CI_JOB_ID}" ]]; then
	export DEZI_CI=false
	export CI_COMMIT_REF_NAME="${CI_COMMIT_REF_NAME:-"$(git symbolic-ref --short HEAD)"}"
	export CI_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA:-"$(git rev-parse --short HEAD)"}"
	export CI_COMMIT_SHA="${CI_COMMIT_SHA:-"$(git rev-parse HEAD)"}"

	export DEZI_PHASE="${DEZI_PHASE:-"staging"}"
fi

DEZI_PHASE="${DEZI_PHASE:?Faltando DEZI_PHASE.}"
CI_COMMIT_REF_NAME="${CI_COMMIT_REF_NAME:?Faltando CI_COMMIT_REF_NAME.}"
CI_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA:?Faltando CI_COMMIT_SHORT_SHA.}"
CI_COMMIT_SHA="${CI_COMMIT_SHA:?Faltando CI_COMMIT_SHA.}"
CI_PROJECT_NAMESPACE="${CI_PROJECT_NAMESPACE:?Faltando CI_PROJECT_NAMESPACE.}"
CI_PROJECT_NAME="${CI_PROJECT_NAME:?Faltando CI_PROJECT_NAME.}"

IMAGE_TAG="${DEZI_PHASE}-${CI_COMMIT_SHORT_SHA}"
IMAGE_TAGS="${DEZI_PHASE}"

# FIXME Atualizar o Gitlab para a versão 12.3 e migrar a lógica abaixo para o gitlab-ci.common.yml usando "rules:if"
if [[ ! -z "${CI_COMMIT_TAG}" ]]; then
	REF_TYPE="tag"
	IMAGE_TAGS="${IMAGE_TAGS};${DEZI_PHASE}-${CI_COMMIT_REF_NAME}"
else
	REF_TYPE="branch"
fi

export IMAGE_NAME="web"
export PROJECT_NAME="${IMAGE_NAME}"
export DEZI_IMAGE="${DEZI_DOCKER_GROUP}/${PROJECT_GROUP}-${PROJECT_NAME}"
export REF_TYPE

printf '*** INFO: Criando imagem "%s": estágio="%s", ref="%s", commit="%s", docker-tags="%s"\n' \
	"${DEZI_IMAGE}" \
	"${DEZI_PHASE}" \
	"${CI_COMMIT_REF_NAME}" \
	"${CI_COMMIT_SHORT_SHA}" \
	"${IMAGE_TAG};${IMAGE_TAGS}" \
	>&2

docker build ${CACHE_CMD} ${BUILD_FLAGS} --pull --quiet --no-cache \
	--tag "${DEZI_IMAGE}:${IMAGE_TAG}" \
	--build-arg DEZI_CI \
	--build-arg CI_PIPELINE_ID \
	--build-arg CI_PIPELINE_IID \
	--build-arg GITLAB_USER_LOGIN \
	--build-arg CI_PIPELINE_SOURCE \
	--build-arg PROJECT_NAME \
	--build-arg PROJECT_GROUP \
	--build-arg DEZI_PHASE \
	--build-arg CI_PROJECT_NAMESPACE \
	--build-arg CI_PROJECT_NAME \
	--build-arg REF_TYPE \
	--build-arg CI_COMMIT_REF_NAME \
	--build-arg CI_COMMIT_SHA \
	--build-arg CI_COMMIT_SHORT_SHA \
	.

if [ -z "${NOPUSH}" ]; then
	printf '*** INFO: Enviando imagem ao Nexus: %s\n' "${DEZI_IMAGE}:${IMAGE_TAG}" >&2
	docker push "${DEZI_IMAGE}:${IMAGE_TAG}"
fi

IFS=";"; for tag in ${IMAGE_TAGS}; do
	printf '*** INFO: Adicionando tag: %s\n' "${DEZI_IMAGE}:${tag}" >&2
	docker tag "${DEZI_IMAGE}:${IMAGE_TAG}" "${DEZI_IMAGE}:${tag}"
	if [ -z "${NOPUSH}" ]; then
		printf '*** INFO: Enviando imagem ao Nexus; tag: %s\n' "${DEZI_IMAGE}:${tag}" >&2
		docker push "${DEZI_IMAGE}:${tag}"
	fi
done
