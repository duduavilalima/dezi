import angular from 'angular';

import stateUtils from 'utils/state.utils';
import { TipoSexoInfo } from '../model/TipoSexo';

//import publicService from 'services/public.service';

export default angular.module('routes.homeoffice', [
	stateUtils,
	/* publicService */
])
	.config(routeConfig)
	.config(translateConfig)
	.name;

/* @ngInject */
function translateConfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('homeoffice');
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./homeoffice.module');

	/* @ngInject */
	const getTipoSexo = () => {
		const situacaoList = [...TipoSexoInfo.names];
		return situacaoList;
	};

	/* @ngInject */
	/* function listEmpresas(EmpresaPublic) {
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
	} */

	/* @ngInject */
	//const translateMessages = ($translate) => $translate(['ERROR.LOGINERROR', 'ERROR.LOGINLOCKERROR']);

	$lazyStateProvider
		.state({
			stateName: 'homeoffice',
			moduleLoader,
			url: '/cadastro',
			templateUrl: 'homeoffice.form.html',
			controller: 'HomeOfficeFormCtrl',
			controllerAs: '$ctrl',
			resolve: {
				comboTipoSexo: getTipoSexo,
			}
		});
	/* .state({
			stateName: 'login.empresa',
			url: '/{empresaId:int}',
			template: '<div></div>',
			controller: 'LoginController as vm',
		}); */
}
