/* @ngInject */
export default function pdiListCtrl(
	$state,
	funcionarios,
	userRoles,
	navigator
) {
	const $ctrl = this;

	$ctrl.funcionarios = funcionarios;
	$ctrl.query = {
		order: ['situacao', 'situacaoFuncional', 'nome'],
		limit: 10,
		page: 1
	};

	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.acaoList = id => navigator.goAcoes(id);
}
