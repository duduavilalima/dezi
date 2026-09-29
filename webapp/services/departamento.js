import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.departamento', [ngResource])
	.factory('Departamento', departamentoFactory)
	.factory('DepartamentoGestor', departamentoGestorFactory)
	.name;

function departamentoFactory($resource) {
	return $resource('${restServer}/departamento/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			params: { action: 'paginar' }
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
		funcionariosAtivosDoDepartamento: {
			method: 'GET',
			params: { action: 'funcionarios' },
			isArray: true
		},
		cargocompetencia: {
			method: 'GET',
			params: { action: 'cargocompetencia' },
			isArray: true
		},
		listarArvoreDeHierarquiaDeDepartamentos: {
			method: 'GET',
			params: { action: 'arvore' },
			isArray: true
		},
		departamentosFilhos: {
			method: 'GET',
			params: { action: 'arvorefilhos' },
			isArray: true
		},
		departamentosDaEmpresaLogada: {
			method: 'GET',
			params: { action: 'departamentosdaempresalogada' },
			isArray: true
		},
		departamentosFuncionarioLogado: {
			method: 'GET',
			params: { action: 'departamentosfuncionariologado' },
			isArray: true
		}
	});
}

function departamentoGestorFactory($resource) {
	return $resource('${restServer}/departamento/:id/:action', { id: '@id' }, {
		relacionarDepartamentosGestor: {
			method: 'POST',
			url: '${restServer}/departamentogestor/relacionar',
		},
	});
}
