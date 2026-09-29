function isIdOnly(object) {
	const keys = Object.keys(object);
	return keys.length === 1 && keys[0] === 'id';
}

function toId(value) {
	return typeof value === 'object' && value !== null
		? toId(value.id)
		: value;
}

export {
	isIdOnly,
	toId
};
