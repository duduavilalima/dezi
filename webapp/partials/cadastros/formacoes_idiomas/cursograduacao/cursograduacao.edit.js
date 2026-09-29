export default function cursoGraduacaoEditController (
	$scope,
	cursoGraduacao,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.cursoGraduacao = cursoGraduacao;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.listRequer = [{nome: 'Sim', value: true}, {nome: 'Não', value: false}];

	$ctrl.handleChange = (field, value) => $ctrl.cursoGraduacao[field] = value;

	$ctrl.handleCreate = () => navigator.goNew();

	$ctrl.save = () => {
		if ($ctrl.cursoGraduacao.id) {
			$ctrl.cursoGraduacao.$update().then(() => {
				$scope.$emit('notify-success', 'Curso de graduação atualizado');
			});
		} else {
			$ctrl.cursoGraduacao.$save().then(() => {
				$scope.$emit('notify-success', 'Curso de graduação criado');
			});
		}
	};

	$ctrl.cancel = () => navigator.goView($ctrl.cursoGraduacao.id);

	$ctrl.back = () => {
		if($ctrl.cursoGraduacao.id) {
			navigator.goView($ctrl.cursoGraduacao.id);
		} else {
			navigator.goList();
		}
	};
}
