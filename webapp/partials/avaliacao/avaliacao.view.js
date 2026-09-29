// import angular from 'angular';
// import moment from 'moment';
import ROUTES from 'utils/routes';

const STATE_CAMPANHA_VIEW = ROUTES.MAIN.CAMPANHA.VIEW.name;
const STATE_CAMPANHA_LIST = ROUTES.MAIN.CAMPANHA.LIST.name;
const STATE_ABA_PENDENCIAS = ROUTES.MAIN.AVALIACAO.VIEW.PENDENCIAS.name;
const STATE_ABA_RESUMO = ROUTES.MAIN.AVALIACAO.VIEW.RESUMO.name;
const STATE_ABA_MENSAGENS = ROUTES.MAIN.AVALIACAO.VIEW.MENSAGENS.name;
const STATE_ABA_PARTICIPANTES = ROUTES.MAIN.AVALIACAO.VIEW.PARTICIPANTES.name;

/* @ngInject */
export default function avaliacaoViewCtrl(
	$state,
	$scope
) {
	const $ctrl = this;
	$ctrl.object = $state.object;
	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;
	$ctrl.subtitulo = $state.current.data.subtitulo;
	$ctrl.model = $scope.model = $state.model;
	//$ctrl.tabs = TABS;

	const load = () => {
		$ctrl.model.tabs[2].title = `Pendencia (${$ctrl.model.avaliacao.pendencias.length})`;
		//loadResumo();

		// NOTE: Necessário para selecionar a aba do controller atual quando o usuário entrar na página via URL
		// Há um bug no md-tab que não permite o seu uso em conjunto com o ui-sref.
		// O issue no repositório do AngularJS sobre este problema está como "corrigido", mas a correção
		// indicada não funcionou para nós, inicialmente. Rever ao final da refatoração.
		// Vide: https://github.com/angular/material/issues/5351
		$scope.$on('selectedIndexTab', function(event, index) {
			$ctrl.selectedIndexTab = index;
		});
	};

	$ctrl.onTabSelected = tab => {
		if (tab.stateName === STATE_ABA_PENDENCIAS) {
			$state.go(tab.stateName, { pendencias: $ctrl.object.pendencias });
		} else if (tab.stateName === STATE_ABA_PARTICIPANTES) {
			$state.go(tab.stateName, {
				participantes: $ctrl.object.participantes,
			});
		} else if (tab.stateName === STATE_ABA_RESUMO) {
			$state.go(tab.stateName, {
				avaliacao: $ctrl.object
			});
		} else if (tab.stateName === STATE_ABA_MENSAGENS) {
			$state.go(tab.stateName, {
				avaliacao: $ctrl.object
			});
		} else {
			$state.go(tab.stateName);
		}
	};

	/* $ctrl.updatePendenciasTab = (tab) => {
		tab.title = `Pendencia (${$ctrl.model.avaliacao.pendencias.length})`;
	}; */

	//TODO: Cachear a avaliação para não chamar o backend toda vez
	$ctrl.abaResumo = () => {
		$state.go(STATE_ABA_RESUMO);
	};

	$ctrl.abaPendencias = () => {
		$state.go(STATE_ABA_PENDENCIAS);
	};

	$ctrl.abaMensagens = () => {
		$state.go(STATE_ABA_MENSAGENS);
	};

	$ctrl.ajudaResumo = modo => {
		import('./avaliacao.help').then(help => help.showAjudaResumo(modo));
	};

	$ctrl.back = () => {
		$state.go(STATE_CAMPANHA_VIEW, {
			objectId: $state.params.campanhaId,
		});
	};

	$ctrl.backToList = () => {
		$state.go(STATE_CAMPANHA_LIST);
	};

	load();
}
