import angular from 'angular';
import ROUTES from 'utils/routes';
import DialogArvoreCtrl from './dialogs/dialogTipoCompCtrl';

/* @ngInject */
export default function competenciaNewCtrl(
	$state,
	$scope,
	$mdMedia,
	$mdDialog,
	Competencia,
	TipoCompetencia
) {
	const $ctrl = this;
	$ctrl.object = undefined;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	const loadObject = () => {
		Competencia.get({ id: $state.params.objectId }).$promise
			.then(data => {
				$ctrl.object = data;
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.update = () => {
		$ctrl.action = true;
		$ctrl.object.$update($ctrl.object)
			.then(() => {
				$scope.$emit('notify-success', '', 'Competência atualizada com sucesso!');
				$ctrl.back();
			})
			.catch(response => {
				$ctrl.action = false;
				$scope.$emit('notify-critical',response);
			});
	};

	const addTipoDeCompetencia = (selectedNode, lastSelected, fn_Hide) => {
		const tipoCompetencia = {};
		if (selectedNode) {
			tipoCompetencia.id = selectedNode.id;
			tipoCompetencia.nome = selectedNode.nome;
			tipoCompetencia.descricao = selectedNode.descricao;
			$ctrl.object.tipo = tipoCompetencia;
		}

		fn_Hide();
	};

	$ctrl.displayTipoDeCompetencia = (ev) => {
		let noArvore;
		if (angular.isDefined($ctrl.object.tipo)) {
			noArvore = {};
			noArvore.id = $ctrl.object.tipo.id;
			noArvore.nome = $ctrl.object.tipo.nome;
			noArvore.descricao = $ctrl.object.tipo.descricao;
			noArvore.situacao = $ctrl.object.tipo.situacao;
			noArvore.selected = true;
		} else {
			noArvore = undefined;
		}

		const useFullScreen = $mdMedia('xs');
		$mdDialog.show({
			controller: DialogArvoreCtrl,
			controllerAs: '$dialog',
			locals: {
				title: 'Tipo de competência',
				object: noArvore,
				fnSave: addTipoDeCompetencia,
				TipoCompetencia: TipoCompetencia,
			},
			templateUrl: 'dialog.tipo.de.competencia.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen,
		});
		$scope.$watch(
			() => {
				return $mdMedia('xs');
			},
			(wantsFullScreen) => {
				$scope.customFullscreen = wantsFullScreen === true;
			}
		);
	};

	$ctrl.back = () => $state.go(ROUTES.MAIN.CADASTRO.COMPETENCIA.VIEW.name, { objectId: $state.params.objectId });

	loadObject();
}
