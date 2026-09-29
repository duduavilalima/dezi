#!/bin/bash

NPMRC_PATH=${NPMRC_PATH:-~/.npmrc}

PYTHON="$(command -v python2.7 || command -v python2 || command -v python)" || {
	echo -e "ERROR: Não há executável python disponível no PATH!"
	exit 1
}

if [ ! -e "$NPMRC_PATH" ]; then
	touch "$NPMRC_PATH"
fi

if cat "$NPMRC_PATH" | grep -E "^\\s*python\\s*=.*" >/dev/null; then
	echo "INFO: Substituindo configuração NPM: python = $PYTHON"
	sed -i -E -e "s%^\\s*python\\s*=.*$%python = $PYTHON%g" "$NPMRC_PATH"
else
	echo "INFO: Adicionando configuração NPM: python = $PYTHON"
	printf "%s\n" "python = $PYTHON" >> "$NPMRC_PATH"
fi
