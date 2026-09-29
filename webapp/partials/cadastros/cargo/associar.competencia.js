// import angular from 'angular';

/* @ngInject */
export default function associarCompetenciaCtrl(
	$state,
	$scope,
	TipoCompetencia,
	CargoCompetencia,
	Cargo,
	DialogAssociarCompetencia,
	$q,
	DialogSituacao
) {
	const $ctrl = this;
	$ctrl.listCompetencias = [];

	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	const load = () => {
		TipoCompetencia.get({ id: $state.params.tipoId })
			.$promise.then(data => {
				$ctrl.tipoCompetencia = data;
			});
		$ctrl.cargo = Cargo.get({id: $state.params.objectId});
		findCargosCompetencias();
	};

	$ctrl.associarCompetencia = () => {
		DialogAssociarCompetencia.associarCompetencia($ctrl.listCompetencias, $ctrl.cargo, null)
			.then(() => {
				refresh(true);
			}).catch(() => {
				refresh(true);
			});
	};

	$ctrl.associarCompetenciaEdit = objCargoComepetencia => {
		DialogAssociarCompetencia.associarCompetencia($ctrl.listCompetencias, $ctrl.cargo, objCargoComepetencia)
			.then(() => {
				refresh(true);
			}).catch(() => {
				refresh(true);
			});
	};

	$ctrl.onChangeSituacao = item => {
		DialogSituacao.show(item, {
			Resource: CargoCompetencia,
			situacaoKey: ['status', 'situacao'],
			messages: { entity:'CARGO.COMPETENCIA_ASSOCIADA' }
		}).then(({ canceled }) => {
			refresh(!canceled);
		});
	};

	const refresh = (forceReload = false) => {
		if (forceReload) {
			load();
		} else {
			$scope.$apply();
		}
	};

	const findCargosCompetencias = () => {
		CargoCompetencia.cargoCompetenciaPorTipoCompetencia({ id: $state.params.objectId, tipo: $state.params.tipoId })
			.$promise.then(data => {
				$ctrl.listCompetencias = data;
				$ctrl.listCompetencias.sort((a, b) => {
					a = a.ordem;
					b = b.ordem;
					return a - b;
				});
			});
	};

	$ctrl.move = (item, move, index) => {
		const listChange = [];
		const promises = [];
		let competencia = {};
		if (move === 'up') {
			if (index > 0 && index < $ctrl.listCompetencias.length) {
				competencia = $ctrl.listCompetencias[index - 1];
				index++;
				competencia.ordem = index;
				item.ordem = index - 1;
			}
		} else {
			if (index >= 0 && index < $ctrl.listCompetencias.length - 1) {
				competencia = $ctrl.listCompetencias[index + 1];
				index++;
				competencia.ordem = index;
				item.ordem = index + 1;
			}
		}

		listChange.push(item);
		listChange.push(competencia);

		listChange.forEach(value => {
			promises.push(CargoCompetencia.update({
				id: value.id
			}, value).$promise);
		});

		$q.all(promises).then(() => {
			findCargosCompetencias();
		}).catch(response => {
			$scope.$emit('notify-critical', response);
		});
	};

	load();
}
