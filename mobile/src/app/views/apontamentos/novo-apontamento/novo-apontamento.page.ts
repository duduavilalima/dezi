import { Component, Injector } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";

import { NavParams } from "ionic-angular";

import { ProtectedPage } from '../../utils/page';
import {
	ApontamentoCompetenciaTO,
	ApontamentoTipoCompetenciaTO,
	ApontamentoFuncionarioTO,
	ApontamentoCreateTO,
} from '../../utils/apontamentoFuncional/transfer/apontamento.transfer';
import { ApontamentoResource } from '../apontamentos.resource';

@Component({
	selector: "app-novo-apontamento",
	templateUrl: "./novo-apontamento.page.html",
})
export class NovoApontamentoPage extends ProtectedPage {

	public avaliado: ApontamentoFuncionarioTO;
	public tipoCompetencia: ApontamentoTipoCompetenciaTO;
	public competencias: ApontamentoCompetenciaTO[];
	public form: FormGroup;

	private successMessage: string;

	constructor(
		injector: Injector,
		private navParams: NavParams,
		private apontamentoResource: ApontamentoResource,
	) {
		super(injector);
		this.form = new FormGroup({
			competenciaId: new FormControl(null, {
				validators: [Validators.required],
			}),
			apontamento: new FormControl(null, {
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
		this.successMessage = await this.translator
			.get("app.views.novo-apontamento.messages.success");
		this.errorMessage = await this.translator
			.get("app.views.novo-apontamento.messages.error");
	}

	async onEnter() {
		this.competencias = await this.apontamentoResource
			.listCompetenciaByTipoCompetenciaId(this.tipoCompetencia.id);
	}

	onSubmitForm = async () => {
		this.utils.loaderPresent("Aguarde...");

		try {
			const values = this.form.value;
			const apontamento: ApontamentoCreateTO = {
				avaliado: { id: this.avaliado.id },
				competencia: { id: Number(values.competenciaId) },
				registroCritico: values.apontamento,
				// TODO Rever com o Barata a lógica do componente app-rating
				graduacaoVisual: values.nota * 2,
			};

			await this.apontamentoResource.create(apontamento);

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
