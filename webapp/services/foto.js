import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.foto', [ngResource])
	.factory('FotoResource', fotoFactory)
	.name;

function fotoFactory($resource) {
	return $resource('${restServer}/foto/:id/:action', null, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/foto/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/foto/search',
			isArray: true
		},
		avatarFuncionario: {
			method: 'GET',
			params: { action: 'avatar' },
		}
	});
}
