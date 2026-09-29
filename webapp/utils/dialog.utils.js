import angular from 'angular';

import '../partials/dialogs/dialog_wait.html';
import '../partials/dialogs/dialog_confirm.html';
import '../partials/dialogs/dialog_detalhes.html'; //TODO Unificar o código aqui
import '../partials/dialogs/dialog_trocar_senha.html';
import { isAtivo } from 'model/Situacao';
import Situacao from '../partials/model/Situacao';

export default angular.module('utils.dialog', [])
	.factory('DialogWait', dialogWaitFactory)
	.factory('DialogConfirm', dialogConfirmFactory)
	.factory('DialogDetalhes', dialogDetalhesFactory)
	.factory('DialogSituacao', dialogSituacaoFactory)
	.factory('DialogTrocarSenha', dialogTrocarSenhaFactory)
	.name;

/* @ngInject */
function dialogWaitFactory($mdDialog) {
	var service = {};

	function dialogCtrl($scope, $mdDialog, mensagem) {
		$scope.mensagem = mensagem;

		$scope.hide = $mdDialog.hide;
		$scope.cancel = $mdDialog.cancel;
		$scope.confirmar = $mdDialog.hide;
	}

	service.show = function (msg) {
		return service.showClick(msg, true);
	};

	service.showClick = function (msg, value) {
		$mdDialog.show({
			controller: dialogCtrl,
			templateUrl: 'dialog_wait.html',
			locals: {
				mensagem: msg
			},
			parent: angular.element(document.body),
			clickOutsideToClose: value,
			escapeToClose: true
		});

		return $mdDialog;
	};

	return service;
}

/* @ngInject */
function dialogConfirmFactory($mdDialog, $translate) {
	var service = {};

	const noCallback = () => undefined;
	const noAction = payload => payload;
	const defaultAction = payload => payload;

	/* @ngInject */
	function dialogCtrl(
		$scope,
		$mdDialog,
		title,
		message,
		successTitle,
		successMessage,
		onConfirm,
		onSuccess,
		onCancel,
	) {
		$scope.title = title;
		$scope.message = message;
		if (onConfirm === defaultAction) {
			throw new Error('Não há ação padrão para onConfirm.');
		}

		if (onCancel === defaultAction) {
			onCancel = noAction;
		}

		if (onSuccess === defaultAction) {
			onSuccess = () => {
				if (successMessage) {
					$scope.$emit('notify-success', successTitle || '', successMessage);
				}
			};
		}

		$scope.cancel = () => {
			const canceled = true;

			if (onCancel !== noCallback) {
				const payload = { canceled };
				return Promise.resolve(payload)
					.then(onCancel)
					.then(result => ({ ...payload, result }))
					.then($mdDialog.hide)
					.catch($mdDialog.cancel);
			}

			return $mdDialog.cancel();
		};

		$scope.confirm = () => {
			const canceled = false;

			if (onConfirm !== noCallback) {
				const payload = { canceled };
				return Promise.resolve(payload)
					.then(onConfirm)
					.then(result => Promise
						.resolve(onSuccess({ ...payload, result }))
						.then(() => ({ ...payload, result }))
					)
					.then(result => Promise.resolve(onSuccess(result)).then(() => result))
					.then($mdDialog.hide)
					.catch($mdDialog.cancel);
			}

			return $mdDialog.hide();
		};
	}

	function show(
		title,
		message,
		params,
	) {
		const translateParams = (params) => {
			//TODO Avaliar se faz sentido manter essa tradução aqui o devemos passar a responsabilidade para quem chama o diálogo
			if (!params) return Promise.resolve({}); //TODO Tem que verificar se não é um map/objeto

			const translationPromises = [];

			Object.keys(params).forEach(key => {
				translationPromises.push($translate(params[key])
					.then(value => {
						return [key, value];
					})
					.catch(() => [key, params[key]])
				);
			});

			return Promise.all(translationPromises)
				.then(translations => {
					return translations.reduce((newParams, item) => {
						newParams[item[0]] = item[1];
						return newParams;
					}, {});
				})
				.catch(e => {
					// Não deixa de mostrar o diálogo por erro de tradução
					console.error('Error translating', e);
					return params;
				});
		};

		/* @ngInject */
		const translate = (id) => {
			return translateParams(params)
				.then(params => {
					return $translate(id, params);
				})
				.catch(e => {
					// Não deixa de mostrar o diálogo por erro de tradução
					console.error('Error translating', e);
					return id;
				});
		};

		return $mdDialog.show({
			controller: dialogCtrl,
			templateUrl: 'dialog_confirm.html',
			clickOutsideToClose: false,
			fullscreen: false,
			locals: {
				onConfirm: noCallback,
				onCancel: noCallback,
				onSuccess: noCallback,
				title: translate(title).then(tit => tit),
				message: translate(message).then(msg => msg),
				successTitle: '',
				successMessage: '',
			}
		}).then(() => {
			return Promise.resolve({});
		})
			.catch(() => {
				return Promise.reject({});
			});
	}

	function confirm({
		messages,
		onConfirm,
		onCancel,
		onSuccess,
	}) {
		return Promise.resolve(messages)
			.then(({
				title,
				message,
				successTitle,
				successMessage,
			}) => $mdDialog.show({
				controller: dialogCtrl,
				templateUrl: 'dialog_confirm.html',
				clickOutsideToClose: false,
				fullscreen: false,
				locals: {
					title,
					message,
					successTitle,
					successMessage,
					onConfirm: onConfirm || noAction,
					onCancel: onCancel || noAction,
					onSuccess: onSuccess || defaultAction,
				}
			}));
	}

	service.show = show;
	service.confirm = confirm;
	service.noAction = noAction;

	return service;
}

