export default function tipoCursoListController(
	navigator,
	$state,
	userRoles,
	tipoCursoList
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.listCursos = tipoCursoList;

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(
		role => requiredRoles.indexOf(role.authority) >= 0
	);

	$ctrl.displayNew = () => navigator.goNew();

	$ctrl.displayView = tipoCurso => navigator.goView(tipoCurso.id);

	$ctrl.tableConf = {
		order: 'nome',
		limit: 10,
		page: 1
	};
}
