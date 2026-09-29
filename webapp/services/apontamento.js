import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.apontamento', [ngResource])
	.factory('ApontamentoFuncionalResource', apontamentoFuncionalFactory)
	.name;

function apontamentoFuncionalFactory($resource) {
	return $resource('${restServer}/apontamentofuncional/:id', null, {
		update: {
			method: 'PUT'
		},
		porParticipante: {
			method: 'POST',
			url: '${restServer}/apontamentofuncional/porparticipante',
			isArray: true
		}
	});
}
