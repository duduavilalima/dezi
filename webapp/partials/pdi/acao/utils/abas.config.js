const BASE_STATE_PDI = 'main.pdi.pesquisa_resultado.acoes';

const TABS_PDI = [
	{
		title: 'PDI.ABA_EM_APROVACAO',
		tabName: 'em_aprovacao',
		stateName: BASE_STATE_PDI + '.em_aprovacao',
	},
	{
		title: 'PDI.ABA_APROVADO',
		tabName: 'recebidas',
		stateName: BASE_STATE_PDI + '.aprovado',
	},
	{
		title: 'PDI.ABA_EM_ANDAMENTO',
		tabName: 'em_andamento',
		stateName: BASE_STATE_PDI + '.em_andamento',
	},
	{
		title: 'PDI.ABA_FINALIZADO',
		tabName: 'finalizado',
		stateName: BASE_STATE_PDI + '.finalizado',
	},
].map((value, index) => ({ ...value, index }));

export default TABS_PDI;

