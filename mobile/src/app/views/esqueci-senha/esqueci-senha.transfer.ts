import { EntityResourceData } from '../../utils/resource';

export interface EsqueciSenhaTO {
	id?: number;
	emailFuncional: string;
}

export type EsqueciSenhaCreateTO = EntityResourceData<EsqueciSenhaTO>;
