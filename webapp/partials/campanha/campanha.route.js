import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import campanhaService from 'services/campanha';

export default angular.module('routes.campanha', [stateUtils, menuUtils, campanhaService])
	.run(menuConfig)
	.config(translateconfig)
	.config(routeConfig)
	.name;

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'campanha',
		title: 'Campanhas',
		group: false,
		icon: 'icon-lightbulb-outline',
		state: 'main.campanha.list',
		translate: 'NAV.CAMPANHAS',
		weight: 2,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('campanha');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./campanha.module');

	/* @ngInject */
	function listCampanhas(Campanha) {
		return Campanha.all().$promise;
	}

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	const data = {
		titulo: 'Campanha',
		modulo: 'campanha',
		subtitulo: 'Campanha'
	};

	const controllerAlias = '$ctrl';

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: 'main.campanha',
			url: '/campanha',
			template: '<ui-view></ui-view>',
			data
		})
		.state({
			moduleLoader,
			stateName: 'main.campanha.list',
			url: '/list',
			templateUrl: 'campanha.list.html',
			controller: 'CampanhaListCtrl',
			controllerAs: controllerAlias,
			resolve: {
				listCampanhas: listCampanhas,
				roles: listRoles
			}
		})
		.state({
			moduleLoader,
			stateName: 'main.campanha.view',
			url: '/{objectId:int}',
			templateUrl: 'campanha.view.html',
			controller: 'CampanhaViewCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			},
			data
		})
		.state({
			moduleLoader,
			stateName: 'main.campanha.new',
			url: '/nova?from&objectId',
			templateUrl: 'campanha.new.html',
			controller: 'CampanhaNewCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			},
			data
		})
		.state({
			moduleLoader,
			stateName: 'main.campanha.edit',
			url: '/{objectId:int}/editar',
			templateUrl: 'campanha.edit.html',
			controller: 'CampanhaEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			},
			data
		});
}
