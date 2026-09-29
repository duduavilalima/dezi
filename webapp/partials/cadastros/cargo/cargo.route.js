import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import cargoService from 'services/cargo';

const BASE_STATE = 'main.cadastro.cargos';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.cargo', [stateUtils, menuUtils, cargoService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/cargo');
}

/* @ngInject */
function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.cargo',
		title: 'Cargos',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.CARGOS',
		weight: 3,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./cargo.module');

	const data = {
		entity: 'CADASTRO.CARGO.TITULO',
		newBtn: 'CARGO.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT
	};

	/* @ngInject */
	const createCargo = (Cargo) => Cargo.get().$promise;

	/* @ngInject */
	const loadCargo = ($stateParams, Cargo) => Cargo.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listCargos = (Cargo) => Cargo.all().$promise;

	/* @ngInject */
	const listDepartamentos = (Departamento) => Departamento.all().$promise;

	/* @ngInject */
	const cargoTipoCompetencia = ($stateParams, CargoTipoCompetencia) => CargoTipoCompetencia.get({ id: $stateParams.tipoId });

	const createNavigator = (baseState) => {
		/* @ngInject */
		const navigatorResolver = ($state) => {
			const service = {};
			service.goList = () => $state.go(baseState + '.list');
			service.goNew = () => $state.go(baseState + '.new');
			service.goView = (id) => $state.go(baseState + '.view', { objectId: id });
			service.goEdit = (id) => $state.go(baseState + '.edit', { objectId: id });
			service.goTipo = (cargo_id,tipo_id) => $state.go(baseState + '.tipo', { objectId: cargo_id,  tipoId: tipo_id});
			service.goFuncionario = id => $state.go('main.cadastro.colaboradores.view', { objectId: id });
			return Promise.resolve(service);
		};

		return navigatorResolver;
	};

	/* @ngInject */
	const previousState = ($state) => {
		return {
			name: $state.current.name,
			params: $state.params,
			url: $state.href($state.current.name, $state.params)
		};
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: BASE_STATE,
			url: '/cargo',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator(BASE_STATE)
			}
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'cargo.list.html',
			controller: 'CargoListController',
			controllerAs: '$ctrl',
			resolve: {
				cargos: listCargos
			},
			data: {
				...data,
				formName: 'Lista'
			}
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'cargo.edit.html',
			controller: 'CargoEditController',
			controllerAs: '$ctrl',
			resolve: {
				cargo: createCargo,
				departamentos: listDepartamentos,
				previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		.state({
			stateName: BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'cargo.view.html',
			controller: 'CargoViewController',
			controllerAs: '$ctrl',
			data: {
				...data,
				mode: 'VIEW'
			},
			resolve: {
				cargo: loadCargo,
				previousState
			}
		})
		.state({
			stateName: BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'cargo.edit.html',
			controller: 'CargoEditController',
			controllerAs: '$ctrl',
			resolve: {
				cargo: loadCargo,
				departamentos: listDepartamentos,
				previousState
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		})
		.state({
			stateName: BASE_STATE + '.tipo',
			url: '/{objectId:int}/tipo/{tipoId:int}',
			templateUrl: 'associar.competencia.html',
			controller: 'AssociarCompetenciaCtrl',
			controllerAs: '$ctrl',
			resolve: {
				cargoTipoCompetencia: cargoTipoCompetencia,
				previousState
			},
			data: {
				...data,
				mode: 'TIPOCOMP'
			}
		});
}
