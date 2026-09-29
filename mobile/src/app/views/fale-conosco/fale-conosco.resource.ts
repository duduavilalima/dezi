import { Injectable } from "@angular/core";
import { Platform } from "ionic-angular";
import { HttpResourceService } from "../../commons/services/http-resource.service";
import { API } from '../../constants/api';
import { LoggedUserStore } from "../../model/services/stores/logged-user.store";
import { HttpResourceDescriptor, HttpResourceMethod, ResourceAuthStatus } from "../../utils/resource";
import { FaleConoscoCreateTO, FaleConoscoTO } from "./fale-conosco.transfer";

const descriptor: HttpResourceDescriptor<FaleConoscoTO, FaleConoscoCreateTO> = {
	name: "FaleConosco",
	basePath: `${API.contextPrefix}/faleconosco`,
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	actions: {
		create: {
			method: HttpResourceMethod.POST,
		},
	},
};

@Injectable()
export class FaleConoscoResource {

	constructor(
		protected loggedUserStore: LoggedUserStore,
		protected httpResource: HttpResourceService,
		private platform: Platform,
	) {}

	async create(faleConosco: FaleConoscoCreateTO) {
		const usuario = await this.loggedUserStore.get();
		const payload: FaleConoscoCreateTO = {
			...faleConosco,
			nomeRemetente: usuario.nome,
			emailRemetente: usuario.emailFuncional,
			plataforma: this.platform.platforms().join(", "),
		};
		return await this.httpResource
			.successfulResponse<FaleConoscoTO>({
				payload,
				action: "create",
				resource: descriptor,
				params: {},
			})
			.toPromise();
	}
}
