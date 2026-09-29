import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.preenchimento', [ngResource])
	.factory('Preenchimento', preenchimentoFactory)
	.factory('Participante', participanteFactory)
	.factory('CompetenciaPreenchimento', competenciaPreenchimentoFactory)
	.factory('CompetenciaAvaliador', competenciaAvaliadorFactory)
	.factory('ParticipantesCompetencia', participantesCompetenciaFactory)
	.factory('AvaliacaoCompetencia', avaliacaoCompetenciaFactory)
	.factory('GraduacaoCompetencia', graduacaoCompetenciaFactory)
	.factory('AvaliacaoCompDet', avaliacaoCompDetFactory)
	.name;

function preenchimentoFactory($resource) {
	return $resource('${restServer}/preenchimento/:id', {
		id: '@id'
	}, {
		update: {
			method: 'PUT',
		},
		porParticipante: {
			method: 'GET',
			url: '${restServer}/participante/:participanteId/preenchimentos',
			isArray: true,
		},
	});
}

function participanteFactory($resource) {
	return $resource('${restServer}/participante/:id/:action', {
		id: '@id'
	}, {
		all: {
			method: 'GET',
			params: {
				action: 'disponiveis'
			},
			isArray: true
		},
		update: {
			method: 'PUT',
		},
		qbe: {
			method: 'POST',
			params: {
				action: 'paginar'
			},
		},
	});
}

function competenciaPreenchimentoFactory($resource) {
	return $resource('${restServer}/participante/:participanteId/competencias/:id', {
		id: '@id',
		participanteId: '@participante.id'
	}, {
		porParticipante: {
			method: 'GET',
			isArray: true,
		},
		salvarCompetencias: {
			method: 'POST',
			url: '${restServer}/participante/:participanteId/competencias',
			isArray: true,
		}
	});
}

function competenciaAvaliadorFactory($resource) {
	return $resource('${restServer}/participante/avaliador/:avaliadorId/competencias', {
		avaliadorId: '@avaliador.id'
	}, {
		porAvaliador: {
			method: 'GET',
			isArray: true,
		},
		porAvaliadorEPerfil: {
			method: 'GET',
			url: '${restServer}/participante/avaliador/:avaliadorId/competencias',
			isArray: true,
		}
	});
}

function avaliacaoCompetenciaFactory($resource) {
	return $resource('${restServer}/avaliacaocompetencia/avaliacao/:avaliacaoId/competencia/:competenciaId', {
		avaliacaoId: '@avaliacaoId',
		competenciaId: '@competenciaId'
	}, {
		porAvaliacaoECompetencia: {
			method: 'GET',
			isArray: false,
		}
	});
}

function graduacaoCompetenciaFactory($resource) {
	return $resource('${restServer}/graduacaocompetencia/avaliacaocompetencia/:avaliacaoCompetenciaId/graduacoes', {
		avaliacaoCompetenciaId: '@avaliacaoCompetenciaId'
	}, {
		porAvaliacaoCompetencia: {
			method: 'GET',
			isArray: true,
		}
	});
}

function avaliacaoCompDetFactory($resource) {
	return $resource('${restServer}/avaliacaocompdet/avaliacaocompetencia/:avaliacaoCompetenciaId/avaliador/:avaliadorId/detalhamentos', {
		avaliacaoCompetenciaId: '@avaliacaoCompetenciaId',
		avaliadorId: '@avaliadorId'
	}, {
		porAvaliacaoCompetenciaEAvaliadorEPerfil: {
			method: 'GET',
			isArray: true,
		},
		salvar: {
			method: 'POST',
			url: '${restServer}/participante/saveAvaliacaoCompDet',
		}
	});
}

function participantesCompetenciaFactory($resource) {
	return $resource('${restServer}/participante/avaliacao/:avaliacaoId/avaliador/:avaliadorId/competencia/:competenciaId/perfil/:perfilId', {
		avaliacaoId: '@avaliacaoId',
		avaliadorId: '@avaliadorId',
		competenciaId: '@competenciaId',
		perfilId: '@perfilId'
	}, {
		porAvaliacaoEAvaliadorECompetenciaEPerfil: {
			method: 'GET',
			isArray: true,
		}
	});
}
