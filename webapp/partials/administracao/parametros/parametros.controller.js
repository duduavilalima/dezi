export default function admParametrosController($scope, Parametro, parametros) {
	$scope.parametros = parametros;

	$scope.salvarParametros = function () {
		var promises = $scope.parametros.map(param => Parametro.update(param).$promise);
		Promise.all(promises).then(() => {
			$scope.$emit('notify-success', 'Parametros alterados com sucesso!');
		});
	};
}
