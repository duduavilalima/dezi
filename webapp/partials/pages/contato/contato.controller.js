/* @ngInject */
export default function contatoController(
	$scope,
	$rootScope,
	$translate,
	$mdDialog,
	$timeout,
	Contato,
	toastr
) {
	$scope.title = 'Abrir Chamado';

	$scope.hide = function () {
		$mdDialog.hide();
	};

	$scope.cancel = function () {
		$scope.hide();
	};

	$scope.listaClassificacao = [];

	$translate(['CLASSIFICACAO.DUVIDA', 'CLASSIFICACAO.CRITICA', 'CLASSIFICACAO.SUGESTAO', 'CLASSIFICACAO.RECLAMACAO']).then(function (translations) {
		$scope.listaClassificacao.push({
			index: 0,
			valor: translations['CLASSIFICACAO.DUVIDA']
		});
		$scope.listaClassificacao.push({
			index: 1,
			valor: translations['CLASSIFICACAO.CRITICA']
		});
		$scope.listaClassificacao.push({
			index: 2,
			valor: translations['CLASSIFICACAO.SUGESTAO']
		});
		$scope.listaClassificacao.push({
			index: 3,
			valor: translations['CLASSIFICACAO.RECLAMACAO']
		});
	});

	$scope.enviar = function () {
		$scope.action = true;
		var msg = '';
		$translate(['CONTATO.TITULO', 'CHAMADO.SUCCESS']).then(function (translations) {
			msg = translations['CHAMADO.SUCCESS'];

			$timeout(function () {
				$scope.object.classificacao = $scope.listaClassificacao[$scope.object.classificacao.index].valor;
				$scope.object.funcionario = $rootScope.user;
				$scope.object.empresa = $rootScope.user.empresa;
				Contato.enviar($scope.object);
				toastr.success(msg, '');
				$scope.hide();
			}, 1000);
		});
	};
}
