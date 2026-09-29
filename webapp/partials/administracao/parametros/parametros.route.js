import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import parametroService from 'services/parametro.service';

export default angular.module('routes.parametros', [stateUtils, menuUtils, parametroService])
	.config(translateconfig)
	.config(routeConfig)
	.run(menuConfig)
	.name;

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('administracao/parametros');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./parametros.module');

	/* @ngInject */
	const listParametros = (Parametro) => Parametro.buscaTodos().$promise;

	$lazyStateProvider.state({
		moduleLoader,
		stateName: 'main.adm.parametros',
		url: '/administracao/parametros',
		templateUrl: 'parametros.html',
		controller: 'AdmParametrosController',
		resolve: {
			parametros: listParametros
		}
	});
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'adm.parametros',
		title: 'Parâmetros gerais',
		group: false,
		state: 'main.adm.parametros',
		translate: 'NAV.PARAMETROS',
		weight: 2,
		roles: ['ROLE_ADMINISTRADOR']
	};

	MenuUtils.addMenu(item);
}
