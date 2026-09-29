import angular from 'angular';
import dadosPreenchimentoService from 'services/dados-preenchimento';
import campanhaService from 'services/campanha';
import departamentoService from 'services/departamento';
import unidadeService from 'services/unidade';
import menuUtils from 'utils/menu.utils';
import stateUtils from 'utils/state.utils';

const BASE_STATE = 'main.acompanhamentos';
const ROLES_VIEW = [ 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_AREA' ];
const ROLES_EDIT = [ 'ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH' ];

export default angular.module('routes.acompanhamentos', [
	stateUtils,
	menuUtils,
	dadosPreenchimentoService,
	campanhaService,
	departamentoService,
	unidadeService
])
	.config(translateConfig)
	.config(routeConfig)
	.run(menuConfig)
	.name;

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'acompanhamentos',
		title: 'Painel de Controle',
		group: false,
		icon: 'icon-apps',
		state: BASE_STATE,
		translate: 'NAV.PAINELDECONTROLE',
		weight: 1,
		roles: [...ROLES_VIEW, ...ROLES_EDIT]
	};

	MenuUtils.addMenu(item);
}

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('acompanhamentos');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./acompanhamentos.module');

	/* @ngInject */
	const listarDepartamentos = (Departamento) => Departamento.departamentosFuncionarioLogado().$promise;

	/* @ngInject */
	const listarCampanhas = (Campanha) => Campanha.all().$promise;

	/* @ngInject */
	const listarResponsaveis = ($stateParams, DadosPreenchimento) =>
		DadosPreenchimento.listarResponsaveis({ ...$stateParams }).$promise;

	/* @ngInject */
	const loadAvaliacao = ($stateParams, Avaliacao) => Avaliacao.get({ id: $stateParams.avaliacaoId }).$promise;

	/* @ngInject */
	const loadDadosChart = ($stateParams, DadosPreenchimento) => DadosPreenchimento.totalizar({ ...$stateParams }).$promise;

	/* @ngInject */
	function loggedUser(UserService) {
		return UserService.logged().$promise;
	}

	$lazyStateProvider
		.state({
			moduleLoader,
			stateName: BASE_STATE,
			url: '/acompanhamentos',
			templateUrl: 'acompanhamentos.html',
			controller: 'AcompanhamentosCtrl',
			controllerAs: '$ctrl',
			resolve: {
				listaDepartamentos: listarDepartamentos,
				listaCampanhas: listarCampanhas
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '.view',
			url: '/{campanhaId:int}/{avaliacaoId:int}?departamentoId&responsavelId',
			templateUrl: 'acompanhamentos.view.html',
			controller: 'AcompanhamentosViewCtrl',
			controllerAs: '$ctrl',
			resolve: {
				dadosChart: loadDadosChart
			}
		})
		.state({
			moduleLoader,
			stateName: BASE_STATE + '_preenchimentos',
			url: '/preenchimentos/{campanhaId:int}/{avaliacaoId:int}?departamentoId?tipoAvaliacao',
			templateUrl: 'acompanhamentos.preenchimento.html',
			controller: 'AcompanhamentosPreenchimentoCtrl',
			controllerAs: '$ctrl',
			resolve: {
				user: loggedUser,
				avaliacao: loadAvaliacao,
				listaResponsaveis: listarResponsaveis
			}
		});
}
