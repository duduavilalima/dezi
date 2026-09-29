import { enumInfo } from 'utils/enum.utils';

const TipoSexo = {
	MASCULINO: 'MASCULINO',
	FEMININO: 'FEMININO',
};

const TipoSexoInfo = enumInfo(TipoSexo, [
	TipoSexo.MASCULINO,
	TipoSexo.FEMININO,
])
	.className('TipoSexo')
	.propertiesPerValue({
		[TipoSexo.MASCULINO]: { code: 1 },
		[TipoSexo.FEMININO]: { code: 2 },
	})
	.property('trLabel', ({value}) => `TIPO.${value}`)
	.build();

export default TipoSexo;

export { TipoSexoInfo };
