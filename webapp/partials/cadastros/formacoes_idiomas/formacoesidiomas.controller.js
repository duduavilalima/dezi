import angular from 'angular';

export default angular
	.module('app.funcionario-formacao-idiomas.controllers')
	.controller('FormacoesIdiomasCtrl', formacoesIdiomasCtrl)
	.name;

function formacoesIdiomasCtrl(
	$scope,
	$state,
	$rootScope,
	Funcionario,
	DialogConfirm,
	Situacao,
	FuncionarioFormacao,
	$mdMedia,
	$mdDialog,
	Escolaridade,
	TipoCurso,
	CursoGraduacao,
	CursoEspecializacao,
	Idioma,
	FuncionarioIdioma
) {
	$scope.funcionarioFormacaoList = [];
	$scope.funcionarioFormacaoIdiomaList = [];
	$scope.object = null;

	$scope.modulo = $state.current.data.modulo;
	$scope.titulo = $state.current.data.titulo;
	$scope.subtitulo = $state.current.data.subtitulo;

	function init() {
		var promise = Funcionario.getSemFiltro({ id: $scope.objectId }).$promise;
		promise
			.then(function(data) {
				$scope.object = data;

				$scope.object.preencheFormacaoEIdiomas = false;

				var promiseUpdate = Funcionario.update({ id: $scope.object.id }, $scope.object).$promise;
				promiseUpdate
					.then(function(dataUpdate) {
						$scope.object = dataUpdate;
						loadFuncionarioFormacao($scope.object.id);
						loadFuncionarioIdioma($scope.object.id);
					})
					.catch(function(response) {
						$scope.$emit('notify-critical',response);
					});
			})
			.catch(function(response) {
				$scope.$emit('notify-critical',response);
			});
	}

	// LISTAR AS FORMAÇÕES DO FUNCIONÁRIO
	var loadFuncionarioFormacao = function(id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;
		FuncionarioFormacao.buscar(null, filtro)
			.$promise.then(function(data) {
				$scope.funcionarioFormacaoList = data;
			})
			.catch(function() {
			});
	};

	// LISTAR OS IDIOMAS DO FUNCIONÁRIO
	var loadFuncionarioIdioma = function(id) {
		var filtro = {};
		filtro.funcionario = {};
		filtro.funcionario.id = id;
		FuncionarioIdioma.buscar(null, filtro)
			.$promise.then(function(data) {
				$scope.funcionarioFormacaoIdiomaList = data;
			})
			.catch(function() {
			});
	};

	init();

	/* @ngInject */
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

		$scope.habilitarCampoSituacao = function() {
			//FIXME A regra que estava aqui não fazia sentido algum, verificar a regra real
			$scope.habilitarSituacao = true;
		};

		if ($scope.object == null) {
			$scope.object = {};
			funcionarioFormacaoResource
				.get()
				.$promise.then(function(data) {
					$scope.object = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		} else {
			funcionarioFormacaoResource
				.get({ id: $scope.object.id })
				.$promise.then(function(data) {
					$scope.object = data;
				})
				.catch(function(response) {
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
			escolaridadeResource
				.porContratanteUsuarioLogado()
				.$promise.then(function(data) {
					$scope.escolaridadeList = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		escolaridadeResourceFN();

		function tipoCursoResourceFN() {
			tipoCursoResource
				.porContratanteUsuarioLogado()
				.$promise.then(function(data) {
					$scope.tiposCursoList = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		tipoCursoResourceFN();

		function cursoGraduacaoResourceFN() {
			cursoGraduacaoResource
				.porContratanteUsuarioLogado()
				.$promise.then(function(data) {
					$scope.cursosGraduacaoList = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		cursoGraduacaoResourceFN();

		function cursoEspecializacaoResourceFN() {
			cursoEspecializacaoResource
				.porContratanteUsuarioLogado()
				.$promise.then(function(data) {
					$scope.cursosEspecializacaoList = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		cursoEspecializacaoResourceFN();

		function situacaoResourceFN() {
			situacaoResource
				.conclusoes()
				.$promise.then(function(data) {
					$scope.situacoesList = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		situacaoResourceFN();

		//Apagando os valores da combo de formação
		$scope.changeEscolaridade = function() {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.escolaridadeList, function(value) {
				if (value.id == $scope.object.escolaridade.id) {
					$scope.object.escolaridade = angular.copy(value);
				}
			});
			delete $scope.object.tipoCurso;
			delete $scope.object.cursoGraduacao;
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
			$scope.habilitarCampoSituacao();
		};

		$scope.changeTipoCurso = function() {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.tiposCursoList, function(value) {
				if (value.id == $scope.object.tipoCurso.id) {
					$scope.object.tipoCurso = angular.copy(value);
				}
			});
			delete $scope.object.cursoGraduacao;
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
			$scope.habilitarCampoSituacao();
		};

		$scope.changeGraduacao = function() {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.cursosGraduacaoList, function(value) {
				if (value.id == $scope.object.cursoGraduacao.id) {
					$scope.object.cursoGraduacao = angular.copy(value);
				}
			});
			delete $scope.object.cursoEspecializacao;
			delete $scope.object.outroCurso;
			$scope.habilitarCampoSituacao();
		};

		$scope.changeEspecializacao = function() {
			//FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
			angular.forEach($scope.cursosEspecializacaoList, function(value) {
				if (value.id == $scope.object.cursoEspecializacao.id) {
					$scope.object.cursoEspecializacao = angular.copy(value);
				}
			});
			delete $scope.object.outroCurso;
			$scope.habilitarCampoSituacao();
		};

		$scope.habilitarCampoSituacao();

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			fn_Cancel($scope.hide);
		};

		$scope.save = function() {
			fn_Save($scope.object, $scope.hide);
		};
	}

	$scope.acaoEditarFormacao = function(ev, item) {
		$scope.acaoNovoFormacao(ev, item);
	};

	$scope.acaoNovoFormacao = function(ev, item) {
		$scope.cancel = function(fn_Hide) {
			fn_Hide();
		};

		$scope.save = function(object, fn_Hide) {
			object.funcionario = $scope.object;
			$scope.action = true;
			if (object.id == null) {
				FuncionarioFormacao.save(object)
					.$promise.then(function() {
						$scope.notification.success('', 'Formação salva com sucesso!');
						loadFuncionarioFormacao($scope.objectId);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			} else {
				FuncionarioFormacao.update({ id: object.id }, object)
					.$promise.then(function() {
						loadFuncionarioFormacao($scope.objectId);
						$scope.notification.success('', 'Formação atualizada com sucesso!');
						fn_Hide();
					})
					.catch(function(response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			}
		};

		var useFullScreen = $mdMedia('sm') || $mdMedia('xs');

		$mdDialog.show({
			controller: dialogFuncionarioFormacao,
			locals: {
				title: 'Adicionar Formação',
				obj: item,
				fn_Save: $scope.save,
				fn_Cancel: $scope.cancel,
				notification: $scope.notification,
				escolaridadeResource: Escolaridade,
				tipoCursoResource: TipoCurso,
				cursoGraduacaoResource: CursoGraduacao,
				cursoEspecializacaoResource: CursoEspecializacao,
				situacaoResource: Situacao,
				funcionarioFormacaoResource: FuncionarioFormacao,
			},
			templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_formacao.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: useFullScreen,
		});
		$scope.$watch(
			function() {
				return $mdMedia('xs') || $mdMedia('sm');
			},
			function(wantsFullScreen) {
				$scope.customFullscreen = wantsFullScreen === true;
			}
		);
	};

	$scope.acaoRemoverFormacao = function(event, index, object) {
		var title = 'Remover formação';
		var msg = 'Deseja remover a escolaridade: ' + object.escolaridade.nome + '?';
		$scope.objectFormacao = object;

		$scope.confirm = function(object, fn_Hide) {
			FuncionarioFormacao.delete({ id: $scope.objectFormacao.id })
				.$promise.then(function() {
					loadFuncionarioFormacao($scope.objectId);
					$scope.notification.success('', 'Formação com escolaridade \'' + $scope.objectFormacao.escolaridade.nome + '\' removida com sucesso!');
					delete $scope.objectFormacao;
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
	};

	/* ngInject */
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
			funcionarioIdioma
				.get()
				.$promise.then(function(data) {
					$scope.object = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		} else {
			funcionarioIdioma
				.get({ id: obj.id })
				.$promise.then(function(data) {
					$scope.object = data;
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		$scope.situacaoNiveisEscritaList = [];
		$scope.situacaoNiveisFalaList = [];
		$scope.situacaoNiveisLeituraList = [];
		$scope.idiomaList = [];

		function situacaoNiveisList() {
			situacaoResource
				.niveis()
				.$promise.then(function(data) {
					$scope.situacaoNiveisEscritaList = angular.copy(data);
					$scope.situacaoNiveisFalaList = angular.copy(data);
					$scope.situacaoNiveisLeituraList = angular.copy(data);
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		var contemNaListaFormacaoIdioma = function(list1, id) {
			var aux = false;
			angular.forEach(list1, function(entity) {
				if (entity.idioma.id === id) {
					aux = true;
				}
			});
			return aux;
		};

		function idiomaList() {
			idiomaResource
				.idiomasByContratanteAndAtivas()
				.$promise.then(function(data) {
					angular.forEach(data, function(value) {
						if (!contemNaListaFormacaoIdioma(list, value.id)) {
							$scope.idiomaList.push(value);
						}
					});
					if (obj != null) {
						$scope.idiomaList.push(obj.idioma);
					}
				})
				.catch(function(response) {
					notification.criticalError(response.status, response.statusText, response.data);
				});
		}

		situacaoNiveisList();
		idiomaList();

		$scope.title = title;

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			fn_Cancel($scope.hide);
		};

		$scope.save = function() {
			fn_Save($scope.object, $scope.hide);
		};
	}

	$scope.acaoNovoIdioma = function(ev, item) {
		$scope.cancel = function(fn_Hide) {
			fn_Hide();
		};

		$scope.save = function(object, fn_Hide) {
			object.funcionario = $scope.object;
			$scope.action = true;
			if (object.id == null) {
				FuncionarioIdioma.save(object)
					.$promise.then(function() {
						$scope.notification.success('', 'Idioma salvo com sucesso!');
						loadFuncionarioIdioma($scope.objectId);
						fn_Hide();
					})
					.catch(function(response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			} else {
				FuncionarioIdioma.update({ id: object.id }, object)
					.$promise.then(function() {
						loadFuncionarioIdioma($scope.objectId);
						$scope.notification.success('', 'Idioma atualizado com sucesso!');
						fn_Hide();
					})
					.catch(function(response) {
						$scope.action = false;
						fn_Hide();
						$scope.$emit('notify-critical',response);
					});
			}
		};

		var useFullScreen = $mdMedia('sm') || $mdMedia('xs');

		$mdDialog.show({
			controller: dialogFuncionarioIdioma,
			locals: {
				title: 'Adicionar Idioma',
				obj: item,
				fn_Save: $scope.save,
				fn_Cancel: $scope.cancel,
				list: $scope.funcionarioFormacaoIdiomaList,
				notification: $scope.notification,
				funcionarioIdioma: FuncionarioIdioma,
				situacaoResource: Situacao,
				idiomaResource: Idioma,
			},
			templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_idioma.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: useFullScreen,
		});
		$scope.$watch(
			function() {
				return $mdMedia('xs') || $mdMedia('sm');
			},
			function(wantsFullScreen) {
				$scope.customFullscreen = wantsFullScreen === true;
			}
		);
	};

	//==== AÇÃO IDIOMA - BOTÃO DO MENU====//
	$scope.acaoEditarIdioma = function(ev, item) {
		$scope.acaoNovoIdioma(ev, item);
	};

	//==== AÇÃO REMOVER IDIOMA - BOTÃO DO MENU====//
	$scope.acaoRemoverIdioma = function(event, index, object) {
		var title = 'Remover idioma';
		var msg = 'Deseja remover o idioma: ' + object.idioma.nome + '?';
		$scope.objectIdioma = object;
		var idioma = object.idioma.nome;

		$scope.confirm = function(object, fn_Hide) {
			FuncionarioIdioma.delete({ id: $scope.objectIdioma.id })
				.$promise.then(function() {
					loadFuncionarioIdioma($scope.objectId);
					$scope.notification.success('', 'Idioma ' + idioma + ' removido com sucesso!');
					delete $scope.objectIdioma;
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
	};

	$scope.back = function() {
		$state.go('app.preenchimento.autoavaliacoes');
	};
}
