import angular from 'angular';

export default angular
	.module('dialogs.unidade', [])
	.factory('DialogSubunidade', dialogSubunidadeFactory)
	.name;

/* @ngInject */
function dialogSubunidadeFactory($mdDialog) {
	/* @ngInject */
	function dialogCtrl($scope, unidade, unidadePai) {
		unidade.unidadePai = unidadePai;
		$scope.object = unidade;
		$scope.save = () => $mdDialog.hide(unidade);
		$scope.cancel = $mdDialog.cancel;
	}

	/* @ngInject */
	const createUnidade = Unidade => {
		return Unidade.get().$promise;
	};

	function show(unidadePai) {
		return $mdDialog.show({
			templateUrl: 'unidade.subunidade.html',
			controller: dialogCtrl,
			resolve: {
				unidade: createUnidade
			},
			locals: {
				unidadePai
			},
			targetEvent: event
		});
	}

	return {
		show
	};
}
