import { NavController } from "ionic-angular";

import { AuthStore } from "../../commons/stores/auth.store";
import { UtilsService } from "../../commons/utils.service";
import { Injector } from "@angular/core";
import { Translator } from "../../commons/services/translate.service";
import { Logger, LoggerService } from '../../commons/services/logger.service';

export abstract class SimplePage {
	prepared?: boolean;
	initialized?: boolean;

	protected loadingMessage?: string = "Aguarde...";
	protected enterMessage?: string = this.loadingMessage;
	protected reenterMessage?: string = this.enterMessage;
	protected errorMessage?: string = "Houve um erro ao carregar os dados. Por favor, tente novamente mais tarde!";

	protected logger: Logger;
	protected translator: Translator;
	protected navController: NavController;
	protected authStore: AuthStore;
	protected utils: UtilsService;

	constructor(injector: Injector) {
		const loggerService = injector.get(LoggerService);
		this.translator = injector.get(Translator);
		this.navController = injector.get(NavController);
		this.authStore = injector.get(AuthStore);
		this.utils = injector.get(UtilsService);
		this.logger = loggerService.for(this.constructor as any);
	}

	get initializing() {
		return this.initialized === undefined;
	}

	get failed() {
		return this.prepared === false || this.initialized === false;
	}

	async ionViewDidEnter() {
		if (this.prepared === undefined) {
			await this.doPrepare();
		}

		if (this.prepared === true) {
			if (this.initialized !== undefined) {
				await this.doReenter();
			} else {
				await this.doEnter();
			}
		}
	}

	protected async onEnter() {}
	protected async onReenter() {}

	protected async onError(error: any) {
		// Subclasses: sobrescrevam totalmente este método caso necessário, sem medo!
		if (this.errorMessage) {
			this.utils.presentToast(this.errorMessage);
		}
	}

	protected async initialize(): Promise<any> {
		this.loadingMessage = await this.translator.get("app.messages.loading");
		this.enterMessage = await this.translator.get("app.messages.loading-view");
		this.reenterMessage = await this.translator.get("app.messages.reloading-view");
		this.errorMessage = await this.translator.get("app.messages.loading-failed");
	}

	private async doPrepare() {
		try {
			await this.initialize();
			this.prepared = true;
		} catch (err) {
			this.logger.exception(err);
			this.prepared = false;
			this.initialized = false;
			this.onError(err);
		}
	}

	protected loaderPresent(message?: string) {
		const loadingMessage = message || this.loadingMessage || "";
		if (loadingMessage) return this.utils.loaderPresent(loadingMessage);
		return Promise.resolve();
	}

	protected loaderDismiss() {
		this.utils.loaderDismiss();
	}

	private async doEnter() {
		const loadingMessage = this.enterMessage || "";
		if (loadingMessage) this.utils.loaderPresent(loadingMessage);
		try {
			await this.onEnter();
			this.initialized = true;
		} catch (err) {
			this.logger.exception(err);
			this.initialized = false;
			this.onError(err);
		} finally {
			if (loadingMessage) this.utils.loaderDismiss();
		}
	}

	private async doReenter() {
		const loadingMessage = this.reenterMessage || "";
		if (loadingMessage) this.utils.loaderPresent(loadingMessage);
		try {
			await this.onReenter();
		} catch (err) {
			this.logger.exception(err);
			this.onError(err);
		} finally {
			if (loadingMessage) this.utils.loaderDismiss();
		}
	}
}

export abstract class ProtectedPage extends SimplePage {

	constructor(injector: Injector) {
		super(injector);
	}

	ionViewWillEnter() {
		this.ensureLoggedIn();
	}

	protected async toLoginPage() {
		// TIP: Se houver necessidade no futuro, emitir um evento e deixar que algum outro
		// serviço faça o redirecionamento para a página de login, já que importar o
		// LoginPage neste arquivo causará imports cíclicos.
	}

	private ensureLoggedIn() {
		if (!this.authStore.isAuthenticated()) {
			this.toLoginPage();
			return false;
		}
		return true;
	}
}
