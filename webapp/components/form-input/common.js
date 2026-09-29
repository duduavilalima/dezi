import 'model/TipoPendencia';

const DEFAULT_MESSAGES = {
	required: 'VALIDATION.REQUIRED',
	minlength: 'VALIDATION.MINLEN',
	maxlength: 'VALIDATION.MAXLEN'
};

function resetControlAndParents(control) {
	const children = control.$getControls && control.$getControls();

	const isPristine = !children || children.every(child => child.$pristine);

	if (isPristine) {
		control.$setPristine();
		control.$$parentForm && resetControlAndParents(control.$$parentForm);
	}
}

class LabeledInputControllerBase {
	static createBaseConfig() {
		return {
			// bindToController: false,
			// scope: {},
			bindings: {
				name: '@',
				label: '@',
				translate: '<?',
				translateLabel: '<?',
				modelOptions: '<',
				nullable: '<?',

				value: '<',
				key: '<?',
				minlength: '<?',
				maxlength: '<?',
				messages: '<?',
				readOnly: '<?',
				required: '<?',

				onChange: '&?',
			},
		};
	}

	constructor(injections) {
		this._backup = {};
		this._translateValues = {};
		this._injections = injections;
		this._ready = false;

		this.translateLabel = true;
		this.nullable = false;
		this.minlength = 0;
		this.maxlength = 255;
		this.translate = false;
		this.readOnly = false;
		this.required = false;
	}

	$onInit() {
		this._initialize();
		this._validateInit();
	}

	reset() {
		const { $scope } = this._injections;

		if (this.value !== this._backup.initialValue) {
			this.setValue(this._backup.initialValue);
		}

		resetControlAndParents($scope[this.name].input);
	}

	setValue(newValue) {
		if (newValue !== this.value) {
			this._backup.previousValue = this.value;
			this.value = newValue;
			this._onChange(this.value);
		}
	}

	_onReset($event) {
		this._resetOnEscapeKey($event);
	}

	/**
	 * Callback interno a ser chamado APÓS a mudança de `this.value`.
	 *
	 * Este método de evento NÃO modifica `this.value`. Para alterá-lo, use `this.setValue()`.
	 *
	 * O uso deste método é mais prominente como eventos reativos a mudanças realizadas por
	 * um `ng-model` nos inputs internos deste componente. Ou seja, no momento da chamada à este
	 * método, é esperado que o valor de `this.value` já tenha sido alterado via _data binding_.
	 */
	_onChange(newValue) {
		if (this._ready) {
			if (this.onChange && !this.readOnly) {
				const previousValue = this.value;
				this.onChange({
					name: this.name,
					value: newValue,
					previousValue: this._backup.previousValue,
					initialValue: this._backup.initialValue,
					controller: this,
				});

				this._backup.previousValue = previousValue;

				if (previousValue !== this.value) {
					this._onTranslateValue({ previousValue });
				}
			}
		}
	}

	_onTranslateValue() {
		this._translateValue();
	}

	_translateValue() {
		const { translate, value } = this;
		const { $translate } = this._injections;

		if (translate && value) {
			this.value = undefined;
			$translate(value)
				.then(value => this.value = value)
				.catch(err => this.value = err);
		}
	}

	_initialize() {
		const { $scope, $mdUtil } = this._injections;

		this._backup.initialValue = this.value;
		this._backup.previousValue = this.value;
		this._translateValue();

		this.messages = {
			...DEFAULT_MESSAGES,
			...this.messages,
		};

		this._translateValues = {
			...this._translateValues,
			minlen: this.minlength,
			maxlen: this.maxlength,
		};

		if (!this.key) {
			this.key = 'id';
		}

		if (!this.modelOptions) {
			this.modelOptions = {};
		}

		if (this.key) {
			this.modelOptions.trackBy = `$value.${this.key}`;
		}

		this._ready = false;
		$mdUtil.nextTick(() => {
			const { input } = $scope[this.name];
			resetControlAndParents(input);
			this._ready = true;
		});
	}

	_validateInit() {
		if (!this.name) {
			throw new Error('Attribute name is required for form and its children');
		}
	}

	_resetOnEscapeKey($event) {
		if ($event.keyCode == 27) {
			this.reset();
		}
	}

	_getFormErrors() {
		const { $scope } = this._injections;
		if (this.name) {
			return $scope[this.name].$error;
		}

		return '';
	}
}

export {
	LabeledInputControllerBase,
};
