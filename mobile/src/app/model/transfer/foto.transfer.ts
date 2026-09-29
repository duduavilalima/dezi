import { TipoFotoTO } from './tipoFoto.transfer';

export interface FotoFuncionarioTO {
	id: number;
}

export interface FotoTO {
	id?: number;
	base64: string;
	tipo: TipoFotoTO;
	funcionario: FotoFuncionarioTO;
}
