/* import EnjoyHint from 'enjoyhint';

const steps = [
	{
		'next #av_gestor': 'Nome da avaliação.<br/>Este será apresentado no momento do preenchimento do usuário',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #situacao':
			'Situação da avaliação. Inicialmente a situação<br/> é "Aguardando liberação", onde são selecionados<br/>' +
			' os participantes. Neste momento os usuários não<br/> poderão visualizar a avaliação.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #perfis': 'Perfis dos avaliadores da avaliação.<br/>' +
			'Gestores: Usuário "Avaliador" de um participante.<br/>' +
			'Subordinados: Usuário ao qual o participante é "Avaliador".<br/>' +
			'Pares: Qualquer usuário adicionado pelo RH.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#perfis').offset().top
			}, 300);
		}
	}, {
		'next #dataAv2': 'Data limite da avaliação.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #autoavaliacao': 'A avaliação do usuário, obrigatóriamente terá consenso.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#autoavaliacao').offset().top
			}, 300);
		}
	}, {
		'next #dataAv1': 'Data limite da autoavaliação.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #consenso':
			'Avaliação entre o usuário e seu gestor. Onde serão confrontadas as avaliações do gestor e usuário',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#consenso').offset().top
			}, 300);
		}
	}, {
		'next #dataAv3': 'Data limite do consenso.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #revisao': 'Responsável pela revisão da avaliação de consenso.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#revisao').offset().top
			}, 300);
		}
	}, {
		'next #dataAv4': 'Data limite da revisão.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #planejamento':
			'Área onde serão adicionados os tipos de competências para a avaliação. Os tipos podem ser por perfil.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#planejamento').offset().top
			}, 300);
		}
	}, {
		'next #adicionar': 'Adicionar tipo de competência.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" }
	}, {
		'next #cancelar_resumo': 'Cancelar a avaliação e voltar para a campanha.',
		'nextButton': { className: "myNext", text: "Próximo" },
		'skipButton': { className: "mySkip", text: "Fechar" },
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#cancelar_resumo').offset().top
			}, 300);
		}
	}, {
		'next #salvar_resumo':
			'Salvar processo da avaliação e ir para a aba de participantes.</br>' +
			'Será necessário confirmar, pois o processo de uma avaliação não poderá ser modificado.',
		'nextButton': { className: "myNext", text: "Fechar" },
		'showSkip': false,
		onBeforeStart: function () {
			$('html,#content').animate({
				scrollTop: $('#salvar_resumo').offset().top
			}, 300);
		}
	}
];

function showHelp() {
	var enjoyhint = new EnjoyHint({});

	enjoyhint.set(steps);
	enjoyhint.run();
}

export default showHelp;
 */