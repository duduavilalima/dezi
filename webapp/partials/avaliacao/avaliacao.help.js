// import EnjoyHintHelper from 'utils/enjoyhint.utils';

// const camposBase = {
// 	editar: {
// 		query: '.md-button.avd-botao-editar:visible',
// 		descricao: 'Inicia a edição da avaliação.'
// 	},
// 	salvar: {
// 		query: '.md-button.avd-botao-salvar:visible',
// 		descricao: 'Salvar as alterações na avaliação.',
// 	},
// 	cancelar: {
// 		query: '.md-button.avd-botao-cancelar:visible',
// 		descricao: 'Cancelar a avaliação e voltar para a campanha.',
// 	},
// 	proximo: {
// 		query: '.md-button.avd-botao-proximo:visible',
// 		descricao: 'Ir para a próxima aba do cadastro.',
// 	},
// 	voltar: {
// 		query: '.md-button.avd-botao-voltar:visible',
// 		descricao: 'Voltar para a campanha.'
// 	}
// };

// const camposAjuda = {
// 	nome: {
// 		query: '#av_gestor',
// 		descricao: 'Nome da avaliação.<br/>Este será apresentado no momento do preenchimento do usuário'
// 	},
// 	situacao: {
// 		query: '#situacao',
// 		descricao:
// 			'Situação da avaliação. Inicialmente a situação<br/> é "Aguardando liberação", onde são selecionados<br/>' +
// 			' os participantes. Neste momento os usuários não<br/> poderão visualizar a avaliação.'
// 	},
// 	perfis: {
// 		query: '#perfis',
// 		descricao:
// 			'Perfis dos avaliadores da avaliação.<br/>' +
// 			'Gestores: Usuário "Avaliador" de um participante.<br/>' +
// 			'Subordinados: Usuário ao qual o participante é "Avaliador".<br/>' +
// 			'Pares: Qualquer usuário adicionado pelo RH.'
// 	},
// 	dataAv2: {
// 		query: '#dataAv2',
// 		descricao: 'Data limite da avaliação.'
// 	},
// 	autoAvaliacao: {
// 		query: '#autoavaliacao',
// 		descricao: 'A avaliação do usuário, obrigatóriamente terá consenso.'
// 	},
// 	dataAv1: {
// 		query: '#dataAv1',
// 		descricao: 'Data limite da autoavaliação.',
// 	},
// 	consenso: {
// 		query: '#consenso',
// 		descricao: 'Avaliação entre o usuário e seu gestor. Onde serão confrontadas as avaliações do gestor e usuário',
// 	},
// 	dataAv3: {
// 		query: '#dataAv3',
// 		descricao: 'Data limite do consenso.',
// 	},
// 	revisao: {
// 		query: '#revisao',
// 		descricao: 'Responsável pela revisão da avaliação de consenso.',
// 	},
// 	dataAv4: {
// 		query: '#dataAv4',
// 		descricao: 'Data limite da revisão.',
// 	},
// 	planejamento: {
// 		query: '#planejamento',
// 		descricao: 'Área onde serão adicionados os tipos de competências para a avaliação. Os tipos podem ser por perfil.',
// 	},
// 	adicionar: {
// 		query: '#adicionar',
// 		descricao: 'Adicionar tipo de competência.',
// 	},
// 	cancelarResumo: {
// 		query: '#cancelar_resumo',
// 		descricao: 'Cancelar a avaliação e voltar para a campanha.',
// 	},
// 	salvarResumo: {
// 		query: '#salvar_resumo',
// 		descricao:
// 			'Salvar processo da avaliação e ir para a aba de participantes.</br>' +
// 			'Será necessário confirmar, pois o processo de uma avaliação não poderá ser modificado.',
// 		showSkip: false,
// 	}
// };

