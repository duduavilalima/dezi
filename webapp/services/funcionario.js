import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.funcionario', [ngResource])
	.factory('Funcionario', funcionarioFactory)
	.factory('FuncionarioFormacao', funcionarioFormacaoFactory)
	.factory('FuncionarioIdioma', funcionarioIdiomaFactory)
	.name;

function funcionarioFactory($resource) {
	return $resource('${restServer}/funcionario/:id/:subentity', null, {
		all: {
			method: 'GET',
			url: '${restServer}/funcionario/all',
			isArray: true,
			timeout: 180000,
		},
		update: {
			method: 'PUT',
			timeout: 180000,
			cache: false
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/funcionario/paginar',
			isArray: false,
			timeout: 180000,
			cache: false
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/funcionario/search',
			isArray: true,
			timeout: 180000,
			cache: false
		},
		colaboradoresPDI: {
			method: 'GET',
			url: '${restServer}/funcionario/colaboradorespdi',
			isArray: true,
			timeout: 180000,
			cache: false
		},
		loadColaboradorPDI: {
			method: 'GET',
			url: '${restServer}/funcionario/loadcolaboradorpdi/:id',
			isArray: false,
			timeout: 180000,
			cache: false
		},

		ativar: {
			method: 'POST',
			url: '${restServer}/funcionario/ativar',
			isArray: false,
			timeout: 180000,
			cache: false
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/funcionario/inativar',
			isArray: false,
			timeout: 180000,
			cache: false
		},
		trocarSenha: {
			method: 'POST',
			url: '${restServer}/funcionario/trocarsenha',
			isArray: false,
			timeout: 180000,
			cache: false
		},
		idiomas: {
			method: 'GET',
			params: { subentity: 'idiomas' },
			isArray: true
		},
		formacoes: {
			method: 'GET',
			params: { subentity: 'formacoes' },
			isArray: true
		},
		getSemFiltro: {
			method: 'GET',
			url: '${restServer}/funcionario/getsemfiltro/:id',
			isArray: false
		},
		/* FIXME:  o TO não é usado no backend, refatorar para usar um GET*/
		funcionariosParaAvaliacao: {
			method: 'POST',
			url: '${restServer}/funcionario/funcionariosparaavaliacao',
			isArray: true
		},
		loginDeConsenso: {
			method: 'POST',
			url: '${restServer}/funcionario/loginDeConsenso',
			isArray: false,
			timeout: 180000,
			cache: false
		},
		subordinados: {
			method: 'GET',
			url: '${restServer}/funcionario/:id/subordinados/:idAvaliacao',
			isArray: true
		},
		avaliador: {
			method: 'GET',
			url: '${restServer}/avaliador/:id/avaliador/:idAvaliacao',
			isArray: false
		},
		avaliadorRequest: {
			method: 'GET',
			url: '${restServer}/avaliador/:id/avaliador/:idAvaliacao',
			isArray: false,
			transformResponse: (data, headersGetter, status) => {
				if (status === 200 && data) {
					return { success: true, data: JSON.parse(data) };
				}

				return { success: false };
			}
		}
	});
}

function funcionarioFormacaoFactory($resource) {
	return $resource('${restServer}/funcionarioformacao/:id', null, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/funcionarioformacao/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/funcionarioformacao/search',
			isArray: true
		}
	});
}

function funcionarioIdiomaFactory($resource) {
	return $resource('${restServer}/funcionarioidioma/:id', null, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/funcionarioidioma/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/funcionarioidioma/search',
			isArray: true
		}
	});
}
