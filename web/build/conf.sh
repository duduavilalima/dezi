#!/usr/bin/env bash

export PROJECT_GROUP="dezi"
export PROJECT_NAME="services"

export CI_PROJECT_NAMESPACE="${CI_PROJECT_NAMESPACE:-"avd"}"
export CI_PROJECT_NAME="${CI_PROJECT_NAME:-"web"}"

# Configuraçõe que podem ser alteradas pelo ambiente
DEZI_DOCKER_GROUP="docker.fasolti.com.br"
M2_IMAGE=${M2_IMAGE:-"maven:3-jdk-11-slim"}
MAVEN_CLI_OPTS=${MAVEN_CLI_OPTS:-"-q"}
M2_PROFILES=${M2_PROFILES:-"build.docker"}

USER_HOME=/var/maven
M2_DIR=${USER_HOME}/.m2

DOCKER_CMD="docker run \
	--rm \
	--user ${UID} \
	-e MAVEN_CONFIG=${M2_DIR} \
	-v "$(pwd)/..":/usr/src/dezi \
	-v "$(pwd)/m2":${M2_DIR} \
	-w /usr/src/dezi \
	${M2_IMAGE}"

# Garante que o diretório existe para que não seja criado pelo container como root
mkdir -p m2/repository

# Copia a configuração do Maven
cp -f settings.xml m2/
