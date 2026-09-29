import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import tipoCompetenciaService from 'services/tipocompetencia';

const BASE_STATE = 'main.cadastro.tipocompetencia';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.tipocompetencia', [stateUtils, menuUtils, tipoCompetenciaService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/tipodecompetencias');
}

/* @ngInject */
function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.tipocompetencia',
		title: 'Tipo de Competências',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.TIPODECOMPETENCIAS',
		weight: 2,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./tipodecompetencias.module');

	const data = {
		entity: 'CADASTRO.TIPOCOMPETENCIA.TITULO',
		newBtn: 'TIPODECOMPETENCIAS.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT
	};

	/* @ngInject */
	const listRoles = (UserService) => UserService.roles().$promise;

	/* @ngInject */
	const listTiposCompetencia = (TipoCompetencia) => TipoCompetencia.all().$promise;

	/* @ngInject */
	const createTipoCompetencia = ($stateParams, TipoCompetencia) => TipoCompetencia.get().$promise;

	/* @ngInject */
	const loadTipoCompetencia = ($stateParams, TipoCompetencia) => TipoCompetencia.get({ id: $stateParams.objectId }).$promise;

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
			url: '/tipocompetencia',
			template: '<ui-view></ui-view>',
			resolve: {
				roles: listRoles,
				navigator: createNavigator(BASE_STATE)
			},
			data
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'tipodecompetencias.list.html',
			controller: 'TipoCompetenciaListCtrl as $ctrl',
			resolve: {
				tiposCompetencia: listTiposCompetencia
			}
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'tipodecompetencias.edit.html',
			controller: 'TipoCompetenciaEditCtrl as $ctrl',
			resolve: {
				tipoCompetencia: createTipoCompetencia
			},
			data: { ...data, mode: 'NEW' }
		})
		.state({
			stateName: BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'tipodecompetencias.edit.html',
			controller: 'TipoCompetenciaEditCtrl as $ctrl',
			resolve: {
				tipoCompetencia: loadTipoCompetencia
			},
			data: { ...data, mode: 'VIEW' }
		})
		.state({
			stateName: BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'tipodecompetencias.edit.html',
			controller: 'TipoCompetenciaEditCtrl as $ctrl',
			resolve: {
				tipoCompetencia: loadTipoCompetencia
			},
			data: { ...data, mode: 'EDIT' }
		});
}
