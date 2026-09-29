import angular from 'angular';
import validarData from '../util/validarDataAvaliacao';
import ROUTES from 'utils/routes';
import dialogDimensaoCtrl from '../dialogs/dialog.adicionar.dimensao';
import dialogTipoCompetenciaCtrl from '../dialogs/dialog.add.tipo.competencia';
import '../dialogs/dialog.add.tipo.competencia.html';
import '../dialogs/dialog.adicionar.dimensao.html';

const STATE_CAMPANHA_VIEW = ROUTES.MAIN.CAMPANHA.VIEW.name;
const STATE_CAMPANHA_LIST = ROUTES.MAIN.CAMPANHA.LIST.name;
const STATE_VIEW = ROUTES.MAIN.AVALIACAO.VIEW.name;

/* @ngInject */
export default function avaliacaoViewCtrl(
	$state,
	$scope,
	$translate,
	$mdDialog,
	TipoResource,
	ValidationService,
	DialogDetalhes,
	DialogConfirm,
	Avaliacao,
	Campanha
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	const errosPreenchimento = 'A soma do total deve ser igual a 100%.';
	$ctrl.dataAtual = new Date();
	const load = () => {
		$ctrl.object = Avaliacao.get(undefined,() => {
			$ctrl.object.campanha = Campanha.get({ id: $state.params.campanhaId });

			//$ctrl.object.participantes = [];
			$ctrl.object.tiposCompetencia = [];
			//fnListFuncionarios();
			//loadResumo($ctrl.object);
			pesquisarAgentes();
			preencheListaPlanejamentoAgentes($ctrl.object);
			// $ctrl.object.tiposCompetencia.forEach(tipoCompetencia => {
			// 	tipoCompetencia.percentualAutoavaliacao? tipoCompetencia.percentualAutoavaliacao : tipoCompetencia.percentualAutoavaliacao = 0;
			// 	tipoCompetencia.percentualGestor? tipoCompetencia.percentualGestor : tipoCompetencia.percentualGestor = 0;
			// 	tipoCompetencia.percentualPares? tipoCompetencia.percentualPares : tipoCompetencia.percentualPares = 0;
			// 	tipoCompetencia.percentualSubordinados? tipoCompetencia.percentualSubordinados : tipoCompetencia.percentualSubordinados = 0;
			// });
			//FIXME:Temporário, precisa de uma solução melhor
			$ctrl.object.limiteAv3 = null;
			$ctrl.object.dataLimite = null;
			// setTimeout(() => {
			// 	$scope.$apply(() => {
			// 		$ctrl.object.dataLimite = undefined;
			// 		$ctrl.object.limiteAv3 = undefined;
			// 	});
			// }, 500);
		},
		error => {
			$scope.$emit('notify-critical', error);
		});
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	//FIXME: Não houve tempo para refatorar
	const pesquisarAgentes = () => {
		return new Promise((resolve, reject) => {
			TipoResource.tiposAgentes()
				.$promise.then(data => {
					$ctrl.listaTiposAgentes = data;
					$ctrl.listaTiposAgentes.forEach(item => {
						//FIXME: Esse codigo não faz nenhum sentido. Por que copiar strings que são imutáveis?
						if (item == 'AUTOAVALIACAO') {
							$ctrl.agenteAutoavaliacao = angular.copy(item);
						}

						if (item == 'GESTOR') {
							$ctrl.agenteGestor = angular.copy(item);
						}

						if (item == 'SUBORDINADO') {
							$ctrl.agenteSubordinado = angular.copy(item);
						}

						if (item == 'PARES') {
							$ctrl.agentePar = angular.copy(item);
						}
					});
					resolve(true);
				}).catch(response => {
					$scope.$emit('notify-critical', response);
					reject(false);
				});
		});
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.desabilitarBotaoAdicionar = () => {
		if ($ctrl.object) {
			if ($ctrl.object.autoavaliacao || $ctrl.object.gestores || $ctrl.object.subordinados || $ctrl.object.pares) {
				return false;
			}
		}

		return true;
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	const preencheListaPlanejamentoAgentes = avaliacao => {
		if (avaliacao.tiposCompetencia) {
			avaliacao.tiposCompetencia.forEach(tc => {
				avaliacao.agentes.forEach(ag => {
					if (tc.nomeCurto === ag.tipoCompetencia.nomeCurto) {
						if (ag.perfilAgente === 'AUTOAVALIACAO') {
							if (ag.peso > 0) {
								tc.autoavaliacao = true;
							}

							tc.percentualAutoavaliacao = ag.peso;
						} else if (ag.perfilAgente === 'GESTOR') {
							if (ag.peso > 0) {
								tc.gestor = true;
							}

							tc.percentualGestor = ag.peso;
						} else if (ag.perfilAgente === 'SUBORDINADO') {
							if (ag.peso > 0) {
								tc.subordinados = true;
							}

							tc.percentualSubordinados = ag.peso;
						} else if (ag.perfilAgente === 'PARES') {
							if (ag.peso > 0) {
								tc.pares = true;
							}

							tc.percentualPares = ag.peso;
						}
					}
				});
			});
		}
	};

	const verificaDataLimite = avaliacao => {
	//FIXME: Replicado em Edit, Não houve tempo para refatorar
		if(!avaliacao.gestores && !avaliacao.subordinados && !avaliacao.pares){
			avaliacao.preencheAv2 = false;
			avaliacao.limiteAv2 = undefined;

			if(!avaliacao.autoavaliacao){
				avaliacao.preencheAv1 = false;
				//FIXME: Tive que passar null pois o componente não interpreta o undefined
				avaliacao.dataLimite = null;
			}
		}else if(avaliacao.gestores || avaliacao.subordinados || avaliacao.pares){
			avaliacao.limiteAv2 = new Date(avaliacao.dataLimite);
			avaliacao.preencheAv2 = true;
		}

		if(avaliacao.autoavaliacao){
			avaliacao.preencheAv1 = true;
			avaliacao.limiteAv1 = new Date(avaliacao.dataLimite);
		}

		if(avaliacao.consenso){
			avaliacao.preencheAv3 = true;
		}
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	//FIXME: Não houve tempo para refatorar
	const createAgente = (tipoCompetencia, perfil, peso) => {
		var agente = {};
		agente.avaliacao = {};
		//agente.avaliacao.id = $ctrl.object.id;
		agente.tipoCompetencia = tipoCompetencia;
		agente.perfilAgente = {};

		if (perfil === 'AUTOAVALIACAO') {
			agente.perfilAgente = $ctrl.agenteAutoavaliacao;
		}

		if (perfil === 'GESTOR') {
			agente.perfilAgente = $ctrl.agenteGestor;
		}

		if (perfil === 'SUBORDINADO') {
			agente.perfilAgente = $ctrl.agenteSubordinado;
		}

		if (perfil === 'PARES') {
			agente.perfilAgente = $ctrl.agentePar;
		}

		agente.peso = peso;

		return agente;
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	const contemAgente = (lista, novoAgente) => {
		let index = -1;
		lista.forEach((item, k) => {
			if (item.tipoCompetencia.id === novoAgente.tipoCompetencia.id
				&& item.perfilAgente === novoAgente.perfilAgente) {
				index = k;
				return;
			}
		});

		return index;
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	const validaTabelaTipoCompetencia = avaliacao => {
		let valid = true;
		if (!ValidationService.verificarPreenchimento(avaliacao.tiposCompetencia) || avaliacao.tiposCompetencia.length <= 0) {
			$scope.$emit('notify-error', 'A avaliação deve ter, pelo menos, um tipo de competência cadastrado.','Atenção!');
			valid = false;
		}

		//const originalAgentes = angular.copy(avaliacao.agentes);

		avaliacao.agentes = [];

		avaliacao.tiposCompetencia.forEach(tipoCompetencia => {
			let totalPercentualCompetencia = 0;
			const copia = angular.copy(tipoCompetencia);

			if (avaliacao.autoavaliacao) {
				totalPercentualCompetencia += tipoCompetencia.percentualAutoavaliacao;

				const existe = false;

				//TODO: Usado no EDITAR AVALIAÇÂO
				// originalAgentes.forEach(item => {
				// 	if (item.tipoCompetencia.id === tipoCompetencia.id && item.perfilAgente === 'AUTOAVALIACAO') {
				// 		item.peso = tipoCompetencia.percentualAutoavaliacao;
				// 		avaliacao.agentes.push(item);
				// 		existe = true;
				// 		return;
				// 	}
				// });

				if (existe === false) {
					const novoAgenteAutoavaliacao = createAgente(copia, 'AUTOAVALIACAO', tipoCompetencia.percentualAutoavaliacao);
					const posicaoAuto = contemAgente(avaliacao.agentes, novoAgenteAutoavaliacao);
					if (posicaoAuto < 0) {
						avaliacao.agentes.push(novoAgenteAutoavaliacao);
					}
				}
			}

			if (avaliacao.gestores) {
				totalPercentualCompetencia += tipoCompetencia.percentualGestor;

				const existe = false;

				//TODO: Usado no EDITAR AVALIAÇÂO
				// originalAgentes.forEach(item => {
				// 	if (item.tipoCompetencia.id === tipoCompetencia.id && item.perfilAgente === 'GESTOR') {
				// 		item.peso = tipoCompetencia.percentualGestor;
				// 		avaliacao.agentes.push(item);
				// 		existe = true;
				// 		return;
				// 	}
				// });

				if (existe === false) {
					const novoAgenteGestor = createAgente(copia, 'GESTOR', tipoCompetencia.percentualGestor);
					const posicaoGes = contemAgente(avaliacao.agentes, novoAgenteGestor);
					if (posicaoGes < 0) {
						avaliacao.agentes.push(novoAgenteGestor);
					}
				}
			}

			if (avaliacao.subordinados) {
				totalPercentualCompetencia += tipoCompetencia.percentualSubordinados;

				const existe = false;

				//TODO: Usado no EDITAR AVALIAÇÂO
				// originalAgentes.forEach(item => {
				// 	if (item.tipoCompetencia.id === tipoCompetencia.id && item.perfilAgente === 'SUBORDINADO') {
				// 		item.peso = tipoCompetencia.percentualSubordinados;
				// 		avaliacao.agentes.push(item);
				// 		existe = true;
				// 		return;
				// 	}
				// });

				if (existe === false) {
					const novoAgenteSubordinado = createAgente(copia, 'SUBORDINADO', tipoCompetencia.percentualSubordinados);
					const posicaoSub = contemAgente(avaliacao.agentes, novoAgenteSubordinado);
					if (posicaoSub < 0) {
						avaliacao.agentes.push(novoAgenteSubordinado);
					}
				}
			}

			if (avaliacao.pares) {
				totalPercentualCompetencia += tipoCompetencia.percentualPares;

				const existe = false;

				//TODO: Usado no EDITAR AVALIAÇÂO
				// originalAgentes.forEach(item => {
				// 	if (item.tipoCompetencia.id === tipoCompetencia.id && item.perfilAgente === 'PARES') {
				// 		item.peso = tipoCompetencia.percentualPares;
				// 		avaliacao.agentes.push(item);
				// 		existe = true;
				// 		return;
				// 	}
				// });

				if (existe === false) {
					const novoAgentePar = createAgente(copia, 'PARES', tipoCompetencia.percentualPares);
					const posicaoPar = contemAgente(avaliacao.agentes, novoAgentePar);
					if (posicaoPar < 0) {
						avaliacao.agentes.push(novoAgentePar);
					}
				}
			}

			if (totalPercentualCompetencia != 100) {
				$scope.$emit('notify-error', errosPreenchimento, 'Atenção!');
				valid = false;
			}
		});
		return valid;
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.totalSomaPercentuais = tipoCompetencia => {
		return tipoCompetencia.percentualAutoavaliacao +
			tipoCompetencia.percentualGestor +
			tipoCompetencia.percentualPares +
			tipoCompetencia.percentualSubordinados;
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.changeAgentes = (agente) => {
		if(agente === 'pares') {
			$ctrl.object.tiposCompetencia.forEach(tipo => {
				tipo.pares = $ctrl.object.pares;
				tipo.percentualPares = 0;
			});
		}

		if(agente === 'subordinados') {
			$ctrl.object.tiposCompetencia.forEach(tipo => {
				tipo.subordinados = $ctrl.object.subordinados;
				tipo.percentualSubordinados = 0;
			});
		}

		if(agente === 'gestores') {
			if(!$ctrl.object.gestores) {
				$ctrl.object.consenso = false;
				$ctrl.object.preencheAv3 = false;
				$ctrl.object.limiteAv3 = undefined;
			}

			$ctrl.object.tiposCompetencia.forEach(tipo => {
				tipo.gestor = $ctrl.object.gestores;
				tipo.percentualGestor = 0;
			});
		}

		if(agente === 'autoavaliacao') {
			if(!$ctrl.object.autoavaliacao) {
				$ctrl.object.preencheAv1 = false;
				$ctrl.object.limiteAv1 = undefined;

				$ctrl.object.consenso = false;
				$ctrl.object.preencheAv3 = false;
				//FIXME: Tive que passar null pois o componente não interpreta o undefined
				$ctrl.object.limiteAv3 = null;
			} else {
				$ctrl.object.preencheAv1 = true;
				$ctrl.object.limiteAv1 = new Date($ctrl.object.dataLimite);
			}

			$ctrl.object.tiposCompetencia.forEach(tipo => {
				tipo.autoavaliacao = $ctrl.object.autoavaliacao;
				tipo.percentualAutoavaliacao = 0;
			});
		}

		if(agente === 'consenso') {
			if ($ctrl.object.consenso) {
				$ctrl.object.autoavaliacao = true;
				$ctrl.object.gestores = true;
				$ctrl.object.preencheAv1 = true;
				$ctrl.object.preencheAv2 = true;
				$ctrl.object.preencheAv3 = true;
			} else {
				$ctrl.object.preencheAv3 = false;
				$ctrl.object.limiteAv3 = undefined;

				if ($ctrl.object.autoavaliacao && $ctrl.object.gestores && (!$ctrl.object.dataLimite || $ctrl.object.dataLimite === null)) {
					$ctrl.object.autoavaliacao = false;
					$ctrl.object.preencheAv1 = false;
					$ctrl.object.limiteAv1 = undefined;

					$ctrl.object.gestores = false;
					$ctrl.object.preencheAv2 = false;
					$ctrl.object.limiteAv2 = undefined;
				}
			}
		}

		verificaDataLimite($ctrl.object);
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	const doBeforeSave = avaliacao => {
		verificaDataLimite($ctrl.object);

		if (avaliacao.preencheAv3) {
			if (!avaliacao.preencheAv1) {
				$scope.$emit('notify-error', 'Por favor, para fazer a Avaliação 3 (AV3) é obrigatório informar a Avaliação 1 (AV1).', 'Atenção!');
				return false;
			}
		}

		if(!validarData($scope, avaliacao, true)) {
			return false;
		}

		if(!validaTabelaTipoCompetencia(avaliacao)){
			return false;
		}

		if ($ctrl.object.campanha.dimensao === true && $ctrl.object.listaDimensao.length <= 0) {
			$scope.$emit('notify-error', 'A avaliação deve ter, pelo menos, uma dimensão selecionada.', 'Atenção!');
			return false;
		}

		return true;
	};

	$ctrl.save = () => {
		const traduzirTexto = () =>
			$translate(['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR'])
				.then(translations => ({ translation: translations['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR'] }));
		const exibirMensagem = ({ translation }) => {
			$scope.$emit('notify-success', '', $ctrl.titulo + translation);
		};

		if (doBeforeSave($ctrl.object)) {
			$ctrl.object.$save()
				.then(obj => $ctrl.backEdit(obj))
				.then(traduzirTexto)
				.then(exibirMensagem);
		}
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.removerTipoCompetencia = tipoCompetencia => {
		const obterItem = element => {
			return element.id === tipoCompetencia.id;
		};

		const remover = () => {
			$ctrl.object.tiposCompetencia.splice(
				$ctrl.object.tiposCompetencia.findIndex(obterItem),
				1,
			);
		};

		if ($ctrl.object.tiposCompetencia != null && $ctrl.object.tiposCompetencia.length == 1) {
			$scope.$emit('notify-error', 'Avaliação deve ter pelo menos um tipo de competência!', 'Atenção!');
		}else {
			dialogAcao(
				tipoCompetencia,
				'DIALOG.REMOVE_TITULO_TIPOCOMPETENCIA',
				'DIALOG.MSG_REMOVE_TIPOCOMPETENCIA',
				'DIALOG.MSG_NOTIFICACAO_TIPOCOMPETENCIA',
				remover,
			);
		}
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.removerDimensao = dimensao => {
		const obterItem = element => {
			return element.id === dimensao.id;
		};

		const remover = () => {
			if ($ctrl.object.listaDimensao != null && $ctrl.object.listaDimensao.length == 1) {
				$scope.$emit('notify-error', 'Avaliação deve ter pelo menos uma dimensão!', 'Atenção!');
			}else {
				$ctrl.object.listaDimensao.splice(
					$ctrl.object.listaDimensao.findIndex(obterItem),
					1,
				);
			}
		};

		dialogAcao(
			dimensao,
			'DIALOG.REMOVE_TITULO_DIMENSAO',
			'DIALOG.MSG_REMOVE_DIMENSAO',
			'DIALOG.MSG_NOTIFICACAO_DIMENSAO',
			remover,
		);
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	//FIXME: Deve-se rever toda a parte de dimensão, o mesmo não esta seguindo nenhuma regra
	$ctrl.AdicionarDimensao = () => {
		$mdDialog.show({
			controller: dialogDimensaoCtrl,
			controllerAs: '$dialog',
			templateUrl: 'dialog.adicionar.dimensao.html',
			parent: angular.element(document.body),
			locals: {
				listDimensoes: $ctrl.object.listaDimensao
			},
			clickOutsideToClose: true
		});
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	$ctrl.adicionarTipoCompetencia = () => {
		$mdDialog.show({
			controller: dialogTipoCompetenciaCtrl,
			controllerAs: '$dialog',
			locals: {
				listaTiposCompetenciaOriginal: $ctrl.object.tiposCompetencia,
				listaTiposCompetenciaSelecionadas: $ctrl.object.tiposCompetencia,
				objetoTela: $ctrl.object,
				toolBarClassDialog: 'md-accent-bg',
				title: 'Adicionar Tipo de competência'
			},
			templateUrl: 'dialog.add.tipo.competencia.html',
			parent: angular.element(document.body),
			clickOutsideToClose: true
		});
	};

	//FIXME: Replicado em Edit, Não houve tempo para refatorar
	const dialogAcao = (avaliacao, titulo, msg, textoNotificacao, acao) => {
		const traduzirTexto = () =>
			$translate([textoNotificacao]).then(translations => {
				$scope.$emit(
					'notify-success',
					'',
					translations[textoNotificacao],
				);
			});
		DialogConfirm.show(titulo, msg, avaliacao)
			.then(acao)
			.then(traduzirTexto);
	};

	/* $ctrl.ajudaResumo = modo => {
		import('../avaliacao.help').then(help => help.showAjudaResumo(modo));
	}; */

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	$ctrl.back = () => {
		$ctrl.backToCampanha();
	};

	$ctrl.backEdit = (obj) => {
		$state.go(STATE_VIEW, { campanhaId: $state.params.campanhaId, objectId: obj.id });
	};

	$ctrl.backToCampanha = () => {
		$state.go(STATE_CAMPANHA_VIEW, { objectId: $state.params.campanhaId });
	};

	$ctrl.backToList = () => {
		$state.go(STATE_CAMPANHA_LIST);
	};

	load();
}
