/* @ngInject */
export default function funcionarioListController(
	colaboradores,
) {
	const $ctrl = this;

	$ctrl.colaboradores = colaboradores;

	$ctrl.conf = {
		limit: 20
	};

	$ctrl.fields = [
		{
			name: 'nome',
			label: 'FUNCIONARIO.NOME',
			width: '20%',
			state: 'view',
		},
		{
			name: 'departamento.nome',
			label: 'FUNCIONARIO.DEPARTAMENTO',
			width: '20%',
		},
		{
			name: 'cargo.nome',
			label: 'FUNCIONARIO.CARGO',
			width: '20%',
		},
		{
			name: 'admissao',
			label: 'FUNCIONARIO.DTADMI',
			width: '10%',
			type: 'localdate',
		},
		{
			name: 'dataCargo',
			label: 'FUNCIONARIO.DTCARGOLIST',
			width: '10%',
			type: 'localdate',
		},
		{
			name: 'situacao',
			label: 'FUNCIONARIO.SITUACAO',
			width: '10%',
			type: 'situacao',
		},
		{
			name: 'situacaoFuncional',
			label: 'FUNCIONARIO.SITUACAO3',
			width: '10%',
			type: 'situacaoFuncional',
		},
	];
}
