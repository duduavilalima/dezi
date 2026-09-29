import { NgModule } from "@angular/core";

import { LoggedUserStore } from "./services/stores/logged-user.store";
import { LoggedUserResource } from "./services/resources/logged-user.resource";
import { PostAuthService } from "./services/post-auth.service";
import { EmpresaAuthResource } from "./services/resources/auth-empresa.resource";
import { FotoAvatarResource } from './services/resources/foto.resource';

const MODULES = [];

const RESOURCES = [
	EmpresaAuthResource,
	LoggedUserResource,
	FotoAvatarResource,
];

const REPOSITORIES = [
];

const SERVICES = [
	PostAuthService,
];

const DIRECTIVES = [];

const PIPES = [];

const COMPONENTS = [];

const STORES = [
	LoggedUserStore,
];

@NgModule({
	declarations: [COMPONENTS, DIRECTIVES, PIPES],
	imports: [MODULES],
	exports: [MODULES, COMPONENTS, DIRECTIVES, PIPES],
	providers: [STORES, RESOURCES, REPOSITORIES, SERVICES],
})
export class BusinessModule {
	constructor(
		private postAuth: PostAuthService,
	) {
		this.postAuth.register();
	}
}
