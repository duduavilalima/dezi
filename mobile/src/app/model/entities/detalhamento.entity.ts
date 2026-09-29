import { EntityRef } from "../../utils/entity";
import { GraduacaoLike } from "./graduacao.entity";
import { PreenchimentoLike } from "./preenchimento.entity";


export type DetalhamentoRef = EntityRef<Detalhamento>;
export type DetalhamentoLike = DetalhamentoRef | Detalhamento;

export interface Detalhamento {
	id: number;
	descricao?: string;
	listaPreenchimentos?: Array<PreenchimentoLike>;
	listaGraduacao?: Array<GraduacaoLike>;
}
