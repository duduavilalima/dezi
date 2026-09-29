import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.hostempresa', [ngResource])
	.factory('HostEmpresa', hostEmpresaFactory)
	.name;

function hostEmpresaFactory($resource) {
	return $resource('${restServer}/empresa/:action', null, {
		todas: {
			method: 'GET',
			params: { action: 'todas' },
			isArray: true
		}
	});
}
