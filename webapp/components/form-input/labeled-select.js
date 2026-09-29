import angular from 'angular';

import { LabeledInputControllerBase } from './common';

import './labeled-select.html';

const alwaysTrue = () => true;

class LabeledSelectController extends LabeledInputControllerBase {
	static createConfiguration() {
		const baseConfig = LabeledInputControllerBase.createBaseConfig();

		return {
			...baseConfig,
			templateUrl: 'labeled-select.html',
			controller: LabeledSelectController,
			bindings: {
				...baseConfig.bindings,
				list: '<?',
				searchable: '<?',
				searchableSize: '<?',
				orderBy: '<?',
				optionLabelKey: '<?',
				onSearchChange: '&?',
			},
		};
	}

	/* @ngInject */
	constructor($scope, $translate, $mdUtil) {
		super({ $scope, $translate, $mdUtil });

		this.searchTerm = '';
	}

	get list() {
		return this._list;
	}

	set list(value) {
		this._list = value;
		this._setSearchable();
		return value;
	}

	reset() {
		this._resetSearch();
		super.reset();
	}

	_initialize() {
		super._initialize();

		if (!this.optionLabelKey) {
			this.optionLabelKey = 'nome';
		}

		if (this.orderBy === undefined) {
			this.orderBy = this.optionLabelKey;
		} else if (this.orderBy === false) {
			this.orderBy = () => false;
		}

		if (this.searchable === undefined) {
			this.searchableSize = 5;
			this._setSearchable();
		}
	}

	_doFilter(searchTerm, option) {
		searchTerm = searchTerm || '';
		const value = option[this.optionLabelKey] || '';
		const valid = value.toLowerCase().includes(searchTerm.toLowerCase());
		return valid;
	}

	_createSearchFilter(searchTerm) {
		if (this.searchable) {
			return option => this._doFilter(searchTerm, option);
		}

		return alwaysTrue;
	}

	_onSearchEventPropagation(targetEvent) {
		if (targetEvent.keyCode !== 27) {
			targetEvent.stopPropagation();
		} else {
			this._resetSearch();
		}
	}

	_onFilterChange(targetEvent) {
		this.searchTerm = targetEvent.target.value;

		if (this.onSearchChange) {
			const controller = this;
			this.onSearchChange(this.searchTerm, { controller, targetEvent });
		}
	}

	_onCloseSelectSearch() {
		this._resetSearch();
	}

	_resetSearch() {
		this.searchTerm = '';
		angular.element('md-select-header input[name=searchTerm]').val(this.searchTerm);
	}

	_setSearchable() {
		if (this.searchableSize !== undefined) {
			this.searchable = this.list.length > this.searchableSize;
		}
	}
}

export default angular.module('comps.formInput.labeledSelect', ['ngMaterial'])
	.component('labeledSelect', LabeledSelectController.createConfiguration());
