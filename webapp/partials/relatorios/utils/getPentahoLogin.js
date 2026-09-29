/* @ngInject */
export default function getPentahoLogin(Pentaho, $rootScope, $http) {
	return Pentaho.login().$promise.then(loginInfo => {
		const url = `${loginInfo.server}/cookie.jsp?id=${loginInfo.sessionId}`;
		const onError = err => console.error(`Erro ao tentar efetuar login em "${loginInfo.server}"!`, err);
		const onFinish = () => loginInfo;

		if (fetch) {
			return fetch(url, {
				credentials: 'include',
				mode: 'no-cors',
			})
				.catch(onError)
				.then(onFinish);
		} else {
			console.error('`fetch` não está disponível. Tentando efetuar login no BI usando $http...');
			// FIXME Rever problemas com CORS no servidor do BI
			return $http.get(url, {
				withCredentials: true,
				cache: false,
			})
				.catch(onError)
				.then(onFinish);
		}
	}).catch(err => {
		const statusText = err.statusText || 'Erro ao acessar o sistema de BI!';
		// eslint-disable-next-line no-console
		console.log(err);
		$rootScope.$emit('notify-critical', { ...err, statusText });
	});
}
