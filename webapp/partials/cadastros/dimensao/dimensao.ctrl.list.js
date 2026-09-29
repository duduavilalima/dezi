const STATE_LIST = 'dimensao.list';
const STATE_NEW = 'dimensao.new';
const STATE_VIEW = 'dimensoes.dimensao.view';
const STATE_EDIT = 'dimensoes.dimensao.edit';

/* @ngInject */
export default function dimensaoListCtrl(
	$rootScope,
	$scope,
	$state,
	listDimensao,
	roles
) {
	$scope.listResult = listDimensao;
	$scope.modulo = $state.current.data.modulo;
	$scope.titulo = $state.current.data.titulo;
	$scope.subtitulo = $state.current.data.subtitulo;

	//TODO Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$scope.authPodeIncluir = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$scope.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$scope.displayNew = function() {
		$state.go(STATE_NEW);
	};

	$scope.displayView = function(item) {
		$state.go(STATE_VIEW, { objectId: item.id });
	};

	$scope.displayEdit = function(item) {
		$state.go(STATE_EDIT, { objectId: item.id });
	};

	$scope.back = function() {
		//TODO Padronizar esse comportamento
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go(STATE_LIST);
		}
	};
}
