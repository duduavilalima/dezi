import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.parametro', [ngResource])
	.factory('Parametro', parametroFactory)
	.name;

function parametroFactory($resource) {
	return $resource('${restServer}/parametro/:id/:action', null, {
		busca: {
			method: 'POST',
			params: { action: 'busca' },
			isArray: true
		},
		buscaTodos: {
			method: 'GET',
			params: { action: 'buscatodos' },
			isArray: true
		},
		update: {
			method: 'POST',
			params: { action: 'update' }
		}
	});
}
