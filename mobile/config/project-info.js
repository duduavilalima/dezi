#!/usr/bin/env node


const { resolve } = require("path");
const { prerelease } = require("semver");
const {
	ENV_CONFIG,
	ENV_ALIASES,
	ENV_DEFAULTS,
} = require("./project-info.config");
const {
	die,
	readPackageJson,
	getUTCTimestamp,
	spawnOutputSyncOr,
} = require("./utils");

const GitRefType = {
	branch: "branch",
	tag: "tag",
};

class Project {

	static load() {
		return new Project()._load();
	}

	constructor() {
		this.now = new Date();
		this.defaults = {};
		this.defaults.selectedEnvironmentNames = [ ...this._getDefaultEnvNames() ];
	}

	toEnvironmentName(envName) {
		if (!!envName) {
			if (envName in this.environments) return envName;
			const alias = this.environmentAliases[envName];
			if (alias) return alias;
		}
		return undefined;
	}

	ensureEnvironmentName(envName) {
		let realEnvName;

		realEnvName = this.toEnvironmentName(envName);
		if (realEnvName) return realEnvName;

		realEnvName = this.toEnvironmentName(this.environmentAliases[envName]);
		if (realEnvName) return realEnvName;

		die(`Ambiente de deploy '${envName}' desconhecido!`);
	}

	getEnvironment(envName) {
		const realEnvName = this.ensureEnvironmentName(envName);
		return this.environments[realEnvName];
	}

	getDefaultEnvironmentOr(envName) {
		const realEnvName = this._getDefaultBuildEnvNameOr(envName);
		const environment = this.environments[realEnvName];
		if (!environment) die(`Não há nenhum ambiente de execução com nome "${realEnvName}"!`);
		return environment;
	}

	createWebpackProcessEnv(environment, extraProcessEnv = {}) {
		const baseEnv = {
			DEZI_MOBILE_ENV: environment.name,
			ENVIRONMENT_NAME: environment.name,
			DEZI_ENV: environment.name,
		};
		const processEnv = environment.env instanceof Function
			? environment.env({ ...extraProcessEnv }, environment)
			: {
				...this.defaultProcessEnv,
				...(environment.env || {}),
				...extraProcessEnv,
				...baseEnv,
			};
		return processEnv;
	}

	createProcessEnv(envConfig) {
		const envVars = this.createWebpackProcessEnv(envConfig, process.env);
		for (let key in envVars) {
			if (envVars[key] === null || envVars[key] === undefined) {
				delete envVars[key];
			}
		}
		return envVars;
	}

	createAndroidContextFor(environment) {
		const { packageInfo, paths, timestamp, git } = this;
		const { projectRoot, buildsRoot } = paths;

		if (typeof environment === 'string') {
			environment = this.getEnvironment(environment);
		}

		const baseName = `${packageInfo.name}-v${packageInfo.version}`;
		const apkBaseName = `${baseName}-${environment.name}.BUILD-${timestamp}-${toSafeFolderName(git.ref)}@${git.commit}`;
		const buildDestRoot = `${buildsRoot}/${baseName}.BUILD-${timestamp}-${toSafeFolderName(git.ref)}@${git.commit}`;

		return {
			paths: {
				...this.paths,
				buildDestRoot,
				dstSignedApkPathName: `${buildDestRoot}/${apkBaseName}.signed.apk`,
				dstSigned8ApkPathName: `${buildDestRoot}/${apkBaseName}.signed-align8.apk`,
				dstApkPathName: `${buildDestRoot}/${apkBaseName}.unsigned.apk`,
				srcApkPathName: `${projectRoot}/platforms/android/app/build/outputs/apk/${environment.cordova.buildMode}/app-${environment.cordova.apkSuffix}.apk`,
			},
		};
	}

	_getDefaultBuildEnvNameOr(fallback) {
		const defaultEnvironmentName = this.defaults.environmentName;
		return this.toEnvironmentName(defaultEnvironmentName)
			|| this.toEnvironmentName(fallback);
	}

