import { TipoPerfilTO } from "./tipoPerfil.transfer";
import { SituacaoTO } from "./situacao.transfer";

export interface LoggedUserCargoTO {
	id: number;
	nome: string;
	situacao: SituacaoTO;
}

export interface LoggedUserEmpresaTO {
	id: number;
	nome: string;
	identificador: string;
	host: string;
	contratante: {
		id: number;
		nome: string;
		situacao: SituacaoTO;
	};
}

export interface LoggedUserConfiguracaoTO {
	id: number;
	linguagem: string;
}

// TODO Separar Usuário de Funcionário (aguardar refatoração do Vini)
export interface LoggedUserTO {
	id: number;
	matricula: string;
	nome: string;
	username: string;
	emailFuncional: string;
	primeiroAcesso: boolean;
	situacao: SituacaoTO;
	tipoPerfil: TipoPerfilTO;
	cargo: LoggedUserCargoTO;
	empresa: LoggedUserEmpresaTO;
	configuracao: LoggedUserConfiguracaoTO;
}
