import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.dimensao', [ngResource])
	.factory('Dimensao', dimensaoFactory)
	.name;

function dimensaoFactory($resource) {
	return $resource('${restServer}/dimensao/:id/:action', {
		id: '@id'
	}, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: {
				'action': 'paginar'
			}
		},
		buscar: {
			method: 'POST',
			params: {
				'action': 'search'
			},
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: {
				'action': 'ativar'
			}
		},
		inativar: {
			method: 'POST',
			params: {
				'action': 'inativar'
			}
		},
		obterArvore: {
			method: 'GET',
			url: '${restServer}/dimensao/obterarvore',
			isArray: true
		},
		obterDetalhamentos: {
			method: 'POST',
			url: '${restServer}/dimensao/detalhamentos',
			isArray: true
		},
		obterdimensaoativa: {
			method: 'GET',
			url: '${restServer}/dimensao/obterdimensaoativa',
			isArray: true
		}
	});
}
