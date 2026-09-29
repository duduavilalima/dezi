import angular from 'angular';

import stateUtils from 'utils/state.utils';

import userService from 'services/user.service';

import './main.html';

export default angular.module('routes.main', [stateUtils, userService])
	.config(routeConfig)
	.name;

/* @ngInject */
function routeConfig($lazyStateProvider) {
	/* @ngInject */
	const listRoles = (UserService) => UserService.roles().$promise;

	$lazyStateProvider
		.state({
			stateName: 'main',
			abstract: true,
			url: '',
			templateUrl: 'main.html',
			resolve: {
				userRoles: listRoles
			}
		});
}
