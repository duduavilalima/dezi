import angular from 'angular';

/* @ngInject */
export default function DialogArvoreCtrl(
	$scope, 
	$mdDialog, 
	title, 
	object, 
	fnSave, 
	TipoCompetencia
) {
	const $dialog = this;

	$dialog.title = title;
	$dialog.habilitar = false;
	$dialog.lastSelected = undefined;
	$dialog.dataForTheTree = [];

	$dialog.treeOptions = {
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

	$dialog.expandedNodes = [];

	function expandedNodes(lista) {
		angular.forEach(lista, value => {
			if (angular.isDefined(value.children)) {
				$dialog.expandedNodes.push(value);
			}

			expandedNodes(value.children);
		});
		return;
	}

	$dialog.findArvore = () => {
		TipoCompetencia.obterArvore()
			.$promise.then(function(data) {
				$dialog.dataForTheTree = data;
				expandedNodes($dialog.dataForTheTree);
				selecionarNo($dialog.dataForTheTree, object);

				//esse if só é executado no screenMode edit
				if (angular.isDefined(object)) {
					$dialog.lastSelected = angular.copy(object);
					$dialog.node = object;
					$dialog.habilitarBotaoSave($dialog.node);
				}
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	$dialog.findArvore();

	function selecionarNo(lista, object) {
		angular.forEach(lista, value => {
			if (value.situacao == 'INATIVA') {
				value.disabled = true;
			}

			if (angular.isDefined(object) && angular.isDefined(object.id) && object.id == value.id) {
				value.selected = object.selected;
				return;
			} else {
				selecionarNo(value.children, object);
			}
		});
	}

	$dialog.habilitarBotaoSave = obj =>  {
		if (angular.isDefined(obj) && obj.id != null) {
			if (angular.isDefined(obj.selected)) {
				$dialog.habilitar = obj.selected;
			} else {
				$dialog.habilitar = true;
			}
		}
	};

	$dialog.showSelected = sel => {
		if (angular.isUndefined($dialog.lastSelected) && angular.isDefined(sel.selected)) {
			$dialog.lastSelected = sel;
			$dialog.habilitarBotaoSave(sel);
		} else if (
			angular.isDefined($dialog.lastSelected) &&
			$dialog.lastSelected.id != sel.id &&
			angular.isDefined($dialog.lastSelected.selected) &&
			angular.isDefined(sel.selected)
		) {
			$dialog.lastSelected.selected = false;
			selecionarNo($dialog.dataForTheTree, $dialog.lastSelected);
			$dialog.lastSelected = sel;
			$dialog.habilitarBotaoSave(sel);
		}

		if (angular.isDefined(sel.selected)) {
			$dialog.node = sel;
			$dialog.habilitarBotaoSave(sel);
		}
	};

	$dialog.hide = () => {
		$mdDialog.hide();
	};

	$dialog.cancel = () => {
		$dialog.hide();
	};

	$dialog.confirm = () => {
		fnSave($dialog.node, $dialog.lastSelected, $dialog.hide);
	};
}