import angular from 'angular';

/* @ngInject */
export default function adavaliacaoCtrl($scope) {
	const $ctrl = this;

	$ctrl.current = 0;

	$ctrl.save = (competencia, final, event) => {
		competencia.acaoPDI = $ctrl.pdi;
		competencia.comentarioFinalAvaliado = $ctrl.comentarioFinalAvaliado;
		competencia.comentarioFinalAvaliador = $ctrl.comentarioFinalAvaliador;

		$ctrl.onSave({
			competencia: competencia,
			final: final,
			event: event
		});
	};

	$ctrl.salvarFinalizar = (event) => {
		/* Pega o ultimo elemento da lista, seta o PDI (av2)
		   e os comentarios finais (av3), seta no último elemento a ser
		   salvo. 
		   Por conta disso, embora não seja setado nos outros elementos,
		   irá salvar no participante o que foi digitado nos campos
		*/
		const competencia = $ctrl.competencias[$ctrl.competencias.length - 1];
		competencia.acaoPDI = $ctrl.pdi;
		competencia.comentarioFinalAvaliado = $ctrl.comentarioFinalAvaliado;
		competencia.comentarioFinalAvaliador = $ctrl.comentarioFinalAvaliador;

		$ctrl.onSave({
			competencia: competencia,
			final: true,
			event: event
		});
	};

	$ctrl.computeProgress = () => {
		$ctrl.progresso = {
			total: 0,
			av1: 0,
			av2: 0,
			av3: 0
		};
		angular.forEach($ctrl.competencias, function(competencia) {
			$ctrl.progresso.total += competencia.detalhamentos.length;
			angular.forEach(competencia.progresso, function(value, stage) {
				if (value) {
					$ctrl.progresso[stage] += value;
				}
			}, $ctrl.progresso);
		}, $ctrl);

		$ctrl.progresso.av1p = Math.round($ctrl.progresso.av1 * 100 / $ctrl.progresso.total);
		$ctrl.progresso.av2p = Math.round($ctrl.progresso.av2 * 100 / $ctrl.progresso.total);
		$ctrl.progresso.av3p = Math.round($ctrl.progresso.av3 * 100 / $ctrl.progresso.total);
	};

	$ctrl.goNext = () => {
		$ctrl.current += 1;
		const len = $ctrl.competencias ? $ctrl.competencias.length : 0;
		if ($ctrl.current < len) {
			$ctrl.compList.collapseAll();
			$ctrl.compList.expand($ctrl.current);
		}
	};

	$ctrl.expandCallback = (index) => {
		$ctrl.current = index;
	};

	$scope.$on('acc:onReady', () => {
		// Abre o primeiro
		$ctrl.current = -1;
		$ctrl.goNext();
	});
}