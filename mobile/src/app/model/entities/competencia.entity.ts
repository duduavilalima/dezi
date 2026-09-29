import { EntityRef } from "../../utils/entity";
import { AvaliacaoLike } from "./avaliacao.entity";
import { DetalhamentoLike } from "./detalhamento.entity";
import { TipoCompetenciaLike } from "./tipoCompetencia.entity";

export type CompetenciaRef = EntityRef<Competencia>;
export type CompetenciaLike = CompetenciaRef | Competencia;

export interface Competencia {
	id: number;
	avaliacao?: AvaliacaoLike;
	descricaoAbreviada: string;
	porcentagemPreenchimento: number;
	direcionamentoGestor: string;
	tipoCompetencia: TipoCompetenciaLike;
	listaDetalhamentos: Array<DetalhamentoLike>;
	qtdCompletosPorPreenchimento: number;
	// TODO Modelar
	// idFuncionario?: number;
	// idAvaliacao?: number;
	// descricao?: string;
	// listaAvaliados?: Array<FuncionarioSimpleTO>;
	// qtdAvaliados?: number;
	// porcentagemPreenchimento?: number;
	// direcionamentoGestor?: string;
	// qtdRespostas?: number;
	// qtdPerguntas?: number;
	// diasRestantes?: number;
	// tipoCompetencia?: TipoCompetenciaTO;
	// listaDetalhamentos?: Array<DetalhamentoTO>;
	// dataUltimaSync?: Date;
	// dataInicioSync?: Date;
	// dataFimSync?: Date;
}
