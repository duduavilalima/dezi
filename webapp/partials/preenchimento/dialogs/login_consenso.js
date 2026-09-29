/* @ngInject */
export default function secondaryLoginController(
	$scope,
	$mdDialog,
	participante,
	user,
	AuthService
) {
	const dialog = this;

	let otherUser;
	if (participante.avaliado.id == user.id) {
		dialog.title = 'Login do avaliador';
		otherUser = participante.avaliador;
	} else {
		dialog.title = 'Login do avaliado';
		otherUser = participante.avaliado;
	}

	dialog.empresa = otherUser.empresa;
	dialog.matricula = otherUser.matricula;

	dialog.login = () => {
		const authInfo = {
			empresaId: dialog.empresa.id,
			matricula: dialog.matricula,
			senha: dialog.password
		};

		AuthService.secondaryLogin(authInfo,
			response => {
				$mdDialog.hide(response);
			},
			error => {
				console.error(error);
				$scope.$emit('notify-error', 'Login e/ou senha inválidos.', 'Atenção!');
			});
	};

	dialog.cancel = () => {
		$mdDialog.cancel('canceled');
	};
}