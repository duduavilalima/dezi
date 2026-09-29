import angular from 'angular';

/* @ngInject */
function navigationController($scope, $mdMedia) {
	// Data
	$scope.bodyEl = angular.element('body');
	// Manter menu aberto = FALSE.
	$scope.folded = false;

	$scope.msScrollOptions = {
		suppressScrollX: true
	};

	// Methods
	$scope.toggleMsNavigationFolded = function () {
		$scope.folded = !$scope.folded;

		var filtros = angular.element('.margin-input');
		var progressoCompetencia = angular.element('#progress_competencia');

		if ($scope.folded === false) {
			filtros.removeClass('teste2');

			if (!$mdMedia('xs')) {
				progressoCompetencia.removeClass('progresso-competencia2');
				progressoCompetencia.addClass('progresso-competencia1');
			}
		} else {
			filtros.addClass('teste2');

			if (!$mdMedia('xs')) {
				progressoCompetencia.removeClass('progresso-competencia1');
				progressoCompetencia.addClass('progresso-competencia2');
			}
		}
	};

	// Close the mobile menu on $stateChangeSuccess
	$scope.$on('$stateChangeSuccess', function () {
		$scope.bodyEl.removeClass('ms-navigation-horizontal-mobile-menu-active');
	});
}

export default navigationController;
