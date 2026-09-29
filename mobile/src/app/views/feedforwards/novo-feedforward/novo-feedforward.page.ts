import { Component, Injector } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { NavParams } from "ionic-angular";
import {
	FeedforwardCompetenciaTO,
	FeedforwardCreateTO,
	FeedforwardFuncionarioTO,
	FeedforwardTipoCompetenciaTO,
} from "../../utils/apontamentoFuncional/transfer/feedforward.transfer";
import { ProtectedPage } from "../../utils/page";
import { FeedforwardResource } from "../feedforwards.resource";

@Component({
	selector: "app-novo-feedforward",
	templateUrl: "./novo-feedforward.page.html",
})
export class NovoFeedforwardPage extends ProtectedPage {
	public avaliado: FeedforwardFuncionarioTO;
	public tipoCompetencia: FeedforwardTipoCompetenciaTO;
	public competencias: FeedforwardCompetenciaTO[];
	public form: FormGroup;

	private successMessage: string;

	constructor(injector: Injector, private navParams: NavParams, private feedforwardResource: FeedforwardResource) {
		super(injector);
		this.form = new FormGroup({
			competenciaId: new FormControl(null, {
				validators: [Validators.required],
			}),
			feedforward: new FormControl(null, {
				validators: [Validators.required],
			}),
			nota: new FormControl(0, {
				validators: [],
			}),
		});
		this.avaliado = this.navParams.get("avaliado");
		this.tipoCompetencia = this.navParams.get("tipoCompetencia");
	}

	get partiallyLoaded() {
		return !!this.avaliado && !!this.tipoCompetencia;
	}

	async initialize() {
		super.initialize();
		this.successMessage = await this.translator.get("app.views.novo-feedforward.messages.success");
		this.errorMessage = await this.translator.get("app.views.novo-feedforward.messages.error");
	}

	async onEnter() {
		this.competencias = await this.feedforwardResource.listCompetenciaByTipoCompetenciaId(this.tipoCompetencia.id);
	}

	onSubmitForm = async () => {
		this.utils.loaderPresent("Aguarde...");

		try {
			const values = this.form.value;
			const apontamento: FeedforwardCreateTO = {
				avaliado: { id: this.avaliado.id },
				competencia: { id: Number(values.competenciaId) },
				feedforward: values.feedforward,
				// TODO Rever com o Barata a lógica do componente app-rating
				graduacaoVisual: values.nota * 2,
			};

			await this.feedforwardResource.create(apontamento);

			this.utils.presentToast(this.successMessage);
			this.navController.pop();
		} catch (err) {
			this.logger.exception(err);
			this.utils.presentToast(this.errorMessage!);
		} finally {
			this.utils.loaderDismiss();
		}
	};
}
