import angular from 'angular';
import moment from 'moment';

/* @ngInject */
function funcionarioPesquisaCtrl(
	$scope,
	$rootScope,
	$state,
	Funcionario,
	UserService
) {
	// INICIAR VARIÁVEIS
	function init() {
		$scope.listResult = null;
		$scope.resource = Funcionario;
		$scope.object = {};

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.goNew = 'funcionario_new';
		$scope.goView = 'funcionario_view';
		$scope.goEdit = 'funcionario_edit';
		$scope.goSearch = 'funcionarios';

		$scope.checkImportPermit('ROLE_ADMINISTRADOR');
	}

	$scope.displayBtnNovo = false;

	$scope.import = null;

	var usuarioLogado = undefined;
	$rootScope.$watch('user', function (user) {
		if (angular.isUndefined(usuarioLogado)) {
			usuarioLogado = user;
			if (angular.isDefined(user)) {
				if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
					$scope.displayBtnNovo = true;
				}
			}
		}
	});

	$scope.checkImportPermit = (requiredRoles) => {
		$scope.import = false;
		const roles = UserService.roles();
		roles.$promise
			.then((roles) => {
				$scope.import = roles.some(role => requiredRoles.indexOf(role.authority) >= 0);
			}).catch((error) => {
				//FIXME Tratamento de erro unificado
				console.error(error);
			});
	};

	init();

	$scope.query = {
		order: 'nome',
		limit: 10,
		page: 1
	};

	$scope.find = function () {
		var filtro = {};
		$scope.promise = $scope.resource.buscar(null, filtro).$promise;

		$scope.promise.then(function (data) {
			$scope.listResult = data;
		}).catch(function (response) {
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	};

	$scope.displayNew = function () {
		$state.go($scope.goNew);
	};

	$scope.displayView = function (item) {
		$state.go($scope.goView, { objectId: item.id });
	};

	$scope.displayEdit = function (item) {
		$state.go($scope.goEdit, { objectId: item.id });
	};

	$scope.back = function () {
		if ($rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			//FIXME $state.go(goSearch);
		}
	};

	$scope.find();
}

/* @ngInject */
function funcionarioCadastroCtrl(
	$scope,
	$rootScope,
	$state,
	$stateParams,
	$translate,
	$timeout,
	$mdMedia,
	$mdDialog,
	Funcionario,
	DialogConfirm,
	TipoResource,
	Situacao,
	Departamento,
	Cargo,
	Escolaridade,
	TipoCurso,
	CursoGraduacao,
	CursoEspecializacao,
	Idioma,
	FuncionarioFormacao,
	FuncionarioIdioma,
	FotoResource,
	Empresa,
	AvaliacaoParticipante,
	ConfiguracaoResource,
	Unidade
) {
	function init() {
		$scope.listResult = null;
		$scope.resource = Funcionario;
		$scope.object = {};
		$scope.configuracao = {};

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.tiposDeSexoList = [];
		$scope.tiposPerfisList = [];
		$scope.situacaoFuncionalList = [];
		$scope.departamentosList = [];
		$scope.cargoList = [];
		$scope.empresaLista = [];
		$scope.empresaPrincipalLista = [];

		$scope.funcionarioFormacaoList = null;
		$scope.funcionarioFormacaoIdiomaList = null;
		$scope.funcionarioEmpresaLista = null;
		$scope.funcionarioAvaliacaoLista = null;

		$scope.goNew = 'funcionario_new';
		$scope.goView = 'funcionario_view';
		$scope.goEdit = 'funcionario_edit';
		$scope.goSearch = 'funcionarios';
	}

	init();

	$scope.displayBtnNovo = false;
	var usuarioLogado = undefined;

	$rootScope.$watch('user', function (user) {
		if (angular.isUndefined(usuarioLogado)) {
			if (angular.isDefined(user)) {
				usuarioLogado = user;
				if (user.tipoPerfil == 'ADMINISTRADOR' || user.tipoPerfil == 'GESTOR_RH') {
					$scope.displayBtnNovo = true;
				}
			}
		}
	});

	function listTiposDeSexo() {
		TipoResource.tiposDeSexo()
			.$promise.then(function (data) {
				$scope.tiposDeSexoList = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function tiposPerfisList() {
		TipoResource.perfis()
			.$promise.then(function (data) {
				angular.forEach(data, function (value) {
					if (value !== 'ADMINISTRADOR') {
						$scope.tiposPerfisList.push(value);
					}
				});
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function situacaoFuncionalList() {
		Situacao.funcionais()
			.$promise.then(function (data) {
				$scope.situacaoFuncionalList = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function cargoList() {
		Cargo.cargosPorEmpresaUsuarioLogado()
			.$promise.then(function (data) {
				$scope.cargoList = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function carregarListaEmpresa() {
		Cargo.cargosPorEmpresaUsuarioLogado()
			.$promise.then(function (data) {
				$scope.empresaLista = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function departamentosList() {
		Departamento.departamentosDaEmpresaLogada()
			.$promise.then(function (data) {
				$scope.departamentosList = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	function carregarConfiguracao(id) {
		ConfiguracaoResource.configuracaoDoFuncionario({ id: id })
			.$promise.then(function (data) {
				$scope.configuracao = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	}

	var loadFuncionarioFoto = function (id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;

		filtro.tipo = {};
		filtro.tipo = 'FOTO_PERFIL';

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

	var loadFuncionarioFormacao = function (id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;
		FuncionarioFormacao.buscar(null, filtro)
			.$promise.then(function (data) {
				$scope.funcionarioFormacaoList = data;
			}).catch(function () {
			});
	};

	var loadFuncionarioIdioma = function (id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;
		FuncionarioIdioma.buscar(null, filtro)
			.$promise.then(function (data) {
				$scope.funcionarioFormacaoIdiomaList = data;
			}).catch(function () {
			});
	};

	var loadFuncionarioAvaliacoes = function (id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;
		AvaliacaoParticipante.listarAvaliacoesPorAvaliado(filtro.funcionario.id)
			.$promise.then(function (data) {
				$scope.funcionarioAvaliacaoLista = data;
			}).catch(function () {
			});
	};

	// CADASTRO //
	$scope.createObject = function () {
		$scope.displayBtnNovo = false;

		$scope.resource.get()
			.$promise.then(function (data) {
				$scope.object = data;
			}).catch(function (response) {
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

	$scope.mdswitch = {};

	$scope.onChangeSituacao = function (situacao) {
		$scope.mdswitch.situacao = situacao;
		if ($scope.object.situacao === 'ATIVA') {
			$scope.inactivate(null, $scope.object);
		} else {
			$scope.activate(null, $scope.object);
		}
	};

	$scope.loadObject = function () {
		$scope.displayBtnTipoAdm = true;
		$scope.objectId = $stateParams.objectId;

		if (!$scope.objectId) {
			$scope.$emit('crud-error', 'invalid-id');
			return;
		} else {
			$scope.resource.get({ id: $scope.objectId })
				.$promise.then(function (data) {
					loadFuncionarioFoto(data.id);
					if ($scope.screenMode === 'VIEW') {
						loadFuncionarioFormacao(data.id);
						loadFuncionarioIdioma(data.id);
						loadFuncionarioAvaliacoes(data.id);
						carregarConfiguracao(data.id);
					}

					//Mon Aug 29 2016 00:00:00 GMT-0300 (BRT)
					data.nascimento = moment(data.nascimento).toDate();
					data.admissao = moment(data.admissao).toDate();
					data.dataCargo = moment(data.dataCargo).toDate();
					if (data.desligamento != null) {
						data.desligamento = moment(data.desligamento).toDate();
					}

					$scope.object = data;

					if ($scope.object.tipoPerfil == 'ADMINISTRADOR') {
						$scope.displayBtnTipoAdm = false;
					}

					$scope.mdswitch.situacao = $scope.object.situacao === 'ATIVA';

					if ($scope.object.avaliador != null) {
						$scope.searchTextAvaliador = $scope.object.avaliador.nome;
					}

					$scope.selectedItemAvaliador = $scope.object.avaliador;

					if ($scope.object.revisor != null) {
						$scope.searchTextRevisor = $scope.object.revisor.nome;
					}

					$scope.selectedItemRevisor = $scope.object.revisor;
				}).catch(function (response) {
					if (usuarioLogado.tipoPerfil != 'ADMINISTRADOR' || usuarioLogado.tipoPerfil != 'GESTOR_RH') {
						if (usuarioLogado.id != $scope.objectId) {
							$state.go('funcionario_view', { objectId: usuarioLogado.id });
						} else {
							$scope.$emit('notify-critical', 'load', response);
						}
					} else {
						$scope.$emit('notify-critical', 'load', response);
					}
				});
		}
	};

	$scope.searchTextAvaliador = null;
	$scope.searchTextRevisor = null;
	$scope.selectedItemAvaliador = null;
	$scope.selectedItemRevisor = null;
	$scope.resultsAvaliador = [];
	$scope.resultsRevisor = [];

	$scope.querySearchAvaliador = function (query) {
		var obj = {};
		obj.nome = query;
		obj.situacao = 'ATIVA';
		obj.ordenacao = 'nome';
		obj.funcionarioTO = {};

		var avaliadores = [];

		var resultado = Funcionario.buscar(null, obj).$promise;

		return resultado.then(function (data) {
			angular.forEach(data, function (avaliador) {
				//nao pode ser a pessoa selecionada e nem o funcionario que está sendo editado
				if (avaliador.nome.trim() !== $scope.object.nome) {
					avaliadores.push(avaliador);
				}
			});
			return avaliadores;
		}).catch(function (response) {
			$scope.$emit('notify-critical', 'search', response);
			avaliadores = [];
			return avaliadores;
		});
	};

	$scope.querySearchRevisor = function (query) {
		var obj = {};
		obj.nome = query;
		obj.situacao = 'ATIVA';
		obj.ordenacao = 'nome';
		obj.funcionarioTO = {};

		var revisores = [];
		var resultado = Funcionario.buscar(null, obj).$promise;

		return resultado.then(function (data) {
			angular.forEach(data, function (revisor) {
				//nao pode ser a pessoa selecionada e nem o funcionario que está sendo editado
				if (revisor.nome.trim() !== $scope.object.nome) {
					revisores.push(revisor);
				}
			});
			return revisores;
		}).catch(function (response) {
			$scope.$emit('notify-critical', 'search', response);
			revisores = [];
			return revisores;
		});
	};

	//setando os valores selecionados no autocomplete em Avaliador
	$scope.selectedItemAvaliadorChange = function (item) {
		if (angular.isDefined(item)) {
			if (angular.isUndefined($scope.object.nome)) {
				$scope.object = {};
			}

			$scope.object.avaliador = item;
			$scope.selectedItemAvaliador = $scope.object.avaliador;
		} else {
			document.querySelectorAll('input[type=search]')[0].value = '';
			$scope.object.avaliador = {};
			delete $scope.object.avaliador;
			delete $scope.selectedItemAvaliador;
		}
	};

	//setando os valores selecionados no autocomplete em Revisor
	$scope.selectedItemRevisorChange = function (item) {
		if (angular.isDefined(item)) {
			if (angular.isUndefined($scope.object.nome)) {
				$scope.object = {};
			}

			$scope.object.revisor = item;
			$scope.selectedItemRevisor = $scope.object.revisor;
		} else {
			document.querySelectorAll('input[type=search]')[1].value = '';
			$scope.object.revisor = {};
			delete $scope.object.revisor;
			delete $scope.selectedItemRevisor;
		}
	};

	$scope.displayNew = function () {
		$state.go($scope.goNew);
	};

	$scope.displayView = function (item) {
		$state.go($scope.goView, { objectId: item.id });
	};

	$scope.displayEdit = function (item) {
		$state.go($scope.goEdit, { objectId: item.id });
	};

	$scope.back = function () {
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

	$scope.save = function () {
		$scope.action = true;

		if (angular.isDefined($scope.object.unidade) && $scope.object.unidade.id == null) {
			$scope.object.unidade = null;
		}

		$scope.object.nascimento = moment($scope.object.nascimento).toDate();
		$scope.object.admissao = moment($scope.object.admissao).toDate();
		if ($scope.object.desligamento != null) {
			$scope.object.desligamento = moment($scope.object.desligamento).toDate();
		}

		$scope.object.dataCargo = moment($scope.object.dataCargo).toDate();

		if ($scope.screenMode === 'NEW') {
			$scope.object.$save()
				.then(function (data) {
					$scope.notification.success('', 'Colaborador salvo com sucesso!');
					$state.go($scope.goView, { objectId: data.id });
				}).catch(function (response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		} else if ($scope.screenMode === 'EDIT') {
			$scope.object.$update({ id: $scope.objectId })
				.then(function (data) {
					$scope.notification.success('', 'Colaborador atualizado com sucesso!');
					$state.go($scope.goView, { objectId: data.id });
				}).catch(function (response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		}
	};

	$scope.activate = function (event, item) {
		$translate([
			'DIALOG_FUNCIONARIO.TITULO_ATIVAR',
			'DIALOG_FUNCIONARIO.BOTAO_CONFIRMAR',
			'DIALOG_FUNCIONARIO.BOTAO_CANCELAR',
			'DIALOG_FUNCIONARIO.MSG_ATIVAR',
			'DIALOG_FUNCIONARIO.TITULO_NOTIFICACAO',
			'DIALOG_FUNCIONARIO.MSG_NOTIFICACAO_ATIVADO'
		]).then(function (translations) {
			const title = translations['DIALOG_FUNCIONARIO.TITULO_ATIVAR'];
			const msg = translations['DIALOG_FUNCIONARIO.MSG_ATIVAR'] + $scope.titulo + ': ' + item.nome + '?';
			const msgNotification = translations['DIALOG_FUNCIONARIO.MSG_NOTIFICACAO_ATIVADO'];

			$scope.confirm = function (object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'ATIVA';
				$scope.resource.update({ id: $scope.objectId }, obj)
					.$promise.then(function (data) {
						$scope.loadObject();
						$scope.desligamento = null;
						$scope.situacao = true;
						$scope.notification.success('', data.nome + msgNotification);
						fn_Hide();
					}).catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
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
			'DIALOG_FUNCIONARIO.TITULO_INATIVAR',
			'DIALOG_FUNCIONARIO.BOTAO_CONFIRMAR',
			'DIALOG_FUNCIONARIO.BOTAO_CANCELAR',
			'DIALOG_FUNCIONARIO.MSG_INATIVAR',
			'DIALOG_FUNCIONARIO.TITULO_NOTIFICACAO_INATIVADO',
			'DIALOG_FUNCIONARIO.MSG_NOTIFICACAO_INATIVADO'
		]).then(function (translations) {
			const title = translations['DIALOG_FUNCIONARIO.TITULO_INATIVAR'];
			const msg = translations['DIALOG_FUNCIONARIO.MSG_INATIVAR'] + ' colaborador' + ': ' + item.nome + '?';
			const msgNotification = translations['DIALOG_FUNCIONARIO.MSG_NOTIFICACAO_INATIVADO'];

			$scope.confirm = function (object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'INATIVA';
				$scope.resource.update({ id: $scope.objectId }, obj)
					.$promise.then(function (data) {
						$scope.loadObject();
						$scope.situacao = false;
						$scope.notification.success('', data.nome + msgNotification);
						fn_Hide();
					}).catch(function (response) {
						$scope.notification.criticalError(response.status, response.statusText, response.data);
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

	$scope.acaoEditarIdioma = function (ev, item) {
		$scope.acaoNovoIdioma(ev, item);
	};

	function dialogFuncionarioIdioma(
		$scope,
		$mdDialog,
		title,
		fn_Save,
		fn_Cancel,
		obj,
		list,
		notification,
		funcionarioIdioma,
		situacaoResource,
		idiomaResource
	) {
		if (obj == null) {
			$scope.object = {};
			funcionarioIdioma.get()
				.$promise.then(function (data) {
					$scope.object = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		} else {
			funcionarioIdioma.get({ id: obj.id })
				.$promise.then(function (data) {
					$scope.object = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		$scope.situacaoNiveisEscritaList = [];
		$scope.situacaoNiveisFalaList = [];
		$scope.situacaoNiveisLeituraList = [];
		$scope.idiomaList = [];

		function situacaoNiveisList() {
			situacaoResource.niveis()
				.$promise.then(function (data) {
					$scope.situacaoNiveisEscritaList = angular.copy(data);
					$scope.situacaoNiveisFalaList = angular.copy(data);
					$scope.situacaoNiveisLeituraList = angular.copy(data);
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		var contemNaListaFormacaoIdioma = function (list1, id) {
			var aux = false; //FIXME WTF???
			angular.forEach(list1, function (entity) {
				if (entity.idioma.id === id) {
					aux = true;
				}
			}); //FIXME Trocar forEach() por some()
			return aux;
		};

		function idiomaList() {
			idiomaResource.idiomasByContratanteAndAtivas()
				.$promise.then(function (data) {
					angular.forEach(data, function (value) {
						if (!contemNaListaFormacaoIdioma(list, value.id)) {
							$scope.idiomaList.push(value);
						}
					});
					if (obj != null) {
						$scope.idiomaList.push(obj.idioma);
					}
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response);
				});
		}

		situacaoNiveisList();
		idiomaList();

		$scope.title = title;

		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			fn_Cancel($scope.hide);
		};

		$scope.save = function () {
			fn_Save($scope.object, $scope.hide);
		};
	}

	$scope.acaoNovoIdioma = function (ev, item) {
		$scope.cancel = function (fn_Hide) {
			fn_Hide();
		};

		$scope.save = function (object, fn_Hide) {
			object.funcionario = $scope.object;
			$scope.action = true;
			if (object.id == null) {
				FuncionarioIdioma.save(object)
					.$promise.then(function () {
						$scope.notification.success('', 'Idioma salvo com sucesso!');
						$scope.loadObject();
						fn_Hide();
					}).catch(function (response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			} else {
				FuncionarioIdioma.update({ id: object.id }, object)
					.$promise.then(function () {
						$scope.loadObject();
						$scope.notification.success('', 'Idioma atualizado com sucesso!');
						fn_Hide();
					}).catch(function (response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			}
		};

		var useFullScreen = ($mdMedia('xs'));

		var tituloDialog = 'Adicionar Idioma';

		$translate(['FUNCIONARIO.BTNADICIONARIDIOMA']).then(function (translations) {
			tituloDialog = translations['FUNCIONARIO.BTNADICIONARIDIOMA'];

			$mdDialog.show({
				controller: dialogFuncionarioIdioma,
				locals: {
					title: tituloDialog,
					obj: item,
					fn_Save: $scope.save,
					fn_Cancel: $scope.cancel,
					list: $scope.funcionarioFormacaoIdiomaList,
					notification: $scope.notification,
					funcionarioIdioma: FuncionarioIdioma,
					situacaoResource: Situacao,
					idiomaResource: Idioma
				},
				templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_idioma.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: useFullScreen
			});
		});

		$scope.$watch(function () {
			return $mdMedia('xs');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	//==== AÇÃO REMOVER IDIOMA - BOTÃO DO MENU====//
	$scope.acaoRemoverIdioma = function (event, index, object) {
		var title = 'Remover idioma';
		var msg = 'Deseja remover o idioma: ' + object.idioma.nome + '?';
		$scope.objectIdioma = object;

		$scope.confirm = function (object, fn_Hide) {
			FuncionarioIdioma.delete({ id: $scope.objectIdioma.id })
				.$promise.then(function () {
					$scope.loadObject();
					$scope.notification.success('', 'Idioma removido com sucesso!');
					delete $scope.objectIdioma;
					fn_Hide();
				}).catch(function (response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
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
	};

	function dialogFuncionarioFormacao(
		$scope,
		$mdDialog,
		title,
		fn_Save,
		fn_Cancel,
		obj,
		notification,
		escolaridadeResource,
		tipoCursoResource,
		cursoGraduacaoResource,
		cursoEspecializacaoResource,
		situacaoResource,
		funcionarioFormacaoResource
	) {
		$scope.object = obj;

		if ($scope.object == null) {
			$scope.object = {};
			funcionarioFormacaoResource.get()
				.$promise.then(function (data) {
					$scope.object = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		} else {
			funcionarioFormacaoResource.get({ id: $scope.object.id })
				.$promise.then(function (data) {
					$scope.object = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		$scope.title = title;
		$scope.escolaridadeList = [];
		$scope.tiposCursoList = [];
		$scope.cursosGraduacaoList = [];
		$scope.cursosEspecializacaoList = [];
		$scope.situacoesList = [];

		function escolaridadeResourceFN() {
			escolaridadeResource.porContratanteUsuarioLogado()
				.$promise.then(function (data) {
					$scope.escolaridadeList = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		escolaridadeResourceFN();

		function tipoCursoResourceFN() {
			tipoCursoResource.porContratanteUsuarioLogado()
				.$promise.then(function (data) {
					$scope.tiposCursoList = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		tipoCursoResourceFN();

		function cursoGraduacaoResourceFN() {
			cursoGraduacaoResource.porContratanteUsuarioLogado()
				.$promise.then(function (data) {
					$scope.cursosGraduacaoList = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		cursoGraduacaoResourceFN();

		function cursoEspecializacaoResourceFN() {
			cursoEspecializacaoResource.porContratanteUsuarioLogado()
				.$promise.then(function (data) {
					$scope.cursosEspecializacaoList = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		cursoEspecializacaoResourceFN();

		function situacaoResourceFN() {
			situacaoResource.conclusoes()
				.$promise.then(function (data) {
					$scope.situacoesList = data;
				}).catch(function (response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		situacaoResourceFN();

		//Apagando os valores da combo de formação
		$scope.changeEscolaridade = function () {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.escolaridadeList, function (value) {
				if (value.id == $scope.object.escolaridade.id) {
					$scope.object.escolaridade = angular.copy(value);
				}
			});
			delete $scope.object.tipoCurso;
			delete $scope.object.cursoGraduacao;
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
		};

		$scope.changeTipoCurso = function () {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.tiposCursoList, function (value) {
				if (value.id == $scope.object.tipoCurso.id) {
					$scope.object.tipoCurso = angular.copy(value);
				}
			});
			delete $scope.object.cursoGraduacao;
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
		};

		$scope.changeGraduacao = function () {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.cursosGraduacaoList, function (value) {
				if (value.id == $scope.object.cursoGraduacao.id) {
					$scope.object.cursoGraduacao = angular.copy(value);
				}
			});
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
		};

		$scope.changeEspecializacao = function () {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.cursosEspecializacaoList, function (value) {
				if (value.id == $scope.object.cursoEspecializacao.id) {
					$scope.object.cursoEspecializacao = angular.copy(value);
				}
			});
			delete $scope.object.outroCurso;
		};

		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			fn_Cancel($scope.hide);
		};

		$scope.save = function () {
			fn_Save($scope.object, $scope.hide);
		};
	}

	$scope.acaoEditarFormacao = function (ev, item) {
		$scope.acaoNovoFormacao(ev, item);
	};

	$scope.acaoNovoFormacao = function (ev, item) {
		$scope.cancel = function (fn_Hide) {
			fn_Hide();
		};

		$scope.save = function (object, fn_Hide) {
			object.funcionario = $scope.object;
			$scope.action = true;
			if (object.id == null) {
				FuncionarioFormacao.save(object)
					.$promise.then(function () {
						$scope.notification.success('', 'Formação salva com sucesso!');
						$scope.loadObject();
						fn_Hide();
					}).catch(function (response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			} else {
				FuncionarioFormacao.update({ id: object.id }, object)
					.$promise.then(function () {
						$scope.loadObject();
						$scope.notification.success('', 'Formação atualizada com sucesso!');
						fn_Hide();
					}).catch(function (response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			}
		};

		var useFullScreen = ($mdMedia('xs'));

		var tituloDialog = 'Adicionar Formação';

		$translate(['FUNCIONARIO.BTNADICIONAR']).then(function (translations) {
			tituloDialog = translations['FUNCIONARIO.BTNADICIONAR'];

			$mdDialog.show({
				controller: dialogFuncionarioFormacao,
				locals: {
					title: tituloDialog,
					obj: item,
					fn_Save: $scope.save,
					fn_Cancel: $scope.cancel,
					notification: $scope.notification,
					escolaridadeResource: Escolaridade,
					tipoCursoResource: TipoCurso,
					cursoGraduacaoResource: CursoGraduacao,
					cursoEspecializacaoResource: CursoEspecializacao,
					situacaoResource: Situacao,
					funcionarioFormacaoResource: FuncionarioFormacao
				},
				templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_formacao.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: useFullScreen
			});
		});

		$scope.$watch(function () {
			return $mdMedia('xs');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	$scope.acaoRemoverFormacao = function (event, index, object) {
		var title = 'Remover formação';
		var msg = 'Deseja remover a escolaridade: ' + object.escolaridade.nome + '?';
		$scope.objectFormacao = object;

		$scope.confirm = function (object, fn_Hide) {
			FuncionarioFormacao.delete({ id: $scope.objectFormacao.id })
				.$promise.then(function () {
					$scope.loadObject();
					$scope.notification.success('', 'Formação removida com sucesso!');
					delete $scope.objectFormacao;
					fn_Hide();
				}).catch(function (response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
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
	};

	if ($scope.screenMode === 'NEW') {
		listTiposDeSexo();
		tiposPerfisList();
		situacaoFuncionalList();
		cargoList();
		carregarListaEmpresa();
		departamentosList();
		carregarUnidades();
		$scope.createObject();
	} else {
		if ($scope.screenMode === 'EDIT') {
			listTiposDeSexo();
			tiposPerfisList();
			situacaoFuncionalList();
			cargoList();
			carregarListaEmpresa();
			departamentosList();
			carregarUnidades();
		}

		$scope.loadObject();
	}

	$scope.acaoAlterarFoto = function (ev, index, item) {
		var useFullScreen = ($mdMedia('xs'));

		$mdDialog.show({
			controller: dialogAlterarFoto,
			locals: {
				object: item,
				msg: '',
				cropper: $scope.cropper,
				index: index,
				title: 'Alterar Foto',
				fn_confirm: $scope.confirmImg
			},
			templateUrl: 'partials/dialogs/funcionario/dialog_alterar_foto.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			escapeToClose: false,
			fullscreen: useFullScreen
		});
		$scope.$watch(function () {
			return $mdMedia('xs');
		}, function (wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	$scope.confirmImg = function (cropper, action, fn_Hide) {
		var foto = {};
		foto.tipo = {};
		foto.base64 = cropper.croppedImage;
		foto.funcionario = $scope.object;

		if (angular.isDefined(cropper.id) && cropper != null)
			foto.id = cropper.id;

		if (angular.isDefined(cropper.tipo) && cropper.tipo != null)
			foto.tipo = cropper.tipo;
		else
			foto.tipo = 'FOTO_PERFIL';

		if (angular.isUndefined($scope.cropper) || $scope.cropper == null) {
			FotoResource.save(foto)
				.$promise.then(function (data) {
					$timeout(function () {
						$scope.cropper = {};
						$scope.cropper.id = data.id;
						$scope.cropper.funcionario = data.funcionario;
						$scope.cropper.tipo = data.tipo;
						$scope.cropper.sourceImage = data.base64;
						$scope.cropper.croppedImage = data.base64;
						action = false;
						$scope.notification.success('', 'Foto do perfil atualizada com sucesso!');
						fn_Hide();
					}, 1500);
				}).catch(function (response) {
					$scope.$emit('notify-critical',response);
				});
		} else {
			FotoResource.update({ id: foto.id }, foto)
				.$promise.then(function (data) {
					$timeout(function () {
						$scope.cropper = {};
						$scope.cropper.id = data.id;
						$scope.cropper.funcionario = data.funcionario;
						$scope.cropper.tipo = data.tipo;
						$scope.cropper.sourceImage = data.base64;
						$scope.cropper.croppedImage = data.base64;
						action = false;
						$scope.notification.success('', 'Foto do perfil realizada com sucesso!');
						fn_Hide();
					}, 1500);
				}).catch(function (response) {
					$scope.$emit('notify-critical',response);
				});
		}
	};

	function dialogAlterarFoto($scope, $mdDialog, object, msg, cropper, title, fn_confirm) {
		$scope.object = object;
		$scope.msg = msg;
		$scope.title = title;
		$scope.action = false;

		if (angular.isUndefined(cropper) || cropper == null) {
			$scope.cropper = {};
			$scope.cropper.sourceImage = null;
			$scope.cropper.croppedImage = null;
		} else
			$scope.cropper = angular.copy(cropper);

		$scope.bounds = {};
		$scope.bounds.left = 0;
		$scope.bounds.right = 200;
		$scope.bounds.top = 200;
		$scope.bounds.bottom = 0;

		$scope.hide = function () {
			$mdDialog.hide();
		};

		$scope.cancel = function () {
			$mdDialog.cancel();
		};

		$scope.confirmar = function () {
			$scope.action = true;
			fn_confirm($scope.cropper, $scope.action, $scope.hide);
		};
	}

	$scope.searchTermCargo;
	$scope.searchTermDepartamento;
	$scope.clearSearchTerm = function () {
		$scope.searchTermCargo = '';
		$scope.searchTermDepartamento = '';
	};

	$scope.onSearchChange = function (event) {
		event.stopPropagation();
	};

	$scope.displayViewAvaliacao = function (avaliacao) {
		$state.go('avaliacao_view', { campanhaId: avaliacao.campanha.id, objectId: avaliacao.id });
	};

	$scope.periodoOnChange = function (situacaoFuncional) {
		if (situacaoFuncional == 'EM_ATIVIDADE') {
			$scope.object.dataFimRecesso = null;
			$scope.object.dataInicioRecesso = null;
		}

		if (situacaoFuncional == 'DESLIGADO') {
			$scope.object.dataFimRecesso = null;
			$scope.object.dataInicioRecesso = null;
		} else {
			$scope.object.desligamento = null;
		}
	};

	function carregarUnidades() {
		Unidade.ativas()
			.$promise.then(function (data) {
				$scope.unidadeList = [];
				//                 $scope.unidadeList.push({id: null, nome: ""});
				angular.forEach(data, function (item) {
					$scope.unidadeList.push(item);
				});
			}).catch(function (response) {
				$scope.$emit('notify-critical',response);
			});
	}

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
}

export {
	funcionarioPesquisaCtrl,
	funcionarioCadastroCtrl
};
