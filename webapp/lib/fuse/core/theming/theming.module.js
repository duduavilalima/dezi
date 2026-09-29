import angular from 'angular';

import fuseGeneratorService from './fuse-generator.service';
import fuseThemingService from './fuse-theming.service';
import fusePalettes from './fuse-palettes.constant';
import fuseThemes from './fuse-themes.constant';
import config from './fuse-theming.config';

export default angular.module('fuse.theming', [])
	.constant('fusePalettes', fusePalettes)
	.constant('fuseThemes', fuseThemes)
	.factory('fuseGenerator', fuseGeneratorService)
	.service('fuseTheming', fuseThemingService)
	.config(config)
	.name;
