import angular from 'angular';

import { LabeledInputControllerBase } from './common';

import './labeled-text.html';

class LabeledTextController extends LabeledInputControllerBase {
	static createConfiguration() {
		const baseConfig = LabeledInputControllerBase.createBaseConfig();

		return {
			...baseConfig,
			templateUrl: 'labeled-text.html',
			controller: LabeledTextController,
			bindings: {
				...baseConfig.bindings,
				inputType: '@?',
				pattern: '<?',
			},
		};
	}

	/* @ngInject */
	constructor($scope, $translate, $mdUtil) {
		super({ $scope, $translate, $mdUtil });

		this.inputType = 'text';
		this.pattern = '';
	}

	_initialize() {
		super._initialize();
	}

	_validateInit() {
		if (!this.inputType) {
			throw new Error('Input type could not be identified');
		}
	}
}

export default angular.module('comps.formInput.labeledText', [])
	.component('labeledText', LabeledTextController.createConfiguration());
