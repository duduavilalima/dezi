import { enumInfo } from 'utils/enum.utils';

const Situacao = {
	ATIVA: 'ATIVA',
	INATIVA: 'INATIVA',
};

const SituacaoInfo = enumInfo(Situacao, [
	// Ordenação natural
	Situacao.ATIVA,
	Situacao.INATIVA,
])
	.className('Situacao')
	.propertiesPerValue({
		[Situacao.ATIVA]: { code: 1 },
		[Situacao.INATIVA]: { code: 98 },
	})
	.property('trLabel', ({value}) => `SITUACAO.${value}`)
	.build();

function isAtivo(elem) {
	return typeof elem === 'string' ? elem === Situacao.ATIVA : isAtivo(elem.situacao);
}

function isInativo(elem) {
	return typeof elem === 'string' ? elem === Situacao.INATIVA : isInativo(elem.situacao);
}

export default Situacao;

export {
	SituacaoInfo,
	isAtivo,
	isInativo,
};
