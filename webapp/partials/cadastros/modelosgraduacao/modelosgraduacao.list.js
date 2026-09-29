/* @ngInject */
export default function modelosGraduacaoListCtrl(
	navigator,
	modelosGraduacao
) {
	const $ctrl = this;

	$ctrl.navigator = navigator;
	$ctrl.modelosGraduacao = modelosGraduacao;

	$ctrl.fields = [
		{
			name: 'descricaoModelo',
			label: 'MODELOS.MODELO',
			state: 'view'
		},
		{
			name: 'qtdGraduacoes',
			label: 'MODELOS.QTD',
			width: '30%'
		},
		{
			name: 'situacao',
			label: 'MODELOS.SITUACAO',
			width: '10%'
		}
	];
}
