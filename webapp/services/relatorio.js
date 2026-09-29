import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.relatorio', [ngResource])
	.factory('Relatorio', relatorioFactory)
	.name;

function relatorioFactory($resource) {
	return $resource('${restServer}/relatorio/:id', null, {
		relatorioAutoAvaliacao: {
			method: 'GET',
			url: '${restServer}/relatorio/autoavaliacao/:id'
		},
		relatorioAvaliacao: {
			method: 'GET',
			url: '${restServer}/relatorio/avaliacao/:id'
		},
		relatorioAvaliacaoConsenso: {
			method: 'GET',
			url: '${restServer}/relatorio/avaliacaoconsenso/:id'
		}
	});
}
