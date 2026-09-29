import './colaborador.card.html';

import anonImage from 'img/avatars/anon.png';

import { TipoSexoInfo } from 'model/TipoSexo';
import SituacaoFuncionario, { SituacaoFuncionarioInfo } from 'model/SituacaoFuncionario';
import { TipoPerfilInfo } from 'model/TipoPerfil';
import { SituacaoInfo } from 'model/Situacao';

export default function configure(module) {
	return module.component('colaboradorEditCard', {
		templateUrl: 'colaborador.card.html',
		controller: colaboradorCardController,
		transclude: true,
		bindings: {
			name: '@',
			object: '<',
			loggedUser: '<?',
			// Se o usuário tem permissões para editar esta entidade
			canEdit: '<',
			// Página de visualização: true
			// Página de edição ou criação: false
			readOnly: '<',
			entityData: '<',
			onEdit: '&',
			onSave: '&',
			onCancel: '&',
			onChangeFoto: '&?',
			showDetalhes: '<',
			afterChangeSituacao: '&?'
		}
	});
}

/* @ngInject */
function colaboradorCardController(
	$scope,
	$notify,
	UserService,
	Unidade,
	Cargo,
	Departamento,
	FotoResource,
	Funcionario,
	DialogFuncionarioFoto,
) {
	const $ctrl = this;
	const {
		// success: notifySuccess,
		restError: notifyRestError,
	} = $notify.atScope($scope).tr;

	$ctrl.Resource = Funcionario;
	$ctrl.TipoSexoEnum = TipoSexoInfo;
	$ctrl.SituacaoFuncionarioEnum = SituacaoFuncionarioInfo;
	$ctrl.TipoPerfilEnum = TipoPerfilInfo;
	$ctrl.SituacaoEnum = SituacaoInfo;

	$ctrl.unidades = [];
	$ctrl.cargos = [];
	$ctrl.departamentos = [];

	$ctrl.cropper = {};
	$ctrl.anonImage = anonImage;

	$ctrl.$onInit = () => {
		let promise = loadFuncionarioFoto();

		if (!$ctrl.name) {
			$ctrl.name = 'formColaboradores';
		}

		if (!$ctrl.readOnly) {
			$ctrl.canEdit = false;
		}

		if (!$ctrl.loggedUser) {
			promise = promise.then(() =>
				UserService.logged()
					.$promise
					.then(loggedUser => {
						$ctrl.loggedUser = loggedUser;
					}));
		}

		promise = promise.then(() =>
			Unidade.ativas()
				.$promise
				.then(data => {
					$ctrl.unidades = data;
				}));

		promise = promise.then(() =>
			Departamento.departamentosDaEmpresaLogada()
				.$promise
				.then(data => {
					$ctrl.departamentos = data;
				}));

		promise = promise.then(() =>
			Cargo.cargosPorEmpresaUsuarioLogado()
				.$promise
				.then(data => {
					$ctrl.cargos = data;
				}));

		return promise;
	};

	$ctrl.isPeriodoRecessoRequired = () => {
		return $ctrl.object.situacaoFuncional
			&& $ctrl.object.situacaoFuncional !== SituacaoFuncionario.EM_ATIVIDADE
			&& $ctrl.object.situacaoFuncional !== SituacaoFuncionario.DESLIGADO;
	};

	$ctrl.isPeriodoFinalRequired = () => {
		return $ctrl.isPeriodoRecessoRequired();
	};

	$ctrl.isDataDesligamentoRequired = () => {
		return $ctrl.object.situacaoFuncional === SituacaoFuncionario.DESLIGADO;
	};

	$ctrl.handleChange = (name, value) => {
		$ctrl.object[name] = value;

		if (!$ctrl.isPeriodoRecessoRequired()) {
			$ctrl.object.dataInicioRecesso = null;
			$ctrl.object.dataFimRecesso = null;
		}

		if (!$ctrl.isDataDesligamentoRequired()) {
			$ctrl.object.desligamento = null;
		}
	};

	$ctrl.showAlterarFoto = (targetEvent) => {
		const { cropper } = $ctrl;

		DialogFuncionarioFoto.show($ctrl.object, {
			targetEvent,
			cropper,
		}).then(payload => {
			const { result } = payload;
			if ($ctrl.onChangeFoto) {
				$ctrl.onChangeFoto({ foto: result });
			}
		}).catch(notifyRestError);
	};

	// FIXME Migrar lógica do cropper e de load/store da foto do colaborador
	// para um serviço próprio. Refatorar também o DialogFuncionarioFoto
	function loadFuncionarioFoto(id = $ctrl.object.id) {
		const filtro = {
			funcionario: { id },
			tipo: 'FOTO_PERFIL',
		};

		const imageChanged = (data) => {
			if (!data) {
				delete $ctrl.cropper;
				throw new Error('Erro ao buscar foto do colaborador!');
			}

			const imageData = data[0];

			if (imageData) {
				$ctrl.cropper = {
					id: imageData.id,
					funcionario: imageData.funcionario,
					tipo: imageData.tipo,
					sourceImage: imageData.base64,
					croppedImage: imageData.base64,
				};
			} else {
				$ctrl.cropper = { ...filtro };
			}
		};

		if (!id) {
			return Promise.resolve(filtro);
		}

		return FotoResource.buscar(null, filtro).$promise
			.then(data => imageChanged(data))
			.catch(notifyRestError);
	}
}
