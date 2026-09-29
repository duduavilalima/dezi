/* @ngInject */
export default function modeloGraduacaoCadastroCtrl(
	$state,
	$scope,
	$translate,
	roles,
	modeloGraduacao,
	navigator,
	Graduacao,
	ModeloGraduacao,
	DialogConfirm,
	DialogDetalhes,
	DialogGraduacao,
	DialogSituacao
) {
	const $ctrl = this;
	$state.$ctrl = $ctrl;

	const { editRoles } = $state.current.data;

	$ctrl.canEdit = roles.some(role => editRoles.some(authority => role.authority === authority));
	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.goEdit = () => {
		navigator.goEdit($ctrl.object.id);
	};

	$ctrl.goList = () => {
		navigator.goList();
	};

	$ctrl.goBack = () => {
		navigator.goList();
	};

	$ctrl.onChangeSituacao = (item, type) => {
		if(type === 'modeloGraduacao') {
			DialogSituacao.show(item, {
				Resource: ModeloGraduacao,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'MODELOS.MODELO' }
			})
				.then(({ object, canceled }) => {
					$ctrl.mdswitch.situacao = isAtivo(object);
					refresh(!canceled);
				});
		} else if (type === 'graduacao') {
			DialogSituacao.show(item, {
				Resource: Graduacao,
				situacaoKey: ['status', 'situacao'],
				messages: { entity:'MODELOS.GRADUACAO' },
				useActivationEndpoint: true
			})
				.then(({ canceled }) => {
					refresh(!canceled);
				});
		}
	};

	$ctrl.inativarBtnDescer = (index, listaGraduacoes) => {
		if (listaGraduacoes[index].label == 'Não posso avaliar'
			|| (!(index==listaGraduacoes.length) && listaGraduacoes[index+1].label == 'Não posso avaliar')
			||	listaGraduacoes[index].situacao == 'INATIVA'
			|| listaGraduacoes[index].ordem == null)
			return true;

		return false;
	};

	const refresh = (forceReload = false) => {
		if (forceReload) {
			loadObject();
		} else {
			$scope.$apply();
		}
	};

	$ctrl.onRemoverGraduacao = createDialogCallback({
		trKeys: {
			entityName: 'MODELOS.GRADUACAO',
			title: 'DIALOG.TITULO_REMOVER',
			message: 'DIALOG.MSG_REMOVER_GRADUACAO',
			successMessage: 'DIALOG.MSG_NOTIFICACAO_REMOVIDO_GRADUACAO',
		},
		onAction: ({ item }) => {
			return Graduacao
				.delete({ id: item.id })
				.$promise
				.then(loadObject);
		},
	});

	$ctrl.onNovaGraduacao = (targetEvent) => {
		const modeloGraduacao = $ctrl.object;
		DialogGraduacao.create({ targetEvent, modeloGraduacao }).then(loadAssociations);
	};

	$ctrl.onEditarGraduacao = (graduacao, targetEvent) => {
		const modeloGraduacao = $ctrl.object;
		DialogGraduacao.edit(graduacao, { targetEvent, modeloGraduacao }).then(loadAssociations);
	};

	$ctrl.onVisualizarGraduacao = (graduacao, targetEvent) => {
		const modeloGraduacao = $ctrl.object;
		DialogGraduacao.view(graduacao, { targetEvent, modeloGraduacao });
	};

	$ctrl.showDetalhes = (targetEvent) => {
		DialogDetalhes.show($ctrl.object, { targetEvent });
	};

	$ctrl.onMoveGraduacaoUp = (item, index) => {
		if (index > 0 && index < $ctrl.graduacoes.length) {
			const above = $ctrl.graduacoes[index - 1];

			index++;
			above.ordem = index;
			item.ordem = index - 1;

			above.modeloGraduacao = $ctrl.object;
			item.modeloGraduacao = $ctrl.object;

			Promise.all(
				[item, above].map(value =>
					Graduacao.update({ id: value.id }, value).$promise
				))
				.then(loadObject)
				.catch(notifyRestError);
		}
	};

	$ctrl.onMoveGraduacaoDown = (item, index) => {
		if (index >= 0 && index < $ctrl.graduacoes.length - 1) {
			const below = $ctrl.graduacoes[index + 1];

			index++;
			below.ordem = index;
			item.ordem = index + 1;

			below.modeloGraduacao = $ctrl.object;
			item.modeloGraduacao = $ctrl.object;

			Promise.all(
				[item, below].map(value =>
					Graduacao.update({ id: value.id }, value).$promise
				))
				.then(loadObject)
				.catch(notifyRestError);
		}
	};

	$ctrl.isAtivo = isAtivo;
	$ctrl.isInativo = isInativo;

	//
	// UTILITÁRIOS
	//

	function isAtivo(valor) {
		return typeof valor === 'string'
			? valor === 'ATIVA'
			: isAtivo(valor.situacao);
	}

	function isInativo(valor){
		return typeof valor === 'string'
			? valor === 'INATIVA'
			: isInativo(valor.situacao);
	}

	function loadObject() {
		$ctrl.object = modeloGraduacao;
		$ctrl.mdswitch = { situacao: isAtivo(modeloGraduacao) };

		ModeloGraduacao
			.get({ id: $ctrl.object.id })
			.$promise
			.then(data => {
				$ctrl.object = data;
				$ctrl.mdswitch.situacao = $ctrl.isAtivo($ctrl.object);
				loadAssociations();
			})
			.catch(error => {
				$ctrl.object = null;
				notifyRestError(error);
			});
	}

	function loadAssociations() {
		const filter = { modeloGraduacao: { id: $ctrl.object.id } };
		delete $ctrl.graduacoes;
		Graduacao.graduacoesPorModelo(null, filter)
			.$promise
			.then(data => {
				data.sort((a, b) => {
					a = a.ordem;
					b = b.ordem;
					return a - b;
				});
				$ctrl.graduacoes = data;
			})
			.catch(error => {
				$ctrl.graduacoes = null;
				notifyRestError(error);
			});
	}

	function notifySuccess(params) {
		$scope.$emit(
			'notify-success',
			params.title || '',
			params.message,
		);
	}

	function notifyRestError(response) {
		console.error(response);
		$scope.$emit('notify-critical', response);
	}

	function createDialogCallback(options) {
		const { trKeys, onAction } = options;
		let { onCancel, messages } = options;
		const KEYS = Object.keys(trKeys).map(k => trKeys[k]);

		onCancel = onCancel || (() => undefined);
		messages = messages || {};

		return (event, item) => $translate(KEYS)
			.then(translations => {
				// FIXME Usar traduções com parâmetros de mensagem, ao invés de montá-las dinamicamente
				// Considerar o uso do DialogConfirm.show()
				const entityName = messages.entityName || translations[trKeys.entityName];
				const title = `${messages.title || translations[trKeys.title]} ${entityName}`;
				const message = `${messages.message || translations[trKeys.message]} ${item.label || entityName}?`;
				const successMessage = `${entityName} ${messages.successMessage || translations[trKeys.successMessage]}`;

				const onConfirm = object => {
					const params = { object, event, item };
					return onAction(params)
						.then(() => notifySuccess({ message: successMessage }))
						.catch(notifyRestError);
				};

				return DialogConfirm.confirm({
					messages: {
						title,
						message,
					},
					onConfirm,
					onCancel,
				});
			});
	}

	//
	// STARTUP
	//

	loadObject();
}
