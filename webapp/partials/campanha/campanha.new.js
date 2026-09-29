const STATE_VIEW = 'main.campanha.view';
const STATE_SEARCH = 'main.campanha.list';

/* @ngInject */
export default function campanhaListCtrl(
	$translate,
	$scope,
	$location,
	$state,
	roles,
	Campanha
) {
	const $ctrl = this;

	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	const loadObject = () => {
		Campanha.get()
			.$promise.then(data => {
				$ctrl.object = data;
			});
	};

	$ctrl.save = () => {
		$ctrl.object.dimensao = false;
		const traduzirTexto = entity => 
			$translate(['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR'])
				.then(translations => ({ entity, translation: translations['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR'] }));
		const exibirMensagem = ({ entity, translation }) => {
			$scope.$emit('notify-success', '', $ctrl.titulo + translation);
			$state.go(STATE_VIEW, {objectId: entity.id});
		};
		
		$ctrl.object.$save()
			.then(traduzirTexto)
			.then(exibirMensagem);
	};

	//TODO Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.authPodeIncluir = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);

	$ctrl.displayView = item => {$state.go(STATE_VIEW, { objectId: item.id });};

	$ctrl.back = () => {
		//TODO: Padronizar esse comportamento
		if ($location.search().from) {
			$state.go(atob($location.search().from), {...$location.search()});
		} else {
			$state.go(STATE_SEARCH);
		}
	};

	(loadObject)();
}
