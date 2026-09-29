import angular from 'angular';
import stateUtils from 'utils/state.utils';
import uiRouter from 'angular-ui-router';
import ROUTES from 'utils/routes';
import TABS from './util/abas.config';

export default angular
	.module('routes.avaliacao', [stateUtils, uiRouter])
	.config(routeConfig)
	.config(translateConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('avaliacao');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./avaliacao.module');

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	/* @ngInject */
	function loadAvaliacaoOnEnter(AvaliacaoViewModel, $stateParams, $state) {
		const avaliacaoId = $stateParams.objectId;

		const loadEditView = (data) => AvaliacaoViewModel.load(avaliacaoId)
			.then(model => ({
				...data,
				model,
				avaliacao: model.avaliacao,
				object: model.avaliacao,
			}));

		const setStateValues = (values) => {
			Object.assign($state, values);
		};

		return Promise.resolve({})
			.then(loadEditView)
			.then(setStateValues);
	}

	// TODO Traduzir
	const data = {
		titulo: 'Avaliação',
		modulo: 'Campanhas',
		subtitulo: 'Campanha',
	};

	/* @ngInject */
	const findTabByStateName = (stateName) => TABS.find(tab => tab.stateName === stateName);

	/* @ngInject */
	const setTabInfo = ($state, $state$) => {
		const { name } = $state$;
		const tab = findTabByStateName(name);
		$state.tab = tab;
		$state.selectedIndexTab = tab.index;
	};

	const controllerAlias = '$ctrl';

	$lazyStateProvider
		.state({
			moduleLoader,
			abstract: true,
			stateName: ROUTES.MAIN.AVALIACAO.name,
			url: '/campanha',
			template: '<ui-view/>',
			resolve: {
				roles: listRoles,
			},
			data,
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.name,
			url: '/{campanhaId:int}/avaliacao/{objectId:int}',
			templateUrl: 'avaliacao.view.html',
			controller: 'AvaliacaoViewCtrl',
			controllerAs: controllerAlias,
			onEnter: loadAvaliacaoOnEnter,
			data
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.EDIT.name,
			url: '/{campanhaId:int}/avaliacao/{objectId:int}/editar',
			templateUrl: 'avaliacao.edit.html',
			controller: 'AvaliacaoEditCtrl',
			controllerAs: controllerAlias,
			onEnter: loadAvaliacaoOnEnter,
			data
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.NEW.name,
			url: '/{campanhaId:int}/avaliacao/new',
			templateUrl: 'avaliacao.new.html',
			controller: 'AvaliacaoNewCtrl',
			controllerAs: controllerAlias,
			data
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.RESUMO.name,
			url: '/resumo',
			templateUrl: 'aba.resumo.view.html',
			controller: 'AbaResumoCtrl',
			controllerAs: controllerAlias,
			onEnter: setTabInfo
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.PARTICIPANTES.name,
			url: '/participantes',
			templateUrl: 'aba.participantes.html',
			controller: 'AbaParticipantesViewCtrl',
			controllerAs: controllerAlias,
			onEnter: setTabInfo,
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.AVALIADORES.name,
			url: '/avaliadores',
			templateUrl: 'aba.avaliadores.html',
			controller: 'AbaAvaliadoresViewCtrl',
			controllerAs: controllerAlias,
			onEnter: setTabInfo,
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.MENSAGENS.name,
			url: '/mensagens',
			templateUrl: 'aba.mensagens.html',
			controller: 'AbaMensagensCtrl',
			controllerAs: controllerAlias,
			onEnter: setTabInfo,
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.NEW_MENSAGEM.name,
			url: '/{campanhaId:int}/avaliacao/{objectId:int}/mensagem/new',
			templateUrl: 'avaliacao.mensagem.html',
			controller: 'MensagemCadastroCtrl',
			controllerAs: controllerAlias,
			onEnter: loadAvaliacaoOnEnter,
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.AVALIACAO.VIEW.PENDENCIAS.name,
			url: '/pendencias',
			templateUrl: 'aba.pendencias.html',
			controller: 'AbaPendenciasCadastroCtrl',
			controllerAs: controllerAlias,
			onEnter: setTabInfo,
		});
}
