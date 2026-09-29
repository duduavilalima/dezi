//FIXME Não faz sentido centralizar constantes, isso só piora a manutenção. 
//FIXME Remover esse arquivo e componentizar a criação de rotas.
const ROUTES_DASHBOARD = {
	DASHBOARD: {
		name: 'main.dashboard',
	},
};

const ROUTES_PDI = {
	PDI: {
		name: 'main.pdi',
		ACAO_PDI: { 
			name: 'main.acao',
			submenu: 'pdi.pdiacao',
			LIST: { name: 'main.acao.list'},
			ACAO_LIST: { name: 'main.acao.devlist'},
			GERAR_RELATORIO: { name: 'main.acao.gerarrelatorio' }
		},
		RELATORIO: { 
			name: 'main.relatorio',
			submenu: 'pdi.relatorio',
			LIST: { name: 'main.relatorio.list'},
		}
	}
};

const ROUTES_CAMPANHAS = {
	CAMPANHA: {
		name: 'main.campanha',
		NEW: { name: 'main.campanha.new' },
		VIEW: { name: 'main.campanha.view' },
		EDIT: { name: 'main.campanha.edit' },
		LIST: { name: 'main.campanha.list' },
	},
	AVALIACAO: {
		name: 'main.avaliacao',
		NEW: { name: 'main.avaliacao.new' },
		EDIT: { name: 'main.avaliacao.edit' },
		VIEW: {
			name: 'main.avaliacao.view',
			RESUMO: { name: 'main.avaliacao.view.abaResumo' },
			PENDENCIAS: { name: 'main.avaliacao.view.abaPendencias' },
			PARTICIPANTES: { name: 'main.avaliacao.view.abaParticipantes' },
			AVALIADORES: { name: 'main.avaliacao.view.abaAvaliadores' },
			MENSAGENS: { name: 'main.avaliacao.view.abaMensagens' },
		},
		NEW_MENSAGEM: { name: 'main.avaliacao.newMensagem' },
	},
};

const ROUTES_PREENCHIMENTOS = {
	PREENCHIMENTO: {
		name: 'main.preenchimento',
		AVALIACOES: {name: 'main.preenchimento.avaliacoes'},
		CONSENSOS: {name: 'main.preenchimento.consensos'},
		DETAIL: {name: 'main.preenchimento.detail'},
		DETAIL_COMPETENCIA: {name: 'main.preenchimento.detail_competencia'},
	},
};

const ROUTES_CONSENSOS = {
	CONSENSO: {
		name: 'main.consenso',
	},
};

const ROUTES_RELATORIOS = {
	RELATORIOS: {
		name: 'main.relatorios',
		HISTORICOS: {
			name: 'main.historicos',
			submenu: 'relatorios.historicos',
			LIST: {name: 'main.historicos.list'}
		},
		RESULTADOS: {
			name: 'main.resultados',
			submenu: 'relatorios.resultados',
			LIST: {name: 'main.resultados.list'}
		},
	},
};

const ROUTES_CADASTROS = {
	CADASTRO: {
		name: 'main.cadastro',
		DEPARTAMENTOS: {
			name: 'main.departamentos',
			NEW: {name: 'main.departamentos.new'},
			VIEW: {name: 'main.departamentos.view'},
			EDIT: {name: 'main.departamentos.edit'},
			LIST: {name: 'main.departamentos.list'}
		},
		UNIDADES: {
			name: 'main.unidades',
			NEW: {name: 'main.unidades.new'},
			VIEW: {name: 'main.unidades.view'},
			EDIT: {name: 'main.unidades.edit'},
			LIST: {name: 'main.unidades.list'}
		},
		COMPETENCIA: {
			name: 'main.competencias',
			NEW: {name: 'main.competencias.new'},
			VIEW: {name: 'main.competencias.view'},
			EDIT: {name: 'main.competencias.edit'},
			LIST: {name: 'main.competencias.list'}
		},
		FORMACOES_IDIOMAS: {
			name: 'main.formacoesidiomas',
			CURSOSESPEC: {
				name: 'main.cadastro.cursosespecializacao',
				NEW: {name: 'main.cadastro.cursosespecializacao.new'},
				VIEW: {name: 'main.cadastro.cursosespecializacao.view'},
				EDIT: {name: 'main.cadastro.cursosespecializacao.edit'},
				LIST: {name: 'main.cadastro.cursosespecializacao.list'}
			},
			CURSOGRADUACAO: {
				name: 'main.cadastro.cursograduacao',
				NEW: {name: 'main.cadastro.cursograduacao.new'},
				VIEW: {name: 'main.cadastro.cursograduacao.view'},
				EDIT: {name: 'main.cadastro.cursograduacao.edit'},
				LIST: {name: 'main.cadastro.cursograduacao.list'}
			},
			TIPOSCURSO: {
				name: 'main.cadastro.tiposcurso',
				NEW: {name: 'main.cadastro.tiposcurso.new'},
				VIEW: {name: 'main.cadastro.tiposcurso.view'},
				EDIT: {name: 'main.cadastro.tiposcurso.edit'},
				LIST: {name: 'main.cadastro.tiposcurso.list'}
			},
			IDIOMA: {
				name: 'main.cadastro.idioma',
				NEW: {name: 'main.cadastro.idioma.new'},
				VIEW: {name: 'main.cadastro.idioma.view'},
				EDIT: {name: 'main.cadastro.idioma.edit'},
				LIST: {name: 'main.cadastro.idioma.list'}
			},
			ESCOLARIDADE: {
				name: 'main.cadastro.escolaridade',
				NEW: {name: 'main.cadastro.escolaridade.new'},
				VIEW: {name: 'main.cadastro.escolaridade.view'},
				EDIT: {name: 'main.cadastro.escolaridade.edit'},
				LIST: {name: 'main.cadastro.escolaridade.list'}
			}
		}
	},
};

const ROUTES_ADMINISTRACAO = {
	ADMINISTRACAO: {
		name: 'main.administracao',
	},
};

const ROUTES = {
	MAIN: {
		name: 'main',
		...ROUTES_DASHBOARD,
		...ROUTES_CAMPANHAS,
		...ROUTES_PREENCHIMENTOS,
		...ROUTES_CONSENSOS,
		...ROUTES_RELATORIOS,
		...ROUTES_CADASTROS,
		...ROUTES_ADMINISTRACAO,
		...ROUTES_PDI
	}
};

export default ROUTES;
