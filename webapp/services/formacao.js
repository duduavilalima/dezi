import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.formacao', [ngResource])
	.factory('CursoGraduacao', cursoGraduacaoFactory)
	.factory('CursoEspecializacao', cursoEspecializacaoFactory)
	.factory('Escolaridade', escolaridadeFactory)
	.factory('Idioma', idiomaFactory)
	.factory('TipoCurso', tipoCursoFactory)
	.name;

function cursoGraduacaoFactory($resource) {
	return $resource('${restServer}//curso/graduacao/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/curso/graduacao/page'
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/curso/graduacao/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/curso/graduacao/inativar'
		},
		porContratanteUsuarioLogado: {
			method: 'GET',
			url: '${restServer}/curso/graduacao/porcontratanteusuariologado',
			isArray: true
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/curso/graduacao/search',
			isArray: true
		}
	});
}

function cursoEspecializacaoFactory($resource) {
	return $resource('${restServer}/curso/especializacao/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/curso/especializacao/page'
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/curso/especializacao/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/curso/especializacao/inativar'
		},
		porContratanteUsuarioLogado: {
			method: 'GET',
			url: '${restServer}/curso/especializacao/porcontratanteusuariologado',
			isArray: true
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/curso/especializacao/search',
			isArray: true
		}
	});
}

function escolaridadeFactory($resource) {
	return $resource('${restServer}/escolaridade/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/escolaridade/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/escolaridade/search',
			isArray: true
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/escolaridade/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/escolaridade/inativar'
		},
		porContratanteUsuarioLogado: {
			method: 'GET',
			url: '${restServer}/escolaridade/porcontratanteusuariologado',
			isArray: true
		}
	});
}

function idiomaFactory($resource) {
	return $resource('${restServer}/idioma/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/idioma/search',
			isArray: true
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/idioma/page'
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/idioma/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/idioma/inativar'
		},
		idiomasByContratanteAndAtivas: {
			method: 'GET',
			url: '${restServer}/idioma/idiomasbycontratanteandativas',
			isArray: true
		}
	});
}

function tipoCursoFactory($resource) {
	return $resource('${restServer}/tipocurso/:id/:action', { id: '@id' }, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/tipocurso/page'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/tipocurso/search',
			isArray: true
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/tipocurso/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/tipocurso/inativar'
		},
		porContratanteUsuarioLogado: {
			method: 'GET',
			url: '${restServer}/tipocurso/porcontratanteusuariologado',
			isArray: true
		}
	});
}
