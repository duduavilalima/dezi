import TipoAvaliacaoPerfil from 'partials/model/TipoAvaliacaoPerfil';

/* @ngInject */
const AdicionarAvaliadorDialog = (
	$scope,
	$mdDialog,
	avaliacao,
	avaliado,
	title,
	toolBarClassDialog,
	tiposDeAgentes,
	fnFindAvaliadores,
	fnAddAvaliadores
) => {
	$scope.avaliacao = avaliacao;
	$scope.avaliado = avaliado;
	$scope.title = title;
	$scope.toolBarClassDialog = toolBarClassDialog;
	$scope.tiposDeAgentes = [];
	$scope.avaliador = {};
	$scope.chipsAvaliador = [];
	$scope.perfilAgente = undefined;

	$scope.tiposDeAgentes = tiposDeAgentes.filter(value => {
		return (
			avaliacao.gestores && value === TipoAvaliacaoPerfil.GESTOR ||
			avaliacao.pares && value === TipoAvaliacaoPerfil.PARES ||
			avaliacao.subordinados && value === TipoAvaliacaoPerfil.SUBORDINADO
		);
	});
	//AUTOCOMPLETE DO AVALIADOR DO FUNCIONARIO
	$scope.querySearchAvaliador = query => {
		return fnFindAvaliadores(query).then((avaliadores) => {
			return avaliadores;
		});
	};

	//setando os valores selecionados no autocomplete em Avaliador
	$scope.selectedItemAvaliadorChange = item => {
		if (item) {
			$scope.selectedItemAvaliador = item.nome;
			$scope.chipsAvaliador.push(item);
		}
	};

	$scope.hide = function() {
		$mdDialog.hide();
	};

	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	$scope.confirmar = function() {
		if ($scope.avaliacao.pendencias.length > 0) {
			return {
				type: 'notify-error',
				statusText: 'Atenção!',
				data: 'Só é permitido a inclusão de um avaliador após a resolução das pendências detectadas.'
			};
		} else if ($scope.chipsAvaliador.length <= 0) {
			return {
				type: 'notify-error',
				statusText: 'Atenção!',
				data: 'Por favor, informe o avaliador.'
			};
		} else if (!$scope.perfilAgente) {
			return {
				type: 'notify-error',
				statusText: 'Atenção!',
				data: 'Por favor, informe o perfil do avaliador.'
			};
		} else {
			return fnAddAvaliadores(
				$scope.chipsAvaliador,
				$scope.perfilAgente,
				$scope.hide);
		}
	};
};

/* @ngInject */
const LiberarAbaAvaliadoresDialog = (
	$scope,
	$mdDialog,
	title,
	toolBarClassDialog,
	msg,
	mdActionClassDialog,
	acao,
	fnSalvarAvaliadores) => {
	$scope.title = title;
	$scope.toolBarClassDialog = toolBarClassDialog;
	$scope.msg = msg;
	$scope.mdActionClassDialog = mdActionClassDialog;

	$scope.hide = () => {
		$mdDialog.hide();
	};

	$scope.salvar = () => {
		fnSalvarAvaliadores();
		$mdDialog.hide();
	};
};

export { AdicionarAvaliadorDialog, LiberarAbaAvaliadoresDialog };
