import ROUTES from 'utils/routes';

export default function tipoCursoEditController (
	$scope,
	$state,
	tipoCurso,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.tipoCurso = tipoCurso;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.listRequer = [{nome: 'Sim', value: true}, {nome: 'Não', value: false}];

	$ctrl.handleChange = (field, value) => $ctrl.tipoCurso[field] = value;

	$ctrl.handleCreate = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.NEW.name);

	$ctrl.save = () => {
		if ($ctrl.tipoCurso.id) {
			$ctrl.tipoCurso.$update().then(() => {
				$scope.$emit('notify-success', 'Tipo de Curso atualizado');
			});
		} else {
			$ctrl.tipoCurso.$save().then(() => {
				$scope.$emit('notify-success', 'Tipo de Curso criado');
			});
		}
	};

	$ctrl.cancel = () => navigator.goView($ctrl.tipoCurso.id);

	$ctrl.back = () => {
		if($ctrl.tipoCurso.id) {
			navigator.goView($ctrl.tipoCurso.id);
		} else {
			navigator.goList();
		}
	};
}
