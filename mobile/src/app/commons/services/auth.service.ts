import { Injectable } from "@angular/core";

import { AuthStore } from "../stores/auth.store";
import { HttpClient } from "@angular/common/http";
import { EnvState, EnvStore } from "../stores/env.store";

interface AuthResponse {
	token: string;
}

export interface AuthCredentials {
	empresaId: number;
	matricula: string;
	senha: string;
}

@Injectable()
export class AuthService {
	authBaseUrl: string;
	authLoginUrl: string;
	authUserUrl: string;
	authEmpresasUrl: string;

	constructor(
		private authStore: AuthStore,
		private httpClient: HttpClient,
		private envStore: EnvStore,
	) {
		this.initialize();
	}

	async login(authCredentials: AuthCredentials) {
		const loginResponse = await this.doLogin(authCredentials);
		const token = loginResponse.token;
		this.authStore.authenticate({
			// PROTIP: DO NOT SPREAD authCredentials!!
			token,
			empresaId: authCredentials.empresaId,
			matricula: authCredentials.matricula,
		});
	}

	logout() {
		return this.authStore.clear();
	}

	isAuthenticated() {
		return this.authStore.isAuthenticated();
	}

	private async initialize() {
		this.envStore.subscribe({ next: this.onEnvChange });
	}

	private onEnvChange = (ENV: EnvState) => {
		this.authBaseUrl = `${ENV.apiBaseUrl}/auth`;
		this.authLoginUrl = `${this.authBaseUrl}/login`;
		this.authUserUrl = `${this.authBaseUrl}/mobile/user`;
		this.authEmpresasUrl = `${this.authBaseUrl}/empresas`;
	}

	private async doLogin(authCredentials: AuthCredentials): Promise<AuthResponse> {
		const { bypassLogin } = await this.envStore.get();
		if (bypassLogin) {
			return { token: this.generateFakeToken() };
		}
		const url = this.authLoginUrl;
		const body = JSON.stringify(authCredentials);
		return this.httpClient.post<AuthResponse>(url, body, {
			responseType: "json",
			observe: "body",
			reportProgress: false,
			withCredentials: false,
			headers: {
				"Content-Type": "application/json",
			},
		}).toPromise();
	}

	private generateFakeToken() {
		return `DEV-${Math.random().toString(36).substr(2)}`;
	}
}
