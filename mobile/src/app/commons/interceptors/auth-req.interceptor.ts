import { Injectable } from "@angular/core";
import { HttpRequest, HttpHandler, HttpInterceptor } from "@angular/common/http";

import { AuthStore } from "../stores/auth.store";

@Injectable()
export class HttpAuthRequestInterceptor implements HttpInterceptor {
	constructor(private authStore: AuthStore) {}

	intercept(request: HttpRequest<any>, next: HttpHandler) {
		const authData = this.authStore.authenticated();
		if (authData) {
			// TODO Incluir aqui o os headers (conforme necessário) de acordo com a nova forma de login
			// (que o Vini ainda vai implementar)
			// Ou alterar o request conforme necessário...
		}
		return next.handle(request);
	}
}
