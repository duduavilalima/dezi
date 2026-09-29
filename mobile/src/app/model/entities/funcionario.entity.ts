import { EntityRef } from "../../utils/entity";
import { UnidadeLike } from "./unidade.entity";
import { EmpresaLike } from "./empresa.entity";

export type FuncionarioRef = EntityRef<Funcionario>;
export type FuncionarioLike = FuncionarioRef | Funcionario;

export interface Funcionario {
	id: number;
	nome: string;
	email?: string;
	empresa: EmpresaLike;
	fotoBase64?: string;
	unidade?: UnidadeLike;

	// TODO Mapear
	// cargo?: CargoTO;
	// departamento?: DepartamentoTO;
	// listaCompetencias?: Array<CompetenciaTO>;
	// listaApontamentos?: Array<ApontamentoTO>;
}
