import angular from 'angular';

import './dialog_associar_tipo.html';

export default angular.module('app.cargo.dialogs', [])
	.service('CargoDialogs', cargoDialogsFactory)
	.name;

/* @ngInject */
function cargoDialogsFactory($mdDialog) {
	const service = this;

	service.associarTipoCompetencia = (cargo, tiposAssociados, cargoTipoCompetencia = undefined) => {
		let tiposAssociadosIds = tiposAssociados.map(cargoTipo => cargoTipo.tipoCompetencia.id);
		
		if (cargoTipoCompetencia) {
			tiposAssociadosIds = tiposAssociadosIds.filter(tipo => tipo != cargoTipoCompetencia.tipoCompetencia.id);
		}

		const listTipos = (TipoCompetencia) => {
			return TipoCompetencia.obterSemPai().$promise.then(todosTipos => {
				return todosTipos.filter(tipo => !tiposAssociadosIds.includes(tipo.id));
			});
		};

		return $mdDialog.show({
			controller: DialogAssociarTipoCompetenciaController,
			templateUrl: 'dialog_associar_tipo.html',
			locals: {
				cargo: cargo,
				cargoTipoCompetencia: cargoTipoCompetencia
			},
			resolve: {
				tiposCompetencias: listTipos
			},
			clickOutsideToClose: true
		});
	};

	/* @ngInject */
	const DialogAssociarTipoCompetenciaController = function(
		$scope, 
		$mdDialog, 
		CargoTipoCompetencia, 
		TipoCompetencia, 
		cargo, 
		tiposCompetencias,
		cargoTipoCompetencia
	) {
		$scope.listDeTipos = tiposCompetencias;

		if (cargoTipoCompetencia) {
			$scope.object = {};
			$scope.object.id = cargoTipoCompetencia.id;
			$scope.object.tipoCompetencia = $scope.listDeTipos.find(data => data.id === cargoTipoCompetencia.tipoCompetencia.id);
			$scope.object.peso = cargoTipoCompetencia.peso;
			$scope.object.situacao = cargoTipoCompetencia.situacao;
			$scope.object.cargo = cargo;
		}else {
			$scope.object = CargoTipoCompetencia.get();
			$scope.object.$promise.then(() => $scope.object.cargo = cargo);
		}

		$scope.comboSubTipo = false;

		$scope.onChangeSubTipo = () => {
			if($scope.object.tipoCompetencia)
				TipoCompetencia.obterFilhos({ id: $scope.object.tipoCompetencia.id })
					.$promise.then(data => {
						$scope.listDeSubTipos = [];
						if (data.length > 0) {
							$scope.comboSubTipo = true;
							$scope.listDeSubTipos = data;
						} else {
							$scope.comboSubTipo = false;
							$scope.listDeSubTipos = data;
						}
					});
		};

		if (cargoTipoCompetencia) {
			$scope.save = () => {
				if ($scope.comboSubTipo == true) {
					$scope.object.tipoCompetencia = $scope.object.subTipoCompetencia;
				}
	
				CargoTipoCompetencia.update({id: $scope.object.id},$scope.object).$promise.then(cargoTipoComp => $mdDialog.hide(cargoTipoComp));
			};
		}else {
			$scope.save = () => {
				if ($scope.comboSubTipo == true) {
					$scope.object.tipoCompetencia = $scope.object.subTipoCompetencia;
				}
	
				$scope.object.$save().then(cargoTipoComp => $mdDialog.hide(cargoTipoComp));
			};
		}

		$scope.cancel = $mdDialog.cancel;

		return service;
	};
}