/* @ngInject */
function dialogDetalhesFactory($mdDialog) {
	var service = {};

	/* @ngInject */
	function dialogCtrl($scope, object) {
		$scope.object = object;
		$scope.cancel = $mdDialog.cancel;
	}

	function show(object, { targetEvent } = {}) {
		return $mdDialog.show({
			controller: dialogCtrl,
			templateUrl: 'dialog_detalhes.html',
			clickOutsideToClose: false,
			fullscreen: false,
			targetEvent,
			locals: {
				object: object
			}
		});
	}

	service.show = show;

	return service;
}

/* @ngInject */
function dialogSituacaoFactory(
	$translateInterpolation,
	$mdDialog,
) {
	const defaultAction = payload => payload;

	/* @ngInject */
	function dialogCtrl(
		$scope,
		$mdDialog,
		situacaoKey,
		object,
		Resource,
		title,
		message,
		successMessage,
		onConfirm,
		onCancel,
		onSuccess,
		useActivationEndpoint,
	) {
		const activationMethods = { [Situacao.ATIVA]: 'ativar', [Situacao.INATIVA]: 'inativar' };
		const hasActivationMethods = Object.keys(activationMethods)
			.map(key => activationMethods[key])
			.every(method => method in Resource);

		$scope.title = title;
		$scope.message = message;
		$scope.isAtivo = isAtivo(object);

		if (onSuccess === defaultAction) {
			onSuccess = () => {
				if (successMessage) {
					$scope.$emit('notify-success', successMessage);
				}
			};
		}

		if (useActivationEndpoint === undefined) {
			useActivationEndpoint = !situacaoKey && hasActivationMethods;
		} else if (useActivationEndpoint && !hasActivationMethods) {
			// console.warn('Falta um ou mais métodos de ativação/inativação no Resource passado.', Resource);
			useActivationEndpoint = false;
		}

		if (!situacaoKey) {
			situacaoKey = ['situacao'];
		}

		if (onConfirm === defaultAction) {
			// FIXME Usar os métodos/endpoints de ativar e inativar dos recursos REST
			onConfirm = ({ object }) => {
				const id = object.id;
				const situacao = isAtivo(object) ? Situacao.INATIVA : Situacao.ATIVA;
				const situacaoMethod = activationMethods[situacao];
				const body = { ...object };
				const updateObject = data => {
					situacaoKey.forEach(key => object[key] = data[key]);
					// console.log('salvo!', { body, data, object });
					return object;
				};

				if (useActivationEndpoint) {
					return Resource[situacaoMethod](null, body)
						.$promise
						.then(updateObject);
				} else {
					situacaoKey.forEach(key => body[key] = situacao);
					return Resource.update({ id }, body)
						.$promise
						.then(updateObject);
				}
			};
		}

		/**
		 * Callback de confirmação do Dialog.
		 *
		 * Retorna uma Promise a ser resolvida para um objeto contendo as seguintes propriedades:
		 * - **`canceled`**: sempre será `false`.
		 * - **`result`**: será sempre o resultado da função de callback `onConfirm`.
		 *    Se `onConfirm === noAction`, `result` será indefinido.
		 *    Se `onConfirm === defaultAction`, `result` será o valor retornado pela rotina/endpoint de
		 *    ativação/inativação da entidade.
		 * - **`object`**: será sempre o objeto passado inicialmente à `DialogSituacao.show()`. Se
		 *   `onConfirm === noAction`, os valores das propriedades nomeadas por `situacaoKey` no
		 *   `object` serão substituídas pelos valores retornados em `result`, i.e.
		 *   `situacaoKey.forEach(key => object[key] = result[key])`.
		 *
		 * A ação de confirmação só deve rejeitar a promise em caso de erros nos callbacks!
		 * Seja a promise resolvida ou rejeitada, a Dialog deverá sempre ser fechada logo em seguida.
		 */
		const confirm = () => {
			const canceled = false;

			const createResult = result => {
				const confirmResult = { object, canceled, result };

				return Promise.resolve(confirmResult)
					.then(onSuccess)
					.then(() => confirmResult);
			};

			return Promise.resolve({ object, canceled })
				.then(onConfirm)
				.then(createResult)
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};

		$scope.confirm = confirm;

		/**
		 * Callback de cancelamento do Dialog.
		 *
		 * Retorna uma Promise a ser resolvida para um objeto contendo as seguintes propriedades:
		 * - **`canceled`**: sempre será `true`.
		 * - **`result`**: será sempre o resultado da função de callback `onCancel`.
		 *    Se `onCancel === noAction`, `result` será indefinido.
		 * - **`object`**: será sempre o objeto passado inicialmente à `DialogSituacao.show()`. Se
		 *   `onConfirm === noAction`, os valores das propriedades nomeadas por `situacaoKey` no
		 *   `object` serão substituídas pelos valores retornados em `result`, i.e.
		 *   `situacaoKey.forEach(key => object[key] = result[key])`.
		 *
		 * `onCancel === defaultAction` é equivalente a `onCancel === noAction`.
		 *
		 * A ação de cancelamento só deve rejeitar a promise em caso de erros nos callbacks!
		 * Seja a promise resolvida ou rejeitada, a Dialog deverá sempre ser fechada logo em seguida.
		 */
		const cancel = () => {
			const canceled = true;

			const createResult = result => {
				return { object, canceled, result };
			};

			return Promise.resolve({ object, canceled })
				.then(onCancel)
				.then(createResult)
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};

		$scope.cancel = cancel;
	}

	const defaultGetName = (object) => {
		return object.tipoCompetencia ? object.tipoCompetencia.nome : undefined
			|| object.competencia ? object.competencia.descricaoAbreviada : undefined
			|| object.nome
			|| object.descricao
			|| object.descricaoAbreviada
			|| object.descricaoModelo
			|| object.label;
	};

	const show = (
		object,
		{
			targetEvent,
			Resource,
			useActivationEndpoint,
			messages,
			getName,
			situacaoKey,
			onConfirm,
			onCancel,
			onSuccess,
		}
	) => {
		const acaoSlug = isAtivo(object) ? 'INATIVAR' : 'ATIVAR';
		const situacaoSlug = isAtivo(object) ? 'INATIVADO' : 'ATIVADO';
		const circularMessageFix = {
			message: undefined,
			successMessage: undefined,
		};

		const defaultMessages = {
			title: `DIALOG.TITULO_${acaoSlug}`,
		};

		const finalMessages = {
			...defaultMessages,
			...messages,
		};

		if (typeof situacaoKey === 'string') {
			situacaoKey = [situacaoKey];
		}

		if (!finalMessages.entity) {
			throw new Error('Faltando chave de tradução do Recurso/Entidade!');
		}

		if (!getName) {
			getName = defaultGetName;
		}

		if (!finalMessages.name) {
			finalMessages.name = getName(object);

			finalMessages.$skip = finalMessages.$skip || [];
			finalMessages.$skip.push('name');
		}

		if (!finalMessages.message) {
			finalMessages.message = () => [
				`DIALOG.MSG_${situacaoSlug}`,
				{ ...finalMessages, ...circularMessageFix },
			];
		}

		if (!finalMessages.successMessage) {
			finalMessages.successMessage = [
				`DIALOG.MSG_NOTIFICACAO_${acaoSlug}`,
				{ ...finalMessages, ...circularMessageFix },
			];
		}

		return $translateInterpolation(finalMessages)
			.then(translations => {
				return $mdDialog.show({
					targetEvent,
					controller: dialogCtrl,
					templateUrl: 'dialog_confirm.html',
					clickOutsideToClose: false,
					fullscreen: false,
					locals: {
						object,
						Resource,
						situacaoKey,
						useActivationEndpoint,
						title: translations.title,
						message: translations.message,
						successMessage: translations.successMessage,
						onConfirm: onConfirm || defaultAction,
						onCancel: onCancel || defaultAction,
						onSuccess: onSuccess || defaultAction,
					}
				});
			});
	};

	return { show };
}

