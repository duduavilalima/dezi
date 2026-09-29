/*import angular from 'angular';

export default angular
	.module('app.competencia')
	.controller('CompetenciaListarCtrl', competenciaListarCtrl)
	.controller('CompetenciaCadastroCtrl', competenciaCadastroCtrl)
	.name;

function competenciaListarCtrl($scope, $state, $rootScope, $timeout, Competencia, UserService) {
	function init() {
		$scope.listResult = null;
		$scope.resource = Competencia;
		$scope.object = null;

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.goNew = 'competencia_new';
		$scope.goView = 'competencia_view';
		$scope.goEdit = 'competencia_edit';
		$scope.goSearch = 'competencias';

		$scope.checkImportPermit('ROLE_ADMINISTRADOR');
	}

	$scope.displayBtnNovo = false;

	$scope.import = null;

	var usuarioLogado = undefined;
	$rootScope.$watch('user', function(user) {
		if (angular.isUndefined(usuarioLogado)) {
			usuarioLogado = user;
			if (angular.isDefined(user)) {
				if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
					$scope.displayBtnNovo = true;
				} else {
					if ($rootScope.$lastState.state.name == 'app.preenchimento.avaliacoes') {
						$state.go('app.preenchimento.avaliacoes');
					} else if ($rootScope.$lastState.state.name == 'app.preenchimento.consensos') {
						$state.go('app.preenchimento.consensos');
					}
				}
			}
		}
	});

	//FIXME: Refatorar
	$scope.checkImportPermit = function(roles) {
		let aux = false;
		UserService.roles.then(
			function(resp) {
				angular.forEach(resp.data, function(value) {
					if (roles.indexOf(value.authority) >= 0) {
						aux = true;
						return;
					}
				});
				$scope.import = aux;
			},
			function(error) {
				console.log(error);
			}
		);
	};

	init();

	$scope.query = {
		order: 'descricaoAbreviada',
		limit: 10,
		page: 1,
	};

	$scope.find = function() {
		var filtro = {};
		$scope.promise = $scope.resource.buscar(null, filtro).$promise;


		$scope.promise
			.then(function(data) {
				$scope.listResult = data;
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

	$scope.displayNew = function() {
		$state.go($scope.goNew);
	};

	$scope.displayView = function(item) {
		$state.go($scope.goView, { objectId: item.id });
	};

	$scope.displayEdit = function(item) {
		$state.go($scope.goEdit, { objectId: item.id });
	};

	$scope.back = function() {
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			//FIXME $state.go(goSearch);
		}
	};

	$timeout(function() {
		$scope.find();
	}, 600);
}

function competenciaCadastroCtrl(
	$scope,
	$state,
	$stateParams,
	$rootScope,
	$timeout,
	$mdMedia,
	$mdDialog,
	Competencia,
	CompetenciaDetalhamentoCalibrahint,
	TipoResource,
	ModeloGraduacao,
	CompetenciaDetalhamento,
	DialogConfirm,
	Graduacao,
	ValidationService,
	$q,
	$translate,
	TipoCompetencia
) {
	// INICIAR VARIÁVEIS
	function init() {
		$scope.listResult = null;
		$scope.resource = Competencia;
		$scope.object = null;

		$scope.listAssociations = null;
		$scope.titleAssociation = 'Detalhamento / Comportamento observável';
		$scope.resourceAssociation = CompetenciaDetalhamento;
		$scope.resourceFindAssociation = CompetenciaDetalhamento.detalhamentosPorCompetencia

		;($scope.urlTemplateDialog = 'partials/dialogs/competencia/dialog_detalhamento.html'), ($scope.modulo = $state.current.data.modulo);
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.goNew = 'competencia_new';
		$scope.goView = 'competencia_view';
		$scope.goEdit = 'competencia_edit';
		$scope.goSearch = 'competencias';

		$scope.listModeloGraduacao = [];
		$scope.resourceMG = ModeloGraduacao;
		$scope.resourceGraduacao = Graduacao;
		$scope.resourceCompetenciaDetalhamentosCalibrahint = CompetenciaDetalhamentoCalibrahint;

		$scope.listTipoCompetencia = [];
		$scope.listaAvaliacao = null;
	}

	init();

	$scope.displayBtnNovo = false;
	var usuarioLogado = undefined;

	$rootScope.$watch('user', function(user) {
		if (angular.isUndefined(usuarioLogado)) {
			if (angular.isDefined(user)) {
				usuarioLogado = user;
				if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
					$scope.displayBtnNovo = true;
				} else {
					if ($rootScope.$lastState.state.name == 'app.preenchimento.avaliacoes') {
						$state.go('app.preenchimento.avaliacoes');
					} else if ($rootScope.$lastState.state.name == 'app.preenchimento.consensos') {
						$state.go('app.preenchimento.consensos');
					}
				}
			}
		}
	});

	function listTiposCompetencias() {
		TipoResource.tiposCompetencias()
			.$promise.then(function(data) {
				$scope.listTipoCompetencia = data;
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function modelosGraduacaoList() {
		var filtro = {};
		filtro.situacao = 'ATIVA';

		ModeloGraduacao.buscar(null, filtro)
			.$promise.then(function(data) {
				$scope.listModeloGraduacao = data;
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	$scope.createObject = function() {
		$scope.displayBtnNovo = false;

		$scope.resource
			.get()
			.$promise.then(function(data) {
				$scope.object = data;
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

	$scope.mdswitch = {};

	$scope.onChangeSituacao = function(situacao) {
		$scope.mdswitch.situacao = situacao;
		if ($scope.object.situacao === 'ATIVA') {
			$scope.inactivate(null, $scope.object);
		} else {
			$scope.activate(null, $scope.object);
		}
	};

	$scope.loadObject = function() {
		$scope.objectId = $stateParams.objectId;

		if (!$scope.objectId) {
			$scope.$emit('crud-error', 'invalid-id');
			return;
		} else {
			$scope.resource
				.get({ id: $scope.objectId })
				.$promise.then(function(data) {
					$scope.object = data;
					if ($scope.screenMode === 'VIEW') {
						carregarArvore();
					}

					$scope.mdswitch.situacao = $scope.object.situacao === 'ATIVA';
					$scope.loadAssociations($scope.object);
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}
	};

	$scope.createFilter = function(object) {
		var filter = null;

		if (ValidationService.verificarPreenchimento(object) && ValidationService.verificarPreenchimento(object.id)) {
			filter = {};
			filter = object;
		}

		return filter;
	};

	$scope.loadAssociations = function(object) {
		var filterAssociation = $scope.createFilter(object);

		if (filterAssociation != null) {
			$scope
				.resourceFindAssociation(filterAssociation)
				.$promise.then(function(data) {
					$scope.listAssociations = data;
					$scope.listAssociations.sort(function(a, b) {
						a = a.ordem;
						b = b.ordem;
						return a - b;
					});
				})
				.catch(function(response) {
					$scope.$emit('notify-critical', 'load', response);
				});
		}

		buscarListaAvaliacao(object);
	};

	$scope.displayNew = function() {
		$state.go($scope.goNew);
	};

	$scope.displayView = function(item) {
		$state.go($scope.goView, { objectId: item.id });
	};

	$scope.displayEdit = function(item) {
		$state.go($scope.goEdit, { objectId: item.id });
	};

	$scope.back = function() {
		if ($scope.screenMode === 'VIEW' || $scope.screenMode === 'NEW') {
			$state.go($scope.goSearch);
		} else if ($scope.screenMode === 'EDIT') {
			$scope.displayView($scope.object);
		} else if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$state.go($scope.goSearch);
		}
	};

	$scope.save = function() {
		$scope.action = true;
		if ($scope.screenMode === 'NEW') {
			$scope.object
				.$save()
				.then(function(data) {
					$scope.notification.success('', 'Competência salva com sucesso!');
					$state.go($scope.goView, { objectId: data.id });
				})
				.catch(function(response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		} else if ($scope.screenMode === 'EDIT') {
			$scope.object
				.$update({ id: $scope.objectId })
				.then(function(data) {
					$scope.notification.success('', 'Competência atualizada com sucesso!');
					$state.go($scope.goView, { objectId: data.id });
				})
				.catch(function(response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		}
	};

	$scope.activate = function(event, item) {
		$translate([
			'DIALOG_COMPETENCIA.TITULO_ATIVAR',
			'DIALOG_COMPETENCIA.BOTAO_CONFIRMAR',
			'DIALOG_COMPETENCIA.BOTAO_CANCELAR',
			'DIALOG_COMPETENCIA.MSG_ATIVAR',
			'DIALOG_COMPETENCIA.TITULO_NOTIFICACAO',
			'DIALOG_COMPETENCIA.MSG_NOTIFICACAO_ATIVADO',
		]).then(function(translations) {
			const title = translations['DIALOG_COMPETENCIA.TITULO_ATIVAR'];
			const msg = translations['DIALOG_COMPETENCIA.MSG_ATIVAR'] + item.descricaoAbreviada + '?';
			const msgNotification = translations['DIALOG_COMPETENCIA.MSG_NOTIFICACAO_ATIVADO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'ATIVA';
				$scope.resource
					.update({ id: $scope.objectId }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.mdswitch.situacao = true;
						$scope.notification.success('', '' + msgNotification);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			$scope.cancel = function(fn_Hide) {
				$timeout(function() {
					$scope.mdswitch.situacao = false;
					$scope.$apply();
				}, 300);
				fn_Hide();
			};

			DialogConfirm.show(event, title, msg, $scope.confirm, $scope.cancel);
		});
	};

	$scope.inactivate = function(event, item) {
		$translate([
			'DIALOG_COMPETENCIA.TITULO_INATIVAR',
			'DIALOG_COMPETENCIA.BOTAO_CONFIRMAR',
			'DIALOG_COMPETENCIA.BOTAO_CANCELAR',
			'DIALOG_COMPETENCIA.MSG_INATIVAR',
			'DIALOG_COMPETENCIA.TITULO_NOTIFICACAO_INATIVADO',
			'DIALOG_COMPETENCIA.MSG_NOTIFICACAO_INATIVADO',
		]).then(function(translations) {
			const title = translations['DIALOG_COMPETENCIA.TITULO_INATIVAR'];
			const msg = translations['DIALOG_COMPETENCIA.MSG_INATIVAR'] + item.descricaoAbreviada + '?';
			const msgNotification = translations['DIALOG_COMPETENCIA.MSG_NOTIFICACAO_INATIVADO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'INATIVA';
				$scope.resource
					.update({ id: $scope.objectId }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.mdswitch.situacao = false;
						$scope.notification.success('', '' + msgNotification);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			$scope.cancel = function(fn_Hide) {
				$timeout(function() {
					$scope.mdswitch.situacao = true;
					$scope.$apply();
				}, 300);
				fn_Hide();
			};

			DialogConfirm.show(event, title, msg, $scope.confirm, $scope.cancel);
		});
	};

	$scope.activateDetalhamento = function(event, item) {
		$translate([
			'DIALOG_COMPETENCIA.TITULO_ATIVAR_DETALHAMENTO',
			'DIALOG_COMPETENCIA.BOTAO_CONFIRMAR',
			'DIALOG_COMPETENCIA.BOTAO_CANCELAR',
			'DIALOG_COMPETENCIA.MSG_ATIVAR_DETALHAMENTO',
			'DIALOG_COMPETENCIA.TITULO_ATIVAR_NOTIFICACAO_DETALHAMENTO',
			'DIALOG_COMPETENCIA.MSG_ATIVAR_NOTIFICACAO_DETALHAMENTO',
		]).then(function(translations) {
			const title = translations['DIALOG_COMPETENCIA.TITULO_ATIVAR_DETALHAMENTO'];
			const msg = translations['DIALOG_COMPETENCIA.MSG_ATIVAR_DETALHAMENTO'] + item.descricao + '?';
			const msgNotification = translations['DIALOG_COMPETENCIA.MSG_ATIVAR_NOTIFICACAO_DETALHAMENTO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy(item);
				obj.situacao = 'ATIVA';
				$scope.resourceAssociation
					.update({ id: obj.id }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.notification.success('', msgNotification);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			$scope.cancel = function(fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(event, title, msg, $scope.confirm, $scope.cancel);
		});
	};

	$scope.inactivateDetalhamento = function(event, item) {
		$translate([
			'DIALOG_COMPETENCIA.TITULO_INATIVAR_DETALHAMENTO',
			'DIALOG_COMPETENCIA.BOTAO_CONFIRMAR',
			'DIALOG_COMPETENCIA.BOTAO_CANCELAR',
			'DIALOG_COMPETENCIA.MSG_INATIVAR_DETALHAMENTO',
			'DIALOG_COMPETENCIA.TITULO_INATIVAR_NOTIFICACAO_DETALHAMENTO',
			'DIALOG_COMPETENCIA.MSG_INATIVAR_NOTIFICACAO_DETALHAMENTO',
		]).then(function(translations) {
			const title = translations['DIALOG_COMPETENCIA.TITULO_INATIVAR_DETALHAMENTO'];
			const msg = translations['DIALOG_COMPETENCIA.MSG_INATIVAR_DETALHAMENTO'] + item.descricao + '?';
			const msgNotification = translations['DIALOG_COMPETENCIA.MSG_INATIVAR_NOTIFICACAO_DETALHAMENTO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy(item);
				obj.situacao = 'INATIVA';
				$scope.resourceAssociation
					.update({ id: obj.id }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.notification.success('', msgNotification);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			$scope.cancel = function(fn_Hide) {
				fn_Hide();
			};

			DialogConfirm.show(event, title, msg, $scope.confirm, $scope.cancel);
		});
	};

	$scope.saveAssociation = function(object, fn_Hide, msg) {
		object.competencia = $scope.object;
		if (object.id == null && angular.isDefined($scope.listAssociations)) {
			object.ordem = $scope.listAssociations.length + 1;
		}

		if (object.id == null) {
			$scope.resourceAssociation
				.save(object)
				.$promise.then(function() {
					$scope.loadObject();
					$scope.notification.success('', msg);
					fn_Hide();
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		if (object.id != null) {
			$scope.resourceAssociation
				.update({ id: object.id }, object)
				.$promise.then(function() {
					$scope.loadObject();
					$scope.notification.success('', msg);
					fn_Hide();
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}
	};

	$scope.cancelAssociation = function(fn_Hide) {
		fn_Hide();
	};

	$scope.displayEditAssociation = function(ev, index, item) {
		var filtro = {};
		filtro = item;


		CompetenciaDetalhamentoCalibrahint.hintspordetalhamentos(null, filtro)
			.$promise.then(function(dataHints) {
				var useFullScreen = $mdMedia('xs');
				$mdDialog.show({
					controller: DialogAssociationCtrl,
					locals: {
						title: $scope.titleAssociation,
						object: item,
						msg: $scope.titleAssociation + ' atualizada com sucesso!',
						acao: 'Edição',
						listHints: dataHints,
						fnSave: $scope.saveAssociation,
						fnCancel: $scope.cancelAssociation,
						resourceAssociation: $scope.resourceAssociation,
					},
					templateUrl: $scope.urlTemplateDialog,
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: true,
					fullscreen: useFullScreen,
				});
				$scope.$watch(
					function() {
						return $mdMedia('xs');
					},
					function(wantsFullScreen) {
						$scope.customFullscreen = wantsFullScreen === true;
					}
				);

			})
			.catch(function() {
			});
	};

	$scope.displayNewAssociation = function(ev) {

		var filtro = {};
		filtro.situacao = 'ATIVA';

		CompetenciaDetalhamento.get()
			.$promise.then(function(data) {
				var competenciaDetalhamento = data;
				competenciaDetalhamento.competencia = $scope.object;
				competenciaDetalhamento.competenciaDetalhamentosCalibrahint = [];

				if (ValidationService.verificarPreenchimento($scope.listAssociations)) {
					competenciaDetalhamento.ordem = $scope.listAssociations.length + 1;
				} else {
					competenciaDetalhamento.ordem = 1;
				}

				var useFullScreen = $mdMedia('xs');
				$mdDialog.show({
					controller: DialogAssociationCtrl,
					locals: {
						title: $scope.titleAssociation,
						object: competenciaDetalhamento,
						msg: $scope.titleAssociation + ' salvo com sucesso!',
						acao: 'Novo',
						listHints: [],
						fnSave: $scope.saveAssociation,
						fnCancel: $scope.cancelAssociation,
						resourceAssociation: $scope.resourceAssociation,
					},
					templateUrl: $scope.urlTemplateDialog,
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: true,
					fullscreen: useFullScreen,
				});
				$scope.$watch(
					function() {
						return $mdMedia('xs');
					},
					function(wantsFullScreen) {
						$scope.customFullscreen = wantsFullScreen === true;
					}
				);
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

	function DialogAssociationCtrl($scope, $mdDialog, title, object, msg, acao, listHints, fnSave, fnCancel, resourceAssociation, Graduacao) {
		$scope.object = null;
		$scope.listHints = listHints;

		if (object.id == null) {
			$scope.object = {};

			resourceAssociation
				.get()
				.$promise.then(function(data) {
					$scope.object = data;
					$scope.object.competenciaDetalhamentosCalibrahint = [];
					findModelosGraduacoes();

				})
				.catch(function(response) {
					console.error(response);
				});
		} else {
			resourceAssociation
				.get({ id: object.id })
				.$promise.then(function(data) {
					$scope.object = data;
					$scope.object.modeloGraduacao = angular.copy($scope.object.modeloGraduacao);
					$scope.idModeloAntigo = $scope.object.modeloGraduacao.id;
					findModelosGraduacoes();
					loadHints();

				})
				.catch(function() {
					//FIXME notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		$scope.title = title;
		$scope.msg = msg;
		$scope.acao = acao;

		function createCompetenciaDetalhamentoHint(graduacao, competenciaDetalhamento) {
			var novoHint = {};
			var novaCompetenciaDetalhamento = angular.copy(competenciaDetalhamento);
			delete novaCompetenciaDetalhamento.competenciaDetalhamentosCalibrahint;

			novoHint.competenciaDetalhamento = novaCompetenciaDetalhamento;
			novoHint.graduacao = graduacao;
			novoHint.hint = graduacao.hint;
			return novoHint;
		}

		$scope.changeModeloGraduacao = function(idModelo) {
			if (angular.isUndefined($scope.idModeloAntigo) || idModelo != $scope.idModeloAntigo) {
				createHintByGraduacao(idModelo);
			} else {
				loadHints();
			}
		};

		function findModelosGraduacoes() {
			var filtro = {};
			filtro.situacao = 'ATIVA';

			ModeloGraduacao.buscar(null, filtro)
				.$promise.then(function(dataModelos) {
					$scope.listModeloGraduacao = [];
					$scope.listModeloGraduacao = dataModelos;
				})
				.catch(function() {
				});
		}

		function createHintByGraduacao(idModelo) {
			var modelo = {};
			modelo.id = idModelo;

			Graduacao.graduacoesAtivasPorModelo(null, { modeloGraduacao: modelo })
				.$promise.then(function(graduacoes) {
					$scope.object.competenciaDetalhamentosCalibrahint = [];

					angular.forEach(graduacoes, function(graduacao) {
						if (graduacao.situacao == 'ATIVA') {
							var hint = createCompetenciaDetalhamentoHint(graduacao, $scope.object);
							$scope.object.modeloGraduacao = modelo;
							$scope.object.competenciaDetalhamentosCalibrahint.push(hint);
						}
					});
				})
				.catch(function() {
				});
		}

		function loadHints() {
			if (angular.isDefined($scope.listHints)) {
				$scope.object.competenciaDetalhamentosCalibrahint = [];
				angular.forEach($scope.listHints, function(hint) {
					if (angular.isUndefined(hint.comentarioObrigatorio)) hint.comentarioObrigatorio = false;
					$scope.object.competenciaDetalhamentosCalibrahint.push(hint);
				});
			}
		}

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			fnCancel($scope.hide);
		};

		$scope.save = function() {
			fnSave($scope.object, $scope.hide, $scope.msg, $scope.acao);
		};
	}

	$scope.moveUp = function(item, index) {
		if (index > 0 && index < $scope.listAssociations.length) {
			var position = index;
			var listChange = [];
			var promises = [];
			var above = {};

			above = $scope.listAssociations[position - 1];

			index++;
			above.ordem = index;
			item.ordem = index - 1;

			above.competencia = $scope.object;
			item.competencia = $scope.object;

			listChange.push(item);
			listChange.push(above);

			angular.forEach(listChange, function(value) {
				promises.push($scope.resourceAssociation.update({ id: value.id }, value).$promise);
			});

			$q.all(promises)
				.then(function() {
					$scope.loadObject();
				})
				.catch(function(response) {
					console.error(response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}
	};

	$scope.moveDown = function(item, index) {
		if (index >= 0 && index < $scope.listAssociations.length - 1) {
			var position = index;
			var listChange = [];
			var promises = [];
			var below = {};

			below = $scope.listAssociations[position + 1];

			index++;
			below.ordem = index;
			item.ordem = index + 1;

			below.competencia = $scope.object;
			item.competencia = $scope.object;

			listChange.push(item);
			listChange.push(below);

			angular.forEach(listChange, function(value) {
				promises.push($scope.resourceAssociation.update({ id: value.id }, value).$promise);
			});

			$q.all(promises)
				.then(function() {
					$scope.loadObject();
				})
				.catch(function(response) {
					console.error(response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}
	};

	if ($scope.screenMode === 'NEW') {
		listTiposCompetencias();
		modelosGraduacaoList();

		$scope.createObject();
	} else {
		if ($scope.screenMode === 'EDIT') {
			listTiposCompetencias();
			modelosGraduacaoList();
		}

		$scope.loadObject();
	}

	function buscarListaAvaliacao(object) {
		var objCompetencia = object;

		if ($scope.screenMode === 'VIEW') {

			Competencia.avaliacaoPorCompetencia({ id: objCompetencia.id })
				.$promise.then(function(data) {
					$scope.listaAvaliacao = data;
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response);
				});
		}
	}

	$scope.displayViewAvaliacao = function(avaliacao) {
		$state.go('avaliacao_view', { campanhaId: avaliacao.campanha.id, objectId: avaliacao.id });
	};

	$scope.displayTipoDeCompetencia = function(ev) {
		var noArvore;

		if (angular.isDefined($scope.object.tipo)) {
			noArvore = {};
			noArvore.id = $scope.object.tipo.id;
			noArvore.nome = $scope.object.tipo.nome;
			noArvore.descricao = $scope.object.tipo.descricao;
			noArvore.situacao = $scope.object.tipo.situacao;
			noArvore.selected = true;
		} else {
			noArvore = undefined;
		}

		var useFullScreen = $mdMedia('xs');
		$mdDialog.show({
			controller: DialogArvoreCtrl,
			locals: {
				title: 'Tipo de competência',
				object: noArvore,
				fnSave: $scope.addTipoDeCompetencia,
				TipoCompetencia: TipoCompetencia,
			},
			templateUrl: 'partials/dialogs/competencia/dialog_tipo_de_competencia.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen,
		});
		$scope.$watch(
			function() {
				return $mdMedia('xs');
			},
			function(wantsFullScreen) {
				$scope.customFullscreen = wantsFullScreen === true;
			}
		);
	};

	$scope.addTipoDeCompetencia = function(selectedNode, lastSelected, fn_Hide) {
		var tipoCompetencia = {};
		if (selectedNode) {
			tipoCompetencia.id = selectedNode.id;
			tipoCompetencia.nome = selectedNode.nome;
			tipoCompetencia.descricao = selectedNode.descricao;

			$scope.object.tipo = tipoCompetencia;
		}

		fn_Hide();
	};

	function DialogArvoreCtrl($scope, $mdDialog, title, object, fnSave, TipoCompetencia) {
		$scope.title = title;
		$scope.habilitar = false;
		$scope.lastSelected = undefined;
		$scope.dataForTheTree = [];

		$scope.treeOptions = {
			nodeChildren: 'children',
			dirSelectable: true,
			injectClasses: {
				ul: 'a1',
				li: 'a2',
				liSelected: 'a7',
				iExpanded: 'a3',
				iCollapsed: 'a4',
				iLeaf: 'a5',
				label: 'a6',
				labelSelected: 'a8',
			},
		};

		$scope.expandedNodes = [];

		function expandedNodes(lista) {
			angular.forEach(lista, function(value) {
				if (angular.isDefined(value.children)) {
					$scope.expandedNodes.push(value);
				}

				expandedNodes(value.children);
			});
			return;
		}

		$scope.findArvore = function() {
			TipoCompetencia.obterArvore()
				.$promise.then(function(data) {
					$scope.dataForTheTree = data;
					expandedNodes($scope.dataForTheTree);
					selecionarNo($scope.dataForTheTree, object);

					//esse if só é executado no screenMode edit
					if (angular.isDefined(object)) {
						$scope.lastSelected = angular.copy(object);
						$scope.node = object;
						$scope.habilitarBotaoSave($scope.node);
					}
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		};

		$scope.findArvore();

		function selecionarNo(lista, object) {
			angular.forEach(lista, function(value) {
				if (value.situacao == 'INATIVA') {
					value.disabled = true;
				}

				//				else if(angular.isUndefined(value.children) || value.children.length == 0){
				//					value.disabled = false;
				//				}
				//				else{
				//					value.disabled = true;
				//				}
				//
				if (angular.isDefined(object) && angular.isDefined(object.id) && object.id == value.id) {
					value.selected = object.selected;
					return;
				} else {
					selecionarNo(value.children, object);
				}
			});
		}

		$scope.habilitarBotaoSave = function(obj) {
			if (angular.isDefined(obj) && obj.id != null) {
				if (angular.isDefined(obj.selected)) {
					$scope.habilitar = obj.selected;
				} else {
					$scope.habilitar = true;
				}
			}
		};

		$scope.showSelected = function(sel) {
			if (angular.isUndefined($scope.lastSelected) && angular.isDefined(sel.selected)) {
				$scope.lastSelected = sel;
				$scope.habilitarBotaoSave(sel);
			} else if (
				angular.isDefined($scope.lastSelected) &&
				$scope.lastSelected.id != sel.id &&
				angular.isDefined($scope.lastSelected.selected) &&
				angular.isDefined(sel.selected)
			) {
				$scope.lastSelected.selected = false;
				selecionarNo($scope.dataForTheTree, $scope.lastSelected);
				$scope.lastSelected = sel;
				$scope.habilitarBotaoSave(sel);
			}

			if (angular.isDefined(sel.selected)) {
				$scope.node = sel;
				$scope.habilitarBotaoSave(sel);
			}
		};

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			$scope.hide();
		};

		$scope.confirm = function() {
			fnSave($scope.node, $scope.lastSelected, $scope.hide);
		};
	}

	$scope.treeOptions = {
		nodeChildren: 'children',
		dirSelectable: true,
		injectClasses: {
			ul: 'a1',
			li: 'a2',
			liSelected: 'a7',
			iExpanded: 'a3',
			iCollapsed: 'a4',
			iLeaf: 'a5',
			label: 'a6',
			labelSelected: 'a8',
		},
	};

	$scope.expandedNodes = [];

	function expandedNodes(lista) {
		$scope.expandedNodes = lista.filter(function(value) {
			if (angular.isDefined(value.children)) {
				return value;
			}

			expandedNodes(value.children);
		});

		return;
	}

	function carregarArvore() {
		Competencia.obterArvoreTipoCompetenciaPorCompetencia({ id: $stateParams.objectId })
			.$promise.then(function(data) {
				$scope.dataForTheTree = data;
				expandedNodes($scope.dataForTheTree);
			})
			.catch(function(response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	$scope.viewNodeCompetencia = function(id) {
		$state.go('competencia_view', { objectId: id });
	};

	$scope.detalhes = function(ev) {
		$mdDialog
			.show({
				controller: DialogController,
				locals: {
					object: $scope.object,
				},
				templateUrl: 'partials/dialogs/detalhes.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: true,
			})
			.then(
				function(answer) {
					$scope.status = 'You said the information was "' + answer + '".';
				},
				function() {
					$scope.status = 'You cancelled the dialog.';
				}
			);
	};

	function DialogController($scope, $mdDialog, object) {
		$scope.object = object;

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			$mdDialog.cancel();
		};

		$scope.answer = function(answer) {
			$mdDialog.hide(answer);
		};
	}
}
*/