import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import departamentoService from 'services/departamento';

const BASE_STATE = 'main.cadastro.departamentos';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.departamentos', [stateUtils, menuUtils, departamentoService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/departamentos');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.departamentos',
		title: 'Setores',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.DEPARTAMENTOS',
		weight: 3,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./departamento.module');

	const controllerAlias = '$ctrl';

	const data = {
		entity: 'CADASTRO.DEPARTAMENTO.TITULO',
		newBtn: 'DEPARTAMENTO.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT
	};

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

	/* @ngInject */
	const loadDepartamento = ($stateParams, Departamento) => {
		return Departamento.get({ id: $stateParams.objectId }).$promise;
	};

	/* @ngInject */
	const createDepartamento = (Departamento) => {
		return Departamento.get().$promise;
	};

	/* @ngInject */
	const listDepartamentos = (Departamento) => {
		return Departamento.all().$promise;
	};

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	/* @ngInject */
	const listHierarquiaDepartamentos = (Departamento) => {
		return Departamento.departamentosDaEmpresaLogada().$promise;
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: BASE_STATE,
			url: '/cadastros/departamentos',
			template: '<ui-view></ui-view>',
			data,
			resolve: {
				navigator: createNavigator(BASE_STATE)
			}
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'departamentos.list.html',
			controller: 'DepartamentosListCtrl',
			controllerAs: controllerAlias,
			resolve: {
				listDepartamentos: listDepartamentos,
				userRoles: listRoles
			},
			data: {
				...data,
				formName: 'Lista'
			}
		})
		.state({
			stateName: BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'departamentos.view.html',
			controller: 'DepartamentosViewCtrl',
			controllerAs: controllerAlias,
			data: {
				...data,
				mode: 'VIEW'
			},
			resolve: {
				departamento: loadDepartamento,
				userRoles: listRoles
			}
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'departamentos.edit.html',
			controller: 'DepartamentosEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				departamento: createDepartamento,
				userRoles: listRoles,
				listSetoresPai: listHierarquiaDepartamentos
			},
			data: {
				...data,
				mode: 'NEW'
			}
		}).state({
			stateName:BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'departamentos.edit.html',
			controller: 'DepartamentosEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				departamento: loadDepartamento,
				userRoles: listRoles,
				listSetoresPai: listHierarquiaDepartamentos
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
