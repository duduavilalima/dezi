import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.pdi', [ngResource])
	.factory('PDI', pdiFactory)
	.factory('PDICompDetal', pdiCompetenciaDetalhamentoFactory)
	.name;

function pdiFactory($resource) {
	return $resource('${restServer}/pdiacao/:id/:action', { id: '@id' }, {
		excluiracao: {
			method: 'POST',
			params: {
				'action': 'excluiracao'
			}
		},
		delete: {
			method: 'DELETE'
		},
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: {
				'action': 'paginar'
			}
		},
		buscar: {
			method: 'POST',
			params: {
				'action': 'search'
			},
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: {
				'action': 'ativar'
			}
		},
		inativar: {
			method: 'POST',
			params: {
				'action': 'inativar'
			}
		},
		lista: {
			method: 'GET',
			params: {
				action: 'list'
			},
			isArray: true,
		},
		listarPorSituacao: {
			method: 'GET',
			params: {
				action: 'list'
			},
			url: '${restServer}/pdiacao/:id/:situacao/:action',
			isArray: true,
		},
		salvarPDI: {
			method: 'POST',
			params: {
				action: 'salvarPDI'
			}
		},
		updatePDI: {
			method: 'PUT',
			params: {
				action: 'updatePDI'
			}
		},
		aprovar: {
			method: 'GET',
			params: {
				action: 'aprovar'
			}
		},
		iniciar: {
			method: 'POST',
			url: '${restServer}/pdiacao/iniciar'
		},
		finalizar: {
			method: 'POST',
			url: '${restServer}/pdiacao/finalizar'
		},
		revisado: {
			method: 'GET',
			params: {
				action: 'revisado'
			}
		},
		filtrarRelatorio: {
			method: 'POST',
			params: {
				action: 'searchRelatorio'
			},
			isArray: true
		},
		exportarRelatorio: {
			method: 'POST',
			url: '${restServer}/pdiacao/exportarRelatorio',
			transformResponse: (data) => {return {responseData: data.toString()};},
			isArray: false
		},
		imprimirRelatorio: {
			method: 'POST',
			url: '${restServer}/pdiacao/imprimirRelatorio',
			transformResponse: (data) => {return {responseData: data.toString()};},
			isArray: false
		}
	});
}

function pdiCompetenciaDetalhamentoFactory($resource) {
	return $resource('${restServer}/pdiacaocompdet/:id/:action', {
		id: '@id'
	}, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: {
				'action': 'paginar'
			}
		},
		buscar: {
			method: 'POST',
			params: {
				'action': 'search'
			},
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: {
				'action': 'ativar'
			}
		},
		inativar: {
			method: 'POST',
			params: {
				'action': 'inativar'
			}
		},
		lista: {
			method: 'GET',
			url: '${restServer}/pdiacaocompdet/:idAvaliado/list',
			isArray: true,
		}
	});
}
