/* @ngInject */
export default function loginController(
	$rootScope,
	$cookies,
	$state,
	$location,
	$stateParams,
	$cacheFactory,
	toastr,
	LoginService,
	UserService,
	listaEmpresas,
	translations,
	Funcionario,
	$scope,
	DialogTrocarSenha
) {
	const vm = this;

	vm.listaEmpresas = listaEmpresas;
	vm.hideSelectEmpresa = false;

	if ($stateParams.empresaId) {
		vm.hideSelectEmpresa = true;
		vm.empresa = listaEmpresas.find(empresa => empresa.id === $stateParams.empresaId);
		if (!vm.empresa) {
			console.error(`Não há nenhuma empresa de ID "${$stateParams.empresaId}" cadastrada para o domínio "${$location.host()}"!`);
			$scope.$emit('notify-error', 'Empresa inválida!');
			$state.go('login', {}, { reload: true});
		}
	} else {
		if (listaEmpresas.length === 1) {
			$state.go('login.empresa', { empresaId: listaEmpresas[0].id }, { reload: true });
			return;
		}
	}

	vm.empresaSelId = vm.empresa && vm.empresa.id;

	vm.forgot = function () {
		$state.go('forgot-password', { objectId: vm.empresaSelId });
	};

	vm.rememberMe = false;

	//ROLE_GESTOR_AREA e ROLE_FUNCIONARIO: Redirecionado para AVALIACOES;
	//ROLE_GESTOR_RH e ROLE_ADMINISTRADOR: Redirecionado para ACOMPANHAMENTOS;
	const gestorRoles = ['ROLE_ADMINISTRADOR', 'ROLE_GESTOR_RH'];

	const loginSuccess = function (result) {
		UserService.login(result.token).then(([user, userRoles]) => {
			//TODO Enviar para state 'main' e deixar um hook no initStates decidir para onde ir
			if(user.primeiroAcesso || user.primeiroAcesso === null) {
				vm.acaoTrocarSenha(user);
			} else if (user.preencheFormacaoEIdiomas || user.preencheFormacaoEIdiomas === null) {
				$state.go('formacoesidiomas', {
					objectId: user.id
				});
			} else if (userRoles.some(role => gestorRoles.indexOf(role.authority) >= 0)) {
				$state.go('main.acompanhamentos');
			} else {
				$state.go('main.preenchimento.avaliacoes');
			}
		});
	};

	vm.acaoTrocarSenha = (user) => {
		const confirm = (pass) => {
			const data = {
				...pass,
				funcionario: user
			};

			return Funcionario.trocarSenha(data)
				.$promise.then(() => {
					const loginParams = {
						empresaId: vm.empresaSelId,
						matricula: vm.matricula,
						senha: data.novaSenha
					};

					LoginService.login(loginParams, loginSuccess, loginError);
				}).catch((response) => {
					// console.log(response);
					throw response.data[0].code;
				});
		};

		DialogTrocarSenha.show(confirm);
	};

	const loginError = function (error) {
		if (error.status == 401) {
			toastr.error(translations['ERROR.LOGINERROR'], 'Atenção');
		} else if (error.status == 403) {
			toastr.error(translations['ERROR.LOGINLOCKERROR'], 'Atenção');
		} else if (error.status == 404) {
			toastr.error(translations['ERROR.IPLOGINLOCKERROR'], 'Atenção');
		}else {
			toastr.error(error.data, 'Error', { allowHtml: false });
		}
	};

	vm.login = () => {
		const loginParams = {
			empresaId: vm.empresaSelId,
			matricula: vm.matricula,
			senha: vm.password
		};

		LoginService.login(loginParams, loginSuccess, loginError);
	};
}
