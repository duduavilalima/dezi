import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.empresa', [ngResource])
	.factory('Empresa', empresaFactory)
	.name;

function empresaFactory($resource) {
	return $resource('${restServer}/empresa/:id', null, {
		contratanteDoUsuarioLogado: {
			method: 'GET',
			url: '${restServer}/empresa/contratantedousuariologado',
			isArray: true
		},
		contratanteDoUsuario: {
			method: 'POST',
			url: '${restServer}/empresa/contratantedousuario',
			isArray: true
		}
	});
}
