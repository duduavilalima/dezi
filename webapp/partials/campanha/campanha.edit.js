const STATE_VIEW = 'main.campanha.view';

/* @ngInject */
export default function campanhaListCtrl(
	$translate,
	$scope,
	$state,
	roles,
	Campanha
) {
	const $ctrl = this;

	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	(() => { //createObject
		Campanha.get({id: $state.params.objectId})
			.$promise.then(data => {
				$ctrl.object = data;
			});
	})();

	$ctrl.update = () => {
		$ctrl.object.dimensao = false;
		const traduzirTexto = () => 
			$translate(['CAMPANHA.NOTIFICACAO_TEXTO_EDICAO'])
				.then(translations => ({ translation: translations['CAMPANHA.NOTIFICACAO_TEXTO_EDICAO'] }));
		const exibirMensagem = ({ translation }) => {
			$scope.$emit('notify-success', '', $ctrl.titulo + translation);
			$ctrl.back();
		};
		
		$ctrl.object.$update({ id: $ctrl.object.id })
			.then(traduzirTexto)
			.then(exibirMensagem);
	};

	//TODO Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.authPodeIncluir = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.back = () => {
		$state.go(STATE_VIEW, { objectId: $ctrl.object.id });
	};
}
