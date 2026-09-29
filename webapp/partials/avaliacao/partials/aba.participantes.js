import angular from 'angular';
// import moment from 'moment';
import ROUTES from 'utils/routes';
import { SituacaoFuncionarioInfo } from '../../model/SituacaoFuncionario';
import SituacaoAvaliacao from '../../model/SituacaoAvaliacao';

const STATE_CAMPANHA = ROUTES.MAIN.CAMPANHA.VIEW.name;
const STATE_ABA_PENDENCIAS = ROUTES.MAIN.AVALIACAO.VIEW.PENDENCIAS.name;
const SELECTED_TAB_EVENT = 'selectedIndexTab';

/* @ngInject */
export default function abaParticipantesCadastroCtrl(
	$scope,
	$state,
	$mdDialog,
	DialogWait,
) {
	const $ctrl = this;
	$ctrl.model = $scope.model = $state.model;
	$scope.$emit(SELECTED_TAB_EVENT, $state.selectedIndexTab);

	/* Filtros */
	$ctrl.searchTextColaborador = '';
	$ctrl.selectedFilter = {};
	/* Variaveis para controle de participantes */
	$ctrl.qtdParticipantesInseridos = 0;
	$ctrl.qtdParticipantesRemovidos = 0;

	$ctrl.getSituacaoFuncional = () => {
		$ctrl.situacaoFuncionalList = [...SituacaoFuncionarioInfo.names];
		$ctrl.situacaoFuncionalList.unshift('TODOS');
	};

	/* Check da table, caso todos os funcionários sejam selecionados */
	$ctrl.toggleAllParticipantesCarregamentoTela = () => {
		const isNotAllSelected = $ctrl.model.funcionarios.find(
			func => !func.selected,
		);
		$ctrl.isAllAvaliadosSelected = !isNotAllSelected;
	};

	$ctrl.initializeFilters = () => {
		$state.model.loadCargos();
		$state.model.loadSetores();
		$ctrl.getSituacaoFuncional();
	};

	$ctrl.initializeParticipantes = () => {
		$state.model.loadAvaliados().then(() => {
			$ctrl.toggleAllParticipantesCarregamentoTela();
		});
	};

	$ctrl.initializeFilters();
	$ctrl.initializeParticipantes();

	$ctrl.back = () => {
		return $state.go(STATE_CAMPANHA, {
			objectId: $state.params.campanhaId,
		});
	};

	/********************************* INÍCIO DAS FUNÇÕES DOS FILTROS DA TABELA DE PARTICIPANTES *********************************/
	$ctrl.participanteTableControl = {
		order: 'nome',
		limit: 10,
		page: 1,
	};

	/* Função dos unificada dos filtros da table */
	$ctrl.searchFilter = (colaborador, cargo, departamento, admissao, situacao) => {
		if(!colaborador) {
			colaborador = '';
		}

		if (!cargo || cargo.nome === 'TODOS') {
			cargo = {
				...cargo,
				nome: ''
			};
		}

		if (!departamento || departamento.nome === 'TODOS') {
			departamento = {
				...departamento,
				nome: ''
			};
		}

		if (!situacao || situacao === 'TODOS') {
			situacao = '';
		}

		let dataCargo = '';
		if (admissao) {
			const dt = new Date(admissao);
			const dataIso = dt.toISOString();

			if (dataIso) {
				dataCargo = dataIso.substring(0, 10) + 'T03:00:00.000+0000';
			}
		}

		$ctrl.filtro_participantes = `${colaborador} ${cargo.nome} ${departamento.nome} ${dataCargo} ${situacao}`;
	};

	//AUTOCOMPLETE DO COLABORADOR
	$ctrl.querySearchColaborador = query => {
		$ctrl.flex = 50;

		query = query.toUpperCase();

		let participantes = [];
		participantes = $ctrl.model.funcionarios.filter(filtro => {
			if (filtro.nome.toUpperCase().includes(query)) {
				return filtro;
			}
		});

		return participantes;
	};

	$ctrl.changeSearchTextColaborador = searchTextColaborador => {
		if (searchTextColaborador === '') {
			$ctrl.flex = 20;
			delete $ctrl.selectedFilter.nome;
			$ctrl.searchFilter(
				$ctrl.selectedFilter.nome,
				$ctrl.selectedFilter.cargo,
				$ctrl.selectedFilter.departamento,
				$ctrl.selectedFilter.admissao,
				$ctrl.selectedFilter.situacaoFuncional
			);
		} else {
			$ctrl.flex = 50;
		}
	};

	//Setando o valor selecionado no autocomplete do colaborador
	$ctrl.selectedItemColaboradorChange = item => {
		if (item) {
			$ctrl.flex = 20;
			$ctrl.selectedFilter.nome = item.nome;
			$ctrl.searchFilter(
				$ctrl.selectedFilter.nome,
				$ctrl.selectedFilter.cargo,
				$ctrl.selectedFilter.departamento,
				$ctrl.selectedFilter.admissao,
				$ctrl.selectedFilter.situacaoFuncional
			);
		}
	};

	$ctrl.changeFilter = () => {
		if($ctrl.model.funcionarios.length === $ctrl.filteredList) {
			$ctrl.isAllAvaliadosSelected = true;
		} else {
			$ctrl.isAllAvaliadosSelected = false;
		}

		$ctrl.searchFilter(
			$ctrl.selectedFilter.nome,
			$ctrl.selectedFilter.cargo,
			$ctrl.selectedFilter.departamento,
			$ctrl.selectedFilter.admissao,
			$ctrl.selectedFilter.situacaoFuncional
		);
	};

	/********************************* FIM DAS FUNÇÕES DOS FILTROS DA TABELA DE PARTICIPANTES *********************************/

	/********************************* INÍCIO FUNÇÕES DE CHAMADA DO BACKEND *********************************/

	/* Ao clicar, altera também o
	checkbox da lista de funcionarios */
	$ctrl.onChangeSelecionados = () => {
		if ($ctrl.somenteSelecionados === true) {
			$ctrl.isAllAvaliadosSelected = true;
		} else {
			$ctrl.isAllAvaliadosSelected = false;
		}
	};

	$ctrl.importarPares = ev => {
		let titulo = '';
		let mensagem = '';

		titulo = 'Importar Pares';
		mensagem = 'O arquivo deverá ser importado no formato excel';

		$mdDialog.show({
			controller: 'ImportarParesDialog',
			locals: {
				title: titulo,
				toolBarClassDialog: 'green-bg white-fg',
				msg: mensagem,
				mdActionClassDialog: 'white-bg',
				titleButtonCancel: 'Cancelar',
				titleButtonImport: 'Importar',
				titleButtonConfirm: 'Confirmar',
				fn_upload: $ctrl.uploadPares,
			},
			templateUrl: 'dialog.importar-pares.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
		});
	};

	$ctrl.uploadPares = (file, fn_Hide) => {
		$ctrl.model
			.uploadPares(file)
			.then(response => {
				$ctrl.updateAndShowDialogPendencias(response);
				$scope.$emit(response.type, response.data, response.statusText);
				fn_Hide();
			})
			.catch(err => $scope.$emit(err.type, err.data, err.statusText));
	};

	$ctrl.prepararParticipantes = () => {
		const dialog = DialogWait.showClick(
			'Aguarde salvando os participantes...',
			false,
		);
		$ctrl.model.prepareParticipantes()
			.then(response => {
				$scope.$emit(response.notify.type, response.notify.data, response.notify.statusText);
				$ctrl.updateAndShowDialogPendencias(response);
				dialog.hide();
			})
			.catch(err => {
				dialog.hide();
				$scope.$emit(err.type, err.data, err.statusText);
			});
	};

	$ctrl.updateAndShowDialogPendencias = response => {
		$ctrl.qtdParticipantesInseridos = 0;
		$ctrl.qtdParticipantesRemovidos = 0;

		/* Caso exista pendencias, mostrar o dialog */
		if (response.openDialogPendencias) {
			$ctrl.dialogPendencias();
		}
	};

	/********************************* FIM DAS FUNÇÕES DE CHAMADA DO BACKEND *********************************/

	/********************************* INICIO DAS FUNÇÕES DO DIALOG VALIDAR *********************************/

	$ctrl.validar = ev => {
		const dialogMensagem = $ctrl.formatarMsg();

		$mdDialog.show({
			controller: 'ValidarParticipantesDialog',
			locals: {
				title: dialogMensagem.titulo,
				toolBarClassDialog: 'green-bg white-fg',
				msg: dialogMensagem.mensagem,
				mdActionClassDialog: 'white-bg',
				titleButtonCancel: 'Cancelar',
				titleButtonConfirm: 'Validar',
				fn_salvarParticipantes: $ctrl.prepararParticipantes,
			},
			templateUrl: 'dialog.validar.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
		});
	};

	$ctrl.formatarMsg = () => {
		let titulo = '';
		let mensagem = '';

		titulo = 'Validar avaliados';
		mensagem = `Deseja realmente validar o(s) avaliado(s) selecionado(s)?
		Esta operação pode levar alguns minutos.`;

		if (
			$ctrl.qtdParticipantesRemovidos > 0 &&
			$ctrl.qtdParticipantesInseridos <= 0
		) {
			const confirmacaoMsg = `Ao confirmar,
			o sistema removerá também os questionários distribuídos aos seus avaliados e avaliadores.`;
			if ($ctrl.qtdParticipantesRemovidos === 1) {
				if (
					$ctrl.model.avaliacao.status !==
					SituacaoAvaliacao.AG_LIBERACAO
				) {
					titulo = 'Validar Avaliado';
					mensagem = `Deseja realmente validar a remoção do avaliado? ${confirmacaoMsg}`;
				} else {
					titulo = 'Validar Avaliado';
					mensagem = `Deseja realmente validar a remoção do avaliado?
					Esta operação pode levar alguns minutos.`;
				}
			} else {
				if (
					$ctrl.model.avaliacao.status !==
					SituacaoAvaliacao.AG_LIBERACAO
				) {
					titulo = 'Validar Avaliados';
					mensagem = `Deseja realmente validar a remoção dos avaliados? ${confirmacaoMsg}`;
				} else {
					titulo = 'Validar Avaliados';
					mensagem = `Deseja realmente validar a remoção dos avaliados?
					Esta operação pode levar alguns minutos.`;
				}
			}
		} else if (
			$ctrl.qtdParticipantesRemovidos > 0 &&
			$ctrl.qtdParticipantesInseridos > 0
		) {
			const confirmacaoMsg = `Ao confirmar,
			o sistema validará e removerá os avaliados selecionados.`;
			if (
				$ctrl.qtdParticipantesRemovidos === 1 &&
				$ctrl.qtdParticipantesInseridos === 1
			) {
				titulo = 'Validar Avaliados';
				mensagem = `Deseja realmente validar a inclusão de
						(${$ctrl.qtdParticipantesInseridos}) avaliado e a remoção
						(${$ctrl.qtdParticipantesRemovidos}) avaliado?
						${confirmacaoMsg}`;
			} else if (
				$ctrl.qtdParticipantesRemovidos > 1 &&
				$ctrl.qtdParticipantesInseridos == 1
			) {
				titulo = 'Validar Avaliados';
				mensagem = `Deseja realmente validar a inclusão de
				(${$ctrl.qtdParticipantesInseridos}) avaliado e a remoção
				(${$ctrl.qtdParticipantesRemovidos}) avaliados?
				${confirmacaoMsg}`;
			} else if (
				$ctrl.qtdParticipantesRemovidos === 1 &&
				$ctrl.qtdParticipantesInseridos > 1
			) {
				titulo = 'Validar Avaliados';
				mensagem = `Deseja realmente validar a inclusão de
				(${$ctrl.qtdParticipantesInseridos}) avaliados e a remoção
				(${$ctrl.qtdParticipantesRemovidos}) avaliado?
				${confirmacaoMsg}`;
			} else if (
				$ctrl.qtdParticipantesRemovidos > 1 &&
				$ctrl.qtdParticipantesInseridos > 1
			) {
				titulo = 'Validar Avaliados';
				mensagem = `Deseja realmente validar a inclusão de
				(${$ctrl.qtdParticipantesInseridos}) avaliados e a remoção
				(${$ctrl.qtdParticipantesRemovidos}) avaliados?
				${confirmacaoMsg}`;
			}
		} else if (
			$ctrl.qtdParticipantesRemovidos <= 0 &&
			$ctrl.qtdParticipantesInseridos > 0
		) {
			if ($ctrl.qtdParticipantesInseridos === 1) {
				if (
					$ctrl.model.avaliacao.status !==
					SituacaoAvaliacao.AG_LIBERACAO
				) {
					titulo = 'Validar Avaliado';
					mensagem = `Deseja realmente validar a inclusão do avaliado selecionado?
					A avaliação já foi liberada,
					o sistema validará somente o avaliado que ainda não tiver os questionários distribuídos.`;
				} else {
					titulo = 'Validar Avaliado';
					mensagem = `Deseja realmente validar a inclusão do avaliado selecionado?
					Esta operação pode levar alguns minutos.`;
				}
			} else {
				if (
					$ctrl.model.avaliacao.status !==
					SituacaoAvaliacao.AG_LIBERACAO
				) {
					titulo = 'Validar Avaliados';
					mensagem = `Deseja realmente validar a inclusão dos avaliados selecionados?
					A avaliação já foi liberada,
                    o sistema validará somente os avaliados que ainda não tiveram os questionários distribuídos.`;
				} else {
					titulo = 'Validar Avaliados';
					mensagem = `Deseja realmente validar a inclusão dos avaliados selecionados?
					Esta operação pode levar alguns minutos.`;
				}
			}
		} else if (
			$ctrl.qtdParticipantesRemovidos == 0 &&
			$ctrl.qtdParticipantesInseridos == 0
		) {
			let contador = 0;

			for (const avaliado of $ctrl.model.funcionarios) {
				if (avaliado.selected) {
					contador++;
					if (contador > 1) {
						break;
					}
				}
			}

			if (contador == 1) {
				titulo = 'Validar avaliados';
				mensagem = `Deseja realmente validar o avaliado selecionado.
				Esta operação pode levar alguns minutos.`;
			} else if (contador > 1) {
				titulo = 'Validar avaliados';
				mensagem = `Deseja realmente validar os avaliados selecionados?
				Esta operação pode levar alguns minutos.`;
			}
		}

		return { titulo, mensagem };
	};

	/********************************* FIM DAS FUNÇÕES DO DIALOG VALIDAR *********************************/

	/********************************* INICIO DAS FUNÇÕES DO DIALOG PENDENCIAS *********************************/

	$ctrl.dialogPendencias = ev => {
		const titulo = 'Atenção';
		let mensagem = '';

		if ($ctrl.model.avaliacao.pendencias) {
			if ($ctrl.model.avaliacao.pendencias.length === 1) {
				mensagem = `Foi detectada ${$ctrl.model.avaliacao.pendencias.length}
				pendência. Para liberar esta avaliação é necessário resolvê-la.`;
			}

			if ($ctrl.model.avaliacao.pendencias.length > 1) {
				mensagem = `Foram detectadas ${$ctrl.model.avaliacao.pendencias.length}
				pendências. Para liberar esta avaliação é necessário resolvê-la.`;
			}
		}

		$mdDialog.show({
			controller: 'ValidarPendenciasDialog',
			locals: {
				title: titulo,
				toolBarClassDialog: 'red-bg white-fg',
				msg: mensagem,
				mdActionClassDialog: 'white-bg',
				fn_goPendencias: () =>
					$state.go(STATE_ABA_PENDENCIAS, {
						pendencias: $ctrl.model.avaliacao.pendencias,
					}),
			},
			templateUrl: 'dialog.validar-pendencias.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
		});
	};

	$ctrl.query = {
		order: 'nome',
	};

	$ctrl.somenteSelecionados = false;

	/********************************* INICIO DAS FUNÇÕES DOS CHECKBOXES DA TABELA DE PARTICIPANTES *********************************/

	$ctrl.toggleAllFuncionarios = () => {
		const response = $ctrl.model.toggleAllAvaliados($ctrl.isAllAvaliadosSelected, $ctrl.filteredList);
		$ctrl.updateQtds(response);
	};

	$ctrl.onChangeList = (event, avaliado) => {
		const response = $ctrl.model.selectAvaliado(avaliado);
		$ctrl.isAllAvaliadosSelected = response.isAllSelected;
		$ctrl.updateQtds(response);
	};

	$ctrl.updateQtds = (response) => {
		if (response.qtdParticipantesInseridos) {
			$ctrl.qtdParticipantesInseridos =
				$ctrl.qtdParticipantesInseridos +
				response.qtdParticipantesInseridos;
		} else {
			$ctrl.qtdParticipantesRemovidos =
				$ctrl.qtdParticipantesRemovidos +
				response.qtdParticipantesRemovidos;
		}
	};

	/********************************* FIM DAS FUNÇÕES DOS CHECKBOXES DA TABELA DE PARTICIPANTES *********************************/
}
