import angular from 'angular';

import './switch-dialog-input.html';

export default angular.module('comps.switchDialogInput', [])
	.component('switchDialogInput', {
		templateUrl: 'switch-dialog-input.html',
		controller: switchDialogInputController,
		bindings: {
			name: '@',
			label: '@?',
			situacao: '<?',
			value: '<',
			options: '<',
			onChange: '&'
		}
	})
	.name;

function switchDialogInputController() {
	const $ctrl = this;

	$ctrl.$onInit = () => {
		if (!$ctrl.name)
			throw new Error('Attribute name is required for form and its children');
			
		$ctrl.valueCopy = $ctrl.value;
		$ctrl.options = $ctrl.options || {};
	};

	$ctrl.handleChange = (newValue) => {
		$ctrl.onChange({ name: $ctrl.name, value: newValue});
	};

	$ctrl.reset = () => {
		if ($ctrl.value != $ctrl.valueCopy) {
			$ctrl.value = $ctrl.valueCopy;
			$ctrl.handleChange($ctrl.value);
		}
	};
}
