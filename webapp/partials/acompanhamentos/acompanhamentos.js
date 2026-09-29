/* @ngInject */
export default function acompanhamentosController(
	$state,
	Campanha,
	listaDepartamentos,
	listaCampanhas
) {
	const $ctrl = this;

	$ctrl.listaDepartamentos = listaDepartamentos.filter(dep => dep.situacao == 'ATIVA');
	$ctrl.listaCampanhas = listaCampanhas;
	$ctrl.selectedFilter = {};

	$ctrl.changeCampanha = () => {
		$ctrl.selectedFilter.avaliacao = undefined;

		if ($ctrl.selectedFilter.campanha) {
			$ctrl.listaAvaliacoes = Campanha.avaliacoesLiberadas({ id: $ctrl.selectedFilter.campanha.id });
			return $ctrl.listaAvaliacoes.$promise;
		}

		return Promise.reject();
	};

	$ctrl.limpar = (form) => {
		$ctrl.selectedFilter = undefined;

		form.$setUntouched();
		form.$setPristine();

		$state.go('main.acompanhamentos');
	};

	$ctrl.filtrar = () => {
		const params = {
			campanhaId: $ctrl.selectedFilter.campanha.id,
			avaliacaoId: $ctrl.selectedFilter.avaliacao.id,
			departamentoId: $ctrl.selectedFilter.departamento && $ctrl.selectedFilter.departamento.id
		};

		$state.go('main.acompanhamentos.view', params);
	};

	// Dados do estado filho
	if ($state.params.campanhaId) {
		$ctrl.selectedFilter.campanha = { id: $state.params.campanhaId };
		$ctrl.changeCampanha().then(() => {
			$ctrl.selectedFilter.avaliacao = { id: $state.params.avaliacaoId };
		});
	}
}
