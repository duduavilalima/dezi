import { Injectable } from '@angular/core';
import { HttpResourceDescriptor, HttpResourceMethod } from '../../utils/resource';
import { HttpResourceService } from '../../commons/services/http-resource.service';
import { EsqueciSenhaTO, EsqueciSenhaCreateTO } from './esqueci-senha.transfer';

const descriptor: HttpResourceDescriptor<EsqueciSenhaTO> = {
	name: "ForgotPassword",
	actions: {
		create: {
			method: HttpResourceMethod.POST,
			path: `auth/forgotpassword`,
		},
	},
};

@Injectable()
export class EsqueciSenhaResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async forgotPassword(payload: EsqueciSenhaCreateTO) {
		return this.httpResource
			.successfulResponse<void>({
				payload,
				action: "create",
				resource: descriptor,
			})
			.toPromise();
	}
}
