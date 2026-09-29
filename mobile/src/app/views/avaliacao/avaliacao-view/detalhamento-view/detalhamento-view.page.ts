import { Component, Injector } from "@angular/core";
import { NavController, NavParams } from "ionic-angular";
import { ProtectedPage } from "../../../utils/page";
import { AvaliacaoResource } from "../../avaliacao.resource";
import { AvaliacaoCompetenciaTO } from "../../avaliacao.transfer";

@Component({
	selector: "app-detalhamento-view",
	templateUrl: "./detalhamento-view.page.html",
})
export class DetalhamentoViewPage extends ProtectedPage {
	public competencia: AvaliacaoCompetenciaTO | undefined;
	public descricaoTipoCompetencia: string = "";
	constructor(
		private injector: Injector,
		private navParams: NavParams,
		private navCtrl: NavController,
		private avaliacaoResource: AvaliacaoResource
	) {
		super(injector);
	}

	protected async onEnter() {
		const id = this.navParams.get("id");
		const descricaoTipoCompetencia = this.navParams.get("descricaoTipoCompetencia");
		this.descricaoTipoCompetencia = descricaoTipoCompetencia;
		const competencia = await this.avaliacaoResource.getAvaliacaoCompetencia(id);
		this.competencia = competencia;
	}

	onUpdateDetalhamentos({ detalhamentoList, isLast }) {
		this.utils.loaderPresent("Aguarde...");

		this.avaliacaoResource
			.updateListDetalhamento(detalhamentoList)
			.then(_ => {
				this.utils.presentToast("Salvo com sucesso!");
				this.utils.loaderDismiss();
				if (isLast) {
					this.navCtrl.pop();
				}
			})
			.catch(err => {
				console.log(err);
				this.utils.presentToast(err);
				this.utils.loaderDismiss();
			});
	}
}
