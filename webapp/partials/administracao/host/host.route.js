import angular from 'angular';

import menuUtils from 'utils/menu.utils';

import hostEmpresaService from 'services/hostempresa.service';

export default angular.module('routes.adm.host', [menuUtils, hostEmpresaService])
	.config(translateconfig)
	.config(routeConfig)
	.run(menuConfig)
	.name;

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('administracao/host');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./host.module');

	/* @ngInject */
	const listEmpresas = (HostEmpresa) => HostEmpresa.todas().$promise;

	$lazyStateProvider.state({
		moduleLoader,
		stateName: 'main.adm.host',
		url: '/administracao/host',
		templateUrl: 'host.html',
		controller: 'AdmHostController',
		resolve: {
			empresas: listEmpresas
		}
	});
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'adm.host',
		title: 'Host por empresas',
		group: false,
		state: 'main.adm.host',
		translate: 'NAV.HOST',
		weight: 3,
		roles: ['ROLE_ADMINISTRADOR']
	};

	MenuUtils.addMenu(item);
}
