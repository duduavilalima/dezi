/* @ngInject */
export default function acompanhamentosViewController(
	$scope,
	$state,
	dadosChart
) {
	const $ctrl = this;

	$ctrl.avaliacaoPreenchimento = (chartName) => {
		if (chartName === 'DASHBOARD.WIDGETAUTOAVALIACAO')
			$state.params.tipoAvaliacao = 0;
		else if (chartName === 'DASHBOARD.WIDGETAVALIACOES')
			$state.params.tipoAvaliacao = 1;
		else if (chartName === 'DASHBOARD.WIDGETCONSENSO')
			$state.params.tipoAvaliacao = 2;

		$state.go('main.acompanhamentos_preenchimentos', $state.params);
	};

	$ctrl.charts = {};

	$ctrl.chartColors = ['#F22509', '#1f76b4', '#2ca02c'];

	$ctrl.chartOptions = {
		type: 'donut',
		tooltips: {
			enabled: true,
			callbacks: {
				label: (tooltipItem, data) => {
					const total = data.datasets[0].data.reduce((total, item) => total + item);
					const value = data.datasets[0].data[tooltipItem.index];

					const perc = Math.round(100.0 * value / total) + '%';
					const label = data.labels[tooltipItem.index];

					return `${label}: ${perc}`;
				},
			},
		},
		legend: {
			display: true,
			position: 'bottom',
			labels: {
				boxWidth: 20,
			},
		}
	};

	const updateCharts = (dadosChart) => {
		const totalAuto = dadosChart.preenchidasAuto + dadosChart.parciaisAuto + dadosChart.naoPreenchidasAuto;
		$ctrl.charts['DASHBOARD.WIDGETAUTOAVALIACAO'] = {
			total: totalAuto,
			preenchidas: dadosChart.preenchidasAuto,
			show: totalAuto > 0,
			labels: ['Não Preenchidas', 'Parciais', 'Preenchidas'],
			data: [dadosChart.naoPreenchidasAuto, dadosChart.parciaisAuto, dadosChart.preenchidasAuto]
		};

		const totalAvaliacoes = dadosChart.preenchidasAvaliacoes + dadosChart.parciaisAvaliacoes + dadosChart.naoPreenchidasAvaliacoes;
		$ctrl.charts['DASHBOARD.WIDGETAVALIACOES'] = {
			total: totalAvaliacoes,
			preenchidas: dadosChart.preenchidasAvaliacoes,
			show: totalAvaliacoes > 0,
			labels: ['Não Preenchidas', 'Parciais', 'Preenchidas'],
			data: [dadosChart.naoPreenchidasAvaliacoes, dadosChart.parciaisAvaliacoes, dadosChart.preenchidasAvaliacoes]
		};

		const totalConsenso = dadosChart.preenchidasConsenso + dadosChart.parciaisConsenso + dadosChart.naoPreenchidasConsenso;
		$ctrl.charts['DASHBOARD.WIDGETCONSENSO'] = {
			total: totalConsenso,
			preenchidas: dadosChart.preenchidasConsenso,
			show: totalConsenso > 0,
			labels: ['Não Preenchidas', 'Parciais', 'Preenchidas'],
			data: [dadosChart.naoPreenchidasConsenso, dadosChart.parciaisConsenso, dadosChart.preenchidasConsenso]
		};

		const hasData = totalAuto > 0 || totalAvaliacoes > 0 || totalConsenso > 0;

		if (!hasData) {
			$scope.$emit(
				'notify-warning',
				'Não há dados para esta pesquisa'
			);
		}
	};

	updateCharts(dadosChart);
}

