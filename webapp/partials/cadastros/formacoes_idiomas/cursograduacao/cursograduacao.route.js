import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';

//const BASE_STATE = 'main.cadastro.cursosespecializacao';
const ROLES = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

export default angular.module('routes.cursograduacao', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/formacoes_idiomas/cursograduacao');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas.cursograduacao',
		title: 'Cursos de Graduação',
		group: false,
		state: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.LIST.name,
		translate: 'NAV.CURSOSDEGRADUACAO',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./cursograduacao.module');

	const data = {
		entity: 'GRADUACAO.CURSODEGRADUACAO',
		baseState: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.name,
		editRoles: ROLES,
		createRoles: ROLES
	};

	/* @ngInject */
	const createCursoGraduacao = (CursoGraduacao) => CursoGraduacao.get().$promise;

	/* @ngInject */
	const loadCursoGraduacao = ($stateParams, CursoGraduacao) => CursoGraduacao.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listCursoGraduacao = (CursoGraduacao) => CursoGraduacao.all().$promise;

	/* @ngInject */
	const createNavigator = () => ($state) => {
		const service = {};
		service.goList = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.LIST.name);
		service.goNew = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.NEW.name);
		service.goView = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.VIEW.name, { objectId: id });
		service.goEdit = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.EDIT.name, { objectId: id });

		return Promise.resolve(service);
	};
	
	// /* @ngInject */
	// const previousState = ($state) => {
	// 	return {
	// 		name: $state.current.name,
	// 		params: $state.params,
	// 		url: $state.href($state.current.name, $state.params)
	// 	};
	// };

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.name,
			url: '/cursograduacao',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator()
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.LIST.name,
			url: '/list',
			templateUrl: 'cursograduacao.list.html',
			controller: 'CursoGraduacaoListController as $ctrl',
			resolve: {
				cursoGraduacaoList: listCursoGraduacao
			},
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.NEW.name,
			url: '/new',
			templateUrl: 'cursograduacao.edit.html',
			controller: 'CursoGraduacaoEditController as $ctrl',
			resolve: {
				cursoGraduacao: createCursoGraduacao,
				//previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		/* .state({
			abstract: true,
			stateName: 'main.cursograduacao.id',
			url: '/{objectId:int}',
			resolve: {
				cursoGraduacao: loadCursoGraduacao,
				previousState
			}
		}) */
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'cursograduacao.view.html',
			controller: 'CursoGraduacaoViewController as $ctrl',
			resolve: {
				cursoGraduacao: loadCursoGraduacao,
			},
			data: {
				...data,
				mode: 'VIEW'
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOGRADUACAO.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'cursograduacao.edit.html',
			controller: 'CursoGraduacaoEditController as $ctrl',
			resolve: {
				cursoGraduacao: loadCursoGraduacao,
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
