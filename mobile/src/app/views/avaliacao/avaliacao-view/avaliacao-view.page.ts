import { Component, Injector, ViewChild } from "@angular/core";
import { NavController, NavParams, Slides } from "ionic-angular";
import { ProtectedPage } from "../../utils/page";
import { AvaliacaoResource } from "../avaliacao.resource";
import { AvaliacaoCompetenciaTO, AvaliacaoTipoCompetenciaTO, AvaliacaoTO } from "../avaliacao.transfer";
import { DetalhamentoViewPage } from "./detalhamento-view/detalhamento-view.page";

@Component({
	selector: "app-avaliacao-view",
	templateUrl: "./avaliacao-view.page.html",
})
export class AvaliacaoViewPage extends ProtectedPage {
	@ViewChild(Slides) slides: Slides;

	public avaliacao: AvaliacaoTO;
	private avaliacaoId: number;
	public selectedTipoId: number;
	public avaliacaoTipoCompetencia: AvaliacaoTipoCompetenciaTO;

	// Cache
	private avaliacaoById: { [avaliacaoId: string]: AvaliacaoTO[] } = {};

	// Sequencing
	private lastPromise: Promise<any> = Promise.resolve();

	constructor(
		private injector: Injector,
		private navParams: NavParams,
		private navCtrl: NavController,
		private avaliacaoResource: AvaliacaoResource
	) {
		super(injector);
	}

	protected async onEnter() {
		this.avaliacaoById = {};
		this.avaliacaoId = this.navParams.get("id");
		this.onLoadAvaliacao(this.avaliacaoId);
	}

	protected async onReenter() {
		this.onLoadAvaliacao(this.avaliacaoId);
		this.avaliacaoTipoCompetencia = this.avaliacao.avaliacaoTipoCompetenciaList!.find(avTipo => avTipo.id === this.selectedTipoId)!;
		/* this.lastPromise = this.lastPromise.then(async () => {
			const key = this.getAvaliacaoCacheId();
			delete this.avaliacaoById[key];
			await this.avaliacaoResource.getAvaliacao(this.avaliacaoId);
		}); */
	}

	/* 	private getAvaliacaoCacheId(avaliacaoId?: number) {
			avaliacaoId = avaliacaoId || this.getAvaliacaoId();
			const key = `${avaliacaoId}`;
			return key;
		}
	
		private getAvaliacaoId(avaliacao?: AvaliacaoTO) {
			return ensureObject(avaliacao || this.avaliacao).id;
		}
	 */
	/* Retorno do "emit" do slider de tipo de competencia */
	onLoadTipo(avTipoCompetencia: AvaliacaoTipoCompetenciaTO) {
		this.avaliacaoTipoCompetencia = avTipoCompetencia;
	}

	onLoadCompetencia(competencia: AvaliacaoCompetenciaTO) {
		this.selectedTipoId = this.avaliacaoTipoCompetencia.id;
		this.navCtrl.push(DetalhamentoViewPage, {
			id: competencia.id,
			descricaoTipoCompetencia: this.avaliacaoTipoCompetencia.descricaoTipoCompetencia
		});
	}

	async onLoadAvaliacao(avaliacaoId: number) {
		const avaliacao = await this.avaliacaoResource.getAvaliacao(avaliacaoId);
		if (avaliacao) {
			this.avaliacao = avaliacao;
		}
	}
}
