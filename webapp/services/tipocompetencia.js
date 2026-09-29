import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.tipocompetencia', [ngResource])
	.factory('TipoCompetencia', tipoCompetenciaFactory)
	.name;

function tipoCompetenciaFactory($resource) {
	return $resource('${restServer}/tipodecompetencias/:id/:action', { id: '@id' }, {
		save: {
			method: 'POST',
		},
		update: {
			method: 'PUT',
		},
		obterFilhos: {
			method: 'POST',
			params: { action: 'obterfilhos', id: undefined },
			isArray: true
		},
		obterAncestrais: {
			method: 'POST',
			params: { action: 'obterancestrais', id: undefined },
			isArray: true
		},
		obterArvore: {
			method: 'GET',
			params: { action: 'obterarvore' },
			isArray: true
		},
		obterArvorePorId: {
			method: 'GET',
			params: { action: 'obterarvore' },
			isArray: true
		},
		tiposAtivos: {
			method: 'GET',
			params: { action: 'tiposativos' },
			isArray: true
		},
		obterSemPai: {
			method: 'GET',
			params: { action: 'obtertipocompetenciapai' },
			isArray: true
		}
	});
}
