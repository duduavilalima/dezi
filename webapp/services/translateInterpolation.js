import angular from 'angular';
import translate from 'angular-translate';
import translatePartial from 'angular-translate-loader-partial';

export default angular.module('services.translateInterpolation', [translate, translatePartial])
	.service('$translateInterpolation', translateInterpolationService)
	.name;

export {
	TranslateInterpolatorError,
};

/* @ngInject */
function translateInterpolationService(
	$translate,
) {
	/**
	 * Traduz uma String, Array ou objeto contendo `translationId`s.
	 *
	 * O retorno da função depende do valor de entrada `initialValue`:
	 *
	 * - Se `initialValue` for uma `string` ou `Promise<string>`,
	 *   é retornada uma `Promise<string>` contendo a tradução desta string.
	 * - Se `initialValue` for uma `Array` ou `Promise<Array>`,
	 *   é retornada uma `Promise<string>` contendo o resultado da tradução
	 *   da chave de tradução em `array[0]` com interpolações obtidas através
	 *   das traduções dos elementos restantes da array:
	 *   - Se `array[1]` for um objeto, é retornado o equivalente a
	 *     `$helper.values(array[1]).then(interp => $translate(array[0], interp))`.
	 *   - Se `array[1]` NÃO for um objeto, é retornado o equivalente a
	 *     `$translate(array.slice(1)).then(interp => $translate(array[0], interp))`.
	 * - Se `initialValue` for um objeto tradicional (i.e. não-nulo, não-função,
	 *   não-Array, etc), é retornado o equivalente a tradução de cada um de seus
	 *   valores, seguindo a semântica desta função. Chaves que comecem com `$`
	 *   são ignoradas para a tradução, mas seus valores serão resolvidos (caso sejam
	 *   `Promises`).
	 * - Se `initialValue` for uma função
	 *
	 * Exemplo:
	 * ```js
	 * // Considere:
	 * const translations = {
	 *     "TR_ID1": "Olá, mundo!",
	 *     "TR_ID2": "Olá {{ITR_ID_1}}, {{ITR_ID_2}}?",
	 *     "TR_ID3": "Olá, {{quem}}!",
	 *     "TR_ID4": "Pessoa",
	 *     "ITR_ID_1": "Mundo",
	 *     "ITR_ID_2": "como vai"
	 * };
	 * const values = $translateInterpolation({
	 *     // Equivalente a `$translate('TR_ID1')`
	 *     message1: 'TR_ID1',
	 *     // Equivalente a `$translate('TR_ID2', { 'ITR_ID_1': "...", 'ITR_ID_1': "..." })`
	 *     message2: ['TR_ID2', 'ITR_ID_1', 'ITR_ID_1'],
	 *     // Equivalente a `$translateHelper.values({ message4: 'TR_ID4' }).then(ip => $translate('TR_ID3', ip))`
	 *     message3: ['TR_ID3', { quem: 'TR_ID4' }],
	 *     // Exemplo de uso do `$skip`, para evitar a tradução de determinadas chaves do objeto
	 *     // Note que `{ key: true }` é equivalente a `{ key: 'key' }` (para fins de evitar repetição)
	 *     message4: ['TR_ID2', { ITR_ID_2: true, ITR_ID_1: 'Alguém', $skip: ['ITR_ID_1'] }],
	 * });
	 * expect(values).toMatchObject({
	 *     message1: "Olá, mundo!",
	 *     message2: "Olá Mundo, como vai?",
	 *     message3: "Olá, Pessoa!",
	 *     message4: "Olá Alguém, como vai?",
	 * });
	 * ```
	 *
	 * @param initialValue O valor a ser traduzido.
	 * @returns Um valor traduzido, de tipo objeto, array ou string (a depender do
	 * tipo inicial de `interpolationObject`).
	 */
	const values = (initialValue) => {
		// TODO Tratar casos de circularidade (talvez?)
		function resolveAny(anyValue, parents = []) {
			if (parents.includes(anyValue)) {
				const errorContext = {
					initialValue,
					parents,
					message: 'Dependência circular detectada! Favor, rever seu objeto de tradução, e remover duplicidades de valores!',
					value: anyValue,
				};
				throw new TranslateInterpolatorError(errorContext);
			}

			if (Array.isArray(anyValue)) {
				return resolveArray(anyValue, parents);
			} else if (typeof anyValue === 'function') {
				return resolveFunction(anyValue, parents);
			} else if (typeof anyValue === 'object' && anyValue !== null) {
				return resolveObject(anyValue, parents);
			} else if (typeof anyValue === 'string') {
				return resolveString(anyValue);
			}

			throw new TranslateInterpolatorError(`Invalid value type "${typeof anyValue}" (constructor: ${anyValue.constructor})`);
		}

		function resolveFunction(func, parents) {
			const realValue = func();
			return resolveAny(realValue, [...parents, func]);
		}

		function resolveString(string) {
			return $translate(string);
		}

		function resolveArray(array, parents) {
			return Promise.all(array.map(elem => Promise.resolve(elem)))
				.then(resolvedArray => {
					const translateId = resolvedArray[0];
					const translateValues = resolvedArray.slice(1);

					const translateWithValues = interpolationValues =>
						$translate(translateId, interpolationValues);

					if (typeof translateValues[0] === 'object') {
						return Promise.resolve(translateValues[0])
							.then(object => resolveObject(object, [...parents, array]))
							.then(translateWithValues);
					}

					return $translate(translateValues)
						.then(translateWithValues);
				});
		}

		function resolveObject(object, parents) {
			const skip = object.$skip || [];
			const skipPromise = Promise.resolve(skip);

			const flatten = object => skipPromise
				.then($skip => Object.keys(object)
					.map(key => ({
						$skip,
						key,
						value: object[key],
					}))
					.filter(({ key, value }) => $skip.includes(key) || !!value)
				);

			const resolveFlattenedValues = (array) => Promise.all(
				array.map(item => Promise.resolve(item.value)
					.then(value => value === true ? item.key : value)
					.then(value => item.key.startsWith('$') || item.$skip.indexOf(item.key) >= 0
						? value
						: resolveAny(value, [...parents, object]))
					.then(value => {
						item.value = value;
						return item;
					})
				)
			);

			const reduce = flattenedObject =>
				flattenedObject.reduce((accum, elem) => {
					accum[elem.key] = elem.value;
					return accum;
				}, {});

			return Promise.resolve(object)
				.then(flatten)
				.then(resolveFlattenedValues)
				.then(reduce);
		}

		return Promise
			.resolve(initialValue)
			.then(resolveAny);
	};

	return values;
}

class TranslateInterpolatorError extends Error {
	constructor(values = {}, message = '') {
		super(message || values.message || '');
		Object.assign(this, values);
		this.status = this.constructor.name;
		this.statusText = this.message;
	}
}
