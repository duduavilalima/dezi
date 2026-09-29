const DefaultElementSelectionModel = {
	onGetKey: (elem) => elem.id,
	onAddOption: (elem, { index }) => { elem.index = index; },
	onRemoveOption: (elem) => { delete elem.index; },
	onUpdateOption: (elem, { index }) => { elem.index = index; },
	onMarkSelected: (elem, { index }) => { elem.selected = { index }; },
	onUnmarkSelected: (elem) => { delete elem.selected; },
	onUpdateSelected: (elem, { index }) => { elem.selected.index = index; },
	onInitialize: () => undefined,
};

class MultiListSelection {
	constructor({
		available = [],
		selected = [],
		model = DefaultElementSelectionModel,
		onGetKey = undefined,
		optionEvents = undefined,
		selectionEvents = undefined,
		onInitialize = undefined,
		_initializing = true,
	} = {}) {
		this.available = available;
		this.selected = selected;
		this.onGetKey = onGetKey || model.onGetKey;
		this.optionEvents = optionEvents || model;
		this.selectionEvents = selectionEvents || model;
		this.onInitialize = onInitialize || model.onInitialize;
		this.initialized = false;

		if (_initializing) {
			this.initialize();
		}
	}

	initialize() {
		if (this.initialize) return;

		if (this.optionEvents.onAddOption) {
			this.available.forEach((elem, index) => this.optionEvents.onAddOption(elem, { index, selection: this }));
		}

		if (this.selectionEvents.onMarkSelected) {
			this.selected.forEach((elem, index) => this.selectionEvents.onMarkSelected(elem, { index, selection: this }));
		}

		if (this.onInitialize) {
			const result = this.onInitialize({ selection: this });
			Promise.resolve(result).then(() => this.initialized = true);
		} else {
			this.initialized = true;
		}
	}

	keyOf(elem) {
		return this._getKey(elem);
	}

	isEmpty() {
		return this.selected.length === 0;
	}

	addOption(elem) {
		const key = this._getKey(elem);
		this._ensureValidKey(key);
		let index = this._indexOf(this.available, key);

		if (index < 0) {
			index = this.available.length;
			this.available.push(elem);

			if (this.optionEvents.onAddOption) {
				this.optionEvents.onAddOption(elem, { index, selection: this });
			}

			return true;
		}

		return false;
	}

	removeOption(elem) {
		const key = this._getKey(elem);
		this._ensureValidKey(key);
		const index = this._indexOf(this.available, key);

		if (index >= 0) {
			this.remove(elem);
			this.available.splice(index, 1);
			this._doRemoveOptionAt(elem, index);
			return true;
		}

		return false;
	}

	containsOption(elem) {
		return this.indexOfOption(elem) >= 0;
	}

	indexOfOption(elem) {
		const key = this._getKey(elem);
		return this._indexOf(this.available, key);
	}

	contains(elem) {
		return this.indexOfSelection(elem) >= 0;
	}

	indexOfSelection(elem) {
		const key = this._getKey(elem);
		return this._indexOf(this.selected, key);
	}

	isAllSelected() {
		return this.selected.length >= this.available.length;
	}

	isNoneSelected() {
		return this.selected.length === 0;
	}

	add(elem) {
		const key = this._getKey(elem);
		this._ensureValidKey(key);
		let index = this._indexOf(this.selected, key);

		if (index < 0) {
			index = this.selected.length;
			this.selected.push(elem);

			if (this.selectionEvents.onMarkSelected) {
				this.selectionEvents.onMarkSelected(elem, { index, selection: this });
			}

			return true;
		}

		return false;
	}

	addAndSelect(elem) {
		if (this.add(elem)) {
			this.selected.push(elem);
			return true;
		} else {
			this.addOption(elem);
		}

		return false;
	}

	addAll() {
		this.available.forEach(elem => this.add(elem));
	}

	remove(elem) {
		const key = this._getKey(elem);
		this._ensureValidKey(key);
		const index = this._indexOf(this.selected, key);

		if (index >= 0) {
			this.selected.splice(index, 1);
			this._doRemoveAt(elem, index);
			return true;
		}

		return false;
	}

	removeAll() {
		[...this.selected].forEach((elem, index) => this._doRemove(elem, index, { all: true }));
	}

	toggleAddAll() {
		if (this.isAllSelected()) {
			this.removeAll();
		} else {
			this.addAll();
		}
	}

	_isValidKey(key) {
		return key !== undefined && key !== null;
	}

	_ensureValidKey(key) {
		if (!this._isValidKey(key)) throw new TypeError('Elemento não possui chave válida!');
	}

	_getKey(elem) {
		const key = this.onGetKey(elem);
		if (this._isValidKey(key)) return key;
		return undefined;
	}

	_indexOf(list, key) {
		if (!this._isValidKey(key)) return -1;
		return list.findIndex(sel => {
			const selKey = this._getKey(sel);
			return key === selKey;
		});
	}

	_doRemoveAt(elem, index, options = {}) {
		if (this.selectionEvents.onUnmarkSelected) {
			this.selectionEvents.onUnmarkSelected(elem, { index, selection: this });
		}

		if (this.selectionEvents.onUpdateSelected) {
			if (!options.all) {
				for (let i = index + 1; i < this.selected.length; i++) {
					this.selectionEvents.onUpdateSelected(this.selected[i], { index: i, selection: this });
				}
			}
		}
	}

	_doRemoveOptionAt(elem, index, options = {}) {
		if (this.optionEvents.onRemoveOption) {
			this.optionEvents.onRemoveOption(elem, { selection: this });
		}

		if (this.optionEvents.onUpdateOption) {
			if (!options.all) {
				for (let i = index + 1; i < this.available.length; i++) {
					this.optionEvents.onUpdateOption(this.available[i], { index: i, selection: this });
				}
			}
		}
	}
}

export default MultiListSelection;

export {
	DefaultElementSelectionModel,
};
