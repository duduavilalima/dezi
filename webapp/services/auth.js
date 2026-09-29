import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.auth', [ngResource])
	.factory('AuthService', authServiceFactory)
	.name;

function authServiceFactory($resource) {
	return $resource('${restServer}/auth/:action', {}, {
		secondaryLogin: {
			method: 'POST',
			params: { action: 'seclogin' }
		},
		forgotPassword: {
			method: 'POST',
			params: { action: 'forgotpassword' }
		},
	});
}
