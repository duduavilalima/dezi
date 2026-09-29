import angular from 'angular';

/* @ngInject */
export default function DialogFuncionarioFotoService(
	$mdDialog,
	FotoResource,
	$notify,
) {
	const defaultAction = value => value;
	const noAction = value => value;

	/* @ngInject */
	function DialogFuncionarioFotoController(
		$scope,
		$mdDialog,
		funcionario,
		cropper,
		onConfirm,
		onSuccess,
		onCancel,
	) {
		const {
			success: notifySuccess,
			// restError: notifyRestError,
		} = $notify.atScope($scope).tr;

		$scope.cropper = cropper;
		$scope.bounds = {
			left: 0,
			bottom: 0,
			right: 200,
			top: 200,
		};

		if (onConfirm === defaultAction) {
			onConfirm = payload => {
				const { funcionario, fotoBase64, cropper } = payload;
				const tipo = cropper.tipo || 'FOTO_PERFIL';

				if (funcionario && funcionario.id) {
					const { id } = cropper;

					if (id) {
						return FotoResource.update({ id }, {
							id,
							tipo,
							funcionario,
							base64: fotoBase64,
						}).$promise;
					} else {
						return FotoResource.save(null, {
							tipo,
							funcionario,
							base64: fotoBase64,
						}).$promise;
					}
				}

				// Retornando objeto pronto para ser salvo (após salvar o funcionário)
				return { tipo, base64: fotoBase64 };
			};
		}

		if (onSuccess === defaultAction) {
			onSuccess = ({ result }) => {
				// Não mostrar notificação se a foto não foi salva
				// (e.g. tela de criação, funcionário ainda não foi salvo mas a foto foi escolhida)
				if (result && result.id) {
					notifySuccess('FUNCIONARIO.MSG_FOTO_SALVA');
				}
			};
		}

		if (onCancel === defaultAction) {
			onCancel = noAction;
		}

		$scope.confirmar = () => {
			const canceled = false;
			const { croppedImage: fotoBase64 } = cropper;

			const payload = {
				canceled,
				fotoBase64,
				funcionario,
				cropper,
			};

			return Promise.resolve(payload)
				.then(onConfirm)
				.then(result => {
					const successPayload = { ...payload, result };
					return Promise.resolve(onSuccess(successPayload))
						.then(() => successPayload);
				})
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};

		$scope.cancel = () => {
			const canceled = true;
			const payload = {
				funcionario,
				canceled,
			};

			if (!funcionario || !funcionario.id) {
				cropper.sourceImage = undefined;
			}

			cropper.croppedImage = cropper.sourceImage;

			return Promise.resolve(payload)
				.then(onCancel)
				.then(result => ({ ...payload, result }))
				.then($mdDialog.hide)
				.catch($mdDialog.cancel);
		};
	}

	// FIXME Migrar lógica do cropper e de load/store da foto do colaborador
	// para um serviço próprio. Refatorar também o colaborador-edit-card,
	// para que ambos usem este serviço. Carregar fotos a partir apenas do ID do
	// funcionário, se um objeto cropper não for fornecido.
	function show(funcionario, {
		targetEvent,
		cropper,
		onConfirm,
		onSuccess,
		onCancel,
	}) {
		return $mdDialog.show({
			targetEvent,
			controller: DialogFuncionarioFotoController,
			templateUrl: 'colaborador_foto.dialog.html',
			parent: angular.element(document.body),
			locals: {
				funcionario,
				cropper: cropper,
				onConfirm: onConfirm || defaultAction,
				onSuccess: onSuccess || defaultAction,
				onCancel: onCancel || defaultAction,
			},
			clickOutsideToClose: false,
			escapeToClose: true,
			fullscreen: false,
		});
	}

	return { show };
}
