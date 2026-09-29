/* @ngInject */
export default function pdiRelatorioIndividual(
	$scope,
	$state,
	userRoles,
	navigator,
	DialogWait
) {
	const $ctrl = this;
	$ctrl.state = $state;
	$ctrl.model = $scope.model = $state.model;

	$ctrl.print = () => {
		const dialog =  DialogWait.showClick('Aguarde...', false);
		$ctrl.model.imprimirRelatorio()
			.then((data) => {
				dialog.hide();
				$scope.$emit(data.type, data.statusText, data.data);
			}).catch((err) => { 
				dialog.hide();
				console.error(err);
			});
	};

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.acaoList = () => navigator.goAcoes($ctrl.model.funcionario.id);
}
