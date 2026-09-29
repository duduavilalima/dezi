/* @ngInject */

function coreConfig(
	$ariaProvider,
	$logProvider,
	fuseConfigProvider
) {
	// Enable debug logging
	$logProvider.debugEnabled(false);

	// ng-aria configuration
	$ariaProvider.config({
		tabindex: false
	});

	// Fuse theme configurations
	fuseConfigProvider.config({
		'disableCustomScrollbars': false,
		'disableCustomScrollbarsOnMobile': true,
		'disableMdInkRippleOnMobile': true
	});
}

export default coreConfig;
