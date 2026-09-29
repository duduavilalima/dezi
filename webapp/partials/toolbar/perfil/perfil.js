import anonImage from 'img/avatars/anon.png';

//ROLE_GESTOR_AREA e ROLE_FUNCIONARIO: Redirecionado para AVALIACOES;
//ROLE_GESTOR_RH e ROLE_ADMINISTRADOR: Redirecionado para ACOMPANHAMENTOS;
const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
/* @ngInject */
export default function perfilCadastroCtrl(
	$state,
	loaders,
	foto,
	configuracao,
	formacoesList,
	avaliacoesList,
	idiomasList,
	userRoles,
	colaborador
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.anonImage = anonImage;

	//Serviços
	$ctrl.configuracao = configuracao;
	$ctrl.formacoesList = formacoesList;
	$ctrl.avaliacoesList = avaliacoesList;
	$ctrl.idiomasList = idiomasList;
	$ctrl.colaborador = colaborador;
	//TODO: Método por tipo, pois agora tem AVATAR e PERFIL
	$ctrl.foto = foto.find(foto => foto.tipo === 'FOTO_PERFIL');

	$ctrl.back = () => {
		if (userRoles.some(role => requiredRoles.indexOf(role.authority) >= 0)) {
			$state.go('main.dashboard');
		} else {
			$state.go('main.preenchimento.avaliacoes');
		}
	};

	//TODO: Refatorar as dialogs de formações e idiomas e avaliações.

	// function init() {
	// 	$scope.resource = Funcionario;
	// 	$scope.object = null;

	// 	$scope.modulo = $state.current.data.modulo;
	// 	$scope.titulo = $state.current.data.titulo;
	// 	$scope.subtitulo = $state.current.data.subtitulo;

	// 	$scope.funcionarioFormacaoList = null;
	// 	$scope.funcionarioFormacaoIdiomaList = null;
	// 	$scope.funcionarioAvaliacaoLista = null;

	// 	$scope.objectId = $scope.user.id;

	// 	$scope.loadObject();
	// }

	// $scope.user = null;

	// $scope.back = function () {
	// 	$state.go('dashboard');
	// };

	// // UserResource.get()
	// // 	.$promise.then(function (data) {
	// // 		$scope.user = angular.copy(data);
	// // 		init();
	// // 		loadFuncionarioFoto(data.id);
	// // 	}).catch(function (response) {
	// // 		$scope.notification.criticalError(response.status, response.statusText, response.data);
	// // 	});

	// var loadFuncionarioAvaliacoes = function (id) {
	// 	var filtro = {};
	// 	filtro.funcionario = {};
	// 	filtro.funcionario.id = id;
	// 	AvaliacaoParticipante.listarAvaliacoesPorAvaliado(filtro.funcionario.id)
	// 		.$promise.then(function (data) {
	// 			$scope.funcionarioAvaliacaoLista = data;
	// 		}).catch(function () {
	// 		});
	// };

	// function carregarConfiguracao(id) {
	// 	ConfiguracaoResource.configuracaoDoFuncionario({ id: id })
	// 		.$promise.then(function (data) {
	// 			$scope.configuracao = data;
	// 		}).catch(function (response) {
	// 			$scope.notification.criticalError(response.status, response.statusText, response.data);
	// 		});
	// }

	// // var loadFuncionarioFoto = function (id) {
	// // 	var filtro = {};
	// // 	filtro.funcionario = {};
	// // 	filtro.funcionario.id = id;

	// // 	filtro.tipo = {};
	// // 	filtro.tipo = 'FOTO_PERFIL';

	// // 	const imageChanged = (data) => {
	// // 		if (!data || data.length == 0) return;

	// // 		const imageData = data[0];

	// // 		$scope.cropper = {};
	// // 		$scope.cropper.id = imageData.id;
	// // 		$scope.cropper.funcionario = imageData.funcionario;
	// // 		$scope.cropper.tipo = imageData.tipo;
	// // 		$scope.cropper.sourceImage = imageData.base64;
	// // 		$scope.cropper.croppedImage = imageData.base64;
	// // 	};

	// // 	FotoResource.buscar(null, filtro).$promise.then((data) => imageChanged(data));
	// // };

	// var loadFuncionarioFormacao = function (id) {
	// 	var filtro = {};
	// 	filtro.funcionario = {};
	// 	filtro.funcionario.id = id;
	// 	$scope.funcionarioFormacaoList = FuncionarioFormacao.buscar(null, filtro);
	// };

	// var loadFuncionarioIdioma = function (id) {
	// 	var filtro = {};
	// 	filtro.funcionario = {};
	// 	filtro.funcionario.id = id;
	// 	$scope.funcionarioFormacaoIdiomaList = FuncionarioIdioma.buscar(null, filtro);
	// };

	// $scope.loadObject = function () {
	// 	if (!$scope.objectId) {
	// 		$scope.$emit('crud-error', 'invalid-id');
	// 		return;
	// 	} else {
	// 		Funcionario.getSemFiltro({ id: $scope.objectId })
	// 			.$promise.then(function (data) {
	// 				loadFuncionarioFormacao(data.id);
	// 				loadFuncionarioIdioma(data.id);
	// 				carregarConfiguracao(data.id);
	// 				loadFuncionarioAvaliacoes(data.id);

	// 				data.nascimento = new Date(data.nascimento);
	// 				data.admissao = new Date(data.admissao);
	// 				data.dataCargo = new Date(data.dataCargo);

	// 				$scope.object = data;
	// 			}).catch(function (response) {
	// 				$scope.$emit('notify-critical', 'load', response);
	// 			});
	// 	}
	// };

	// init();

	// $scope.acaoNovoIdioma = function (ev, item) {
	// 	$scope.cancel = function (fn_Hide) {
	// 		fn_Hide();
	// 	};

	// 	$scope.save = function (object, fn_Hide) {
	// 		object.funcionario = $scope.object;
	// 		$scope.action = true;
	// 		if (object.id == null) {
	// 			FuncionarioIdioma.save(object)
	// 				.$promise.then(function () {
	// 					$scope.notification.success('', 'Idioma salvo com sucesso!');
	// 					$scope.loadObject();
	// 					fn_Hide();
	// 				}).catch(function (response) {
	// 					$scope.action = false;
	// 					fn_Hide();
	// 					$scope.$emit('notify-critical',response);
	// 				});
	// 		} else {
	// 			FuncionarioIdioma.update({ id: object.id }, object)
	// 				.$promise.then(function () {
	// 					$scope.loadObject();
	// 					$scope.notification.success('', 'Idioma atualizado com sucesso!');
	// 					fn_Hide();
	// 				}).catch(function (response) {
	// 					$scope.action = false;
	// 					fn_Hide();
	// 					$scope.$emit('notify-critical',response);
	// 				});
	// 		}
	// 	};

	// 	var useFullScreen = ($mdMedia('xs'));

	// 	var tituloDialog = 'Adicionar Idioma';

	// 	$translate(['FUNCIONARIO.BTNADICIONARIDIOMA']).then(function (translations) {
	// 		tituloDialog = translations['FUNCIONARIO.BTNADICIONARIDIOMA'];

	// 		$mdDialog.show({
	// 			controller: dialogFuncionarioIdioma,
	// 			locals: {
	// 				title: tituloDialog,
	// 				obj: item,
	// 				fn_Save: $scope.save,
	// 				fn_Cancel: $scope.cancel,
	// 				list: $scope.funcionarioFormacaoIdiomaList,
	// 				notification: $scope.notification,
	// 				funcionarioIdioma: FuncionarioIdioma,
	// 				situacaoResource: Situacao,
	// 				idiomaResource: Idioma
	// 			},
	// 			templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_idioma.html',
	// 			parent: angular.element(document.body),
	// 			targetEvent: ev,
	// 			clickOutsideToClose: false,
	// 			fullscreen: useFullScreen
	// 		});
	// 	});

	// 	$scope.$watch(function () {
	// 		return $mdMedia('xs');
	// 	}, function (wantsFullScreen) {
	// 		$scope.customFullscreen = (wantsFullScreen === true);
	// 	});
	// };

	// /* @ngInject */
	// function dialogFuncionarioIdioma(
	// 	$scope,
	// 	$mdDialog,
	// 	title,
	// 	fn_Save,
	// 	fn_Cancel,
	// 	obj,
	// 	list,
	// 	notification,
	// 	funcionarioIdioma,
	// 	situacaoResource,
	// 	idiomaResource
	// ) {
	// 	if (obj == null) {
	// 		$scope.object = {};
	// 		funcionarioIdioma.get()
	// 			.$promise.then(function (data) {
	// 				$scope.object = data;
	// 			}).catch(function (response) {
	// 				notification.criticalError(response.status, response.statusText, response.data);
	// 			});
	// 	} else {
	// 		funcionarioIdioma.get({ id: obj.id })
	// 			.$promise.then(function (data) {
	// 				$scope.object = data;
	// 			}).catch(function (response) {
	// 				notification.criticalError(response.status, response.statusText, response.data);
	// 			});
	// 	}

	// 	$scope.situacaoNiveisEscritaList = [];
	// 	$scope.situacaoNiveisFalaList = [];
	// 	$scope.situacaoNiveisLeituraList = [];
	// 	$scope.idiomaList = [];

	// 	function situacaoNiveisList() {
	// 		situacaoResource.niveis()
	// 			.$promise.then(function (data) {
	// 				$scope.situacaoNiveisEscritaList = angular.copy(data);
	// 				$scope.situacaoNiveisFalaList = angular.copy(data);
	// 				$scope.situacaoNiveisLeituraList = angular.copy(data);
	// 			}).catch(function (response) {
	// 				notification.criticalError(response.status, response.statusText, response.data);
	// 			});
	// 	}

	// 	var contemNaListaFormacaoIdioma = function (list1, id) {
	// 		var aux = false;
	// 		angular.forEach(list1, function (entity) {
	// 			if (entity.idioma.id === id) {
	// 				aux = true;
	// 			}
	// 		});
	// 		return aux;
	// 	};

	// 	function idiomaList() {
	// 		idiomaResource.idiomasByContratanteAndAtivas()
	// 			.$promise.then(function (data) {
	// 				angular.forEach(data, function (value) {
	// 					if (!contemNaListaFormacaoIdioma(list, value.id)) {
	// 						$scope.idiomaList.push(value);
	// 					}
	// 				});
	// 				if (obj != null) {
	// 					$scope.idiomaList.push(obj.idioma);
	// 				}
	// 			}).catch(function (response) {
	// 				notification.criticalError(response.status, response.statusText, response);
	// 			});
	// 	}

	// 	situacaoNiveisList();
	// 	idiomaList();

	// 	$scope.title = title;

	// 	$scope.hide = function () {
	// 		$mdDialog.hide();
	// 	};

	// 	$scope.cancel = function () {
	// 		fn_Cancel($scope.hide);
	// 	};

	// 	$scope.save = function () {
	// 		fn_Save($scope.object, $scope.hide);
	// 	};
	// }

	// $scope.acaoNovoFormacao = function (ev, item) {
	// 	$scope.cancel = function (fn_Hide) {
	// 		fn_Hide();
	// 	};

	// 	$scope.save = function (object, fn_Hide) {
	// 		object.funcionario = $scope.object;
	// 		$scope.action = true;
	// 		if (object.id == null) {
	// 			FuncionarioFormacao.save(object)
	// 				.$promise.then(function () {
	// 					$scope.notification.success('', 'Formação salva com sucesso!');
	// 					$scope.loadObject();
	// 					fn_Hide();
	// 				}).catch(function (response) {
	// 					$scope.action = false;
	// 					fn_Hide();
	// 					$scope.$emit('notify-critical',response);
	// 				});
	// 		} else {
	// 			FuncionarioFormacao.update({ id: object.id }, object)
	// 				.$promise.then(function () {
	// 					$scope.loadObject();
	// 					$scope.notification.success('', 'Formação atualizada com sucesso!');
	// 					fn_Hide();
	// 				}).catch(function (response) {
	// 					$scope.action = false;
	// 					fn_Hide();
	// 					$scope.$emit('notify-critical',response);
	// 				});
	// 		}
	// 	};

	// 	var useFullScreen = ($mdMedia('xs'));

	// 	var tituloDialog = 'Adicionar Formação';

	// 	$translate(['FUNCIONARIO.BTNADICIONAR']).then(function (translations) {
	// 		tituloDialog = translations['FUNCIONARIO.BTNADICIONAR'];

	// 		$mdDialog.show({
	// 			controller: dialogFuncionarioFormacao,
	// 			locals: {
	// 				title: tituloDialog,
	// 				obj: item,
	// 				fn_Save: $scope.save,
	// 				fn_Cancel: $scope.cancel,
	// 				notification: $scope.notification,
	// 				escolaridadeResource: Escolaridade,
	// 				tipoCursoResource: TipoCurso,
	// 				cursoGraduacaoResource: CursoGraduacao,
	// 				cursoEspecializacaoResource: CursoEspecializacao,
	// 				situacaoResource: Situacao,
	// 				funcionarioFormacaoResource: FuncionarioFormacao
	// 			},
	// 			templateUrl: 'partials/dialogs/funcionario/dialog_funcionario_formacao.html',
	// 			parent: angular.element(document.body),
	// 			targetEvent: ev,
	// 			clickOutsideToClose: false,
	// 			fullscreen: useFullScreen
	// 		});
	// 	});

	// 	$scope.$watch(function () {
	// 		return $mdMedia('xs');
	// 	}, function (wantsFullScreen) {
	// 		$scope.customFullscreen = (wantsFullScreen === true);
	// 	});
	// };
}
