import TipoPendencia from '../../../partials/model/TipoPendencia';

const SELECTED_TAB_EVENT = 'selectedIndexTab';
/* @ngInject */
export default function abaPendenciasCadastroCtrl(
	$scope,
	$state,
	$window,
	DialogConfirm,
	DialogWait
) {
	const $ctrl = this;
	$ctrl.model = $scope.model = $state.model;
	$scope.$emit(SELECTED_TAB_EVENT, $state.selectedIndexTab);

	/* Foi necessário repassar para o $ctrl
	para que fosse feito o ternário do TipoPendencia no HTML */
	$ctrl.TipoPendencia = TipoPendencia;

	$ctrl.formatarData = (dataAtualizacao) => {
		const dataSemHora = dataAtualizacao.split('T')[0];		
		const ano = dataSemHora.split('-')[0];
		const mes = dataSemHora.split('-')[1];
		const dia = dataSemHora.split('-')[2];
		return `${dia}/${mes}/${ano}`;
	};

	if($ctrl.model.avaliacao.pendencias.length > 0)
		$ctrl.dataAtualizacao = $ctrl.formatarData($ctrl.model.avaliacao.pendencias[0].dataUltimaAtualizacao);

	$ctrl.revalidar = () => {
		const dialog = DialogWait.showClick(
			'Aguarde salvando os participantes...',
			false,
		);

		const qtdPendencias = $ctrl.model.avaliacao.pendencias.length;
		$ctrl.model.prepareParticipantes()
			.then(() => {
				dialog.hide();
				if ($ctrl.model.avaliacao.pendencias.length < qtdPendencias) {
					$scope.$emit(
						'notify-success',
						'Pendência resolvida com sucesso.',
					);
				}
			})
			.catch((err) => {
				dialog.hide();
				$scope.$emit(err.type, err.data, err.statusText);
			});
	};

	$ctrl.resolverPendencia = item => {
		//FIXME Olhar essas rotas após a refatoração dos cadastros
		if (item.tipoPendencia == TipoPendencia.CARGO) {
			$state.go('main.cadastro.cargos.view', {objectId: item.cargo.id});
		} else if (item.tipoPendencia == TipoPendencia.COLABORADORES) {
			$state.go('main.cadastro.colaboradores.view', {objectId: item.avaliado.id});
		}
	};

	$ctrl.removerPendencia = (event, item) => {
		DialogConfirm.show('DIALOG.REMOVE_TITLE', 'PENDENCIAS.REMOVE_MSG', { name: 'Pendências' })
			.then(() => {
				$ctrl.model.removePendencia(item)
					.then((res) => {
						$scope.$emit(res.type, res.data, res.statusText);
					})
					.catch((err) => $scope.$emit(err.type, err.data, err.statusText));
			}).catch((err) => {
				console.error(err);
			});
	};
}
