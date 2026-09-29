#!/usr/bin/env bash

set -e

. ./conf.sh

${DOCKER_CMD} mvn -P ${M2_PROFILES} -Duser.home=${USER_HOME} clean package -DskipTests
