/* @ngInject */
export default function forgotController(
	$scope, 
	$stateParams,
	$translate, 
	AuthService, 
	$state
) {
	const $ctrl = this;
	$ctrl.user = {};
	$ctrl.user.empresa = {};
	
	if($stateParams.objectId) {
		$ctrl.user.empresa.id = $stateParams.objectId;
	}

	$ctrl.back = () => {
		if($ctrl.user.empresa.id) {
			$state.go('login.empresa', {empresaId: $ctrl.user.empresa.id});
		} else {
			$state.go('login');
		}
	};

	$ctrl.forgotPassword = () => {
		AuthService.forgotPassword($ctrl.user)
			.$promise
			.then(() => {
				$translate(['ESQUECEUSENHA.TITLESUCCESS', 'ESQUECEUSENHA.MESSAGESUCCESS'])
					.then((translations) => {
						$scope.$emit('notify-success', 
							translations['ESQUECEUSENHA.MESSAGESUCCESS'],
							translations['ESQUECEUSENHA.TITLESUCCESS']);
						$ctrl.back();
					});
			}).catch((response) => {
				$translate(response.data[0].code, {field: response.data[0].params[1]})
					.then((translations) => {
						$scope.$emit('notify-error', 
							translations,
							'Atenção!');
					});
			});
	};
}