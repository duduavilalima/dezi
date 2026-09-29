import angular from 'angular';

import './dialog_tipocompetencia.html';
import { isIdOnly } from 'utils/entity.utils';

/* @ngInject */
export default function DialogTipoCompetenciaService(
	$mdDialog,
	$mdMedia,
	$translateInterpolation,
	TipoCompetencia,
) {
	const noAction = () => undefined;
	const defaultAction = () => undefined;

	const createTipoCompetencia = ({ object }) => {
		return TipoCompetencia.save(null, object).$promise;
	};

	const updateTipoCompetencia = ({ object }) => {
		return TipoCompetencia.update({ id: object.id }, object).$promise;
	};

	const loadTipoCompetencia = tipoCompetencia => {
		return Promise.resolve(tipoCompetencia)
			.then(object => isIdOnly(object)
				? TipoCompetencia.get({ id: object.id }).$promise
				: object);
	};

	const translateMessagesObject = (tipoCompetencia, options, defaultMessages = {}) => {
		const hasPai =
			(options.pai || options.idPai)
			|| (tipoCompetencia && (tipoCompetencia.pai || tipoCompetencia.idPai));

		const TR_ENTITY = hasPai
			? 'TIPODECOMPETENCIAS.SUBTIPO'
			: 'TIPODECOMPETENCIAS.NOME';
		const messagesInput = options.messages || {};

		const messages = {
			...messagesInput,
			title: messagesInput.title
				|| [defaultMessages.title, { entity: TR_ENTITY }],
			successMessage: messagesInput.successMessage
				|| (defaultMessages.successMessage && [defaultMessages.successMessage, { entity: TR_ENTITY }]),
		};

		return $translateInterpolation(messages);
	};

	const show = (object, {
		mode,
		messages: {
			title,
			successMessage,
			successTitle,
		},
		onConfirm,
		onCancel,
		onSuccess,
		targetEvent,
		fullscreen,
	}) => {
		// TODO Transferir para serviço de dialog próprio `DialogTipoCompetencia.show`
		const useFullScreen = $mdMedia('xs');

		return $mdDialog.show({
			targetEvent,
			controller: DialogAssociationCtrl,
			locals: {
				object,
				mode,
				title,
				successMessage,
				successTitle,
				onConfirm: onConfirm || noAction,
				onCancel: onCancel || noAction,
				onSuccess: onSuccess || noAction,
			},
			templateUrl: 'dialog_tipocompetencia.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: fullscreen !== undefined ? fullscreen : useFullScreen,
		});
	};

	const view = (tipoCompetencia, options = {}) => {
		const defaultMessages = {
			title: 'DIALOG.TITULO_VISUALIZAR',
		};
		return loadTipoCompetencia(tipoCompetencia)
			.then(object => translateMessagesObject(object, options, defaultMessages)
				.then(messages => show(object, {
					...options,
					messages,
					mode: 'VIEW',
				}))
			);
	};

	const edit = (tipoCompetencia, options = {}) => {
		const { onConfirm, onSuccess } = options;
		const defaultMessages = {
			title: 'DIALOG.TITULO_EDITAR',
			successMessage: 'DIALOG.MSG_NOTIFICACAO_ALTERADO',
		};

		return loadTipoCompetencia(tipoCompetencia)
			.then(object => translateMessagesObject(object, options, defaultMessages)
				.then(messages => show(object, {
					...options,
					messages,
					mode: 'EDIT',
					onConfirm: onConfirm || updateTipoCompetencia,
					onSuccess: onSuccess || defaultAction,
				}))
			);
	};

	const create = (options = {}) => {
		const { onConfirm, onSuccess } = options;
		const { pai, idPai, template } = options;
		const defaultMessages = {
			title: 'DIALOG.TITULO_CRIAR',
			successMessage: 'DIALOG.MSG_NOTIFICACAO_CRIADO',
		};
		const object = template || {
			pai,
			idPai: idPai || (pai ? pai.id : undefined),
			situacao: 'ATIVA',
		};

		return translateMessagesObject(object, options, defaultMessages)
			.then(messages => show(object, {
				...options,
				messages,
				mode: 'NEW',
				onConfirm: onConfirm || createTipoCompetencia,
				onSuccess: onSuccess || defaultAction,
			}));
	};

	/* @ngInject */
	function DialogAssociationCtrl(
		$scope,
		$mdDialog,
		mode,
		title,
		successTitle,
		successMessage,
		object,
		onConfirm,
		onCancel,
		onSuccess,
	) {
		const $ctrl = this;
		$scope.$ctrl = this;

		$ctrl.title = title;
		$ctrl.object = object;

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

		$ctrl.cancel = () => {
			const payload = { object, mode, canceled: true };
			return Promise.resolve(payload)
				.then(onCancel)
				.then(result => ({ ...payload, result }))
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};

		$ctrl.save = function() {
			const payload = { object, mode, canceled: false };
			return Promise.resolve(payload)
				.then(onConfirm)
				.then(result => Promise
					.resolve(onSuccess({ ...payload, result }))
					.then(() => ({ ...payload, result })))
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};
	}

	return { view, edit, create };
}
