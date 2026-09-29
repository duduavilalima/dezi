import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';
import getPentahoLogin from '../utils/getPentahoLogin';

export default angular.module('routes.resultados', [stateUtils, menuUtils])
	.run(menuConfig)
	.config(routeConfig)
	.name;

function menuConfig(MenuUtils) {
	var item = {
		stateName: ROUTES.MAIN.RELATORIOS.RESULTADOS.submenu,
		title: 'Resultados',
		state: ROUTES.MAIN.RELATORIOS.RESULTADOS.LIST.name,
		icon: 'icon-chart-bar',
		group: false,
		weight: 4,
		translate: 'NAV.RESULTADOS',
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./resultados.module');

	const isAdministrator = (user) => ['ADMINISTRADOR', 'GESTOR_RH'].includes(user.tipoPerfil);

	/* @ngInject */
	function loggedUser(UserService) {
		return UserService.logged().$promise;
	}

	/* @ngInject */
	function fetchCampanhas(Campanha, UserService) {
		return loggedUser(UserService).then((user) => {
			if (isAdministrator(user)) {
				return Campanha.all().$promise
					.then(campanhas => campanhas.filter(c => c.status == 'ATIVA'));
			}

			return Campanha.campanhasByEmpresaPerfil({ id: user.id }).$promise
				.then(campanhas => campanhas.filter(c => c.status == 'ATIVA'));
		});
	}

	const data = {
		titulo: 'Resultados',
		modulo: 'Relatórios',
		subtitulo: ''
	};

	const controllerAlias = '$ctrl';

	/* @ngInject */
	$lazyStateProvider
		.state({
			moduleLoader,
			abstract: true,
			controllerAlias,
			stateName: ROUTES.MAIN.RELATORIOS.RESULTADOS.name,
			url: '/relatorios/resultados',
			template: '<ui-view/>',
			data
		})
		.state({
			stateName: ROUTES.MAIN.RELATORIOS.RESULTADOS.LIST.name,
			url: '/list',
			templateUrl: 'resultados.list.html',
			controller: 'ResultadosBIListCtrl',
			controllerAs: controllerAlias,
			data,
			resolve: {
				listCampanhas: fetchCampanhas,
				user: loggedUser,
				pentahoLogin: getPentahoLogin
			}
		});
}
