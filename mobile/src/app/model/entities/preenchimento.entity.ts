import { EntityRef } from "../../utils/entity";
import { GraduacaoLike } from "./graduacao.entity";
import { ParticipanteLike } from "./participante.entity";
import { TipoClientTO, TipoAvTO } from "../../views/avaliacao/avaliacao.transfer";

export type PreenchimentoRef = EntityRef<Preenchimento>;
export type PreenchimentoLike = PreenchimentoRef | Preenchimento;

export interface Preenchimento {
	id: number;
	graduacao?: GraduacaoLike;
	participante?: ParticipanteLike;
	tipoAv?: TipoAvTO;
	observacao?: string;
	graduacaoCompAv1Id?: number;
	graduacaoCompAv2Id?: number;
	tipoClient?: TipoClientTO;

}
