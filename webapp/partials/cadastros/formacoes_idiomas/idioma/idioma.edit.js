import ROUTES from 'utils/routes';

export default function idiomaEditController (
	$scope,
	$state,
	idioma,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.idioma = idioma;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.listRequer = [{nome: 'Sim', value: true}, {nome: 'Não', value: false}];

	$ctrl.handleChange = (field, value) => $ctrl.idioma[field] = value;

	$ctrl.handleCreate = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.NEW.name);

	$ctrl.save = () => {
		if ($ctrl.idioma.id) {
			$ctrl.idioma.$update().then(() => {
				$scope.$emit('notify-success', 'Idioma atualizado');
			});
		} else {
			$ctrl.idioma.$save().then(() => {
				$scope.$emit('notify-success', 'Idioma criado');
			});
		}
	};

	$ctrl.cancel = () => navigator.goView($ctrl.idioma.id);

	$ctrl.back = () => {
		if($ctrl.idioma.id) {
			navigator.goView($ctrl.idioma.id);
		} else {
			navigator.goList();
		}
	};
}
