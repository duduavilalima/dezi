import ROUTES from 'utils/routes';

const STATE_ABA_PENDENCIAS = ROUTES.MAIN.AVALIACAO.VIEW.PENDENCIAS.name;
const STATE_ABA_RESUMO = ROUTES.MAIN.AVALIACAO.VIEW.RESUMO.name;
const STATE_ABA_MENSAGENS = ROUTES.MAIN.AVALIACAO.VIEW.MENSAGENS.name;
const STATE_ABA_PARTICIPANTES = ROUTES.MAIN.AVALIACAO.VIEW.PARTICIPANTES.name;
const STATE_ABA_AVALIADORES = ROUTES.MAIN.AVALIACAO.VIEW.AVALIADORES.name;

const TABS = [
	{
		title: 'RESUMO.RESUMO',
		tabName: 'resumo',
		stateName: STATE_ABA_RESUMO,
		status: true
	},
	{
		title: 'PARTICIPANTES.PARTICIPANTES',
		tabName: 'participantes',
		stateName: STATE_ABA_PARTICIPANTES,
		status: true
	},
	{
		title: 'Pendencia ()',
		tabName: 'pendencia',
		stateName: STATE_ABA_PENDENCIAS,
		status: true
	},
	{
		title: 'AVALIADORES.AVALIADORES',
		tabName: 'avaliadores',
		stateName: STATE_ABA_AVALIADORES,
		status: true
	},
	{
		title: 'MENSAGEM.MENSAGENS',
		tabName: 'mensagens',
		stateName: STATE_ABA_MENSAGENS,
		status: true
	},
].map((value, index) => ({ ...value, index }));

export default TABS;
