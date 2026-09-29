const { spawnSync } = require('child_process');
const { readFileSync } = require('fs-extra');

function spawnOutputSync(...args) {
	const procInfo = spawnSync(...args);
	if (procInfo.status === 0) {
		if (procInfo.stdout) return procInfo.stdout.toString();
		return; // Acontece apenas se { stdio: "inherit" }
	}
	throw new Error(`Failed to execute process for executable ${args[0]} with arguments "${args[1].join(" ")}"`);
}

function spawnOutputSyncOr(fallback, ...args) {
	try {
		return spawnOutputSync(...args);
	} catch (err) {
		return fallback;
	}
}

function die(message) {
	console.error(message);
	process.exit(1);
}

function readPackageJson(projectRoot) {
	const packageJsonName = `${projectRoot}/package.json`;
	const packageJson = readFileSync(packageJsonName);
	return JSON.parse(packageJson);
}

function padLeft(value, padding, qty) {
	const input = String(Math.abs(value));
	const sign = value < 0 ? "-" : ""
	const padSize = qty - input.length;
	if (padSize <= 0) return `${sign}${input}`;
	return `${sign}${padding.repeat(padSize)}${input}`;
}

function getUTCTimestamp(date) {
	return `${
		padLeft(date.getUTCFullYear(), "0", 4)
	}${
		padLeft(date.getUTCMonth() + 1, "0", 2)
	}${
		padLeft(date.getUTCDate(), "0", 2)
	}T${
		padLeft(date.getUTCHours(), "0", 2)
	}${
		padLeft(date.getUTCMinutes(), "0", 2)
	}${
		padLeft(date.getUTCSeconds(), "0", 2)
	}Z`;
}

module.exports = {
	die,
	readPackageJson,
	getUTCTimestamp,
	spawnOutputSync,
	spawnOutputSyncOr,
};
