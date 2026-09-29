import { enumInfo } from 'utils/enum.utils';

const TipoAvaliacaoPerfil = {
	GESTOR: 'GESTOR',
	AUTOAVALIACAO: 'AUTOAVALIACAO',
	SUBORDINADO: 'SUBORDINADO',
	PARES: 'PARES',
};

const TipoAvaliacaoPerfilView = {
	TODOS: 'TODOS',
	...TipoAvaliacaoPerfil,
};

const TipoAvaliacaoPerfilInfo = enumInfo(TipoAvaliacaoPerfil, [
	// Ordenação natural
	TipoAvaliacaoPerfil.AUTOAVALIACAO,
	TipoAvaliacaoPerfil.GESTOR,
	TipoAvaliacaoPerfil.SUBORDINADO,
	TipoAvaliacaoPerfil.PARES,
])
	.className('TipoAvaliacaoPerfil')
	.propertiesPerValue({
		[TipoAvaliacaoPerfil.AUTOAVALIACAO]: { code: 100 },
		[TipoAvaliacaoPerfil.GESTOR]: { code: 101 },
		[TipoAvaliacaoPerfil.SUBORDINADO]: { code: 102 },
		[TipoAvaliacaoPerfil.PARES]: { code: 103 },
	})
	.build();

export default TipoAvaliacaoPerfil;
export { TipoAvaliacaoPerfilView, TipoAvaliacaoPerfilInfo };
