import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.acompanhamento', [ngResource])
	.factory('Acompanhamento', acompanhamentoFactory)
	.name;

function acompanhamentoFactory($resource) {
	return $resource('${restServer}/acompanhamento/:action/:id/:type', null, {
		graficoAvaliacoes: {
			method: 'POST',
			params: { action: 'avaliacoes', 'type': 'grafico' }
		},
		graficoConsenso: {
			method: 'POST',
			params: { action: 'consensos', 'type': 'grafico' }
		},
		obterTotalAcompanhamentoAvaliacoes: {
			method: 'POST',
			params: { action: 'avaliacoes' }
		},
		obterTotalAcompanhamentoConsenso: {
			method: 'POST',
			params: { action: 'consensos' }
		},
		dadosAvaliacaoDoAcompanhamento: {
			method: 'POST',
			params: { action: 'avaliacoes', 'type': 'dados' },
			isArray: true
		},
		dadosConsensoDoAcompanhamento: {
			method: 'POST',
			params: { action: 'consensos', 'type': 'dados' },
			isArray: true
		},
		enviarmensagem: {
			method: 'POST',
			params: { action: 'mensagem' }
		}
	});
}
