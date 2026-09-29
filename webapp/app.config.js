import angular from 'angular';
import moment from 'moment';

import AppConfig from '$env';

/* @ngInject */
function initStates(
	$rootScope,
	$state,
	$stateParams,
	$transitions
) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	const roles = ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH'];
	$transitions.onError({}, transition => {
		const error = transition.error();

		if (error.detail && error.detail.status == 401) {
			//TODO Guardar o estado para retornar após o login
			$state.go('login');
		}

		//TODO Ir para uma página de erro caso não seja 401
	});

	$transitions.onBefore({ entering: 'login' }, transition => {
		// Se já estiver logado vai para o dashboard
		//TODO Tem que verificar as roles para direcionar para a página correta
		if ($rootScope.user && $rootScope.user.id) {
			const isAdmin = roles.some(role => roles.indexOf(role.authority) >= 0);
			if(isAdmin) {
				return transition.router.stateService.target('main.acompanhamentos');
			} else {
				return transition.router.stateService.target('main.preenchimento.avaliacoes');
			}
		}
	});
}

/* @ngInject */
function configStates($stateProvider, $urlRouterProvider, $locationProvider) {
	$locationProvider.html5Mode(false);

	$urlRouterProvider
		.when('/404', '/notFound')
		.otherwise('/login');
}

/* @ngInject */
function resourcesConfig($resourceProvider) {
	var newActions = {
		delete: {
			method: 'DELETE'
		},
		update: {
			method: 'PUT'
		},
		all: {
			method: 'GET',
			params: { action: 'all' },
			isArray: true
		},
		page: {
			method: 'POST',
			params: { action: 'page' }
		},
		search: {
			method: 'POST',
			params: { action: 'search' },
			isArray: true
		},
		transition: {
			method: 'PUT',
			params: { action: 'transition' }
		},
		activate: {
			method: 'PUT',
			params: { action: 'activate' }
		},
		deactivate: {
			method: 'PUT',
			params: { action: 'deactivate' }
		},
	};

	angular.extend($resourceProvider.defaults.actions, newActions);
}

/* @ngInject */
function httpConfig($httpProvider) {
	/* @ngInject */
	$httpProvider.interceptors.push(function ($q, $rootScope, ErrorProcessor) {
		$rootScope.loadingProgress = false;
		$rootScope.loadingCount = 0;

		const increaseCount = () => {
			$rootScope.loadingCount++;
			$rootScope.loadingProgress = true;
		};

		const decreaseCount = () => {
			$rootScope.loadingCount--;
			if ($rootScope.loadingCount < 0) $rootScope.loadingCount = 0;
			$rootScope.loadingProgress = $rootScope.loadingCount > 0;
		};

		const RESTSERVER = '${restServer}/';
		const isRestCall = (url) => url.startsWith(RESTSERVER);

		const isValidationError = (response) => /^4/.test(response.status) && Array.isArray(response.data) && response.data[0].typeDesc === 'VALIDATION';

		return {
			'request': function (config) {
				if (isRestCall(config.url)) {
					config.url = config.url.replace(RESTSERVER, AppConfig.restServer);
					if ($rootScope.authToken) {
						config.headers['X-Auth-Token'] = $rootScope.authToken;
					}
				}

				//TODO Só colocar quando for REST?
				increaseCount();

				return config || $q.when(config);
			},
			'requestError': function (rejection) {
				decreaseCount();

				return $q.reject(rejection);
			},
			'response': function (response) {
				decreaseCount();

				return response || $q.when(response);
			},
			'responseError': function (response) {
				decreaseCount();
				const url = response.config && response.config.url;

				if (url && isRestCall(url)) {
					ErrorProcessor.responseError(response);
				}

				if(isValidationError(response)) {
					ErrorProcessor.validationError(response);
				}

				return $q.reject(response);
			},
		};
	});

	// Evita o popup de login/senha do navegador
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.defaults.withCredentials = true;
}

/* @ngInject */
function fuseConfig(
	$mdDateLocaleProvider,
	$ariaProvider,
	fuseConfigProvider,
	msScrollConfigProvider
) {
	// ng-aria configuration
	$ariaProvider.config({
		tabindex: false
	});

	// Fuse theme configurations
	fuseConfigProvider.config({
		'disableCustomScrollbars': false,
		'disableCustomScrollbarsOnMobile': true,
		'disableMdInkRippleOnMobile': true
	});

	// msScroll configuration
	msScrollConfigProvider.config({
		wheelPropagation: true
	});

	$mdDateLocaleProvider.formatDate = function (date) {
		if (angular.isDate(date)) {
			return moment(date).format('DD/MM/YYYY');
		} else {
			return null;
		}
	};
}

/* @ngInject */
function ariaConfig($mdAriaProvider) {
	// Evita alertas incorretos devido ao uso do angular-translate em atributos 'aria-label'
	$mdAriaProvider.disableWarnings();
}

/* @ngInject */
function toastrConfig(toastrConfig) {
	angular.extend(toastrConfig, {
		autoDismiss: false,
		containerId: 'toast-container',
		maxOpened: 0,
		newestOnTop: true,
		positionClass: 'toast-top-right',
		preventDuplicates: false,
		preventOpenDuplicates: true,
		allowHtml: false,
		closeButton: true,
		closeHtml: '<button>&times;</button>',
		extendedTimeOut: 10000,
		iconClasses: {
			error: 'toast-error',
			info: 'toast-info',
			success: 'toast-success',
			warning: 'toast-warning'
		},
		messageClass: 'toast-message',
		onHidden: null,
		onShown: null,
		onTap: null,
		progressBar: false,
		tapToDismiss: true,
		timeOut: 10000,
		titleClass: 'toast-title',
		toastClass: 'toast'
	});
}

/* @ngInject */
function authStateConfig($rootScope, $cookies, UserService) {
	// Verifica no bootstrap se já tem login
	const authToken = $cookies.get('authToken');
	UserService.login(authToken);
}

export {
	initStates,
	configStates,
	resourcesConfig,
	httpConfig,
	fuseConfig,
	ariaConfig,
	toastrConfig,
	authStateConfig
};
