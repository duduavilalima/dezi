import { EntityResourceData } from '../../utils/resource';

export enum ClassificacaoFaleConoscoTO {
	DUVIDA = "duvida",
	CRITICA = "critica",
	SUGESTAO = "sugestao",
	RECLAMACAO = "reclamacao",
}

export interface FaleConoscoTO {
	id: number;
	assunto: string;
	mensagem: string;
	classificacao: string;
	emailRemetente: string;
	nomeRemetente: string;
	plataforma: string;
}

export type FaleConoscoCreateTO = EntityResourceData<FaleConoscoTO>;
