import { Component, Injector } from "@angular/core";
import { NavController } from "ionic-angular";
import { ProtectedPage } from "../../utils/page";
import { AvaliacaoViewPage } from "../avaliacao-view/avaliacao-view.page";
import { AvaliacaoResource } from "../avaliacao.resource";
import { AvaliacaoTO } from "../avaliacao.transfer";

@Component({
	selector: "app-avaliacao-list",
	templateUrl: "./avaliacao-list.page.html",
})
export class AvaliacaoListPage extends ProtectedPage {
	public avaliacoes: AvaliacaoTO[] = [];

	constructor(
		private injector: Injector,
		private navCtrl: NavController,
		private avaliacaoResource: AvaliacaoResource
	) {
		super(injector);
	}

	protected async onEnter() {
		const avaliacoes = await this.avaliacaoResource.listAvaliacao();
		this.avaliacoes = avaliacoes;
	}

	protected async onReenter() {
		this.avaliacoes = [];
		const avaliacoes = await this.avaliacaoResource.listAvaliacao();
		this.avaliacoes = avaliacoes;
	}

	onClickAvaliacao(avaliacao: AvaliacaoTO) {
		this.navCtrl.push(AvaliacaoViewPage, { id: avaliacao.id });
	}
}
