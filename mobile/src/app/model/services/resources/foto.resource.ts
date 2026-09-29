import { Injectable } from "@angular/core";
import { HttpResourceService } from "../../../commons/services/http-resource.service";
import { ensureObject } from "../../../utils/guards";
import { HttpResourceDescriptor, ResourceAuthStatus } from "../../../utils/resource";
import { FotoTO } from '../../transfer/foto.transfer';

const descriptor: HttpResourceDescriptor<FotoTO> = {
	name: "FotoAvatar",
	basePath: "foto",
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	actions: {
		get: { path: ({ id }) => `${id}/avatar` },
	},
};

@Injectable()
export class FotoAvatarResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async getOne(funcionarioId: number) {
		const foto = await this.httpResource
			.successfulResponse<FotoTO>({
				action: "get",
				resource: descriptor,
				params: { id: funcionarioId },
			})
			.map(value => ensureObject(value))
			.toPromise();

		return foto;
	}
}
