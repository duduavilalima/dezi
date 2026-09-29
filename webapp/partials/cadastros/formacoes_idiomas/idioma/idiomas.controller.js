angular.module('app.idioma.controllers')

.controller('IdiomaPesquisaCtrl', ['$scope', '$state', '$stateParams', '$rootScope', '$timeout', 'Idioma', function($scope,  $state, $stateParams, $rootScope, $timeout, Idioma){

    // INICIAR VARIÁVEIS
    function init() {
        $scope.listResult = null;
        $scope.resource = Idioma;
        $scope.object = {};

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.goNew = 'idioma_new';
        $scope.goView = 'idioma_view';
        $scope.goEdit = 'idioma_edit';
        $scope.goSearch = 'idiomas';
    }

    init();

    $scope.query = {
		order: 'nome',
		limit: 10,
		page: 1
	};

	$scope.find = () => {
		var filtro = {};

		$scope.promise = $scope.resource.buscar(null, filtro).$promise;


		$scope.promise.then(function(data) {
			$scope.listResult = data;
		}).catch(function(response) {
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	};

	$scope.logPagination = (page, limit) => {
		console.log('page: ', page);
		console.log('limit: ', limit);
	};

    $scope.displayNew = item => {
        $state.go($scope.goNew);
    };

    $scope.displayView = item => {
        $state.go($scope.goView, { objectId: item.id });
    };

    $scope.displayEdit = item => {
        $state.go($scope.goEdit, { objectId: item.id });
    };

    $scope.back = () => {
        if ($rootScope.$lastState.state.name !== '') {
            $state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
        } else {
            $state.go(goSearch);
        }
    };

    $timeout(function(){
		$scope.find();
	}, 600);
}])

.controller('IdiomaCadastroCtrl', ['$scope', '$state', '$stateParams', '$rootScope', '$timeout', 'Idioma', 'DialogConfirm', '$mdDialog', function($scope,  $state, $stateParams, $rootScope, $timeout, Idioma, DialogConfirm, $mdDialog) {

     // INICIAR VARIÁVEIS
     function init() {
         $scope.listResult = null;
         $scope.resource = Idioma;
         $scope.object = null;

         $scope.modulo = $state.current.data.modulo;
         $scope.titulo = $state.current.data.titulo;
         $scope.subtitulo = $state.current.data.subtitulo;

         $scope.goNew = 'idioma_new';
         $scope.goView = 'idioma_view';
         $scope.goEdit = 'idioma_edit';
         $scope.goSearch = 'idiomas';

         $scope.toolBarClassDialog = 'red-bg';
		 $scope.toolBarClassDialogATIVACAO = 'activate-bg';
	 	 $scope.toolBarIconClassDialog = '';

		 $scope.mdActionClassDialog = '';

		 $scope.buttonConfirmClassDialog = 'md-raised md-default';
		 $scope.buttonCancelClassDialog = 'md-raised md-default';
     }

    init();


    $scope.createObject = () => {

        $scope.resource.get()
            .$promise.then(function(data) {
                $scope.object = data;

            }).catch(function(response) {
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

    };

    $scope.mdswitch = {};

	$scope.onChangeSituacao = situacao => {
		$scope.mdswitch.situacao = situacao;
		if ($scope.object.situacao === 'ATIVA') {
			$scope.inactivate(null, $scope.object);
		} else {
			$scope.activate(null, $scope.object);
		}
	};

    $scope.loadObject = () => {

        $scope.objectId = $stateParams.objectId;

        if (!$scope.objectId) {
            $scope.$emit('crud-error', 'invalid-id');
            return;
        } else {
            $scope.resource.get({ id: $scope.objectId })
                .$promise.then(function(data) {
                    $scope.object = data;
					$scope.mdswitch.situacao = $scope.object.situacao === 'ATIVA';
                }).catch(function(response) {
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
        }
    };

    $scope.displayNew = item => {
        $state.go($scope.goNew);
    };

    $scope.displayView = item => {
        $state.go($scope.goView, { objectId: item.id });
    };

    $scope.displayEdit = item => {
        $state.go($scope.goEdit, { objectId: item.id });
    };

    $scope.back = () => {
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

    $scope.save = () => {
		$scope.action = true;
		if ($scope.screenMode === 'NEW') {
			$scope.object.$save()
				.then(function(data) {
					$scope.notification.success("", "Idioma salvo com sucesso!");
					$state.go($scope.goView, {objectId: data.id});
				}).catch(function(response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		} else if ($scope.screenMode === 'EDIT') {
			$scope.object.$update({ id: $scope.objectId })
				.then(function(data) {
					$scope.notification.success("", "Idioma atualizado com sucesso!");
					$state.go($scope.goView, {objectId: data.id});
				}).catch(function(response) {
					$scope.action = false;
					$scope.$emit('notify-critical',response);
				});
		}
	};


	$scope.activate = (event, item) => {
		var title = 'Ativar';
		var msg = 'Deseja ativar o(a) idioma: ' + item.nome + '?';

		$scope.confirm = function(object, fn_Hide) {
			var obj = angular.copy($scope.object);
			obj.situacao = 'ATIVA';
			$scope.resource.update({id: $scope.objectId }, obj)
				.$promise.then(function(data) {
					$scope.loadObject();
					$scope.mdswitch.situacao = true;
					$scope.notification.success("Idioma ativado(a) com sucesso!");
					fn_Hide();
				}).catch(function(response) {
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		};

		$scope.cancel = fn_Hide => {
			$timeout(function() {
				$scope.mdswitch.situacao = false;
				$scope.$apply();
			}, 300);
			fn_Hide();
		};

		DialogConfirm.show(event, title, msg, titleButtonConfirm, titleButtonCancel, $scope.confirm, $scope.cancel, $scope.toolBarClassDialogATIVACAO, $scope.toolBarIconClassDialog, $scope.mdActionClassDialog, $scope.buttonConfirmClassDialog, $scope.buttonCancelClassDialog);
	};

	$scope.inactivate = (event, item) => {
		var title = 'Inativar';
		var msg = 'Deseja inativar o(a) idioma: ' + item.nome + '?';
		var titleButtonConfirm = 'Confirmar';
		var titleButtonCancel = 'Cancelar';

		$scope.confirm = function(object, fn_Hide) {
			var obj = angular.copy($scope.object);
			obj.situacao = 'INATIVA';
			$scope.resource.update({id: $scope.objectId }, obj)
				.$promise.then(function(data) {
					$scope.loadObject();
					$scope.mdswitch.situacao = false;
					$scope.notification.success("Idioma inativado(a) com sucesso!");
					fn_Hide();
				}).catch(function(response) {
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

		DialogConfirm.show(event, title, msg, titleButtonConfirm, titleButtonCancel, $scope.confirm, $scope.cancel, $scope.toolBarClassDialog, $scope.toolBarIconClassDialog, $scope.mdActionClassDialog, $scope.buttonConfirmClassDialog, $scope.buttonCancelClassDialog);
	};

	$scope.detalhes = ev => {
    	$mdDialog.show({
    	  controller: DialogController,
    	  locals: {
    		  object: $scope.object
    	  },
    	  templateUrl: 'partials/dialogs/detalhes.html',
    	  parent: angular.element(document.body),
    	  targetEvent: ev,
    	  clickOutsideToClose:true
    	})
    	.then(function(answer) {
    	  $scope.status = 'You said the information was "' + answer + '".';
    	}, function() {
    	  $scope.status = 'You cancelled the dialog.';
    	});
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

    if($scope.screenMode === 'NEW'){
        $scope.createObject();
    }
    else{
        $scope.loadObject();
    }

}]);
