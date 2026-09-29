import ROUTES from 'utils/routes';

export default function cursoEspecializacaoEditController (
	$scope,
	$state,
	cursoEspecializacao,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.cursoEspecializacao = cursoEspecializacao;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.handleChange = (field, value) => {
		return $ctrl.cursoEspecializacao[field] = value;
	};

	$ctrl.handleCreate = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.NEW.name);

	$ctrl.save = () => {
		if ($ctrl.cursoEspecializacao.id) {
			$ctrl.cursoEspecializacao.$update().then(() => {
				$scope.$emit('notify-success', 'Curso de especialização atualizado');
			});
		} else {
			$ctrl.cursoEspecializacao.$save().then(() => {
				$scope.$emit('notify-success', 'Curso de especialização criado');
			});
		}
	};

	$ctrl.cancel = () => navigator.goView($ctrl.cursoEspecializacao.id);

	$ctrl.back = () => {
		if($ctrl.cursoEspecializacao.id) {
			navigator.goView($ctrl.cursoEspecializacao.id);
		} else {
			navigator.goList();
		}
	};
}
