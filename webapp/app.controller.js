import angular from 'angular';

import './partials/dialogs/dialog_trocar_senha.html';

/* @ngInject */
function appController(
	$scope,
	$translate,
	$rootScope,
	$timeout,
	$interval,
	$mdDialog,
	$state,
	$mdMedia,
	fuseTheming,
	UserService,
	Funcionario,
	MensageriaResource
) {
	$scope.mensagensNaoLidas = null;
	$scope.stopTime = null;
	$scope.txtVerificarMensagens = false;

	const mensagensNaoLidas = (user) => {
		$scope.txtVerificarMensagens = true;
		var promise = MensageriaResource.mensagensNaoLidas({
			id: user.id
		}).$promise;

		promise.then(function (data) {
			$scope.txtVerificarMensagens = false;
			$scope.mensagensNaoLidas = data.mensagensNaoLidas;
		});
	};

	const verificarMensagens = (user) => () => mensagensNaoLidas(user);

	//FIXME: Esse controller é chamado apenas uma vez, (ao carregar a aplicação), logo, ele não altera o 'user'
	UserService.logged().$promise.then(user => {
		mensagensNaoLidas(user);
		$scope.atualizacaoColaborador(user);
		$scope.stopTime = $interval(verificarMensagens(user), 300000);
	}, (error) => {
		$scope.$emit('notify-critical', error);
	});

	$scope.themes = fuseTheming.themes;
	fuseTheming.setActiveTheme('default');

	$scope.layoutMode = 'wide';
	var bodyEl = angular.element('body');

	$scope.$on('$viewContentAnimationEnded', function () {
		$rootScope.$broadcast('msSplashScreen::remove');
	});

	// Update class on body element
	bodyEl.toggleClass('boxed', ($scope.layoutMode === 'boxed'));

	$scope.booleanList = [{
		value: true,
		label: 'Sim',
		trLabel: 'FORM.YES'
	}, {
		value: false,
		label: 'Não',
		trLabel: 'FORM.NO'
	}];
	$scope.pontuacaoList = [{
		value: 1,
		label: '1'
	},
	{
		value: 2,
		label: '2'
	},
	{
		value: 3,
		label: '3'
	},
	{
		value: 4,
		label: '4'
	},
	{
		value: 5,
		label: '5'
	},
	{
		value: 6,
		label: '6'
	},
	{
		value: 7,
		label: '7'
	},
	{
		value: 8,
		label: '8'
	},
	{
		value: 9,
		label: '9'
	},
	{
		value: 10,
		label: '10'
	}
	];

	$scope.atualizacaoColaborador = function (user) {
		var promise = Funcionario.getSemFiltro({
			id: user.id
		}).$promise;
		promise.then(function (data) {
			var funcionario = data;
			if (funcionario.primeiroAcesso === null || funcionario.primeiroAcesso === true) {
				$scope.acaoTrocarSenha();
			} else if (funcionario.preencheFormacaoEIdiomas === null || funcionario.preencheFormacaoEIdiomas === true) {
				$state.go('formacoesidiomas', {
					objectId: funcionario.id
				});
			}
		}).catch(function (response) {
			$scope.$emit('notify-critical', response);
		});
	};

	//=== DIALOG ===// - Remover depois
	function dialogs($scope, $state, $mdDialog) {
		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			$mdDialog.cancel();
		};

		$scope.confirmar = function () {
			$mdDialog.hide();
		};
	}

	//=== AÇÃO IMPORTAR ===//  - Remover depois
	$scope.acaoContrucao = function (ev) {
		$mdDialog.show({
			controller: dialogs,
			locals: {},
			templateUrl: 'partials/dialogs/dialog_em_contrucao.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	// DIALOG PARA TROCA SE SENHA
	function dialogTrocaSenha($scope, $mdDialog, title, enabledButtonClose, enabledButtonCancel, fn_confirm) {
		$scope.title = title;
		$scope.object = {};
		$scope.action = false;
		$scope.enabledButtonClose = enabledButtonClose;
		$scope.enabledButtonCancel = enabledButtonCancel;

		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			$scope.hide();
		};

		$scope.setAction = function (value) {
			$scope.action = value;
		};

		$scope.confirm = function () {
			fn_confirm($scope.object, $scope.hide, $scope.setAction);
		};

		// eslint-disable-next-line no-useless-escape
		const strongRegex = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{6,})');
		const mediumRegex = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{6,})');

		const validpasswordStrong = (val) => {
			$scope.basicForm.nova_senha_atual.$setValidity('passwordStrong', val);
		};

		$scope.validationInputPwdText = (value) => {
			if (value && value.length > 5 && strongRegex.test(value)) {
				validpasswordStrong(true);
				$scope.passwordStrength = 4;
				$scope.passwordStrengthMsg = 'Senha forte';
			} else if (value && value.length > 5 && mediumRegex.test(value)) {
				validpasswordStrong(true);
				$scope.passwordStrength = 2;
				$scope.passwordStrengthMsg = 'Senha média';
			} else if (value && value.length > 0) {
				validpasswordStrong(true);
				$scope.passwordStrength = 0;
				$scope.passwordStrengthMsg = 'Senha Fraca';
			} else {
				validpasswordStrong(false);
				$scope.passwordStrength = undefined;
				$scope.passwordStrengthMsg = '';
			}
		};
	}

	// ACAO PARA TROCAR SENHA
	$scope.acaoTrocarSenha = function () {
		$scope.confirm = function (object, fn_Hide, fn_set_action) {
			fn_set_action(true);
			object.funcionario = $rootScope.user;
			Funcionario.trocarSenha(object)
				.$promise.then(function (data) {
					$timeout(function () {
						$scope.$emit('', 'Redefinição de senha realizada com sucesso!');
						//$scope.notification.success('', 'Redefinição de senha realizada com sucesso!');
						fn_Hide();

						if (object && object.funcionario && object.funcionario.primeiroAcesso) {
							var botaoAjuda = document.getElementById('ajuda-competencia');
							if (angular.isDefined(botaoAjuda)) {
								$('#ajuda-competencia').click();
							}
						}

						if (data.preencheFormacaoEIdiomas === null || data.preencheFormacaoEIdiomas === true)
							$state.go('formacoesidiomas', {
								objectId: $rootScope.user.id
							});
					}, 1000);
				}).catch(function (response) {
					fn_set_action(false);
					const codeErr = `${response.data[0].code}`;
					$translate([codeErr]).then(translations => {
						$scope.$emit('notify-error',
							`${translations[codeErr]}`,
							'Atenção!'
						);
					});
				});
		};

		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;

		$mdDialog.show({
			controller: dialogTrocaSenha,
			locals: {
				title: 'Redefinir senha',
				enabledButtonClose: false,
				enabledButtonCancel: false,
				fn_confirm: $scope.confirm
			},
			templateUrl: 'dialog_trocar_senha.html',
			parent: angular.element(document.body),
			targetEvent: angular.element(document.body),
			clickOutsideToClose: false,
			escapeToClose: true,
			fullscreen: useFullScreen
		});
		$scope.$watch(function () {
			return $mdMedia('xs') || $mdMedia('sm');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	$rootScope.tableControl = {
		limit: 20,
		limit_options: [5, 10, 15, 20, 25, 30, 35, 40, 45, 50]
	};
}

export default appController;
