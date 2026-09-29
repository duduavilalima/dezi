import { EntityRef } from "../../utils/entity";
import { AvaliacaoTipoCompetenciaLike } from "./avaliacao-tipo-competencia.entity";

export type AvaliacaoRef = EntityRef<Avaliacao>;
export type AvaliacaoLike = AvaliacaoRef | Avaliacao;

export interface Avaliacao {
	id: number;
	descricaoAbreviada: string;
	avaliacaoTipoCompetenciaList: Array<AvaliacaoTipoCompetenciaLike>;
}
