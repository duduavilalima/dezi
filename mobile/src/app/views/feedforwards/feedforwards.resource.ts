import { Injectable } from "@angular/core";

import { HttpResourceService } from "../../commons/services/http-resource.service";
import { API } from "../../constants/api";
import { toString } from "../../utils/collections";
import { isObject } from "../../utils/guards";
import { JsonValue } from "../../utils/json";
import { ResourceAuthStatus, HttpResourceDescriptor } from "../../utils/resource";
import { RequiredSlice } from "../../utils/typescript";
import {
	FeedforwardCompetenciaTO,
	FeedforwardFuncionarioTO,
	FeedforwardFilterTO,
	FeedforwardCreateTO,
	FeedforwardTipoCompetenciaTO,
	FeedforwardTO,
} from "../utils/apontamentoFuncional/transfer/feedforward.transfer";


const descriptorBase = {
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	basePath: `${API.contextPrefix}/ff`,
};

const descriptor: FeedforwardResourceDescriptor = {
	...descriptorBase,
	name: "Feedforward",
	actions: {
		list: {
			params: ({ avaliadoId, tipoCompetenciaId }) => ({
				avaliadoId: toString(avaliadoId),
				tipoCompetenciaId: toString(tipoCompetenciaId),
			}),
			response(value: JsonValue) {
				if (isObject(value) && typeof value["dataApontamento"] === "string") {
					value["dataApontamento"] = new Date(value["dataApontamento"]);
				}
				return value;
			},
		},
		create: {},
	},
};

const tipoCompetenciaDescriptor: HttpResourceDescriptor<FeedforwardTipoCompetenciaTO> = {
	...descriptorBase,
	name: "Feedforward.TipoCompetencia",
	actions: {
		list: { path: "all/tipoCompetencia" },
	},
};

const avaliadoDescriptor: HttpResourceDescriptor<FeedforwardFuncionarioTO> = {
	...descriptorBase,
	name: "Feedforward.Avaliado",
	actions: {
		list: { path: "all/avaliado" },
	}
};

const competenciaDescriptor: HttpResourceDescriptor<FeedforwardCompetenciaTO> = {
	...descriptorBase,
	name: "Feedforward.Competencia",
	actions: {
		list: { path: "competencia/all" },
	}
};


type FeedforwardResourceDescriptor = HttpResourceDescriptor<
	FeedforwardTO,
	FeedforwardCreateTO,
	FeedforwardFilterTO>;

@Injectable()
export class FeedforwardResource {

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async listAvaliado() {
		return this.httpResource.successfulResponse<FeedforwardFuncionarioTO[]>({
			action: "list",
			params: {},
			resource: avaliadoDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async listTipoCompetenciaByAvaliadoId(avaliadoId: number) {
		return this.httpResource.successfulResponse<FeedforwardTipoCompetenciaTO[]>({
			action: "list",
			params: { avaliadoId },
			resource: tipoCompetenciaDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async listCompetenciaByTipoCompetenciaId(tipoCompetenciaId: number) {
		return this.httpResource.successfulResponse<FeedforwardCompetenciaTO[]>({
			action: "list",
			params: { tipoCompetenciaId },
			resource: competenciaDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async list(params: RequiredSlice<FeedforwardFilterTO, "avaliadoId" | "tipoCompetenciaId">) {
		return this.httpResource.successfulResponse<FeedforwardTO[]>({
			params,
			action: "list",
			resource: descriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async create(input: FeedforwardCreateTO) {
		return this.httpResource.successfulResponse<void>({
			action: "create",
			params: input,
			resource: descriptor,
		})
		.toPromise();
	}
}
