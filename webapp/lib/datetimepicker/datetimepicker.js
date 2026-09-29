import angular from 'angular';
//import moment from 'moment';

export default angular.module('app.dataPicker', [])
	.directive('ttDatePicker', datepicker)
	// .directive('ttTimePicker', timepicker)
	// .directive('ttDateTimePicker', datetimepicker)
	.name;

function datepicker () {	
	return {
		require: 'ngModel',
		restrict: 'A',
		scope: {
			locale: '=',
			maxDate: '=',
			minDate: '=',
			ngModel: '='
		},
		controller: ($element,$scope) => {
			const local = window.navigator.userLanguage || window.navigator.language;
			$.datetimepicker.setLocale(local === 'pt'? 'pt-BR' : local);
			const opt = {
				timepicker:false,
				format:	'd/m/Y',
				formatDate:	'd/m/Y',
			};
			if (!angular.isUndefined($($element[0]).attr('now'))) {
				opt.minDate = new Date();
			}
			
			if (!angular.isUndefined($($element[0]).attr('max-date'))) {
				opt.maxDate = new Date();
			}

			$($element[0]).datetimepicker(opt);
			$scope.$watch('minDate', minimoDate => {
				if (minimoDate) {
					$($element[0]).datetimepicker({
						minDate: minimoDate,
					});
				}
			});
		},
		// link: (scope, element, attrs, ngModel) => {
		// 	scope.$watch(attrs.ngModel, newValue => {
		// 		if (angular.isDefined(newValue) || newValue != null) {
		// 			ngModel.$setViewValue(moment(element.val(), 'DD/MM/YYYY').toDate());
		// 		} else {
		// 			ngModel.$setViewValue('');
		// 		}
		// 	}, true);
		// 	ngModel.$render = () => {
		// 		if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
		// 			element.val(moment(ngModel.$modelValue).format('L'));
		// 		}
		// 	};
		// }
	};
}

// function timepicker () {	
// 	return {
// 		require: 'ngModel',
// 		restrict: 'A',
// 		scope: {
// 			localeCode: '=',
// 			maxDate: '=maxDate',
// 			minDate: '=minDate',
// 			ngModel: '='
// 		},
// 		controller: function ($element, $attrs, $scope) {
// 			$scope.$watch('localeCode', function (newValue) {
// 				if (angular.isDefined(newValue)) {
// 					var options = {};
// 					if (newValue) {
// 						options.lang = newValue;
// 					} else {
// 						options.lang = 'pt-BR';
// 					}

// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 			$scope.$watch('maxDate', function (newValue) {
// 				if (angular.isDefined(newValue) && angular.isDate(newValue)) {
// 					var options = {};
// 					if (angular.isDefined($scope.maxDate) && angular.isDate($scope.maxDate)) {
// 						options.maxDate = new Date($scope.maxDate);
// 					}

// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 			$scope.$watch('minDate', function (newValue) {
// 				if (angular.isDefined(newValue) && angular.isDate(newValue)) {
// 					var options = {};
// 					if (angular.isDefined($scope.minDate) && angular.isDate($scope.minDate)) {
// 						options.minDate = new Date($scope.minDate);
// 					}

// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 		},
// 		link: function (scope, element, attrs, ngModel) {
// 			scope.$watch(attrs.ngModel, function (newValue) {
// 				if (angular.isDefined(newValue) || newValue != null) {
// 					ngModel.$setViewValue(moment(element.val(), 'DD/MM/YYYY').toDate());
// 				} else {
// 					ngModel.$setViewValue('');
// 				}
// 			}, true);
// 			ngModel.$render = () => {
// 				if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
// 					element.val(moment(ngModel.$modelValue).format('DD/MM/YYYY'));
// 				}
// 			};

// 			setTimeout(() => {
// 				var options = {};

// 				if (angular.isDefined(scope.maxDate) && angular.isDate(scope.maxDate)) {
// 					options.maxDate = new Date(scope.maxDate);
// 				}

// 				if (angular.isDefined(scope.minDate) && angular.isDate(scope.minDate)) {
// 					options.minDate = new Date(scope.minDate);
// 				}

// 				if (scope.localeCode) {
// 					options.lang = scope.localeCode;
// 				} else {
// 					options.lang = 'pt-BR';
// 				}

