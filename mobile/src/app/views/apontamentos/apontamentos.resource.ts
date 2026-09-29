import { Injectable } from "@angular/core";

import { API } from '../../constants/api';
import { HttpResourceService } from "../../commons/services/http-resource.service";
import {
	HttpResourceDescriptor,
	ResourceAuthStatus,
} from "../../utils/resource";

import {
	ApontamentoTO,
	ApontamentoCreateTO,
	ApontamentoFilterTO,
	ApontamentoTipoCompetenciaTO,
	ApontamentoFuncionarioTO,
	ApontamentoCompetenciaTO,
} from "../utils/apontamentoFuncional/transfer/apontamento.transfer";
import { toString } from '../../utils/collections';
import { JsonValue } from '../../utils/json';
import { isObject } from '../../utils/guards';
import { RequiredSlice } from '../../utils/typescript';

const descriptorBase = {
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	basePath: `${API.contextPrefix}/apontamento`,
};


type ApontamentoResourceDescriptor = HttpResourceDescriptor<
	ApontamentoTO,
	ApontamentoCreateTO,
	ApontamentoFilterTO>;

@Injectable()
export class ApontamentoResource {

	descriptor: ApontamentoResourceDescriptor = {
		...descriptorBase,
		name: "Apontamento",
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

	tipoCompetenciaDescriptor: HttpResourceDescriptor<ApontamentoTipoCompetenciaTO> = {
		...descriptorBase,
		name: "Apontamento.TipoCompetencia",
		actions: {
			list: { path: "all/tipoCompetencia" },
		},
	};

	avaliadoDescriptor: HttpResourceDescriptor<ApontamentoFuncionarioTO> = {
		...descriptorBase,
		name: "Apontamento.Avaliado",
		actions: {
			list: { path: "all/avaliado" },
		}
	};

	competenciaDescriptor: HttpResourceDescriptor<ApontamentoCompetenciaTO> = {
		...descriptorBase,
		name: "Apontamento.Competencia",
		actions: {
			list: { path: "competencia/all" },
		}
	};

	constructor(
		protected httpResource: HttpResourceService,
	) {}

	async listAvaliado() {
		return this.httpResource.successfulResponse<ApontamentoFuncionarioTO[]>({
			action: "list",
			params: {},
			resource: this.avaliadoDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async listTipoCompetenciaByAvaliadoId(avaliadoId: number) {
		return this.httpResource.successfulResponse<ApontamentoTipoCompetenciaTO[]>({
			action: "list",
			params: { avaliadoId },
			resource: this.tipoCompetenciaDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async listCompetenciaByTipoCompetenciaId(tipoCompetenciaId: number) {
		return this.httpResource.successfulResponse<ApontamentoCompetenciaTO[]>({
			action: "list",
			params: { tipoCompetenciaId },
			resource: this.competenciaDescriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async list(filter: RequiredSlice<ApontamentoFilterTO, "avaliadoId" | "tipoCompetenciaId">) {
		return this.httpResource.successfulResponse<ApontamentoTO[]>({
			action: "list",
			params: filter,
			resource: this.descriptor,
		})
		.map(value => value || [])
		.toPromise();
	}

	async create(entity: ApontamentoCreateTO) {
		return this.httpResource.successfulResponse<ApontamentoTO[]>({
			action: "create",
			params: entity,
			resource: this.descriptor,
		})
		.toPromise();
	}
}
