import { EntityResourceFilter, EntityResourceRefLike } from '../../utils/resource';

export type FeedforwardRecebidoFuncionarioTO = {
	id: number;
	nome: string;
	fotoBase64: string;
};

export type FeedforwardRecebidoTipoCompetenciaTO = {
	id: number;
	nome: string;
	nomeCurto: string;
};

export type FeedforwardRecebidoCompetenciaTO = {
	id: number;
	descricaoAbreviada: string;
	tipo?: EntityResourceRefLike<FeedforwardRecebidoTipoCompetenciaTO>;
};

export interface FeedforwardRecebidoTO {
	id: number;

	competencia: FeedforwardRecebidoCompetenciaTO;
	avaliador: FeedforwardRecebidoFuncionarioTO;
	avaliado?: FeedforwardRecebidoFuncionarioTO;

	graduacaoVisual: number;
	feedforward: string;
	dataApontamento: Date;
}

export type FeedforwardRecebidoFilterTO = EntityResourceFilter<FeedforwardRecebidoTO> & {
	avaliadorId?: number;
	avaliadoId?: number;
	competenciaId?: number;
	tipoCompetenciaId?: number;
};
