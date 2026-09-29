import angular from 'angular';
import ROUTES from '../../../../utils/routes';

/* @ngInject */
export default function detailCompetencia(
	$scope,
	$state,
	$stateParams,
	$rootScope,
	$timeout,
	$sce,
	AvaliacaoCompetencia,
	AvaliacaoCompDet,
	DialogWait
) {
	const $ctrl = this;

	// INICIAR VARIÁVEIS
	function init() {
		$ctrl.object = undefined;
		$ctrl.detalhamentos = undefined;
		$ctrl.graduacoes = undefined;
		$ctrl.funcionarioPreenchimentos = [];

		$ctrl.competenciaId = $stateParams.competenciaId;
		$ctrl.avaliadorId = $stateParams.avaliadorId;
		$ctrl.avaliacaoId = $stateParams.avaliacaoId;
		$ctrl.perfilId = $stateParams.perfilId;
		$ctrl.todosConsensosLiberados = $stateParams.todosConsensosLiberados;
		$ctrl.final = $stateParams.final;
		$ctrl.listaCompetencias = $stateParams.listaCompetencias.split(',');
		$ctrl.loadObject();
	}

	$ctrl.loadObject = () => {
		if ($ctrl.competenciaId) {
			$ctrl.waitQuestionario = DialogWait.showClick('Aguarde ...', false);

			AvaliacaoCompetencia.porAvaliacaoECompetencia({
				avaliacaoId: $ctrl.avaliacaoId,
				competenciaId: $ctrl.competenciaId
			})
				.$promise.then(data => {
					$ctrl.object = data;
					$ctrl.findDetalhamentos($ctrl.object);
				}).catch(response => {
					$rootScope.loadingProgress = false;
					$scope.$emit('notify-critical', response);
				});
		}
	};

	$ctrl.back = () => {
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
		}
	};

	$ctrl.cancel = () => {
		$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
	};

	let detalhamentoPessoas = [];

	$ctrl.currentSlide = 0;

	$ctrl.findDetalhamentos = obj => {
		AvaliacaoCompDet.porAvaliacaoCompetenciaEAvaliadorEPerfil({
			avaliacaoCompetenciaId: obj.id,
			avaliadorId: $ctrl.avaliadorId
		}).$promise
			.then(dataDetalhamentos => {
				$ctrl.detalhamentos = angular.copy(dataDetalhamentos);
				$ctrl.calculaProgresso();

				$ctrl.detalhamentos.forEach(detalhamento => {
					detalhamento.descricao = detalhamento.descricao.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;').replace(/\n/g, '<br/>').replace(/\s/g, '&nbsp;');
					$ctrl.ordenaPreenchimentosPorGraduacao(detalhamento);
				});

				$ctrl.slickPanels = {
					enabled: true,
					draggable: true,
					dots: true,
					infinite: false,
					slidesToShow: 1,
					adaptiveHeight: true,
					event: {
						afterChange: (event, slick, currentSlide) => {
							$ctrl.currentSlide = currentSlide;
							$ctrl.pessoasDetalhamento = detalhamentoPessoas[currentSlide].filter(detalhamento => {
								return detalhamento;
							});
						}
					}
				};

				detalhamentoPessoas = $ctrl.detalhamentos.map(detalhamento => {
					return detalhamento.preenchimentos;
				});

				$ctrl.pessoasDetalhamento = detalhamentoPessoas[0];

				$rootScope.loadingProgress = false;

				$timeout(() => {
					$ctrl.waitQuestionario.hide();
				}, 1000);
			}).catch(response => {
				$rootScope.loadingProgress = false;
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.calculaProgresso = () => {
		let percentual = 0;
		let qtdPerguntas = 0;
		let qtdRespostas = 0;
		if ($ctrl.detalhamentos.length) {
			$ctrl.detalhamentos.forEach( detalhamento => {
				detalhamento.preenchimentos.forEach( preench => {
					qtdPerguntas = qtdPerguntas + 1;
					if (!preench.idGraduacaoTemp) {
						preench.idGraduacaoTemp = '';
					}

					if (!preench.observacao) {
						preench.observacao = undefined;
					}

					if (preench.participante.avaliado.id.toString() === $ctrl.avaliadorId.toString()) {
						if (preench.idGraduacaoTemp != '') {
							preench.graduacaoAv1Id = preench.idGraduacaoTemp;
							preench.observacaoAv1 = preench.observacao;
						}

						if (preench.graduacaoAv1Id)
							qtdRespostas = qtdRespostas + 1;
					} else if (preench.participante.avaliado.id.toString() !== $ctrl.avaliadorId.toString()) {
						if (preench.idGraduacaoTemp != '') {
							preench.graduacaoAv2Id = preench.idGraduacaoTemp;
							preench.observacaoAv2 = preench.observacao;
						}

						if (preench.graduacaoAv2Id)
							qtdRespostas = qtdRespostas + 1;
					}
				});
			});
		}

		if (qtdPerguntas > 0 && qtdRespostas > 0) {
			percentual = (qtdRespostas * 100) / qtdPerguntas;
		}

		$ctrl.object.qtdPerguntas = qtdPerguntas;
		$ctrl.object.qtdRespostas = qtdRespostas;
		$ctrl.object.progresso = Math.round(percentual);
	};

	// FIXME: Código repetido
	$ctrl.ordenaPreenchimentosPorGraduacao = obj => {
		if ($ctrl.detalhamentos.length) {
			$ctrl.detalhamentos.forEach( detalhamento => {
				if (detalhamento.id == obj.id) {
					detalhamento.preenchimentos.forEach( preench => {
						if (!preench.idGraduacaoTemp) {
							preench.idGraduacaoTemp = '';
						}

						if (!preench.observacao) {
							preench.observacao = undefined;
						}

						if (preench.participante.avaliado.id.toString() === $ctrl.avaliadorId.toString()) {
							if (preench.graduacaoAv1Id && preench.idGraduacaoTemp === '') {
								preench.idGraduacaoTemp = preench.graduacaoAv1Id;
								preench.observacao = preench.observacaoAv1;
							} else if (preench.idGraduacaoTemp != '') {
								preench.graduacaoAv1Id = preench.idGraduacaoTemp;
								preench.observacaoAv1 = preench.observacao;
							}
						} else {
							if (preench.graduacaoAv2Id && preench.idGraduacaoTemp === '') {
								preench.idGraduacaoTemp = preench.graduacaoAv2Id;
								preench.observacao = preench.observacaoAv2;
							} else if (preench.idGraduacaoTemp != '') {
								preench.graduacaoAv2Id = preench.idGraduacaoTemp;
								preench.observacaoAv2 = preench.observacao;
							}
						}

						detalhamento.hints.forEach( hint => {
							if (preench.participante.avaliado.id.toString() === $ctrl.avaliadorId.toString() &&
								hint.graduacao.idGraduacao == preench.graduacaoAv1Id) {
								preench.graduacaoTemp = hint.graduacao;
							} else if (preench.participante.avaliado.id.toString() !== $ctrl.avaliadorId.toString() &&
								hint.graduacao.idGraduacao == preench.graduacaoAv2Id) {
								preench.graduacaoTemp = hint.graduacao;
							}
						});
					});
				}
			});
		}
	};

	init();

	$ctrl.changeGraduacao = (detalhamento, preenchimento) => {
		$ctrl.calculaProgresso();

		detalhamento.hints.forEach( hint => {
			if (preenchimento.participante.avaliado.id == $ctrl.avaliadorId) {
				if (preenchimento.idGraduacaoTemp == hint.idGraduacao) {
					preenchimento.comentarioObrigatorio = hint.comentarioObrigatorio;
				}
			} else if (preenchimento.participante.avaliado.id != $ctrl.avaliadorId) {
				if (preenchimento.idGraduacaoTemp == hint.idGraduacao) {
					preenchimento.comentarioObrigatorio = hint.comentarioObrigatorio;
				}
			}
		});

		$ctrl.ordenaPreenchimentosPorGraduacao(detalhamento);
	};

	$ctrl.validarPreenchimento = listaDetalhamentos => {
		const resultadoValidacao = {};

		resultadoValidacao.preenchimentoOk = true;
		resultadoValidacao.erros = '';

		$ctrl.detalhamentos.forEach( detalhamento => {
			detalhamento.preenchimentos.forEach( preench => {
				preench.tipoClient = 'WEB';
				if (preench.participante.avaliado.id.toString() === $ctrl.avaliadorId.toString()) {
					if (preench.graduacaoAv1Id && preench.idGraduacaoTemp === '') {
						preench.idGraduacaoTemp = preench.graduacaoAv1Id;
						preench.observacao = preench.observacaoAv1;
					} else if (preench.idGraduacaoTemp != '') {
						preench.graduacaoAv1Id = preench.idGraduacaoTemp;
						preench.observacaoAv1 = preench.observacao;
					}

					if (preench.comentarioObrigatorio && (preench.observacaoAv1 == null || preench.observacaoAv1.trim() === '')) {
						resultadoValidacao.erros = resultadoValidacao.erros + detalhamento.descricao + ' -> colaborador: '
							+ preench.participante.avaliado.nome + ' \n';
						resultadoValidacao.preenchimentoOk = false;
					} else {
						listaDetalhamentos.push(detalhamento);
					}
				} else {
					if (preench.graduacaoAv2Id && preench.idGraduacaoTemp === '') {
						preench.idGraduacaoTemp = preench.graduacaoAv2Id;
						preench.observacao = preench.observacaoAv2;
					} else if (preench.idGraduacaoTemp != '') {
						preench.graduacaoAv2Id = preench.idGraduacaoTemp;
						preench.observacaoAv2 = preench.observacao;
					}

					if (preench.comentarioObrigatorio && (preench.observacaoAv2 == null || preench.observacaoAv2.trim() === '')) {
						resultadoValidacao.erros = resultadoValidacao.erros + detalhamento.descricao + ' -> colaborador: '
							+ preench.participante.avaliado.nome + ' \n';
						resultadoValidacao.preenchimentoOk = false;
					} else {
						listaDetalhamentos.push(detalhamento);
					}
				}
			});
		});

		return resultadoValidacao;
	};

	$ctrl.salvar = acao => {
		const listaDetalhamentos = [];

		const validacao = $ctrl.validarPreenchimento(listaDetalhamentos);

		if (validacao.preenchimentoOk) {
			if (listaDetalhamentos.length > 0) {
				$ctrl.waitPreenchimento = DialogWait.showClick('Aguarde, salvando os preenchimentos...', false);
			}

			const promises = [];

			listaDetalhamentos.forEach( avalCompDet => {
				const promise = AvaliacaoCompDet.salvar(avalCompDet).$promise;
				promises.push(promise);
			});

			Promise.all(promises).then(() => {
				$scope.$emit('notify-success', 'Preenchimento salvo com sucesso!');

				if (listaDetalhamentos.length > 0) {
					$ctrl.waitPreenchimento.hide();
				}

				if (acao === 'proximo' && $ctrl.object.progresso === 100) {
					const obj = $ctrl.listaCompetencias[0];
					const valores = obj.split('/');

					const stage = valores[0];
					const participanteId = valores[1];
					const avaliacaoId = valores[2];
					const avaliadorId = valores[3];
					const competenciaId = valores[4];
					const perfilId = valores[5];
					const todosConsensosLiberados = valores[6];

					$ctrl.waitPreenchimento.hide();

					if (stage
						&& participanteId
						&& avaliacaoId
						&& avaliadorId
						&& competenciaId) {
						$ctrl.listaCompetencias.splice(0, 1);
						$state.go(ROUTES.MAIN.PREENCHIMENTO.DETAIL_COMPETENCIA.name, {
							stage: stage,
							objectId: participanteId,
							avaliacaoId: avaliacaoId,
							avaliadorId: avaliadorId,
							competenciaId: competenciaId,
							perfilId: perfilId,
							listaCompetencias: $ctrl.listaCompetencias,
							todosConsensosLiberados: todosConsensosLiberados
						});
					} else {
						$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
					}
				} else {
					$state.go(ROUTES.MAIN.PREENCHIMENTO.AVALIACOES.name);
				}
			}).catch(response => {
				$ctrl.waitPreenchimento.hide();
				$rootScope.loadingProgress = false;
				$scope.$emit('notify-critical', response);
				console.error('Error',response);
			});
		} else {
			$scope.$emit('notify-error', '', 'Preencha os comentários obrigatórios!');
		}
	};
}
