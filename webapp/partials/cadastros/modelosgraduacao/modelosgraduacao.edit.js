/* @ngInject */
export default function modeloGraduacaoEditCtrl(
	$scope,
	modeloGraduacao,
	onSave,
	onCancel,
	messages,
	navigator
) {
	const $ctrl = this;

	$ctrl.object = modeloGraduacao;
	$ctrl.booleanList = $scope.booleanList;

	$ctrl.handleChange = ({ name, value }) => $ctrl.object[name] = value;

	const save = object => {
		return onSave(object)
			.then(object => {
				$scope.$emit('notify-success', '', messages.success);
				navigator.goView(object.id);
			});
	};

	$ctrl.onSave = () => save($ctrl.object);
	$ctrl.onCancel = () => $ctrl.cancel();
	$ctrl.cancel = () => onCancel($ctrl.object);
}
