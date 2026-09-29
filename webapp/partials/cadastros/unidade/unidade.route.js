import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import unidadeService from 'services/unidade';

const BASE_STATE = 'main.cadastro.unidades';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.unidade', [stateUtils, menuUtils, unidadeService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/unidade');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.unidades',
		title: 'Unidades',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.UNIDADE',
		weight: 2,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./unidade.module');

	const controllerAlias = '$ctrl';

	const data = {
		entity: 'UNIDADES.UNIDADES',
		newBtn: 'UNIDADES.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT
	};

	/* @ngInject */
	const listUnidades = (Unidade) => Unidade.all().$promise;

	/* @ngInject */
	const listRoles = (UserService) => UserService.roles().$promise;

	/* @ngInject */
	const createUnidade = ($stateParams, Unidade) => Unidade.get().$promise;

	/* @ngInject */
	const loadUnidade = ($stateParams, Unidade) => Unidade.get({ id: $stateParams.objectId }).$promise;

	const createNavigator = (baseState) => {
		/* @ngInject */
		const navigatorResolver = ($state) => {
			const service = {};
			service.goList = () => $state.go(baseState + '.list');
			service.goNew = () => $state.go(baseState + '.new');
			service.goView = (id) => $state.go(baseState + '.view', { objectId: id });
			service.goEdit = (id) => $state.go(baseState + '.edit', { objectId: id });

			return Promise.resolve(service);
		};
		
		return navigatorResolver;
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: BASE_STATE,
			url: '/unidade',
			template: '<ui-view></ui-view>',
			resolve: {
				navigator: createNavigator(BASE_STATE),
				roles: listRoles,
			},
			data
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'unidade.list.html',
			controller: 'UnidadeListCtrl',
			controllerAs: controllerAlias,
			resolve: {
				listUnidades: listUnidades
			},
			
		})
		.state({
			stateName:  BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'unidade.view.html',
			controller: 'UnidadeViewCtrl',
			controllerAs: controllerAlias,
			resolve: {
				unidade: loadUnidade
			},
			data: { ...data, mode: 'VIEW' }
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'unidade.edit.html',
			controller: 'UnidadeEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				unidade: createUnidade
			},
			data: { ...data, mode: 'NEW' }
		})
		.state({
			stateName: BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'unidade.edit.html',
			controller: 'UnidadeEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				unidade: loadUnidade
			},
			data: { ...data, mode: 'EDIT' }
		});
}
