import angular from 'angular';
import secondaryLoginController from '../dialogs/login_consenso';
import ROUTES from '../../../utils/routes';
//import detail_ajuda from './preenchimento.detail.ajuda';

/* @ngInject */
export default function detailController(
	$state,
	$mdDialog,
	$scope,
	$stateParams,
	$rootScope,
	objectId,
	stage,
	Participante,
	FotoResource,
	ApontamentoFuncionalResource,
	CompetenciaPreenchimento,
	DialogWait
) {
	//giz de cera
	//$ctrl.ajuda = detail_ajuda;
	//console.log('detail_ajuda',detail_ajuda);

	const $ctrl = this;

	$ctrl.screenMode = 'EDIT';
	$ctrl.objectId = objectId;
	$ctrl.stage = stage;

	$ctrl.object = {};

	$ctrl.stages = {
		'av1': {
			nome: 'Sua AUTOAVALIAÇÃO deve ser concluída ',
			tipo: 1
		},
		'av2': {
			nome: 'Sua AVALIAÇÃO deve ser concluída ',
			tipo: 2
		},
		'av3': {
			nome: 'Você e seu gestor devem concluir a AVALIAÇÃO DE CONSENSO',
			tipo: 3
		},
	};

	function showDialogConsenso() {
		/* @ngInject */
		return $mdDialog.show({
			controller: secondaryLoginController,
			controllerAs: 'dialog',
			locals: {
				participante: $ctrl.participante,
				user: $ctrl.user,
				notification: $ctrl.notification
			},
			bindToController: true,
			templateUrl: 'login_consenso.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			escapeToClose: false
		});
	}

	$ctrl.loginError = (err) => {
		if (err === 'canceled')
			$scope.$emit('notify-error', 'Login cancelado!', 'Atenção!');
		else if (err === 'outdated')
			$scope.$emit('notify-error', 'Consenso fora do prazo!', 'Atenção!');
		else
			$scope.$emit('notify-critical', err);
	};

	function carregarParticipante() {
		$ctrl.participante = Participante.get({
			id: objectId
		});
		$ctrl.participante.$promise
			.then(data => {
				if ($ctrl.stage == 'av3') {
					if (data.consensoLiberado) {
						return showDialogConsenso(data);
					} else
						return Promise.reject('outdated');
				}
			})
			.then(data => {
				if ($ctrl.stage == 'av2') {
					$ctrl.stages.av2.nome = 'Sua AVALIAÇÃO deve ser concluída ';
				}

				carregarAvaliacao();
				return data;
			})
			.catch(response => {
				$ctrl.loginError(response);

				$state.go($stateParams.from);
			});
	}

	function carregarAvaliacao() {
		const competencias = buscaCompetencias();
		competencias.then(data => {
			data.forEach(competencia => {
				competencia.detalhamentos.forEach(detalhamento => {
					detalhamento.hints.forEach(hint => {
						detalhamento.preenchimentos.forEach(preench => {
							if ($ctrl.stage == 'av2') {
								if (preench.graduacaoAv2Id == hint.idGraduacao) {
									detalhamento.obrigatorio = hint.comentarioObrigatorio;
								}
							} else if ($ctrl.stage == 'av1') {
								if (preench.graduacaoAv1Id == hint.idGraduacao) {
									detalhamento.obrigatorio = hint.comentarioObrigatorio;
								}
							} else if ($ctrl.stage == 'av3') {
								if (preench.graduacaoAv3Id == hint.idGraduacao) {
									detalhamento.obrigatorio = hint.comentarioObrigatorio;
								}
							}
						});
					});
				});
			});
		}).catch(response => {
			$scope.$emit('notify-critical', response);
		});
		$ctrl.stages.av1.prazo = $ctrl.participante.avaliacao.limiteAv1;
		$ctrl.stages.av2.prazo = $ctrl.participante.avaliacao.limiteAv2;
		$ctrl.stages.av3.prazo = $ctrl.participante.avaliacao.limiteAv3;
		loadFuncionarioFoto($ctrl.participante.avaliado.id);
	}

	carregarParticipante();

	function loadFuncionarioFoto(id) {
		const filtro = {
			funcionario: {
				id: id
			},
			tipo: 'FOTO_PERFIL'
		};
		FotoResource.buscar(null, filtro).$promise
			.then(data => {
				if (data && data.length && data.length > 0) {
					$ctrl.cropper = {};
					$ctrl.cropper.id = data[0].id;
					$ctrl.cropper.funcionario = data[0].funcionario;
					$ctrl.cropper.tipo = data[0].tipo;
					$ctrl.cropper.sourceImage = data[0].base64;
					$ctrl.cropper.croppedImage = data[0].base64;
				}
			});
	}

	function buscaCompetencias() {
		$ctrl.competencias = CompetenciaPreenchimento.porParticipante({
			participanteId: $ctrl.participante.id
		});
		return $ctrl.competencias.$promise;
	}

	// TODO Criar um evento de mudança de usuario
	$rootScope.$watch('user', function (user) {
		$ctrl.user = user;
	});

	$ctrl.validarComentarios = function (competencia, mensagemErro) {
		competencia.detalhamentos.forEach(detalhamento => {
			detalhamento.preenchimentos.forEach(preench => {
				if ($ctrl.stage == 'av2') {
					if (detalhamento.obrigatorio && (preench.observacaoAv2 === null || preench.observacaoAv2.trim() === '')) {
						const pos = mensagemErro.search('Preencha os comentários obrigatórios!');
						if (pos < 0) {
							mensagemErro = mensagemErro + '\n Preencha os comentários obrigatórios!';
						}
					}
				} else if ($ctrl.stage === 'av1') {
					if (detalhamento.obrigatorio && (preench.observacaoAv1 == null || preench.observacaoAv1.trim() === '')) {
						const pos = mensagemErro.search('Preencha os comentários obrigatórios!');
						if (pos < 0) {
							mensagemErro = mensagemErro + '\n Preencha os comentários obrigatórios!';
						}
					}
				} else if ($ctrl.stage == 'av3') {
					if (detalhamento.obrigatorio && (preench.observacaoAv3 === null || preench.observacaoAv3.trim() === '')) {
						const pos = mensagemErro.search('Preencha os comentários obrigatórios!');
						if (pos < 0) {
							mensagemErro = mensagemErro + '\n Preencha os comentários obrigatórios!';
						}
					}
				}
			});
		});

		return mensagemErro;
	};

	$ctrl.salvarCompetencia = (comp, final) => {
		const progresso = comp.progresso;

		let mensagemErro = '';

		if (final == true) {
			$ctrl.competencias.forEach(competencia => {
				competencia.detalhamentos.forEach(pergunta => {
					pergunta.finalizou = true;

					if ($ctrl.stage == 'av1' && angular.isUndefined(pergunta.preenchimentos[0].graduacaoAv1Id)) {
						mensagemErro = mensagemErro + '\n A competência "' + competencia.descricaoAbreviada + '" possui graduações que não foram marcadas.';
					} else if ($ctrl.stage == 'av2' && angular.isUndefined(pergunta.preenchimentos[0].graduacaoAv2Id)) {
						mensagemErro = mensagemErro + '\n A competência "' + competencia.descricaoAbreviada + '" possui graduações que não foram marcadas.';
					} else if ($ctrl.stage == 'av3' && angular.isUndefined(pergunta.preenchimentos[0].graduacaoAv3Id)) {
						mensagemErro = mensagemErro + '\n A competência "' + competencia.descricaoAbreviada + '" possui graduações que não foram marcadas.';
					}
				});
			});
		} else {
			mensagemErro = $ctrl.validarComentarios(comp, mensagemErro);
		}

		if (mensagemErro != '') {
			$scope.$emit('notify-error', mensagemErro, 'Atenção!');
		} else if (final == true) {
			const wait = DialogWait.show('Aguarde, salvando...');

			$ctrl.competencias.forEach(competencia => {
				competencia.detalhamentos.forEach(detalhamento => {
					detalhamento.finalizou = true;
					detalhamento.preenchimentos.forEach(preenchimento => {
						preenchimento.tipoClient = 'WEB';
					});
				});
			});

			//FIXME: Código repetido
			CompetenciaPreenchimento.salvarCompetencias({ participanteId: $ctrl.participante.id }, $ctrl.competencias).$promise.then(() => {
				$scope.$emit('notify-success', 'Avaliação salva com sucesso!');
				wait.hide();
				if (stage === 'av1' || stage === 'av2') {
					$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
				} else {
					$state.go($stateParams.from);
				}
			}).catch(response => {
				$scope.$emit('notify-critical', response);
				wait.hide();
			});
		} else {
			const wait = DialogWait.show('Aguarde, salvando...');

			comp.detalhamentos.forEach(detalhamento => {
				detalhamento.preenchimentos.forEach(preenchimento => {
					preenchimento.tipoClient = 'WEB';
				});
			});

			comp.$save({
				participanteId: $ctrl.participante.id
			})
				.then(data => {
					data.progresso = progresso;
					if (final) {
						$scope.$emit('notify-success', 'Avaliação salva com sucesso!');
						wait.hide();
						if (stage === 'av1' || stage === 'av2') {
							$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
						} else {
							$state.go($stateParams.from);
						}
					} else {
						$scope.$emit('notify-success', 'Atualizado com sucesso!');
						wait.hide();
					}
				}).catch(response => {
					$scope.$emit('notify-critical', response);
					wait.hide();
				});
		}
	};

	$ctrl.verifyAvaliacaoStage = (comp, final) => {
		let mensagemErro = '';

		if (!final) {
			mensagemErro = $ctrl.validarComentarios(comp, mensagemErro);
		} else {
			$ctrl.competencias.forEach(comp => {
				mensagemErro = $ctrl.validarComentarios(comp, mensagemErro);
			});
		}

		if (mensagemErro != '') {
			$scope.$emit('notify-error', mensagemErro, 'Atenção!');
			return;
		}

		if ($ctrl.stage === 'av3') {
			showDialogConsenso().then(() => {
				$ctrl.salvarCompetencia(comp, final);
			}).catch(response => {
				$ctrl.loginError(response);
			});
		} else {
			$ctrl.salvarCompetencia(comp, final);
		}
	};

	function dialogApontamentoFuncional($mdDialog, title, listaApontamentos, user) {
		const $dialog = this;
		$dialog.object = {};
		$dialog.title = title;
		$dialog.listaApontamentos = listaApontamentos;
		$dialog.user = user;

		$dialog.hide = () => {
			$mdDialog.hide();
		};

		$dialog.cancel = () => {
			$mdDialog.cancel();
		};

		$dialog.confirmar = () => {
			$mdDialog.hide();
		};
	}

	$ctrl.acaoApontamentoFuncional = event => {
		const listaApontamentos = [];

		const promise = ApontamentoFuncionalResource.porParticipante($ctrl.participante).$promise;

		$rootScope.loadingProgress = true;

		promise.then(data => {
			data.forEach(value => {
				listaApontamentos.push(value);
			});
			$mdDialog.show({
				controller: dialogApontamentoFuncional,
				controllerAs: '$dialog',
				locals: {
					title: 'Registro crítico (histórico)',
					color: 'background-color: rgb(30, 150, 0);color: rgba(255,255,255,0.87);',
					listaApontamentos: listaApontamentos,
					user: $ctrl.user
				},
				templateUrl: 'dialog_apontamento_funcional.html',
				parent: angular.element(document.body),
				targetEvent: event,
				clickOutsideToClose: false,
				fullscreen: false
			});
			$rootScope.loadingProgress = false;
		}).catch(response => {
			$rootScope.loadingProgress = false;
			$scope.$emit('notify-critical', response);
		});
	};
}
