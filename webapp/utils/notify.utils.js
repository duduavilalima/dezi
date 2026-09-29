import angular from 'angular';

import translateInterpolation from 'services/translateInterpolation';

export default angular.module('utils.notify', [translateInterpolation])
	.factory('$notify', NotifyFactory)
	.name;

/* @ngInject */
function NotifyFactory(
	$rootScope,
	$translateInterpolation,
) {
	const atScope = ($scope) => {
		const success = (messages) => {
			return Promise.resolve(messages)
				.then(messages => {
					if (typeof messages === 'string') {
						messages = { message: messages };
					}

					const { title, message } = messages;

					$scope.$emit(
						'notify-success',
						title || '',
						message,
					);
				});
		};

		const restError = (response) => {
			console.error(response);
			$scope.$emit('notify-critical', response);
		};

		const tr = {
			success: (messages) =>
				$translateInterpolation(messages).then(success),
			restError,
		};

		return { tr, success, restError };
	};

	return {
		atScope,
		...atScope($rootScope),
	};
}
