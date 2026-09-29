import angular from 'angular';
import TABS_MENSAGENS from './utils/abas.config';

/* @ngInject */
export default function mensagensController(
	$scope,
	$state,
	$rootScope,
	$mdDialog,
	$mdMedia,
	MensageriaResource,
	$translate,
) {
	const $ctrl = this;
	$ctrl.state = $state;
	$ctrl.tabs = TABS_MENSAGENS;
	//FIXME: Arrumar o serviço para obter o usuario logado

	$ctrl.user = $rootScope.user;
	$ctrl.listEnviados = [];
	$ctrl.listDestinos = [];
	$ctrl.listTodos    = [];

	$ctrl.onTabSelected = tab => {
		$state.go(tab.stateName);
	};

	$ctrl.queryTodos = {
		order: 'titulo',
		limit: 5,
		page: 1
	};

	$ctrl.queryEnviados = {
		order: 'titulo',
		limit: 5,
		page: 1
	};

	$ctrl.queryRecebidos = {
		order: 'titulo',
		limit: 5,
		page: 1
	};

	const listMensagens = () => {
		MensageriaResource.mensagens({id: $ctrl.user.id}).$promise.then((data) => {
			$ctrl.listEnviados = [];
			$ctrl.listDestinos = [];
			$ctrl.listTodos = [];

			data.envio.forEach(value => {
				const msg = {
					...value,
					isEnvio: true
				};
				$ctrl.listTodos.push(msg);
				$ctrl.listEnviados.push(msg);
			});

			data.destino.forEach(value => {
				const msg = {
					...value,
					isDestino: true
				};
				if (msg.dataHoraAbertura == null) {
					msg.isNew = true;
				}

				$ctrl.listTodos.push(msg);
				$ctrl.listDestinos.push(msg);
			});
		}).catch(function(response) {
			$scope.$emit('notify-critical',response);
		});
	};

	$ctrl.confirmExibicao = (fn_hide) => {
		fn_hide();
		listMensagens();
	};

	// EXIBIR MENSAGEM //
	$ctrl.acaoExibirMensagemEnviada = (ev, index, item) => {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'));
		$mdDialog.show({
			controller: 'dialogMensagemExibicao',
			locals: {
				object: item,
				title: 'Exibir mensagem',
				index: index,
				mensageriaResource: MensageriaResource,
				fn_confirm: $ctrl.confirmExibicao,
				$rootScope: $rootScope,
				resp_exibicao: $ctrl.acaoResponderMensagem
			},
			templateUrl: 'dialog.exibir-mensagem.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen
		});
		$scope.$watch(function() {
			return $mdMedia('xs') || $mdMedia('sm');
		}, (wantsFullScreen) => {
			$ctrl.customFullscreen = (wantsFullScreen === true);
		});
	};

	// RESPONDER

	$ctrl.confirmResponder = (object, fn_hide) => {
		$translate(['AVALIACAO.MSG_NOTIFICACAO','AVALIACAO.MSG_LINK_APPLICATION']).then((translations) => {
			object.avisoEmailInternacionalizado = translations['AVALIACAO.MSG_NOTIFICACAO'];
			object.linkEmailInternacionalizado = translations['AVALIACAO.MSG_LINK_APPLICATION'];
			MensageriaResource.save(object).$promise.then(() => {
				fn_hide();
				listMensagens();
				$scope.$emit('notify-success', 'Mensagem enviada com sucesso!');
			}).catch((response) => {
				$scope.$emit('notify-critical',response);
			});
		});
	};

	$ctrl.acaoResponderMensagem = (ev, item) => {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'));
		
		$mdDialog.show({
			controller: 'dialogMensagemResponder',
			locals: {
				object: item,
				title: 'Responder mensagem',
				mensageriaResource: MensageriaResource,
				fn_confirm: $ctrl.confirmResponder
			},
			templateUrl: 'dialog.responder-mensagem.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen
		});
		$scope.$watch(function() {
			return $mdMedia('xs') || $mdMedia('sm');
		}, function(wantsFullScreen) {
			$scope.customFullscreen = (wantsFullScreen === true);
		});
	};

	$ctrl.acaoEncaminharMensagem = (ev, index, item) => {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'));
		$mdDialog.show({
			controller: 'dialogMensagem',
			locals: {
				object: item,
				msg: '',
				index: index,
				title: 'Encaminhar mensagem',
				color: 'background-color: rgb(39,84,152);color: rgba(255,255,255,0.87);'
			},
			templateUrl: 'dialog.encaminhar-mensagem.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: useFullScreen
		});
		$scope.$watch(function() {
			return $mdMedia('xs') || $mdMedia('sm');
		}, function(wantsFullScreen) {
			$ctrl.customFullscreen = (wantsFullScreen === true);
		});
	};

	//FIXME: ???
	//$ctrl.stopTimeMsg = $interval(listMensagens, 60000);

	listMensagens();
}