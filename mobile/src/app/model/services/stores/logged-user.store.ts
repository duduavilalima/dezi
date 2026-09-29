import { Injectable } from "@angular/core";

import { LoggedUserTO } from "../../transfer/loggedUser.transfer";
import { Nothing, ensureObject } from "../../../utils/guards";
import { LocalStorage } from "../../../commons/services/local-storage.service";
import { VolatileStore } from "../../../utils/store";
import { STORAGE_PATH } from "../../../constants/storage";

export interface LoggedUserState {
	inicializado: boolean;
	usuario?: LoggedUserTO;
	dataInicializacao?: Date;
}

export interface InitializedLoggedUserState {
	inicializado: true;
	usuario: LoggedUserTO;
	dataInicializacao: Date;
}

export function isValidLoggedUserState(state: LoggedUserState | Nothing): state is InitializedLoggedUserState {
	return !!state && state.inicializado;
}

@Injectable()
export class LoggedUserStore extends VolatileStore<LoggedUserState> {
	storagePath = STORAGE_PATH.for(LoggedUserStore);

	constructor(
		protected stateStorage: LocalStorage,
	) {
		super();
		this.initialize();
	}

	empty() {
		return !this.getState().inicializado;
	}

	get() {
		const state = this.getState();
		return ensureObject(state.usuario, "Faltando dados do usuário autenticado.");
	}

	set(usuario: LoggedUserTO) {
		return this.setState({
			inicializado: true,
			usuario: usuario,
			dataInicializacao: new Date(),
		});
	}

	getUserId() {
		return this.get().id;
	}

	clear() {
		return super.clear();
	}

	protected createInitialState(): LoggedUserState {
		return {
			inicializado: false,
		};
	}
}
