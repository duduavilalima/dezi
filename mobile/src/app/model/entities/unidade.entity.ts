import { EntityRef } from "../../utils/entity";

export type UnidadeRef = EntityRef<Unidade>;
export type UnidadeLike = UnidadeRef | Unidade;

export interface Unidade {
	id: number;
	nome: string;
	unidadePai?: UnidadeLike;
}
