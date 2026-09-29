import angular from 'angular';

import moment from 'moment';

import '../pages/contato/dialog_contato.html';
import '../dialogs/dialog_trocar_senha.html';

/* @ngInject */
function toolbarController(
	$scope,
	$rootScope,
	$mdSidenav,
	$window,
	$cookies,
	$http,
	$state,
	$mdMedia,
	$mdDialog,
	$timeout,
	$translate,
	Funcionario,
	FotoResource,
	UserService,
	MensageriaResource,
	$interval,
	ConfiguracaoResource,
	ConfigurationService
) {
	//FIXME: Código replicado no app.controller,
	//não está chamando mais de uma vez os métodos de lá
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
		})
			.catch(error => {
				$scope.$emit('notify-critical', error);
			});
	};

	const verificarMensagens = (user) => () => mensagensNaoLidas(user);
	$scope.stopTime = $interval(verificarMensagens($scope.user), 300000);

	// TODO Criar um evento de mudança de usuario
	$rootScope.$watch('user', function (user) {
		$scope.user = user;
		if (angular.isDefined($scope.user) && angular.isDefined($scope.user.id)) {
			loadFuncionarioFoto($scope.user.id);
			$scope.loadConfiguracoes($scope.user);
			mensagensNaoLidas($scope.user);
		}
	});

	$scope.smallAction = function (event) {
		$scope.selectedFont.code = 'small';
		$scope.changeFont($scope.selectedFont);
		event.preventDefault();
		$('ms-widget').animate({ 'font-size': '80%' });
		$('md-input-container').animate({ 'font-size': '80%' });
		//			$("table").animate({"font-size": "80%"});
		$('textarea').animate({ 'font-size': '80%' });
		$('input').animate({ 'font-size': '80%' });
		$('md-checkbox').animate({ 'font-size': '80%' });
		$('md-container').animate({ 'font-size': '80%' });
		$('md-select').animate({ 'font-size': '80%' });
		$('button').animate({ 'font-size': '80%' });
		$('td').animate({ 'font-size': '80%' });
		$('th').animate({ 'font-size': '80%' });
		$('.breadcrumb').animate({ 'font-size': '80%' });
		$('.h2').animate({ 'font-size': '80%' });
		$('.h3').animate({ 'font-size': '80%' });
		//            $("table.md-table td.md-cell").animate({"font-size": "80%"});
		//		tela de preenchimentos
		$('.white-bg').animate({ 'font-size': '80%' });
		$('.ng-binding').animate({ 'font-size': '80%' });
	};

	$scope.mediumAction = function (event) {
		$scope.selectedFont.code = 'medium';
		$scope.changeFont($scope.selectedFont);
		event.preventDefault();
		$('ms-widget').animate({ 'font-size': '100%' });
		//			$("table").animate({"font-size": "100%"});
		$('textarea').animate({ 'font-size': '100%' });
		$('input').animate({ 'font-size': '100%' });
		$('md-checkbox').animate({ 'font-size': '100%' });
		$('button').animate({ 'font-size': '100%' });
		$('td').animate({ 'font-size': '100%' });
		$('th').animate({ 'font-size': '100%' });
		$('md-table-pagination').animate({ 'font-size': '100%' });
		$('.breadcrumb').animate({ 'font-size': '100%' });
		$('.h2').animate({ 'font-size': '100%' });
		$('.h3').animate({ 'font-size': '100%' });
		//			$("table.md-table td.md-cell").animate({"font-size": "100%"});
		//		tela de preenchimentos
		$('.white-bg').animate({ 'font-size': '100%' });
		$('.ng-binding').animate({ 'font-size': '100%' });
	};

	$scope.largeAction = function (event) {
		$scope.selectedFont.code = 'large';
		$scope.changeFont($scope.selectedFont);
		event.preventDefault();
		$('ms-widget').animate({ 'font-size': '130%' });
		//			$("table").animate({"font-size": "130%"});
		$('textarea').animate({ 'font-size': '130%' });
		$('md-checkbox').animate({ 'font-size': '130%' });
		$('input').animate({ 'font-size': '130%' });
		$('md-container').animate({ 'font-size': '130%' });
		$('button').animate({ 'font-size': '130%' });
		$('td').animate({ 'font-size': '130%' });
		$('th').animate({ 'font-size': '130%' });
		$('md-table-pagination').animate({ 'font-size': '130%' });
		$('.breadcrumb').animate({ 'font-size': '130%' });
		$('.h2').animate({ 'font-size': '150%' });
		$('.h3').animate({ 'font-size': '130%' });
		// tela de preenchimentos
		//		$(".space-10").animate({"font-size": "130%"});
		//		$(".md-body-1").animate({"font-size": "130%"});
		//		$(".pl-16").animate({"font-size": "130%"});
		$('.white-bg').animate({ 'font-size': '150%' });
		$('.ng-binding').animate({ 'font-size': '130%' });

		//			$("table.md-table td.md-cell").animate({"font-size": "130%"});
	};

	$scope.loadConfiguracoes = function (user) {
		$scope.loadConfiguracaoLang(user);
		$scope.loadConfiguracaoFont(user);
	};

	$scope.loadConfiguracaoLang = function (user) {
		if (angular.isUndefined($cookies.get('lang'))) {
			if (angular.isDefined(user.configuracao) && user.configuracao != null && angular.isDefined(user.configuracao.linguagem)
				&& user.configuracao.linguagem != null && user.configuracao.linguagem.trim() != '') {
				$cookies.put('lang', user.configuracao.linguagem.trim());
				$scope.selectedLanguage = { code: user.configuracao.linguagem.trim() };
				$translate.use($scope.selectedLanguage.code);
			} else {
				$cookies.put('lang', $translate.use());
				$scope.selectedLanguage = { code: $translate.proposedLanguage() };
				$translate.use($scope.selectedLanguage.code);
			}
		} else {
			$scope.selectedLanguage = { code: $cookies.get('lang') };
			$translate.use($scope.selectedLanguage.code);
		}
	};

	$scope.loadConfiguracaoFont = function (user) {
		$scope.selectedFont = ConfigurationService.font(user);
	};

	$scope.changeLanguage = function (lang) {
		var configuracao = angular.copy($rootScope.user.configuracao);
		configuracao.linguagem = lang.code.trim();

		moment.locale(lang.code.trim());

		var promiseAlteracao = ConfiguracaoResource.persistirLinguagem(configuracao).$promise;
		promiseAlteracao.then(function (data) {
			$rootScope.user.configuracao = data;
			$scope.selectedLanguage = lang;
			$translate.use(lang.code);
			$cookies.put('lang', $translate.proposedLanguage());
		}).catch(function (response) {
			$scope.$emit('notify-critical', response);
		});
	};

	$scope.changeFont = function (font) {
		if ($rootScope.user.configuracao.tamanhoFonte != font.code.trim()) {
			var configuracao = angular.copy($rootScope.user.configuracao);
			configuracao.tamanhoFonte = font.code.trim();

			var promise = ConfiguracaoResource.persistirFonte(configuracao).$promise;
			promise.then(function (data) {
				$scope.selectedFont = font;
				$cookies.put('font', data.tamanhoFonte.trim());
			}).catch(function (response) {
				$scope.$emit('notify-critical', response);
			});
		}
	};

	$scope.languages = {
		pt: {
			'title': 'Português',
			'translation': 'TOOLBAR.BRAZIL',
			'code': 'pt_BR',
			'flag': 'br'
		},
		en: {
			'title': 'Inglês',
			'translation': 'TOOLBAR.ENGLISH',
			'code': 'en_US',
			'flag': 'us'
		},
		es: {
			'title': 'Espanhol',
			'translation': 'TOOLBAR.SPANISH',
			'code': 'es',
			'flag': 'es'
		},
		fr: {
			'title': 'Francês',
			'translation': 'TOOLBAR.FRENCH',
			'code': 'fr',
			'flag': 'fr'
		}
	};

	// OBTÉM A FOTO DO FUNCIONÁRIO
	var loadFuncionarioFoto = function (id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;

		filtro.tipo = {};
		filtro.tipo = 'FOTO_PERFIL';

		//TODO O mesmo código está no perfil.controller e funcionario.controller, bibliotecar isso
		const imageChanged = (data) => {
			if (!data || data.length == 0) return;

			const imageData = data[0];

			$scope.cropper = {};
			$scope.cropper.id = imageData.id;
			$scope.cropper.funcionario = imageData.funcionario;
			$scope.cropper.tipo = imageData.tipo;
			$scope.cropper.sourceImage = imageData.base64;
			$scope.cropper.croppedImage = imageData.base64;
		};

		FotoResource.buscar(null, filtro).$promise.then((data) => imageChanged(data));
	};

	$scope.logout = function () {
		UserService.logout();
		$state.go('login.empresa', { empresaId: $scope.user.empresa.id });
		//TODO Usar o router e passar ou para o UserService ou para hook do router
		//$window.location.href = '#!/login/' + $scope.user.empresa.id;
	};

	$scope.perfil = function () {
		$state.go('main.perfil');
	};

	$scope.mensagens = function () {
		$state.go('main.mensagens');
	};

	$scope.bodyEl = angular.element('body');

	// Methods
	$scope.toggleSidenav = function (sidenavId) {
		$mdSidenav(sidenavId).toggle();
	};

	$scope.toggleHorizontalMobileMenu = function () {
		$scope.bodyEl.toggleClass('ms-navigation-horizontal-mobile-menu-active');
	};

	// DIALOG PARA TROCA DE SENHA
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
			object.funcionario = $scope.user;
			Funcionario.trocarSenha(object)
				.$promise.then(function () {
					$timeout(function () {
						$scope.$emit('notify-success', '', 'Redefinição de senha realizada com sucesso');
						fn_Hide();
						//FIXME: Ao alterar a senha,
						// o usuario não consegue acessar mais nenhuma parte do sistema,
						//por isso o logout
						$scope.logout();
					}, 500);
				}).catch(function (response) {
					fn_set_action(false);
					const codeErr = `${response.data[0].code}`;
					$translate(['validation.'+codeErr]).then(translations => {
						$scope.$emit('notify-error',
							`${translations['validation.'+codeErr]}`,
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
				enabledButtonClose: true,
				enabledButtonCancel: true,
				fn_confirm: $scope.confirm
			},
			templateUrl: 'dialog_trocar_senha.html',
			parent: angular.element(document.body),
			targetEvent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: useFullScreen
		});
		$scope.$watch(function () {
			return $mdMedia('xs') || $mdMedia('sm');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	$scope.contatoDialog = function (ev) {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;

		$mdDialog.show({
			controller: 'ContatoController',
			templateUrl: 'dialog_contato.html',
			parent: angular.element(document.body),
			//FIXME Estava duplicado, qual o correto? targetEvent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: useFullScreen
		});
		$scope.$watch(function () {
			return $mdMedia('xs') || $mdMedia('sm');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};
}

export default toolbarController;
