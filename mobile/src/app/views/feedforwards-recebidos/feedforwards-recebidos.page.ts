import { Component, Injector } from "@angular/core";

import { ProtectedPage } from "../utils/page";

import { FeedforwardRecebidoResource } from "./feedforwards-recebidos.resource";
import {
	FeedforwardRecebidoTO,
	FeedforwardRecebidoTipoCompetenciaTO,
} from "./feedforwards-recebidos.transfer";

@Component({
	selector: "app-feedforwards-recebidos",
	templateUrl: "./feedforwards-recebidos.page.html",
})
export class FeedforwardsRecebidosPage extends ProtectedPage {
	tipoCompetencia?: FeedforwardRecebidoTipoCompetenciaTO;
	tiposCompetencia: FeedforwardRecebidoTipoCompetenciaTO[] = [];

	feedforwards: FeedforwardRecebidoTO[] = [];

	private feedforwardsCache: { [tipoCompetenciaId: number]: FeedforwardRecebidoTO[] } = {};

	constructor(
		injector: Injector,
		private ffRecebidoResource: FeedforwardRecebidoResource,
	) {
		super(injector);
	}

	get empty()  {
		return !this.tiposCompetencia || this.tiposCompetencia.length === 0;
	}

	async onSelectedTipoCompetencia(event$: FeedforwardRecebidoTipoCompetenciaTO) {
		this.tipoCompetencia = event$;
		if (this.tipoCompetencia) {
			this.feedforwards = await this.listFeedforwards();
		} else {
			this.feedforwards = [];
		}
	}

	protected async initialize() {
		const tiposCompetencia = await this.ffRecebidoResource.listTiposCompetencia();
		this.tiposCompetencia = tiposCompetencia;
		if (tiposCompetencia && tiposCompetencia.length > 0) {
			this.onSelectedTipoCompetencia(this.tiposCompetencia[0]);
		}
	}

	private async listFeedforwards() {
		const tipoCompetenciaId = this.tipoCompetencia!.id;
		const key = tipoCompetenciaId;
		let feedforwards = this.feedforwardsCache[key];
		if (!feedforwards) {
			feedforwards = await this.ffRecebidoResource.list({ tipoCompetenciaId });
			this.feedforwardsCache[key] = feedforwards;
		}
		return feedforwards;
	}
}
