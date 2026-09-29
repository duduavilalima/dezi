import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.modelosgraduacao', [ngResource])
	.factory('Graduacao', graduacaoFactory)
	.factory('ModeloGraduacao', modelosGraduacaoFactory)
	.name;

function graduacaoFactory($resource) {
	return $resource('${restServer}/graduacao/:id/:action', { id: '@id' }, {
		save: {
			method: 'POST',
		},
		update: {
			method: 'PUT',
		},
		graduacoesPorModelo: {
			method: 'POST',
			params: { action: 'graduacoespormodelo' },
			isArray: true
		},
		graduacoesAtivasPorModelo: {
			method: 'POST',
			params: { action: 'graduacoesativaspormodelo' },
			isArray: true
		},
		ativar: {
			method: 'GET',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'GET',
			params: { action: 'inativar' }
		}
	});
}

function modelosGraduacaoFactory($resource) {
	return $resource('${restServer}/modelo/:id/:action', { id: '@id' }, {
		save: {
			method: 'POST',
		},
		update: {
			method: 'PUT',
		},
		buscar: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true
		},
		ativar: {
			method: 'GET',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'GET',
			params: { action: 'inativar' }
		}
	});
}

