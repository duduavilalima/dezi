import angular from 'angular';

import ngAnimate from 'angular-animate';
import ngAria from 'angular-aria';
import ngCookies from 'angular-cookies';
import ngMessages from 'angular-messages';
import ngResource from 'angular-resource';
import ngSanitize from 'angular-sanitize';
import translate from 'angular-translate';
import ngMaterial from 'angular-material';
import uirouter from 'angular-ui-router';

import fuseConfig from './config/fuse-config.provider';

import fuseTheming from './theming/theming.module';
import fuseFilters from './filters/filters.module';
import fuseServices from './services/services.module';

import run from './core.run';
import config from './core.config';

export default angular.module('app.core',
	[
		ngAnimate,
		ngAria,
		ngCookies,
		ngMessages,
		ngResource,
		ngSanitize,
		translate,
		ngMaterial,
		uirouter,

		fuseTheming,
		fuseFilters,
		fuseServices
	])
	.provider('fuseConfig', fuseConfig)
	.config(config)
	.run(run)
	.name;
