import { EntityRef } from "../../utils/entity";
import { FuncionarioLike } from "./funcionario.entity";

export type TipoCompetenciaRef = EntityRef<TipoCompetencia>;
export type TipoCompetenciaLike = TipoCompetenciaRef | TipoCompetencia;

export interface TipoCompetencia {
	id: number;
	funcionario?: FuncionarioLike;
	pai?: TipoCompetenciaLike;
	nome: string;
	descricao: string;
	nomeCurto: string;
}
