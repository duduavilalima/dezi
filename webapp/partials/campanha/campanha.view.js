import ROUTES from 'utils/routes';
import SituacaoAvaliacao from '../model/SituacaoAvaliacao';

const STATE_NEW = ROUTES.MAIN.CAMPANHA.NEW.name;
const STATE_VIEW = ROUTES.MAIN.CAMPANHA.VIEW.name;
const STATE_EDIT = ROUTES.MAIN.CAMPANHA.EDIT.name;
const STATE_LIST = ROUTES.MAIN.CAMPANHA.LIST.name;
const STATE_AVALIACAO = ROUTES.MAIN.AVALIACAO.VIEW.name;
const STATE_EDITAR_AVALIACAO = ROUTES.MAIN.AVALIACAO.EDIT.name;
const STATE_NOVA_AVALIACAO = ROUTES.MAIN.AVALIACAO.NEW.name;

/* @ngInject */
export default function campanhaListCtrl(
	$stateParams,
	$translate,
	$location,
	$state,
	$scope,
	DialogConfirm,
	DialogDetalhes,
	roles,
	Campanha,
	Avaliacao,
	DialogWait
) {
	const $ctrl = this;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	$ctrl.onChangeSituacao = () => {
		const situacaoOriginal = $ctrl.object.status;
		const textoNotificacao = $ctrl.situacaoCampanha
			? 'DIALOG.TEXT_ATIVAR_NOTIFICATION'
			: 'DIALOG.TEXT_INATIVAR_NOTIFICATION';
		const traduzirTexto = () =>
			$translate([textoNotificacao]).then(translations => {
				$scope.$emit(
					'notify-success',
					'',
					translations[textoNotificacao],
				);
			});
		DialogConfirm.show(
			$ctrl.situacaoCampanha
				? 'DIALOG.TITLE_ATIVAR_CAMPANHA'
				: 'DIALOG.TITLE_INATIVAR_CAMPANHA',
			$ctrl.situacaoCampanha
				? 'DIALOG.MSG_ATIVAR_CAMPANHA'
				: 'DIALOG.MSG_INATIVAR_CAMPANHA',
			$ctrl.object,
		)
			.then(() => {
				$ctrl.object.status =
					situacaoOriginal === 'ATIVA' ? 'INATIVA' : 'ATIVA';
				$ctrl.situacaoCampanha =
					situacaoOriginal === 'ATIVA' ? false : true;
				return $ctrl.object
					.$transition({
						status:
							situacaoOriginal === 'ATIVA' ? 'INATIVA' : 'ATIVA',
					})
					.then(traduzirTexto);
			})
			.catch(() => {
				$ctrl.object.status = situacaoOriginal;
				$ctrl.situacaoCampanha =
					situacaoOriginal === 'ATIVA' ? true : false;
			});
	};

	$ctrl.acaoLiberarAvaliacao = avaliacao => {
		const liberar = () => {
			const dialog = DialogWait.showClick('Aguarde, distribuindo avaliações...', false);

			return new Promise((resolve, reject) => {
				Avaliacao.liberarAvaliacao(avaliacao)
					.$promise.then(() => {
						$ctrl.listAvaliacoes.forEach(item => {
							if (avaliacao.id === item.id) {
								item.status = SituacaoAvaliacao.AG_LIBERACAO;
								item.situacao = SituacaoAvaliacao.AG_LIBERACAO;
								item.emLiberacao = true;
							}
						});
						dialog.hide();
						resolve(true);
					})
					.catch(response => {
						//TODO: refatorar, tratar melhor o erro da regra de negócio no backEnd, $resource retorna um erro critico que não deveria
						dialog.hide();
						console.error(response);
						response.data.forEach(data => {
							const translationMsg = `${data.code}.${data.params[0]}`;

							$translate([translationMsg]).then(translations => {
								$scope.$emit(
									'notify-error',
									translations[translationMsg],
									'Erro',
								);
							});
						});
						reject(false);
					});
			});
		};

		dialogAcao(
			avaliacao,
			'DIALOG.TITULO_LIBERAR',
			'DIALOG.MSG_LIBERAR',
			'DIALOG.NOTIFICACAO_TEXTO_EM_LIBERACAO',
			liberar,
		);
	};

	$ctrl.acaoConcluirAvaliacao = avaliacao => {
		dialogAcao(
			avaliacao,
			'DIALOG.TITULO_CONCLUIR',
			'DIALOG.MSG_CONCLUIR',
			'DIALOG.NOTIFICACAO_TEXTO_CONCLUIR',
			() => avaliacao.$transition({ status: 'CONCLUIDA' }).$promise,
		);
	};

	$ctrl.acaoInativarAvaliacao = avaliacao => {
		dialogAcao(
			avaliacao,
			'DIALOG.TITLE_INATIVAR_CAMPANHA',
			'DIALOG.MSG_INATIVAR_AVAL',
			'DIALOG.TEXT_INATIVAR_AVAL_NOTIFICATION',
			() => avaliacao.$transition({ status: 'INATIVA' }).$promise,
		);
	};

	$ctrl.acaoAtivarAvaliacao = avaliacao => {
		dialogAcao(
			avaliacao,
			'DIALOG.TITLE_ATIVAR_CAMPANHA',
			'DIALOG.MSG_ATIVAR_AVAL',
			'DIALOG.TEXT_ATIVAR_AVAL_NOTIFICATION',
			() => avaliacao.$transition({ status: 'LIBERADA' }).$promise,
		);
	};

	$ctrl.acaoRemoverAvaliacao = avaliacao => {
		const obterItem = element => {
			return element.id === avaliacao.id;
		};

		const remover = () => {
			return Avaliacao.removeragliberacao({
				id: avaliacao.id,
			}).$promise.then(() => {
				$ctrl.listAvaliacoes.splice(
					$ctrl.listAvaliacoes.findIndex(obterItem),
					1,
				);
			});
		};

		dialogAcao(
			avaliacao,
			'DIALOG.TITLE_REMOVE',
			'DIALOG.MSG_REMOVER',
			'DIALOG.TEXT_REMOVE_NOTIFICATION',
			remover,
		);
	};

	$ctrl.acaoRepetirAvaliacao = avaliacao => {
		const traduzirCopyAv = () =>
			$translate(['AVALIACAO.COPY']).then(
				translations => translations['AVALIACAO.COPY'],
			);
		const copiarAvaliacao = () => {
			traduzirCopyAv().then(copiaText => {
				avaliacao.descricaoAbreviada =
					avaliacao.descricaoAbreviada + ' - ' + copiaText;
				return Avaliacao.copiar(avaliacao).$promise.then(() => {
					loadObject();
				});
			});
		};

		dialogAcao(
			avaliacao,
			'DIALOG.TITULO_REPETIR',
			'DIALOG.MSG_REPETIR',
			'DIALOG.NOTIFICACAO_TEXTO_REPETIR',
			copiarAvaliacao,
		);
	};

	const dialogAcao = (avaliacao, titulo, msg, textoNotificacao, acao) => {
		const traduzirTexto = () =>
			$translate([textoNotificacao]).then(translations => {
				$scope.$emit(
					'notify-success',
					'',
					translations[textoNotificacao],
				);
			});
		DialogConfirm.show(titulo, msg, avaliacao)
			.then(acao)
			.then(traduzirTexto);
	};

	$ctrl.detalhes = () => {
		DialogDetalhes.show($ctrl.object);
	};

	const loadObject = () => {
		$ctrl.object = Campanha.get({ id: $stateParams.objectId });
		$ctrl.object.$promise.then(() => {
			$ctrl.situacaoCampanha = $ctrl.object.status === 'ATIVA';
		});
		$ctrl.listAvaliacoesPromise = Avaliacao.porCampanha({
			campanhaId: $state.params.objectId,
		}).$promise.then(data => {
			$ctrl.listAvaliacoes = data;
		});
	};

	//TODO: Criar um método para isso no AuthService
	const requiredRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];
	$ctrl.authPodeIncluir = roles.some(
		role => requiredRoles.indexOf(role.authority) >= 0,
	);

	$ctrl.admin = roles.some(role => role.authority === 'ROLE_ADMINISTRADOR');

	$ctrl.acaoNovaAvaliacao = () => {
		$state.go(STATE_NOVA_AVALIACAO, { campanhaId: $ctrl.object.id });
	};

	$ctrl.displayNew = () => {
		$state.go(STATE_NEW, { from: btoa(STATE_VIEW), ...$state.params });
	};

	$ctrl.displayEdit = campanha => {
		$state.go(STATE_EDIT, {
			from: btoa(STATE_VIEW),
			objectId: campanha.id,
		});
	};

	$ctrl.exibirAvaliacao = avaliacao => {
		$state.go(STATE_AVALIACAO, {
			campanhaId: $ctrl.object.id,
			objectId: avaliacao.id,
		});
	};

	$ctrl.editAvaliacao = avaliacao => {
		$state.go(STATE_EDITAR_AVALIACAO, {
			campanhaId: $ctrl.object.id,
			objectId: avaliacao.id,
			from: 'campanha',
		});
	};

	$ctrl.back = () => {
		if ($location.search().from) {
			$state.go($location.search().from, { ...$location.search() });
		} else {
			$state.go(STATE_LIST);
		}
	};

	loadObject();
}
