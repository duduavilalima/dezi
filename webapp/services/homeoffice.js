import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.homeoffice', [ngResource])
	.factory('HomeOffice', homeOfficeFactory)
	.name;

function homeOfficeFactory($resource) {
	return $resource('${restServer}/homeofficebrasil/', null, {
		save: {
			method: 'POST',
		},
	});
}