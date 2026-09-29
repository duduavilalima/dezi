import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';
const ROLES = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

export default angular.module('routes.idioma', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/formacoes_idiomas/idioma');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas.idioma',
		title: 'Idiomas',
		group: false,
		state: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.LIST.name,
		translate: 'NAV.IDIOMAS',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./idioma.module');

	const data = {
		entity: 'IDIOMA.IDIOMA',
		baseState: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.name,
		editRoles: ROLES,
		createRoles: ROLES
	};

	/* @ngInject */
	const createIdioma = (Idioma) => Idioma.get().$promise;

	/* @ngInject */
	const loadIdioma = ($stateParams, Idioma) => Idioma.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listIdioma = (Idioma) => Idioma.all().$promise;

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
		service.goList = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.LIST.name);
		service.goNew = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.NEW.name);
		service.goView = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.VIEW.name, { objectId: id });
		service.goEdit = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.EDIT.name, { objectId: id });

		return Promise.resolve(service);
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.name,
			url: '/idioma',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator()
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.LIST.name,
			url: '/list',
			templateUrl: 'idioma.list.html',
			controller: 'IdiomaListController as $ctrl',
			resolve: {
				idiomaList: listIdioma
			},
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.NEW.name,
			url: '/new',
			templateUrl: 'idioma.edit.html',
			controller: 'IdiomaEditController as $ctrl',
			resolve: {
				idioma: createIdioma,
				previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		/* .state({
			abstract: true,
			stateName: 'main.idioma.id',
			url: '/{objectId:int}',
			resolve: {
				idioma: loadIdioma,
				previousState
			}
		}) */
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'idioma.view.html',
			controller: 'IdiomaViewController as $ctrl',
			resolve: {
				idioma: loadIdioma
			},
			data: {
				...data,
				mode: 'VIEW'
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.IDIOMA.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'idioma.edit.html',
			controller: 'IdiomaEditController as $ctrl',
			resolve: {
				idioma: loadIdioma
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
