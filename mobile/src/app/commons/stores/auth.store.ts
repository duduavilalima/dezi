import { Injectable } from "@angular/core";

import { VolatileStore } from "../../utils/store";
import { LocalStorage } from "../services/local-storage.service";
import { Nothing } from "../../utils/guards";
import { STORAGE_PATH } from "../../constants/storage";

export interface AuthState {
	autenticado: boolean;
	empresaId?: number;
	matricula?: string;
	token?: string;
}

export interface ValidAuthState extends AuthState {
	autenticado: true;
	empresaId: number;
	matricula: string;
	token: string;
}

export type AuthStateInput = Pick<ValidAuthState,
	| "empresaId"
	| "matricula"
	| "token"
	>;

const mensagemPadrao = "Usuário não está logado.";

export function isValidAuthState(state: AuthState | Nothing): state is ValidAuthState {
	return !!state && state.autenticado;
}

@Injectable()
export class AuthStore extends VolatileStore<AuthState> {
	storagePath = STORAGE_PATH.for(AuthStore);

	constructor(protected stateStorage: LocalStorage) {
		super();
		this.initialize();
	}

	authenticate(stateInput: AuthStateInput): ValidAuthState {
		this.setState({ ...stateInput, autenticado: true });
		const newState = this.getState();
		return newState as ValidAuthState;
	}

	authenticated(): ValidAuthState | undefined {
		const config = this.getState();
		if (!isValidAuthState(config)) return undefined;
		return config as ValidAuthState;
	}

	get() {
		const data = this.authenticated();
		if (!data) throw new Error(mensagemPadrao);
		return data;
	}

	getToken() {
		return this.get().token;
	}

	clear() {
		return super.clear();
	}

	isAuthenticated() {
		return !!this.authenticated();
	}

	protected createInitialState() {
		return { autenticado: false };
	}
}
