import './dialog.avaliados.html';
import './dialog.enviar_mensagem.html';
import './dialog.percentual_preenchimento.html';

/* @ngInject */
export default function acompanhamentosDialogsService($mdDialog) {
	const service = {};

	function DialogEnviarMensagemController(
		$scope,
		$mdDialog,
		translations,
		destinatarios,
		user,
		Acompanhamento
	) {
		$scope.object = {};
		$scope.remetente = user.nome;
		$scope.cancel = $mdDialog.cancel;

		$scope.object.listaColaboradores = destinatarios;
		if (!destinatarios || destinatarios.length == 0)
			return;

		if (destinatarios.length == 1) {
			$scope.para = $scope.object.listaColaboradores[0].nome;
		} else {
			$scope.para = 'Todos';
		}

		$scope.confirmar = function () {
			$scope.object.avisoEmailInternacionalizado = translations['AVALIACAO.MSG_NOTIFICACAO'];
			$scope.object.linkEmailInternacionalizado = translations['AVALIACAO.MSG_LINK_APPLICATION'];

			Acompanhamento.enviarmensagem(null, $scope.object).$promise
				.then(() => {
					$scope.$emit('notify-success', 'Mensagem enviada com sucesso!');
					$mdDialog.hide();
				});
		};
	}

	service.enviarMensagem = function($scope, destinatarios, user) {
		/* @ngInject */
		const translate = ($translate) => $translate(['AVALIACAO.MSG_NOTIFICACAO', 'AVALIACAO.MSG_LINK_APPLICATION']);
		$mdDialog.show({
			controller: DialogEnviarMensagemController,
			templateUrl: 'dialog.enviar_mensagem.html',
			locals: {
				destinatarios,
				user
			},
			resolve: {
				translations: translate
			}
		}).then(() => {
			$scope.$emit('notify-success', 'Mensagem enviada com sucesso!');
		});
	};

	service.showAvaliados = (avaliados) => {
		function DialogController($scope, $mdDialog, $item, FotoResource) {
			$scope.avaliados = $item;
			$scope.avaliados.forEach(funcionario => {
				FotoResource.avatarFuncionario({id: funcionario.id}).$promise.then((foto) => {
					funcionario.croppedImage = foto.base64;
				});
			});
			$scope.cancel = $mdDialog.cancel;
		}

		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'dialog.avaliados.html',
			locals: {
				$item: avaliados
			},
			clickOutsideToClose: true,
		});
	};

	service.showAvaliacoes = function(item, lista) {
		function DialogController($scope, $mdDialog, $item) {
			$scope.cancel = $mdDialog.cancel;
			$scope.item = $item;

			$scope.item.percAuto = Math.round(100.0 * $scope.item.preenchAuto / $scope.item.totalAuto) + '%';
			$scope.item.percGestor = Math.round(100.0 * $scope.item.preenchGestor / $scope.item.totalGestor) + '%';
			$scope.item.percSubordinados = Math.round(100.0 * $scope.item.preenchSubordinados / $scope.item.totalSubordinados) + '%';
			$scope.item.percPares = Math.round(100.0 * $scope.item.preenchPares / $scope.item.totalPares) + '%';
			$scope.item.percConsenso = Math.round(100.0 * $scope.item.preenchConsenso / $scope.item.totalConsenso) + '%';
		}

		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'dialog.percentual_preenchimento.html',
			locals: {
				$item: item,
				$lista: lista
			},
			clickOutsideToClose: true,
		});
	};

	return service;
}
