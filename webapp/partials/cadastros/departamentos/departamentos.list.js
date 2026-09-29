/* @ngInject */
export default function departamentosListCtrl(
	listDepartamentos,
) {
	const $ctrl = this;

	$ctrl.departamentos = listDepartamentos;

	$ctrl.fields = [
		{
			name: 'nome',
			label: 'DEPARTAMENTO.DEPARTAMENTO',
			state: 'view'
		},
		{
			name: 'idDepartamentoCliente',
			label: 'DEPARTAMENTO.ID',
			width: '40%'
		},
		{
			name: 'situacao',
			label: 'DEPARTAMENTO.SITUACAO',
			width: '10%'
		}
	];
}
