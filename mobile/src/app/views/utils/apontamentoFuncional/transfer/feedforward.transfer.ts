import { EntityResourceRef } from "../../../../utils/resource";

export interface FeedforwardCargoTO {
	id: number;
	nome: string;
}

export interface FeedforwardFuncionarioTO {
	id: number;
	nome: string;
	cargo: FeedforwardCargoTO;
}

export interface FeedforwardTipoCompetenciaTO {
	id: number;
	nome: string;
	nomeCurto: string;
}

export interface FeedforwardCompetenciaTO {
	id: number;
	descricaoAbreviada: string;
	tipo: Pick<FeedforwardTipoCompetenciaTO, "id">;
}

export interface FeedforwardTO {
	id: number;
	dataApontamento: Date;
	graduacaoVisual: number;
	feedforward: string;
	avaliado?: FeedforwardFuncionarioTO;
	avaliador?: Pick<FeedforwardFuncionarioTO, "id">;
	competencia: FeedforwardCompetenciaTO;
}

export type FeedforwardCreateTO = Pick<FeedforwardTO,
	| "feedforward"
	| "graduacaoVisual">
	& {
		avaliado: EntityResourceRef<FeedforwardFuncionarioTO>;
		competencia: EntityResourceRef<FeedforwardCompetenciaTO>;
	};

export interface FeedforwardFilterTO {
	id?: number;
	avaliadoId?: number;
	tipoCompetenciaId?: number;
}
