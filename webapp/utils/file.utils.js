const base64ToBlobFactory = (dataURI, type) => {
	const base64Str = dataURI.replace(/\s/g, '');
	var byteString = atob(base64Str);
	var ab = new ArrayBuffer(byteString.length);
	var ia = new Uint8Array(ab);

	for (var i = 0; i < byteString.length; i++) {
		ia[i] = byteString.charCodeAt(i);
	}

	return new Blob([ab], { type: type });
};

export default base64ToBlobFactory;