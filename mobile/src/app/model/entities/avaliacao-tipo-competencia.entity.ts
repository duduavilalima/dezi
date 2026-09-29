import { EntityRef } from "../../utils/entity";
import { CompetenciaLike } from "./competencia.entity";


export type AvaliacaoTipoCompetenciaRef = EntityRef<AvaliacaoTipoCompetencia>;
export type AvaliacaoTipoCompetenciaLike = AvaliacaoTipoCompetenciaRef | AvaliacaoTipoCompetencia;


export interface AvaliacaoTipoCompetencia {
	id: number /*Id do Tipo de Competencia, nao da AVALIACAO tipo competencia!*/;
	descricaoTipoCompetencia: string;
	listaCompetencias: Array<CompetenciaLike>;
	diasRestantes: number;
	porcentagemPreenchimentoPorCompetencia: number;
	porcentagemPreenchimentoPorPreenchimento: number;
	qtdCompletosPorCompetencia: number;
	qtdCompletosPorPreenchimento: number;
}
