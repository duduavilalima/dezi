import angular from 'angular';
import ROUTES from 'utils/routes';
import {TipoAvaliacaoPerfilInfo} from 'partials/model/TipoAvaliacaoPerfil';

const STATE_CAMPANHA = ROUTES.MAIN.CAMPANHA.VIEW.name;
const SELECTED_TAB_EVENT = 'selectedIndexTab';

/* @ngInject */
export default function abaAvaliadoresViewCtrl(
	$scope,
	$state,
	$mdDialog,
	$mdMedia,
	DialogWait,
	DialogConfirm
) {
	const $ctrl = this;
	$ctrl.object = $ctrl.avaliacao = $state.object;
	$ctrl.model = $scope.model = $state.model;
	$ctrl.listaTiposAgentes = TipoAvaliacaoPerfilInfo.values;
	$ctrl.promises = {};
	$ctrl.query = {};

	$ctrl.avaliadorTableControl = {
		order: 'nome',
		page: 1
	};

	$scope.$emit(SELECTED_TAB_EVENT, $state.selectedIndexTab);

	$ctrl.init = () => {
		$ctrl.canAddAvaliadores = $ctrl.disableAddAvaliadores();
		$ctrl.avaliado = undefined;
	};

	$ctrl.notificarErro = ex => {
		$scope.$emit('notify-critical', ex);
	};

	$ctrl.getQtdAvaliadoresStr = (avaliado) => {
		if (avaliado.avaliadoresSelection) return `${avaliado.avaliadoresSelection.length}`;
		return '-';
	};

	$ctrl.onMostrarAvaliadores = (avaliado) => {
		$ctrl.avaliado = avaliado;
		$ctrl.model.loadAvaliadores(avaliado);
	};

	$ctrl.back = () => {
		return $state.go(STATE_CAMPANHA, {
			objectId: $state.params.campanhaId,
		});
	};

	$ctrl.onOcultarAvaliadores = () => {
		$ctrl.avaliado = undefined;
	};

	$ctrl.onToggleAllAvaliadores = (avaliado) => {
		$ctrl.model.toggleAllAvaliadores(avaliado)
			.catch(err => $scope.$emit(err.type, err.data, err.statusText));
	};

	$ctrl.disableAddAvaliadores = () => {
		if($ctrl.model.avaliacao.status == 'INATIVA'
		|| $ctrl.model.avaliacao.status == 'CANCELADA'
		||  $ctrl.model.avaliacao.status == 'CONCLUIDA') {
			return true;
		}

		return false;
	};

	$ctrl.onChangeListAvaliadores = (event, avaliado, avaliador) => {
		$ctrl.model.selectAvaliador(avaliado, avaliador)
			.catch(err => $scope.$emit(err.type, err.data, err.statusText));
	};

	$ctrl.openDialogAddAvaliadores = (ev, avaliado) => {
		const useFullScreen = $mdMedia('xs') || $mdMedia('sm');

		$mdDialog.show({
			controller: 'AdicionarAvaliadorDialog',
			locals: {
				avaliacao: $ctrl.object,
				avaliado: avaliado,
				title: 'Adicionar Avaliador',
				toolBarClassDialog: 'activate-bg',
				tiposDeAgentes: $ctrl.listaTiposAgentes,
				pendencias: $ctrl.avaliacao.pendencias,
				fnFindAvaliadores: $ctrl.findAvaliadores,
				fnAddAvaliadores: $ctrl.addAvaliadores
			},
			templateUrl: 'dialog.add-avaliadores.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen,
		});

		/* $ctrl.$watch(
			function() {
				return $mdMedia('xs') || $mdMedia('sm');
			},
			function(wantsFullScreen) {
				$ctrl.customFullscreen = wantsFullScreen === true;
			}
		); */
	};

	$ctrl.findAvaliadores = (query) => {
		return $ctrl.model.findAvaliadores($ctrl.avaliado, query);
	};

	$ctrl.addAvaliadores = (avaliadores, perfilAgente, fn_Hide, error) => {
		if(error) {
			$scope.$emit(error.type, error.data, error.statusText);
			return;
		}

		$ctrl.model.addAvaliadores($ctrl.avaliado, avaliadores, perfilAgente)
			.then(() => fn_Hide())
			.catch(err => $scope.$emit(err.type, err.data, err.statusText));
	};

	$ctrl.salvarAvaliadores = () => {
		const dialog =  DialogWait.showClick('Aguarde salvando os avaliadores...', false);
		$ctrl.model.saveAvaliadores($ctrl.model.avaliados)
			.then(response => {
				$scope.$emit(response.notify.type, response.notify.data, response.notify.statusText);
				dialog.hide();
				if (response.openDialogPendencias) {
					$ctrl.dialogPendencias();
				}
			})
			.then(() => {
				//FIXME: Recarregar a lista
				window.location.reload(false);
			})
			.catch(err => {
				dialog.hide();
				$scope.$emit(err.type, err.data, err.statusText);
			});
	};

	$ctrl.salvarELiberarAvaliadores = () => {
		const dialog =  DialogWait.showClick('Aguarde, distribuindo avaliações...', false);

		$ctrl.model.saveAvaliadores($ctrl.model.avaliados, 'SALVAR_LIBERAR')
			.then(response => {
				$scope.$emit(response.notify.type, response.notify.data, response.notify.statusText);
				dialog.hide();
				if (response.openDialogPendencias) {
					$ctrl.dialogPendencias();
				}
			})
			.catch(err => {
				dialog.hide();
				$scope.$emit(err.type, err.data, err.statusText);
			});
	};

	$ctrl.salvarLiberar = () => {
		DialogConfirm.show('DIALOG.LIBERAR_TITLE', 'DIALOG.MSG_LIBERAR_AVALIADORES', { name: 'avaliação' })
			.then(() => {
				const dialog =  DialogWait.showClick('Aguarde, distribuindo avaliações...', false);

				$ctrl.model.saveAvaliadores($ctrl.model.avaliados, 'SALVAR_LIBERAR')
					.then(response => {
						$scope.$emit(response.notify.type, response.notify.data, response.notify.statusText);
						dialog.hide();
						if (response.openDialogPendencias) {
							$ctrl.dialogPendencias();
						}
					})
					.catch(err => {
						dialog.hide();
						$scope.$emit(err.type, err.data, err.statusText);
					});
			});
	};

	$ctrl.init();
}
