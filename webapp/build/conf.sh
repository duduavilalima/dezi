#!/usr/bin/env bash


export PROJECT_GROUP="dezi"
export PROJECT_NAME="web"

export CI_PROJECT_NAMESPACE="${CI_PROJECT_NAMESPACE:-"avd"}"
export CI_PROJECT_NAME="${CI_PROJECT_NAME:-"webapp"}"

DEZI_DOCKER_GROUP="docker.fasolti.com.br"

NODE_IMAGE=${NODE_IMAGE:-"node:lts"}

DOCKER_CMD="docker run \
	--rm \
	--user ${UID} \
	-v "$(pwd)/..":/usr/src/dezi \
	-e HOME=/usr/src/dezi
	-w /usr/src/dezi/source
	${NODE_IMAGE}"
