import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.mensagem', [ngResource])
	.factory('MensagemService', mensagemServiceFactory)
	.name;

function mensagemServiceFactory($resource) {
	return $resource('${restServer}/mensagem/:action', null, {
		enviarMensagemBoasVindas: {
			method: 'POST',
			params: { action: 'enviarmensagemboasvindas' },
			isArray: true
		}
	});
}
