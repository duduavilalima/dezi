import angular from 'angular';

import './form-page.html';

export default angular.module('comps.formPage', [])
	.component('formPage', {
		templateUrl: 'form-page.html',
		controller: formPageController,
		transclude: true,
		bindings: {
			canCreate: '@?',
			onCreate: '&',
			onBack: '&'
		}
	})
	.name;

function formPageController($state) {
	const $ctrl = this;

	$ctrl.$onInit = () => {
		$ctrl.canCreate = $ctrl.canCreate == 'true';

		$ctrl.location = $state.current;
	};

	$ctrl.handleBack = () => {
		$ctrl.onBack();
	};
}
