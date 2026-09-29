import { Injectable } from "@angular/core";
import { HttpResourceService } from "../../commons/services/http-resource.service";
import { API } from "../../constants/api";
import { HttpResourceDescriptor, ResourceAuthStatus } from "../../utils/resource";
import { AvaliacaoCompetenciaTO, AvaliacaoDetalhamentoTO, AvaliacaoTO } from "./avaliacao.transfer";

const descriptorBase = {
	requiredAuthStatus: ResourceAuthStatus.AUTHENTICATED,
	basePath: `${API.contextPrefix}/avaliacao`,
};

type AvaliacaoResourceDescriptor = HttpResourceDescriptor<AvaliacaoTO>;

@Injectable()
export class AvaliacaoResource {
	avaliacaoDescriptor: AvaliacaoResourceDescriptor = {
		...descriptorBase,
		name: "Avaliacao",
		actions: {
			list: { path: "all" },
			get: { path: ({ id }) => `${id}` },
		},
	};

	avaliacaoCompetenciaDescriptor: HttpResourceDescriptor<AvaliacaoCompetenciaTO> = {
		...descriptorBase,
		name: "Avaliacao.Competencia",
		actions: {
			get: { path: ({ id }) => `competencia/${id}` },
		},
	};

	competenciaDetalhamentoDescriptor: HttpResourceDescriptor<AvaliacaoDetalhamentoTO> = {
		...descriptorBase,
		name: "Avaliacao.Competencia.Detalhamento",
		actions: {
			update: { path: "competencia/detalhamento" },
		},
	};

	constructor(protected httpResource: HttpResourceService) {}

	async listAvaliacao() {
		return this.httpResource
			.successfulResponse<AvaliacaoTO[]>({
				action: "list",
				params: {},
				resource: this.avaliacaoDescriptor,
			})
			.map(value => value || [])
			.toPromise();
	}

	async getAvaliacao(avaliacaoId: number) {
		return this.httpResource
			.successfulResponse<AvaliacaoTO>({
				action: "get",
				params: {id: avaliacaoId},
				resource: this.avaliacaoDescriptor,
			})
			.map(value => value || undefined)
			.toPromise();
	}

	async getAvaliacaoCompetencia(avaliacaoCompetenciaId: number) {
		return this.httpResource
			.successfulResponse<AvaliacaoCompetenciaTO>({
				action: "get",
				params: {id: avaliacaoCompetenciaId },
				resource: this.avaliacaoCompetenciaDescriptor,
			})
			.map(value => value || undefined)
			.toPromise();
	}

	async updateListDetalhamento(detalhamentoList: AvaliacaoDetalhamentoTO[]) {
		return this.httpResource
			.successfulResponse<void>({
				action: "update",
				params: detalhamentoList,
				resource: this.competenciaDetalhamentoDescriptor,
			})
			.toPromise();
	}
}
