import ROUTES from 'utils/routes';
import angular from 'angular';
import { toId } from '../../../utils/entity.utils';
import { isAtivo } from '../../model/Situacao';

const BASE_STATE = 'main.cadastro.departamentos';

/* @ngInject */
export default function departamentosViewCtrl(
	$rootScope,
	$scope,
	$state,
	Departamento,
	DepartamentoGestor,
	DialogDetalhes,
	$translate,
	userRoles,
	DialogSituacao,
	navigator
) {
	const $ctrl = this;
	$ctrl.listResult = null;
	$ctrl.object = null;
	$ctrl.mdswitch = {};
	$ctrl.listaFuncionarios = null;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.expandedNodes = [];
	$ctrl.admin = userRoles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.toolBarClassDialog = 'red-bg'; //'md-accent-bg';
	$ctrl.toolBarClassDialogATIVACAO = 'activate-bg';
	$ctrl.toolBarIconClassDialog = '';

	$ctrl.mdActionClassDialog = '';
	$ctrl.displayBtnNovo = false;

	$ctrl.buttonConfirmClassDialog = 'md-raised md-default';
	$ctrl.buttonCancelClassDialog = 'md-raised md-default';

	let usuarioLogado = undefined;

	$ctrl.query = {
		order: 'idDepartamentoCliente',
		limit: 10,
		page: 1
	};

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

	$ctrl.displayView = (item) => {
		const object = (item || $ctrl.object);
		const id = toId(object);
		navigator.goView(id);
	};

	$ctrl.displayViewFuncionario = (item) => {
		$state.go('main.cadastro.funcionarios.view', { objectId: item.id });
	};

	$ctrl.displayViewCargo = (item) => {
		$state.go('main.cadastro.cargos.view', { objectId: item.id });
	};

	$ctrl.displayViewCompetencia = (item) => {
		//FIXME: main.cadastro.competencias.view
		$state.go('main.competencias.view', { objectId: item.id });
	};

	$rootScope.$watch('user', user => {
		if(angular.isUndefined(usuarioLogado)){
			if(angular.isDefined(user)){
				usuarioLogado = user;
				if(user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH'){
					$ctrl.displayBtnNovo = true;
				}
			}
		}
	});

	function refresh(forceReload = false) {
		if (forceReload) {
			loadObject();
		} else {
			$scope.$apply();
		}
	}

	$ctrl.onChangeSituacao = (item) => {
		DialogSituacao.show(item, {
			Resource: Departamento,
			situacaoKey: ['status', 'situacao'],
			messages: { entity:'DEPARTAMENTO.DEPARTAMENTO' }
		})
			.then(({ object, canceled }) => {
				$ctrl.mdswitch.situacao = isAtivo(object);
				refresh(!canceled);
			});
	};

	const carregarArvoreHierarquia = () => {
		Departamento.listarArvoreDeHierarquiaDeDepartamentos()
			.$promise.then(data => {
				$ctrl.expandedNodes.splice(0);
				$ctrl.dataForTheTree.splice(0);

				data.forEach(value => $ctrl.dataForTheTree.push(value));

				const expandNodes = lista => lista.forEach(value => {
					if (value.children) {
						$ctrl.expandedNodes.push(value);
					}

					expandNodes(value.children);
				});

				expandNodes($ctrl.dataForTheTree);
			}).catch(response => {
				$rootScope.loadingProgress = false;
				$scope.$emit('notify-critical', response);
			});
	};

	const loadObject = () => {
		$ctrl.dataForTheTree =  [];
		$ctrl.objectId = $state.params.objectId;

		Departamento.get({id: $ctrl.objectId}).$promise
			.then(data => {
				$ctrl.object = data;
				$ctrl.mdswitch.situacao = $ctrl.object.situacao === 'ATIVA';
				obterFuncionariosDoDepartamento(data);
				obterCargoCompetencia(data);
				carregarArvoreHierarquia();
			}).catch(response => {
				if (usuarioLogado.tipoPerfil != 'ADMINISTRADOR' || usuarioLogado.tipoPerfil != 'GESTOR_RH') {
					if (usuarioLogado.departamento.id != $ctrl.objectId) {
						$state.go(ROUTES.MAIN.CADASTRO.DEPARTAMENTOS.VIEW.name, {objectId: usuarioLogado.departamento.id});
					} else {
						$rootScope.loadingProgress = false;
						$scope.$emit('notify-critical', response);
					}
				} else {
					$rootScope.loadingProgress = false;
					$scope.$emit('notify-critical', response);
				}
			});
	};

	const obterFuncionariosDoDepartamento = departamento => {
		Departamento.funcionariosAtivosDoDepartamento({id: departamento.id}).$promise.then(data => {
			$ctrl.listaFuncionarios = data;
		}).catch(response => {
			$rootScope.loadingProgress = false;
			$scope.$emit('notify-critical', response);
		});
	};

	const obterCargoCompetencia = departamento => {
		Departamento.cargocompetencia({id: departamento.id}).$promise.then(data => {
			$ctrl.listaCargoCompetencia = data;
		}).catch(response => {
			$rootScope.loadingProgress = false;
			$scope.$emit('notify-critical', response);
		});
	};

	$ctrl.relacionarDepartamentosGestor = () => {
		DepartamentoGestor.relacionarDepartamentosGestor();
	};

	$ctrl.back = () => {
		$state.go(BASE_STATE + '.list');
	};

	$ctrl.displayNew = () => {
		$state.go(BASE_STATE + '.new');
	};

	$ctrl.displayEdit = item => {
		$state.go(BASE_STATE + '.edit', { objectId: item.id });
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	loadObject();
}
