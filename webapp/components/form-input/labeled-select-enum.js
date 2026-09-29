import angular from 'angular';

import { LabeledInputControllerBase } from './common';

import './labeled-select-enum.html';

// NOTE Assume-se aqui que `$ctrl.value` será sempre uma string ou número.
// `$ctrl.value` NÃO DEVERÁ ser um objeto.
class LabeledSelectEnumController extends LabeledInputControllerBase {
	static createConfiguration() {
		const baseConfig = LabeledInputControllerBase.createBaseConfig();

		return {
			...baseConfig,
			templateUrl: 'labeled-select-enum.html',
			controller: LabeledSelectEnumController,
			bindings: {
				...baseConfig.bindings,
				list: '<?',
				enumInfo: '<?',
				optionLabelKey: '<?',
				optionValueKey: '<?',
			},
		};
	}

	/* @ngInject */
	constructor($scope, $translate, $mdUtil) {
		super({ $scope, $translate, $mdUtil });

		this._optionsPerValue = {};
	}

	_initialize() {
		if (!this.key) {
			this.key = 'value';
		}

		super._initialize();

		if (!this.optionLabelKey) {
			this.optionLabelKey = 'trLabel';
			this.translate = true;
		}

		if (!this.optionValueKey) {
			this.optionValueKey = this.key;
		}

		if (this.enumInfo) {
			this.list = this.enumInfo.elements;
			this._optionsPerValue = this.enumInfo.element;
		} else {
			if (!this.list) {
				throw new Error('Faltando opções.');
			}

			this._optionsPerValue = this.list
				.reduce((accum, option) => {
					accum[option.value] = option;
				}, {});
		}
	}
}

export default angular.module('comps.formInput.labeledSelectEnum', [])
	.component('labeledSelectEnum', LabeledSelectEnumController.createConfiguration());
