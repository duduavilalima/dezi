import { Component, Injector } from "@angular/core";

import { AuthService } from "../../commons/services/auth.service";
import { EsqueciSenhaPage } from "../esqueci-senha/esqueci-senha.page";
import { EmpresaAuthResource } from "../../model/services/resources/auth-empresa.resource";
import { LandingPage } from "../landing/landing.page";
import { EmpresaAuthTO } from '../../model/transfer/empresaAuth.transfer';
import { SimplePage } from '../utils/page';
import { EnvStore } from '../../commons/stores/env.store';

@Component({
	selector: "app-login",
	templateUrl: "./login.page.html",
})
export class LoginPage extends SimplePage {

	matricula = "";
	senha = "";
	empresas: EmpresaAuthTO[] = [];
	empresa: EmpresaAuthTO | undefined;

	production = false;
	version = "0.0.0";
	environment = "Dev.";
	gitRef = "?";
	gitCommit = "?";

	private mensagemErro: string;

	constructor(
		injector: Injector,
		private authService: AuthService,
		private empresaResource: EmpresaAuthResource,
		private envStore: EnvStore,
	) {
		super(injector);
	}

	async initialize() {
		const ENV = await this.envStore.get();
		const key = `app.env.${ENV.name}.label`;
		const environment = await this.translator.get(key);
		this.version = ENV.version;
		this.environment = environment === key ? ENV.name : environment;
		this.production = ENV.production;
		this.gitRef = ENV.gitRef;
		this.gitCommit = ENV.gitCommit;

		this.mensagemErro = await this.translator.get("app.views.login.messages.error");
		this.empresas = await this.empresaResource.list();

		if (this.empresas.length === 1) {
			this.empresa = this.empresas[0];
		}
	}

	async login() {
		try {
			this.loaderPresent();
			const authCredentials = {
				matricula: this.matricula,
				senha: this.senha,
				empresaId: this.empresa!.id!,
			};
			await this.authService.login(authCredentials);
			if (this.navController.canGoBack()) {
				this.navController.pop();
			} else {
				this.navController.push(LandingPage);
			}
		} catch (err) {
			if (err.status === 401) {
				this.mensagemErro = await this.translator.get("app.views.login.messages.error");
			}
			else if (err.status === 403) {
				this.mensagemErro = await this.translator.get("app.views.login.messages.error.lock");
			}
			else if (err.status === 404) {
				this.mensagemErro = await this.translator.get("app.views.login.messages.error.server");
			}
			this.utils.presentToast(this.mensagemErro);
			this.logger.exception(err, "Erro ao efetuar login!");
		} finally {
			this.loaderDismiss();
		}
	}

	compareById(a: EmpresaAuthTO, b: EmpresaAuthTO) {
		return a && b ? a.id === b.id : a === b;
	}

	goToForgotPassword() {
		this.navController.push(EsqueciSenhaPage);
	}
}
