import angular from 'angular'; //TODO Trocar no sistema os utilitários do angular por seus equivalentes modernos

const STATE_NEW = 'main.campanha.new';
const STATE_VIEW = 'main.campanha.view';
const STATE_EDIT = 'main.campanha.edit';
const STATE_SEARCH = 'main.campanha.list';

/* @ngInject */
function campanhaController(
	$scope,
	$state,
	$rootScope,
	Campanha
) {
	function init() {
		$scope.listResult = null;

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;
	}

	$scope.displayBtnNovo = false;

	var usuarioLogado = undefined;
	$rootScope.$watch('user', function (user) {
		if (!usuarioLogado && user) {
			usuarioLogado = user;
			if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
				$scope.displayBtnNovo = true;
			}
		}
	});

	$scope.query = {
		order: 'descricao',
		limit: 10,
		page: 1
	};

	$scope.find = function () {
		$scope.listResult = Campanha.search({});
		$scope.listResult.$promise.catch(function (response) {
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	};

	$scope.displayNew = function () {
		$state.go(STATE_NEW);
	};

	$scope.displayView = function (item) {
		$state.go(STATE_VIEW, { objectId: item.id });
	};

	$scope.displayEdit = function (item) {
		$state.go(STATE_EDIT, { objectId: item.id });
	};

	$scope.back = function () {
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go(STATE_SEARCH);
		}
	};

	init();

	$scope.find();
}

