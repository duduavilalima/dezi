import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.configuracao', [ngResource])
	.factory('ConfiguracaoResource', configuracaoFactory)
	.name;

function configuracaoFactory($resource) {
	return $resource('${restServer}/configuracao/:id', null, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/configuracao/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/configuracao/search',
			isArray: true
		},
		persistirLinguagem: {
			method: 'POST',
			url: '${restServer}/configuracao/persistirlinguagem'
		},
		persistirFonte: {
			method: 'POST',
			url: '${restServer}/configuracao/persistirfonte'
		},
		configuracaoDoFuncionario: {
			method: 'POST',
			url: '${restServer}/configuracao/configuracaodofuncionario'
		}
	});
}
