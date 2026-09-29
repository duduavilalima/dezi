const BASE_STATE = 'main.cadastro.cargos';

/* @ngInject */
export default function cargoEditController (
	$scope,
	$state,
	cargo,
	departamentos,
	userRoles
) {
	const $ctrl = this;

	$ctrl.cargo = cargo;
	$ctrl.departamentos = departamentos;

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.handleChange = (field, value) => $ctrl.cargo[field] = value;

	$ctrl.handleCreate = () => $state.go('main.cargo.new');

	const toView = id => $state.go(BASE_STATE+'.view',{objectId: id});

	$ctrl.save = () => {
		if ($ctrl.cargo.id) {
			$ctrl.cargo.$update().then(data => {
				$scope.$emit('notify-success', 'Cargo atualizado com sucesso!');
				return data;
			})
				.then(data => toView(data.id));
		} else {
			$ctrl.cargo.$save().then(data => {
				$scope.$emit('notify-success', 'Cargo criado com sucesso!');
				return data;
			})
				.then(data => toView(data.id));
		}
	};
	
	$ctrl.cancel = () => $state.go('main.cadastro.cargos.list');
}