/* @ngInject */
function dialogTrocarSenhaFactory($mdDialog, $translate) {
	var service = {};

	/* @ngInject */
	function dialogCtrl($scope, $mdDialog, title, onConfirm) {
		$scope.hide = $mdDialog.hide;
		$scope.cancel = $mdDialog.cancel;

		$scope.title = title;
		$scope.object = {};
		$scope.action = false;

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

		const onSuccess = () => {
			$scope.$emit('notify-success', 'Redefinição de senha realizada com sucesso');
		};

		$scope.confirm = () => {
			const canceled = false;
			const payload = { canceled };
			return Promise.resolve(payload)
				.then(onConfirm($scope.object)
					.then(result => Promise
						.resolve(onSuccess({ ...payload, result }))
						.then(() => ({ ...payload, result }))
					)
					.then(result => Promise.resolve(onSuccess(result)).then(() => result))
					.then($mdDialog.hide)
					.catch((err) => {
						const codeErr = `validation.${err}`;
						$translate([codeErr]).then(translations => {
							$scope.$emit('notify-error',
								`${translations[codeErr]}`,
								'Atenção!'
							);
						});
					}));
		};
	}

	function show(onConfirm) {
		return $mdDialog.show({
			controller: dialogCtrl,
			locals: {
				title: 'Redefinir senha',
				onConfirm: onConfirm
			},
			templateUrl: 'dialog_trocar_senha.html',
			parent: angular.element(document.body),
			targetEvent: angular.element(document.body),
			clickOutsideToClose: false,
			escapeToClose: true,
			fullscreen: false
		});
	}

	service.show = show;

	return service;
}
