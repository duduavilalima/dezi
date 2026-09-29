import { enumInfo } from 'utils/enum.utils';

const SituacaoPDI = {
	EM_APROVACAO: 'EM_APROVACAO',
	APROVADO: 'APROVADO',
	EM_ANDAMENTO: 'EM_ANDAMENTO',
	FINALIZADO: 'FINALIZADO'
};

const SituacaoPDIInfo = enumInfo(SituacaoPDI, [
	// Ordenação natural
	SituacaoPDI.EM_APROVACAO,
	SituacaoPDI.APROVADO,
	SituacaoPDI.EM_ANDAMENTO,
	SituacaoPDI.FINALIZADO,
])
	.className('SituacaoPDI')
	.propertiesPerValue({
		[SituacaoPDI.EM_APROVACAO]: { code: 1 },
		[SituacaoPDI.APROVADO]: { code: 2 },
		[SituacaoPDI.EM_ANDAMENTO]: { code: 3 },
		[SituacaoPDI.FINALIZADO]: { code: 4 },
	})
	.build();

export default SituacaoPDI;

export {
	SituacaoPDIInfo,
};
