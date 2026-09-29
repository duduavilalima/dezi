// import EnjoyHint from 'lib/enjoyhint/enjoyhint';

// function ajuda() {
// 	//Tive que selecionar uma lista de abas e ir adicionando classes nelas para poder seleciona-las
// 	// const abas = document.querySelectorAll('md-tab-item');
// 	// const aba1 = abas[0].classList.add('competencia');
// 	// const aba2 = abas[1].classList.add('colaborador');

// 	//Tive que selecionar o botão do menu contexto para conseguir abri-lo para mostrar no dezi tour

// 	// var button = document.querySelector('.buttons');
// 	// var texto = button.querySelector('div');
// 	// texto.classList.add('total');
// 	var toolbar = document.querySelector('.user-button');
// 	var menu = document.querySelectorAll('.md-in-menu-bar');
// 	menu[0].classList.add('perfil');
// 	menu[1].classList.add('mensagem');
// 	menu[2].classList.add('redefinir');
// 	menu[3].classList.add('chamado');

// 	var enjoyhint_instance2 = new EnjoyHint({});

// 	var enjoyhint_script_steps_primeiro_acesso = [{
// 		'next #title_av': '<h1 style="font-size: 30px;">Bem Vindo ao DEZI Tour!</h1>Explicaremos como navegar pelo ambiente, preencher '+
// 		'suas avaliações e acompanhar a evolução da campanha. É muito simples e intuitivo.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #title_av': 'Visualize o andamento da campanha e o  preenchimento das avaliações, quantas competências estão sendo avaliadas '+
// 		'e quantas já foram preenchidas por tipo.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .competencia': 'Selecione essa aba para responder às perguntas. Compare as pessoas que você precisa avaliar, inclusive você mesmo.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #lista_competencias': '<div style="width: 200px;">Visualize todas as avaliações que foram selecionadas para você preencher.</div>',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #lista_competencias': '<div style="width: 200px;">Selecione a avaliação que você deseja realizar pressionando o botão na coluna preencher.</div>',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .perfil': 'Clique na sua foto ou no seu nome para acessar seus dados pessoais registrados no Departamento Pessoal assim como '+
// 		'avaliações já realizadas.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: function () {
// 			$('html,#content').animate({
// 				scrollTop: $('#user-menu').offset().top
// 			}, 400);
// 			toolbar.click();
// 		}
// 	}, {
// 		'next .mensagem': 'Confira sua caixa de mensagens enviadas pelos participantes da campanha.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .redefinir': 'Você pode redefinir sua senha a qualquer momento. Um link será enviado para seu e-mail para realizar a redefinição.',
// 		'nextButton': {
// 			className: 'myNext next',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip skip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .chamado': 'Dúvidas, críticas e sugestões. Esse é o espaço para você mandar sua mensagem.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #lc_chat_header': 'Converse online com o suporte DEZI e tire dúvidas rapidamente. O atendimento é de 9h às 18h, de segunda à sexta.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		'showSkip': true,
// 		'showNext': false
// 	}];

// 	var enjoyhint_script_steps = [{
// 		'next #title_av': 'Visualize o andamento da campanha e o preenchimento das avaliações, quantas competências estão sendo avaliadas e quantas '+
// 		'já foram preenchidas por tipo.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .competencia': 'Selecione essa aba para responder às perguntas. Compare as pessoas que você precisa avaliar, inclusive você mesmo.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #lista_competencias': '<div style="width: 200px;">Visualize todas as avaliações que foram selecionadas para você preencher.</div>',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #lista_competencias': '<div style="width: 200px;">Selecione a avaliação que você deseja realizar pressionando o botão na coluna preencher'+
// 		'.</div>',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		'showSkip': false,
// 		'showNext': false
// 	}];

// 	const primeiro_acesso = JSON.parse(window.localStorage.getItem('primeiro_acesso'));

// 	if (primeiro_acesso === undefined || primeiro_acesso === null) {
// 		window.localStorage.setItem('primeiro_acesso', true);
// 		enjoyhint_instance2.set(enjoyhint_script_steps_primeiro_acesso);
// 	} else {
// 		enjoyhint_instance2.set(enjoyhint_script_steps);
// 	}

// 	enjoyhint_instance2.run();
// }

// function ajudaAbaColaboradores() {
// 	var enjoyhint_instance = new EnjoyHint({});

// 	var enjoyhint_script_steps = [{
// 		'next #filtro_pesquisa_funcionarios': 'Área de filtros para pesquisar por avaliações: não preenchida, parcialmente preenchida ou '+
// 		'totalmente preenchida.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #table-list-funcionarios': 'Visualize todas as avaliações que foram selecionadas para você preencher. Selecione a avaliação que '+
// 		'você deseja realizar pressionando o botão na coluna preencher.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		showSkip: false,
// 		showNext: false
// 	}];

// 	enjoyhint_instance.set(enjoyhint_script_steps);
// 	enjoyhint_instance.run();
// }

// function ajudaAbaGestores() {
// 	//Tive que selecionar uma lista de abas para poder mostra-las
// 	var abas = document.querySelectorAll('md-tab-item');
// 	abas[0].classList.add('gestores');
// 	abas[1].classList.add('subordinados');
// 	var enjoyhint_instance = new EnjoyHint({});

// 	var enjoyhint_script_steps = [{
// 		'next #title_competencia': 'Essa é a tela dedicada à realização da reunião para o consenso entre a nota da autoavaliação, e a nota dada '+
// 		'pelo seu gestor.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .gestores': 'Nesta aba você realizará o consenso com seu superior.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #legendas_funcionario': 'Utilize os filtros para visualizar a situação dos consensos.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #table-gestores': 'Inicie o consenso clicando no botão na coluna preencher. O login e senha do outro usuário será solicitado em uma janela.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #table-gestores': '<strong>IMPORTANTE!!!</strong> A avaliação de consenso só pode ser feita quando o avaliado e o avaliador tiverem '+
// 		'concluído suas avaliações. Avaliador e avaliado podem alterar suas avaliações até a data limite.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		showSkip: false,
// 		showNext: false
// 	}];

// 	enjoyhint_instance.set(enjoyhint_script_steps);
// 	enjoyhint_instance.run();
// }

// function ajudaSubordinados() {
// 	//Tive que selecionar uma lista de abas para poder mostra-las
// 	var abas = document.querySelectorAll('md-tab-item');
// 	abas[1].classList.add('subordinados');
// 	var enjoyhint_instance = new EnjoyHint({});

// 	var enjoyhint_script_steps = [{
// 		'next #title_competencia': 'Essa é a tela dedicada à realização da reunião para o consenso entre a nota da autoavaliação, e a nota dada pelo '+
// 		'seu gestor.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next .subordinados': 'Nesta aba você realizará o consenso com seu subordinado.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #legendas_funcionario_subordinados': 'Utilize os filtros para visualizar a situação dos consensos.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #table-funcionarios': 'Inicie o consenso clicando no ícone do lápis na coluna preencher. O login e senha do outro usuário será '+
// 		'solicitado em uma janela.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #table-funcionarios': '<strong>IMPORTANTE!!!</strong> A avaliação de consenso só pode ser feita quando o avaliado e o avaliador '+
// 		'tiverem concluído suas avaliações. Avaliador e avaliado podem alterar suas avaliações até a data limite.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		showSkip: false,
// 		showNext: false
// 	}];

// 	enjoyhint_instance.set(enjoyhint_script_steps);
// 	enjoyhint_instance.run();
// }

// export {
// 	ajuda,
// 	ajudaAbaColaboradores,
// 	ajudaAbaGestores,
// 	ajudaSubordinados
// };
