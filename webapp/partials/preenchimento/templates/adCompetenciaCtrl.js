import angular from 'angular';

/* @ngInject */
export default function adCompetenciaCtrl($scope) {
	const $ctrl = this;

	$ctrl.change = (detalhamento, prop, value) => {
		if (angular.isUndefined(detalhamento.preenchimentos) || detalhamento.preenchimentos.length == 0) {
			detalhamento.preenchimentos.push({});
		}

		detalhamento.preenchimentos[0][prop] = value;
		$ctrl.computeProgress(prop);
	};

	$ctrl.save = (next, final) => {
		let ok = true;
		angular.forEach($ctrl.competencia.detalhamentos, detalhamento => {
			// Validar cometários obrigatórios.
			detalhamento.hints.forEach(graduacaoHint => {
				let graduacaoId, observacao;

				if ($ctrl.competencia.progresso.av1 > 0) {
					graduacaoId = detalhamento.preenchimentos[0].graduacaoAv1Id;
					observacao = detalhamento.preenchimentos[0].observacaoAv1;
				} else if ($ctrl.competencia.progresso.av2 > 0) {
					graduacaoId = detalhamento.preenchimentos[0].graduacaoAv2Id;
					observacao = detalhamento.preenchimentos[0].observacaoAv2;
				}

				if ($ctrl.competencia.progresso.av3 == 0 && graduacaoHint.graduacao.id == graduacaoId) {
					if (graduacaoHint.comentarioObrigatorio) {
						if (angular.isUndefined(observacao) || observacao == '' || observacao == null) {
							ok = false;
							if ($ctrl.competencia.progresso.av1 > 0) {
								$scope.$emit('notify-error', detalhamento.descricao, 'Cometário obrigatório para o detalhamento:');
							} else {
								$scope.$emit('notify-error', 'O comentário é obrigatório para a graduação selecionada.', 'Atenção!');
							}

							return;
						}
					}
				}
			});
			if (!ok) {
				return;
			}
		});

		if (ok) {
			$ctrl.onSave({
				final: final
			});
			if (next)
				$ctrl.avaliacaoCtrl.goNext();
		}
	};

	$ctrl.back = () => {
		$ctrl.avaliacaoCtrl.current = $ctrl.avaliacaoCtrl.current - 1;
	};

	$ctrl.computeProgress = () => {
		$ctrl.competencia.progresso = {
			av1: 0,
			av2: 0,
			av3: 0
		};
		angular.forEach($ctrl.competencia.detalhamentos, detalhamento => {
			//FIXME: Se vale para o primeiro, é valido para todos?
			if (angular.isUndefined($ctrl.consensoLiberado)) {
				$ctrl.consensoLiberado = detalhamento.preenchimentos[0].participante.consensoLiberado;
			}

			if (detalhamento.preenchimentos[0]) {
				if (detalhamento.preenchimentos[0].graduacaoAv1Id)
					$ctrl.competencia.progresso.av1++;
				if (detalhamento.preenchimentos[0].graduacaoAv2Id)
					$ctrl.competencia.progresso.av2++;
				if (detalhamento.preenchimentos[0].graduacaoAv3Id)
					$ctrl.competencia.progresso.av3++;
			}

			//FIXME: Refatorar. Melhorar código.
			// Colocar obrigatóriedade conforme graduação já selecionada anteriormente.
			angular.forEach(detalhamento.hints, function(graduacaoHint) {
				if (angular.isDefined(detalhamento.preenchimentos[0]) && angular.isDefined(detalhamento.preenchimentos[0].graduacaoAv1Id)) {
					if (graduacaoHint.graduacao.id == detalhamento.preenchimentos[0].graduacaoAv1Id) {
						if (graduacaoHint.comentarioObrigatorio) {
							detalhamento.obrigatorio = graduacaoHint.comentarioObrigatorio;
						}
					}
				}

				if (angular.isDefined(detalhamento.preenchimentos[0]) && angular.isDefined(detalhamento.preenchimentos[0].graduacaoAv2Id)) {
					if (graduacaoHint.graduacao.id == detalhamento.preenchimentos[0].graduacaoAv2Id) {
						if (graduacaoHint.comentarioObrigatorio) {
							detalhamento.obrigatorio = graduacaoHint.comentarioObrigatorio;
						}
					}
				}
			});
		}, $ctrl.competencia.progresso);
		$ctrl.avaliacaoCtrl.computeProgress();
	};

	$ctrl.$onInit = function() {
		$ctrl.computeProgress($ctrl.competencia);
	};
}