	_load() {
		const _loadProjectPaths = () => {
			const projectRoot = resolve(__dirname, "..");
			const buildsRoot = resolve(projectRoot,
				process.env.DEZI_MOBILE_BUILDS_DIR
					|| process.env.DEZI_BUILDS_DIR
					|| process.env.BUILDS_DIR
					|| "dist/apps"
			);

			Object.assign(this,{
				paths: {
					projectRoot,
					buildsRoot,
				},
			});
		};

		const _loadTimestamp = () => {
			const timestamp = getUTCTimestamp(this.now);
			Object.assign(this, { timestamp })
		};

		const _loadPackageJson = () => {
			const packageInfo = readPackageJson(this.paths.projectRoot);
			Object.assign(this, { packageInfo });
		};

		const _loadDebugFlag = () => {
			Object.assign(this, {
				debug:
					/^(true|yes|on|1)$/.test(process.env.DEBUG || "") ? true
					: /^(false|no|off|0)$/.test(process.env.DEBUG || "") ? false
					: false,
			});
		};

		const _loadDefaultProcessEnv = () => {
			const { packageInfo, now } = this;

			const defaultProcessEnv = {
				DEZI_MOBILE_NAME: packageInfo.name,
				DEZI_MOBILE_VERSION: packageInfo.version,
				DEZI_MOBILE_BUILD_DATE: now.toISOString(),
			};

			[
				{ name: 'DEZI_MOBILE_BUILD_TYPE' },
				{ name: "DEZI_MOBILE_BUILD_CI_ID", valuesFrom: ["CI_AUTOMATION_ID", "CI_JOB_ID"] },
				{ name: "DEZI_MOBILE_BUILD_CI_NAME", valuesFrom: ["CI_AUTOMATION_NAME", "CI_JOB_NAME"] },
				{ name: "DEZI_MOBILE_BUILD_CI_COMMIT", valuesFrom: ["CI_GIT_COMMIT_SHA", "CI_COMMIT_SHA"] },
				{ name: "DEZI_MOBILE_BUILD_CI_REF", valuesFrom: ["CI_GIT_REF", "CI_COMMIT_REF_NAME", "CI_COMMIT_TAG"] },
				{ name: "DEZI_MOBILE_BUILD_CI_REFTYPE", valuesFrom: [
					// Ionic AppFlow
					"CI_GIT_REF_TYPE",
					// Gitlab
					() => !!process.env["CI_COMMIT_TAG"] ? GitRefType.tag : !!process.env["CI_COMMIT_REF_NAME"] ? GitRefType.branch : "" ],
				},
				{ name: "DEZI_MOBILE_API_PROT" },
				{ name: "DEZI_MOBILE_API_HOST" },
				{ name: "DEZI_MOBILE_API_PORT" },
				{ name: "DEZI_MOBILE_API_CONTEXT" },
				{ name: "DEZI_MOBILE_API_BASEURL" },
				{ name: "DEZI_MOBILE_LOG_LEVEL" },
				{ name: "DEZI_MOBILE_LOG_APPENDERS" },
				{ name: "DEZI_MOBILE_MODE", valuesFrom: [ "DEZI_MOBILE_MODE", "DEZI_MODE", "ENVIRONMENT" ], assignAll: true },
			].forEach(envVar => {
				if (envVar.when && !envVar.when()) return;

				const name = envVar.name;
				const value = (envVar.valuesFrom || [envVar.name])
					.map(name => name instanceof Function
						? name()
						: process.env[name])
					.filter(value => !!value)[0];

				if (value) {
					defaultProcessEnv[name] = value;
				} else {
					defaultProcessEnv[name] = null;
				}

				if (envVar.assignAll && envVar.valuesFrom) {
					envVar.valuesFrom.forEach(name => {
						defaultProcessEnv[name] = value;
					});
				}
			});

			Object.assign(this, {
				defaultProcessEnv,
			});
		};

		const _loadGitInfo = () => {
			const { defaultProcessEnv } = this;
			const git = getGitInfo(defaultProcessEnv);
			Object.assign(this, { git });
		};

		const _complementProcessEnv = () => {
			const { git, defaultProcessEnv } = this;

			Object.assign(defaultProcessEnv, {
				DEZI_MOBILE_BUILD_COMMIT:
					git.commit
					|| null,
				DEZI_MOBILE_BUILD_BRANCH:
					git.branch
					|| null,
				DEZI_MOBILE_BUILD_TAG:
					git.tag
					|| null,
				DEZI_MOBILE_BUILD_REF:
					git.branch
					|| git.tag
					|| null,
				DEZI_MOBILE_BUILD_REFTYPE:
					(git.tag && GitRefType.tag)
					|| (git.branch && GitRefType.branch)
					|| null,
			});

			console.log("*** INFO: AMBIENTE PADRÃO: ", defaultProcessEnv);
			console.log("*** INFO: INFORMAÇÕES GIT: ", git);
		};

		const _loadEnvConfigs = () => {
			Object.assign(this, {
				environments: ENV_CONFIG,
				environmentNames: Object.keys(ENV_CONFIG),
				environmentAliases: ENV_ALIASES,
			});
		};

		const _loadDefaultEnvConfig = () => {
			let environmentName = undefined;
			let environment = undefined;

			const processEnvName = process.env.DEZI_MOBILE_ENV
				|| process.env.DEZI_ENV
				|| process.env.ENVIRONMENT_NAME
				|| process.env.NODE_ENV;

			if (processEnvName) {
				environmentName = this.ensureEnvironmentName(processEnvName);

			} else {
				const { git, environmentNames, environments } = this;
				const { ref, refType } = git;

				console.log("*** INFO: Determinando ambiente de execução a partir das informações do Git/CI/CD... ", git);

				if (refType === GitRefType.branch) {
					environmentName = environmentNames.filter(environmentName => {
						const environment = environments[environmentName];
						return environment.branch === ref;
					})[0];

					console.log("*** INFO: Nome do ambiente de execução (detectado via branch): ", environmentName);

				} else if (refType === GitRefType.tag) {
					environmentName = "production";

					console.log("*** INFO: Nome do ambiente de execução (detectado via tag): ", environmentName);

				} else {
					die("Não há informação suficiente para determinar o modo de build!");
				}
			}

			if (!environmentName) {
				environmentName = undefined;
			} else {
				this.ensureEnvironmentName(environmentName);
			}

			if (environmentName) {
				environment = this.environments[environmentName];
			}

			console.log("*** INFO: Ambiente de execução padrão detectado: ", environment);

			Object.assign(this, {
				defaults: {
					...this.defaults,
					environment,
					environmentName,
				}
			});
		}

		const _checkDefaultEnvNames = () => {
			this.defaults.selectedEnvironmentNames = this.defaults.selectedEnvironmentNames
				.filter(envName => !!envName)
				.map(envName => this.ensureEnvironmentName(envName));

			if (this.defaults.selectedEnvironmentNames.length === 0) {
				this.defaults.selectedEnvironmentNames = [ this.environmentName ];
			}
		}

		_loadProjectPaths();
		_loadTimestamp();
		_loadPackageJson();
		_loadDebugFlag();
		_loadDefaultProcessEnv();
		_loadGitInfo();
		_complementProcessEnv();
		_loadEnvConfigs();
		_loadDefaultEnvConfig();
		_checkDefaultEnvNames();

		return this;
	}

