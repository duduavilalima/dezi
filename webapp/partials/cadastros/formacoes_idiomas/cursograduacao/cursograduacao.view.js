import Situacao from '../../../model/Situacao';

export default function cursoGraduacaoViewController(
	$scope,
	$state,
	userRoles,
	cursoGraduacao,
	DialogConfirm,
	navigator
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.cursoGraduacao = cursoGraduacao;
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.canEdit = userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0);
	$ctrl.displayEdit = () => navigator.goEdit($ctrl.cursoGraduacao.id);

	$ctrl.back = () => navigator.goList();

	$ctrl.handleChange = (field, value) => {
		const oldValue =  $ctrl.cursoGraduacao[field];
		$ctrl.cursoGraduacao[field] = Situacao.ATIVA;
		let title = 'DIALOG.TITLE.ACTIVATE';
		let msg = 'DIALOG.MESSAGE.ACTIVATE';
		let notify = 'NOTIFY.ACTIVATE_SUCCESS';

		if(!value) {
			$ctrl.cursoGraduacao[field] = Situacao.INATIVA;
			title = 'DIALOG.TITLE.INACTIVATE';
			msg = 'DIALOG.MESSAGE.INACTIVATE';
			notify = 'NOTIFY.INACTIVATE_SUCCESS';
		}

		DialogConfirm.show(title, msg, { name: 'TIPOS.TIPODECURSO' })
			.then((canceled) => {
				if (canceled) {
					return $ctrl.cursoGraduacao[field] = oldValue;
				}

				$ctrl.cursoGraduacao.$update().then(() => {
					$scope.$emit('notify-success', name + notify);
				})
					.catch((err) => {
						$scope.$emit(err.type, err.data, err.statusText);
						$ctrl.cursoGraduacao[field] = oldValue;
					});
			});
	};
}
