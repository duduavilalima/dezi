import angular from 'angular';
import ROUTES from 'utils/routes';
import DialogAssociationCtrl from './dialogs/dialogAssociationCtrl';
import './dialogs/dialog.detalhamento.html';
import { isAtivo } from '../../model/Situacao';

/* @ngInject */
export default function CompetenciaViewCtrl(
	$state,
	$scope,
	$mdMedia,
	$mdDialog,
	Competencia,
	DialogDetalhes,
	roles,
	CompetenciaDetalhamento,
	ModeloGraduacao,
	Graduacao,
	ValidationService,
	$q,
	CompetenciaDetalhamentoCalibrahint,
	DialogSituacao
) {
	const $ctrl = this;
	$ctrl.listResult = null;
	$ctrl.resource = Competencia;
	$ctrl.object = null;

	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.listAssociations = null;
	$ctrl.titleAssociation = 'Detalhamento / Comportamento observável';
	const resourceAssociation = CompetenciaDetalhamento;
	const resourceFindAssociation = CompetenciaDetalhamento.detalhamentosPorCompetencia;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.goNew = 'competencia_new';
	$ctrl.goView = 'competencia_view';
	$ctrl.goEdit = 'competencia_edit';
	$ctrl.goSearch = 'competencias';

	$ctrl.listModeloGraduacao = [];
	$ctrl.resourceMG = ModeloGraduacao;
	$ctrl.resourceGraduacao = Graduacao;
	$ctrl.expandedNodes = [];
	$ctrl.dataForTheTree = [];
	$ctrl.mdswitch = {};

	$ctrl.listTipoCompetencia = [];
	$ctrl.listaAvaliacao = null;

	$ctrl.treeOptions = {
		nodeChildren: 'children',
		dirSelectable: true,
		injectClasses: {
			ul: 'a1',
			li: 'a2',
			liSelected: 'a7',
			iExpanded: 'a3',
			iCollapsed: 'a4',
			iLeaf: 'a5',
			label: 'a6',
			labelSelected: 'a8',
		},
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	$ctrl.expandedNodes = [];

	const expandedNodes = lista => {
		$ctrl.expandedNodes = lista.filter(value => {
			if (angular.isDefined(value.children)) {
				return value;
			}

			expandedNodes(value.children);
		});

		return;
	};

	const carregarArvore = () => {
		Competencia.obterArvoreTipoCompetenciaPorCompetencia({ id: $state.params.objectId }).$promise
			.then(data => {
				$ctrl.dataForTheTree = data;
				expandedNodes($ctrl.dataForTheTree);
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.createFilter = object => {
		let filter = null;

		if (ValidationService.verificarPreenchimento(object) && ValidationService.verificarPreenchimento(object.id)) {
			filter = {};
			filter = object;
		}

		return filter;
	};

	$ctrl.onChangeSituacao = (item, type) => {
		if(type === 'competencia') {
			DialogSituacao.show(item, {
				Resource: Competencia,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'COMPETENCIA.COMPETENCIA' }
			})
				.then(({ object, canceled }) => {
					$ctrl.mdswitch.situacao = isAtivo(object);
					refresh(!canceled);
				});
		} else if (type === 'detalhamento') {
			DialogSituacao.show(item, {
				Resource: resourceAssociation,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'COMPETENCIA.DETALHAMENTO' }
			})
				.then(({ canceled }) => {
					refresh(!canceled);
				});
		}
	};

	$ctrl.displayEditAssociation = (ev, index, item) => {
		CompetenciaDetalhamentoCalibrahint.hintspordetalhamentos({id: undefined}, item)
			.$promise.then(dataHints => {
				const useFullScreen = $mdMedia('xs');
				$mdDialog.show({
					controller: DialogAssociationCtrl,
					controllerAs: '$dialog',
					locals: {
						title: $ctrl.titleAssociation,
						object: item,
						msg: $ctrl.titleAssociation + ' atualizada com sucesso!',
						acao: 'Edição',
						listHints: dataHints,
						fnSave: saveAssociation,
						fnCancel: cancelAssociation,
						resourceAssociation: resourceAssociation,
					},
					templateUrl: 'dialog.detalhamento.html',
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
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.displayNewAssociation = (ev) => {
		const filtro = {};
		filtro.situacao = 'ATIVA';

		CompetenciaDetalhamento.get()
			.$promise.then(data => {
				const competenciaDetalhamento = data;
				competenciaDetalhamento.competencia = $scope.object;
				competenciaDetalhamento.competenciaDetalhamentosCalibrahint = [];

				if (ValidationService.verificarPreenchimento($scope.listAssociations)) {
					competenciaDetalhamento.ordem = $scope.listAssociations.length + 1;
				} else {
					competenciaDetalhamento.ordem = 1;
				}

				const useFullScreen = $mdMedia('xs');
				$mdDialog.show({
					controller: DialogAssociationCtrl,
					controllerAs: '$dialog',
					locals: {
						title: $ctrl.titleAssociation,
						object: competenciaDetalhamento,
						msg: $ctrl.titleAssociation + ' salvo com sucesso!',
						acao: 'Novo',
						listHints: [],
						fnSave: saveAssociation,
						fnCancel: cancelAssociation,
						resourceAssociation: resourceAssociation,
					},
					templateUrl: 'dialog.detalhamento.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: true,
					fullscreen: useFullScreen,
				});
				$scope.$watch(
					() => {
						return $mdMedia('xs');
					},
					wantsFullScreen => {
						$scope.customFullscreen = wantsFullScreen === true;
					}
				);
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	const buscarListaAvaliacao = object => {
		Competencia.avaliacaoPorCompetencia({id: undefined}, object).$promise
			.then(data => {
				$ctrl.listaAvaliacao = data;
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.moveUp = (item, index) => {
		if (index > 0 && index < $ctrl.listAssociations.length) {
			const position = index;
			const listChange = [];
			const promises = [];
			let above = {};

			above = $ctrl.listAssociations[position - 1];

			index++;
			above.ordem = index;
			item.ordem = index - 1;

			above.competencia = $ctrl.object;
			item.competencia = $ctrl.object;

			listChange.push(item);
			listChange.push(above);

			angular.forEach(listChange, value => {
				promises.push(resourceAssociation.update({ id: value.id }, value).$promise);
			});

			$q.all(promises)
				.then(() => {
					loadObject();
				})
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}
	};

	$ctrl.moveDown = (item, index) => {
		if (index >= 0 && index < $ctrl.listAssociations.length - 1) {
			const position = index;
			const listChange = [];
			const promises = [];
			let below = {};

			below = $ctrl.listAssociations[position + 1];

			index++;
			below.ordem = index;
			item.ordem = index + 1;

			below.competencia = $ctrl.object;
			item.competencia = $ctrl.object;

			listChange.push(item);
			listChange.push(below);

			angular.forEach(listChange, value => {
				promises.push(resourceAssociation.update({ id: value.id }, value).$promise);
			});

			$q.all(promises)
				.then(() => {
					loadObject();
				})
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}
	};

	const cancelAssociation = fn_Hide => fn_Hide();

	const saveAssociation = (object, fn_Hide, msg) => {
		const {competenciaDetalhamentosCalibrahint, ...compDet} = object;

		compDet.competencia = $ctrl.object;
		if (!compDet.id && $ctrl.listAssociations) {
			compDet.ordem = $ctrl.listAssociations.length + 1;
		}

		const success = () => {
			$scope.$emit('notify-success','',msg);
			fn_Hide();
		};

		// const saveOrUpdateDetalhamentoCalibraHint = (detalhamentoCalibraList, persistedCompDet) => {
		// 	detalhamentoCalibraList.forEach(det => {
		// 		det.competenciaDetalhamento = persistedCompDet;
		// 		if(!det.id) {
		// 			CompetenciaDetalhamentoCalibrahint.save(det).$promise;
		// 		} else {
		// 			CompetenciaDetalhamentoCalibrahint.update(det).$promise;
		// 		}
		// 	});
		// };

		//FIXME: Melhorar essa questao do id da competencia detalhamento
		if (compDet.id == null) {
			compDet.competenciaDetalhamentosCalibrahint = competenciaDetalhamentosCalibrahint;
			resourceAssociation
				.save(compDet).$promise
				// .then((persistedCompDet) => saveOrUpdateDetalhamentoCalibraHint(competenciaDetalhamentosCalibrahint, persistedCompDet))
				.then(success)
				.then(loadObject)
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}else if (compDet.id != null) {
			compDet.competenciaDetalhamentosCalibrahint = competenciaDetalhamentosCalibrahint;
			resourceAssociation
				.update({ id: compDet.id }, compDet).$promise
				// .then((persistedCompDet) => saveOrUpdateDetalhamentoCalibraHint(competenciaDetalhamentosCalibrahint, persistedCompDet))
				.then(success)
				.then(loadObject)
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}
	};

	const loadAssociations = object => {
		const filterAssociation = $ctrl.createFilter(object);

		if (filterAssociation != null) {
			resourceFindAssociation(filterAssociation).$promise
				.then(data => {
					$ctrl.listAssociations = data;
					$ctrl.listAssociations.sort((a, b) => {
						a = a.ordem;
						b = b.ordem;
						return a - b;
					});
				})
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}

		buscarListaAvaliacao(object);
	};

	const loadObject = () => {
		$ctrl.resource.get({ id: $state.params.objectId }).$promise
			.then(data => {
				$ctrl.object = data;
				carregarArvore();
				$ctrl.mdswitch.situacao = $ctrl.object.situacao === 'ATIVA';
				loadAssociations($ctrl.object);
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	const refresh = (forceReload = false) => {
		if (forceReload) {
			loadObject();
		} else {
			$scope.$apply();
		}
	};

	$ctrl.viewNodeCompetencia = (id) => {
		$state.go(ROUTES.MAIN.CADASTRO.COMPETENCIA.VIEW.name, { objectId: id });
	};

	$ctrl.back = () => $state.go(ROUTES.MAIN.CADASTRO.COMPETENCIA.LIST.name);
	$ctrl.displayNew = () => $state.go(ROUTES.MAIN.CADASTRO.COMPETENCIA.NEW.name);
	$ctrl.displayEdit = () => $state.go(ROUTES.MAIN.CADASTRO.COMPETENCIA.EDIT.name,{ objectId: $state.params.objectId });

	loadObject();
}
