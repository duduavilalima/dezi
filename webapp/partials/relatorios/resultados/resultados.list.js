/* @ngInject */
export default function resultadoBIController(
	$state,
	listCampanhas,
	user,
	pentahoLogin,
	Campanha,
	BiParametroUsuario
) {
	const $ctrl = this;
	$ctrl.object = $state.object;
	$ctrl.listCampanhas = listCampanhas;
	$ctrl.loggedUser = user;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.isAdministrator = ['ADMINISTRADOR', 'GESTOR_RH'].includes(user.tipoPerfil);

	$ctrl.isGestor = 'GESTOR_AREA' == user.tipoPerfil;

	$ctrl.tableConf = {
		page: 1,
		limit: 10,
		order: 'descricao'
	};

	$ctrl.mostraResultado = function (item) {
		const biParametroUsuarioTO = BiParametroUsuario.criarparametros({campanhaId: item.id, idFuncionario: item.id});
		Campanha.avaliacoes({ id: item.id }).$promise.then(function(data) {
			let somenteAutoavalicao = true;
			data.forEach(avaliacao => {
				if (!avaliacao.autoavaliacao || avaliacao.gestores) {
					somenteAutoavalicao = false;
				}
			});

			let link = pentahoLogin.server;
			if (somenteAutoavalicao) {
				link += '/reports_v2.jsp' +
					'?campanhaId=' + item.id +
					'&campanha=' + item.descricao +
					'&empresaId=' + user.empresa.id +
					'&identificador=' + user.empresa.identificador +
					'&user=' + user.id +
					'&chave=' + biParametroUsuarioTO.chave +
					'&reportFile=avaliacao.wcdf';
			} else if (user.empresa.versaoBi == 1) {
				link += '/reports.jsp' +
					'?campanhaId=' + item.descricao +
					'&avaliacao=All Avaliacaos' +
					'&identificador=' + user.empresa.identificador +
					'&chave=' + biParametroUsuarioTO.chave ;

				if ($ctrl.isAdministrator) {
					link += '&gestorId=All Gestors';
				} else if ($ctrl.isGestor) {
					link += '&gestorId=' + user.nome;
				} else {
					link += '&avaliadoId=' + user.nome;
				}

				if ($ctrl.isAdministrator || $ctrl.isGestor) {
					link += '&reportFile=resultados.wcdf';
				} else {
					link += '&reportFile=resultados_colaborador.wcdf';
				}
			} else {
				link += '/reports_v2.jsp' +
					'?chave=' + biParametroUsuarioTO.chave +
					'&reportFile=resultados.wcdf' ;
			}

			window.open(link, '_blank');
		});
	};
}