// const camposAvaliacao = {
// 	nome: {
// 		queries: {
// 			view: '#descricao_abreviado_campanha',
// 			edit: '#av_gestor1',
// 			new: '#av_gestor'
// 		},
// 		descricao: 'Nome da avaliação.<br/>Este será apresentado no momento do preenchimento do usuário.'
// 	},
// 	situacao: {
// 		query: '#situacao',
// 		descricao:
// 			'Situação da avaliação. Inicialmente a situação<br/>' +
// 			'é "Aguardando liberação", onde são selecionados<br/>' +
// 			'os participantes. Neste momento os usuários não<br/>' +
// 			'poderão visualizar a avaliação.'
// 	},
// 	descricao: {
// 		queries: {
// 			view: '#descricao_resumo_view',
// 			edit: '#descricao_resumo_edit',
// 			new: '#descricao_resumo_new'
// 		},
// 		descricao: 'Descrição da campanha.',
// 	},
// 	agentes_datas_limites: {
// 		query: '#agentes_datas_limites',
// 		descricao: 'Indique quem será avaliado nessa campanha e se haverá consenso.'
// 	},
// 	tipo_vs_peso: {
// 		query: '#tipo_vs_peso',
// 		descricao: 'Adicione os tipos de competências e pesos que serão avaliados nessa avaliação.'
// 	},
// 	editar: {
// 		query: '#editar_resumo',
// 		descricao: camposBase.editar.descricao
// 	},
// 	salvar: {
// 		query: '#salvar_resumo',
// 		descricao: camposBase.salvar.descricao,
// 	},
// 	//FIXME Duplicado
// 	// salvar: {
// 	// 	query: '#salvar_proximo',
// 	// 	descricao: 'Salva os dados cadastrados e libera as abas “Avaliados” e “Avaliadores"',
// 	// },
// 	cancelar: {
// 		query: '#cancelar_resumo',
// 		queries: {
// 			new: '#cancelar_resumo_new'
// 		},
// 		descricao: camposBase.cancelar.descricao,
// 	},
// 	proximo: {
// 		query: '#proximo_resumo_new',
// 		descricao: camposBase.proximo.descricao,
// 	},
// 	voltar: {
// 		query: '#voltar_resumo',
// 		queries: {
// 			view: '#voltar_resumo_view',
// 			edit: '#voltar_resumo'
// 		},
// 		descricao: camposBase.voltar.descricao
// 	}
// };

// const camposEtapasDatasLimite = {
// 	perfis: {
// 		query: '#perfis',
// 		queries: {
// 			view: '#perfis_view',
// 			edit: '#perfis_edit'
// 		},
// 		descricao:
// 			'Perfis dos avaliadores da avaliação.<br/>' +
// 			'Gestores: Usuário "Avaliador" de um participante.<br/>' +
// 			'Subordinados: Usuário ao qual o participante é "Avaliador".<br/>' +
// 			'Pares: Qualquer usuário adicionado pelo RH.',
// 		scroll: true
// 	},
// 	dataAv1: {
// 		query: '#dataAv1',
// 		descricao: 'Data limite da autoavaliação.',
// 		scroll: true
// 	},
// 	dataAv2: {
// 		query: '#dataAv2',
// 		descricao: 'Data limite da avaliação.',
// 		scroll: true
// 	},
// 	dataAv3: {
// 		query: '#dataAv3',
// 		descricao: 'Data limite do consenso.',
// 		scroll: true
// 	},
// 	dataAv4: {
// 		query: '#dataAv4',
// 		descricao: 'Data limite da revisão.',
// 		scroll: true
// 	},
// 	autoAvaliacao: {
// 		query: '#autoavaliacao',
// 		queries: {
// 			edit: '#autoavaliacao_edit',
// 			view: '#autoavaliacao_view'
// 		},
// 		descricao: 'A avaliação do usuário, obrigatóriamente terá consenso.',
// 		scroll: true
// 	},
// 	consenso: {
// 		query: '#consenso',
// 		queries: {
// 			edit: '#consenso_edit',
// 			view: '#consenso_view'
// 		},
// 		descricao:
// 			'Avaliação entre o usuário e seu gestor.' +
// 			'Onde serão confrontadas as avaliações do gestor e usuário.',
// 		scroll: true
// 	},
// 	revisao: {
// 		query: '#revisao',
// 		queries: {
// 			edit: '#revisao_edit',
// 			view: '#revisao_view'
// 		},
// 		descricao: 'Responsável pela revisão da avaliação de consenso.',
// 		scroll: true
// 	},
// 	editar: {
// 		query: '#editar_etapas',
// 		descricao: camposBase.editar.descricao
// 	},
// 	salvar: {
// 		query: '#salvar_etapas',
// 		queries: {
// 			new: '#salvar_etapas_new',
// 			edit: '#salvar_etapas_edit'
// 		},
// 		descricao: camposBase.salvar.descricao
// 	},
// 	cancelar: {
// 		query: '#cancelar_etapas',
// 		queries: {
// 			view: '#cancelar_etapas_view',
// 			edit: '#cancelar_etapas_edit'
// 		},
// 		descricao: camposBase.cancelar.descricao
// 	},
// 	proximo: {
// 		query: '#proximo_etapas',
// 		descricao: camposBase.proximo.descricao
// 	},
// 	voltar: {
// 		query: '#voltar_etapas',
// 		queries: {
// 			view: '#voltar_etapas_view'
// 		},
// 		descricao: camposBase.voltar.descricao
// 	}
// };

