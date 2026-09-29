import angular from 'angular';
import ngResource from 'angular-resource';

import menuUtils from 'utils/menu.utils';

export default angular.module('services.user', [ngResource, menuUtils])
	.factory('UserService', userFactory)
	.name;

/* @ngInject */
function userFactory($rootScope, $resource, $cacheFactory, $cookies, MenuUtils, Parametro) {
	const userCache = $cacheFactory('user');

	const res = $resource('${restServer}/user/:action', null, {
		logged: {
			method: 'GET',
			params: { action: 'logged' },
			cache: userCache
		},
		roles: {
			method: 'GET',
			params: { action: 'roles' },
			isArray: true,
			cache: userCache
		}
	});

	const userMenuVisibility = userMenuVisibilityFactory(Parametro, MenuUtils);

	function login(authToken) {
		// Coloca o token na sessão e guarda em cookie
		if ($rootScope.authToken !== authToken) {
			$rootScope.authToken = authToken;
			$cookies.put('authToken', authToken, { path: '/' });
		}

		// Limpa o cache do resoruce
		userCache.removeAll();

		// Atualiza os dados do usuário na sessão
		$rootScope.user = res.logged();
		$rootScope.userRoles = res.roles();

		// Atualiza o menu de acordo com as roles
		return Promise.all([$rootScope.user.$promise, $rootScope.userRoles.$promise])
			.then(([user, roles]) => {
				return userMenuVisibility.setMenusVisibilities(user)
					.then(() => [ user, roles ]);
			})
			.then(([user, roles]) => {
				MenuUtils.refresh(roles);
				return [ user, roles ];
			})
			.catch(err => {
				console.error('Erro buscando dados de login', err);
				return [];
			});
	}

	function logout() {
		// Remove o cookie
		$cookies.remove('authToken', '', { path: '/' });

		// Limpa a sessão
		delete $rootScope.user;
		delete $rootScope.authToken;
		delete $rootScope.userRoles;

		// Limpa o cache do resoruce
		userCache.removeAll();
	}

	return {
		logged: res.logged,
		roles: res.roles,
		clear: userCache.removeAll,
		login,
		logout
	};
}

// Vide http://git.fasolti.com.br/avd/web/issues/213 para maiores detalhes.
// SUBSTITUÍDO POR http://git.fasolti.com.br/avd/web/issues/259#note_14189 !!
// TL;DR O código a seguir é um quickfix/workaround temporário.
// Está separado do resto do serviço de usuário para que sua remoção
// seja facilitada posteriormente.
function userMenuVisibilityFactory(Parametro, MenuUtils) {
	function setMenusVisibilities(user) {
		return fetchMenuVisibilities(user)
			.then(configurations => {
				if (configurations) {
					MenuUtils.configureMenuVisibilities(configurations);
				}
			});
	}

	function fetchMenuVisibilities(user) {
		const chave = `menus.visibilidade.porEmpresaId.${user.empresa.id}`;

		return Parametro.buscaTodos(null, { chave })
			.$promise
			.then(result => {
				if (!Array.isArray(result) || result.length === 0) return;

				const parametro = result.filter(parametro => parametro.chave === chave)[0];
				if (!parametro) return;

				const valor = parametro.valor.trim();
				if (!valor) return;

				const configurations = JSON.parse(valor);

				return configurations;
			});
	}

	return {
		setMenusVisibilities,
	};
}
