import angular from 'angular';

import { LabeledInputControllerBase } from './common';

import './labeled-select-boolean.html';

class LabeledSelectBooleanController extends LabeledInputControllerBase {
	static createConfiguration() {
		const baseConfig = LabeledInputControllerBase.createBaseConfig();

		return {
			...baseConfig,
			templateUrl: 'labeled-select-boolean.html',
			controller: LabeledSelectBooleanController,
		};
	}

	/* @ngInject */
	constructor($scope, $translate, $mdUtil) {
		super({ $scope, $translate, $mdUtil });
	}

	_initialize() {
		super._initialize();
	}
}

export default angular.module('comps.formInput.labeledSelectBoolean', [])
	.component('labeledSelectBoolean', LabeledSelectBooleanController.createConfiguration());
