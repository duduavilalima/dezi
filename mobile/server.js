var fs = require('fs');
var path = require('path');
var express = require('express');
var compression = require('compression');

const SUBDOMAIN_SUFFIX = 'mobile';
const SUBDOMAIN_SUFFIX_REGEX = new RegExp(`${SUBDOMAIN_SUFFIX}$`, 'i');
const HOST_SUFFIX_REGEX = new RegExp(`^([^.]+)${SUBDOMAIN_SUFFIX}\..+$`, 'i');

const ENV = process.env.DEZI_MOBILE_ENV || process.env.DEZI_ENV || process.env.ENVIRONMENT_NAME || 'production';
const PORT = process.env.PORT || 3000;

let ENV_MODE = process.env.DEZI_MOBILE_MODE || process.env.DEZI_MODE || process.env.ENVIRONMENT;

if (!ENV_MODE) {
	ENV_MODE = ENV === 'production' ? 'production' : 'development';
}

const makeName = (descr) => `DEZI Mobile App - ${descr}`;
const makeShortName = (descr) => `DEZI M. ${descr}`;

const manifestsPerHost = {};
const baseManifest = {};

const createBaseProductionManifest = (host) => {
	if (ENV === "production" && HOST_SUFFIX_REGEX.test(host)) {
		const hostParts = host.split('.');
		const clientSubdomain = hostParts[0];
		const clientIdentifier = clientSubdomain.replace(SUBDOMAIN_SUFFIX_REGEX, '').toUpperCase();

		return {
			name: makeName(`Empresa ${clientIdentifier}`),
			short_name: makeShortName(clientIdentifier),
		}
	}

	return {};
};

switch (ENV) {
case "staging":
	baseManifest.name = makeName("Ambiente Homologação");
	baseManifest.short_name = makeShortName("HOMO");
	break;

case "staging-hotfix":
	baseManifest.name = makeName("Ambiente Hotfix");
	baseManifest.short_name = makeShortName("HOTFIX");
	break;

case "demo":
	baseManifest.name = makeName("Ambiente Demonstração");
	baseManifest.short_name = makeShortName("DEMO");
	break;

case "development":
case "production":
	break;

default:
	console.error(`Unknown execution environment "${ENV}".`);
	process.exit(2);
}

let distDir = path.resolve("dist", ENV);

if (!fs.existsSync(distDir)) {
	distDir = path.resolve("www");
}

if (!fs.existsSync(distDir) || !fs.lstatSync(distDir).isDirectory()) {
	console.error(`Invalid environment name "${ENV}"! Distribution folder "${distDir}" does not exist or is not a directory!`);
	process.exit(1);
}

function loadEnvManifest(req) {
	const host = req.header('Host');
	let manifest = manifestsPerHost[host];

	if (!manifest) {
		const realManifestPath = path.resolve(distDir, 'manifest.json');
		const realManifest = require(realManifestPath);
		const clientManifest = createBaseProductionManifest(host);

		manifest = {
			...realManifest,
			...baseManifest,
			...clientManifest,
		};

		manifestsPerHost[host] = manifest;
	}

	return manifest;
}

var app = express();

console.log('Environment: ' + ENV);
console.log('Execution mode: ' + ENV_MODE);

if (ENV_MODE === 'production') {
	console.log('Enabling server compression');
	app.use(compression());
}

app.use('/env', function(req, res, next) {
	req.url = req.url.replace(/\$env/, ENV);
	next();
});

app.route('/manifest.json').get(function(req, res) {
	res.json(loadEnvManifest(req));
	res.end();
});

app.use('/', express.static(distDir));

const server = app.listen(PORT, function () {
	console.log('Server listening on port ' + PORT);
});

process.on('SIGTERM', shutDown);
process.on('SIGINT', shutDown);

function shutDown() {
	console.log('Terminating web server...');

    server.close(() => {
        console.log('Web server terminated.');
    });
}
