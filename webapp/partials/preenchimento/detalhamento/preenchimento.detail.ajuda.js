// import EnjoyHint from 'lib/enjoyhint/enjoyhint';

// /* @ngInject */
// export default function ajuda($mdMedia,stage) {
// 	//Tive que pegar o accordion por id para conseguir clicar
// 	const accordion = document.querySelector('#processos');

// 	$('html, #content').animate({
// 		scrollTop: $('#content-container').offset().top
// 	}, 300);

// 	$('html,.fixedCard').animate({
// 		scrollTop: $('#content-container').offset().top
// 	}, 300);

// 	const enjoyhint_instance = new EnjoyHint({
// 		onEnd: () => {
// 			accordion.click();
// 		}
// 	});

// 	const enjoyhint_script_steps_consenso = [{
// 		'next #consenso_avaliacao': 'Nessa tela é realizada a avaliação de consenso.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #prazos': 'Confira o prazo e progresso do processo de avaliação de consenso.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			if ($mdMedia('xs')) {
// 				$('html,#content').animate({
// 					scrollTop: $('#prazos').offset().top
// 				}, 300);
// 			}
// 		}
// 	}, {
// 		'next #acc': 'Essa é a lista de competências para realização do consenso. <br>Veja quantos itens de cada competência que você já' +
// 			'avaliou, e quantos ainda faltam avaliar.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #processos': 'Clique na linha da competência ou no + para visualizar os itens de cada competência.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			accordion.click();
// 		}
// 	}, {
// 		'next #detalhamentos': 'É importante prestar atenção nas cores.' +
// 			'<br>A nota sublinhada de laranja indica a nota dada por você, em verde é a nota dada pelo seu superior.' +
// 			'<br>Clique na opção que representa o consenso.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			$('html,#content').animate({
// 				scrollTop: $('#detalhamentos').offset().top
// 			}, 300);
// 		}
// 	}, {
// 		'next #comentario': '<div style="width: 500px;">O consenso automático é quando o avaliado e gestor avaliam da mesma forma ' +
// 			'(mesma graduação). Neste caso, o sistema já seleciona à graduação e o deixa desabilitado. Se necessário, o gestor poderá preencher um ' +
// 			'comentário, indicando alguma ação de desenvolvimento que o gestor julgue ser importante para o desenvolvimento do avaliado.</div>',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #salvar_detalhamento': 'Você pode salconst e concluir o consenso posteriormente.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		showSkip: false,
// 		showNext: false,
// 		onBeforeStart: () => {
// 			$('html,.fixedCard').animate({
// 				scrollTop: $('#salvar_detalhamento').offset().top
// 			}, 300);
// 		}
// 	}];

// 	const enjoyhint_script_steps_avaliacao = [{
// 		'next #consenso_avaliacao': 'Essa é a tela na qual você preencherá questionário de avaliação por colaborador.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #prazos': 'Confira o prazo e progresso do processo de avaliação.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			if ($mdMedia('xs')) {
// 				$('html,#content').animate({
// 					scrollTop: $('#prazos').offset().top
// 				}, 300);
// 			}
// 		}
// 	}, {
// 		'next #acc': 'Essa é a lista de competências para realização da avaliação.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #processos': 'Clique na linha da competência ou no + para visualizar os itens de cada competência.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			accordion.click();
// 		}
// 	}, {
// 		'next #detalhamentos': 'Clique na opção que representa avaliação.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		},
// 		onBeforeStart: () => {
// 			$('html,#content').animate({
// 				scrollTop: $('#detalhamentos').offset().top
// 			}, 300);
// 		}
// 	}, {
// 		'next #comentario': 'Utilize essa área para escrever comentários que justifiquem sua resposta sobre a competência avaliada. ' +
// 			'O limite é de 500 caracteres.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Próximo'
// 		},
// 		'skipButton': {
// 			className: 'mySkip',
// 			text: 'Fechar'
// 		}
// 	}, {
// 		'next #salvar_detalhamento': 'Você pode salconst e concluir avaliação posteriormente até a data limite.',
// 		'nextButton': {
// 			className: 'myNext',
// 			text: 'Fechar'
// 		},
// 		showSkip: false,
// 		showNext: false,
// 		onBeforeStart: () => {
// 			$('html,.fixedCard').animate({
// 				scrollTop: $('#salvar_detalhamento').offset().top
// 			}, 300);
// 		}
// 	}];

// 	enjoyhint_instance.set(stage === 'av3' ? enjoyhint_script_steps_consenso : enjoyhint_script_steps_avaliacao);
// 	enjoyhint_instance.run();
// }
