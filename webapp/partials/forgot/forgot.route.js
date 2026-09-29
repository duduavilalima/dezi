import angular from 'angular';

import stateUtils from 'utils/state.utils';

import publicService from 'services/public.service';

export default angular.module('routes.forgot', [
	stateUtils,
	publicService
])
	.config(routeConfig)
	.name;

/* @ngInject */
function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./forgot.module');

	$lazyStateProvider
		.state({
			moduleLoader,
			stateName: 'forgot-password',
			url: '/forgot-password/{objectId:int}',
			params: {
				objectId: null
			},
			templateUrl: 'forgot-password.html',
			controller: 'ForgotController',
			controllerAs: '$ctrl',
		});
}
