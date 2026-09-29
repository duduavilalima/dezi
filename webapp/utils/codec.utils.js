import angular from 'angular';

import translateInterpolation from 'services/translateInterpolation';

export default angular.module('utils.codec', [translateInterpolation])
	.factory('CodecUtils', codecUtilFactory)
	.name;

/* @ngInject */
function codecUtilFactory() {
	const service = {};

	service.decode = (data) => {
		if (data) {
			return JSON.parse(window.atob(data));
		}
		
		return '';
	};

	service.encode = (data) => {
		if (data) {
			return window.btoa(JSON.stringify(data));
		}

		return '';
	};

	service.objectToStringEncode = (data) => {
		if (data) {
			return service.encode(data);
		}

		return '';
	};

	service.stringEncodeToObject = (data) => {
		if (data) {
			return service.decode(data);
		}

		return {};
	};

	service.isEmpty = (object) => {
		if (!object) {
			return true;
		}

		return JSON.stringify(object) !== JSON.stringify({});
	};

	service.isNotEmpty = (object) => {
		return service.isEmpty(object);
	};

	return service;
}
