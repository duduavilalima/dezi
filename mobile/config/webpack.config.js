const webpack = require("webpack");
const webpackConfigs = require("@ionic/app-scripts/config/webpack.config.js");

const { getProject } = require("./project-info");

module.exports = function() {
	const reviewWebpackConfigs = (project) => {
		for (const envName in webpackConfigs) {
			const webpackConfig = webpackConfigs[envName];
			const environment = project.getDefaultEnvironmentOr(envName);
			const processEnv = project.createWebpackProcessEnv(environment);
			const envPlugin = new webpack.EnvironmentPlugin(processEnv);
			webpackConfig.plugins = [envPlugin, ...(webpackConfig.plugins || [])];
		}
		return project;
	}

	const project = getProject();
	return reviewWebpackConfigs(project);
};
