import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import funcionarioService from 'services/funcionario';

import './funcionario.list.html';
import './funcionario.edit.html';

const BASE_STATE = 'main.cadastro.colaboradores';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.colaboradores', [stateUtils, menuUtils, funcionarioService])
	.config(translateConfig)
	.run(menuConfig)
	.config(routeConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/funcionarios');
}

/* @ngInject */
function menuConfig(MenuUtils) {
	var item = {
		title: 'Colaboradores',
		stateName: 'cadastros.colaboradores',
		group: false,
		state: BASE_STATE + '.list',
		translate: 'NAV.FUNCIONARIOS',
		weight: 4,
		roles: [ ...ROLES_VIEW, ...ROLES_EDIT ]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./funcionario.module');

	const data = {
		entity: 'CADASTRO.FUNCIONARIO.TITULO',
		newBtn: 'FUNCIONARIO.BTNNOVO',
		baseState: BASE_STATE,
		editRoles: ROLES_EDIT,
		createRoles: ROLES_EDIT,
	};

	/* @ngInject */
	const createLoaders = (
		$stateParams,
		FuncionarioFormacao,
		FuncionarioIdioma,
		AvaliacaoParticipante
	) => {
		const { objectId } = $stateParams;

		const loadFuncionarioFormacoes = (id = objectId) => FuncionarioFormacao.buscar(null, { funcionario: { id } }).$promise;
		const loadFuncionarioIdiomas = (id = objectId) => FuncionarioIdioma.buscar(null, { funcionario: { id } }).$promise;
		const loadFuncionarioAvaliacoes = (id = objectId) => AvaliacaoParticipante.listarAvaliacoesPorAvaliado(id).$promise;

		return {
			loadFuncionarioFormacoes,
			loadFuncionarioIdiomas,
			loadFuncionarioAvaliacoes,
		};
	};

	/* @ngInject */
	const createColaborador = (Funcionario) => Funcionario.get().$promise;

	/* @ngInject */
	const loadColaborador = ($stateParams, Funcionario) => Funcionario.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const loadFuncionarioFormacoes = (loaders) => loaders.loadFuncionarioFormacoes();

	/* @ngInject */
	const loadFuncionarioIdiomas = (loaders) => loaders.loadFuncionarioIdiomas();

	/* @ngInject */
	const loadFuncionarioAvaliacoes = (loaders) => loaders.loadFuncionarioAvaliacoes();

	/* @ngInject */
	const listColaborador = (Funcionario) => Funcionario.all().$promise;

	/* @ngInject */
	const getLoggedUser = (UserService) => UserService.logged().$promise;

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
			url: '/colaboradores',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator(BASE_STATE),
				loggedUser: getLoggedUser,
				previousState,
			}
		})
		.state({
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'funcionario.list.html',
			controller: 'FuncionarioListController',
			controllerAs: '$ctrl',
			resolve: {
				colaboradores: listColaborador,
				previousState,
			},
			data: {
				...data,
				formName: 'Lista',
			}
		})
		.state({
			stateName: BASE_STATE + '.new',
			url: '/new',
			templateUrl: 'funcionario.edit.html',
			controller: 'FuncionarioEditController',
			controllerAs: '$ctrl',
			resolve: {
				colaborador: createColaborador,
				previousState,
			},
			data: {
				...data,
				mode: 'NEW',
			}
		})
		.state({
			stateName: BASE_STATE + '.view',
			url: '/{objectId:int}/view',
			templateUrl: 'funcionario.view.html',
			controller: 'FuncionarioViewController',
			controllerAs: '$ctrl',
			data: {
				...data,
				mode: 'VIEW',
			},
			resolve: {
				colaborador: loadColaborador,
				loaders: createLoaders,
				funcionarioFormacoes: loadFuncionarioFormacoes,
				funcionarioIdiomas: loadFuncionarioIdiomas,
				funcionarioAvaliacoes: loadFuncionarioAvaliacoes,
				previousState,
			}
		})
		.state({
			stateName: BASE_STATE + '.edit',
			url: '/{objectId:int}/edit',
			templateUrl: 'funcionario.edit.html',
			controller: 'FuncionarioEditController',
			controllerAs: '$ctrl',
			resolve: {
				colaborador: loadColaborador,
				loaders: createLoaders,
				previousState,
			},
			data: {
				...data,
				mode: 'EDIT',
			}
		});
}
