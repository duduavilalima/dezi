/* @ngInject */
export default function departamentosEditCtrl(
	$scope,
	departamento,
	userRoles,
	navigator,
	listSetoresPai
) {
	const $ctrl = this;

	$ctrl.departamento = departamento;
	$ctrl.listSetoresPai = listSetoresPai.filter(depto => depto.id !== $ctrl.departamento.id);

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.handleChange = (field, value) => $ctrl.departamento[field] = value;

	$ctrl.handleCreate = () => navigator.goNew();

	$ctrl.save = () => {
		if ($ctrl.departamento.id) {
			$ctrl.departamento.$update().then(() => {
				$scope.$emit('notify-success', 'Setor atualizado com sucesso!');
				navigator.goView($ctrl.departamento.id);
			});
		} else {
			$ctrl.departamento.$save().then(() => {
				$scope.$emit('notify-success', 'Setor salvo com sucesso!');
				navigator.goView($ctrl.departamento.id);
			});
		}
	};

	$ctrl.cancel = () => {
		if($ctrl.departamento.id) {
			navigator.goView($ctrl.departamento.id);
		} else {
			navigator.goList();
		}
	};
}
