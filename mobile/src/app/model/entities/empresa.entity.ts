import { EntityRef } from "../../utils/entity";

export type EmpresaRef = EntityRef<Empresa>;
export type EmpresaLike = EmpresaRef | Empresa;

export interface Empresa {
	id: number;
	nome: string;
	isFeedforward: boolean;
}
