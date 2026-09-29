import { Injectable } from "@angular/core";

import { HttpResourceService } from "../../../commons/services/http-resource.service";
import { HttpResourceDescriptor } from "../../../utils/resource";
import { EmpresaAuthTO } from '../../transfer/empresaAuth.transfer';

const descriptor: HttpResourceDescriptor<EmpresaAuthTO> = {
	name: "Auth.Empresa",
	basePath: "auth",
	actions: {
		list: { path: "mobile/empresas" },
	},
};

@Injectable()
export class EmpresaAuthResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async list() {
		return this.httpResource.successfulResponse<EmpresaAuthTO[]>({
			action: "list",
			resource: descriptor,
		})
		.map(value => value || [])
		.toPromise();
	}
}
