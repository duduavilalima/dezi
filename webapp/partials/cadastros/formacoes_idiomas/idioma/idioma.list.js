export default function idiomaListController(
	navigator,
	$state,
	userRoles,
	idiomaList
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.listIdiomas = idiomaList;

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.displayNew = () => navigator.goNew();

	$ctrl.displayView = idioma => navigator.goView(idioma.id);

	$ctrl.tableConf = {
		order: 'nome',
		limit: 10,
		page: 1
	};
}
