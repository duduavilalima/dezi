import { enumInfo } from 'utils/enum.utils';

const TipoPendencia = {
	COLABORADORES: 'COLABORADORES',
	CARGO: 'CARGO',
	DIMENSAO: 'DIMENSAO',
};

const TipoPendenciaInfo = enumInfo(TipoPendencia, [
	// Ordenação natural
	TipoPendencia.COLABORADORES,
	TipoPendencia.CARGO,
	TipoPendencia.DIMENSAO,
])
	.className('TipoPendencia')
	.propertiesPerValue({
		[TipoPendencia.COLABORADORES]: { code: 1 },
		[TipoPendencia.CARGO]: { code: 2 },
		[TipoPendencia.DIMENSAO]: { code: 3 },
	})
	.build();

export default TipoPendencia;

export { TipoPendenciaInfo };
