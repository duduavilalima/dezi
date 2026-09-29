/* @ngInject */
function abaAcompanhamentoCadastroCtrl(
	$scope,
	DialogConfirm,
	AcompanhamentoDialogsService
) {
	$scope.acaoAdicionarParticipante = AcompanhamentoDialogsService.showDialogAdicionarParticipante;
	$scope.acaoAlterarColaborador = AcompanhamentoDialogsService.showDialogAlterarColaborador;
	$scope.acaoAlterarDatasLimites = AcompanhamentoDialogsService.showDialogAlterarDatasLimite;
	$scope.adicionarTipoCompetencia = AcompanhamentoDialogsService.showDialogAdicionarTipoCompetencia;

	$scope.ajuda = function () {

	};

	$scope.changeGestores = function () {
		if (!$scope.object.gestores) {
			$scope.object.preencheAv1 = false;
			$scope.object.preencheAv3 = false;
			$scope.object.tiposCompetencia.forEach(function (tipo) {
				tipo.gestor = false;
				tipo.percentualGestor = 0;
			});
		}
	};

	$scope.changeSubordinados = function () {
		if (!$scope.object.subordinados) {
			$scope.object.tiposCompetencia.forEach(function (tipo) {
				tipo.subordinados = false;
				tipo.percentualSubordinados = 0;
			});
		}
	};

	$scope.changePares = function () {
		if (!$scope.object.pares) {
			$scope.object.tiposCompetencia.forEach(function (tipo) {
				tipo.pares = false;
				tipo.percentualPares = 0;
			});
		}
	};

	$scope.changeCheckBoxTipoCompetencia = function (item) {
		if (!item.gestor) {
			item.percentualGestor = 0;
		}

		if (!item.subordinados) {
			item.percentualSubordinados = 0;
		}

		if (!item.pares) {
			item.percentualPares = 0;
		}
	};

	$scope.desabilitarBotaoAdicionar = function () {
		if ($scope.object) {
			if ($scope.object.gestores || $scope.object.subordinados || $scope.object.pares) {
				return false;
			}
		}

		return true;
	};

	$scope.removerTipoCompetencia = function (event, posicao, item) {
		var title = 'Remover Tipo de competência';
		var msg = 'Deseja remover o tipo de competência: ' + item.nomeCurto + ' ?';

		$scope.confirm = function (object, fn_Hide) {
			if ($scope.object.tiposCompetencia != null && $scope.object.tiposCompetencia.length == 0) {
				$scope.notification.error('', 'Avaliação deve ter pelo menos um tipo de competência!');
				fn_Hide();
			} else {
				$scope.object.tiposCompetencia.splice(posicao, 1);
				fn_Hide();
			}
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
}

export default abaAcompanhamentoCadastroCtrl;
