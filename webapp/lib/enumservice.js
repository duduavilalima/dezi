import angular from 'angular';

export default angular.module('enumservice', ['ngResource'])
	.factory('EnumService', enumServiceFactory)
	.name;

function enumServiceFactory($resource) {
	return $resource('${restServer}/enumservice/simples/:pacote/:classe/:enum', null, {
		enumValues: {
			method: 'GET',
			isArray: true
		}
	});
}
