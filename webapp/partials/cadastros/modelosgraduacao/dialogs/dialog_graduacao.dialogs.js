import angular from 'angular';
import './dialog_graduacao.html';

const TK_NEW_SUCCESS = 'MODELOS.GRADUACAOCRIADA';
const TK_EDIT_SUCCESS = 'MODELOS.GRADUACAOATUALIZADA';
const TITLE = 'MODELOS.GRADUACAO';

/* @ngInject */
export default function DialogGraduacaoService(
	$mdDialog,
	Graduacao,
	ModeloGraduacao,
	DialogConfirm
) {
	/* @ngInject */
	const loadTranslations = ($translate) => $translate([
		TK_NEW_SUCCESS,
		TK_EDIT_SUCCESS,
		TITLE,
	]);

	const onLoadModeloGraduacao = modeloGraduacao => Promise.resolve(modeloGraduacao)
		.then(object => isIdOnly(object)
			? ModeloGraduacao.get({ id: object.id }).$promise
			: object);

	const onLoadGraduacao = graduacao => Promise.resolve(graduacao)
		.then(object => isIdOnly(object)
			? Graduacao.get({ id: object.id }).$promise
			: object);

	const show = (object, {
		mode,
		messages,
		modeloGraduacao,
		targetEvent,
		fullscreen,
		onSuccess,
		onSave,
		onCancel,
	}) => {
		return onLoadModeloGraduacao(modeloGraduacao || object.modeloGraduacao)
			.then(modeloGraduacao => $mdDialog.show({
				targetEvent,
				fullscreen,
				clickOutsideToClose: false,
				controller: DialogGraduacaoCtrl,
				templateUrl: 'dialog_graduacao.html',
				parent: angular.element(document.body),
				locals: {
					mode,
					object,
					modeloGraduacao,
					messages,
					onSuccess,
					onSave,
					onCancel,
				},
				resolve: {
					translations: loadTranslations,
				},
			}));
	};

	const view = (graduacao, options) => {
		return onLoadGraduacao(graduacao)
			.then(object => show(object, {
				...options,
				mode: 'VIEW',
			}));
	};

	const edit = (graduacao, options) => {
		return onLoadGraduacao(graduacao)
			.then(object => show(object, {
				...options,
				mode: 'EDIT',
			}));
	};

	const create = (options) => {
		const { modeloGraduacao, template } = options;
		const object = template || {
			modeloGraduacao,
			situacao: 'ATIVA',
		};
		return show(object, { ...options, mode: 'NEW' });
	};

	/* @ngInject */
	function DialogGraduacaoCtrl(
		$scope,
		mode,
		object,
		modeloGraduacao,
		messages,
		onSuccess,
		onSave,
		onCancel,
		translations,
	) {
		messages = messages || {};
		const onDummy = value => value;
		const $ctrl = this;
		$scope.$ctrl = this;

		const defaultTitle = translations[TITLE];

		const defaultSuccessMessage =
			mode === 'EDIT'
				? translations[TK_EDIT_SUCCESS]
				: mode === 'NEW'
					? translations[TK_NEW_SUCCESS]
					: '';

		const onDefaultSuccess = result => {
			notifySuccess({ message: messages.success || defaultSuccessMessage });
			return result;
		};

		if (!onSave) {
			switch (mode) {
			case 'EDIT':
				onSave = ({ object }) => Graduacao.update({ id: object.id }, object).$promise;
				break;
			case 'NEW':
				onSave = ({ object }) => Graduacao.save(null, object).$promise;
				break;
			}
		}

		$ctrl.mode = mode;
		$ctrl.object = object;
		$ctrl.modeloGraduacao = modeloGraduacao;
		$ctrl.title = messages.title || defaultTitle;
		$ctrl.messages = messages;
		$ctrl.canSave = mode === 'EDIT' || mode === 'NEW';

		$ctrl.cancel = () => {
			return Promise.resolve()
				.then(onCancel || onDummy)
				.then(object => ({ object, mode, canceled: true }))
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};

		$ctrl.save = () => {
			// $mdDialog.hide();
			// return DialogConfirm.show('DIALOG.ATENCAO','DIALOG.MSG_EDIT_MG')
			// 	.then(onSave)
			// 	.then(object => ({ object, mode, canceled: false }))
			// 	.then(data => {
			// 		(onSuccess || onDefaultSuccess)(data);
			// 		return data;
			// 	})
			// 	.catch($mdDialog.cancel);
			return DialogConfirm.show(
				'DIALOG.ATENCAO',
				'DIALOG.MSG_EDIT_MG'
			).then( () => {
				return Promise.resolve({ object })
					.then(onSave)
					.then(object => ({ object, mode, canceled: false }))
					.then(data => {
						(onSuccess || onDefaultSuccess)(data);
						//FIXME: Criar um view model como padrao singleton,
						//ou remover a componentização e jogar tudo no controller do view!
						window.location.reload(false);
						return data;
					})
					.then($mdDialog.hide)
					.catch($mdDialog.cancel);
			})
				.catch($mdDialog.cancel);
		};

		function notifySuccess(params) {
			$scope.$emit(
				'notify-success',
				params.title || '',
				params.message,
			);
		}
	}

	return { view, edit, create };
}

function isIdOnly(object) {
	const keys = Object.keys(object);
	return keys.length === 1 && keys[0] === 'id';
}
