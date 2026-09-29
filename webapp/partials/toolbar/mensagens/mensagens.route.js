import angular from 'angular';

import stateUtils from 'utils/state.utils';
import TABS_MENSAGENS from './utils/abas.config';

export default angular
	.module('routes.mensagens', [stateUtils])
	.config(routeConfig)
	.name;

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./mensagens.module');

	const ctrlAlias = '$ctrl';

	/* @ngInject */
	const findTabByStateName = (stateName) => TABS_MENSAGENS.find(tab => tab.stateName === stateName);

	/* @ngInject */
	const setTabInfo = ($state, $state$) => {
		const { name } = $state$;
		const tab = findTabByStateName(name);
		$state.tab = tab;
		$state.selectedIndexTab = tab.index;
	};

	$lazyStateProvider
		.state({
			moduleLoader,
			stateName: 'main.mensagens',
			url: '/mensagens',
			templateUrl: 'mensagens.html',
			controller: 'MensagensCtrl',
			controllerAs: ctrlAlias,
		})
		.state({
			moduleLoader,
			stateName: 'main.mensagens.todas',
			url: '/todas',
			templateUrl: 'mensagens.todas.html',
			controller: 'MensagensCtrl',
			onEnter: setTabInfo,
			controllerAs: ctrlAlias
			// data
		})
		.state({
			moduleLoader,
			stateName: 'main.mensagens.recebidas',
			url: '/recebidas',
			templateUrl: 'mensagens.recebidas.html',
			controller: 'MensagensCtrl',
			onEnter: setTabInfo,
			controllerAs: ctrlAlias
			// data
		})
		.state({
			moduleLoader,
			stateName: 'main.mensagens.enviadas',
			url: '/enviadas',
			templateUrl: 'mensagens.enviadas.html',
			controller: 'MensagensCtrl',
			onEnter: setTabInfo,
			controllerAs: ctrlAlias
			// data
		});
	// .state({
	// 	moduleLoader,
	// 	stateName: 'mensagens',
	// 	url: '/mensagens',
	// 	templateUrl: 'mensagens.html',
	// 	controller: 'MensagensCtrl',
	// /* 		data: {
	// 	titulo: 'Mensagens',
	// 	modulo: 'Mensagens',
	// 	subtitulo: 'Mensagens'
	// }*/
	// });
}
