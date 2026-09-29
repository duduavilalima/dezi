import { EntityRef } from "../../utils/entity";
import { Omit } from "../../utils/typescript";
import { Competencia, CompetenciaLike } from "./competencia.entity";
import { FuncionarioLike } from "./funcionario.entity";

export type ApontamentoRef = EntityRef<Apontamento>;
export type ApontamentoLike = ApontamentoRef | Apontamento;

export interface Apontamento {
	id: number;
	avaliador: FuncionarioLike;
	avaliado: FuncionarioLike;
	nota: number;
	apontamento?: string;
	feedforward?: string;
	competencia: CompetenciaLike;
	data: Date;
	foto: string;
}

export interface FeedForwardRecebido extends Omit<Apontamento, "competencia"> {
	feedforward: string;
	competencia: Pick<Competencia, "id" | "descricaoAbreviada" | "tipoCompetencia">;
}
