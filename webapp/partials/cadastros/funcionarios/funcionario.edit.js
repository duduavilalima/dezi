/* @ngInject */
export default function funcionarioEditController(
	navigator,
	colaborador,

	$scope,
	$state,
	$notify,

	Funcionario,
	FotoResource,
) {
	const $ctrl = this;
	const {
		success: notifySuccess,
		restError: notifyRestError,
	} = $notify.atScope($scope).tr;

	const {
		mode,
	} = $state.current.data;

	function init() {
		$ctrl.object = colaborador;
	}

	$ctrl.goNew = navigator.goNew;
	$ctrl.goList = navigator.goList;
	$ctrl.goView = (id = $ctrl.object.id) => navigator.goView(id);
	$ctrl.goEdit = (id = $ctrl.object.id) => navigator.goEdit(id);

	$ctrl.isViewMode = () => mode === 'VIEW';
	$ctrl.isCreateMode = () => mode === 'NEW';
	$ctrl.isEditMode = () => mode === 'EDIT';

	$ctrl.save = () => {
		const { object } = $ctrl;
		const promise = $ctrl.isCreateMode()
			? Funcionario.save(null, object).$promise
			: Funcionario.update({ id: object.id }, object).$promise;

		return promise
			.then(funcionario => {
				// Devemos salvar a foto APÓS a criação do usuário.
				// $ctrl.foto só será não-nulo se a tela estiver em modo de criação.
				const foto =  {
					...$ctrl.foto,
					funcionario
				};

				if ($ctrl.foto) {
					return FotoResource.save(null, foto)
						.$promise
						.then(() => funcionario);
				}

				return funcionario;
			})
			.then(({ id }) => {
				if ($ctrl.isCreateMode()) {
					notifySuccess('FUNCIONARIO.MSGSALVO');
				} else {
					notifySuccess('FUNCIONARIO.MSGATUALIZADO');
				}

				return id;
			})
			.then($ctrl.goView)
			.catch(err => Promise.resolve(err)
				.then(notifyRestError)
				.then(() => $ctrl.back())
			);
	};

	$ctrl.back = (id = $ctrl.object.id) => $ctrl.isEditMode() ? $ctrl.goView(id) : $ctrl.goList();
	$ctrl.cancel = () => $ctrl.back();

	$ctrl.onChangeFoto = (foto) => {
		if ($ctrl.isCreateMode()) {
			$ctrl.foto = foto;
		}
	};

	init();
}
