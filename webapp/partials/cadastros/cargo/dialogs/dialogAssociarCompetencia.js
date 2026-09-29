import angular from 'angular';

import './dialogassociarcompetencia.html';
import Situacao from '../../../model/Situacao';

export default angular.module('app.cargo.competencia', [])
	.service('DialogAssociarCompetencia', DialogAssociarCompetenciaFactory)
	.name;

/* @ngInject */
function DialogAssociarCompetenciaFactory(
	$mdDialog,
	Competencia,
	CargoCompetencia,
	DetalhamentoCargoCompetencia,
	ValidationService
) {
	const service = this;

	service.associarCompetencia = (listCargoCompetencias, cargo, objCargoCompetencia) => {
		const getTamanhoListaCargoCompetencia = () => {
			let tamanhoLista = 0;
			if (ValidationService.verificarPreenchimento(listCargoCompetencias)) {
				tamanhoLista = listCargoCompetencias.length;
			}

			return tamanhoLista;
		};

		return $mdDialog.show({
			controller: DialogAssociarCompetencia,
			templateUrl: 'dialogassociarcompetencia.html',
			locals: {
				cargo: cargo,
				objCargoCompetencia: objCargoCompetencia,
				tamanhoLista: getTamanhoListaCargoCompetencia(),
				listCargoCompetencias: listCargoCompetencias,
			},
			clickOutsideToClose: true,
			fullscreen: false
		});
	};

	/* @ngInject */
	function DialogAssociarCompetencia(
		$scope,
		$mdDialog,
		cargo,
		objCargoCompetencia,
		tamanhoLista,
		listCargoCompetencias,
		$rootScope,
		$stateParams,
	) {
		load();

		$scope.cancel = () => {
			$mdDialog.cancel();
		};

		$scope.onChangeCompetencia = () => {
			obterListaDetalhamentosPorCompetencia();
		};

		$scope.save = () => {
			const invalido = $scope.listDetalhamentosCargoComp.some(value => {
				if (value.aplicavelAoCargo) {
					if (!value.peso) {
						return true;
					} else {
						return false;
					}
				}
			});

			//TODO: Ver as validações de INATIVO
			if (invalido) {
				$scope.$emit('notify-error', '', 'Pelo menos um deve ser selecionado com peso!');
				return;
			}

			if (!$scope.cargoCompetencia.id) {
				CargoCompetencia.save($scope.cargoCompetencia)
					.$promise.then((cargoCompetencia) => {
						//Seta o "novo" cargoCompetencia (com o id) nos detalhamentos
						$scope.listDetalhamentosCargoComp.forEach(detCC => detCC.cargoCompetencia = cargoCompetencia);
						updateDetalhamentoCC();
					});
			} else {
				CargoCompetencia.update({ id: $scope.cargoCompetencia.id }, $scope.cargoCompetencia)
					.$promise.then(() => {
						updateDetalhamentoCC();
					});
			}
		};

		function load() {
			$scope.cargoCompetencia = {};
			$scope.listaCompetenciaSelecionavel = [];

			if (objCargoCompetencia) {
				$scope.cargoCompetencia = objCargoCompetencia;
				loadCompetenciaList('edit');
			} else {
				loadCompetenciaList('new');
			}
		}

		function loadCompetenciaList(mode) {
			$rootScope.loadingProgress = true;
			Competencia.buscarCompetenciaPorTipoCompetencia({ id: $stateParams.tipoId }).$promise
				.then(cargoCompetenciaListData => {
					//Carrega a lista de competencias (incluindo a que foi selecionada), 
					//removendo da lista as que já estão associadas
					$scope.listaCompetenciaSelecionavel = cargoCompetenciaListData
						.filter(comp => !listCargoCompetencias
							.find(cargoComp => cargoComp.competencia.id === comp.id)
						);

					if (mode === 'edit') {
						$scope.listaCompetenciaSelecionavel.push(objCargoCompetencia.competencia);
						obterListaDetalhamentosCC();
					}

					if (mode === 'new') {
						createCargoCompetencia();
					}
				})
				.then(() => {
					$rootScope.loadingProgress = false;
				})
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}

		/* Obtém caso não troque a competencia na combobox */
		function obterListaDetalhamentosCC() {
			$scope.listDetalhamentosCargoComp = [];
			DetalhamentoCargoCompetencia
				.detalhamentosPorCargoCompetencia({
					competenciaId: $scope.cargoCompetencia.competencia.id,
					cargoCompetenciaId: $scope.cargoCompetencia.id
				}).$promise
				.then(dataCC => {
					dataCC.forEach(detalhe => {
						$scope.listDetalhamentosCargoComp.push(detalhe);
					});
				}).catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}

		function createCargoCompetencia() {
			$scope.cargoCompetencia = {
				cargo: cargo,
				ordem: tamanhoLista + 1,
				peso: 1,
				situacao: Situacao.ATIVA
			};
		}

		/* Obtém caso a competencia seja alterada (onChangeCompetencia)*/
		function obterListaDetalhamentosPorCompetencia() {
			$scope.listDetalhamentosCargoComp = [];
			DetalhamentoCargoCompetencia
				.detalhamentosPorCompetencia({
					competenciaId: $scope.cargoCompetencia.competencia.id,
					cargoCompetenciaId: $scope.cargoCompetencia.id
				}).$promise
				.then(dataCC => {
					dataCC.forEach(detalhe => {
						$scope.listDetalhamentosCargoComp.push(detalhe);
					});
				}).catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}

		function updateDetalhamentoCC() {
			Promise.resolve()
				.then(() => $scope.listDetalhamentosCargoComp.forEach(detCC => {
					if(!detCC.id) {
						DetalhamentoCargoCompetencia.save(detCC).$promise;
					} else {
						DetalhamentoCargoCompetencia.update({id: detCC.id}, detCC).$promise;
					}
				}))
				.then(() => {
					$scope.$emit('notify-success', '', 'Competência associada atualizada com sucesso!');
					$scope.cancel();
				});
		}
	}
}