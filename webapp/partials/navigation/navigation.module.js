import angular from 'angular';

import msNavigation from 'fuse/core/directives/ms-navigation/ms-navigation.directive';

import navigationController from './navigation.controller';
import './navigation.html';

export default angular.module('app.navigation', [msNavigation])
	.config(navigationConfig)
	.controller('NavigationController', navigationController)
	.name;

function navigationConfig(msNavigationServiceProvider, $translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('navigation');
}
