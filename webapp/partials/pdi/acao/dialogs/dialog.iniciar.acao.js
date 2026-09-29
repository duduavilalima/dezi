import SituacaoPDI from '../../../model/SituacaoPDI';

/* @ngInject */
export default function dialogIniciaAcaoDesenv(
	$mdDialog,
	$scope,
	acao,
	fn_stateGo
) {
	const $dialog = this;
	$dialog.acaoSelected = acao;
	$dialog.acaoSelected.dataInicio = new Date();
	$dialog.dataMinimaPrevista = new Date($dialog.acaoSelected.dataPrevistaInicio);

	$dialog.saveAcao = () => {
		$dialog.acaoSelected.situacao = SituacaoPDI.EM_ANDAMENTO;
		$mdDialog.hide();
		fn_stateGo($dialog.acaoSelected);
	};

	$dialog.cancelAcao = $mdDialog.cancel;
}
