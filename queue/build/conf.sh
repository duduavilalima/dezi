#!/usr/bin/env bash

PROJECT_GROUP="dezi"
PROJECT_NAME="queue"

# Configuraçõe que podem ser alteradas pelo ambiente
DEZI_IMAGE=${DEZI_IMAGE:-"docker.fasolti.com.br/${PROJECT_GROUP}-${PROJECT_NAME}"}
M2_IMAGE=${M2_IMAGE:-"maven:3-jdk-11-slim"}
M2_PROFILES=${M2_PROFILES:-"build.docker"}

USER_HOME=/var/maven
M2_DIR=${USER_HOME}/.m2

DOCKER_CMD="docker run \
	--rm --name dezi-compiler \
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
