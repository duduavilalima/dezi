import angular from 'angular';

import './form-table.html';

export default angular.module('comps.formTable', [])
	.filter('formTable__translateFieldValue', translateFieldFilter)
	.component('formTable', {
		templateUrl: 'form-table.html',
		controller: formTableController,
		transclude: false,
		bindings: {
			source: '<',
			fields: '<',
			initialOrder: '@?',
			filter: '<',
			conf: '<'
		}
	})
	.name;

function formTableController($state, $filter) {
	const $ctrl = this;

	const defaultConf = {
		limit: 10,
		limitOptions: [ 5, 10, 15, 20, 25, 30, 40, 50 ]
	};

	const typeDefinitions = {
		any: {},
		localdate: {
			filter: '!localdate',
		},
		situacao: {
			filter: '!situacao',
			translate: true,
			class: (object, { field }) => {
				const value = $ctrl.resolveFieldValue(field, object);
				return {
					'green-bg': value === 'ATIVA',
					'red-bg' : value !== 'ATIVA',
					'estilo_situacao': true
				};
			},
			align: 'center',
		},
		situacaoFuncional: {
			filter: '!situacaoFuncional',
			translate: true,
			class: (object, { field }) => {
				const value = $ctrl.resolveFieldValue(field, object);
				return {
					'green-bg': value === 'EM_ATIVIDADE',
					'red-bg' : value !== 'EM_ATIVIDADE',
					'estilo_situacao': true
				};
			},
			align: 'center',
		},
	};

	const changeState = (object, state) => $state.go($state.current.data.baseState + '.' + state, { objectId: object.id });

	$ctrl.$onInit = () => {
		$ctrl.conf = { ...defaultConf, ...$ctrl.conf };

		$ctrl.page = 1;

		$ctrl.order = $ctrl.initialOrder || $ctrl.fields[0].name;

		$ctrl.fieldsWithOrder = new Array();
		$ctrl.fields.filter(f => ( !('ordenar' in f) || f.ordenar )).forEach(f => { $ctrl.fieldsWithOrder.push(f.name); }  );
	};

	$ctrl.$onChanges = (changesObj) => {
		if (changesObj.filter) {
			const value = changesObj.filter.currentValue;
			$ctrl.normFilter = value && value.toLowerCase();
		}

		if (changesObj.fields) {
			changesObj.fields.currentValue.forEach(field => {
				if (typeof field.type === 'string') {
					const typeDefinition = typeDefinitions[field.type];

					if (!typeDefinition) {
						throw new Error(`Nome de template desconhecido: "${field.type}"`);
					}

					assignDefaults(field, typeDefinition);
				} else if (field.name == 'situacao') {
					assignDefaults(field, typeDefinitions['situacao']);
				}
			});
		}
	};

	$ctrl.resolveFieldValue = (field, object) => {
		let value;

		if (field.value) {
			value = field.value(object, { field });
		} else {
			value = object;
			const keys = field.name.split('.');
			while (keys.length > 0) {
				if (typeof value !== 'object') break;
				const key = keys.shift();
				value = value[key];
			}
		}

		return value;
	};

	$ctrl.filterFieldValue = (value, field, object) => {
		if (value === undefined) return;

		const applyFilter = (value, args) => {
			const filterName = args[0];
			const filterArgs = args.slice(1);
			return $filter(filterName)(value, ...filterArgs);
		};

		if (typeof field.filter === 'function') {
			value = field.filter(value, { object, field, $filter });
		} else if (Array.isArray(field.filter)) {
			if (Array.isArray(field.filter)) {
				field.filter.forEach(args => {
					value = applyFilter(value, args);
				});
			} else {
				value = applyFilter(value, field.filter);
			}
		} else if (typeof field.filter === 'string') {
			switch (field.filter) {
			case '!localdate': value = $filter('date')(value, 'dd/MM/yyyy'); break;
			case '!situacao': case '!situacaoFuncional': value = `SITUACAO.${value}`; break;
			default: value = $filter(field.filter)(value); break;
			}
		}

		return value;
	};

	$ctrl.fieldValue = (field, object) => {
		let value = $ctrl.resolveFieldValue(field, object);
		value = $ctrl.filterFieldValue(value, field, object);
		return value;
	};

	$ctrl.hasLink = (field) => field.link || field.state;

	$ctrl.handleClick = (field, object) => {
		if (field.state) {
			changeState(object, field.state);
		} else if (field.link) {
			field.link(object);
		} else if (field.click) {
			field.click(object);
		}
	};

	$ctrl.handleClickHeader = (field) => {
		if (field.linkHeader) {
			field.linkHeader();
		} else if (field.clickHeader) {
			field.clickHeader();
		}
	};

	$ctrl.fieldClass = (field, object) => {
		if (field.class) return field.class(object, { field });

		return { clickable: field.click };
	};

	$ctrl.filterPredicate = (object) => $ctrl.fields.some(field => {
		const value = $ctrl.fieldValue(field, object);
		const strValue = String(value).toLowerCase();
		return strValue.includes($ctrl.normFilter);
	});
}

/* @ngInject */
function translateFieldFilter($translate) {
	const cache = {};

	const filter = (fieldValue, field) => {
		if (fieldValue in cache) {
			return cache[fieldValue];
		}

		if (!field || !field.translate) {
			return fieldValue;
		}

		$translate(fieldValue)
			.then(translation => cache[fieldValue] = translation )
			.catch(() => cache[fieldValue] = fieldValue);

		return '';
	};

	filter.$stateful = true;
	return filter;
}

function assignDefaults(to, from) {
	Object
		.keys(from)
		.forEach(key => to[key] = to[key] !== undefined
			? to[key]
			: from[key]);
	return to;
}
