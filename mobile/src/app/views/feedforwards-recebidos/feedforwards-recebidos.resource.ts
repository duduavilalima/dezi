import { Injectable } from "@angular/core";

import { HttpResourceService } from "../../commons/services/http-resource.service";
import { API } from "../../constants/api";
import { HttpResourceDescriptor, ResourceAuthStatus } from "../../utils/resource";
import { RequiredSlice } from "../../utils/typescript";

import {
	FeedforwardRecebidoFilterTO,
	FeedforwardRecebidoTipoCompetenciaTO,
	FeedforwardRecebidoTO,
} from "./feedforwards-recebidos.transfer";

const baseDescriptor = {
	basePath: `${API.contextPrefix}/ffrecebido`,
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
};

const descriptor: HttpResourceDescriptor<FeedforwardRecebidoTO> = {
	...baseDescriptor,
	name: "FeedForwardRecebido",
	actions: {
		list: {
			response: value => {
				if (value && typeof value["dataApontamento"] === "string") {
					value["dataApontamento"] = new Date(value["dataApontamento"]);
				}
				return value;
			}
		},
	},
};

const tipoCompetenciaDescriptor: HttpResourceDescriptor<FeedforwardRecebidoTO> = {
	...baseDescriptor,
	name: "FeedForwardRecebido.TipoCompetencia",
	basePath: `${baseDescriptor.basePath}/all/tipocompetencia`,
	actions: {
		list: { path: "" },
	},
};

@Injectable()
export class FeedforwardRecebidoResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async listTiposCompetencia(): Promise<FeedforwardRecebidoTipoCompetenciaTO[]> {
		return this.httpResource.successfulResponse<FeedforwardRecebidoTipoCompetenciaTO[]>({
			action: "list",
			resource: tipoCompetenciaDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async list(params: RequiredSlice<FeedforwardRecebidoFilterTO, "tipoCompetenciaId">) {
		return this.httpResource.successfulResponse<FeedforwardRecebidoTO[]>({
			params,
			action: "list",
			resource: descriptor,
		})
		.map(value => value || [])
		.toPromise();
	}
}
