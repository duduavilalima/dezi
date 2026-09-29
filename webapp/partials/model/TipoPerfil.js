import { enumInfo } from 'utils/enum.utils';

const TipoPerfil = {
	ADMINISTRADOR: 'ADMINISTRADOR',
	FUNCIONARIO: 'FUNCIONARIO',
	CONSULTOR_RH: 'CONSULTOR_RH',
	GESTOR_RH: 'GESTOR_RH',
	GESTOR_AREA: 'GESTOR_AREA',
};

const TipoPerfilInfo = enumInfo(TipoPerfil, [
	// Ordenação natural
	TipoPerfil.ADMINISTRADOR,
	TipoPerfil.FUNCIONARIO,
	TipoPerfil.CONSULTOR_RH,
	TipoPerfil.GESTOR_RH,
	TipoPerfil.GESTOR_AREA,
])
	.className('TipoPerfil')
	.propertiesPerValue({
		[TipoPerfil.ADMINISTRADOR]: { code: 3 },
		[TipoPerfil.FUNCIONARIO]: { code: 4 },
		[TipoPerfil.CONSULTOR_RH]: { code: 14 },
		[TipoPerfil.GESTOR_RH]: { code: 15 },
		[TipoPerfil.GESTOR_AREA]: { code: 16 },
	})
	.property('trLabel', ({value}) => `PERFIL.${value}`)
	.property('roleName', ({value}) => `ROLE_${value}`)
	.build();

export default TipoPerfil;

export { TipoPerfilInfo };
