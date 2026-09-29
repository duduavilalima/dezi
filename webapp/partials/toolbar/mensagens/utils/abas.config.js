const BASE_STATE_MENSAGENS = 'main.mensagens';

const TABS_MENSAGENS = [
	{
		title: 'MENSAGENS.TABS.TODAS',
		tabName: 'todas',
		stateName: BASE_STATE_MENSAGENS + '.todas',
	},
	{
		title: 'MENSAGENS.TABS.RECEBIDAS',
		tabName: 'recebidas',
		stateName: BASE_STATE_MENSAGENS + '.recebidas',
	},
	{
		title: 'MENSAGENS.TABS.ENVIADAS',
		tabName: 'enviadas',
		stateName: BASE_STATE_MENSAGENS + '.enviadas',
	},
].map((value, index) => ({ ...value, index }));

export default TABS_MENSAGENS;