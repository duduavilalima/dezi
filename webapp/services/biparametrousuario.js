import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.biparametrousuario', [ngResource])
	.factory('BiParametroUsuario', biparametrousuarioFactory)
	.name;

function biparametrousuarioFactory($resource) {
	return $resource('${restServer}/parametrousuario/:action/:id', null, {
		criarparametros: {
			method: 'GET',
			url: '${restServer}/parametrousuario/criarparametros/:campanhaId/:idFuncionario',
		}
	});
}
