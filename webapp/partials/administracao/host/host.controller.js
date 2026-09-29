export default function admHostController($scope, Empresa, empresas) {
	$scope.empresas = empresas;

	$scope.salvarParametros = function () {
		//TODO Está estranho buscar de um resource e fazer update em outro
		const promises = $scope.empresas.map(value => Empresa.update({ id: value.id }, value).$promise);
		Promise.all(promises).then(() => {
			$scope.$emit('notify-success', 'Hosts atualizados com sucesso!');
		});
	};
}
