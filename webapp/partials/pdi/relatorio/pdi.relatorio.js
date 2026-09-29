//import ROUTES from 'utils/routes';
import { saveAs } from 'file-saver';
import base64ToBlobFactory from 'utils/file.utils';

/* @ngInject */
export default function pdiRelatorioIndividual(
	$scope,
	userRoles,
	listFuncionarios,
	listSetores,
	listCargos,
	listCompetencia,
	listSituacao,
	listCampanhas,
	PDI,
) {
	const $ctrl = this;
	$ctrl.comboFuncionario = listFuncionarios;
	$ctrl.isLoading = false;

	$ctrl.comboSetor = listSetores;

	$ctrl.comboCargo = listCargos;

	$ctrl.comboCompetencia = listCompetencia;

	$ctrl.comboSituacao = listSituacao;

	$ctrl.comboCampanha = listCampanhas;

	$ctrl.tableControl = {
		order: 'nome',
		limit: 10,
		page: 1
	};

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.initializeFilters = () => {
		$ctrl.selectedFilter = {
			dataInicial: undefined,
			dataFinal: undefined,
			funcionario: undefined,
			campanha: undefined,
			competencia: undefined,
			cargo: undefined,
			situacao: undefined,
			setor: undefined
		};
	};

	//AUTOCOMPLETE DO COLABORADOR
	$ctrl.querySearch = (query, entity) => {
		query = query.toUpperCase();

		let list = [];

		if(entity === 'funcionario') {
			list = $ctrl.comboFuncionario;
		}

		if(entity === 'setor') {
			list = $ctrl.comboSetor;
		}

		if(entity === 'cargo') {
			list = $ctrl.comboCargo;
		}

		if(entity === 'competencia') {
			list = $ctrl.comboCompetencia;
		}

		return list.filter(filtro => {
			if(entity !== 'competencia') {
				if (filtro.nome.toUpperCase().includes(query)) {
					return filtro;
				}
			} else {
				if (filtro.descricaoAbreviada.toUpperCase().includes(query)) {
					return filtro;
				}
			}
		});
	};

	$ctrl.changeSearchText = (searchText, entity) => {
		if(entity === 'funcionario') {
			if (searchText === '') {
				$ctrl.selectedFilter.funcionario = $ctrl.comboFuncionario[0];
			}
		}

		if(entity === 'setor') {
			if (searchText === '') {
				$ctrl.selectedFilter.setor = $ctrl.comboSetor[0];
			}
		}

		if(entity === 'cargo') {
			if (searchText === '') {
				$ctrl.selectedFilter.cargo = $ctrl.comboCargo[0];
			}
		}

		if(entity === 'competencia') {
			if (searchText === '') {
				$ctrl.selectedFilter.competencia = $ctrl.comboCompetencia[0];
			}
		}
	};

	$scope.onSearchChange = function(event) {
		event.stopPropagation();
	};

	//Setando o valor selecionado no autocomplete do colaborador
	$ctrl.selectedItemChange = (item, entity) => {
		if(entity === 'funcionario') {
			if (item) {
				$ctrl.selectedFilter.funcionario = item;
			}
		}

		if(entity === 'setor') {
			if (item) {
				$ctrl.selectedFilter.setor = item;
			}
		}

		if(entity === 'cargo') {
			if (item) {
				$ctrl.selectedFilter.cargo = item;
			}
		}

		if(entity === 'competencia') {
			if (item) {
				$ctrl.selectedFilter.competencia = item;
			}
		}
	};

	$scope.handleChangeCampanha = () => {
		if(!$ctrl.selectedFilter.campanha.id) {
			$ctrl.comboAvaliacao = undefined;
		} else {
			$ctrl.comboAvaliacao = $ctrl.selectedFilter.campanha.avaliacoes;
		}
	};

	$ctrl.filtrarRelatorio = () => {
		$ctrl.isLoading = true;
		$ctrl.isAllPDISelected = false;
		/* 	const filtro = {
			competencia: $ctrl.selectedFilter.competencia,
			situacao: $ctrl.selectedFilter.situacao === 'TODOS'? null : $ctrl.selectedFilter.situacao,
			dataInicio: $ctrl.selectedFilter.dataInicial,
			dataFim: $ctrl.selectedFilter.dataFinal,
			campanha: $ctrl.selectedFilter.campanha,
			avaliacao: $ctrl.selectedFilter.avaliacao,
			cargo: $ctrl.selectedFilter.cargo,
			departamento: $ctrl.selectedFilter.setor,
			avaliado: $ctrl.selectedFilter.funcionario
		}; */
		
		PDI.filtrarRelatorio($ctrl.selectedFilter).$promise.then((data) =>{
			$ctrl.isLoading = false;
			$ctrl.acoesPDI = data;
		});
	};

	$ctrl.exportarRelatorio = () => {
		$ctrl.isLoading = true;
		const acoes = $ctrl.acoesPDI.filter(pdi => pdi.isSelected);
		if(acoes.length === 0) {
			$scope.$emit('notify-error', 'Selecione pelo menos uma ação!');
			$ctrl.isLoading = false;
			return;
		}

		PDI.exportarRelatorio(acoes).$promise.then((data) => {
			const hoje = new Date();
			saveAs(
				base64ToBlobFactory(data.responseData, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'),
				// eslint-disable-next-line max-len
				`relatorio_${hoje.getDate()}-${hoje.getMonth()+1 < 10? '0' + hoje.getMonth()+1 : hoje.getMonth()+1}-${hoje.getFullYear()}_${hoje.getHours()}-${hoje.getMinutes()}.xlsx`
			);

			$ctrl.isLoading = false;
			$scope.$emit('notify-success', 'Ação exportada com sucesso!');
		})
			.catch(err => {
				console.error(err);
				$ctrl.isLoading = false;
				$scope.$emit('notify-error', err);
			});
	};

	$ctrl.clearFilter = () => {
		$ctrl.initializeFilters();
	};

	$ctrl.toggleAllPDI = () => {
		if($ctrl.isAllPDISelected) {
			$ctrl.acoesPDI.forEach(pdi => pdi.isSelected = true);
		} else {
			$ctrl.acoesPDI.forEach(pdi => pdi.isSelected = false);
		}
	};

	$ctrl.handleChangeSelected = () => {
		const isSelected = $ctrl.acoesPDI.filter(pdi => pdi.isSelected === true);
		if(isSelected.length !== $ctrl.acoesPDI.length) {
			$ctrl.isAllPDISelected = false;
		} else {
			/* Thus, is all selected */
			$ctrl.isAllPDISelected = true;
		}
	};

	$ctrl.initializeFilters();
}
