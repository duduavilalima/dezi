import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.participante', [ngResource])
	.factory('Participante', participanteFactory)
	.name;

function participanteFactory($resource) {
	return $resource('${restServer}/participante/:id/:action', {
		id: '@id'
	}, {
		all: {
			method: 'GET',
			params: {
				action: 'disponiveis'
			},
			isArray: true
		},
		update: {
			method: 'PUT',
		},
		qbe: {
			method: 'POST',
			params: {
				action: 'paginar'
			},
		},
	});
}
