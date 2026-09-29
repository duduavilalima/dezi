import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.competencia', [ngResource])
	.factory('CompetenciaDetalhamento', competenciaDetalhamentoFactory)
	.factory('CompetenciaDetalhamentoCalibrahint', competenciaCalibrahintFactory)
	.factory('Competencia', competenciaFactory)
	.name;

function competenciaDetalhamentoFactory($resource) {
	return $resource('${restServer}/competencia/detalhamento/:action/:id', { id: '@id' }, {
		ativar: {
			method: 'GET',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'GET',
			params: { action: 'inativar' }
		},
		detalhamentosPorCompetencia: {
			method: 'GET',
			params: { action: 'detalhamentosporcompetencia' },
			isArray: true
		},
		obterCompetenciaDetalhamento: {
			method: 'GET',
			params: { action:'toView'}
		}
	});
}

function competenciaCalibrahintFactory($resource) {
	return $resource('${restServer}/competencia/detalhamentocalibrahint/:action/:id', { id: '@id' }, {
		hintspordetalhamentos: {
			method: 'POST',
			params: { action:'hintspordetalhamentos'},
			isArray: true
		}
	});
}

function competenciaFactory($resource) {
	return $resource('${restServer}/competencia/:action/:id/:posAction', { id: '@id' }, {
		ativar: {
			method: 'GET',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'GET',
			params: { action: 'inativar' }
		},
		avaliacaoPorCompetencia : {
			method: 'POST',
			params: { action: 'avaliacoesporcompetencia' },
			isArray: true
		},
		buscarAtivos: {
			method: 'GET',
			params: { action: 'listar' },
			isArray: true
		},
		buscarCompetenciaPorTipoCompetencia: {
			method: 'GET',
			params: { action:'tipo'},
			isArray: true
		},
		obterArvoreDeCompetenciaPorTipoCompetencia: {
			method: 'GET',
			params: { action: 'arvore', id: 'tipocompetencia'},
			isArray: true
		},
		obterArvoreTipoCompetenciaPorCompetencia: {
			method: 'GET',
			params: { action:'arvore'},
			isArray: true
		},
		obterCompetencia: {
			method: 'GET',
			params: { action:'toView'},
		},
		transition: {
			method: 'PUT',
			params: { posAction: 'transition' }
		}
	});
}