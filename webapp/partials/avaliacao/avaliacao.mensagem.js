import ROUTES from '../../utils/routes';
import TipoAvaliacaoPerfil from '../model/TipoAvaliacaoPerfil';

const STATE_MENSAGENS = ROUTES.MAIN.AVALIACAO.VIEW.MENSAGENS.name;
const MIN_CARACTERES_FILTRO = 1;

const TipoAvaliacaoPerfilExibicao = {
	TODOS: 'TODOS',
	PONTO_FOCAL: 'PONTO_FOCAL',
	...TipoAvaliacaoPerfil,
};

/* @ngInject */
export default function MensagemCadastroCtrl(
	$scope,
	$state,
	$stateParams,
	$rootScope,
	$filter,
	Avaliacao,
	AvaliacaoParticipante,
	MensageriaResource,
	$translate,
	TipoResource,
) {
	const $ctrl = this;
	$ctrl.object = $ctrl.avaliacao = $state.object;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	// TODO Rever depois de terminada a aba de avaliados
	$ctrl.participantes =
		$state.participantes
		|| $scope.participantes
		|| undefined;

	$ctrl.mensagem = {};
	$ctrl.mensagem.checkAll = false;
	$ctrl.mensagem.checkAllEmail = true;
	$ctrl.avaliacao = Avaliacao.get({ id: $scope.objectId || $stateParams.objectId });

	$ctrl.listFuncionarios = [];
	$ctrl.listFuncionariosMensagem = null;

	$ctrl.init = () => {
		$ctrl.carregarTiposPerfis();
		$ctrl.carregarListaDestinatarios();
	};

	$ctrl.displayView = (avaliacao = $ctrl.object) => {
		const params = {
			objectId: avaliacao.id,
			campanhaId: avaliacao.campanha.id,
		};
		$state.go(STATE_MENSAGENS, params);
	};

	$ctrl.notificarErro = ex => {
		$scope.$emit('notify-critical', ex);
	};

	$ctrl.notificarSucesso = mensagem => {
		$scope.$emit('notify-success', '', mensagem);
	};

	// TODO Rever depois de terminada a aba de avaliados
	$ctrl.carregarListaDestinatarios = () => {
		if ($ctrl.participantes) return;
		AvaliacaoParticipante.porPerfisEAvaliacao({ avaliacaoId: $stateParams.objectId }).$promise
			.then((data) => {
				data.forEach(value => {
					value.checkEmail = true;
				});
				$ctrl.listFuncionarios = data;
				$ctrl.listFuncionariosMensagem = data;
			})
			.catch($ctrl.notificarErro);
	};

	$ctrl.carregarTiposPerfis = () => {
		TipoResource.tiposAgentes().$promise
			.then((data) => {
				$ctrl.perfis = [];
				$ctrl.perfis.push(TipoAvaliacaoPerfilExibicao.TODOS);
				$ctrl.perfis.push(TipoAvaliacaoPerfilExibicao.PONTO_FOCAL);
				$ctrl.perfis.push(...data.filter(item => item != TipoAvaliacaoPerfilExibicao.AUTOAVALIACAO));
			})
			.catch($ctrl.notificarErro);
	};

	$ctrl.onChangeFiltro = () => {
		$ctrl.deselectAll();
		$ctrl.listFuncionariosMensagem = $filter('filter')($ctrl.listFuncionarios, filtrarMensagens);
	};

	$ctrl.onChangePerfil = () => {
		$ctrl.deselectAll();
		$ctrl.listFuncionariosMensagem = $filter('filter')($ctrl.listFuncionarios, filtrarMensagens);
	};

	$ctrl.toggleSelectedAll = () => {
		$ctrl.listFuncionariosMensagem.forEach(value => {
			value.check = !$ctrl.mensagem.checkAll;
		});
	};

	$ctrl.toggleSelectedAllEmail = () => {
		$ctrl.listFuncionariosMensagem.forEach(value => {
			value.checkEmail = !$ctrl.mensagem.checkAllEmail;
		});
	};

	$ctrl.deselectAll = () => {
		$ctrl.mensagem.checkAll = false;
		$ctrl.listFuncionariosMensagem.forEach(funcionario => {
			funcionario.check = false;
		});
	};

	$ctrl.verificarItensSelecionadosParticipantes = () => {
		const funcionarios = $ctrl.listFuncionariosMensagem || [];
		$ctrl.mensagem.checkAll =
			funcionarios.length > 0
			&& funcionarios.every(funcionario => funcionario.check);
	};

	$ctrl.verificarItensSelecionadosEmail = () => {
		const funcionarios = $ctrl.listFuncionariosMensagem || [];
		$ctrl.mensagem.checkAllEmail =
			funcionarios.length > 0
			&& funcionarios.every(funcionario => funcionario.checkEmail);
	};

	$ctrl.isNenhumSelecionado = () => {
		const funcionarios = $ctrl.listFuncionariosMensagem || [];
		return funcionarios.length === 0
			|| !funcionarios.some(funcionario => funcionario.check);
	};

	$ctrl.enviarMensagem = () => {
		const ENVIOSUCESSO_KEY = 'MENSAGEM.ENVIOSUCESSO';
		const MSG_NOTIFICACAO_KEY = 'AVALIACAO.MSG_NOTIFICACAO';
		const MSG_LINK_AVALIACAO_KEY = 'AVALIACAO.MSG_LINK_APPLICATION';

		const translationKeys = [
			MSG_NOTIFICACAO_KEY,
			MSG_LINK_AVALIACAO_KEY,
			ENVIOSUCESSO_KEY,
		];

		$ctrl.promise = $translate(translationKeys).then(translations => {
			const listMsgs = [];

			$ctrl.listFuncionariosMensagem.forEach(value => {
				if (value.check) {
					listMsgs.push({
						dataHoraAbertura: null,
						dataHoraEnvio: new Date(),
						avaliacao: $ctrl.object,
						usuarioDestino: value,
						funcionarioEnvio: $ctrl.user,
						usuarioEnvio: $ctrl.user,
						avaliacaoEnvio: $ctrl.object,
						avaliacaoDestino: null,
						mensagemOriginal: null,
						texto: $ctrl.mensagem.texto,
						titulo: $ctrl.mensagem.titulo,
						envioEmail: value.checkEmail,
						avisoEmailInternacionalizado: translations[MSG_NOTIFICACAO_KEY],
						linkEmailInternacionalizado: translations[MSG_LINK_AVALIACAO_KEY],
					});
				}
			});

			return Avaliacao.enviarMensagem({id: $ctrl.avaliacao.id}, listMsgs).$promise
				.then(() => {
					$state.go(STATE_MENSAGENS, { campanhaId: $ctrl.object.campanha.id, objectId: $ctrl.object.id });
					$ctrl.notificarSucesso(translations[ENVIOSUCESSO_KEY]);
				})
				.catch($ctrl.notificarErro);
		});
	};

	$ctrl.cancelar = () => {
		if ($rootScope.$lastState
			&& $rootScope.$lastState.state
			&& $rootScope.$lastState.state.name !== '') {
			$state.go($rootScope.$lastState.state.name, $rootScope.$lastState.params);
		} else {
			$ctrl.displayView();
		}
	};

	$ctrl.transitionToMensagens = () => {
		$ctrl.cancelar();
	};

	$ctrl.transitionToAvaliacao = () => {
		$ctrl.displayView();
	};

	$ctrl.transitionToCampanha = () => {
		$ctrl.displayView();
	};

	$ctrl.transitionToCampanhas = () => {
		$ctrl.displayView();
	};

	function filtrarMensagens(participante) {
		const filtroInformado = ($ctrl.filtro || '').toUpperCase();
		const perfilInformado = $ctrl.mensagem.perfil || '';

		if (filtroInformado && filtroInformado.length >= MIN_CARACTERES_FILTRO) {
			const valores = [
				(participante.nome || '').toUpperCase(),
				(participante.cargo.nome || '').toUpperCase(),
				(participante.departamento.nome || '').toUpperCase(),
			];

			if (!valores.some(valor => valor.startsWith(filtroInformado))) {
				return false;
			}
		}

		if (perfilInformado) {
			const { tipoPerfis } = participante;

			switch (perfilInformado) {
			case TipoAvaliacaoPerfilExibicao.TODOS:
				return true;
			case TipoAvaliacaoPerfilExibicao.PONTO_FOCAL:
				if (tipoPerfis.pontoFocal) return true;
				break;
			case TipoAvaliacaoPerfilExibicao.GESTOR:
				if (tipoPerfis.gestor) return true;
				break;
			case TipoAvaliacaoPerfilExibicao.SUBORDINADO:
				if (tipoPerfis.subordinado) return true;
				break;
			case TipoAvaliacaoPerfilExibicao.PARES:
				if (tipoPerfis.par) return true;
				break;
			}

			return false;
		}

		// Nenhum filtro foi aplicado, ou os filtros passaram
		return true;
	}

	$ctrl.init();
}
