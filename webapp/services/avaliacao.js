import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.avaliacao', [ngResource])
	.factory('Avaliacao', avaliacaoFactory)
	.factory('ChartAvaliacao', chartAvaliacaoFactory)
	.factory('AvaliacaoParticipante', avaliacaoParticipanteResource)
	.factory('Pendencia', pendenciafactory)
	.name;

function avaliacaoFactory($resource) {
	return $resource('${restServer}/avaliacao/:id/:action', {
		id: '@id'
	}, {
		copiar: {
			method: 'POST',
			//				params: { action: 'copiar' }
			// FIXME: Chamada acima não funciona.
			url: '${restServer}/avaliacao/copiar',
		},
		salvarParticipantes: {
			method: 'POST',
			//params: {
			//	action: 'salvarparticipantes'
			//}
			url: '${restServer}/avaliacao/salvarparticipantes',
		},
		salvarAvaliadores: {
			method: 'POST',
			//params: {
			//	action: 'salvaravaliadores'
			//}
			// FIXME: Chamada acima não funciona.
			url: '${restServer}/avaliacao/salvaravaliadores',
		},
		enviarMensagem: {
			method: 'POST',
			params: { action: 'mensagens' },
			isArray: true
		},
		liberarAvaliacao: {
			method: 'POST',
			// params: { action: 'liberar' }
			// FIXME: Chamada acima não funciona.
			url: '${restServer}/avaliacao/liberar'
		},
		ajustarpesos: {
			method: 'POST',
			params: { action: 'ajustarpesos' },
		},
		avaliacoesPorCampanha: {
			method: 'POST',
			params: {
				action: 'avaliacoesporcampanha'
			},
			isArray: true,
		},
		cancelar: {
			method: 'POST',
			params: {
				action: 'cancelar'
			}
		},
		porCampanha: {
			method: 'GET',
			url: '${restServer}/campanha/:campanhaId/avaliacoes',
			isArray: true,
		},
		removerAvaliado: {
			method: 'POST',
			params: {
				action: 'removeravaliado'
			}
		},
		removerTodosParticipantes: {
			method: 'POST',
			params: {
				action: 'removertodosparticipantes'
			}
		},
		removeragliberacao: {
			method: 'GET',
			params: {
				action: 'removeragliberacao'
			}
		},
		salvarPares: {
			method: 'POST',
			params: {
				action: 'salvarpares'
			}
		},
		uploadPares: {
			method: 'POST',
			headers: {
				'Content-Type': undefined,
			},
			transformRequest: angular.identity,
			url: '${restServer}/avaliacao/upload',
			isArray: true
		},
		obterAvaliacoesPDI: {
			method: 'GET',
			url: '${restServer}/avaliacao/pdi',
			isArray: true
		}
	});
}

function chartAvaliacaoFactory($resource) {
	return $resource('${restServer}/avaliacao/:id/chart/:tipo', null, {
		percentual: {
			method: 'GET',
			params: {
				tipo: 'percentual'
			},
		},
		chartRadarByAvaliacao: {
			method: 'POST',
			params: {
				tipo: 'chartradarbyavaliacao'
			},
			isArray: true,
		},
		chartRadarAvaliacaoByColaborador: {
			method: 'POST',
			params: {
				tipo: 'chartradaravaliacaobycolaborador'
			},
			isArray: true,
		},
		chartRadarAvaliacaoByCargo: {
			method: 'POST',
			params: {
				tipo: 'chartradaravaliacaobycargo'
			},
			isArray: true,
		},
		graficoPreenchimentos: {
			method: 'GET',
			params: {
				tipo: 'graficopreenchimentos'
			},
		},
		graficoLinePreenchimentos: {
			method: 'GET',
			params: {
				tipo: 'graficolinepreenchimentos'
			},
		},
		graficoLinePreenchimentosPorPeriodo: {
			method: 'GET',
			params: {
				tipo: 'graficolinepreenchimentosporperiodo'
			},
		}
	});
}

function avaliacaoParticipanteResource($resource) {
	return $resource('${restServer}/avaliacaoparticipante/:id', null, {
		update: {
			method: 'PUT'
		},
		qbe: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/paginar'
		},
		buscar: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/search',
			isArray: true
		},
		ativar: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/ativar'
		},
		inativar: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/inativar'
		},
		saveAll: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/saveAll',
			isArray: true
		},
		atualizaAvaliadorAndRevisor: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/atualizaavaliadorandrevisor'
		},
		liberarAvaliacaoConsenso: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/liberaavaliacaoconsenso'
		},
		listarAvaliacoesPorAvaliado: {
			method: 'POST',
			url: '${restServer}/avaliacaoparticipante/listaravaliacoesporavaliado',
			isArray: true
		},
		porAvaliadoAndAvaliacao: {
			method: 'GET',
			url: '${restServer}/avaliacaoparticipante/avaliado/:avaliadoId/avaliacao/:avaliacaoId/participantes',
			isArray: true
		},
		porPerfisEAvaliacao: {
			method: 'GET',
			url: '${restServer}/avaliacaoparticipante/avaliacao/:avaliacaoId/participantes',
			isArray: true
		},
		PDI: {
			method: 'GET',
			url: '${restServer}/avaliacaoparticipante/pdi/:participanteId/participante',
			isArray: true
		}
	});
}

function pendenciafactory($resource) {
	return $resource('${restServer}/pendencia/:id', null, {
		update: {
			method: 'PUT'
		},
		delete: {
			method: 'DELETE',
		},
		qbe: {
			method: 'POST',
			params: {
				action: 'page'
			}
		},
		buscar: {
			method: 'POST',
			params: {
				action: 'search'
			},
			isArray: true
		}
	});
}
