import angular from 'angular';
import ROUTES from 'utils/routes';

import '../dialogs/dialog_exibir_mensagem.html';
import MdTableParams from '../../../utils/md-table-params';
import SituacaoAvaliacao from '../../model/SituacaoAvaliacao';

const STATE_MENSAGEM_NEW = ROUTES.MAIN.AVALIACAO.NEW_MENSAGEM.name;

const TR_TITLEEXIBIRMENSAGEM_KEY = 'MENSAGEM.TITLEEXIBIRMENSAGEM';

const SITUACOES_AVALIACAO_NOVA_MENSAGEM = [
	SituacaoAvaliacao.CANCELADA,
	SituacaoAvaliacao.INATIVA,
	SituacaoAvaliacao.CONCLUIDA,
];

/* @ngInject */
export default function abaMensagensCtrl(
	$stateParams,
	$scope,
	$state,
	$mdDialog,
	$mdMedia,
	MensageriaResource,
	$translate,
) {
	const $ctrl = this;
	$ctrl.object = $ctrl.avaliacao = $state.object;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;

	// NOTE: Necessário para selecionar a aba do controller atual quando o usuário entrar na página via URL
	// Há um bug no md-tab que não permite o seu uso em conjunto com o ui-sref.
	// O issue no repositório do AngularJS sobre este problema está como "corrigido", mas a correção
	// indicada não funcionou para nós, inicialmente. Rever ao final da refatoração.
	// Vide: https://github.com/angular/material/issues/5351
	$scope.$emit('selectedIndexTab', $state.selectedIndexTab);

	$ctrl.mensagensEnviadas = [];
	$ctrl.mensagensRecebidas = [];

	$ctrl.enviadosTableParams = new MdTableParams();
	$ctrl.recebidosTableParams = new MdTableParams();

	$ctrl.init = () => {
		$ctrl.findMensageria();
	};

	$ctrl.notificarErro = ex => {
		$scope.$emit('notify-critical', ex);
	};

	$ctrl.findMensageria = () => {
		var filtro = {};
		filtro.avaliacao = {};
		filtro.avaliacao.id = $stateParams.objectId;
		filtro.idOriginalIsNull = true;

		// Mensagens enviadas
		$ctrl.promiseMensagensEnviadas = MensageriaResource.getAllMessagesByAvaliacao({ id: $stateParams.objectId }).$promise;
		$ctrl.promiseMensagensEnviadas
			.then(mensagens => {
				$ctrl.mensagensEnviadas.push(...mensagens);
				$ctrl.onPaginateMensagensEnviadas();
			})
			.catch($ctrl.notificarErro);

		// Mensagens recebidas
		$ctrl.promiseMensagensRecebidas = MensageriaResource.getAllMessagesRecebidasByAvaliacao({ id: $stateParams.objectId }).$promise;
		$ctrl.promiseMensagensRecebidas
			.then(mensagens => {
				$ctrl.mensagensRecebidas.push(...mensagens);
				$ctrl.onPaginateMensagensRecebidas();
			})
			.catch($ctrl.notificarErro);
	};

	$ctrl.canAdicionarMensagem = () => {
		return !!$ctrl.avaliacao && SITUACOES_AVALIACAO_NOVA_MENSAGEM.some(status => $ctrl.avaliacao.status === status);
	};

	$ctrl.onAdicionarMensagem = () => {
		$state.go(STATE_MENSAGEM_NEW, { campanhaId: $ctrl.object.campanha.id, objectId: $ctrl.object.id });
	};

	$ctrl.onPaginateMensagensEnviadas = () => {
		$ctrl.mensagensEnviadasPage = $ctrl.enviadosTableParams.paginate($ctrl.mensagensEnviadas);
	};

	$ctrl.onPaginateMensagensRecebidas = () => {
		$ctrl.mensagensRecebidasPage = $ctrl.recebidosTableParams.paginate($ctrl.mensagensRecebidas);
	};

	$ctrl.onExibirMensagem = (ev, item) => {
		var useFullScreen = $mdMedia('xs');

		$translate([TR_TITLEEXIBIRMENSAGEM_KEY]).then(translations => {
			$mdDialog.show({
				controller: ExibirMensagemDialogController,
				controllerAs: '$dialog',
				locals: {
					title: translations[TR_TITLEEXIBIRMENSAGEM_KEY],
					avaliacao: $ctrl.avaliacao,
					mensagem: item,
					participantes: $ctrl.avaliacao.participantes,
					mensageriaResource: MensageriaResource,
				},
				templateUrl: 'dialog_exibir_mensagem.html',
				parent: angular.element(document.body),
				openFrom: angular.element('#table-list'),
				closeTo: angular.element('#table-list'),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: useFullScreen,
			});
		});
	};

	function ExibirMensagemDialogController(
		$scope,
		$mdDialog,
		title,
		avaliacao,
		mensagem,
		participantes,
		mensageriaResource,
	) {
		const $dialog = this;
		$scope.$dialog = $dialog;

		$dialog.title = title;
		$dialog.avaliacao = avaliacao;
		$dialog.participantes = participantes;

		$dialog.mensagens = null;
		$dialog.destinatariosTableParams = new MdTableParams();

		$dialog.promise = mensageriaResource.buscar(null, {
			titulo: mensagem.titulo,
			numMensagem: mensagem.numMensagem,
		}).$promise
			.then(data => {
				$dialog.mensagens = data;
				$dialog.mensagem = data[0];
			})
			.catch(error => {
				$ctrl.notificarErro(error);
				$mdDialog.hide();
			});

		$dialog.onFechar = () => {
			$mdDialog.hide();
		};
	}

	$ctrl.init();
}
