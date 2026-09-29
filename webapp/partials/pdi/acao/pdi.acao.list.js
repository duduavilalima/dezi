import angular from 'angular';
import dialogAddAcao from './dialogs/dialog.add.acao';
import './dialogs/dialog.add.acao.html';
import dialogAnotacoes from './dialogs/dialog.anotacoes';
import './dialogs/dialog.anotacoes.html';
import dialogIniciaAcaoDesenv from './dialogs/dialog.iniciar.acao';
import './dialogs/dialog.iniciar.acao.html';
import dialogfinalizarAcaoDesenv from './dialogs/dialog.finalizar.acao';
import './dialogs/dialog.finalizar.acao.html';
//import SituacaoPDI from '../../model/SituacaoPDI';
import TABS_PDI from './utils/abas.config';
import SituacaoPDI from '../../model/SituacaoPDI';

/* @ngInject */
export default function pdiAcaoList(
	userRoles,
	user,
	$scope,
	$state,
	$mdDialog,
	DialogWait,
	DialogConfirm,
	navigator
) {
	const $ctrl = this;

	$ctrl.state = $state;
	$ctrl.model = $scope.model = $state.model;

	$ctrl.user = user;
	$ctrl.isFuncionario = $ctrl.user.authorities.some(role => role.authority === 'ROLE_FUNCIONARIO');
	$state.model.loadCompetenciasAvaliado();
	$state.model.loadAvaliacoesAvaliado();
	$scope.$watch(() => $ctrl.state.selectedIndexTab, function() {
		$state.model.clearList();
		$state.model.loadPDI($ctrl.state.selectedIndexTab);
	});
	$state.model.loadFoto();

	$ctrl.tabs = TABS_PDI;

	$ctrl.onTabSelected = tab => {
		$state.go(tab.stateName);
	};

	$ctrl.query = {
		order: 'ad',
		limit: 10,
		page: 1
	};

	$ctrl.AdicionarAcao = () => {
		$mdDialog.show({
			controller: dialogAddAcao,
			templateUrl: 'dialog.add.acao.html',
			parent: angular.element(document.body),
			locals: {
				title: 'Adicionar Ação para Desenvolvimento',
				listCampanhas: $ctrl.model.campanhas,
				listParticipantes: $ctrl.model.listAvaliacaoParticipante,
				listCompetencias: $ctrl.model.competencias,
				funcionario: $ctrl.model.funcionario,
				acao: undefined,
				fn_addAcao: $ctrl.addAcao
			},
			clickOutsideToClose: true
		});
	};

	$ctrl.AdicionarAcaoEdit = item => {
		$mdDialog.show({
			controller: dialogAddAcao,
			templateUrl: 'dialog.add.acao.html',
			parent: angular.element(document.body),
			locals: {
				title: 'Editar Ação para Desenvolvimento',
				listCampanhas: $ctrl.model.campanhas,
				listParticipantes: $ctrl.model.listAvaliacaoParticipante,
				listCompetencias: $ctrl.model.competencias,
				funcionario: $ctrl.model.funcionario,
				acao: item,
				fn_addAcao: $ctrl.addAcao
			},
			clickOutsideToClose: true
		});
	};

	$ctrl.addAcao = (acao) => {
		$state.model.saveOrUpdate(acao, $ctrl.state.selectedIndexTab).then((data) =>{
			$scope.$emit(data.type, data.statusText, data.data);
		});
	};

	$ctrl.Anotacoes = () => {
		$mdDialog.show({
			controller: dialogAnotacoes,
			controllerAs: '$dialog',
			templateUrl: 'dialog.anotacoes.html',
			parent: angular.element(document.body),
			locals: {
				listCampanhas: $ctrl.model.campanhasComPDI,
				listAvaliacaoParticipante: $ctrl.model.listAvaliacaoParticipantePDI
			},
			clickOutsideToClose: true
		});
	};

	$ctrl.iniciarAcaoPDI = acao => {
		$mdDialog.show({
			controller: dialogIniciaAcaoDesenv,
			controllerAs: '$dialog',
			templateUrl: 'dialog.iniciar.acao.html',
			parent: angular.element(document.body),
			locals: {
				acao: acao,
				fn_stateGo:  $ctrl.iniciarAcao
			},
			clickOutsideToClose: true
		});
	};

	$ctrl.finalizarAcaoPDI = acao => {
		$mdDialog.show({
			controller: dialogfinalizarAcaoDesenv,
			controllerAs: '$dialog',
			templateUrl: 'dialog.finalizar.acao.html',
			parent: angular.element(document.body),
			locals: {
				acao: acao,
				fn_stateGo: $ctrl.finalizarAcao
			},
			clickOutsideToClose: true
		});
	};

	/* TODO: FAZER METODO GENERICO PARA PERSISTIR AS TROCAS DE STATUS */
	$ctrl.aprovar = acao => { // FIXME: Traduzi para remover erro no console
		DialogConfirm.show('Aprovar', `Deseja realmente aprovar esta ação: ${acao.acaoParaDesenvolvimento}?`)
			.then(() => {
				acao.situacao = SituacaoPDI.APROVADO;
				const dialog =  DialogWait.showClick('Aguarde...', false);
				$state.model.saveOrUpdate(acao, $ctrl.state.selectedIndexTab).then((data) =>{
					dialog.hide();
					$scope.$emit(data.type, data.statusText, data.data);
					navigator.goAbaAprovados();
				});
				dialog.hide();
			});
	};

	$ctrl.revisado = acao => { // FIXME: Traduzi para remover erro no console
		DialogConfirm.show('DIALOG.REVISAR', 'DIALOG.REVISAR_MSG', { name: acao.acaoParaDesenvolvimento })
			.then(() => {
				const dialog =  DialogWait.showClick('Aguarde...', false);
				$state.model.saveOrUpdate(acao, $ctrl.state.selectedIndexTab).then((data) =>{
					$scope.$emit(data.type, data.statusText, data.data);
				});
				dialog.hide();
			})
			.catch(() => $ctrl.model.loadPDI($ctrl.state.selectedIndexTab));
	};

	$ctrl.remover = acao => { // FIXME: Traduzi para remover erro no console
		DialogConfirm.show('DIALOG.REMOVER', 'DIALOG.REMOVER_MSG', { name: acao.acaoParaDesenvolvimento })
			.then(() => {
				const dialog =  DialogWait.showClick('Aguarde...', false);
				$state.model.delete(acao.id, $ctrl.state.selectedIndexTab)
					.then((data) => {
						dialog.hide();
						$scope.$emit(data.type, data.statusText, data.data);
					})
					.catch(() => dialog.hide());
			});
	};

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH', 'ROLE_GESTOR_AREA'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.back = () => navigator.goList();

	$ctrl.iniciarAcao = (acao) => {
		$state.model.saveOrUpdate(acao, $ctrl.state.selectedIndexTab).then((data) =>{
			$scope.$emit(data.type, data.statusText, data.data);
			navigator.goAbaEmAndamento();
		});
	};

	$ctrl.finalizarAcao = (acao) => {
		$state.model.saveOrUpdate(acao, $ctrl.state.selectedIndexTab).then((data) =>{
			$scope.$emit(data.type, data.statusText, data.data);
			navigator.goAbaFinalizado();
		});
	};

	$ctrl.gerarRelatorio = () => {
		$state.model.getListaRelatorio();
		navigator.goRelatorio($ctrl.model.funcionario.id);
	};
}
