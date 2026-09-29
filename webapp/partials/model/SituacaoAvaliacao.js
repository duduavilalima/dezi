import { enumInfo } from 'utils/enum.utils';

const SituacaoAvaliacao  = {
	AG_LIBERACAO: 'AG_LIBERACAO',
	LIBERADA: 'LIBERADA',
	INATIVA: 'INATIVA',
	CANCELADA: 'CANCELADA',
	CONCLUIDA: 'CONCLUIDA',
};

const SituacaoAvaliacaoInfo = enumInfo(SituacaoAvaliacao, [
	// Ordenação natural
	SituacaoAvaliacao.AG_LIBERACAO,
	SituacaoAvaliacao.LIBERADA,
	SituacaoAvaliacao.INATIVA,
	SituacaoAvaliacao.CANCELADA,
	SituacaoAvaliacao.CONCLUIDA,
])
	.className('SituacaoAvaliacao')
	.propertiesPerValue({
		[SituacaoAvaliacao.AG_LIBERACAO]: { code: 1 },
		[SituacaoAvaliacao.LIBERADA]: { code: 2 },
		[SituacaoAvaliacao.INATIVA]: { code: 98 },
		[SituacaoAvaliacao.CANCELADA]: { code: 99 },
		[SituacaoAvaliacao.CONCLUIDA]: { code: 100 },
	})
	.build();

export default SituacaoAvaliacao;

export {
	SituacaoAvaliacaoInfo,
};
