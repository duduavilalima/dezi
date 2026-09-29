import { enableProdMode } from "@angular/core";

export type EnvName =
	| "development"
	| "production"
	| "demo"
	| "staging"
	| "staging-hotfix";

export type ExecutionModeName =
	| "development"
	| "production";

export type BuildTypeName =
	| "any"
	| "pwa";

export const envNames: EnvName[] = [
	"development",
	"production",
	"demo",
	"staging",
	"staging-hotfix",
];

export const buildTypeNames: BuildTypeName[] = [ "any", "pwa" ];
export const defaultBuildTypeName = buildTypeNames[0];

export const executionModeNames: ExecutionModeName[] = [
	"development",
	"production",
];

function makeBaseUrl(
	protocol: string,
	hostname: string,
	port: string | number | undefined,
	context: string | undefined,
) {
	const protPart = `${protocol}:`;
	const hostPart = `//${hostname}`;
	const portPart = !!port ? `:${port}` : "";
	const pathPart = context ? `/${context.replace(/^\/+/, '')}` : "";
	return `${protPart}${hostPart}${portPart}${pathPart}`;
}

export function getEnvNameOr(fallback: EnvName): EnvName {
	let nomeDetectado = fallback;
	const nomeAmbiente: any = process.env.DEZI_MOBILE_ENV
		|| process.env.DEZI_ENV
		|| process.env.ENVIRONMENT_NAME;

	if (nomeAmbiente && envNames.indexOf(nomeAmbiente) >= 0) {
		nomeDetectado = nomeAmbiente;
	}

	console.log(`Executando DEZI no ambiente "${nomeDetectado}".`);

	return nomeDetectado;
}

function getExecutionModeNameOr(fallback: ExecutionModeName): ExecutionModeName {
	const nome: any = process.env.DEZI_MOBILE_MODE
		|| process.env.DEZI_MODE
		|| process.env.ENVIRONMENT;

	if (nome && executionModeNames.indexOf(nome) >= 0) {
		return nome;
	}

	return fallback;
}

export function initializeExecutionMode(envName: EnvName) {
	const fallback = envName === 'production' ? 'production' : 'development'
	const executionModeName = getExecutionModeNameOr(fallback);

	if (executionModeName === 'production') {
		console.log("Ativando modo produção.");
		enableProdMode();
	}
}

function validateBuildTypeName(name: string): BuildTypeName {
	const realName = name as BuildTypeName;

	if (buildTypeNames.indexOf(realName) < 0) {
		throw new Error(`Invalid build type "${name}".`);
	}

	return realName
}

const envVarConfig = (() => {
	const version = process.env.DEZI_MOBILE_VERSION;
	const buildType = process.env.DEZI_MOBILE_BUILD_TYPE;
	const buildDate = process.env.DEZI_MOBILE_BUILD_DATE;
	const apiProtocol = process.env.DEZI_MOBILE_API_PROT;
	const apiHostname = process.env.DEZI_MOBILE_API_HOST;
	const apiPort = process.env.DEZI_MOBILE_API_PORT;
	const apiContext = process.env.DEZI_MOBILE_API_CONTEXT;
	const apiBaseUrl = process.env.DEZI_MOBILE_API_BASEURL;
	const gitCommit = process.env.DEZI_MOBILE_BUILD_COMMIT;
	const gitRef = process.env.DEZI_MOBILE_BUILD_TAG
		|| process.env.DEZI_MOBILE_BUILD_BRANCH
		|| process.env.DEZI_MOBILE_BUILD_REF
		|| process.env.DEZI_MOBILE_BUILD_CI_REF;

	const logLevel = process.env.DEZI_MOBILE_LOG_LEVEL;
	const logAppenders = process.env.DEZI_MOBILE_LOG_APPENDERS;

	const envVarConfig = {
		version,
		buildType,
		buildDate,
		apiProtocol,
		apiHostname,
		apiPort,
		apiContext,
		apiBaseUrl,
		logLevel,
		logAppenders,
		gitCommit,
		gitRef,
	};

	return envVarConfig;
})();

function makeConfig<K extends EnvName>(name: K, custom?: Partial<typeof envVarConfig>) {
	custom = custom || {};
	const version = envVarConfig.version || custom.version || "0.0.0";
	const buildType = validateBuildTypeName(envVarConfig.buildType || custom.buildType || defaultBuildTypeName);
	const buildDate = envVarConfig.buildDate || custom.buildDate || "";
	const gitCommit = envVarConfig.gitCommit || custom.gitCommit || "";
	const gitRef = envVarConfig.gitRef || custom.gitRef || "";
	const apiProtocol = envVarConfig.apiProtocol || custom.apiProtocol || "http";
	const apiHostname = envVarConfig.apiHostname || custom.apiHostname || "localhost";
	const apiPort = envVarConfig.apiPort || custom.apiPort;
	const apiContext = envVarConfig.apiContext || custom.apiContext;
	const logAppenders = (envVarConfig.logAppenders || custom.logAppenders || "console").split(",");
	const logLevel = parseInt(envVarConfig.logLevel || custom.logLevel || "0");

	const apiBaseUrl = envVarConfig.apiBaseUrl
		|| custom.apiBaseUrl
		|| makeBaseUrl(apiProtocol, apiHostname, apiPort, apiContext);

	const environment: typeof environmentBase = {
		...environmentBase,
		version,
		buildType,
		buildDate,
		gitCommit,
		gitRef,
		apiBaseUrl,
		logLevel,
		logAppenders,
	};

	return {
		...environment,
		name,
	};
}

export const environmentBase = {
	name: "",
	logAppenders: ["console"],
	production: false,
	apiBaseUrl: "",
	version: "0.0.0",
	buildType: defaultBuildTypeName,
	buildDate: "",
	gitCommit: "",
	gitRef: "",
	logLevel: 0,
	bypassLogin: false,
};

export const environments = {
	development: {
		...makeConfig("development", {
			apiPort: "8080",
			apiContext: "",
			logLevel: "-1", // DEBUG
		}),
		production: false,
	},
	staging: {
		...makeConfig("staging", {
			apiProtocol: "https",
			apiHostname: "homo.dezi.com.br",
			apiContext: "/rest",
		}),
		production: false,
	},
	'staging-hotfix': {
		...makeConfig("staging-hotfix", {
			apiProtocol: "https",
			apiHostname: "hotfix.dezi.com.br",
			apiContext: "/rest",
		}),
		production: false,
	},
	demo: {
		...makeConfig("demo", {
			apiProtocol: "https",
			apiHostname: "demo.dezi.com.br",
			apiContext: "/rest",
		}),
		production: false,
	},
	production: makeConfig("production", {
		apiProtocol: "https",
		apiHostname: "fasolti.dezi.com.br",
		apiContext: "/rest",
	}),
};
