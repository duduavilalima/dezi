
const createEnvConfig = (name, values) =>  ({ ...values, name, });

/**
 * Objeto contendo as configurações básicas de todos os ambientes de execução.
 */
const ENV_CONFIG = {
	production: createEnvConfig("production", {
		branch: "master",
		env: {},
		ionic: {
			flags: ["--prod"],
		},
		cordova: {
			buildMode: "release",
			apkSuffix: "release-unsigned",
			flags: ["--release", "--prod"],
		},
	}),
	demo: createEnvConfig("demo", {
		env: {},
		ionic: {
			flags: ["--prod"],
		},
		cordova: {
			buildMode: "release",
			apkSuffix: "release-unsigned",
			flags: ["--release"],
		},
	}),
	development: createEnvConfig("development", {
		env: {},
		ionic: {
			flags: ["--source-map"],
		},
		cordova: {
			buildMode: "debug",
			apkSuffix: "debug",
			flags: ["--debug", "--source-map"],
		},
	}),
	staging: createEnvConfig("staging", {
		branch: "development",
		env: {},
		ionic: {
			flags: ["--source-map"],
		},
		cordova: {
			buildMode: "debug",
			apkSuffix: "debug",
			flags: ["--debug", "--source-map"],
		},
	}),
	'staging-hotfix': createEnvConfig("staging-hotfix", {
		branch: "hotfix",
		env: {},
		ionic: {
			flags: ["--source-map"],
		},
		cordova: {
			buildMode: "debug",
			apkSuffix: "debug",
			flags: ["--debug", "--source-map"],
		},
	}),
};

/**
 * Objeto contendo nomes de ambientes de execução substitutos para nomes comumente usados.
 *
 * Use-o para dar nomes curtos, ou para manter compatibilidade com outras ferramentas,
 * se necessário.
 */
const ENV_ALIASES = {
	prod: "production",
	dev: "development",
};

/**
 * Lista contendo todos os ambientes de execução para os quais serão gerados APKs.
 *
 * O script de geração de APKs, `apk-build.js`, usará esta lista caso não receba uma lista
 * de ambientes na chamada ao script.
 */
const ENV_DEFAULTS = [ 'staging', 'staging-hotfix' ];

module.exports = {
	ENV_CONFIG,
	ENV_ALIASES,
	ENV_DEFAULTS,
};
