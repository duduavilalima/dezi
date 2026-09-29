import angular from 'angular';

import { LabeledInputControllerBase } from './common';

import './labeled-datepicker.html';

class LabeledDatepickerController extends LabeledInputControllerBase {
	static createConfiguration() {
		const baseConfig = LabeledInputControllerBase.createBaseConfig();

		return {
			...baseConfig,
			templateUrl: 'labeled-datepicker.html',
			controller: LabeledDatepickerController,
			bindings: {
				...baseConfig.bindings,
				minDate: '<?',
				maxDate: '<?',
			},
		};
	}

	/* @ngInject */
	constructor($scope, $translate, $mdUtil) {
		super({ $scope, $translate, $mdUtil });

		this.translateOptionLabel = true;

		this._optionsPerValue = {};
	}

	_initialize() {
		if (!this.key) {
			this.key = 'value';
		}

		super._initialize();

		if (typeof this.minDate === 'string') {
			if (this.minDate === 'now') {
				this.minDate = new Date();
			}
		}

		if (typeof this.maxDate === 'string') {
			if (this.maxDate === 'now') {
				this.maxDate = new Date();
			}
		}
	}
}

export default angular.module('comps.formInput.labeledDatepicker', [])
	.component('labeledDatepicker', LabeledDatepickerController.createConfiguration());
