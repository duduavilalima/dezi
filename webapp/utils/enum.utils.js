class EnumInfoBuilder {
	constructor(
		enumObject,
		{
			name = undefined,
			valueOrder = undefined,
			onAddValue = () => ({}),
		}
	) {
		this.ordinal = 0;
		this.onAddValue = onAddValue;
		this.enumObject = enumObject;
		this.detectedNames = Object.keys(enumObject);
		this.detectedValues = this.detectedNames.map(k => enumObject[k]);

		this.info = {
			name,
			enum: enumObject,
			names: [],
			values: [],
			elements: [],
			element: {},
		};

		if (typeof valueOrder === 'function') {
			valueOrder = this.detectedValues
				.map(value => ({ value, order: valueOrder(value, { enumObject }) }))
				.sort((a, b) => a - b)
				.map(elem => elem.value);
		}

		valueOrder = valueOrder || this.detectedValues;
		this._validateValuesList(valueOrder);
		valueOrder.forEach(value => this._addValue(value, this.onAddValue));
	}

	/**
	 * Determina o nome da enumeração. Usado para fins de depuração, quando necessário.
	 */
	className(name) {
		if (typeof name === 'function') {
			this.info.name = name(this.info);
		} else {
			this.info.name = name;
		}

		return this;
	}

	/**
	 * Adiciona uma propriedade ao objeto-informação de enum.
	 */
	classProperty(propertyName, assignedValue = undefined) {
		if (typeof propertyName === 'string') {
			this._addClassProperty(propertyName, assignedValue);
		} else {
			const propertyMap = propertyName;

			Object.keys(propertyMap)
				.forEach(propertyName => {
					const assignedValue = propertyMap[propertyName];
					this.classProperty(propertyName, assignedValue);
				});
		}

		return this;
	}

	/**
	 * Adiciona um método ao objeto-informação de enum.
	 */
	classMethod(propertyName, assignedValue = undefined) {
		if (typeof propertyName === 'string') {
			this.classProperty(propertyName, () => assignedValue);
		} else {
			const propertyMap = propertyName;

			Object.keys(propertyMap)
				.forEach(propertyName => {
					const assignedValue = propertyMap[propertyName];
					this.classProperty(propertyName, () => assignedValue);
				});
		}

		return this;
	}

	/**
	 * Adiciona uma ou mais propriedades a todos os elementos do enum.
	 *
	 * Opcionalmente, `propertyName` pode ser um mapa de nome de propriedade para
	 * o valor a ser atribuído.
	 *
	 * Se o valor for uma função, este valor será chamado para obter o valor real a
	 * ser atribuído. Para atribuir uma função como valor de uma propriedade, faça-a
	 * ser retornada por uma função adicional, e.g. `this.property('nome', () => () => { ... }))`
	 */
	property(propertyName, assignedValue = undefined) {
		if (typeof propertyName === 'string') {
			this.info.values.forEach(value => {
				this.uniqueProperty(value, propertyName, assignedValue);
			});
		} else {
			const propertyMap = propertyName;

			Object.keys(propertyMap)
				.forEach(propertyName => {
					const assignedValue = propertyMap[propertyName];
					this.property(propertyName, assignedValue);
				});
		}

		return this;
	}

	/**
	 * Adiciona uma ou mais propriedades a um elemento específico do enum.
	 */
	uniqueProperty(enumValue, propertyName, assignedValue) {
		const element = this.info.element[enumValue];
		const { name, value, ordinal } = element;
		const fixedProperties = { name, value, ordinal };

		if (typeof assignedValue === 'function') {
			const calculatedValue = assignedValue(element, { propertyName, enumInfo });
			element[propertyName] = calculatedValue;
		} else {
			element[propertyName] = assignedValue;
		}

		Object.assign(element, fixedProperties);

		return this;
	}

	/**
	 * Adiciona uma ou mais propriedades aos elementos presentes no mapa de
	 * elementos passado.
	 */
	uniqueProperties(valuesMap) {
		return this._addValueProperties(valuesMap, { all: false });
	}

	propertiesPerValue(valuesMap) {
		return this._addValueProperties(valuesMap, { all: true });
	}

	build() {
		Object.freeze(this.info.names);
		Object.freeze(this.info.values);
		Object.freeze(this.info.elements);
		Object.freeze(this.info.element);
		// this.info.elements.forEach(element => Object.freeze(element));
		return Object.freeze(this.info);
	}

	_addValue(value, additionalProperties) {
		const { info: enumInfo } = this;
		const name = this.enumObject[value];
		const ordinal = this.ordinal++;
		const fixedProperties = {
			value,
			name,
			ordinal,
		};

		if (typeof additionalProperties === 'function') {
			additionalProperties = additionalProperties(
				value,
				{ enumInfo, element: { ...fixedProperties } }
			) || {};
		}

		const properties = {
			...additionalProperties,
			...fixedProperties,
		};

		this.info.names.push(name);
		this.info.values.push(value);
		this.info.elements.push(properties);
		this.info.element[value] = properties;
	}

	_addValueProperties(valuesMap, { all = false }) {
		const values = Object.keys(valuesMap);
		if (all) this._validateValuesList(values);

		values.forEach(value => {
			const propertyMap = valuesMap[value];
			const propertyNames = Object.keys(propertyMap);

			propertyNames.forEach(propertyName => {
				const assignedValue = propertyMap[propertyName];
				this.uniqueProperty(value, propertyName, assignedValue);
			});
		});

		return this;
	}

	_addClassProperty(propertyName, assignedValue = undefined) {
		if (typeof assignedValue === 'function') {
			const calculatedValue = assignedValue(this.info, { propertyName });
			this.info[propertyName] = calculatedValue;
		} else {
			this.info[propertyName] = assignedValue;
		}

		return this;
	}

	_validateValuesList(values) {
		values.forEach(value => {
			if (!this.detectedValues.includes(value)) {
				throw new Error(`Value "${value}" does not belong to this enum.`);
			}
		});

		this.detectedValues.forEach(value => {
			if (!values.includes(value)) {
				throw new Error(`Value "${value}" is missing in the value ordering list.`);
			}
		});
	}
}

function enumInfo(enumObject, options) {
	if (Array.isArray(options)) {
		options = { valueOrder: options };
	}

	return new EnumInfoBuilder(enumObject, options);
}

export {
	enumInfo
};
