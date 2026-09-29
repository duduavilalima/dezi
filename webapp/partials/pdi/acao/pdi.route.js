import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import TABS_PDI from './utils/abas.config';

const BASE_STATE = 'main.pdi.pesquisa_resultado';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH', 'ROLE_FUNCIONARIO' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH', 'ROLE_GESTOR_AREA' ];

export default angular.module('routes.pdiacao', [stateUtils,menuUtils])
	.run(menuConfig)
	.config(routeConfig)
	.name;

function menuConfig(MenuUtils) {
	const item = {
		stateName: 'pdi.pdiacao',
		title: 'Pesquisa e Resultado',
		group: false,
		icon: 'icon-clipboard-account',
		state: BASE_STATE + '.list',
		translate: 'PDI.MENU_TITULO_ACAO',
		weight: 4,
		roles: [...ROLES_EDIT, ...ROLES_VIEW]
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./pdi.acao.module');

	const controllerAlias = '$ctrl';

	/* @ngInject */
	function loggedUser(UserService) {
		return UserService.logged().$promise;
	}

	const createNavigator = (baseState) => {
		/* @ngInject */
		const navigatorResolver = ($state) => {
			const service = {};
			service.goList = () => $state.go(baseState + '.list');
			service.goAcoes = (id) => $state.go(baseState + '.acoes', { objectId: id });
			service.goAbaEmAprovacao = () => $state.go(baseState + '.acoes.em_aprovacao');
			service.goAbaAprovados = () => $state.go(baseState + '.acoes.aprovado');
			service.goAbaEmAndamento = () => $state.go(baseState + '.acoes.em_andamento');
			service.goAbaFinalizado = () => $state.go(baseState + '.acoes.finalizado');
			service.goRelatorio = (id) => $state.go(baseState + '.relatorio', {objectId: id});
			return Promise.resolve(service);
		};

		return navigatorResolver;
	};

	/* @ngInject */
	function listFuncionarios(Funcionario, FotoResource) {
		return 	Funcionario.buscar(null,  { situacao: 'ATIVA' }).$promise
			.then((funcionarios) => {
				funcionarios.forEach(funcionario => {
					FotoResource.avatarFuncionario({id: funcionario.id}).$promise.then((foto) => {
						funcionario.croppedImage = foto.base64;
					});
				});
				return funcionarios;
			});
	}

	/* @ngInject */
	function loadPDIOnEnter(PDIViewModel, $stateParams, $state) {
		const funcionarioId = $stateParams.objectId;
		/* TODO: PASSAR A ABA SELECIONADA PARA O EDIT VIEW FILTRAR AS LISTAS DE PDI A PARTIR DO STATUS */
		const loadEditView = (data) => PDIViewModel.load(funcionarioId)
			.then(model => ({
				...data,
				model,
				//avaliacao: model.avaliacao,
				//object: model.avaliacao,
			}));

		const setStateValues = (values) => {
			Object.assign($state, values);
		};

		return Promise.resolve({})
			.then(loadEditView)
			.then(setStateValues);
	}

	/* @ngInject */
	const findTabByStateName = (stateName) => TABS_PDI.find(tab => tab.stateName === stateName);

	/* @ngInject */
	const setTabInfo = ($state, $state$) => {
		const { name } = $state$;
		const tab = findTabByStateName(name);
		$state.tab = tab;
		$state.selectedIndexTab = tab.index;
	};

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: BASE_STATE,
			url: '/pesquisa_resultado',
			template: '<ui-view />',
			resolve: {
				navigator: createNavigator(BASE_STATE)
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.list',
			url: '/list',
			templateUrl: 'pdi.list.html',
			controller: 'PDIListCtrl',
			controllerAs: controllerAlias,
			resolve: {
				funcionarios: listFuncionarios,
				roles: listRoles,
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.relatorio',
			url: '/relatorio/{objectId:int}',
			templateUrl: 'pdi.relatorio.individual.html',
			controller: 'PDIAcaoRelatorioCtrl',
			controllerAs: controllerAlias,
			resolve: {
				roles: listRoles
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.acoes',
			url: '/{objectId:int}/acoes',
			templateUrl: 'pdi.acao.list.html',
			controller: 'PDIAcaoListCtrl',
			onEnter: loadPDIOnEnter,
			controllerAs: controllerAlias,
			resolve: {
				user: loggedUser,
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.acoes.em_aprovacao',
			url: '/em_aprovacao',
			templateUrl: 'em.aprovacao.html',
			onEnter: setTabInfo,
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.acoes.aprovado',
			url: '/aprovado',
			templateUrl: 'aprovado.html',
			onEnter: setTabInfo,
		})
		.state({
			moduleLoader,
			stateName:  BASE_STATE + '.acoes.em_andamento',
			url: '/em_andamento',
			templateUrl: 'em.andamento.html',
			onEnter: setTabInfo,
		})

		.state({
			moduleLoader,
			stateName: BASE_STATE + '.acoes.finalizado',
			url: '/finalizado',
			templateUrl: 'finalizado.html',
			onEnter: setTabInfo,
			/* resolve: {
				roles: listRoles
			} */
		});
}
