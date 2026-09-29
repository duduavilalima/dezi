//FIXME Reestruturar o código de tratamento de erros
/* eslint-disable max-len */
import angular from 'angular';

import toastr from 'angular-toastr';
import 'angular-toastr/dist/angular-toastr.css';

import moment from 'moment';
import 'moment/min/locales.min.js';

import DataUtils from './dataUtil';

// Tratamento de erros
import errorProcessorProvider from './rest-error';

//TODO Separar as notificações em outro módulo
export default angular.module('utils', [toastr])
	.factory('ValidationService', validationServiceProvider)
	.factory('ConfigurationService', configurationServiceProvider)
	.factory('ErrorProcessor', errorProcessorProvider)
	.factory('DataUtil', dataUtilFactory)
	.run(notificationConfig)
	.name;

function dataUtilFactory() {
	return DataUtils();
}

function notificationConfig($rootScope, toastr, $mdDialog) {
	const toastrConfig = {
		allowHtml: true,
		closeButton: true,
		extendedTimeOut: 1000,
		progressBar: true,
		timeOut: 5000
	};
	const toastrErroConfig = {
		allowHtml: true,
		closeButton: true,
	};
	const notification = {
		info: (title, message) => {
			toastr.info(message, title, toastrConfig);
		},
		success: (title, message) => {
			toastr.success(message, title, toastrConfig);
		},
		warning: (title, message) => {
			toastr.warning(message, title, toastrConfig);
		},
		error: (title, message) => {
			toastr.error(message, title, toastrErroConfig);
		},
		criticalError: (status, statusText, message) => {
			if (!message) {
				message = '<ul><li>' + status + '</li>';

				if (statusText) {
					message += '<li>' + statusText + '</li>';
				}

				message = '<ul><li>' + status + '</li>';
				if (statusText) {
					message += '<li>' + statusText + '</li>';
				}

				message += '</ul>';
			}

			toastr.error(message, 'Erro crítico', toastrErroConfig);
		}
	};

	$rootScope.$on('notify-info', (event, message, title) => {
		notification.info(title ? title : '', message);
	});

	$rootScope.$on('notify-success', (event, message, title) => {
		notification.success(title ? title : '', message);
	});

	$rootScope.$on('notify-warning', (event, message, title) => {
		notification.warning(title ? title : 'Aviso', message);
	});

	$rootScope.$on('notify-error', (event, message, title) => {
		notification.error(title ? title : 'Erro', message);
	});

	let dialogOpen = false;

	$rootScope.$on('notify-critical', response => {
		if (response.status === 401 || response.status === 403) {
			if (!dialogOpen) {
				localStorage.setItem('last_address_logout', window.location.href);
				showAlert();
			}
		} else if(response.status){
			notification.criticalError(response.status, response.statusText,
				response.data !== null && response.data !== undefined && JSON.stringify(response.data));
		}
	});

	const showAlert = () => {
		$mdDialog.show(
			$mdDialog.alert()
				.parent(angular.element(document.querySelector('#popupContainer')))
				.clickOutsideToClose(false)
				.title('Sessão encerrada')
				.textContent('Sua sessão foi encerrada. Faça um novo login.')
				.ariaLabel('Sessão encerrada')
				.ok('Continuar')
		).then(() => {
			dialogOpen = false;
			//FIXME: Recarregar a lista
			window.location.reload(false);
		});
		dialogOpen = true;
	};
}

function validationServiceProvider() {
	var validations = {
		verificarPreenchimento: function (campo) {
			return campo !== undefined && campo != null && campo !== '';
		},

		verificarTamanhoMenorIgual: function (campo, tamanho) {
			if (!tamanho) {
				throw new Error('Parametro tamanho não está preenchido.');
			}

			return campo.length <= tamanho && this.verificarPreenchimento(campo);
		},

		verificarFormatoEmail: function (email) {
			return email.match(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i);
		},

		verificarSomenteLetras: function (campo) {
			return campo.match(/\d/g) ? false : true;
		},

		validarEmail: function (email) {
			var validacaoPreenchimentoAndFormato = this.verificarPreenchimento(email) && this.verificarTamanhoMenorIgual(email, 100);
			var validacao = validacaoPreenchimentoAndFormato && this.verificarFormatoEmail(email);

			return validacao;
		},

		validarNome: function (nome) {
			return this.verificarPreenchimento(nome) && this.verificarSomenteLetras(nome);
		},

		calcularDiasEntre: function (date1, date2) {
			//formato do brasil 'pt-br'
			moment.locale('pt-br');
			//setando data1
			var data1 = moment(date1).format('L');
			//setando data2
			var data2 = moment(date2).format('L');
			//tirando a diferenca da data2 - data1 em dias
			var diff = data2.diff(data1, 'days');

			return diff;
		},

		validarCpf: function (cpf) {
			cpf = cpf.replace(/[^\d]+/g, '');
			if (cpf === '') return false;
			// Elimina CPFs invalidos conhecidos
			if (cpf.length != 11 || /([0-9])\1{10}/.test(cpf))
				return false;
			var add = 0;
			for (var i = 0; i < 9; i++)
				add += parseInt(cpf.charAt(i)) * (10 - i);
			var rev = 11 - (add % 11);
			if (rev == 10 || rev == 11)
				rev = 0;
			if (rev != parseInt(cpf.charAt(9)))
				return false;
			// Valida 2o digito
			add = 0;
			for (i = 0; i < 10; i++)
				add += parseInt(cpf.charAt(i)) * (11 - i);
			rev = 11 - (add % 11);
			if (rev == 10 || rev == 11)
				rev = 0;
			if (rev != parseInt(cpf.charAt(10)))
				return false;
			return true;
		},

		validarTelefone: function (telefone) {
			return telefone.match(/^[1-9]{2}[2-9][0-9]{7,8}$/g);
		},

		somenteNumeros: function (campo) {
			return campo.match(/\d/g);
		}
	};

	return validations;
}

function configurationServiceProvider($cookies, $timeout) {
	var configuration = {};

	configuration.font = function (user) {
		var selectedFont = {};

		if (angular.isUndefined($cookies.get('font'))) {
			if (angular.isDefined(user.configuracao) && user.configuracao != null && angular.isDefined(user.configuracao.tamanhoFonte) &&
				user.configuracao.tamanhoFonte != null && user.configuracao.tamanhoFonte.trim() != '') {
				$cookies.put('font', user.configuracao.tamanhoFonte.trim());
				selectedFont.code = user.configuracao.tamanhoFonte.trim();

				if (selectedFont.code == 'small') {
					$timeout(function () {
						angular.element('#small').triggerHandler('click');
					}, 100);
				} else if (selectedFont.code == 'large') {
					$timeout(function () {
						angular.element('#large').triggerHandler('click');
					}, 100);
				}
			} else {
				$cookies.put('font', 'medium');
				selectedFont.code = 'medium';
			}
		} else {
			selectedFont.code = $cookies.get('font');

			if (selectedFont.code == 'small') {
				$timeout(function () {
					angular.element('#small').triggerHandler('click');
				}, 100);
			} else if (selectedFont.code == 'large') {
				$timeout(function () {
					angular.element('#large').triggerHandler('click');
				}, 100);
			}
		}

		return selectedFont;
	};

	return configuration;
}
