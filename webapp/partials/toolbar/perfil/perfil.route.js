import angular from 'angular';

import stateUtils from 'utils/state.utils';
import avaliacaoService from 'services/avaliacao';

export default angular
	.module('routes.perfil', [stateUtils, avaliacaoService])
	.config(translateConfig)
	.config(routeConfig)
	.name;

function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('toolbar/perfil');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./perfil.module');

	const data = {
		titulo: 'Perfil',
		modulo: 'Perfil',
		subtitulo: 'Perfil'
	};

	/* @ngInject */
	function createLoaders(
		FotoResource, 
		ConfiguracaoResource,
		FuncionarioIdioma, 
		$rootScope,
		AvaliacaoParticipante, 
		FuncionarioFormacao,
		Funcionario
	) {
		const funcionario =  {...$rootScope.user};

		const loadConfiguracao = (id = funcionario.id) =>ConfiguracaoResource.configuracaoDoFuncionario({id}).$promise;
		const loadFoto = (id = funcionario.id) => FotoResource.buscar({ funcionario: { id } }).$promise;
		const loadAvaliacoes = (id = funcionario.id) => AvaliacaoParticipante.listarAvaliacoesPorAvaliado(id).$promise;
		const loadFormacoes = (id = funcionario.id) => FuncionarioFormacao.buscar({ funcionario: { id } }).$promise;
		const loadIdiomas = (id = funcionario.id) => FuncionarioIdioma.buscar({ funcionario: { id } }).$promise;
		const loadColaborador = (id = funcionario.id) => Funcionario.get({id}).$promise;
		
		return {
			loadFoto,
			loadConfiguracao,
			loadAvaliacoes,
			loadIdiomas,
			loadFormacoes,
			loadColaborador
		};
	}

	/* @ngInject */
	const loadColaborador = (loaders) => loaders.loadColaborador();

	/* @ngInject */
	const loadFormacoes = (loaders) => loaders.loadFormacoes();

	/* @ngInject */
	const loadAvaliacoes = (loaders) => loaders.loadAvaliacoes();

	/* @ngInject */
	const loadIdiomas = (loaders) => loaders.loadIdiomas();

	/* @ngInject */
	const loadFoto = (loaders) => loaders.loadFoto();

	/* @ngInject */
	const loadConfiguracao = (loaders) => loaders.loadConfiguracao();

	$lazyStateProvider
		.state({
			moduleLoader,
			stateName: 'main.perfil',
			url: '/perfil',
			templateUrl: 'perfil.html',
			controller: 'PerfilCadastroCtrl',
			controllerAs: '$ctrl',
			data,
			resolve: {
				loaders: createLoaders,
				foto: loadFoto,
				configuracao: loadConfiguracao,
				formacoesList: loadFormacoes,
				avaliacoesList: loadAvaliacoes,
				idiomasList: loadIdiomas,
				colaborador: loadColaborador
			}
		});
}