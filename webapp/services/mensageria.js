import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.mensageria', [ngResource])
	.factory('MensageriaResource', mensageriaFactory)
	.name;

function mensageriaFactory($resource) {
	return $resource('${restServer}/mensageria/:action/:id/:filter', null, {
		update: {
			method: 'PUT'
		},
		buscar: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true
		},
		mensagens: {
			method: 'GET',
			params: { action: 'user', filter: 'all' }
		},
		mensagensNaoLidas: {
			method: 'GET',
			params: { action: 'user', filter: 'naolidas' }
		},
		getAllMessagesByAvaliacao: {
			method: 'POST',
			params: { action: 'getallmessagesbyavaliacao' },
			isArray: true
		},
		destinatariosmensagem: {
			method: 'POST',
			params: { action: 'destinatariosmensagem' },
			isArray: true
		},
		getAllMessagesRecebidasByAvaliacao: {
			method: 'POST',
			params: { action: 'getallmessagesrecebidasbyavaliacao' },
			isArray: true
		}
	});
}
