import UF from './utils/uf-list';

/* @ngInject */
export default function homeOfficeFormCtrl(
	$state,
	$scope,
	comboTipoSexo,
	MunicipioPublic,
	HomeOffice
) {
	const $ctrl = this;
	$ctrl.object = {};
	$ctrl.comboTipoSexo = comboTipoSexo;
	$ctrl.estadoList = UF;
	$ctrl.municipioList = [];
	$ctrl.isMunicipioEnabled = false;
	$ctrl.actualDate = new Date();
	$ctrl.isLoading = false;

	$ctrl.signIn = () => {
		$ctrl.isLoading = true;
		if (!$ctrl.object.isAmbienteEmpresa && !$ctrl.object.isDiagnostico) {
			$scope.$emit('notify-error', 'É preciso marcar pelo menos uma das checkboxes.', 'Atenção!');
			$ctrl.isLoading = false;
			return;
		}

		HomeOffice.save($ctrl.object).$promise.then(() => {
			$scope.$emit(
				'notify-success',
				'Seus dados foram inseridos com sucesso! Em instantes você receberá um e-mail com todas as informações.',
				'Salvo com sucesso');
			$ctrl.object = undefined;
			$ctrl.homeOfficeForm.$setUntouched();
			$ctrl.homeOfficeForm.$setPristine();
			$ctrl.isLoading = false;
			//$state.go('login');
		}).catch(err => {
			$ctrl.isLoading = false;
			console.error(err);
		});
	};

	$ctrl.changeEstado = (item) => {
		if (item) {
			$ctrl.municipioList = [];
			$ctrl.isMunicipioEnabled = false;
			$ctrl.object.municipio = undefined;

			MunicipioPublic.getMunicipoByUf({ uf: item.sigla }).$promise
				.then((data) => {
					$ctrl.isMunicipioEnabled = true;
					return $ctrl.municipioList = data;
				});
		}
	};

	$ctrl.querySearchEstado = query => {
		query = query.toUpperCase();
		return $ctrl.estadoList.filter(uf => uf.sigla.toUpperCase().includes(query));
	};

	$ctrl.querySearchMunicipio = query => {
		query = query.toUpperCase();
		query = query.normalize('NFD').replace(/[\u0300-\u036f]/g, '');

		return $ctrl.municipioList.filter(municipio => {
			return municipio.nome.toUpperCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').includes(query);
		});
	};

	$ctrl.onCancel = () => {
		//$state.go('login');
		$ctrl.object = undefined;
		$ctrl.homeOfficeForm.$setUntouched();
		$ctrl.homeOfficeForm.$setPristine();
	};
}
