import angular from 'angular';

import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';

import tipoService from 'services/tipo';
import { TipoPerfilInfo } from '../../model/TipoPerfil';
import funcionarioService from '../../../services/funcionario';

export default angular.module('routes.adm.mensagens', [stateUtils, menuUtils, tipoService, funcionarioService])
	.config(translateconfig)
	.config(routeConfig)
	.run(menuConfig)
	.name;

function translateconfig($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('administracao/mensagens');
}

/* @ngInject */
function listFuncionarios(Funcionario) {
	return Funcionario.all().$promise;
}

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./mensagem.module');

	/* @ngInject */
	const listPerfis = () => {
		return [...TipoPerfilInfo.names].filter(item => item != 'ADMINISTRADOR');
	};

	$lazyStateProvider.state({
		moduleLoader,
		stateName: 'main.adm.mensagens',
		url: '/administracao/mensagens',
		templateUrl: 'mensagemadm.html',
		controller: 'AdmMensagemCtrl',
		resolve: {
			perfis: listPerfis,
			funcionarios: listFuncionarios
		}
	});
}

function menuConfig(MenuUtils) {
	var item = {
		stateName: 'adm.mensagens',
		title: 'Mensagem Boas vindas',
		group: false,
		state: 'main.adm.mensagens',
		translate: 'NAV.MENSAGEM',
		weight: 1,
		roles: ['ROLE_ADMINISTRADOR']
	};

	MenuUtils.addMenu(item);
}
