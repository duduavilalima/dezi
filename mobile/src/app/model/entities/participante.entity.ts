import { EntityRef } from "../../utils/entity";


export type ParticipanteRef = EntityRef<Participante>;
export type ParticipanteLike = ParticipanteRef | Participante;

export interface Participante {
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
