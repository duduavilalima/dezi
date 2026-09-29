import angular from 'angular';
import ocLazyLoad from 'oclazyload';

export default angular.module('utils.state', [ocLazyLoad])
	.provider('$lazyState', lazyStateProvider)
	.name;

/* @ngInject */
function lazyStateProvider($stateProvider) {
	const resolveModule = (moduleLoader) => /* @ngInject */($ocLazyLoad) => {
		return moduleLoader().then(module => {
			$ocLazyLoad.load({ name: module.default });
			return module.default;
		});
	};

	this.state = function({ moduleLoader, stateName, resolve, ...stateData }) {
		if (moduleLoader) {
			resolve = Object.assign({
				module: resolveModule(moduleLoader)
			}, resolve);
		}

		$stateProvider.state(stateName, {
			resolve,
			...stateData
		});

		return this;
	};

	this.$get = $stateProvider.$get;
}
