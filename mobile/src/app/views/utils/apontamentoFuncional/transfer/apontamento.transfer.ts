import { EntityResourceRef } from "../../../../utils/resource";

export interface ApontamentoCargoTO {
	id: number;
	nome: string;
}

export interface ApontamentoFuncionarioTO {
	id: number;
	nome: string;
	cargo: ApontamentoCargoTO;
}

export interface ApontamentoTipoCompetenciaTO {
	id: number;
	nome: string;
	nomeCurto: string;
}

export interface ApontamentoCompetenciaTO {
	id: number;
	descricaoAbreviada: string;
	tipo: Pick<ApontamentoTipoCompetenciaTO, "id">;
}

export interface ApontamentoTO {
	id: number;
	dataApontamento: Date;
	graduacaoVisual: number;
	registroCritico: string;
	avaliado?: ApontamentoFuncionarioTO;
	avaliador?: Pick<ApontamentoFuncionarioTO, "id">;
	competencia: ApontamentoCompetenciaTO;
}

export type ApontamentoCreateTO = Pick<ApontamentoTO,
	| "registroCritico"
	| "graduacaoVisual">
	& {
		avaliado: EntityResourceRef<ApontamentoFuncionarioTO>;
		competencia: EntityResourceRef<ApontamentoCompetenciaTO>;
	};

export interface ApontamentoFilterTO {
	id?: number;
	avaliadoId?: number;
	tipoCompetenciaId?: number;
}
