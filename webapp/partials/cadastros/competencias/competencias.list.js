import ROUTES from 'utils/routes';

const STATE_LIST = ROUTES.MAIN.CADASTRO.COMPETENCIA.LIST.name;
const STATE_NEW = ROUTES.MAIN.CADASTRO.COMPETENCIA.NEW.name;
const STATE_VIEW = ROUTES.MAIN.CADASTRO.COMPETENCIA.VIEW.name;
const STATE_EDIT = ROUTES.MAIN.CADASTRO.COMPETENCIA.EDIT.name;

/* @ngInject */
export default function competenciaListCtrl(
	$rootScope,
	$scope,
	$state,
	listarCompetencia,
	roles
) {
	const $ctrl = this;

	$ctrl.listResult = listarCompetencia;

	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.query = {
		order: 'ad',
		limit: 10,
		page: 1
	};

	//TODO Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.authPodeIncluir = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.displayNew = () => {
		$state.go(STATE_NEW);
	};

	$ctrl.displayView = item => {
		$state.go(STATE_VIEW, { objectId: item.id });
	};

	$ctrl.displayEdit = item => {
		$state.go(STATE_EDIT, { objectId: item.id });
	};

	$ctrl.back = () => {
		//TODO Padronizar esse comportamento
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go(STATE_LIST);
		}
	};
}
