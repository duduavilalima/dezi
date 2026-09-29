var { resolve } = require('path');
var { mkdirpSync, copyFileSync, existsSync } = require('fs-extra');
var { removeSync, existsSync } = require('fs-extra');

const { die, spawnOutputSync } = require("./config/utils");
const { getProject } = require('./config/project-info');

function main() {
	const project = getProject();
	let build;

	_selectEnvironments();
	_deleteIonicBuildFolders();
	_executeBuild();

	function _selectEnvironments() {
		let environmentNames = process.argv;
		environmentNames.splice(0, 2);

		if (environmentNames.length === 0) {
			environmentNames = [ ...project.defaults.selectedEnvironmentNames ];
		}

		if (environmentNames.some(envName => !project.toEnvironmentName(envName))) {
			console.error("*** ERRO: Um ou mais nomes de ambiente desconhecidos.");
			process.exit(1);
		}

		console.log("*** APK-BUILD ***");
		console.log("*** Ambientes selecionados:", environmentNames);

		build = {
			project,
			environmentNames,
		};
	}

	function _deleteIonicBuildFolders() {
		const { project } = build;
		const projectRoot = project.paths.projectRoot;
		const ionicBuildFolders = [ 'www', 'platforms' ];

		console.log("*** Apagando pastas: ", ionicBuildFolders);

		ionicBuildFolders
			.map(folder => resolve(projectRoot, folder))
			.forEach(absoluteFolder => {
				removeSync(absoluteFolder);
			});

		return build;
	}

	function _executeBuild() {
		const { environmentNames, project } = build;

		environmentNames.forEach(environmentName  => {
			const environment = project.getEnvironment(environmentName);
			const processEnv = project.createProcessEnv(environment);

			const baseBuildContext = {
				...build,
				processEnv,
				environmentName,
				environment,
				platforms: {},
			};

			// Um contexto por plataforma
			for (let platform of ["android"]) {
				baseBuildContext.platforms[platform] = _createPlatformContext(baseBuildContext, platform);
			}

			// (likewise), Uma execução por plataforma
			Object.keys(baseBuildContext.platforms).forEach(platform => {
				const buildContext = {
					...baseBuildContext,
					platform,
				};

				announceBuild(buildContext);
				cordovaBuild({ ...buildContext, platform });
			});
		});

		console.log("*** BUILD FINALIZADA. ***");
	}

	function _createPlatformContext(build, platform) {
		const { project } = build;
		switch (platform) {
		case "android": return project.createAndroidContextFor(build.environment);
		}
		die(`Plataforma "${platform} desconhecida.`);
	}
}

function announceBuild(buildContext) {
	const { processEnv, project } = buildContext;

	console.log("*** CONSTRUINDO APP ***");
	console.log("*** Criando APK para o ambiente: ", buildContext.environmentName);

	if (project.debug) {
		console.log("*** Variáveis de ambiente (DEZI_*): ", Object.keys(processEnv)
			.filter(k => k.startsWith("DEZI_"))
			.reduce((accum, elem) => {
				accum[elem] = processEnv[elem];
				return accum;
			}, {}),
		);
	}
}

function cordovaBuild(buildContext) {
	const { platform } = buildContext;
	switch (platform) {
	case "android": return cordovaAndroidBuild(buildContext);
	}
	die(`Plataforma "${platform} desconhecida.`);
}

function cordovaAndroidBuild(buildContext) {
	const platform = "android";
	const { environment, processEnv, platforms } = buildContext;
	const { android } = platforms;
	const { projectRoot, buildsRoot, buildDestRoot } = android.paths;
	const apkSrc = android.paths.srcApkPathName;
	const apkDst = android.paths.dstApkPathName;
	const signedApkDst = android.paths.dstSignedApkPathName;
	const signed8ApkDst = android.paths.dstSigned8ApkPathName;

	_createFolders();
	_runCordova();
	_copyApks();
	_signApks();

	function _createFolders() {
		console.log("*** Criando diretório de destino para os APKs: ", buildDestRoot);
		if (!existsSync(buildsRoot)) mkdirpSync(buildsRoot);
		if (!existsSync(buildDestRoot)) mkdirpSync(buildDestRoot);
	};

	function _runCordova() {
		console.log("*** Executando build do Cordova...");
		spawnOutputSync("npx", ["ionic", "cordova", "build", platform, ...environment.cordova.flags], {
			stdio: 'inherit',
			env: processEnv,
			cwd: projectRoot,
		});
	};

	function _copyApks() {
		console.log("*** Copiando APKs: ", `"${apkSrc}" -> "${apkDst}"`);
		copyFileSync(apkSrc, apkDst);
		return buildContext;
	};

	function _signApks() {
		const keystoreFile = resolve(projectRoot, 'build', 'dezi_mobile.jks');
		const keystorePassword = process.env.DEZI_MOBILE_ANDROID_KEYSTORE_PASS
			|| process.env.DEZI_ANDROID_KEYSTORE_PASS
			|| process.env.ANDROID_KEYSTORE_PASS
			|| process.env.KEYSTORE_PASS
			|| '';
		const keystoreKey = process.env.DEZI_MOBILE_ANDROID_KEYSTORE_KEY
			|| process.env.DEZI_ANDROID_KEYSTORE_KEY
			|| process.env.ANDROID_KEYSTORE_KEY
			|| process.env.KEYSTORE_KEY
			|| 'dezi';
		const keyPassword = process.env.DEZI_MOBILE_ANDROID_KEYSTORE_KEYPASS
			|| process.env.DEZI_ANDROID_KEYSTORE_KEYPASS
			|| process.env.ANDROID_KEYSTORE_KEYPASS
			|| process.env.KEYSTORE_KEYPASS
			|| '';
		if (existsSync(keystoreFile)) {
			console.log("*** Assinando APKs: ", `"${signedApkDst}"`);
			const jarsignerOptions = [
				'-verbose',
				'-keystore', keystoreFile,
				...(keystorePassword ? ['-storepass', keystorePassword] : []),
				...(keyPassword ? ['-keypass', keyPassword] : []),
				'-sigalg', 'SHA1withRSA',
				'-digestalg', 'SHA1',
				apkSrc,
				keystoreKey,
			];
			spawnOutputSync("jarsigner", jarsignerOptions, {
				stdio: 'inherit',
				env: processEnv,
				cwd: projectRoot,
			});
			spawnOutputSync("zipalign", ['-v', '-f','4', apkSrc, signedApkDst], {
				stdio: 'inherit',
				env: processEnv,
				cwd: projectRoot,
			});
			spawnOutputSync("zipalign", ['-v', '-f','8', apkSrc, signed8ApkDst], {
				stdio: 'inherit',
				env: processEnv,
				cwd: projectRoot,
			});
			removeSync(apkSrc);
		}
	}
}

main();
