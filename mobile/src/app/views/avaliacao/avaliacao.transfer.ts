/* Enums */

import { SituacaoTO } from "../../model/transfer/situacao.transfer";

export enum TipoAvTO {
	AV1,
	AV2,
	AV3,
}

export enum TipoClientTO {
	WEB,
	ANDROID,
	IOS,
}

export interface AvaliacaoTO {
	id: number;
	descricaoAbreviada: string;
	avaliacaoTipoCompetenciaList?: Array<AvaliacaoTipoCompetenciaTO>;
}

export interface AvaliacaoTipoCompetenciaTO {
	id: number /*Id do Tipo de Competencia, nao da AVALIACAO tipo competencia!*/;
	descricaoTipoCompetencia: string;
	listaCompetencias?: Array<AvaliacaoCompetenciaTO>;
	diasRestantes?: number;
	porcentagemPreenchimentoPorCompetencia?: number;
	porcentagemPreenchimentoPorPreenchimento?: number;
	qtdCompletosPorCompetencia?: number;
	qtdCompletosPorPreenchimento?: number;
	qtdTotalPorPreenchimento: number;
}

export interface AvaliacaoCompetenciaTO {
	id: number;
	descricaoAbreviada?: string;
	porcentagemPreenchimento?: number;
	direcionamentoGestor?: string;
	listaDetalhamentos: Array<AvaliacaoDetalhamentoTO>;
	qtdCompletosPorPreenchimento: number;
	qtdTotalDePreenchimentos: number;
}

export interface AvaliacaoDetalhamentoTO {
	id: number;
	descricao?: string;
	listaPreenchimentos?: Array<AvaliacaoPreenchimentoTO>;
	listaGraduacao?: Array<AvaliacaoGraduacaoTO>;
}

export interface AvaliacaoPreenchimentoTO {
	id: number;
	graduacao?: AvaliacaoGraduacaoTO;
	participante?: AvaliacaoParticipanteTO;
	tipoAv: TipoAvTO;
	observacao?: string;
	graduacaoCompAv1Id?: number;
	graduacaoCompAv2Id?: number;
	tipoClient: TipoClientTO;
}
export interface AvaliacaoParticipanteTO {
	id: number;
	avaliacaoId?: number;
	avaliadorId?: number;
	nomeAvaliador?: string;
	nomeAvaliado?: string;
	fotoAvaliado?: string;
	preenchidaAV1?: boolean;
	preenchidaAV2?: boolean;
	dataInicioAv1?: Date;
	dataInicioAv2?: Date;
	limiteAv1?: Date;
	limiteAv2?: Date;
	consensoLiberado?: boolean;
}

export interface AvaliacaoGraduacaoTO {
	id?: number;
	label?: string;
	ordem?: number;
	hint?: string;
	ponto?: number;
	cor?: string;
	situacao?: SituacaoTO;
	comentarioObrigatorio?: boolean;
}
