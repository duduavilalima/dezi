import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

export default angular
	.module('routes.preenchimento', [stateUtils, menuUtils])
	.config(routeConfig)
	.config(translateConfig)
	.run(menuConfig)
	.name;

function menuConfig(MenuUtils) {
	const avaliacoes = {
		stateName: 'avaliacoes',
		title: 'Avaliações',
		group: false,
		icon: 'icon-checkbox-marked-outline',
		state: 'main.preenchimento.avaliacoes',
		translate: 'NAV.AVALIACOES',
		weight: 3
	};

	MenuUtils.addMenu(avaliacoes);

	const consensos = {
		stateName: 'consensos',
		title: 'Consensos',
		group: false,
		icon: 'icon-checkbox-multiple-marked-outline',
		state: 'main.preenchimento.consensos',
		translate: 'NAV.CONSENSOS',
		weight: 4
	};

	MenuUtils.addMenu(consensos);
}

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('preenchimento');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./preenchimento.module');

	const data = {
		titulo: 'Preenchimento',
		modulo: 'Preenchimento',
		subtitulo: 'Preenchimento'
	};

	const controllerAlias = '$ctrl';

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: 'main.preenchimento',
			url: '/preenchimento',
			template: '<ui-view></ui-view>',
		})
		.state({
			moduleLoader,
			stateName: 'main.preenchimento.avaliacoes',
			url: '/avaliacoes/list',
			templateUrl: 'preenchimento.list.avaliacoes.html',
			controller: 'PreenchimentoListController',
			controllerAs: controllerAlias,
			data
		})
		.state({
			moduleLoader,
			stateName: 'main.preenchimento.consensos',
			url: '/consensos/list',
			templateUrl: 'preenchimento.list.consensos.html',
			controller: 'PreenchimentoListController',
			controllerAs: controllerAlias
		})
		.state({
			moduleLoader,
			stateName: 'main.preenchimento.detail',
			url: '/:id/:stage?from',
			templateUrl: 'preenchimento.detail.html',
			controller: 'PreenchimentoDetailController',
			controllerAs: controllerAlias,
			resolve: {
				/* @ngInject */
				stage: $stateParams => {
					return $stateParams.stage;
				},
				/* @ngInject */
				objectId: $stateParams => {
					return $stateParams.id;
				},
				/* @ngInject */
				from: $stateParams => {
					return $stateParams.from;
				}
			},
			params: { stage: '' }
		})
		.state({
			moduleLoader,
			stateName: 'main.preenchimento.detail_competencia',
			url:
			'/:stage/avaliacao/:avaliacaoId/avaliador/:avaliadorId/competencia/:competenciaId/' +
			'perfil/:perfilId/listaCompetencias/:listaCompetencias/todosConsensosLiberados/:todosConsensosLiberados/final/:final',
			templateUrl: 'detail.competencia.html',
			controller: 'PreenchimentoDetailCompetencia',
			controllerAs: controllerAlias,
		});
}
