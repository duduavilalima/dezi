import angular from 'angular';
import c3 from 'c3-angular';
import moment from 'moment';
import ROUTES from 'utils/routes';
import { ajuda, ajudaAbaColaboradores, ajudaAbaGestores, ajudaSubordinados } from './listController.ajuda';
import SituacaoAvaliacao from '../model/SituacaoAvaliacao';

/** @ngInject */
export default function listController(
	$scope,
	$rootScope,
	$state,
	Participante,
	UserService,
	CompetenciaAvaliador,
) {
	const $ctrl = this;

	$ctrl.filtroPorTipo = {};
	$ctrl.displayAv1 = false;
	$ctrl.displayAv2 = false;

	/*teste giz de cera*/
	$ctrl.ajuda = ajuda;
	$ctrl.ajudaAbaGestores = ajudaAbaGestores;
	$ctrl.ajudaAbaColaboradores = ajudaAbaColaboradores;
	$ctrl.ajudaSubordinados = ajudaSubordinados;

	$ctrl.selecionados = {};

	$ctrl.avaliacoes = {};

	// TODO Criar um evento de mudança de usuario
	$rootScope.$watch('user', user => {
		$ctrl.user = user;
		if (user) {
			filterData();
		}
	});

	const load = () => {
		if ($ctrl.user)
			filterData();
	};

	$ctrl.queryAvaliacoes = {
		order: 'avaliador.nome',
		limit: 10,
		page: 1
	};

	$ctrl.queryAvaliacoesRevisores = {
		order: 'avaliado.nome',
		limit: 20,
		page: 1
	};

	$ctrl.queryCompetencias = {
		order: 'descricaoAbreviada',
		limit: 20,
		page: 1
	};

	$ctrl.queryCompetenciasGestores = {
		order: 'descricaoAvaliacao',
		limit: 20,
		page: 1
	};

	$ctrl.queryCompetenciasSubordinados = {
		order: 'descricaoAvaliacao',
		limit: 20,
		page: 1
	};

	$ctrl.queryCompetenciasPares = {
		order: 'descricaoAvaliacao',
		limit: 20,
		page: 1
	};

	$ctrl.dataLimitePorCompetencia = function (avaliacao) {
		return !moment().isSameOrBefore(moment(avaliacao.limiteAv2).endOf('day'));
	};

	$ctrl.getData = (item) => {
		if (item.limiteAv2) {
			return item.limiteAv2;
		} else if (item.limiteAv1) {
			return item.limiteAv1;
		} else if (item.limiteAv3) {
			return item.limiteAv3;
		} else if (item.avaliacao.limiteAv2) {
			return item.avaliacao.limiteAv2;
		} else if (item.avaliacao.limiteAv1) {
			return item.avaliacao.limiteAv1;
		} else if (item.avaliacao.limiteAv3) {
			return item.avaliacao.limiteAv3;
		}
	};

	$ctrl.openAV2PorCompetencia = (avaliacaoId, competenciaId, perfil, posicao, todosConsensosLiberados) => {
		const avaliadorId = $ctrl.user.id;
		let participanteId;
		const listaCompetencias = [];
		let perfilId = undefined;
		let final = undefined;

		if (perfil === 'GESTOR') {
			perfilId = 101;
		} else if (perfil === 'SUBORDINADO') {
			perfilId = 102;
		} else if (perfil === 'PARES') {
			perfilId = 103;
		}

		if (angular.isUndefined(final)) {
			final = true;
		}

		const stage = 'av2';
		$state.go(ROUTES.MAIN.PREENCHIMENTO.DETAIL_COMPETENCIA.name, {
			stage: stage,
			objectId: participanteId,
			avaliacaoId: avaliacaoId,
			avaliadorId: avaliadorId,
			competenciaId: competenciaId,
			perfilId: perfilId,
			listaCompetencias: listaCompetencias,
			todosConsensosLiberados: todosConsensosLiberados,
			final: final
		});
	};

	function openAv1(participanteId) {
		openAv(participanteId, 'av1');
	}

	function openAv2(participanteId) {
		openAv(participanteId, 'av2');
	}

	function openAv3(participanteId) {
		$state.go(ROUTES.MAIN.PREENCHIMENTO.DETAIL.name, {
			stage: 'av3',
			id: participanteId,
			from: $state.current.name
		});
	}

	function openAv(participanteId, stage) {
		$state.go(ROUTES.MAIN.PREENCHIMENTO.DETAIL.name, {
			stage: stage,
			id: participanteId
		});
	}

	// FIXME: Refatorar.
	const checkDate = date => {
		return !moment().isSameOrBefore(moment(new Date(date)).endOf('day'));
	};

	// FIXME: Melhorar código abaixo... como desabilitar o botão conforme data da avaliação??????
	const validarDataLimiteAv1 = item => {
		const dataAv1 = moment(item.limiteAv1).toDate();

		if (dataAv1 != null) {
			return false;
		}

		return true;
	};

	const validarDataLimiteAv2 = item => {
		const dataAv2 = moment(item.limiteAv2).toDate();

		if (dataAv2 != null) {
			return false;
		}

		return true;
	};

	const validarAv3 = item => {
		if (item.limiteAv3 != null) {
			return checkDate(new Date(item.limiteAv3));
		}

		return true;
	};

	const acoesAv1 = [{
		nome: 'Preencher autoavaliação',
		click: openAv1,
		icon: 'icon-pencil-box',
		cor: 'green-fg',
		disabled: validarDataLimiteAv1,
		tooltip: 'Preencher avaliação'
	}];

	const acoesAv2 = [{
		nome: 'Preencher avaliação',
		click: openAv2,
		icon: 'icon-pencil-box',
		cor: 'green-fg',
		disabled: validarDataLimiteAv2,
		tooltip: 'Preencher avaliação'
	}];

	const acoesAv3 = [{
		nome: 'Preencher consenso',
		click: openAv3,
		icon: 'icon-pencil-box',
		cor: 'green-fg',
		disabled: validarAv3,
		tooltip: 'Preencher consenso'
	}];

	//preenche as listas de competências das "sub-abas" que estão dentro da aba competência
	function carregarCompetencias(competenciasGestores, perfilAgente) {
		competenciasGestores.$promise
			.then(data => {
				if (perfilAgente === 'GESTOR') {
					$ctrl.competenciasGestores = data;
					$ctrl.competenciasGestoresFilter = data;
				} else if (perfilAgente === 'SUBORDINADO') {
					$ctrl.competenciasSubordinados = data;
				} else if (perfilAgente === 'PARES') {
					$ctrl.competenciasPares = data;
				}

				angular.forEach(data, function (competencia, key) {
					competencia.posicao = key;

					competencia.percentualPreenchimento = 0;
					if (competencia.qtdPerguntas > 0 && competencia.qtdRespostas > 0) {
						competencia.percentualPreenchimento = (competencia.qtdRespostas * 100) / competencia.qtdPerguntas;
						competencia.percentualPreenchimento = competencia.percentualPreenchimento.toFixed(2);
					}

					$ctrl.qtdCompetencias++;

					if (competencia.percentualPreenchimento == 100)
						$ctrl.qtdCompetenciasPreenchidas++;

					const tipo = {
						nome: competencia.tipoCompetencia,
						qtd: 0
					};
					const posicao = $ctrl.containsTipo($ctrl.tiposCompetencia, tipo.nome);

					if (posicao > -1) {
						const objeto = $ctrl.tiposCompetencia[posicao];
						objeto.qtd = objeto.qtd + 1;
					} else {
						const novoObjeto = {
							nome: competencia.tipoCompetencia,
							qtd: 1
						};
						$ctrl.tiposCompetencia.push(novoObjeto);
					}
				});

				$ctrl.calculosGrafico();
			}).catch(response => {
				$rootScope.loadingProgress = false;
				$scope.$emit('notify-critical', response);
			});
	}

	$ctrl.containsTipo = function (tipos, nome) {
		let index = -1;

		angular.forEach(tipos, function (tipo, key) {
			if (tipo.nome == nome)
				index = key;
		});

		return index;
	};

	$ctrl.montaGrafico = (idGrafico, arrayColunas) => {
		return c3.generate({
			bindto: idGrafico,
			data: {
				columns: arrayColunas,
				type: 'donut'
			}
		});
	};

	function filterData() {
		const myId = $ctrl.user.id;

		// Avaliado
		let totalPreenchidaAv1Avaliado = 0;
		let totalPreenchidaAv2Avaliado = 0;
		let totalPreenchidaAv3Avaliado = 0;
		let totalPreenchidaAv4Avaliado = 0;

		// Avaliador
		let totalAutoAvaliacao = 0;
		let totalAvaliacao = 0;
		let totalConsenso = 0;
		let totalRevisor = 0;
		// const avaliadoAv1 = 0;

		$ctrl.avaliacoes.avaliado = [];
		$ctrl.avaliacoes.avaliado.estagio = {
			av1: 0,
			av3: 0
		};
		$ctrl.avaliacoes.avaliador = [];
		$ctrl.avaliacoes.avaliador.estagio = {
			av2: 0,
			av3: 0,
			av4: 0
		};

		const dataAtual = new Date();
		const hoje = new Date(dataAtual.getFullYear(), dataAtual.getMonth(), dataAtual.getDate());

		Participante.all().$promise
			.then(data => {
				const byConsensoHabilitado = participante => ($state.current.name !== 'main.preenchimento.consensos' || participante.avaliacao.preencheAv3)
																&& participante.avaliacao.situacao !== SituacaoAvaliacao.INATIVA;
				data.filter(byConsensoHabilitado).forEach(participante => {
					if (participante.avaliado.id === myId && (participante.avaliacao.preencheAv1 && participante.limiteAv1)) {
						const dataAv1 = moment(participante.limiteAv1).toDate();

						if (dataAv1.getTime() >= hoje.getTime()) {
							$ctrl.displayAv1 = true;
							$ctrl.avaliacoes.avaliado.push(participante);

							$ctrl.avaliacoes.avaliado.estagio.av1++;
							if (participante.consensoLiberado) {
								if (participante.consensoIniciado) {
									participante.acoes = acoesAv1;

									participante.acoes2 = acoesAv3;
								} else {
									participante.acoes2 = acoesAv3;
									participante.acoes = acoesAv1;
								}
							} else {
								participante.acoes = acoesAv1;
							}

							if (participante.avaliacao.preencheAv1 && participante.preenchidaAV1) {
								totalPreenchidaAv1Avaliado = totalPreenchidaAv1Avaliado + 1;
								$ctrl.avaliacoes.avaliado.estagio.av1 = totalPreenchidaAv1Avaliado;
							}

							if (participante.avaliacao.preencheAv2 && participante.preenchidaAV2) {
								totalPreenchidaAv2Avaliado = totalPreenchidaAv2Avaliado + 1;
								$ctrl.avaliacoes.avaliado.estagio.av2 = totalPreenchidaAv2Avaliado;
							}

							if (participante.avaliacao.preencheAv3 && participante.preenchidaAV3) {
								totalPreenchidaAv3Avaliado = totalPreenchidaAv3Avaliado + 1;
								$ctrl.avaliacoes.avaliado.estagio.av3 = totalPreenchidaAv3Avaliado;
							}

							if (participante.avaliacao.preencheAv4 && participante.preenchidaAV4) {
								totalPreenchidaAv4Avaliado = totalPreenchidaAv4Avaliado + 1;
								$ctrl.avaliacoes.avaliado.estagio.av4 = totalPreenchidaAv4Avaliado;
							}
						} else if (angular.isDefined(participante.limiteAv3)) {
							const dataAv3 = moment(participante.limiteAv3).toDate();

							if (dataAv3.getTime() >= hoje.getTime()) {
								$ctrl.avaliacoes.avaliado.push(participante);

								if (participante.consensoLiberado) {
									if (participante.consensoIniciado) {
										participante.acoes = acoesAv1;
										participante.acoes2 = acoesAv3;
									} else {
										participante.acoes2 = acoesAv3;
										participante.acoes = acoesAv1;
									}
								}

								if (participante.avaliacao.preencheAv3 && participante.preenchidaAV3) {
									totalPreenchidaAv3Avaliado = totalPreenchidaAv3Avaliado + 1;
									$ctrl.avaliacoes.avaliado.estagio.av3 = totalPreenchidaAv3Avaliado;
								}
							}
						}

						// FIXME Como saber que finalizou AV3?
					} else if (participante.avaliador && participante.avaliador.id == myId && participante.avaliacao.preencheAv2 && participante.limiteAv2) {
						const dataAv2 = moment(participante.limiteAv2).toDate();

						if (dataAv2.getTime() >= hoje.getTime()) {
							$ctrl.displayAv2 = true;
							$ctrl.avaliacoes.avaliador.push(participante);
							$ctrl.avaliacoes.avaliador.estagio.av2++;

							if (participante.consensoLiberado) {
								if (participante.consensoIniciado) {
									participante.acoes2 = acoesAv3;
									participante.acoes = acoesAv2;
								} else {
									participante.acoes2 = acoesAv3;
									participante.acoes = acoesAv2;
								}
							} else {
								participante.acoes = acoesAv2;
							}

							// FIXME Como saber que finalizou AV3?
							// FIXME Como saber que finalizou AV4?
							if (participante.preenchidaAV1) {
								if ($ctrl.avaliacoes.avaliado.length > 0) {
									//totalAutoAvaliacao = totalAutoAvaliacao;
								} else {
									totalAutoAvaliacao = totalAutoAvaliacao + 1;
								}

								$ctrl.avaliacoes.avaliador.estagio.av1 = totalAutoAvaliacao;
							}

							if (participante.preenchidaAV2) {
								if ($ctrl.avaliacoes.avaliado.length > 0) {
									//totalAvaliacao = totalAvaliacao;
								} else {
									totalAvaliacao = totalAvaliacao + 1;
								}

								$ctrl.xv2 = totalAvaliacao;
								$ctrl.avaliacoes.avaliador.estagio.av2 = totalAvaliacao;
							}

							if (participante.preenchidaAV3) {
								if ($ctrl.avaliacoes.avaliado.length > 0) {
									//totalConsenso = totalConsenso;
								} else {
									totalConsenso = totalConsenso + 1;
								}

								$ctrl.avaliacoes.avaliador.estagio.av3 = totalConsenso;
							}

							if (participante.preenchidaAV4) {
								if ($ctrl.avaliacoes.avaliado.length > 0) {
									totalRevisor = totalConsenso;
								} else {
									totalRevisor = totalConsenso + 1;
								}

								$ctrl.avaliacoes.avaliador.estagio.av4 = totalRevisor;
							}
						} else if (angular.isDefined(participante.limiteAv3)) {
							const dataAv3 = moment(participante.limiteAv3).toDate();

							if (dataAv3.getTime() >= hoje.getTime()) {
								$ctrl.avaliacoes.avaliador.push(participante);
								if (participante.consensoLiberado) {
									if (participante.consensoIniciado) {
										participante.acoes2 = acoesAv3;
										participante.acoes = acoesAv2;
									} else {
										participante.acoes2 = acoesAv3;
										participante.acoes = acoesAv2;
									}
								}

								if (participante.preenchidaAV3) {
									if ($ctrl.avaliacoes.avaliado.length > 0) {
										//totalConsenso = totalConsenso;
									} else {
										totalConsenso = totalConsenso + 1;
									}

									$ctrl.avaliacoes.avaliador.estagio.av3 = totalConsenso;
								}
							}
						}
					}
				});
				//faz exibir a autoavaliação na aba "POR COLABORADORES" do menu "Avaliações"
				if (angular.isDefined($ctrl.avaliacoes.avaliado) && $ctrl.avaliacoes.avaliado.length > 0) {
					angular.forEach($ctrl.avaliacoes.avaliado, function (value) {
						if (value.avaliado.id == myId && (value.avaliacao.preencheAv1 && value.limiteAv1)) {
							$ctrl.avaliacoes.avaliador.push(angular.copy(value));
						}
					});
				}

				// Avaliado
				const autoAvResultado = (100 * $ctrl.avaliacoes.avaliado.estagio.av1 / $ctrl.avaliacoes.avaliado.length);
				if (!isNaN(autoAvResultado)) {
					$ctrl.calculoGraficoAutoAvaliacoes = autoAvResultado;
				} else {
					$ctrl.calculoGraficoAutoAvaliacoes = 0;
				}

				const conResultado = (100 * $ctrl.avaliacoes.avaliado.estagio.av3 / $ctrl.avaliacoes.avaliado.length);
				if (!isNaN(conResultado)) {
					//				competenciasGestores
					$ctrl.calculoGraficoConsenso = conResultado;
				} else {
					$ctrl.calculoGraficoConsenso = 0;
				}
			});

		$ctrl.verificaStatusAv = item => {
			if (item.idCompetencia && item.percentualPreenchimento === 0) {
				return 'preenchimento-vermelho';
			} else if (item.percentualPreenchimento > 0 && item.percentualPreenchimento < 100) {
				return 'preenchimento-azul';
			} else {
				return 'preenchimento-verde';
			}
		};

		$ctrl.exibirIconeVerde = function (item, tipo) {
			if ($ctrl.user) {
				if (tipo == 'av1') {
					if ($ctrl.user.id == item.avaliado.id && item.avaliacao.limiteAv1 != null && item.limiteAv1 != null &&
						item.dataInicioAv1 != null && item.dataConclusaoAv1 != null) {
						return true;
					}

					return false;
				} else if (tipo == 'av2') {
					if ($ctrl.user.id != item.avaliado.id && item.avaliacao.limiteAv2 != null && item.limiteAv2 != null &&
						item.dataInicioAv2 != null && item.dataConclusaoAv2 != null) {
						return true;
					}

					return false;
				} else if (tipo == 'av3') {
					if (item.avaliacao.limiteAv3 != null && item.limiteAv3 != null && item.dataInicioAv3 != null &&
						item.dataConclusaoAv3 != null) {
						return true;
					}

					return false;
				}
			}
		};

		$ctrl.exibirIconeAzul = function (item, tipo) {
			if ($ctrl.user) {
				if (tipo == 'av1') {
					if ($ctrl.user.id == item.avaliado.id && item.avaliacao.limiteAv1 != null && item.limiteAv1 != null &&
						item.dataInicioAv1 != null && item.dataConclusaoAv1 == null) {
						return true;
					}

					return false;
				} else if (tipo == 'av2') {
					if ($ctrl.user.id != item.avaliado.id && item.avaliacao.limiteAv2 != null && item.limiteAv2 != null &&
						item.dataInicioAv2 != null && item.dataConclusaoAv2 == null) {
						return true;
					}

					return false;
				} else if (tipo == 'av3') {
					if (item.avaliacao.limiteAv3 != null && item.limiteAv3 != null && item.dataInicioAv3 != null && item.dataConclusaoAv3 == null) {
						return true;
					}

					return false;
				}
			}
		};

		$ctrl.exibirIconeVermelho = function (item, tipo) {
			if ($ctrl.user) {
				if (tipo == 'av1') {
					if ($ctrl.user.id == item.avaliado.id && item.avaliacao.limiteAv1 != null && item.limiteAv1 != null &&
						item.dataInicioAv1 == null && item.dataConclusaoAv1 == null) {
						return true;
					}

					return false;
				} else if (tipo == 'av2') {
					if ($ctrl.user.id != item.avaliado.id && item.avaliacao.limiteAv2 != null && item.limiteAv2 != null &&
						item.dataInicioAv2 == null && item.dataConclusaoAv2 == null) {
						return true;
					}

					return false;
				} else if (tipo == 'av3') {
					if (item.avaliacao.limiteAv3 != null && item.limiteAv3 != null && item.dataInicioAv3 == null && item.dataConclusaoAv3 == null) {
						return true;
					}

					return false;
				}
			}
		};

		$ctrl.gaugeAv1 = valor => {
			setTimeout(() => {
				c3.generate({
					bindto: '#gauge-plot3-chart',
					data: {
						columns: [
							['Avaliação', valor]
						],
						type: 'gauge',
					},
					gauge: {

					},
					color: {
						pattern: ['#008000'], // the three color levels for the percentage values.
						threshold: {
							values: []
						}
					},
					size: {
						height: 180
					}
				});
			}, 500);
		};

		$ctrl.gaugeAv2 = valor => {
			setTimeout(() => {
				c3.generate({
					bindto: '#gauge-plot4-chart',
					data: {
						columns: [
							['Consenso', valor]
						],
						type: 'gauge',
					},
					gauge: {

					},
					color: {
						pattern: ['#FF0000'], // the three color levels for the percentage values.
						threshold: {
							values: []
						}
					},
					size: {
						height: 180
					}
				});
			}, 500);
		};

		$ctrl.gaugeAv4 = valor => {
			setTimeout(() => {
				c3.generate({
					bindto: '#gauge-plot5-chart',
					data: {
						columns: [
							['data', valor]
						],
						type: 'gauge',
					},
					gauge: {

					},
					color: {
						pattern: ['#000000'], // the three color levels for the percentage values.
						threshold: {
							values: []
						}
					},
					size: {
						height: 180
					}
				});
			}, 500);
		};

		const url = window.location.href;

		if (url.endsWith('preenchimento/autoavaliacoes/list')) {
			if ($ctrl.avaliacoes.avaliado.length > 0) {
				$ctrl.realizados_v2 = $ctrl.avaliacoes.avaliador.estagio.av2;
				$ctrl.total_v2 = $ctrl.avaliacoes.avaliado.length;

				$ctrl.realizados_v3 = $ctrl.avaliacoes.avaliador.estagio.av3;
				$ctrl.total_v3 = $ctrl.avaliacoes.avaliado.length;

				$ctrl.realizados_v4 = $ctrl.avaliacoes.avaliador.estagio.av4;
				$ctrl.total_v4 = $ctrl.avaliacoes.avaliado.length;

				// Avaliado
				$ctrl.calcularGraficoAv1Avaliador = (100 * $ctrl.avaliacoes.avaliador.estagio.av2 / $ctrl.avaliacoes.avaliado.length);
				$ctrl.gaugeAv1($ctrl.calcularGraficoAv1Avaliador);

				$ctrl.calcularGraficoConsensoAv2valiador = (100 * $ctrl.avaliacoes.avaliador.estagio.av3 / $ctrl.avaliacoes.avaliado.length);
				$ctrl.gaugeAv2($ctrl.calcularGraficoConsensoAv2valiador);

				$ctrl.calcularGraficoRevisaoAv4Avaliador = (100 * $ctrl.avaliacoes.avaliador.estagio.av4 / $ctrl.avaliacoes.avaliado.length);
				$ctrl.gaugeAv4($ctrl.calcularGraficoRevisaoAv4Avaliador);
			}
		} else if (url.endsWith('preenchimento/avaliacoes/list')) {
			if ($ctrl.avaliacoes.avaliador.length > 0) {
				$ctrl.realizados_v2 = 0;
				$ctrl.realizados_v3 = 0;
				$ctrl.total_v3 = 0;

				angular.forEach($ctrl.avaliacoes.avaliador, item => {
					if (item.preenchidaAV2 == true) {
						$ctrl.realizados_v2++;
					}

					if (item.avaliacao.preencheAv3 == true && item.perfilAgente === 'GESTOR') {
						if (item.preenchidaAV3) {
							$ctrl.realizados_v3++;
						}

						$ctrl.total_v3++;
					}
				});

				$ctrl.total_v2 = $ctrl.avaliacoes.avaliador.length;

				$ctrl.realizados_v4 = $ctrl.avaliacoes.avaliador.estagio.av4;
				$ctrl.total_v4 = $ctrl.avaliacoes.avaliador.length;

				// Avaliador
				$ctrl.calcularGraficoAv1Avaliador = (100 * $ctrl.realizados_v2 / $ctrl.avaliacoes.avaliador.length);
				$ctrl.gaugeAv1($ctrl.calcularGraficoAv1Avaliador);

				$ctrl.calcularGraficoConsensoAv2valiador = (100 * $ctrl.realizados_v3 / $ctrl.total_v3);
				$ctrl.gaugeAv2($ctrl.calcularGraficoConsensoAv2valiador);

				$ctrl.calcularGraficoRevisaoAv4Avaliador = (100 * $ctrl.avaliacoes.avaliador.estagio.av4 / $ctrl.avaliacoes.avaliador.length);
				$ctrl.gaugeAv4($ctrl.calcularGraficoRevisaoAv4Avaliador);
			}
		}

		$ctrl.tiposCompetencia = [];
		$ctrl.qtdCompetencias = 0;
		$ctrl.qtdCompetenciasPreenchidas = 0;
		$ctrl.percentualCompetenciasPreenchidas = 0;
		$ctrl.percentualCompetenciasPendentes = 0;

		const competenciasGestores = CompetenciaAvaliador.porAvaliadorEPerfil({
			avaliadorId: myId
		});

		carregarCompetencias(competenciasGestores, 'GESTOR');
	}

	$ctrl.calculosGrafico = () => {
		$ctrl.arrayColunasCompetencias = [];
		$ctrl.arrayColunasTipos = [];
		if ($ctrl.qtdCompetencias > 0) {
			$ctrl.percentualCompetenciasPreenchidas = ($ctrl.qtdCompetenciasPreenchidas * 100) / $ctrl.qtdCompetencias;

			$ctrl.percentualCompetenciasPendentes = 100 - $ctrl.percentualCompetenciasPreenchidas;
			//mudar aqui!
			$ctrl.arrayColunasCompetencias = [
				['Preenchidos', $ctrl.percentualCompetenciasPreenchidas],
				['Pendentes', $ctrl.percentualCompetenciasPendentes]
			];

			$ctrl.tiposCompetencia.forEach(tipo => {
				if ($ctrl.qtdCompetencias > 0 && tipo.qtd > 0) {
					const percentual = (tipo.qtd * 100) / $ctrl.qtdCompetencias;
					const coluna = [tipo.nome, percentual];
					$ctrl.arrayColunasTipos.push(coluna);
				}
			});
		}
	};

	$ctrl.acaoAv1 = () => {
		$state.go('app.preenchimento', {});
	};

	$ctrl.acaoFormacaoIdiomas = () => {
		UserService.logged().$promise
			.then(data => {
				$state.go('formacoesidiomas', {
					objectId: data.id
				});
			}).catch(response => {
				$rootScope.loadingProgress = false;
				$scope.$emit('notify-critical', response);
			});
	};

	load();
}
