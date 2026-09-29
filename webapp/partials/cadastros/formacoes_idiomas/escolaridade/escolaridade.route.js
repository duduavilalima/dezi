import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import ROUTES from 'utils/routes';
const ROLES = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

export default angular.module('routes.escolaridade', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/formacoes_idiomas/escolaridade');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas.escolaridade',
		title: 'Escolaridade',
		group: false,
		state: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.LIST.name,
		translate: 'NAV.ESCOLARIDADE',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./escolaridade.module');

	const data = {
		entity: 'ESCOLARIDADE.ESCOLARIDADE2',
		baseState: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.name,
		editRoles: ROLES,
		createRoles: ROLES
	};

	/* @ngInject */
	const createEscolaridade = (Escolaridade) => Escolaridade.get().$promise;

	/* @ngInject */
	const loadEscolaridade = ($stateParams, Escolaridade) => Escolaridade.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listEscolaridade = (Escolaridade) => Escolaridade.all().$promise;

	/* @ngInject */
	const previousState = ($state) => {
		return {
			name: $state.current.name,
			params: $state.params,
			url: $state.href($state.current.name, $state.params)
		};
	};

	/* @ngInject */
	const createNavigator = () => ($state) => {
		const service = {};
		service.goList = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.LIST.name);
		service.goNew = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.NEW.name);
		service.goView = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.VIEW.name, { objectId: id });
		service.goEdit = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.EDIT.name, { objectId: id });

		return Promise.resolve(service);
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.name,
			url: '/escolaridade',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator()
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.LIST.name,
			url: '/list',
			templateUrl: 'escolaridade.list.html',
			controller: 'EscolaridadeListController as $ctrl',
			resolve: {
				escolaridadeList: listEscolaridade
			},
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.NEW.name,
			url: '/new',
			templateUrl: 'escolaridade.edit.html',
			controller: 'EscolaridadeEditController as $ctrl',
			resolve: {
				escolaridade: createEscolaridade,
				previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		/* .state({
			abstract: true,
			stateName: 'main.escolaridade.id',
			url: '/{objectId:int}',
			resolve: {
				escolaridade: loadEscolaridade,
				previousState
			}
		}) */
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'escolaridade.view.html',
			controller: 'EscolaridadeViewController as $ctrl',
			resolve: {
				escolaridade: loadEscolaridade,
			},
			data: {
				...data,
				mode: 'VIEW'
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.ESCOLARIDADE.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'escolaridade.edit.html',
			controller: 'EscolaridadeEditController as $ctrl',
			resolve: {
				escolaridade: loadEscolaridade,
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
