import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import './cadastros.html';

export default angular.module('app.cadastros', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros',
		title: 'Cadastros',
		group: false,
		icon: 'icon-table-edit',
		translate: 'NAV.CADASTROS',
		weight: 7,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function controller($scope, $state, userRoles) {
	const $ctrl = this;

	$scope.filter = { value: '' };

	$scope.$watch(() => $state.current, function(state) {
		const editRoles = state.data.editRoles;
		const createRoles = state.data.createRoles;
		const baseState = state.data.baseState;
		const mode = state.data.mode;

		const objectId = $state.params.objectId;

		$ctrl.canEdit = () => userRoles.some(role => editRoles.indexOf(role.authority) >= 0);
		$ctrl.canCreate = () => userRoles.some(role => createRoles.indexOf(role.authority) >= 0);

		$ctrl.goNew = () => $state.go(baseState + '.new');
		//$ctrl.back = () => $state.go(baseState + '.list');
		$ctrl.back = () => {
			if(objectId && mode !== 'VIEW') {
				$state.go(baseState + '.view', {objectId: objectId});
			} else {
				$state.go(baseState + '.list');
			}
		};
	});
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	$lazyStateProvider
		.state({
			abstract: true,
			stateName: 'main.cadastro',
			url: '/cadastros',
			templateUrl: 'cadastros.html',
			controller: controller,
			controllerAs: '$ctrl'
		});
}
