import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';

//const BASE_STATE = 'main.cadastro.cursosespecializacao';
const ROLES = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

export default angular.module('routes.cursosespecializacao', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/formacoes_idiomas/cursosespecializacao');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas.cursosespecializacao',
		title: 'Cursos de Especialização',
		group: false,
		state: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.LIST.name,
		translate: 'NAV.CURSOSDEESPECILIZACAO',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./cursosespecializacao.module');

	const data = {
		entity: 'ESPECIALIZACAO.ESPECIALIZA',
		baseState: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.name,
		editRoles: ROLES,
		createRoles: ROLES
	};

	/* @ngInject */
	const createCursoEspecializacao = (CursoEspecializacao) => CursoEspecializacao.get().$promise;

	/* @ngInject */
	const loadCursoEspecializacao = ($stateParams, CursoEspecializacao) => CursoEspecializacao.get({ id: $stateParams.objectId }).$promise;

	/* @ngInject */
	const listCursoEspecializacao = (CursoEspecializacao) => CursoEspecializacao.all().$promise;

	/* @ngInject */
	const createNavigator = () => ($state) => {
		const service = {};
		service.goList = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.LIST.name);
		service.goNew = () => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.NEW.name);
		service.goView = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.VIEW.name, { objectId: id });
		service.goEdit = (id) => $state.go(ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.EDIT.name, { objectId: id });

		return Promise.resolve(service);
	};

	/* @ngInject */
	/* const previousState = ($state) => {
		return {
			name: $state.current.name,
			params: $state.params,
			url: $state.href($state.current.name, $state.params)
		};
	}; */

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.name,
			url: '/cursosespecializacao',
			template: '<ui-view/>',
			resolve: {
				navigator: createNavigator()
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.LIST.name,
			url: '/list',
			templateUrl: 'cursosespecializacao.list.html',
			controller: 'CursoEspecializacaoListController as $ctrl',
			resolve: {
				cursoEspecializacaoList: listCursoEspecializacao
			},
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.NEW.name,
			url: '/new',
			templateUrl: 'cursosespecializacao.edit.html',
			controller: 'CursoEspecializacaoEditController as $ctrl',
			resolve: {
				cursoEspecializacao: createCursoEspecializacao,
				//previousState
			},
			data: {
				...data,
				mode: 'NEW'
			}
		})
		/* .state({
			abstract: true,
			stateName: 'main.cursosespecializacao.id',
			url: '/{objectId:int}',
			resolve: {
				cursoEspecializacao: loadCursoEspecializacao,
				previousState
			}
		}) */
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'cursosespecializacao.view.html',
			controller: 'CursoEspecializacaoViewController as $ctrl',
			resolve: {
				cursoEspecializacao: loadCursoEspecializacao,
			},
			data: {
				...data,
				mode: 'VIEW'
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.FORMACOES_IDIOMAS.CURSOSESPEC.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'cursosespecializacao.edit.html',
			controller: 'CursoEspecializacaoEditController as $ctrl',
			resolve: {
				cursoEspecializacao: loadCursoEspecializacao,
			},
			data: {
				...data,
				mode: 'EDIT'
			}
		});
}
