import angular from 'angular';

import { altDate } from './altDate.filter';
import { filterByIds } from './filterByIds.filter';
import { filterByPropIds } from './filterByPropIds.filter';
import { toTrustedFilter, htmlToPlainTextFilter, nospaceFilter, humanizeDocFilter } from './basic.filter';

export default angular.module('fuse.filters', [])
	.filter('altDate', altDate)
	.filter('filterByIds', filterByIds)
	.filter('filterByPropIds', filterByPropIds)
	.filter('toTrusted', toTrustedFilter)
	.filter('htmlToPlaintext', htmlToPlainTextFilter)
	.filter('nospace', nospaceFilter)
	.filter('humanizeDoc', humanizeDocFilter)
	.name;
