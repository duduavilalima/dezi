import angular from 'angular';

/* @ngInject */
export default function adDetalhamentoCtrl($mdDialog) {
	const $ctrl = this;

	function capitalizeFirstLetter(string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	}

	function DialogController($mdDialog) {
		const $dialog = this;

		$dialog.hidecomentarios = () => {
			$mdDialog.hide();
		};
	}

	$ctrl.selectOption = (stage, detalhamento) => {
		const prop = 'graduacao' + capitalizeFirstLetter(stage) + 'Id';
		$ctrl.onChange({
			prop: prop,
			value: $ctrl.respostas[stage]
		});
		detalhamento.hints.forEach(hint => {
			detalhamento.preenchimentos.forEach(preench => {
				if (stage == 'av2') {
					if (preench.graduacaoAv2Id == hint.idGraduacao) {
						detalhamento.obrigatorio = hint.comentarioObrigatorio;
					}
				} else if (stage == 'av1') {
					if (preench.graduacaoAv1Id == hint.idGraduacao) {
						detalhamento.obrigatorio = hint.comentarioObrigatorio;
					}
				} else if (stage == 'av3') {
					if (preench.graduacaoAv3Id == hint.idGraduacao) {
						detalhamento.obrigatorio = hint.comentarioObrigatorio;
					}
				}
			});
		});
	};

	$ctrl.changeComment = stage => {
		const prop = 'observacao' + capitalizeFirstLetter(stage);
		$ctrl.onChange({
			prop: prop,
			value: $ctrl.comentarios[stage]
		});
	};

	$ctrl.showcomentarios = (ev, comentarios) => {
		//const comentarios = comentarios || {};
		const av1 = comentarios.av1 || '(sem comentários)';
		const av2 = comentarios.av2 || '(sem comentários)';
		$mdDialog.show({
			controller: DialogController,
			controllerAs: 'dialog',
			locals: {
				av1: av1,
				av2: av2
			},
			bindToController: true,
			templateUrl: 'ad-avaliacao-dialog.dlg.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true
		});
	};

	$ctrl.$onInit = () => {
		if ($ctrl.detalhamento)
			$ctrl.detalhamento.descricao = $ctrl.detalhamento.descricao
				.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
				.replace(/\n/g, '<br/>')
				.replace(/\s/g, '&nbsp;');

		if (!$ctrl.detalhamento.preenchimentos[0])
			$ctrl.detalhamento.preenchimentos[0].push({});

		$ctrl.respostas = {
			av1: $ctrl.detalhamento.preenchimentos[0].graduacaoAv1Id,
			av2: $ctrl.detalhamento.preenchimentos[0].graduacaoAv2Id,
			av3: $ctrl.detalhamento.preenchimentos[0].graduacaoAv3Id,
		};
		$ctrl.comentarios = {
			av1: $ctrl.detalhamento.preenchimentos[0].observacaoAv1,
			av2: $ctrl.detalhamento.preenchimentos[0].observacaoAv2,
			av3: $ctrl.detalhamento.preenchimentos[0].observacaoAv3,
		};
	};
}
