#!/bin/sh

set -e

. "$(dirname "$0")/conf.sh"

(
	printf 'Entrando em "%s"...\n' "${PROJECT_ROOT}/build"
	cd "${PROJECT_ROOT}/build"
	DOCKER npm run ci:build:app "$@"
)
