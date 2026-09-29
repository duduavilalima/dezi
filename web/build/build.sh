#!/usr/bin/env bash

set -e

. ./conf.sh

${DOCKER_CMD} mvn ${MAVEN_CLI_OPTS} -P ${M2_PROFILES} -Duser.home=${USER_HOME} package \
	&& tar -czf target/dezi-db-migration.tgz -C ../domain/src/main/resources/db/migration .