// 				$(element[0]).datetimepicker({
// 					datepicker: false,
// 					scrollTime: false,
// 					onClose: () => {
// 						ngModel.$parsers.unshift(function (viewValue) {
// 							if (angular.isUndefined(viewValue) 
// 									|| viewValue === '' 
// 									|| viewValue === ' ' 
// 									|| viewValue === 'Invalid Date' 
// 									|| viewValue == null) {
// 								return null;
// 							} else {
// 								if (angular.isDate(viewValue)) {
// 									return viewValue;
// 								} else {
// 									return moment(viewValue, 'DD/MM/YYYY').toDate();
// 								}
// 							}
// 						});
// 					},
// 					mask: '29:59',
// 					format: 'H:i',
// 					onShow: () => {
// 						this.setOptions(options);
// 					},
// 					allowBlank: true
// 				}).keyup(function (e) {
// 					if (e.keyCode == 8 || e.keyCode == 46) {
// 						$(element[0]).val('');
// 					}
// 				});
// 			});
// 		}
// 	};
// }

// function datetimepicker () {
// 	return {
// 		require: 'ngModel',
// 		restrict: 'A',
// 		scope: {
// 			localeCode: '=',
// 			maxDate: '=maxDate',
// 			minDate: '=minDate',
// 			ngModel: '='
// 		},
// 		controller: function ($element, $scope) {
// 			$scope.$watch('localeCode', function (newValue) {
// 				if (angular.isDefined(newValue)) {
// 					var options = {};
// 					if (newValue) {
// 						options.lang = newValue;
// 					} else {
// 						options.lang = 'pt-BR';
// 					}

// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 			$scope.$watch('maxDate', function (newValue) {
// 				if (angular.isDefined(newValue) && angular.isDate(newValue)) {
// 					var options = {};
// 					if (angular.isDefined($scope.maxDate) && angular.isDate($scope.maxDate)) {
// 						options.maxDate = new Date($scope.maxDate);
// 					}
					
// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 			$scope.$watch('minDate', function (newValue) {
// 				if (angular.isDefined(newValue) && angular.isDate(newValue)) {
// 					var options = {};
// 					if (angular.isDefined($scope.minDate) && angular.isDate($scope.minDate)) {
// 						options.minDate = new Date($scope.minDate);
// 					}

// 					$($element[0]).datetimepicker({
// 						onShow: () => {
// 							this.setOptions(options);
// 						}
// 					});
// 				}
// 			});
// 		},
// 		link: function (scope, element, attrs, ngModel) {
// 			scope.$watch(attrs.ngModel, function (newValue) {
// 				if (angular.isDefined(newValue) || newValue != null) {
// 					ngModel.$setViewValue(moment(element.val(), 'DD/MM/YYYY').toDate());
// 				} else {
// 					ngModel.$setViewValue('');
// 				}
// 			}, true);
// 			ngModel.$render = () => {
// 				if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
// 					element.val(moment(ngModel.$modelValue).format('DD/MM/YYYY'));
// 				}
// 			};

// 			setTimeout(() => {
// 				var options = {};

// 				if (angular.isDefined(scope.maxDate) && angular.isDate(scope.maxDate)) {
// 					options.maxDate = new Date(scope.maxDate);
// 				}

// 				if (angular.isDefined(scope.minDate) && angular.isDate(scope.minDate)) {
// 					options.minDate = new Date(scope.minDate);
// 				}

// 				if (scope.localeCode) {
// 					options.lang = scope.localeCode;
// 				} else {
// 					options.lang = 'pt-BR';
// 				}

// 				$(element[0]).datetimepicker({
// 					scrollMonth: false,
// 					scrollTime: false,
// 					allowBlank: true,
// 					mask: '39/19/9999',
// 					format: 'd/m/Y H:i',
// 					onShow: () => {
// 						this.setOptions(options);
// 					},
// 					//mask: false,
// 					onClose: () => {
// 						ngModel.$parsers.unshift(function (viewValue) {
// 							if (angular.isUndefined(viewValue) 
// 									|| viewValue === '' 
// 									|| viewValue === ' ' 
// 									|| viewValue === 'Invalid Date' 
// 									|| viewValue == null) {
// 								return null;
// 							} else {
// 								if (angular.isDate(viewValue)) {
// 									return viewValue;
// 								} else {
// 									return moment(viewValue, 'DD/MM/YYYY').toDate();
// 								}
// 							}
// 						});
// 					}
// 				});
// 			});
// 		}
// 	};
// }