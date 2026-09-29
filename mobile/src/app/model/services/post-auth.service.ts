import { Injectable } from "@angular/core";
import { Platform } from "ionic-angular";

import { AuthStore, isValidAuthState } from "../../commons/stores/auth.store";
import { LoggedUserResource } from "./resources/logged-user.resource";
import { LoggedUserStore } from "./stores/logged-user.store";
import { LoggerService, Logger } from "../../commons/services/logger.service";

@Injectable()
export class PostAuthService {

	private logger: Logger;

	constructor(
		private platform: Platform,
		private authStore: AuthStore,
		private loggedUserResource: LoggedUserResource,
		private loggedUserStore: LoggedUserStore,
		private loggerService: LoggerService,
	) {
		this.logger = this.loggerService.for(PostAuthService);
	}

	register() {
		this.logger.debug("Registrando observers para o AuthStore...");
		this.platform.ready().then(() => this.authStore.subscribe({
			next: async (authState) => {
				const log = this.logger.atMethod(PostAuthService, "register");
				if (isValidAuthState(authState)) {
					if (this.loggedUserStore.empty()) {
						const usuario = await this.loggedUserResource.getOne();
						log.debug("Informações de usuário autenticado obtidas:", usuario);
						this.loggedUserStore.set(usuario);
					}
				} else {
					log.debug("Informações de usuário autenticado apagadas.");
					this.loggedUserStore.clear();
				}
			},
		}));
	}
}
