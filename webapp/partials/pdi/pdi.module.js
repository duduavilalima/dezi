import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';

export default angular.module('app.pdi', [stateUtils,menuUtils])
	.config(routeConfig)
	.config(translateconfig)
	.run(menuConfig)
	.name;

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('pdi');
}

function menuConfig(MenuUtils) {
	const item = {
		stateName: 'pdi',
		title: 'PDI',
		group: false,
		icon: 'icon-chart-areaspline',
		translate: 'PDI.MENU_TITULO',
		weight: 6,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH', 'ROLE_GESTOR_AREA', 'ROLE_FUNCIONARIO']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	$lazyStateProvider
		.state({
			abstract: true,
			stateName: ROUTES.MAIN.PDI.name,
			url: '/pdi',
			template: '<ui-view/>'
		});
}
