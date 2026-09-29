const STATE_NEW = 'main.campanha.new';
const STATE_VIEW = 'main.campanha.view';

/* @ngInject */
export default function campanhaListCtrl(
	$state,
	listCampanhas,
	roles
) {
	const $ctrl = this;
	$ctrl.listResult = listCampanhas;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.query = {
		order: 'descricao',
		limit: 10,
		page: 1
	};

	//TODO Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.authPodeIncluir = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.displayNew = () => {$state.go(STATE_NEW);};

	$ctrl.displayView = campanha => {$state.go(STATE_VIEW, { objectId: campanha.id });};
}
