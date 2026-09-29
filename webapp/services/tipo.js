import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.tipo', [ngResource])
	.factory('TipoResource', tipoFactory)
	.name;

function tipoFactory($resource) {
	return $resource('${restServer}/tipo/:action', null, {
		tiposDeSexo: {
			method: 'GET',
			params: { action: 'tiposdesexo' },
			isArray: true
		},
		perfis: {
			method: 'GET',
			params: { action: 'perfis' },
			isArray: true
		},
		tiposCompetencias: {
			method: 'GET',
			params: { action: 'tiposcompetencias' },
			isArray: true
		},
		tiposAgentes: {
			method: 'GET',
			params: { action: 'tiposagentes' },
			isArray: true
		}
	});
}
