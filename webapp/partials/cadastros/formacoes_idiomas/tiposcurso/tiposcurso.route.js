import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';
const ROLES = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

export default angular.module('routes.tiposcurso', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/formacoes_idiomas/tiposcurso');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas.tiposcurso',
		title: 'Tipos de Curso',
		group: false,
		state: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.LIST.name,
		translate: 'NAV.TIPOSDECURSO',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./tiposcurso.module');

	const data = {
		entity: 'TIPOS.TIPOSCURSO',
		baseState: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.name,
		editRoles: ROLES,
		createRoles: ROLES
	};

	/* @ngInject */
	const createTipoCurso = (TipoCurso) => TipoCurso.get().$promise;

	/* @ngInject */
	const loadTipoCurso = ($stateParams, TipoCurso) => TipoCurso.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listTipoCurso = (TipoCurso) => TipoCurso.all().$promise;

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
		service.goList = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.LIST.name);
		service.goNew = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.NEW.name);
		service.goView = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.VIEW.name, { objectId: id });
		service.goEdit = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.EDIT.name, { objectId: id });

		return Promise.resolve(service);
	};

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.name,
			url: '/tiposcurso',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator()
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.LIST.name,
			url: '/list',
			templateUrl: 'tiposcurso.list.html',
			controller: 'TipoCursoListController as $ctrl',
			resolve: {
				tipoCursoList: listTipoCurso
			},
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.NEW.name,
			url: '/new',
			templateUrl: 'tiposcurso.edit.html',
			controller: 'TipoCursoEditController as $ctrl',
			resolve: {
				tipoCurso: createTipoCurso,
				previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		/* 	.state({
			abstract: true,
			stateName: 'main.tipocurso.id',
			url: '/{objectId:int}',
			resolve: {
				tipoCurso: loadTipoCurso,
				previousState
			}
		}) */
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'tiposcurso.view.html',
			controller: 'TipoCursoViewController as $ctrl',
			controllerAs: '$ctrl',
			resolve: {
				tipoCurso: loadTipoCurso,
			},
			data: {
				...data,
				mode: 'VIEW'
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.TIPOSCURSO.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'tiposcurso.edit.html',
			controller: 'TipoCursoEditController as $ctrl',
			resolve: {
				tipoCurso: loadTipoCurso,
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
