/* @ngInject */
const dialogMensagemResponder = ($scope, $mdDialog, object, title, fn_confirm) => {
	$scope.title = title;
	//FIXME: Ver o que é realmente usado desse objeto ao salvar uma msg
	$scope.object = {
		mensagemOriginal: {...object},
		dataHoraEnvio: new Date(),
		usuarioEnvio: object.usuarioEnvio,
		usuarioDestino: object.usuarioDestino,
		avaliacaoEnvio: object.avaliacaoEnvio,
		avaliacaoDestino: object.avaliacaoDestino,
		numMensagem: object.numMensagem,
		avaliacao: object.avaliacao,
		titulo: 'Re: ' + object.titulo
	};

	$scope.cancel = () => {
		$mdDialog.hide();
	};

	$scope.responder = () => {
		fn_confirm($scope.object, $mdDialog.hide);
	};
};

/* @ngInject */
const dialogMensagem = ($scope, $mdDialog, title, color) => {
	$scope.title = title;
	$scope.color = color;
	$scope.object = {data: new Date()};

	$scope.hide = function() {
		$mdDialog.hide();
	};

	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	$scope.confirmar = function() {
		$mdDialog.hide();
	};
};

/* @ngInject */
const dialogMensagemExibicao = (
	$scope,
	$mdDialog,
	object,
	title,
	index,
	mensageriaResource,
	fn_confirm,
	resp_exibicao
) => {
	$scope.title = title;
	$scope.object = object;
	const destino = object.isDestino;

	// MENSAGEM RECEBIDA
	if ($scope.object.dataHoraAbertura == null && $scope.object.isDestino) {
		$scope.object.dataHoraAbertura = new Date();

		mensageriaResource.update({id: $scope.object.id}, $scope.object)
			.$promise.then((data) => {
				$scope.object = {
					...data,
					isDestino: destino
				};
			}).catch((response) => {
				console.error(response);
				$scope.$emit('notify-critical',response);
			});
	}

	$scope.responder_exibicao = function(ev){
		$mdDialog.hide();
		resp_exibicao(ev, object);
	};

	// MENSAGEM ENVIADA
	//		if ($scope.object.isEnvio && $scope.object.mensagemOriginal == null) {
	//			$scope.promise = mensageriaResource.destinatariosmensagem($scope.object).$promise;
	//
	//			$scope.promise.then(function(data) {
	//                $scope.listAnalise = [];
	//				angular.forEach(data, function(value, index) {
	//					if (value.usuarioEnvio.id === $scope.object.usuarioEnvio.id) {
	//					    $scope.listAnalise.push(value);
	//					}
	//				});
	//			}).catch(function(response) {
	//				$scope.$emit('notify-critical',response);
	//			});
	//		}

	$scope.cancel = () => {
		$mdDialog.hide();
	};

	$scope.confirmar = () => {
		fn_confirm($mdDialog.hide);
	};
};

export { dialogMensagemResponder, dialogMensagem, dialogMensagemExibicao };
