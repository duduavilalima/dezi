/* @ngInject */
export default function historicosBIListCtrl(
	$state,
	user,
	pentahoLogin,
	listFuncionarios,
	BiParametroUsuario) {
	const $ctrl = this;
	$ctrl.object = $state.object;
	$ctrl.user = user;
	$ctrl.listFuncionarios = listFuncionarios;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.tableConf = {
		page: 1,
		limit: 10,
		order: 'nome'
	};

	$ctrl.mostraResultado = (item) => {
		if (user.empresa.versaoBi == 1) {
			const link = pentahoLogin.server + '/reports.jsp' +
				'?avaliadoId=' + item.nome  +
				'&avaliacao=All Avaliacaos' +
				'&reportFile=graficos_evolucao.wcdf' +
				'&identificador=' + $ctrl.user.empresa.identificador;

			window.open(link,'_blank');
		} else {
			BiParametroUsuario.criarparametros({campanhaId: 0, idFuncionario: item.id})
				.$promise.then(function(parametros) {
					const link = pentahoLogin.server + '/reports_v2.jsp' +
					'?chave=' + parametros.chave +
					'&reportFile=graficos_evolucao.wcdf';
					window.open(link,'_blank');
				});
		}
	};
}
