import { Component, Injector } from "@angular/core";

import { FeedforwardResource } from "./feedforwards.resource";
import {
	FeedforwardFuncionarioTO,
	FeedforwardTipoCompetenciaTO,
} from "../utils/apontamentoFuncional/transfer/feedforward.transfer";
import {
	createFeedforwardPageState,
	FeedforwardPageState,
} from '../utils/apontamentoFuncional/apontamentoFuncionalPage.state';
import { ProtectedPage } from "../utils/page";
import { NovoFeedforwardPage } from "./novo-feedforward/novo-feedforward.page";

@Component({
	selector: "app-feedforwards",
	templateUrl: "./feedforwards.page.html",
})
export class FeedforwardsPage extends ProtectedPage {

	protected reenterMessage = undefined;

	private state: FeedforwardPageState;

	constructor(
		injector: Injector,
		feedforwardResource: FeedforwardResource,
	) {
		super(injector);
		this.state = createFeedforwardPageState(feedforwardResource);
	}

	get avaliado() { return this.state.avaliado; }
	get tipoCompetencia() { return this.state.tipoCompetencia; }
	get avaliados() { return this.state.avaliados; }
	get tiposCompetencia() { return this.state.tiposCompetencia; }
	get feedforwards() { return this.state.apontamentosFuncionais; }

	onSelectedAvaliado = async (avaliado?: FeedforwardFuncionarioTO) => {
		await this.state.selectAvaliado(avaliado);
	}

	onSelectedTipoCompetencia = async (tipoCompetencia?: FeedforwardTipoCompetenciaTO) => {
		await this.state.selectTipoCompetencia(tipoCompetencia);
	}

	onClickNovoFeedForward = () => {
		this.navController.push(NovoFeedforwardPage, {
			avaliado: this.avaliado!,
			tipoCompetencia: this.tipoCompetencia!,
		});
	};

	protected async onEnter() {
		await this.state.enterPage();
	}

	protected async onReenter() {
		await this.state.reenterPage();
	}
}

