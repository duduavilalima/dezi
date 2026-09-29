import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.cargo', [ngResource])
	.factory('Cargo', cargoFactory)
	.factory('CBO', cboFactory)
	.factory('CargoTipoCompetencia', cargoTipoCompetenciaFactory)
	.factory('CargoCompetencia', cargoCompetenciaFactory)
	.factory('DetalhamentoCargoCompetencia', detalhamentoCargoCompetenciaFactory)
	.name;

function cargoFactory($resource) {
	return $resource('${restServer}/cargo/:id/:action', { id: '@id' }, {
		cargosPorEmpresaUsuarioLogado: {
			method: 'GET',
			params: { action: 'cargosporempresausuariologado' },
			isArray: true
		},
		cargosCompetencias: {
			method: 'GET',
			params: { action: 'cargoscompetencias' },
			isArray: true
		},
		funcionariosAtivos: {
			method: 'GET',
			params: { 'action': 'funcionarios' },
			isArray: true
		},
		listaDeHierarquiaDeCargos: {
			method: 'GET',
			params: { 'action': 'hierarquia' },
			isArray: true
		}
	});
}

function cboFactory($resource) {
	return $resource('${restServer}/cbo/:action/:id/:subentity', { id: '@id' }, {
		buscar: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true,
			cache: true
		},
		ocupacoes: {
			method: 'GET',
			params: { action: 'ocupacoes' },
			isArray: true,
			cache: true
		},
		perfis: {
			method: 'GET',
			params: { action: 'ocupacao', subentity: 'perfis' },
			isArray: true,
			cache: true
		},
		atividades: {
			method: 'GET',
			params: { action: 'perfil', subentity: 'atividades' },
			isArray: true,
			cache: true
		}
	});
}

function cargoTipoCompetenciaFactory($resource) {
	return $resource('${restServer}/cargo/tipocompetencia/:action/:id', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: { action: 'page' }
		},
		buscar: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'POST',
			params: { action: 'inativar' }
		},
		porcargo: {
			method: 'GET',
			params: { action: 'porcargo' },
			isArray: true
		},
		buscartodosAtivos: {
			method: 'GET',
			params: { action: 'buscartodos' },
			isArray: true
		}
	});
}

function cargoCompetenciaFactory($resource) {
	return $resource('${restServer}/cargo/competencia/:id/:action/:tipo', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: { action: 'page' }
		},
		buscar: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: { action: 'ativar' }
		},
		inativar: {
			method: 'POST',
			params: { action: 'inativar' }
		},
		cargoCompetenciaPorTipoCompetencia: {
			method: 'GET',
			params: { action: 'tipo' },
			isArray: true
		}
	});
}

function detalhamentoCargoCompetenciaFactory($resource) {
	return $resource('${restServer}/cargoCompetencia/detalhamento/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: { action: 'detalhamento/page'}
		},
		buscar: {
			method: 'POST',
			params: { action: 'detalhamento/search'},
			isArray: true
		},
		ativar: {
			method: 'POST',
			params: { action: 'detalhamento/ativar'}
		},
		inativar: {
			method: 'POST',
			params: { action: 'detalhamento/inativar'}
		},
		detalhamentosPorCargoCompetencia: {
			method: 'GET',
			url: '${restServer}/cargoCompetencia/detalhamento/detalhamentosPorCargoCompetencia/:competenciaId/:cargoCompetenciaId',
			isArray: true
		},
		detalhamentosPorCompetencia: {
			method: 'GET',
			url: '${restServer}/cargoCompetencia/detalhamento/detalhamentosPorCompetencia/:competenciaId/:cargoCompetenciaId',
			isArray: true
		},
		salvarTudo: {
			method: 'POST',
			params: { action: 'saveAllDetalhamentos'},
			isArray: true
		}
	});
}

