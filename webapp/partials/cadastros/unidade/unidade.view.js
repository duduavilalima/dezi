import angular from 'angular';
import { isAtivo } from '../../model/Situacao';

/* @ngInject */
export default function unidadeViewCtrl(
	$state,
	$scope,
	Unidade,
	roles,
	$stateParams,
	DialogDetalhes,
	DialogSubunidade,
	DialogSituacao,
	navigator
) {
	const $ctrl = this;

	$ctrl.openDialogDetalhes = DialogDetalhes.show;
	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.object = undefined;
	$ctrl.expandedNodes = [];
	$ctrl.listFilhos = [];
	$ctrl.mdswitch = {};
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	const ExpandedNodes = lista => {
		angular.forEach(lista, value => {
			if (angular.isDefined(value.children)) {
				$ctrl.expandedNodes.push(value);
			}
			
			ExpandedNodes(value.children);
		});
		return;
	};

	const carregarArvoreUnidades = () => {
		Unidade.obterArvoreDeUnidades()
			.$promise.then(data => {
				$ctrl.dataForTheTree = data;
				ExpandedNodes($ctrl.dataForTheTree);
			}).catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	const carregarFilhos = () => {
		Unidade.obterFilhosDoTipoDeCompetenciaRaiz({ id: $state.params.objectId }).$promise
			.then(data => {
				$ctrl.listFilhos = data;
			}).catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	const loadObject = () => {
		Unidade.get({ id: $state.params.objectId }).$promise
			.then(data => {
				$ctrl.object = data;
				$ctrl.mdswitch.situacao = $ctrl.object.situacao === 'ATIVA';
			})
			.then(carregarArvoreUnidades)
			.then(carregarFilhos)
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};
	
	$ctrl.displayNew = () => navigator.goNew();

	$ctrl.displayView = item => navigator.goView(item.id);

	$ctrl.displayEdit = () => navigator.goEdit($stateParams.objectId);

	$ctrl.onChangeSituacao = (item, type) => {
		if(type === 'unidade') {
			DialogSituacao.show(item, {
				Resource: Unidade,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'UNIDADES.UNIDADE' }
			})
				.then(({ object, canceled }) => {
					$ctrl.mdswitch.situacao = isAtivo(object);
					refresh(!canceled);
				});
		} else if (type === 'subunidade') {
			DialogSituacao.show(item, {
				Resource: Unidade,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'UNIDADES.SUBUNIDADE' }
			})
				.then(({ canceled }) => {
					refresh(!canceled);
				});
		}
	};

	const refresh = (forceReload = false) => {
		if (forceReload) {
			loadObject();
		} else {
			$scope.$apply();
		}
	};

	$ctrl.cancelAssociation = fn_Hide => {fn_Hide();};

	$ctrl.displayViewAssociation = (ev, mode, item) => {
		$state.go('unidade_view', { objectId: item.id });
	};

	$ctrl.openDialogSubunidade = obj => {
		DialogSubunidade.show(obj)
			.then(subUnidade => {
				subUnidade.$save();
			})
			.then(() => {
				$scope.$emit('notify-success','','Subunidade salva com sucesso!');
				carregarFilhos();
				carregarArvoreUnidades();
			}).catch(() => {
			});
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
			labelSelected: 'a8'
		}
	};

	loadObject();
}
