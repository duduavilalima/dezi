import ROUTES from 'utils/routes';

const STATE_CAMPANHA_VIEW = ROUTES.MAIN.CAMPANHA.VIEW.name;
const STATE_CAMPANHA_LIST = ROUTES.MAIN.CAMPANHA.LIST.name;
const STATE_EDIT = ROUTES.MAIN.AVALIACAO.EDIT.name;

/* @ngInject */
export default function abaResumoCtrl(
	$state,
	$scope,
	TipoResource,
	DialogDetalhes,
	Avaliacao,
	roles
) {
	const $ctrl = this;
	$ctrl.object = $ctrl.avaliacao = $state.object;
	$scope.$emit('selectedIndexTab', $state.selectedIndexTab);
	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	//FIXME: Não houve tempo para refatorar
	const desabilitarBotaoSalvarLiberar = () => {
		if (!$ctrl.object) return true;

		const temPendencias = $ctrl.object.pendencias && $ctrl.object.pendencias.length > 0;
		const temParticipantes = $ctrl.object.participantes && $ctrl.object.participantes.length > 0;
		const temAvaliados = $ctrl.avaliados && $ctrl.avaliados.length > 0;

		if (!temParticipantes || temPendencias) return true;

		if (!temAvaliados) return false;

		const somentePares = !$ctrl.object.autoavaliacao && !$ctrl.object.gestores &&
			!$ctrl.object.subordinados && $ctrl.object.pares;

		if (!somentePares) return false;

		// Verifica se todos os avaliados tem pares
		const verificaPar = (avaliado, avaliador) =>
			avaliador.perfilAgente == $ctrl.agentePar && avaliador.avaliador.id != avaliado.id;

		const todosTemPares = $ctrl.avaliados.every(avaliado =>
			avaliado.avaliadores.some(avaliador => verificaPar(avaliado, avaliador))
		);

		return !todosTemPares;
	};

	const pesquisarAgentes = () => {
		TipoResource.tiposAgentes().$promise
			.then(data => {
				$ctrl.listaTiposAgentes = data;
				$ctrl.listaTiposAgentes.forEach(item => {
					if (item === 'AUTOAVALIACAO') {
						$ctrl.agenteAutoavaliacao = 'AUTOAVALIACAO';
					} else if (item === 'GESTOR') {
						$ctrl.agenteGestor = 'GESTOR';
					} else if (item === 'SUBORDINADO') {
						$ctrl.agenteSubordinado = 'SUBORDINADO';
					} else if (item === 'PARES') {
						$ctrl.agentePar = 'PARES';
					}
				});
				return data;
			});
	};

	const preencheListaPlanejamentoAgentes = obj => {
		obj.tiposCompetencia.forEach(tc => {
			obj.agentes.forEach(ag => {
				if (tc.nomeCurto === ag.tipoCompetencia.nomeCurto) {
					if (ag.perfilAgente === 'AUTOAVALIACAO') {
						if (ag.peso > 0) {
							tc.autoavaliacao = true;
						}

						tc.percentualAutoavaliacao = ag.peso;
					} else if (ag.perfilAgente === 'GESTOR') {
						if (ag.peso > 0) {
							tc.gestor = true;
						}

						tc.percentualGestor = ag.peso;
					} else if (ag.perfilAgente === 'SUBORDINADO') {
						if (ag.peso > 0) {
							tc.subordinados = true;
						}

						tc.percentualSubordinados = ag.peso;
					} else if (ag.perfilAgente === 'PARES') {
						if (ag.peso > 0) {
							tc.pares = true;
						}

						tc.percentualPares = ag.peso;
					}
				}
			});
		});
	};

	const load = () => {
		$ctrl.listDimensao = $ctrl.object.listaDimensao.map(item => {
			return item.dimensao;
		});

		pesquisarAgentes();
		preencheListaPlanejamentoAgentes($ctrl.object);

		if ($ctrl.object.gestores || $ctrl.object.subordinados || $ctrl.object.pares) {
			$ctrl.object.dataLimite = $ctrl.object.limiteAv2;
		}

		if ($ctrl.object.preencheAv1) {
			$ctrl.object.autoavaliacao = true;
			$ctrl.object.dataLimite = $ctrl.object.limiteAv1;
		}

		if ($ctrl.object.preencheAv3) {
			$ctrl.object.consenso = true;
		}

		desabilitarBotaoSalvarLiberar();
	};

	$ctrl.pesos = (id) => {
		Avaliacao.ajustarpesos({id: id});
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	$ctrl.ajudaResumo = modo => {
		import('../avaliacao.help').then(help => help.showAjudaResumo(modo));
	};

	$ctrl.editar = () => {
		$state.go(STATE_EDIT, {
			campanhaId: $state.params.campanhaId,
			objectId: $ctrl.object.id,
			avaliacao: $ctrl.object
		});
	};

	$ctrl.back = () => {
		$state.go(STATE_CAMPANHA_VIEW, {
			objectId: $state.params.campanhaId
		});
	};

	$ctrl.backToList = () => {
		$state.go(STATE_CAMPANHA_LIST);
	};

	load();
}
