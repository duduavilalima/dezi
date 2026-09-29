import { isAtivo } from '../../model/Situacao';

/* @ngInject */
export default function cargoViewController(
	$scope,
	$state,
	navigator,
	Cargo,
	cargo,
	userRoles,
	CargoDialogs,
	$stateParams,
	CargoTipoCompetencia,
	DialogSituacao,
	DialogDetalhes,
) {
	const $ctrl = this;
	$ctrl.entityCargo = Cargo;
	$ctrl.object = cargo;
	$ctrl.mdswitch = {};
	$ctrl.funcionarios = [];
	$ctrl.expandedNodes = [];

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
			labelSelected: 'a8'
		}
	};

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.admin = userRoles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.handleExpand = () => {
		if ($ctrl.funcionarios.$promise) return;
		$ctrl.funcionarios = Cargo.funcionariosAtivos({ id: cargo.id }); //TODO Esse método deveria ser do controller de funcionário
		$ctrl.funcionarios.$promise.then(result => $ctrl.funcionarios = result);
	};

	$ctrl.displayView = item => navigator.goView(item.id);

	$ctrl.displayEdit = () => navigator.goEdit($stateParams.objectId);

	$ctrl.associarCompetenciaTipo = item => navigator.goTipo($ctrl.object.id,item.tipoCompetencia.id);

	$ctrl.displayFuncionario = funcionario => navigator.goFuncionario(funcionario.id);

	$ctrl.associarTipoCompetencia = () => {
		CargoDialogs.associarTipoCompetencia($ctrl.object, $ctrl.tiposCompetencias).then(cargoTipoComp => {
			$ctrl.tiposCompetencias.push(cargoTipoComp);
		});
	};

	$ctrl.associarTipoCompetenciaEdit = item => {
		CargoDialogs.associarTipoCompetencia($ctrl.object, $ctrl.tiposCompetencias, item)
			.then(() => {
				$ctrl.tiposCompetencias = CargoTipoCompetencia.porcargo({ id: $state.params.objectId });
			});
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	$ctrl.onChangeSituacao = (item, type) => {
		if(type === 'cargo') {
			DialogSituacao.show(item, {
				Resource: Cargo,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'CARGO.CARGO' }
			})
				.then(({ object, canceled }) => {
					$ctrl.mdswitch.situacao = isAtivo(object);
					refresh(!canceled);
				});
		} else if (type === 'cargoTipoCompetencia') {
			DialogSituacao.show(item, {
				Resource: CargoTipoCompetencia,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'CARGO.TIPO' }
			})
				.then(({ canceled }) => {
					refresh(!canceled);
				});
		}
	};

	const carregarHierarquiaDeCargos = () => {
		$ctrl.isLoading = true;
		Cargo.listaDeHierarquiaDeCargos({id: 12})
			.$promise.then((data) => {
				$ctrl.dataForTheTree = data;
				ExpandedNodes($ctrl.dataForTheTree);
			}).catch((response) => {
				$scope.$emit(response.status, response.statusText, response.data);
			});
	};

	const ExpandedNodes = (lista) => {
		lista.forEach(value => {
			if(value.children) {
				$ctrl.expandedNodes.push(value);
			}

			ExpandedNodes(value.children);
		});
		$ctrl.isLoading = false;
		return;
	};

	const carregarTipoCompetenciaList = () => {
		CargoTipoCompetencia
			.porcargo({ id: $stateParams.objectId })
			.$promise.then((tiposCompetencias) => $ctrl.tiposCompetencias = tiposCompetencias);
	};

	const loadObject = (forceReload = false) => {
		if(forceReload) {
			Cargo.get({ id: $state.params.objectId }).$promise
				.then(data => {
					$ctrl.object = data;
					$ctrl.mdswitch.situacao = $ctrl.object.situacao === 'ATIVA';
				})
				.then(carregarHierarquiaDeCargos)
				.then(carregarTipoCompetenciaList)
				.catch(response => {
					$scope.$emit('notify-critical', response);
				});
		} else {
			$ctrl.mdswitch.situacao = $ctrl.object.situacao === 'ATIVA';
			carregarHierarquiaDeCargos();
		}
	};

	const refresh = (forceReload = false) => {
		if (forceReload) {
			loadObject(forceReload);
		} else {
			$scope.$apply();
		}
	};

	loadObject(true);
}
