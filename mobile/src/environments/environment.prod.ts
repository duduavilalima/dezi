import { environmentBase, environments, getEnvNameOr, initializeExecutionMode } from './environment.base';

export const ENV_NAME = getEnvNameOr("production");

export const environment: typeof environmentBase = {
	...environments[ENV_NAME],
};

initializeExecutionMode(ENV_NAME);
