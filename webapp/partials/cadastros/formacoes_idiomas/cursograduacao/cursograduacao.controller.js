import angular from 'angular';

export default angular
	.module('app.cursograduacao.controllers')
	.controller('CursoGraduacaoCadastroCtrl', cursoGraduacaoCadastroCtrl)
	.controller('CursoGraduacaoListarCtrl', cursoGraduacaoListarCtrl).name;

function cursoGraduacaoListarCtrl($scope, $state, $stateParams, $rootScope, $timeout, CursoGraduacao) {
	// INICIAR VARIÁVEIS
	function init() {
		$scope.listResult = null;
		$scope.resource = CursoGraduacao;
		$scope.object = {};

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.goNew = 'tiposgraduacao_new';
		$scope.goView = 'tiposgraduacao_view';
		$scope.goEdit = 'tiposgraduacao_edit';
		$scope.goSearch = 'cursosgraduacao';
	}

	init();

	$scope.query = {
		order: 'nome',
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

function cursoGraduacaoCadastroCtrl($scope, $state, $stateParams, $rootScope, $timeout, CursoGraduacao, DialogConfirm, $translate, $mdDialog) {
	// INICIAR VARIÁVEIS
	function init() {
		$scope.listResult = null;
		$scope.resource = CursoGraduacao;
		$scope.object = null;

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.goNew = 'tiposgraduacao_new';
		$scope.goView = 'tiposgraduacao_view';
		$scope.goEdit = 'tiposgraduacao_edit';
		$scope.goSearch = 'cursosgraduacao';
	}

	init();

	$scope.createObject = function() {
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
					$scope.mdswitch.situacao = $scope.object.situacao === 'ATIVA';
				})
				.catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		}
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
					$scope.notification.success('', 'Curso de graduação salvo com sucesso!');
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
					$scope.notification.success('', 'Curso de graduação atualizado com sucesso!');
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
			'DIALOG.TITULO_ATIVAR',
			'DIALOG.BOTAO_CONFIRMAR',
			'DIALOG.BOTAO_CANCELAR',
			'DIALOG.MSG_ATIVAR',
			'DIALOG.TITULO_NOTIFICACAO',
			'DIALOG.MSG_NOTIFICACAO_ATIVADO',
		]).then(function(translations) {
			const title = translations['DIALOG.TITULO_ATIVAR'];
			const msg = translations['DIALOG.MSG_ATIVAR'] + 'curso de graduação: ' + item.nome + '?';
			const msgNotification = translations['DIALOG.MSG_NOTIFICACAO_ATIVADO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'ATIVA';
				$scope.resource
					.update({ id: $scope.objectId }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.mdswitch.situacao = true;
						$scope.notification.success('', 'Curso de graduação ' + msgNotification);
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
			'DIALOG.TITULO_INATIVAR',
			'DIALOG.BOTAO_CONFIRMAR',
			'DIALOG.BOTAO_CANCELAR',
			'DIALOG.MSG_INATIVAR',
			'DIALOG.TITULO_NOTIFICACAO_INATIVADO',
			'DIALOG.MSG_NOTIFICACAO_INATIVADO',
		]).then(function(translations) {
			const title = translations['DIALOG.TITULO_INATIVAR'];
			const msg = translations['DIALOG.MSG_INATIVAR'] + 'curso de graduação: ' + item.nome + '?';
			const msgNotification = translations['DIALOG.MSG_NOTIFICACAO_INATIVADO'];

			$scope.confirm = function(object, fn_Hide) {
				var obj = angular.copy($scope.object);
				obj.situacao = 'INATIVA';
				$scope.resource
					.update({ id: $scope.objectId }, obj)
					.$promise.then(function() {
						$scope.loadObject();
						$scope.mdswitch.situacao = false;
						$scope.notification.success('', 'Curso de graduação ' + msgNotification);
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

	if ($scope.screenMode === 'NEW') {
		$scope.createObject();
	} else {
		$scope.loadObject();
	}
}
