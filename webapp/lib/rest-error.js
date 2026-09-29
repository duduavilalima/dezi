/* @ngInject */
export default function errorProcessorProvider($rootScope, $translate) {
	const service = {};

	service.responseError = function(response) {
		const errorCode = response.status;
		const errorText = response.statusText;

		console.error('error', errorCode, errorText);

		$rootScope.$emit('notify-critical', response);
	};

	service.validationError = (response) => {
		const errorCode = response.data[0].code;
		//const responseEntity = response.data[0].params[0];
		//const entity = responseEntity.charAt(0).toUpperCase() + responseEntity.slice(1);

		//console.error('error', errorCode, entity);
		console.error('error', errorCode);

		$translate([errorCode]).then(translations => {
			// $rootScope.$emit('notify-error',
			// 	`${entity} ${translations[errorCode]}`,
			// 	'Atenção!'
			// );
			$rootScope.$emit('notify-error',
				`${translations[errorCode]}`,
				'Atenção!'
			);
		});
	};

	return service;
}
