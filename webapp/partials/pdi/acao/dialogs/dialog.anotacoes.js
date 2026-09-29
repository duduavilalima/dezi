/* @ngInject */
export default function dialogAnotacoes(
	$mdDialog,
	listCampanhas,
	listAvaliacaoParticipante
) {
	const $dialog = this;
	$dialog.campanhaSelected = undefined;
	$dialog.avaliacaoSelected = undefined;

	const load = () => {
		comboCampanha();
	};

	$dialog.changeRegistroAcao = pdi => {
		$dialog.acoesRegistradas = pdi;
	};

	$dialog.changeAvaliacao = id_campanha => {
		const avaliacoes = listAvaliacaoParticipante.filter(part => part.avaliacao.campanha.id === id_campanha);
		$dialog.listAvaliacao = [];
		avaliacoes.forEach(av => {
			if (!$dialog.listAvaliacao.find(scopeAv => scopeAv.id === av.id)) {
				$dialog.listAvaliacao.push(av);
			}
		});
	};

	const comboCampanha = () => {
		$dialog.listCampanha = listCampanhas;
	};

	$dialog.cancelAcao = $mdDialog.cancel;

	load();
}