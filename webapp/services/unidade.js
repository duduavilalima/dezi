import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.unidade', [ngResource])
	.factory('Unidade', unidadeFactory)
	.factory('UnidadeDepartamento', unidadeDepartamentoFactory)
	.name;

function unidadeFactory($resource) {
	return $resource('${restServer}/unidade/:id/:action', { id: '@id' }, {
		obterFilhosDoTipoDeCompetenciaRaiz: {
			method: 'GET',
			params: { action: 'filhos' },
			isArray: true
		},
		obterArvoreDeUnidades: {
			method: 'GET',
			params: { action: 'arvore' },
			isArray: true
		},
		ativas: {
			method: 'GET',
			params: { action: 'listar' },
			isArray: true
		}
	});
}

function unidadeDepartamentoFactory($resource) {
	return $resource('${restServer}/unidadedepartamento/:id/:action', { id: '@id' }, {
		obterDepartamentosUnidadePorUnidade: {
			method: 'GET',
			params: { action: 'departamentos' },
			isArray: true
		}
	});
}
