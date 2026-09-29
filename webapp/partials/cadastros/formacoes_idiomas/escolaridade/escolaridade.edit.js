import ROUTES from 'utils/routes';

export default function escolaridadeEditController (
	$scope,
	$state,
	escolaridade,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.escolaridade = escolaridade;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.handleChange = (field, value) => $ctrl.escolaridade[field] = value;

	$ctrl.handleCreate = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.NEW.name);

	$ctrl.save = () => {
		if ($ctrl.escolaridade.id) {
			$ctrl.escolaridade.$update().then(() => {
				$scope.$emit('notify-success', 'Curso de graduação atualizado');
			});
		} else {
			$ctrl.escolaridade.$save().then(() => {
				$scope.$emit('notify-success', 'Curso de graduação criado');
			});
		}
	};

	$ctrl.cancel = () => navigator.goView($ctrl.escolaridade.id);

	$ctrl.back = () => {
		if($ctrl.escolaridade.id) {
			navigator.goView($ctrl.escolaridade.id);
		} else {
			navigator.goList();
		}
	};
}
