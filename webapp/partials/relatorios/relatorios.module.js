import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

export default angular.module('app.relatorios', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateconfig)
	.run(menuConfig)
	.name;

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('relatorios');
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'relatorios',
		title: 'Relatórios',
		group: false,
		icon: 'icon-chart-pie',
		weight: 5,
		translate: 'NAV.RELATORIOS',
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	$lazyStateProvider
		.state({
			abstract: true,
			stateName: 'main.relatorios',
			url: '/relatorios',
			templateUrl: '<ui-view/>'
		});
}
