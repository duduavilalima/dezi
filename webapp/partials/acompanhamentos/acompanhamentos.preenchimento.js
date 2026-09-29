/* @ngInject */
export default function acompanhamentosPreenchimentoController(
	$scope,
	$state,
	$stateParams,
	user,
	AcompanhamentosDialogService,
	avaliacao,
	listaResponsaveis
) {
	const $ctrl = this;

	if ($state.params.tipoAvaliacao == 0) {
		$ctrl.tipoAvaliacao = 'DASHBOARD.WIDGETAUTOAVALIACAO';
		listaResponsaveis = listaResponsaveis.filter(dado => dado.situacaoAuto != 'NAO_APLICAVEL');
	} else if ($state.params.tipoAvaliacao == 1) {
		$ctrl.tipoAvaliacao = 'DASHBOARD.WIDGETAVALIACOES';
		listaResponsaveis =
			listaResponsaveis.filter(dado => (dado.situacaoAvaliacoes != 'NAO_APLICAVEL'
				|| dado.situacaoPares != 'NAO_APLICAVEL'
				|| dado.situacaoSubordinados != 'NAO_APLICAVEL'
			));
	} else if ($state.params.tipoAvaliacao == 2) {
		$ctrl.tipoAvaliacao = 'DASHBOARD.WIDGETCONSENSO';
		listaResponsaveis = listaResponsaveis.filter(dado => dado.situacaoConsenso != 'NAO_APLICAVEL');
	}

	$ctrl.avaliacao = avaliacao;

	$ctrl.apenasPendencias = false;

	const filtroMostrar = d => !$ctrl.apenasPendencias ||
		((d.totalAuto > 0 && d.situacaoAuto != 'TOTALMENTE_PREENCH' && d.situacaoAuto != 'NAO_APLICAVEL')
			|| (d.totalAvaliacoes > 0 && d.situacaoAvaliacoes != 'TOTALMENTE_PREENCH' && d.situacaoAvaliacoes != 'NAO_APLICAVEL')
			|| (d.totalConsenso > 0 && d.situacaoConsenso != 'TOTALMENTE_PREENCH' && d.situacaoConsenso != 'NAO_APLICAVEL'));

	const calcSituacao = (total, preench) => {
		if (total == 0) return 'NAO_APLICAVEL';
		if (total == preench) return 'TOTALMENTE_PREENCH';
		if (preench == 0) return 'NAO_PREENCH';
		return 'PARCIALMENTE_PREENCH';
	};

	const responsaveis = listaResponsaveis
		.reduce((result, dado) => {
			let dadoResponsavel = result.find(d => (d.responsavelId == dado.responsavelId));

			if (!dadoResponsavel) {
				dadoResponsavel = { ...dado };
				delete dadoResponsavel.avaliado;
				delete dadoResponsavel.avaliadoId;
				result.push(dadoResponsavel);
			} else {
				dadoResponsavel.totalAuto += dado.totalAuto;
				dadoResponsavel.totalAvaliacoes += dado.totalAvaliacoes;
				dadoResponsavel.totalConsenso += dado.totalConsenso;
				dadoResponsavel.preenchAuto += dado.preenchAuto;
				dadoResponsavel.preenchAvaliacoes += dado.preenchAvaliacoes;
				dadoResponsavel.preenchConsenso += dado.preenchConsenso;
				dadoResponsavel.preenchPares += dado.preenchPares;
				dadoResponsavel.totalPares += dado.totalPares;
				dadoResponsavel.preenchSubordinados += dado.preenchSubordinados;
				dadoResponsavel.totalSubordinados += dado.totalSubordinados;
				dadoResponsavel.emailResponsavel = dado.emailResponsavel;
				dadoResponsavel.emailAvaliado = dado.emailAvaliado;
			}

			return result;
		}, []);

	responsaveis.forEach(dado => {
		dado.situacaoAuto = calcSituacao(dado.totalAuto, dado.preenchAuto);
		dado.situacaoAvaliacoes = calcSituacao(dado.totalAvaliacoes, dado.preenchAvaliacoes);
		dado.situacaoConsenso = calcSituacao(dado.totalConsenso, dado.preenchConsenso);
	});

	$ctrl.responsaveisFiltrados = responsaveis;

	$ctrl.filterList = () => {
		$ctrl.responsaveisFiltrados = responsaveis.filter(filtroMostrar);
	};

	$ctrl.filter = undefined;

	$ctrl.conf = { limit: 10 };

	const fieldSituacaoConf = {
		class: (object, { field }) => {
			return {
				'green-bg': object[field.name] === 'TOTALMENTE_PREENCH',
				'blue-bg': object[field.name] === 'PARCIALMENTE_PREENCH',
				'red-bg' : object[field.name] === 'NAO_PREENCH',
				estilo_situacao: object[field.name] != 'NAO_APLICAVEL',
				clickable: !!field.click & object[field.name] != 'NAO_APLICAVEL'
			};
		},
		compact: false,
		value: (object, { field }) => 'PREENCHIMENTO.' + object[field.name],
		translate: true,
		align: 'center'
	};

	$ctrl.fields = [
		{
			name: 'responsavel',
			label: 'Colaborador',
			width: '30%',
			link: (item) => {
				AcompanhamentosDialogService.showAvaliacoes(item, listaResponsaveis);
			}
		},
		{
			label: 'Setor',
			name: 'departamentoResponsavel',
			width: '30%'
		},
		{
			label: 'Cargo',
			name: 'cargoResponsavel',
			width: '20%'
		}
	];

	if ($state.params.tipoAvaliacao == 0) {
		$ctrl.fields.push(
			{
				...fieldSituacaoConf,
				label: 'Situação',
				name: 'situacaoAuto'
			}
		);
	} else if ($state.params.tipoAvaliacao == 1){
		$ctrl.fields.push(
			{
				...fieldSituacaoConf,
				label: 'Situação',
				name: 'situacaoAvaliacoes'
			}
		);
	} else if ($state.params.tipoAvaliacao == 2) {
		$ctrl.fields.push(
			{
				...fieldSituacaoConf,
				label: 'Situação',
				name: 'situacaoConsenso',
				click: (item) => $ctrl.exibirAvaliados(item),
				hint: () => 'DASHBOARD.OPCAO_AVALIADOS'
			}
		);
	}

	$ctrl.fields.push(
		{
			name: 'iconHeader',
			label: 'Notificar todos',
			width: '5%',
			ordenar: false,
			icon: 'icon-email',
			link: (resp) => $ctrl.enviarMsg(resp),
			hint: () => 'DASHBOARD.OPCAO_ENVIARMSG',
			iconHeader: 'icon-email',
			hintHeader: () => 'DASHBOARD.NOTIFICAR',
			linkHeader: () => $ctrl.enviarMsgTodos(),
			align : 'center'
		}
	);

	$ctrl.back = () => $state.go('main.acompanhamentos.view', $stateParams);

	$ctrl.enviarMsg = (item) => {
		let destinatarios = [];

		const criaObj = i => { return { id: i.responsavelId, nome: i.responsavel, emailFuncional: i.emailResponsavel }; };

		if (!Array.isArray(item)) {
			destinatarios.push(criaObj(item));
		} else {
			destinatarios = item.map(
				preench => criaObj(preench)
			);
		}

		AcompanhamentosDialogService.enviarMensagem(
			$scope,
			destinatarios,
			user,
		);
	};

	$ctrl.enviarMsgTodos = () => {
		$ctrl.enviarMsg($ctrl.responsaveisFiltrados);
	};

	$ctrl.exibirAvaliados = (item) => {
		const listaAvaliados = listaResponsaveis
			.filter(r => r.totalConsenso > 0 && r.responsavelId == item.responsavelId)
			.reduce((result, dado) => {
				let dadoAvaliado = result.find(d => d.id == dado.id);

				if (!dadoAvaliado) {
					dadoAvaliado = {
						id: dado.avaliadoId,
						nome: dado.avaliado,
						totalConsenso: dado.totalConsenso,
						preenchConsenso: dado.preenchConsenso
					};
					result.push(dadoAvaliado);
				} else {
					dadoAvaliado.totalConsenso += dado.totalConsenso;
					dadoAvaliado.preenchConsenso += dado.preenchConsenso;
				}

				return result;
			}, [])
			.sort((d1, d2) => d1.nome.localeCompare(d2.nome));

		listaAvaliados.forEach(dado => dado.percentual = Math.round(100.0 * dado.preenchConsenso / dado.totalConsenso) + '%');

		AcompanhamentosDialogService.showAvaliados(listaAvaliados);
	};
}
