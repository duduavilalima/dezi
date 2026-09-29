import angular from 'angular';

import stateUtils from 'utils/state.utils';

import publicService from 'services/public.service';

export default angular.module('routes.login', [
	stateUtils,
	publicService
])
	.config(routeConfig)
	.config(translateConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('login');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./login.module');

	/* @ngInject */
	function listEmpresas(EmpresaPublic) {
		const loadEmpresas = (empresas) => {
			if (empresas && empresas.length > 0) {
				return empresas;
			}

			return EmpresaPublic.empresas().$promise
				.catch(error => {
					console.error('Erro ao buscar todas as empresas!', error);
					return [];
				});
		};

		return Promise.resolve().then(loadEmpresas);
	}

	/* @ngInject */
	const translateMessages = ($translate) => $translate(['ERROR.LOGINERROR', 'ERROR.LOGINLOCKERROR', 'ERROR.IPLOGINLOCKERROR']);

	$lazyStateProvider
		.state({
			stateName: 'login',
			moduleLoader,
			url: '/login',
			templateUrl: 'login.html',
			controller: 'LoginController as vm',
			resolve: {
				listaEmpresas: listEmpresas,
				translations: translateMessages,
			}
		})
		.state({
			stateName: 'login.empresa',
			url: '/{empresaId:int}',
			template: '<div></div>',
			controller: 'LoginController as vm',
		});
}
