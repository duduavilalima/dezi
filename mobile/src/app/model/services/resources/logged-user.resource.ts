import { Injectable } from "@angular/core";

import { HttpResourceService } from "../../../commons/services/http-resource.service";
import { LoggedUserTO } from "../../transfer/loggedUser.transfer";
import { ResourceAuthStatus, HttpResourceDescriptor } from "../../../utils/resource";
import { ensureObject } from "../../../utils/guards";

const descriptor: HttpResourceDescriptor<LoggedUserTO> = {
	name: "LoggedUser",
	basePath: `user/logged`,
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	actions: {
		get: { path: "" },
	},
};

@Injectable()
export class LoggedUserResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async getOne() {
		const usuario = await this.httpResource.successfulResponse<LoggedUserTO>({
			action: "get",
			resource: descriptor,
			params: {},
		})
		.map(value => ensureObject(value))
		.toPromise();
		return usuario;
	}
}
