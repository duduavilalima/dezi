import angular from 'angular';
import ROUTES from 'utils/routes';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import competenciaService from 'services/competencia';

export default angular.module('routes.competencia', [stateUtils, menuUtils, competenciaService])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('cadastros/competencias');
}

/* @ngInject */
function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.competencias',
		title    : 'Competências',
		group    : false,
		state    : 'main.competencias.list',
		translate: 'NAV.COMPETENCIAS',
		weight   : 2,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./competencia.module');

	const controllerAlias = '$ctrl';

	const data = {
		titulo: 'Competências',
		modulo: 'Cadastros',
		subtitulo: ''
	};

	/* @ngInject */
	function listarCompetencia(Competencia) {
		return Competencia.all().$promise;
	}

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.CADASTRO.COMPETENCIA.name,
			url: '/cadastros/competencias',
			template: '<ui-view></ui-view>',
			data
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.COMPETENCIA.LIST.name,
			url: '/list',
			templateUrl: 'competencias.list.html',
			controller: 'CompetenciaListCtrl',
			controllerAs: controllerAlias,
			resolve: {
				listarCompetencia: listarCompetencia,
				roles: listRoles
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.COMPETENCIA.VIEW.name,
			url: '/{objectId:int}/view',
			templateUrl: 'competencia.view.html',
			controller: 'CompetenciaViewCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.COMPETENCIA.NEW.name,
			url: '/new',
			templateUrl: 'competencia.new.html',
			controller: 'CompetenciaNewCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			}
		})
		.state({
			stateName: ROUTES.MAIN.CADASTRO.COMPETENCIA.EDIT.name,
			url: '/{objectId:int}/edit',
			templateUrl: 'competencia.edit.html',
			controller: 'CompetenciaEditCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			}
		});
}
