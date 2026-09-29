import angular from 'angular';

import { msApiProvider } from './ms-api.provider';
import { apiResolverService } from './api-resolver.service';
import { msUtilsService } from './ms-utils.service';

export default angular.module('fuse.services', [])
	.provider('msApi', msApiProvider)
	.factory('apiResolver', apiResolverService)
	.factory('msUtils', msUtilsService)
	.name;