// const camposAgentesConteudoPeso = {
// 	adicionar: {
// 		query: '#adicionar_agentes',
// 		queries: {
// 			view: '#adicionar_agentes_view',
// 			edit: '#adicionar_agentes_edit'
// 		},
// 		descricao: 'Adicionar tipo de competência.'
// 	},
// 	editar: {
// 		query: '#editar_agentes',
// 		queries: {
// 			view: '#editar_agentes_view'
// 		},
// 		descricao: camposBase.editar.descricao
// 	},
// 	salvar: {
// 		query: '#salvar_agentes',
// 		queries: {
// 			view: '#salvar_agentes_view',
// 			edit: '#salvar_agentes_edit'
// 		},
// 		descricao:
// 			'Salconst processo da avaliação e ir para a aba de participantes.</br>' +
// 			'Será necessário confirmar, pois o processo de uma avaliação não poderá ser modificado.'
// 	},
// 	cancelar: {
// 		query: '#cancelar_agentes',
// 		queries: {
// 			view: '#cancelar_agentes_view',
// 			edit: '#cancelar_agentes_edit'
// 		},
// 		descricao: camposBase.cancelar.descricao
// 	},
// 	proximo: {
// 		query: '#proximo_agentes',
// 		descricao: camposBase.proximo.descricao
// 	},
// 	voltar: {
// 		query: '#voltar_agentes',
// 		queries: {
// 			view: '#voltar_agentes_view',
// 			edit: '#voltar_agentes_edit'
// 		},
// 		descricao: camposBase.voltar.descricao
// 	}
// };

// //TODO Verificar o contexto desse help para nomear corretamente a função e os campos
// function showAjuda() {
// 	const helper = new EnjoyHintHelper('view', camposAjuda);

// 	helper
// 		.etapa('nome')
// 		.etapa('nome')
// 		.etapa('situacao')
// 		.etapa('perfis')
// 		.etapa('dataAv2')
// 		.etapa('autoAvaliacao')
// 		.etapa('dataAv1')
// 		.etapa('consenso')
// 		.etapa('dataAv3')
// 		.etapa('revisao')
// 		.etapa('dataAv4')
// 		.etapa('planejamento')
// 		.etapa('adicionar')
// 		.etapa('cancelarResumo')
// 		.etapa('salvarResumo');

// 	helper.executar();
// }

// function showAjudaResumo(modo) {
// 	const helper = new EnjoyHintHelper(modo, camposAvaliacao);

// 	helper
// 		.etapa('nome')
// 		.etapa('situacao')
// 		.etapa('descricao')
// 		.etapa('agentes_datas_limites')
// 		.etapa('tipo_vs_peso');

// 	if (modo === 'new') {
// 		helper.etapa('salvar');
// 	} else if (modo === 'edit') {
// 		helper.etapa('salvar');
// 	}

// 	helper.executar();
// }

// function showAjudaEtapasDatasLimite(modo) {
// 	var helper = new EnjoyHintHelper(modo, camposEtapasDatasLimite);

// 	helper
// 		.etapa('perfis')
// 		.etapa('autoAvaliacao')
// 		.etapa('consenso')
// 		.etapa('revisao')
// 		.etapa('dataAv2')
// 		.etapa('dataAv1')
// 		.etapa('dataAv3');

// 	if (modo === 'new') {
// 		helper
// 			.etapa('cancelar')
// 			.etapa('proximo');
// 	} else if (modo === 'edit') {
// 		helper
// 			.etapa('cancelar')
// 			.etapa('salvar');
// 	} else if (modo === 'view') {
// 		helper
// 			.etapa('voltar')
// 			.etapa('editar');
// 	}

// 	helper.executar();
// }

// function showAjudaAgentesConteudoPeso(modo) {
// 	var helper = new EnjoyHintHelper(modo, camposAgentesConteudoPeso);

// 	if (modo === 'edit' || modo === 'new') {
// 		helper.etapa('adicionar');
// 	}

// 	if (modo === 'new') {
// 		helper
// 			.etapa('cancelar')
// 			.etapa('salvar');
// 	} else if (modo === 'edit') {
// 		helper
// 			.etapa('cancelar')
// 			.etapa('salvar');
// 	} else if (modo === 'view') {
// 		helper
// 			.etapa('voltar')
// 			.etapa('editar');
// 	}

// 	helper.executar();
// }

// export {
// 	showAjuda,
// 	showAjudaResumo,
// 	showAjudaEtapasDatasLimite,
// 	showAjudaAgentesConteudoPeso
// };
