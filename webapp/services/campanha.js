import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.campanha', [ngResource])
	.factory('Campanha', campanhaFactory)
	.name;

function campanhaFactory($resource) {
	return $resource('${restServer}/campanha/:id/:action', { id: '@id' }, {
		avaliacoes: {
			method: 'GET',
			params: { action: 'avaliacoes' },
			isArray: true,
		},
		avaliacoesLiberadas: {
			method: 'GET',
			params: { action: 'avaliacoesliberadas' },
			isArray: true,
		},
		campanhasByEmpresa: {
			method: 'GET',
			params: { action: 'campanhasporempresa' },
			isArray: true,
		},
		campanhasByEmpresaPerfil: {
			method: 'GET',
			params: { action: 'campanhasporempresaperfil' },
			isArray: true,
		},
		transition: {
			method: 'PUT',
			params: { action: 'transition' },
			transformRequest: function () {
				//FIXME Para que serve isso?
				return '';
			},
		},
	});
}
