import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.situacao', [ngResource])
	.factory('Situacao', situacaoFactory)
	.name;

function situacaoFactory($resource) {
	return $resource('${restServer}/situacao/:action', null, {
		qbe: {
			method: 'POST',
			params: { action: 'page' }
		},
		funcionais: {
			method: 'GET',
			params: { action: 'funcionais' },
			isArray: true
		},
		conclusoes: {
			method: 'GET',
			params: { action: 'conclusoes' },
			isArray: true
		},
		niveis: {
			method: 'GET',
			params: { action: 'niveis' },
			isArray: true
		},
		ativoOrInativo: {
			method: 'GET',
			params: { action: 'ativoorinativo' },
			isArray: true
		}
	});
}
