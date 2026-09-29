import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.contratante', [ngResource])
	.factory('ContratanteResource', contratanteFactory)
	.name;

function contratanteFactory($resource) {
	return $resource('${restServer}/contratante/:id', null, {
		contratanteDoUsuarioLogado: {
			method: 'POST',
			url: '${restServer}/contratante/usuariologado'
		}
	});
}
