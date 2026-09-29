import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import ROUTES from 'utils/routes';
import getPentahoLogin from '../utils/getPentahoLogin';

export default angular.module('routes.historicos', [stateUtils,  menuUtils])
	.run(menuConfig)
	.config(routeConfig)
	.name;

function menuConfig(MenuUtils) {
	var item = {
		stateName:  ROUTES.MAIN.RELATORIOS.HISTORICOS.submenu,
		title: 'Historicos',
		state: ROUTES.MAIN.RELATORIOS.HISTORICOS.LIST.name,
		icon: 'icon-chart-line',
		group: false,
		weight: 4,
		translate: 'NAV.HISTORICOS',
	};

	MenuUtils.addMenu(item);
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./historicos.module');

	const isAdministrator = (user) => ['ADMINISTRADOR', 'GESTOR_RH'].includes(user.tipoPerfil);

	const isGestor = (user) => 'GESTOR_AREA' == user.tipoPerfil;

	/* @ngInject */
	function loggedUser(UserService) {
		return UserService.logged().$promise;
	}

	/* @ngInject */
	function fetchFuncionarios(Funcionario, Departamento, UserService) {
		return loggedUser(UserService).then((user) => {
			if (isAdministrator(user)) {
				return Funcionario.buscar(null, { situacao: 'ATIVA' }).$promise;
			}

			if (isGestor(user)) {
				return fetchFuncionariosDepto(Departamento, user.departamento);
			}

			return Funcionario.get({ id: user.id }).$promise.then(funcionario => [ funcionario ]);
		});
	}

	//FIXME Deveria ter um método no backend para isso
	const fetchFuncionariosDepto = (Departamento, deptoPai) => {
		return Departamento.departamentosFilhos({ id: deptoPai.id }).$promise
			.then(departamentos => {
				departamentos.push(deptoPai);
				return Promise.all(
					departamentos.map(depto =>
						Departamento.funcionariosAtivosDoDepartamento({ id: depto.id }).$promise
					)
				);
			})
			.then(results => results.flat());
	};

	const data = {
		titulo: 'Históricos',
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
			stateName: ROUTES.MAIN.RELATORIOS.HISTORICOS.name,
			url: '/relatorios/historicos',
			template: '<ui-view/>',
			data
		})
		.state({
			stateName: ROUTES.MAIN.RELATORIOS.HISTORICOS.LIST.name,
			url: '/list',
			templateUrl: 'historicos.list.html',
			controller: 'HistoricosBIListCtrl',
			controllerAs: controllerAlias,
			data,
			resolve: {
				listFuncionarios: fetchFuncionarios,
				user: loggedUser,
				pentahoLogin: getPentahoLogin
			}
		});
}
