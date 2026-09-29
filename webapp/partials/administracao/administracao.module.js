import angular from 'angular';

import menuUtils from 'utils/menu.utils';

export default angular.module('app.administracao', [menuUtils])
	.config(routeConfig)
	.run(menuConfig)
	.name;

/* @ngInject */
function routeConfig($lazyStateProvider) {
	$lazyStateProvider.state({
		stateName: 'main.adm',
		abstract: true,
		template: '<ui-view/>'
	});
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'adm',
		title: 'Administração',
		group: false,
		icon: 'icon-cog',
		translate: 'NAV.ADMINISTRACAO',
		weight: 10,
		roles: ['ROLE_ADMINISTRADOR']
	};

	MenuUtils.addMenu(item);
}
