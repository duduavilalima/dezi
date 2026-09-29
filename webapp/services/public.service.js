import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.public', [ngResource])
	.factory('EmpresaPublic', empresaPublicFactory)
	.factory('LoginService', loginServiceFactory)
	.factory('MunicipioPublic', municipioFactory)
	.name;

function empresaPublicFactory($resource) {
	return $resource('${restServer}/auth/:action/:host', null, {
		empresas: {
			method: 'GET',
			params: { action: 'empresas' },
			isArray: true,
			cache: true
		},
	});
}

function loginServiceFactory($resource) {
	return $resource('${restServer}/auth/login', null, {
		login: {
			method: 'POST'
		},
	});
}

function municipioFactory($resource) {
	//TODO: Pegar os endpoints
	return $resource('${restServer}/municipio/:uf/:municipio', null, {
		getUf: {
			method: 'GET',
			isArray: true
		},
		getMunicipoByUf: {
			method: 'GET',
			isArray: true
		}
	});
}
