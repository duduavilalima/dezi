import { DepartamentoTO } from "./departamento.transfer";
import { CargoTO } from "./cargo.transfer";
import { CompetenciaTO } from "./competencia.transfer";

export interface FuncionarioTO {
	id?: number;
	empresaId: number;
	fotoBase64?: string;
	nome?: string;
	unidadeId?: number;
	cargo?: CargoTO;
	departamento?: DepartamentoTO;
	listaCompetencias?: Array<CompetenciaTO>;
	email?: string;
}
