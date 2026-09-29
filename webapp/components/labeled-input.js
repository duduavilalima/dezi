import angular from 'angular';

import './labeled-input.html';

export default angular.module('comps.labeledInput', [])
	.component('labeledInput', {
		templateUrl: 'labeled-input.html',
		controller: labeledInputController,
		bindings: {
			name: '@',
			type: '@?',
			label: '@',
			value: '<',
			selectOptions: '<',
			fieldType: '@?',
			messages: '<',
			options: '<',
			onChange: '&'
		}
	})
	.name;

const DEFAULT_MESSAGES = {
	required: 'VALIDATION.REQUIRED',
	minlength: 'VALIDATION.MINLEN',
	maxlength: 'VALIDATION.MAXLEN'
};

// Essa função é necessária porque o $setPristine() não propaga para cima
function resetControlAndParents(control) {
	const children = control.$getControls && control.$getControls();

	const isPristine = !children || children.every(child => child.$pristine);

	if (isPristine) {
		control.$setPristine();
		control.$$parentForm && resetControlAndParents(control.$$parentForm);
	}
}

function labeledInputController($scope, $translate, $mdUtil) {
	const $ctrl = this;
	$ctrl.BOOLEAN_LIST = [{label: 'Sim', value: true}, {label:'Não', value: false}];
	let ready = true;

	$ctrl.$onInit = () => {
		if (!$ctrl.name)
			throw new Error('Attribute name is required for form and its children');

		$ctrl.valueCopy = $ctrl.value;

		$ctrl.messages = { ...DEFAULT_MESSAGES, ...$ctrl.messages };

		$ctrl.options = $ctrl.options || {};

		$ctrl.translateValues = {
			minlen: $ctrl.options.min,
			maxlen: $ctrl.options.max
		};

		$ctrl.type = $ctrl.type || 'input';
		$ctrl.fieldType = $ctrl.fieldType || 'text';

		if ($ctrl.options.translate) {
			const value = $ctrl.value;
			$ctrl.value = undefined; // Avoid showing ID on screen
			$translate(value)
				.then(value => $ctrl.value = value)
				.catch(err => $ctrl.value = err);
		}

		if ($ctrl.selectOptions) {
			// Default is key = 'id'
			if (!$ctrl.selectOptions.key && !$ctrl.selectOptions.value) {
				$ctrl.selectOptions.key = 'id';
			}

			if ($ctrl.selectOptions.key) {
				$ctrl.selectModelOptions = { trackBy: '$value.' + $ctrl.selectOptions.key };
			}

			if ($ctrl.options.disabled && $ctrl.selectOptions.label) {
				$ctrl.value = $ctrl.value[$ctrl.selectOptions.label];
			}

			// Resolvendo bug do select que emite um change qdo é montado
			// Issue https://github.com/angular/material/issues/9403
			ready = false;
			$mdUtil.nextTick(() => {
				resetControlAndParents($scope[$ctrl.name].input);
				ready = true;
			});
		}
	};

	$ctrl.handleChange = (newValue) => {
		ready && !$ctrl.options.disabled && $ctrl.onChange({ name: $ctrl.name, value: newValue });
	};

	$ctrl.showSelect = () => $ctrl.type === 'select' && !$ctrl.options.disabled;

	$ctrl.showSelectBoolean = () => $ctrl.type === 'select-boolean' && !$ctrl.options.disabled;

	$ctrl.selectOptionValue = (option) => {
		if($ctrl.type !== 'select-boolean') {
			return ($ctrl.selectOptions.value ? option[$ctrl.selectOptions.value] : option);
		} else 
			return option;
	};

	$ctrl.handleKeyup = ($event) => ($event.keyCode == 27) && $ctrl.reset();

	$ctrl.reset = () => {
		if ($ctrl.value != $ctrl.valueCopy) {
			$ctrl.value = $ctrl.valueCopy;
			$ctrl.handleChange($ctrl.value);
		}

		resetControlAndParents($scope[$ctrl.name].input);
	};

	$ctrl.formErrors = () => $ctrl.name && $scope[$ctrl.name].$error;
}
