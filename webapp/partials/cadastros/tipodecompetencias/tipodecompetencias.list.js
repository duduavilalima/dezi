/* @ngInject */
export default function tipoCompetenciaListCtrl(
	tiposCompetencia,
	navigator
) {
	const $ctrl = this;

	$ctrl.navigator = navigator;
	$ctrl.tiposCompetencia = tiposCompetencia;

	$ctrl.fields = [
		{
			name: 'nome',
			label: 'TIPODECOMPETENCIAS.NOME',
			state: 'view'
		},
		{
			name: 'situacao',
			label: 'TIPODECOMPETENCIAS.SITUACAO',
			width: '10%'
		}
	];
}
