import { EntityRef } from "../../utils/entity";
import { SituacaoTO } from "../transfer/situacao.transfer";

export type GraduacaoRef = EntityRef<Graduacao>;
export type GraduacaoLike = GraduacaoRef | Graduacao;

export interface Graduacao {
	id: number;
	label?: string;
	ordem?: number;
	hint?: string;
	ponto?: number;
	cor?: string;
	comentarioObrigatorio?: boolean;
	situacao?: SituacaoTO;
}
