(function() {
	'use strict';

	angular
		.module('app.others', [])

		.constant('moduleUrl', 'partials/pages/others')

		.config(config);

	function config($translatePartialLoaderProvider, moduleUrl, $stateProvider) {
		$translatePartialLoaderProvider.addPart(moduleUrl);

		/* ===== Access is denied ===== */
		$stateProvider
		.state('accessisdenied', {
			url: '/accessisdenied',
			templateUrl: 'partials/pages/others/accessisdenied.html',
		})
	};

})();
