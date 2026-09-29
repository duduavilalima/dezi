/* @ngInject */
export default function admMensagemController($scope, $rootScope, MensagemService, perfis, funcionarios) {
	$scope.perfis = perfis;
	$scope.funcionarios = funcionarios;
	$scope.tipoDestinatario = 'colaborador';

	$scope.listaTipoDestinatario = [
		{
			'label': 'Perfil',
			'value': 'perfil',
		},
		{
			'label': 'Colaborador',
			'value': 'colaborador',
		}
	];

	$scope.searchTextColaborador = '';
	$scope.selectedFilter = {};

	$scope.object = {};
	$scope.object.empresa = $rootScope.user.empresa;

	//TODO Usar template HTML
	$scope.object.mensagem =
		'Olá!<br/>' +
		'Esse é o novo Sistema de Avaliação de Desempenho da ' + $scope.object.empresa.nome + '.<br/><br/>' +
		'Agora você terá acesso a um Portal onde, além de preencher as suas avaliações, ' +
		'você poderá acompanhar o preenchimento dos seus avaliadores e ainda acessar os relatórios sobre o seu desempenho.<br/><br/>' +
		'Seguem abaixo os dados para acesso ao sistema:<br/>' +
		'Endereço web: #URL#<br/>' +
		'Login: #LOGIN#<br/>' +
		'Senha: #PASSWORD#<br/>' +
		'Atenciosamente,<br/>' +
		'Assessoria de Gestão de Pessoas ' + $scope.object.empresa.nome;

	$scope.enviarMensagem = function () {
		MensagemService.enviarMensagemBoasVindas($scope.object).$promise.then(() => {
			$scope.$emit('notify-success', 'Mensagem enviada com sucesso!');
		});
	};

	//Funções do autocomplete
	//Setando o valor selecionado no autocomplete do colaborador
	$scope.selectedItemColaboradorChange = item => {
		if (item.nome) {
			$scope.object.colaborador = item;
			$scope.selectedFilter.nome = item.nome;
		}
	};

	$scope.changeSearchTextColaborador = searchTextColaborador => {
		$scope.selectedFilter.nome = searchTextColaborador;
	};

	$scope.querySearchColaborador = query => {
		query = query.toUpperCase();

		let participantes = [];
		participantes = $scope.funcionarios.filter(filtro => {
			if (filtro.nome.toUpperCase().includes(query)) {
				return filtro;
			}
		});

		return participantes;
	};

	$scope.limparSelecao = () => {
		delete $scope.object.perfil;
		delete $scope.selectedFilter.nome;
		delete $scope.object.colaborador;
	};
}
