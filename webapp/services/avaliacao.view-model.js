import angular from 'angular';

import avaliacaoService from './avaliacao';
import funcionarioService from './funcionario';
import Situacao from '../partials/model/Situacao';
import SituacaoAvaliacao from '../partials/model/SituacaoAvaliacao';
import TipoAvaliacaoPerfil from '../partials/model/TipoAvaliacaoPerfil';
import TipoPendencia from '../partials/model/TipoPendencia';
import TABS from '../partials/avaliacao/util/abas.config';
import * as moment from 'moment';

export default angular.module(
	'models.avaliacao', [avaliacaoService,funcionarioService])
	.factory('AvaliacaoViewModel', avaliacaoViewModelFactory)
	.name;

const noReturn = () => undefined;

class ParticipanteAvaliador {
	constructor({ id, avaliador, perfilAgente, selected }) {
		this.id = id;
		this.avaliador = avaliador;
		this.perfilAgente = perfilAgente;
		this.selected = selected;
	}
}

/* @ngInject */
function avaliacaoViewModelFactory(
	Avaliacao,
	Funcionario,
	Cargo,
	Departamento,
	Pendencia,
	ValidationService,
) {
	class AvaliacaoEditModel {
		constructor({ avaliacao }) {
			this.avaliacao = avaliacao;
			this.tabs = TABS;
			this.callBacks = {
				/* Função para fazer a validação da data limite
				e se existe participantes selecionados */
				validateDataLimite: (listaParticipantes) => {
					const dataLimite = new Date(moment(this.callBacks.getDataLimite()).format('YYYY-MM-DD'));
					const hoje = new Date(moment().format('YYYY-MM-DD'));
					if (moment(hoje).isAfter(moment(dataLimite))) {
						throw {
							type: 'notify-error',
							statusText: 'Atenção!',
							data: 'A data limite expirou.'
						};
					}

					return listaParticipantes;
				},
				validateParticipantes: (listaParticipantes) => {
					if (
						!ValidationService.verificarPreenchimento(listaParticipantes) ||
						listaParticipantes.length <= 0
					) {
						throw {
							type: 'notify-error',
							statusText: 'Atenção!',
							data: 'A avaliação deve ter, pelo menos, um participante selecionado.'
						};
					}

					return listaParticipantes;
				},

				validateAvaliadores: (listaParticipantes) => {
					if (
						!ValidationService.verificarPreenchimento(listaParticipantes) ||
						listaParticipantes.length <= 0 &&
						(this.avaliacao.pares === true
							|| this.avaliacao.gestores === true
							|| this.avaliacao.subordinados === true)
					) {
						throw {
							type: 'notify-error',
							statusText: 'Atenção!',
							data: 'Para prosseguir é obrigatório inserir pelo menos um avaliador.'
						};
					}

					return listaParticipantes;
				},

				/* Obtém a data limite da avaliacao */
				getDataLimite: () => {
					let dataLimite;
					if (this.avaliacao.preencheAv1) {
						this.avaliacao.autoavaliacao = true;
						dataLimite = this.avaliacao.limiteAv1;
					} else if (this.avaliacao.gestores || this.avaliacao.subordinados || this.avaliacao.pares) {
						dataLimite = this.avaliacao.limiteAv2;
					}

					return dataLimite;
				},

			};
			this.updateAvaliados();
		}

		get dataLimite() {
			let dataLimite = undefined;

			if (this.avaliacao.preencheAv1) {
				// TODO Verificar se `this.avaliacao.autoavaliacao` realmente precisa ser (re?)atribuído aqui
				this.avaliacao.autoavaliacao = true;
				dataLimite = this.avaliacao.limiteAv1;
			} else if (this.avaliacao.gestores || this.avaliacao.subordinados || this.avaliacao.pares) {
				dataLimite = this.avaliacao.limiteAv2;
			}

			return dataLimite;
		}

		updateAvaliados() {
			const loadAvaliados = () => {
				this.avaliados = this.avaliacao.participantes
					.map(participante => participante.avaliado)
					// Remover duplicados por ID, se houverem
					.filter((funcionario, index, array) =>
						index === array.findIndex(elem => elem.id === funcionario.id));

				/* Obtém a quantidade de avaliadores inicialmente */
				this.avaliados.forEach(avaliado => {
					const qtd = this.avaliacao.participantes.filter(participante => participante.avaliado.id === avaliado.id).length;
					avaliado.qtdAvaliadores = qtd;
					avaliado.isAllAvaliadoresSelected = true;
				});
				return Promise.resolve();
			};

			/* FIXME: Precisa carregar todos os avaliadores,
			pois o backend interpreta os que nao tiverem
			sido carregados como 'inexistentes' e os remove
			da avaliação!*/
			const loadAllAvaliadores = () => {
				this.avaliados.forEach(avaliado => {
					avaliado.avaliadores = [];
					const avaliadores = this.avaliacao.participantes
						.filter(participante =>
							participante.avaliado.id === avaliado.id &&
							participante.avaliador.id !== avaliado.id)
						.map(participante =>
							new ParticipanteAvaliador({
								id: participante.id,
								avaliador: participante.avaliador,
								perfilAgente: participante.perfilAgente,
								selected: true
							})
						);

					avaliado.avaliadores.push(...avaliadores);
				});
			};

			return loadAvaliados()
				.then(loadAllAvaliadores);
		}

		/* --------------------------- ABA AVALIADOS (PARTICIPANTES) ------------------------------------ */

		/** Verifica se as datas-limite (preenchimento, consenso e revisão) expiraram. */
		isDatasLimiteExpiradas() {
			if (!this.dataLimite) return false;
			const hoje = moment().endOf('day');
			return hoje.isAfter(this.dataLimite);
		}

		/** Carrega a lista `this.funcionarios`, se não existir. */
		loadFuncionarios(options = {}) {
			if (this.funcionarios && !options.force) return Promise.resolve();
			return Funcionario.funcionariosParaAvaliacao({ id: this.avaliacao.id }).$promise
				.then(funcionarios => {
					this.funcionarios = funcionarios;
				});
		}

		/** Carrega a lista `this.cargos`, se não existir. */
		loadCargos(options = {}) {
			if (this.cargos && !options.force) return Promise.resolve();
			return Cargo.all().$promise
				.then(cargos => {
					this.cargos = cargos
						.filter(cargo => {
							return cargo.situacao !== Situacao.INATIVA;
						})
						.sort((a, b) => {
							if (a.nome < b.nome) return -1;
							if (a.nome > b.nome) return 1;
							return 0;
						});
					this.cargos.unshift({ nome: 'TODOS' });
				});
		}

		/* Usado no dialog de avaliadores (autoComplete) */
		findAvaliadores(avaliado, query) {
			const autoCompleteAvaliadores = [];

			return Funcionario.buscar(null, {
				nome: query,
				situacao: 'ATIVA',
				ordenacao: 'nome'
			}).$promise.then((data) => {
				data.forEach(value => {
					//nao pode ser a pessoa selecionada e nem o funcionario que está sendo editado
					if (value.nome !== avaliado.nome) {
						autoCompleteAvaliadores.push(value);
					}
				});
				return autoCompleteAvaliadores;
			}).catch(response => {
				throw (response);
			});
		}

		/** Carrega a lista `this.setores`, se não existir. */
		loadSetores(options = {}) {
			if (this.cargos && !options.force) return Promise.resolve();
			return Departamento.all().$promise
				.then(setores => {
					this.setores = setores
						.filter(setor => {
							return setor.situacao !== Situacao.INATIVA;
						})
						.sort((a, b) => {
							if (a.nome < b.nome) return -1;
							if (a.nome > b.nome) return 1;
							return 0;
						});
					this.setores.unshift({ nome: 'TODOS' });
				});
		}

		loadAvaliados() {
			const loadAvaliadosSelecionados = () => {
				this.avaliacao.participantes.forEach(participante => {
					const indice = this.findFuncionarioOnList(
						this.funcionarios,
						participante.avaliado,
					);
					if (indice >= 0) {
						const avaliado = this.funcionarios[indice];
						avaliado.selected = true;
						/* Essas props indicam que o avaliado (funcionario)
						ja teve sua avaliacao previamente liberada */
						if (participante.idCargoAvaliado || participante.idDeptoAvaliado) {
							avaliado.selectedOld = true;
						}
					}
				});
			};

			return this.loadFuncionarios()
				.then(() => loadAvaliadosSelecionados())
				.then(noReturn);
		}

		/* Caso o funcionario já esteja na lista de avaliados
		(está fora das funções pois é usado diretamente no $ctrl.model
		da aba pendencias ao selecionar um funcionario) */
		findFuncionarioOnList(list, funcionario) {
			let indice = -1;
			indice = list.findIndex(item => {
				return funcionario.id === item.id;
			});
			return indice;
		}

		/* A pendencia pode ser por cargo ou por colaboradores */
		verifyFuncionarioPendencias(funcionario, pendencia) {
			if (
				pendencia.tipoPendencia === TipoPendencia.CARGO &&
				pendencia.cargo.id === funcionario.cargo.id
			) {
				return true;
			} else if (
				pendencia.tipoPendencia == TipoPendencia.COLABORADORES &&
				pendencia.avaliado.id == funcionario.id
			) {
				return true;
			} else {
				return false;
			}
		}

		prepareParticipantes() {
			const updateListaParticipantes = () => {
				const listaParticipantes = [];

				this.funcionarios.forEach((funcionario) => {
					if (funcionario.selected) {
						/* Verifica se o funcionario possui alguma pendencia */
						this.avaliacao.pendencias.forEach((pendencia) => {
							if (this.verifyFuncionarioPendencias(funcionario, pendencia)) {
								funcionario.selected = false;
								return;
							}
						});

						/* Copia as informações do funcionario para o avaliado em participante
						Após isso, o adiciona na lista de participantes a serem salvos*/
						const novoParticipante = this.createParticipanteAvaliado(funcionario, null);
						listaParticipantes.push(novoParticipante);
					}
				});

				return listaParticipantes;
			};

			/* Salva os avaliados adicionados na lista de participantes */
			const saveParticipantes = (listaParticipantes) => {
				this.avaliacao.participantes.splice(0, this.avaliacao.participantes.length, ...listaParticipantes);
				return Avaliacao.salvarParticipantes(this.avaliacao).$promise
					.then(data => {
						this.avaliacao.participantes = data.participantes;
						this.updateAvaliados();
						const openDialog = this.updatePendencias(data);
						return {
							...openDialog,
							notify: {
								type: 'notify-success',
								statusText: '',
								data: 'Avaliados salvos com sucesso!'
							}
						};
					})
					.catch(response => {
						console.error(response);
						console.error('erro ao salvar participantes');
						if (response.data[0].code === 'validation.already-exists') {
							return Promise.reject({
								type: 'notify-error',
								statusText: '',
								data: `O avaliado (a) ${response.data[0].params[1]} já possui agente do tipo ${response.data[0].params[3]}
									com o (a) ${response.data[0].params[2]}`
							});
						} else {
							return Promise.reject({
								type: 'notify-error',
								statusText: response.statusText,
								data: response.data
							});
						}
					});
			};

			return Promise.resolve()
				.then(updateListaParticipantes)
				.then(this.callBacks.validateDataLimite)
				.then(this.callBacks.validateParticipantes)
				.then(saveParticipantes);
		}

		updatePendencias(data) {
			this.avaliacao.pendencias = data.pendencias;
			this.updatePendenciasTabCount();
			if (this.avaliacao.pendencias.length > 0) {
				return ({ openDialogPendencias: true });
			}

			return ({ openDialogPendencias: false });
		}

		updatePendenciasTabCount() {
			this.tabs[2].title = `Pendencia (${this.avaliacao.pendencias.length})`;
		}

		verifyPerfilAgente() {
			if (
				this.avaliacao.autoavaliacao &&
				!this.avaliacao.gestores &&
				!this.avaliacao.subordinados &&
				!this.avaliacao.pares
			) {
				return TipoAvaliacaoPerfil.AUTOAVALIACAO;
			} else if (
				!this.avaliacao.autoavaliacao &&
				this.avaliacao.gestores &&
				!this.avaliacao.subordinados &&
				!this.avaliacao.pares
			) {
				return TipoAvaliacaoPerfil.GESTOR;
			} else if (
				!this.avaliacao.autoavaliacao &&
				!this.avaliacao.gestores &&
				this.avaliacao.subordinados &&
				!this.avaliacao.pares
			) {
				return TipoAvaliacaoPerfil.SUBORDINADO;
			} else if (
				!this.avaliacao.autoavaliacao &&
				!this.avaliacao.gestores &&
				!this.avaliacao.subordinados &&
				this.avaliacao.pares
			) {
				return TipoAvaliacaoPerfil.PARES;
			} else {
				return TipoAvaliacaoPerfil.GESTOR;
			}
		}

		uploadPares(file) {
			/* Transforma o documento excel em um formData (multipart)
				e envia-o para o backend*/
			const saveDocument = (file) => {
				const formData = new FormData();
				formData.append('file', file);
				return Avaliacao.uploadPares(formData).$promise
					.then(response => {
						return response;
					})
					.catch(errResponse => {
						console.error(errResponse);
						throw errResponse;
					});
			};

			/* Faz a validação da planilha inserida */
			const validateSheet = (participantes) => {
				const listaPares = participantes;

				if (!listaPares || listaPares.length == 0) {
					return false;
				}

				return true;
			};

			/* Verifica a validação e copia a avaliacao para os participantes */
			const copyAvaliacao = (participantes) => {
				const listaPares = participantes;

				if (!validateSheet(listaPares)) {
					throw {
						type: 'notify-error',
						statusText: 'Atenção!',
						data: 'A planilha para a importação de pares está vazia.'
					};
				}

				/* Copia a avaliação para os participantes,
					precisa ser feito pois dá erro de nullPointer
					no backend */
				for (const par of listaPares) {
					par.avaliacao = this.avaliacao;
				}

				return listaPares;
			};

			const savePares = (listaPares) => {
				/* FIXME: Ao adicionar um par (avaliado) pela planilha
				 e se ele já estiver previamente selecionado,
				 dá erro de duplicidade no backend! */
				return Avaliacao.salvarPares(listaPares).$promise.then((data) => {
					this.avaliacao.participantes = data.participantes;
					const openDialog = this.updatePendencias(data);

					/* Necessário para atualizar os avaliados selecionados em tela */
					this.loadAvaliados();
					return {
						...openDialog,
						type: 'notify-success',
						statusText: '',
						data: 'Pares importados com sucesso.',

					};
				})
					.catch(response => {
						console.error('erro ao salvar os pares.', response);
						throw {
							type: 'notify-error',
							statusText: response.statusText,
							data: response.data
						};
					});
			};

			return saveDocument(file)
				.then(this.callBacks.validateDataLimite)
				//.then(this.callBacks.validateParticipantes) Nao faz sentido fazer essa validação na planilha!
				.then(copyAvaliacao)
				.then(savePares);
		}

		removePendencia(item) {
			return Pendencia.delete({ id: item.id })
				.$promise.then((res) => {
					const index = this.avaliacao.pendencias.indexOf(item);
					if (index > -1) {
						this.avaliacao.pendencias.splice(index, 1);

						const indice = this.findFuncionarioOnList(
							this.funcionarios,
							item.avaliado,
						);
						if (indice >= 0) {
							const avaliado = this.funcionarios[indice];
							avaliado.selected = false;
						}

						this.updatePendenciasTabCount();
						return Promise.resolve({
							...res,
							type: 'notify-success',
							data: 'Pendência removida com sucesso!',
						});
					} else {
						throw {
							type: 'notify-critical',
							statusText: '',
							data: 'Pendência não encontrada'
						};
					}
				})
				.catch((response) => {
					console.error(response);
					return Promise.reject({
						type: 'notify-critical',
						statusText: response.statusText,
						data: response.data
					});
				});
		}

		createParticipanteAvaliado(funcionario, params) {
			return {
				id: undefined,
				perfilAgente: this.verifyPerfilAgente(),
				avaliado: {
					id: funcionario.id,
					situacaoFuncional: funcionario.situacaoFuncional,
					cargo: { ...funcionario.cargo }
				},
				...params
			};
		}

		selectAvaliado(avaliado) {
			const findAvaliadoOnFuncionarios = (avaliado) => {
				const qtd = this.updateQtdAvaliados(avaliado);

				const isNotAllSelected = isAllSelected();

				return { ...qtd, isAllSelected: !isNotAllSelected };
			};

			const isAllSelected = () => {
				let isNotAllSelected = true;

				/* Se achar alguem que nao esteja 'selected',
				logo, todos nao estarao selecionados */
				isNotAllSelected = this.funcionarios.find(
					participante => !participante.selected,
				);

				return isNotAllSelected;
			};

			return findAvaliadoOnFuncionarios(avaliado);
		}

		updateQtdAvaliados(avaliado) {
			if (!avaliado.selectedOld) {
				if (!avaliado.selected) {
					return { qtdParticipantesInseridos: -1 };
				} else {
					return { qtdParticipantesInseridos: 1 };
				}
			} else {
				if (!avaliado.selected) {
					return { qtdParticipantesRemovidos: 1 };
				} else {
					return { qtdParticipantesRemovidos: -1 };
				}
			}
		}

		toggleAllAvaliados(isAllAvaliadosSelected, filteredList) {
			let qtdParticipantesInseridos = 0;
			let qtdParticipantesRemovidos = 0;
			this.funcionarios.forEach(func => {
				filteredList.forEach(funcFilter => {
					if (func.id === funcFilter.id) {
						func.selected = isAllAvaliadosSelected;
						const qtd = this.updateQtdAvaliados(func);
						if (qtd.qtdParticipantesInseridos) {
							qtdParticipantesInseridos++;
						} else {
							qtdParticipantesRemovidos++;
						}
					}
				});
			});
			return { qtdParticipantesInseridos, qtdParticipantesRemovidos };
		}

		/* --------------------------- ABA AVALIADORES ------------------------------------ */

		loadAvaliadores(avaliado, options = {}) {
			if (avaliado.avaliadores && !options.force) return;
			avaliado.avaliadores = [];
			const avaliadores = this.avaliacao.participantes
				.filter(participante =>
					participante.avaliado.id === avaliado.id &&
					participante.avaliador.id !== avaliado.id)
				.map(participante =>
					new ParticipanteAvaliador({
						id: participante.id,
						avaliador: participante.avaliador,
						perfilAgente: participante.perfilAgente,
						selected: true
					})
				);

			avaliado.avaliadores.push(...avaliadores);
		}

		selectAvaliador(avaliado, avaliador) {
			const findAvaliadoOnFuncionarios = () => {
				// if (avaliador.selected) {
				// 	avaliado.qtdAvaliadores++;
				// } else {
				// 	avaliado.qtdAvaliadores--;
				// }

				const unicoAvaliadorSelecionado = !avaliador.selected
					&& avaliado.avaliadores.filter(avaliador => avaliador.selected).length === 0;
				if (unicoAvaliadorSelecionado) {
					avaliador.selected = true;
					throw {
						type: 'notify-error',
						statusText: '',
						data: 'O participante deve ter pelo menos um avaliador.'
					};
				}
			};

			const isAllSelected = () => {
				/* Se achar alguem que nao esteja 'selected',
				logo, todos nao estarao selecionados */
				const isNotAllSelected = avaliado.avaliadores.find(
					avaliador => !avaliador.selected,
				);
				avaliado.isAllAvaliadoresSelected = !isNotAllSelected;
			};

			return Promise.resolve()
				.then(findAvaliadoOnFuncionarios)
				.then(isAllSelected)
				.then(noReturn);
		}

		toggleAllAvaliadores(avaliado) {
			const toggleAll = () => {
				if (!avaliado.isAllAvaliadoresSelected) {
					avaliado.isAllAvaliadoresSelected = true;
					throw {
						type: 'notify-error',
						statusText: '',
						data: 'Para prosseguir é obrigatório inserir pelo menos um avaliador.'
					};
				}

				avaliado.avaliadores.forEach((avaliador) => {
					avaliador.selected = avaliado.isAllAvaliadoresSelected;
				});
			};

			return Promise.resolve()
				.then(toggleAll)
				.then(noReturn);
		}

		addAvaliadores(avaliado, avaliadores, perfilAgente) {
			const addAvaliador = () => {
				let avaliadoresDuplicados = '';
				avaliadores.forEach(avaliador => {
					const indice = avaliado.avaliadores.findIndex(item => avaliador.id === item.avaliador.id);

					/* Quer dizer que o avaliador não está na lista */
					if (indice < 0) {
						if (!avaliado.avaliadores) {
							avaliado.avaliadores = [];
						}

						avaliado.avaliadores.push(this.createParticipanteAvaliador(avaliador, perfilAgente));
					} else {
						avaliadoresDuplicados = avaliadoresDuplicados + avaliador.nome + ', ';
					}
				});
				if (avaliadoresDuplicados) {
					throw {
						type: 'notify-error',
						statusText: 'Atenção!',
						data: `Os avaliadores ${avaliadoresDuplicados} já foram selecionados para o(a) ${avaliado.nome}.`
					};
				}
			};

			return Promise.resolve()
				.then(addAvaliador)
				.then(noReturn);
		}

		saveAvaliadores(avaliados, acao) {
			const prepareAvaliadores = () => {
				const listaParticipantes = [];
				avaliados.forEach(avaliado => {
					if (avaliado.avaliadores) {
						avaliado.avaliadores.forEach(partAvaliador => {
							// Obter somente os selecionados.
							if (partAvaliador.selected) {
								const participante = this.createParticipanteAvaliado(
									avaliado,
									{
										avaliador: partAvaliador.avaliador,
										id: partAvaliador.id,
										perfilAgente: partAvaliador.perfilAgente
									});
								if (!this.avaliacao.autoavaliacao
									&& !this.avaliacao.gestores
									&& !this.avaliacao.subordinados
									&& this.avaliacao.pares) {
									if (avaliado.avaliadores.length > 1) {
										if (partAvaliador.avaliador.id != avaliado.id) {
											listaParticipantes.push(participante);
										}
									}
								} else {
									listaParticipantes.push(participante);
								}
							}
						});
					}
				});

				return listaParticipantes;
			};

			const persistAvaliadores = (listaParticipantes) => {
				let mensagem = 'Avaliação salva com sucesso!';
				if (acao === 'SALVAR_LIBERAR') {
					this.avaliacao.status = SituacaoAvaliacao.LIBERADA;
					mensagem = 'Avaliação liberada com sucesso!';
				}

				this.avaliacao.participantes.splice(0, this.avaliacao.participantes.length, ...listaParticipantes);
				return Avaliacao.salvarAvaliadores(this.avaliacao).$promise
					.then(data => {
						this.avaliacao.participantes = data.participantes;
						const openDialog = this.updatePendencias(data);
						return {
							...openDialog,
							notify: {
								type: 'notify-success',
								statusText: '',
								data: mensagem
							}
						};
					})
					.catch(response => {
						console.error(response);
						console.error('erro ao salvar avaliadores');
						return Promise.reject({
							type: 'notify-error',
							statusText: response.statusText,
							data: response.data
						});
					});
			};

			return Promise.resolve()
				.then(prepareAvaliadores)
				//.then((lista) => this.updateListaParticipantes(lista))
				.then(this.callBacks.validateDataLimite)
				.then(this.callBacks.validateAvaliadores)
				.then(persistAvaliadores);
		}

		createParticipanteAvaliador(funcionario, perfilAgente) {
			return new ParticipanteAvaliador({
				avaliador: funcionario,
				perfilAgente: perfilAgente,
				selected: true
			});
		}
	}

	class AvaliacaoViewModelFactory {
		load(avaliacaoId) {
			const loadAvaliacao = (values) => Avaliacao.get({ id: avaliacaoId }).$promise
				.then(avaliacao => ({ ...values, avaliacao }));

			const createEditModel = (values) =>
				new AvaliacaoEditModel(values);

			return Promise.resolve({})
				.then(loadAvaliacao)
				.then(createEditModel);
		}
	}

	const instance = new AvaliacaoViewModelFactory();
	return instance;
}
