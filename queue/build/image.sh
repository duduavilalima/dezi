#!/usr/bin/env bash

set -e

. ./conf.sh

# TODO Rever o uso do --cache-from
#CACHE_TAG=${1}
#
#if [[ "$CACHE_TAG" != "" ]]; then
#	CACHE_CMD="--cache-from ${DEZI_IMAGE}:${CACHE_TAG}"
#	# Tenta baixar a imagem para o cache mas não falha se não conseguir
#	docker pull ${DEZI_IMAGE}:${CACHE_TAG} || true
#fi

if [[ -z "${CI_JOB_ID}" ]]; then
	export DEZI_CI=false
	if [[ "$#" -gt 0 ]]; then
		IMAGE_TAG="${1}"; shift
	fi

	DEZI_PHASE="${DEZI_PHASE:-custom}"
	IMAGE_TAG="${IMAGE_TAG:-$DEZI_PHASE}"

	printf '*** INFO: Criando imagem customizada: docker-tag="%s"\n' \
		"${IMAGE_TAG}" \
		>&2

	docker build ${CACHE_CMD} ${BUILD_FLAGS} --pull \
		--label fasolti \
		--label fasolti.project \
		--label fasolti.project.stage="$DEZI_PHASE" \
		--tag "${DEZI_IMAGE}:${IMAGE_TAG}" \
		.

	printf '*** INFO: Enviando imagem ao Nexus: %s\n' "${DEZI_IMAGE}:${IMAGE_TAG}" >&2
	docker push "${DEZI_IMAGE}:${IMAGE_TAG}"

else
	export DEZI_CI=true
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

	printf '*** INFO: Criando imagem: estágio="%s", ref="%s", commit="%s", docker-tags="%s"\n' \
		"${DEZI_PHASE}" \
		"${CI_COMMIT_REF_NAME}" \
		"${CI_COMMIT_SHORT_SHA}" \
		"${IMAGE_TAG};${IMAGE_TAGS}" \
		>&2

	(
		export REF_TYPE
		cd "${CI_PROJECT_DIR}/build"
		docker build ${CACHE_CMD} ${BUILD_FLAGS} --pull \
			--tag "${DEZI_IMAGE}:${IMAGE_TAG}" \
			--build-arg DEZI_CI \
			--build-arg CI_PIPELINE_ID \
			--build-arg CI_PIPELINE_IID \
			--build-arg GITLAB_USER_LOGIN \
			--build-arg CI_PIPELINE_SOURCE \
			--build-arg DEZI_SERVICE_NAME \
			--build-arg DEZI_PHASE \
			--build-arg CI_PROJECT_NAMESPACE \
			--build-arg CI_PROJECT_NAME \
			--build-arg REF_TYPE \
			--build-arg CI_COMMIT_REF_NAME \
			--build-arg CI_COMMIT_SHA \
			--build-arg CI_COMMIT_SHORT_SHA \
			.
	)

	printf '*** INFO: Enviando imagem ao Nexus: %s\n' "${DEZI_IMAGE}:${IMAGE_TAG}" >&2
	docker push "${DEZI_IMAGE}:${IMAGE_TAG}"

	IFS=";"; for tag in ${IMAGE_TAGS}; do
		printf '*** INFO: Enviando imagem ao Nexus: %s\n' "${DEZI_IMAGE}:${tag}" >&2
		docker tag "${DEZI_IMAGE}:${IMAGE_TAG}" "${DEZI_IMAGE}:${tag}"
		docker push "${DEZI_IMAGE}:${tag}"
	done
fi
