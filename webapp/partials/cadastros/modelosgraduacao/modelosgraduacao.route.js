import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import modeloGraduacaoService from 'services/modelograduacao';

const BASE_STATE = 'main.cadastro.modelosgraduacao';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.modelosgraduacao', [stateUtils, menuUtils, modeloGraduacaoService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/modelosgraduacao');
}

/* @ngInject */
function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.modelosgraduacao',
		title: 'Escalas de Graduaçãos',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.MODELOSDEGRADUACAO',
		weight: 2,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./modelosgraduacao.module');

	const data = {
		entity: 'MODELOS.MODELOSGRADUACAO',
		newBtn: 'MODELOS.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT
	};

	/* @ngInject */
	const loadCreateMessages = ($translate) => {
		const TK_SUCCESS = 'MODELOS.MSGCRIADO';
		const KEYS = [TK_SUCCESS];
		return $translate(KEYS).then(tr => ({
			success: tr[TK_SUCCESS],
		}));
	};

	/* @ngInject */
	const loadEditMessages = ($translate) => {
		const TK_SUCCESS = 'MODELOS.MSGATUALIZADO';
		const KEYS = [TK_SUCCESS];
		return $translate(KEYS).then(tr => ({
			success: tr[TK_SUCCESS],
		}));
	};

	/* @ngInject */
	const listRoles = (UserService) => UserService.roles().$promise;

	/* @ngInject */
	const createModeloGraduacao = (ModeloGraduacao) => ModeloGraduacao.get().$promise;

	/* @ngInject */
	const loadModeloGraduacao = ($stateParams, ModeloGraduacao) => ModeloGraduacao.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listModeloGraduacoes = (ModeloGraduacao) => ModeloGraduacao.all().$promise;

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
			url: '/modelograduacao',
			template: '<ui-view></ui-view>',
			resolve: {
				roles: listRoles,
				navigator: createNavigator(BASE_STATE)
			}
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'modelosgraduacao.list.html',
			controller: 'ModelosGraduacaoListCtrl as $ctrl',
			resolve: {
				modelosGraduacao: listModeloGraduacoes,
			},
			data
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'modelosgraduacao.edit.html',
			controller: 'ModelosGraduacaoEditCtrl as $ctrl',
			data: { ...data, mode: 'NEW' },
			resolve: {
				modeloGraduacao: createModeloGraduacao,
				messages: loadCreateMessages,
				onCancel: /* @ngInject */ (navigator) => () => navigator.goList(),
				onSave: /* @ngInject */ (ModeloGraduacao) =>
					object => ModeloGraduacao.save({ id: object.id }, object).$promise,
			}
		})
		.state({
			stateName: BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'modelosgraduacao.view.html',
			controller: 'ModelosGraduacaoViewCtrl as $ctrl',
			resolve: {
				modeloGraduacao: loadModeloGraduacao
			},
			data: { ...data, mode: 'VIEW' },
		})
		.state({
			stateName: BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'modelosgraduacao.edit.html',
			controller: 'ModelosGraduacaoEditCtrl as $ctrl',
			resolve: {
				modeloGraduacao: loadModeloGraduacao,
				messages: loadEditMessages,
				onCancel: /* @ngInject */ (navigator) => (object) => navigator.goView(object.id),
				onSave: /* @ngInject */ (ModeloGraduacao) =>
					object => ModeloGraduacao.update({ id: object.id }, object).$promise,
			},
			data: { ...data, mode: 'EDIT' },
		});
}
