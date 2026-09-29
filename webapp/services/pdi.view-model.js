import angular from 'angular';
import { saveAs } from 'file-saver';
import base64ToBlobFactory from 'utils/file.utils';

import SituacaoPDI, { SituacaoPDIInfo } from '../partials/model/SituacaoPDI';

export default angular.module(
	'models.pdi', [
	])
	.factory('PDIViewModel', pdiViewModelFactory)
	.name;

/* @ngInject */
function pdiViewModelFactory(
	Funcionario,
	FotoResource,
	Cargo,
	PDI,
	AvaliacaoParticipante,
	CompetenciaDetalhamento,
	$translate
) {
	class PDIListModel {
		constructor({ funcionario }) {
			this.funcionario = funcionario;

			this.loadFoto = () => {
				var filtro = {
					funcionario: { id: this.funcionario.id },
					tipo: 'FOTO_PERFIL'
				};

				return FotoResource.buscar(null, filtro).$promise.then((data) => this.croppedImg = data[0].base64);
			};
		}

		loadCompetenciasAvaliado() {
			const listCompetenciasAvaliado = () => {
				return Cargo.cargosCompetencias({ id: this.funcionario.cargo.id })
					.$promise.then((data) => {
						/* Transforma os cargos com as respectivas competencias,
						em apenas competencias */
						const competencias = data.map(cargo => cargo.competencia);

						/* Para cada competencia, obtém a lista de detalhamentos */
						competencias.forEach(competencia => {
							competencia.detalhamentos = [];
							listDetalhamentosCompetencia(competencia.id).then(data => {
								/* Remove o atributo "competencia" dentro do detalhamento */
								data.forEach(detalhamento => delete detalhamento.competencia);
								competencia.detalhamentos = data;
							});
						});
						this.competencias = competencias;
					});
			};

			/* Obtém os detalhamentos do cargo do avaliado */
			const listDetalhamentosCompetencia = (competenciaId) => {
				return CompetenciaDetalhamento.detalhamentosPorCompetencia({ id: competenciaId }).$promise;
			};

			return listCompetenciasAvaliado();
		}

		loadAvaliacoesAvaliado() {
			return AvaliacaoParticipante.PDI({ participanteId: this.funcionario.id })
				.$promise.then((participantes) => {
					this.campanhas = [];
					this.campanhasComPDI = [];
					participantes.forEach(participante => {
						if (!this.campanhas.find(camp => camp.id === participante.avaliacao.campanha.id)) {
							this.campanhas.push(participante.avaliacao.campanha);
						}

						if (participante.pdi && !this.campanhasComPDI.find(camp => camp.id === participante.avaliacao.campanha.id)) {
							this.campanhasComPDI.push(participante.avaliacao.campanha);
						}
					});
					this.listAvaliacaoParticipantePDI = participantes.filter(part => part.pdi);
					this.listAvaliacaoParticipante = participantes;
				});
		}

		/* mapToCampanha(campanhaList) {
			return Array.from(new Set(campanhaList.map(s => s.id)))
				.map(id => {
					return {
						id: id,
						descricao: campanhaList.find(s => s.id === id).descricao
					};
				});
		} */

		clearList() {
			delete this.listaAcao;
		}

		loadPDI(tabIndex) {
			const situacao = SituacaoPDIInfo.elements.find(elm => elm.ordinal === tabIndex);
			return PDI.listarPorSituacao({ id: this.funcionario.id, situacao: situacao ? situacao.value : SituacaoPDI.EM_APROVACAO }).$promise
				.then(data => {
					data.map(pdi => pdi.aprovadoPrintCheckbox = false);
					this.listaAcao = data;
				});
		}
		getListaRelatorio() {
			this.listRelatorios = this.listaAcao.filter(obj => obj.aprovadoPrintCheckbox === true);
			return this.listRelatorios;
		}

		saveOrUpdate(acao, tabIndex) {
			if (acao.id) {
				return PDI.update({ id: acao.id }, acao)
					.$promise
					.then(() => {
						this.loadPDI(tabIndex);
						return {
							type: 'notify-success',
							statusText: '',
							data: 'Ação atualizada com sucesso!'
						};
					})
					.catch((err) => {
						console.error(err);
						const translateCode = err.data[0].code;
						$translate([translateCode]).then(translations => {
							throw {
								type: 'notify-error',
								statusText: 'Atenção!',
								data: translations[translateCode]
							};
						});
					});
			} else {
				return PDI.save(acao)
					.$promise.then(() => {
						//FIXME: Usar o objeto que é retornado do backend (data)
						this.loadPDI();
						return {
							type: 'notify-success',
							statusText: '',
							data: 'Ação salva com sucesso!'
						};
					});
			}
		}

		delete(id, tabIndex) {
			return PDI.excluiracao({ id }).$promise
				.then(() => {
					this.loadPDI(tabIndex);
					return {
						type: 'notify-success',
						statusText: '',
						data: 'Ação para desenvolvimento removida com sucesso!'
					};
				});
		}

		imprimirRelatorio() {
			return PDI.imprimirRelatorio(this.listRelatorios).$promise.then((data) => {
				const hoje = new Date();
				saveAs(
					base64ToBlobFactory(data.responseData, 'pdf'),
					// eslint-disable-next-line max-len
					`relatorio_${this.funcionario.nome}_${hoje.getDate()}-${hoje.getMonth() + 1 < 10 ? '0' + hoje.getMonth() + 1 : hoje.getMonth() + 1}-${hoje.getFullYear()}_${hoje.getHours()}-${hoje.getMinutes()}.pdf`
				);
				return {
					type: 'notify-success',
					statusText: '',
					data: 'Impressão realizada com sucesso!'
				};
			})
				.catch(err => {
					console.error(err);
				});
		}
	}

	class PDIViewModelFactory {
		load(funcionarioId) {
			const loadFuncionario = values => Funcionario.get({ id: funcionarioId }).$promise
				.then((funcionario) => ({ ...values, funcionario }));

			const createListModel = (values) =>
				new PDIListModel(values);

			return Promise.resolve({})
				.then(loadFuncionario)
				.then(createListModel);
		}
	}

	const instance = new PDIViewModelFactory();
	return instance;
}
