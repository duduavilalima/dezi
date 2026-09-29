import angular from 'angular';

export default angular.module('app.directive', [])
	.directive('pwCheck', pwCheckDirective)
	.directive('focusMe', focusMeDirective)
	.directive('autoTabTo', autoTabToDirective)
	.directive('myEnter', myEnterDirective)
	.directive('mdpDatePickerRequiredFix', mdpDatePickerRequiredFixDirective)
	.name;

function pwCheckDirective() {
	return {
		require: 'ngModel',
		link: function (scope, elem, attrs, ctrl) {
			var firstPassword = '#' + attrs.pwCheck;
			elem.add(firstPassword).on('keyup', function () {
				scope.$apply(function () {
					var v = elem.val() === $(firstPassword).val();
					ctrl.$setValidity('pwmatch', v);
				});
			});
		}
	};
}

function focusMeDirective($timeout, $parse) {
	return {
		//scope: true,   // optionally create a child scope
		link: function (scope, element, attrs) {
			var elementId = attrs.focusMeId;
			var model = $parse(attrs.focusMe);
			scope.$watch(model, function (value) {
				if (value === true) {
					$timeout(function () {
						if (elementId != null && elementId != '' && elementId !== '') {
							element.find('#' + elementId).focus();
						} else {
							element[0].focus();
						}
					});
				}
			});
		}
	};
}

function autoTabToDirective() {
	return {
		link: function (scope, element, attrs) {
			element.bind('keydown keypress', function () {
				var element = document.getElementById(attrs.autoTabTo);
				if (element)
					element[0].focus();
			});
		}
	};
}

function myEnterDirective() {
	return {
		link: function (scope, element, attrs) {
			element.bind('keydown keypress', function (event) {
				if (event.which === 13) {
					scope.$apply(function () {
						element[0].focus();
						scope.$eval(attrs.myEnter);
					});

					event.preventDefault();
				}
			});
		}
	};
}

function mdpDatePickerRequiredFixDirective() {
	return {
		link: function (scope, element, attrs, ngModel) {
			ngModel.$validators.required = function (modelValue) {
				return !(modelValue === undefined || modelValue == null || modelValue == '');
			};
		},
		require: 'ngModel',
		restrict: 'A'
	};
}
