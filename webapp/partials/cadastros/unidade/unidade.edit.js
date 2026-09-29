/* @ngInject */
export default function unidadeEditCtrl(
	$scope,
	userRoles,
	navigator,
	unidade,
) {
	const $ctrl = this;

	$ctrl.unidade = unidade;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.handleChange = (field, value) => $ctrl.unidade[field] = value;

	$ctrl.handleCreate = () => navigator.goNew();

	$ctrl.save = () => {
		if ($ctrl.unidade.id) {
			$ctrl.unidade.$update().then(() => {
				$scope.$emit('notify-success', 'Unidade atualizada com sucesso!');
				navigator.goView($ctrl.unidade.id);
			});
		} else {
			$ctrl.unidade.$save().then(() => {
				$scope.$emit('notify-success', 'Unidade salva com sucesso!');
				navigator.goView($ctrl.unidade.id);
			});
		}
	};

	$ctrl.cancel = () => {
		if($ctrl.unidade.id) {
			navigator.goView($ctrl.unidade.id);
		} else {
			navigator.goList();
		}
	};
}