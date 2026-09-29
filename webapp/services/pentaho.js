import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.pentaho', [ngResource])
	.factory('Pentaho', pentahoFactory)
	.name;

function pentahoFactory($resource) {
	return $resource('${restServer}/pentaho/login', {}, {
		login: {
			method: 'GET',
			isArray: false
		}
	});
}