/* @ngInject */
function campanhaCadastroCtrl(
	$scope, $state, $stateParams, $rootScope, $timeout, $translate,
	Campanha, Empresa, Avaliacao, DialogConfirm, DialogWait, $mdDialog) {
	// INICIAR VARIÁVEIS
	function init() {
		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.empresaList = [];

		$scope.listCompetencias = null;
	}

	$scope.displayBtnNovo = false;
	var usuarioLogado = undefined;

	//TODO Unificar esse comportamento
	$rootScope.$watch('user', function (user) {
		if (!usuarioLogado && user) {
			usuarioLogado = user;
			if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
				$scope.displayBtnNovo = true;
			}
		}
	});

	function listAvaliacoes(campanha) {
		/* Foi usado promise em uma variável pois Luiz reclamou que a mensagem de
		 * não há registro aparecia antes da lista de avaliações serem carregadas.
		 * Assim (código abaixo) a tabela md-table-container recebe o promise e mostra
		 * a barra de "carregamento" esperando o resultado do backend.
		 */
		var listAvaliacoesPromise = Avaliacao.porCampanha({ campanhaId: campanha.id });
		listAvaliacoesPromise
			.$promise.then(function (data) {
				$scope.listAvaliacoes = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	init();

	$scope.createObject = function () {
		$scope.displayBtnNovo = false;

		Campanha.get()
			.$promise.then(function (data) {
				$scope.object = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

	$scope.mdswitch = {};

	$scope.onChangeSituacao = function (situacao) {
		$scope.mdswitch.situacao = situacao;
		if ($scope.object.status === 'ATIVA') {
			$scope.inactivate(null, $scope.object);
		} else {
			$scope.activate(null, $scope.object);
		}
	};

	$scope.loadObject = function () {
		$scope.objectId = $stateParams.objectId;

		if (!$scope.objectId) {
			$scope.$emit('crud-error', 'invalid-id');
			return;
		} else {
			$scope.object = Campanha.get({ id: $scope.objectId });
			$scope.object.$promise
				.then(function (data) {
					$scope.mdswitch.situacao = $scope.object.status === 'ATIVA';
					if ($scope.screenMode === 'VIEW') {
						listAvaliacoes(data);
					}
				})
				.catch(function (response) {
					$scope.notification.criticalError(response.status, response.statusText, response);
				});
		}
	};

	$scope.acaoNovaAvaliacao = function () {
		$state.go('avaliacao_new', { campanhaId: $scope.object.id });
	};

	$scope.acaoExibirAvaliacao = function (ev, item) {
		$state.go('avaliacao_view', { campanhaId: $scope.object.id, objectId: item.id });
	};

	$scope.displayNew = function () {
		$state.go(STATE_NEW);
	};

	$scope.displayView = function (item) {
		$state.go(STATE_VIEW, { objectId: item.id });
	};

	$scope.displayEdit = function (item) {
		$state.go(STATE_EDIT, { objectId: item.id });
	};

	$scope.displayEditAvaliacao = function (ev, item) {
		$state.go('avaliacao_edit', { campanhaId: $scope.object.id, objectId: item.id });
	};

	$scope.back = function () {
		if ($scope.screenMode === 'VIEW' || $scope.screenMode === 'NEW') {
			$state.go(STATE_SEARCH);
		} else if ($scope.screenMode === 'EDIT') {
			$scope.displayView($scope.object);
		} else if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go(STATE_SEARCH);
		}
	};

	$scope.save = function () {
		$scope.action = true;
		if ($scope.screenMode === 'NEW') {
			const obj = angular.copy($scope.object);
			obj.$save()
				.then(function (data) {
					//FIXME Achar uma forma melhor do que esse $translate no meio do código
					$translate(['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR']).then(function (translations) {
						const texto = translations['CAMPANHA.NOTIFICACAO_TEXTO_SALVAR'];
						$scope.notification.success('', $scope.titulo + texto);
					});

					$state.go(STATE_VIEW, {
						objectId: data.id
					});
				}).catch(function (response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		} else if ($scope.screenMode === 'EDIT') {
			const obj = angular.copy($scope.object);
			obj.$update({ id: $scope.objectId })
				.then(function (data) {
					$translate(['CAMPANHA.NOTIFICACAO_TEXTO_EDICAO']).then(function (translations) {
						const texto = translations['CAMPANHA.NOTIFICACAO_TEXTO_EDICAO'];
						$scope.notification.success('', $scope.titulo + texto);
					});

					$state.go(STATE_VIEW, {
						objectId: data.id
					});
				}).catch(function (response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		}
	};

	$scope.acaoRepetirAvaliacao = function (event, item) {
		var msg = 'Deseja repetir a Avaliação: ' + item.descricaoAbreviada + '?';

		$translate([
			'AVALIACAO.COPY',
			'DIALOG.TITULO_REPETIR',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.NOTIFICACAO_TITULO_REPETIR',
			'DIALOG.NOTIFICACAO_TEXTO_REPETIR',
			'DIALOG.MSG_AGUARDAR',
			'DIALOG.MSG_REPETIR'
		]).then(function (translations) {
			const title = translations['DIALOG.TITULO_REPETIR'];
			const notificacaoTexto = translations['DIALOG.NOTIFICACAO_TEXTO_REPETIR'];
			const msgAguardar = translations['DIALOG.MSG_AGUARDAR'];

			msg = translations['DIALOG.MSG_REPETIR'];
			msg = msg + item.descricaoAbreviada + '?';

			$scope.confirmRepetir = function () {
				var wait = DialogWait.show(msgAguardar);

				item.descricaoAbreviada = item.descricaoAbreviada + ' - ' + translations['AVALIACAO.COPY'];

				var promisseAv = Avaliacao.copiar(item).$promise;

				promisseAv.then(function () {
					$scope.loadObject();
					// Timeout é devido a um bug no componente wait.
					$timeout(function () {
						wait.hide();
						$scope.notification.success('', notificacaoTexto);
					}, 500);
				}).catch(function (response) {
					// Timeout é devido a um bug no componente wait.
					$timeout(function () {
						wait.hide();
						console.error(response);
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					}, 500);
				});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirmRepetir,
				$scope.cancel
			);
		});
	};

	$scope.acaoLiberarAvaliacao = function (event, item) {
		$translate([
			'DIALOG.TITULO_LIBERAR',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.NOTIFICACAO_TITULO_LIBERAR',
			'DIALOG.NOTIFICACAO_TEXTO_LIBERAR',
			'DIALOG.MSG_AGUARDAR_LIBERAR',
			'DIALOG.MSG_LIBERAR'
		]).then(function (translations) {
			var title = translations['DIALOG.TITULO_LIBERAR'];
			var notificacaoTexto = translations['DIALOG.NOTIFICACAO_TEXTO_LIBERAR'];
			var msgAguardar = translations['DIALOG.MSG_AGUARDAR_LIBERAR'];

			var msg = translations['DIALOG.MSG_LIBERAR'] + item.descricaoAbreviada + '?';

			$scope.confirmLiberacao = function () {
				var wait = DialogWait.showClick(msgAguardar, false);

				item.status = 'LIBERADA';
				var promisseLiberacao = Avaliacao.liberarAvaliacao(item).$promise;

				promisseLiberacao.then(function () {
					$timeout(function () {
						angular.forEach($scope.listAvaliacoes, function (avaliacao) {
							if (avaliacao.id == item.id) {
								item.status = 'LIBERADA';
								item.situacao = 'LIBERADA';
							}
						});

						wait.hide();
						$scope.notification.success('', notificacaoTexto);
					}, 600);
				}).catch(function (response) {
					$timeout(function () {
						wait.hide();
						console.error(response);
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					}, 600);
				});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirmLiberacao,
				$scope.cancel
			);
		});
	};

	$scope.acaoConcluirAvaliacao = function (event, item) {
		$translate([
			'DIALOG.TITULO_CONCLUIR',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.NOTIFICACAO_TITULO_CONCLUIR',
			'DIALOG.NOTIFICACAO_TEXTO_CONCLUIR',
			'DIALOG.MSG_AGUARDAR_CONCLUIR',
			'DIALOG.MSG_CONCLUIR'
		]).then(function (translations) {
			var title = translations['DIALOG.TITULO_CONCLUIR'];
			var notificacaoTexto = translations['DIALOG.NOTIFICACAO_TEXTO_CONCLUIR'];
			var msgAguardar = translations['DIALOG.MSG_AGUARDAR_CONCLUIR'];

			var msg = translations['DIALOG.MSG_CONCLUIR'] + item.descricaoAbreviada + '?';

			$scope.confirmLiberacao = function () {
				var wait = DialogWait.showClick(msgAguardar, false);

				item.$transition({ status: 'CONCLUIDA' })
					.then(function () {
						$timeout(function () {
							wait.hide();
							$scope.notification.success('', notificacaoTexto);
						}, 600);
					})
					.catch(function (response) {
						$timeout(function () {
							wait.hide();
							console.error(response);
							$scope.notification.criticalError(response.status, response.statusText, response.data);
						}, 600);
					});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirmLiberacao,
				$scope.cancel
			);
		});
	};

	$scope.acaoCancelarAvaliacao = function (event, item) {
		$translate([
			'DIALOG.TITLE_REMOVE',
			'DIALOG.MSG_REMOVER',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.REMOVE_CANCEL',
			'DIALOG.TITLE_CANCEL_NOTIFICATION',
			'DIALOG.TEXT_REMOVE_NOTIFICATION'
		]).then(function (translations) {
			var title = translations['DIALOG.TITLE_REMOVE'];
			var textNotification = translations['DIALOG.TEXT_REMOVE_NOTIFICATION'];

			var msg = translations['DIALOG.MSG_REMOVER'] + ': ' + item.descricaoAbreviada + '?';

			$scope.confirm = function (object, fn_Hide) {
				Avaliacao.removeragliberacao({ id: item.id })
					.$promise.then(function () {
						$scope.loadObject();
						$scope.notification.success('', textNotification);
						fn_Hide();
					}).catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						fn_Hide();
					});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirm,
				$scope.cancel
			);
		});
	};

	$scope.acaoInativarAvaliacao = function (event, item) {
		$translate([
			'DIALOG.TITLE_INATIVAR',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.TEXT_INATIVAR_AVAL_NOTIFICATION',
			'DIALOG.MSG_INATIVAR_AVAL'
		]).then(function (translations) {
			var title = translations['DIALOG.TITLE_INATIVAR'];
			var notificacaoTexto = translations['DIALOG.TEXT_INATIVAR_AVAL_NOTIFICATION'];

			var msg = translations['DIALOG.MSG_INATIVAR_AVAL'] + item.descricaoAbreviada + '?';

			$scope.confirm = function (object, fn_Hide) {
				item.$transition({ status: 'INATIVA' })
					.then(function () {
						$scope.notification.success('', notificacaoTexto);
						fn_Hide();
					})
					.catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						fn_Hide();
					});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirm,
				$scope.cancel
			);
		});
	};

	$scope.acaoAtivarAvaliacao = function (event, item) {
		$translate([
			'DIALOG.TITLE_ATIVAR',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.TEXT_ATIVAR_AVAL_NOTIFICATION',
			'DIALOG.MSG_ATIVAR_AVAL'
		]).then(function (translations) {
			var title = translations['DIALOG.TITLE_ATIVAR'];
			var notificacaoTexto = translations['DIALOG.TEXT_ATIVAR_AVAL_NOTIFICATION'];

			var msg = translations['DIALOG.MSG_ATIVAR_AVAL'] + item.descricaoAbreviada + '?';

			$scope.confirm = function (object, fn_Hide) {
				item.$transition({ status: 'LIBERADA' })
					.then(function () {
						$scope.notification.success('', notificacaoTexto);
						fn_Hide();
					})
					.catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						fn_Hide();
					});
			};

			$scope.cancel = function (fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirm,
				$scope.cancel
			);
		});
	};

	$scope.activate = function (event, item) {
		$translate([
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.TITLE_ATIVAR',
			'DIALOG.TITLE_ATIVAR_NOTIFICATION',
			'DIALOG.TEXT_ATIVAR_NOTIFICATION',
			'DIALOG.MSG_ATIVAR'
		]).then(function (translations) {
			var title = translations['DIALOG.TITLE_ATIVAR'];
			var textNotification = translations['DIALOG.TEXT_ATIVAR_NOTIFICATION'];

			var msg = translations['DIALOG.MSG_ATIVAR'] + 'campanha' + ': ' + item.descricao + '?';

			$scope.confirm = function (object, fn_Hide) {
				$scope.object.$transition({ status: 'ATIVA' })
					.then(function () {
						$scope.mdswitch.situacao = true;
						$scope.notification.success('', textNotification);
						fn_Hide();
					})
					.catch(function (response) {
						if (response.status == 400) {
							$scope.notification.error(response.status, response.statusText, response.data);
						} else {
							$scope.notification.criticalError(response.status, response.statusText, response.data);
						}

						fn_Hide();
					});
			};

			$scope.cancel = function (fn_Hide) {
				$timeout(function () {
					$scope.mdswitch.situacao = false;
					$scope.$apply();
				}, 300);
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirm,
				$scope.cancel
			);
		});
	};

	$scope.inactivate = function (event, item) {
		$translate([
			'DIALOG.BUTTON_CANCEL',
			'DIALOG.BUTTON_CONFIRM',
			'DIALOG.TITLE_INATIVAR',
			'DIALOG.TITLE_INATIVAR_NOTIFICATION',
			'DIALOG.TEXT_INATIVAR_NOTIFICATION',
			'DIALOG.MSG_INATIVAR'
		]).then(function (translations) {
			var title = translations['DIALOG.TITLE_INATIVAR'];
			var textNotification = translations['DIALOG.TEXT_INATIVAR_NOTIFICATION'];

			var msg = translations['DIALOG.MSG_INATIVAR'] + 'campanha' + ': ' + item.descricao + '?';

			$scope.confirm = function (object, fn_Hide) {
				$scope.object.$transition({ status: 'INATIVA' })
					.then(function () {
						$scope.notification.success('', textNotification);
						$scope.mdswitch.situacao = false;
						fn_Hide();
					}).catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						fn_Hide();
					});
			};

			$scope.cancel = function (fn_Hide) {
				$timeout(function () {
					$scope.mdswitch.situacao = true;
					$scope.$apply();
				}, 300);
				fn_Hide();
			};

			DialogConfirm.show(
				event,
				title,
				msg,
				$scope.confirm,
				$scope.cancel
			);
		});
	};

	$scope.detalhes = function (ev) {
		$mdDialog.show({
			controller: DialogController,
			locals: {
				object: $scope.object
			},
			templateUrl: 'partials/dialogs/detalhes.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true
		})
			.then(function (answer) {
				$scope.status = 'You said the information was "' + answer + '".';
			}, function () {
				$scope.status = 'You cancelled the dialog.';
			});
	};

	function DialogController($scope, $mdDialog, object) {
		$scope.object = object;

		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			$mdDialog.cancel();
		};

		$scope.answer = function (answer) {
			$mdDialog.hide(answer);
		};
	}

	if ($scope.screenMode === 'NEW') {
		$scope.createObject();
	} else {
		$scope.loadObject();
	}
}

export {
	campanhaController,
	campanhaCadastroCtrl
};
