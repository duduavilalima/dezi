import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.dadosPreenchimento', [ngResource])
	.factory('DadosPreenchimento', dadosPreenchimentoFactory)
	.name;

function dadosPreenchimentoFactory($resource) {
	return $resource('${restServer}/dados/campanha/:campanhaId/avaliacao/:avaliacaoId/:action', null, {
		listarResponsaveis: {
			method: 'GET',
			params: { 'action': 'responsaveis' },
			isArray: true
		},
		listarPendencias: {
			method: 'GET',
			params: { 'action': 'pendencias' },
			isArray: true
		},
		totalizar: {
			method: 'GET',
			params: { 'action': 'total' }
		}
	});
}
