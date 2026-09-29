import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import dimensaoService from 'services/dimensao';

export default angular.module('routes.dimensao', [stateUtils, menuUtils, dimensaoService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/dimensao');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.dimensao',
		title: 'Dimensão',
		group: false,
		state: 'main.dimensao.list',
		translate: 'NAV.DIMENSAO',
		weight: 2,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./dimensao.module');

	const data = {
		titulo: 'Dimensão',
		modulo: 'Cadastros',
		subtitulo: ''
	};

	/* @ngInject */
	function createDimensao(Dimensao) {
		return Dimensao.get().$promise;
	}

	/* @ngInject */
	function loadDimensao($stateParams, Dimensao) {
		return Dimensao.get({ id: $stateParams.objectId }).$promise;
	}

	/* @ngInject */
	function listDimensao(Dimensao) {
		return Dimensao.all().$promise;
	}

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: 'main.dimensao',
			url: '/cadastros/dimensao',
			template: '<ui-view></ui-view>',
			data
		})
		.state({
			stateName: 'main.dimensao.list',
			url: '/list',
			templateUrl: 'dimensao_list.html',
			controller: 'DimensaoListCtrl',
			resolve: {
				listDimensao: listDimensao,
				roles: listRoles
			}
		})
		.state({
			stateName: 'main.dimensao.new',
			url: '/new',
			templateUrl: 'dimensao.html',
			controller: 'dimensaoEditCtrl',
			data: { ...data, screenMode: 'NEW' },
			resolve: {
				dimensao: createDimensao
			}
		})
		.state({
			abstract: true,
			stateName: 'main.dimensoes.dimensao',
			url: '/{objectId:int}',
			resolve: {
				dimensao: loadDimensao
			}
		})
		.state({
			stateName: 'main.dimensoes.dimensao.view',
			url: '/view',
			templateUrl: 'dimensao_view.html',
			controller: 'DimensaoViewCtrl',
			data: { ...data, screenMode: 'VIEW' },
		})
		.state({
			stateName: 'main.dimensoes.dimensao.edit',
			url: '/edit',
			templateUrl: 'dimensao_edit.html',
			controller: 'dimensaoEditCtrl',
			data: { ...data, screenMode: 'EDIT' },
		});
}
