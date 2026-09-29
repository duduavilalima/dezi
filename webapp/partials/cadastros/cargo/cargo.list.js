/* @ngInject */
export default function cargoListCtrl(cargos) {
	const $ctrl = this;

	$ctrl.cargos = cargos;

	$ctrl.conf = {
		limit: 20,
		limitOptions: [ 5, 10, 15, 20, 25, 30, 40, 50 ]
	};

	$ctrl.fields = [
		{
			name: 'nome',
			label: 'CARGO.CARGO',
			state: 'view'
		},
		{
			name: 'idCargoCliente',
			label: 'CARGO.IDRH',
			width: '20%'
		},
		{
			name: 'situacao',
			label: 'CARGO.SITUACAO',
			width: '10%'
		}
	];
}
