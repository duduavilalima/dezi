import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.conceito', [ngResource])
	.factory('Conceito', conceitoFactory)
	.name;

function conceitoFactory($resource) {
	return $resource('${restServer}/conceito/:id', null, {
		conceitosDaEmpresaLogada: {
			method: 'GET',
			url: '${restServer}/conceito/conceitosdaempresalogada',
			isArray: true
		}
	});
}
