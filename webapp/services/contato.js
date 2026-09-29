import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.contato', [ngResource])
	.factory('Contato', painelControleFactory)
	.name;

function painelControleFactory($resource) {
	return $resource('${restServer}/contato/:id', null, {
		enviar: {
			method: 'POST',
			url: '${restServer}/contato/enviar',
			isArray: false,
			timeout: 180000,
			cache: false
		}
	});
}
