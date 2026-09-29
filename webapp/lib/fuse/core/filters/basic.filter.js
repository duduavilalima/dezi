function toTrustedFilter($sce) {
	return function(value) {
		return $sce.trustAsHtml(value);
	};
}

function htmlToPlainTextFilter() {
	return function(text) {
		return String(text).replace(/<[^>]+>/gm, '');
	};
}

function nospaceFilter() {
	return function(value) {
		return !value ? '' : value.replace(/ /g, '');
	};
}

function humanizeDocFilter() {
	return function(doc) {
		if (!doc) {
			return;
		}

		if (doc.type === 'directive') {
			return doc.name.replace(/([A-Z])/g, function($1) {
				return '-' + $1.toLowerCase();
			});
		}

		return doc.label || doc.name;
	};
}

export {
	toTrustedFilter,
	htmlToPlainTextFilter,
	nospaceFilter,
	humanizeDocFilter
};
