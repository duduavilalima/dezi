import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import './formacoesidiomas.html';

export default angular
	.module('app.formacoesidiomas', [stateUtils, menuUtils])
	.config(routeConfig)
	.run(menuConfig)
	.name;

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'cadastros.formacoesidiomas',
		title: 'Formação e Idiomas',
		translate: 'NAV.FORMACAOEIDIOMAS',
		icon: 'icon-library',
		weight: 6,
		group: false,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH']
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	$lazyStateProvider
		.state({
			abstract: true,
			stateName: 'main.cadastros.formacoesidiomas',
			url: '/cadastros/formacoesidiomas',
			templateUrl: 'formacoesidiomas.html'
		});
}
