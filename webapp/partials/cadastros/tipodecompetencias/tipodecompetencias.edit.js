import { isAtivo, isInativo } from 'model/Situacao';
import { toId } from 'utils/entity.utils';
import Situacao from '../../model/Situacao';

/* @ngInject */
export default function tipoCompetenciaEditCtrl(
	$scope,
	$state,
	$stateParams,
	$notify,
	roles,
	navigator,
	DialogDetalhes,
	DialogTipoCompetencia,
	DialogSituacao,
	TipoCompetencia,
	tipoCompetencia
) {
	const $ctrl = this;
	const { data: currentData } = $state.current;
	const { editRoles } = currentData;
	const {
		success: notifySuccess,
		restError: notifyRestError
	} = $notify.atScope($scope).tr;

	$ctrl.screenMode = currentData.mode;
	$ctrl.modulo = currentData.modulo;
	$ctrl.titulo = currentData.titulo;
	$ctrl.subtitulo = currentData.subtitulo;

	$ctrl.displayView = displayView;
	$ctrl.displayEdit = displayEdit;
	$ctrl.isAtivo = isAtivo;
	$ctrl.isInativo = isInativo;

	$ctrl.canEdit = roles.some(role => editRoles.some(authority => role.authority === authority));
	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

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

	$ctrl.viewMode = $ctrl.screenMode === 'VIEW';
	$ctrl.newMode = $ctrl.screenMode === 'NEW';
	$ctrl.editMode = $ctrl.screenMode === 'EDIT';

	$ctrl.onChangeSituacao = (targetEvent) => {
		const entity = 'TIPODECOMPETENCIAS.TIPODECOMPETENCIA';
		DialogSituacao.show($ctrl.object, {
			targetEvent,
			Resource: TipoCompetencia,
			messages: { entity },
		})
			.then(({ object, canceled }) => {
				$ctrl.mdswitch.situacao = isAtivo(object);
				refresh(!canceled);
			})
			.catch(notifyRestError);
	};

	$ctrl.back = () => {
		if ($ctrl.editMode) {
			return displayView($ctrl.object.id);
		}

		return displayList();
	};

	$ctrl.save = () => {
		return saveOrUpdate($ctrl.object);
	};

	$ctrl.onChangeSituacao = (item, type) => {
		if(type === 'tipoCompetencia') {
			DialogSituacao.show(item, {
				Resource: TipoCompetencia,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'TIPODECOMPETENCIAS.TIPODECOMPETENCIA' }
			})
				.then(({ object, canceled }) => {
					//FIXME: entender o pq de estar vindo undefined
					!object.situacao ? object.situacao = Situacao.INATIVA : object.situacao;

					$ctrl.mdswitch.situacao = isAtivo(object);
					refresh(!canceled);
				});
		} else if (type === 'subtipoCompetencia') {
			DialogSituacao.show(item, {
				Resource: TipoCompetencia,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'TIPODECOMPETENCIAS.NOMESUBTIPO' }
			})
				.then(({ canceled }) => {
					refresh(!canceled);
				});
		}
	};

	$ctrl.showDetalhes = (item, targetEvent) => {
		DialogDetalhes.show(item, { targetEvent });
	};

	$ctrl.showEditSubtipo = (subtipo, targetEvent) => {
		DialogTipoCompetencia.edit(subtipo, { targetEvent })
			.then(reload)
			.catch(notifyRestError);
	};

	$ctrl.showNewSubtipo = (targetEvent) => {
		const pai = $ctrl.object;
		DialogTipoCompetencia.create({ pai, targetEvent })
			.then(reload)
			.catch(notifyRestError);
	};

	function findArvore() {
		return TipoCompetencia.obterArvore().$promise
			.then(data => {
				// TIP As arrays devem ser sempre as mesmas, em termos de referência,
				// ao longo de todo o tempo de vida do componente `treecontrol`.
				// Ou seja, o objeto array nunca deve mudar para um inteiramente novo.
				// Do contrário, o `treecontrol` não re-renderiza alterações na lista.
				// Por este motivo, splice é usado para esvaziar as listas e repopulá-las
				// com os novos dados pesquisados.
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
			}, notifyRestError);
	}

	function listObterFilhos() {
		const id = $ctrl.object.id;
		$ctrl.listFilhos = [];

		if (id) {
			TipoCompetencia.obterFilhos({ id }).$promise
				.then(data => {
					$ctrl.listFilhos = data;
				});
		}
	}

	function obterAncestrais() {
		const id = $ctrl.object.id;
		$ctrl.listAncestrais = [];

		if ($ctrl.object.id) {
			TipoCompetencia.obterAncestrais({ id }).$promise
				.then(data => {
					$ctrl.listAncestrais = data;

					if (
						$ctrl.listAncestrais &&
						$ctrl.listAncestrais.length > 1
					) {
						$ctrl.bloquearSubtipo = true;
					}
				});
		}
	}

	function initialize() {
		$ctrl.object = tipoCompetencia;
		$ctrl.dataForTheTree = [];
		$ctrl.expandedNodes = [];
		$ctrl.bloquearSubtipo = false;
		$ctrl.mdswitch = { situacao: isAtivo($ctrl.object) };

		if ($stateParams.listParent) {
			//Por padrão, separa a lista por vírgulas, se a lista for definida, pega cada 'id' dentro da lista (pilha) e cria um objeto.
			$ctrl.listParent = $stateParams.listParent.split(',');
			if (!$ctrl.listParent[0]) {
				//Caso o primeiro elemento for vazio (ao entrar no objeto selecionado, a partir da lista de Tipo de Competência) retira-o
				$ctrl.listParent.splice(0, 1);
			}
		} else {
			//Caso não for definida, cria-se a lista (pilha)
			$ctrl.listParent = [];
		}

		reload();
	}

	function reload() {
		listObterFilhos();
		obterAncestrais();
		findArvore();
	}

	function refresh(forceReload = false) {
		if (forceReload) {
			reload();
		} else {
			$scope.$apply();
		}
	}

	function displayList() {
		navigator.goList();
	}

	function displayView(item) {
		const object = (item || $ctrl.object);
		const id = toId(object);
		navigator.goView(id);
	}

	function displayEdit(item) {
		const object = (item || $ctrl.object);
		const id = toId(object);
		navigator.goEdit(id);
	}

	function saveOrUpdate(tipoCompetencia) {
		const { id } = tipoCompetencia;

		if (!id) {
			return TipoCompetencia.save(null, tipoCompetencia).$promise
				.then(object => {
					notifySuccess('TIPODECOMPETENCIAS.MSGSALVO');
					displayView(object);
				});
		} else {
			return TipoCompetencia.update({ id }, tipoCompetencia).$promise
				.then(() => {
					notifySuccess('TIPODECOMPETENCIAS.MSGATUALIZADO');
					displayView(id);
				});
		}
	}

	initialize();
}
