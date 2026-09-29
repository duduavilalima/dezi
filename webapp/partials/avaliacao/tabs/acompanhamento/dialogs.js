import angular from 'angular';

import './dialog_adicionar_participante.html';
import './dialog_alterar_colaborador.html';
import './dialog_alterar_data_limite.html';
import './dialog_add_tipo_competencia.html';

/* @ngInject */
function AcompanhamentoDialogsService($scope, $mdDialog, $mdMedia) {
	this.showDialogAdicionarParticipante = (ev) => {
		/* @ngInject */
		function dialogCtrl($scope, $mdDialog) {
			$scope.cancel = $mdDialog.cancel;
			$scope.confirmar = $mdDialog.hide;
		}

		$mdDialog.show({
			controller: dialogCtrl,
			templateUrl: 'dialog_adicionar_participante.html',
			parent: angular.element(document.body),
			openFrom: angular.element('#table-list'),
			closeTo: angular.element('#table-list'),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	this.showDialogAlterarColaborador = (ev, index, item) => {
		/* @ngInject */
		function dialogCtrl($scope, $mdDialog, object, msg, title) {
			$scope.title = title;
			$scope.object = object;
			$scope.msg = msg;

			$scope.cancel = $mdDialog.cancel;
			$scope.confirmar = $mdDialog.hide;
		}

		var title = $scope.object.preencheAv4 ? 'Alterar Avaliador/Revisor' : 'Alterar Avaliador';

		$mdDialog.show({
			controller: dialogCtrl,
			locals: {
				object: item,
				msg: '',
				index: index,
				title: title
			},
			templateUrl: 'dialog_alterar_colaborador.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: $mdMedia('xs') //TODO Faz sentido isso?
		});
		$scope.$watch(function () {
			return $mdMedia('xs');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	this.showDialogAlterarDatasLimite = (ev, index, item) => {
		/* @ngInject */
		function dialogCtrl($scope, $mdDialog, object, title) {
			$scope.object = object;
			$scope.title = title;

			$scope.cancel = $mdDialog.cancel;
			$scope.confirmar = $mdDialog.hide;
		}

		$mdDialog.show({
			controller: dialogCtrl,
			locals: {
				object: item,
				index: index,
				title: 'Alterar Datas Limites'
			},
			templateUrl: 'dialog_alterar_data_limite.html',
			parent: angular.element(document.body),
			openFrom: angular.element('#table-list'),
			closeTo: angular.element('#table-list'),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	this.showDialogAdicionarTipoCompetencia = (ev) => {
		/* @ngInject */
		function dialogCtrl(
			$scope,
			$mdDialog,
			listaTiposCompetenciaOriginal,
			listaTiposCompetenciaSelecionadas,
			title,
			toolBarClassDialog
		) {
			const tipoCompetenciaList = [];

			$scope.title = title;
			$scope.toolBarClassDialog = toolBarClassDialog;
			$scope.object = {};

			listaTiposCompetenciaOriginal.forEach(original => {
				var encontrou = false;

				listaTiposCompetenciaSelecionadas.forEach(function (selecionado) {
					if (original.id == selecionado.id) {
						encontrou = true;
					}
				});

				if (!encontrou) {
					tipoCompetenciaList.push(angular.copy(original));
				}
			});

			$scope.cancel = $mdDialog.cancel;
			$scope.confirmar = function () {
				$scope.object.tipoCompetencia.gestor = false;
				$scope.object.tipoCompetencia.subordinados = false;
				$scope.object.tipoCompetencia.pares = false;
				$scope.listaTiposCompetenciaSelecionadas.push($scope.object.tipoCompetencia);
				$mdDialog.cancel();
			};
		}

		$mdDialog.show({
			controller: dialogCtrl,
			locals: {
				listaTiposCompetenciaOriginal: $scope.listaTiposCompetencias,
				listaTiposCompetenciaSelecionadas: $scope.object.tiposCompetencia,
				toolBarClassDialog: 'primary-bg',
				title: 'Adicionar Tipo de competência'
			},
			templateUrl: 'dialog_add_tipo_competencia.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: $mdMedia('xs')
		});
	};
}

export default AcompanhamentoDialogsService;
