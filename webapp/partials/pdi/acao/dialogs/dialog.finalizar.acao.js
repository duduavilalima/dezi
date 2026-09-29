import SituacaoPDI from '../../../model/SituacaoPDI';

/* @ngInject */
export default function dialogfinalizarAcaoDesenv(
	$mdDialog,
	$scope,
	acao,
	PDI,
	fn_stateGo
) {
	const $dialog = this;
	$dialog.acaoSelected = acao;
	$dialog.acaoSelected.dataFim = new Date();
	$dialog.acaoSelected.situacao = SituacaoPDI.FINALIZADO;
	$dialog.dataMinimaPrevista = new Date($dialog.acaoSelected.dataInicio);

	$dialog.saveAcao = () => {
		$dialog.acaoSelected.situacao = SituacaoPDI.FINALIZADO;
		$mdDialog.hide();
		fn_stateGo($dialog.acaoSelected);
	};

	$dialog.cancelAcao = $mdDialog.cancel;
}
