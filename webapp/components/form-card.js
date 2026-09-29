import angular from 'angular';

import './form-card.html';

export default angular.module('comps.formCard', [])
	.component('formCard', {
		templateUrl: 'form-card.html',
		controller: formCardController,
		transclude: true,
		bindings: {
			name: '@',
			cardTitle: '@',
			entityName: '@',
			entityResource: '<?',
			object: '<',
			canEdit: '<',
			canSave: '<?',
			entityData: '<',
			onEdit: '&',
			onSave: '&',
			onCancel: '&',
			useDialogSituacao: '<?',
			afterChangeSituacao: '&?',
			showDetalhes: '<?'
		}
	})
	.name;

function formCardController(
	$state,
	$attrs,
	DialogSituacao,
	DialogDetalhes,
	DialogConfirm
) {
	const $ctrl = this;

	$ctrl.canEditSituation = false;

	$ctrl.$onInit = () => {
		if (!$ctrl.name)
			throw new Error('Attribute name is required for form and its children');

		if ($ctrl.canSave === undefined) {
			$ctrl.canSave = !!$attrs.onSave;
		}

		$ctrl.canEditSituation = $ctrl.canEdit && $ctrl.object && $ctrl.object.hasOwnProperty('situacao');

		if ($ctrl.canEditSituation) {
			Object.defineProperty($ctrl, 'active', {
				set: (val) => changeActive(val),
				get: () => $ctrl.object.situacao == 'ATIVA'
			});
		}
	};

	$ctrl.goEdit = () => {
		$state.go($state.current.data.baseState + '.edit', { objectId: $ctrl.object.id });
	};

	$ctrl.detalhes = (targetEvent) => {
		DialogDetalhes.show($ctrl.object, { targetEvent });
	};

	const changeActive = (val) => {
		// FIXME Revisar o uso dos dialogs e decidir qual usar.
		if ($ctrl.useDialogSituacao) {
			DialogSituacao.show($ctrl.object, {
				Resource: $ctrl.entityResource,
				messages: { entity: $ctrl.entityName },
			})
				.then(() => $ctrl.afterChangeSituacao && $ctrl.afterChangeSituacao());
		} else {
			const params = val
				? ['SITUACAO.TITULO', 'SITUACAO.MSG_ATIVAR']
				: ['SITUACAO.TITULO', 'SITUACAO.MSG_DESATIVAR'];

			const interpolationParams = {
				type: $ctrl.entityData.typeName,
				article: $ctrl.entityData.typeArticle,
				name: $ctrl.object[$ctrl.entityData.nameField]
			};

			DialogConfirm.show(...params, interpolationParams)
				.then(() => {
					return val ? $ctrl.object.$activate() : $ctrl.object.$deactivate();
				});
		}
	};
}