	_getDefaultEnvNames() {
		const defaultEnvNames = process.env.DEZI_MOBILE_ENV_DEFAULTS
			|| process.env.DEZI_ENV_DEFAULTS
			|| process.env.ENV_DEFAULTS;
		if (defaultEnvNames === "none") {
			return [];
		} else if (defaultEnvNames) {
			const envNames = defaultEnvNames.split(",").map(name => name.trim());
			return envNames;
		} else if (!!process.env.CI  || 'CI' in process.env || !!process.env.CI_APP_ID) {
			return [];
		}
		return ENV_DEFAULTS;
	}
}

function toSafeFolderName(name) {
	return name.replace(/[^a-zA-Z0-9_]/g, "_");
}

const getGitCommit = (data) => {
	const { env } = data;
	let commit = env.DEZI_MOBILE_BUILD_CI_COMMIT;
	if (commit) return { ...data, commit };
	commit = spawnOutputSyncOr("HEAD", "git", [ "rev-parse", "--short", "HEAD" ]).trim();
	return { ...data, commit };
};

const getGitBranch = (data) => {
	const expectedRefType = GitRefType.branch;
	const { env } = data;
	if (expectedRefType === env.DEZI_MOBILE_BUILD_CI_REFTYPE) {
		const ref = env.DEZI_MOBILE_BUILD_CI_REF;
		return { ...data, refType: expectedRefType, ref, branch: ref };
	} else if (GitRefType.tag !== env.DEZI_MOBILE_BUILD_CI_REFTYPE) {
		const ref = spawnOutputSyncOr("", "git", [ "symbolic-ref", "--short", "HEAD" ]).trim();
		return ref ? ({ ...data, ref, refType: expectedRefType, branch: ref }) : data;
	}
	return data;
};

const getGitTag = (data) => {
	const expectedRefType = GitRefType.tag;
	const { env } = data;
	const commit = data.commit || "HEAD";
	if (data.refType) return data;
	if (expectedRefType === env.DEZI_MOBILE_BUILD_CI_REFTYPE) {
		const ref = env.DEZI_MOBILE_BUILD_CI_REF;
		return { ...data, refType: expectedRefType, ref, tag: ref };
	} else if (GitRefType.branch !== env.DEZI_MOBILE_BUILD_CI_REFTYPE) {
		const ref = spawnOutputSyncOr("", "git", [ "tag", "-l", "--contains", commit ]).trim();
		return ref ? ({ ...data, ref, refType: expectedRefType, tag: ref }) : data;
	}
	return data;
};

const getGitInfo = (env = {}) => {
	let git = { env };
	git = getGitCommit(git);
	git = getGitTag(git);
	git = getGitBranch(git);
	return git;
};

function getProject() {
	return Project.load();
}

module.exports = {
	getProject,
};
