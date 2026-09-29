#!/usr/bin/env bash

set -e

. ./conf.sh

# Compile using docker node
${DOCKER_CMD} bash -c "yarn && yarn build"

(	# Create an archive
	cd ..
	mkdir -p build/target
	tar --create --file build/target/dezi-web.txz --xz dist/ env/ server.js -C build package.json yarn.lock
)
