/* @ngInject */
export default function unidadeListCtrl(
	listUnidades,
) {
	const $ctrl = this;

	$ctrl.unidades = listUnidades;

	$ctrl.fields = [
		{
			name: 'nome',
			label: 'UNIDADES.UNIDADE',
			state: 'view',
			width: '35%'
		},
		{
			name: 'descricao',
			label: 'UNIDADES.DESCRICAO',
			width: '55%'
		},
		{
			name: 'situacao',
			label: 'UNIDADES.SITUACAO',
			width: '10%'
		}
	];
}
