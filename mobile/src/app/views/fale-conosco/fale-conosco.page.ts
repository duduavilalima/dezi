import { Component, Injector } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";

import { FaleConoscoTO, ClassificacaoFaleConoscoTO } from "./fale-conosco.transfer";
import { FaleConoscoResource } from "./fale-conosco.resource";
import { ProtectedPage } from '../utils/page';

@Component({
	selector: "app-fale-conosco",
	templateUrl: "./fale-conosco.page.html",
})
export class FaleConoscoPage extends ProtectedPage {
	public classificacoes: { valor: string; nome: string }[] = [];

	public form: FormGroup;

	constructor(
		injector: Injector,
		private faleConoscoResource: FaleConoscoResource,
	) {
		super(injector);
		this.form = new FormGroup({
			assunto: new FormControl(null, {
				validators: [Validators.required],
			}),
			classificacao: new FormControl(null, {
				validators: [Validators.required],
			}),
			mensagem: new FormControl(null, {
				validators: [Validators.required],
			}),
		});

		Object.keys(ClassificacaoFaleConoscoTO).forEach(valor => {
			this.classificacoes.push({ valor: valor, nome: ClassificacaoFaleConoscoTO[valor] });
		});
	}

	onSubmitForm = async () => {
		const faleConosco: FaleConoscoTO = this.form.value;
		const classificacaoKey = `app.views.fale-conosco.fields.classificacoes.${ClassificacaoFaleConoscoTO[faleConosco.classificacao!]}`;
		const classificacao = await this.translator.get(classificacaoKey);
		this.loaderPresent();
		try {
			await this.faleConoscoResource.create(faleConosco);
			const mensagemSucesso = await this.translator.get("app.views.fale-conosco.messages.success", { classificacao });
			this.loaderDismiss();
			this.utils.presentToast(mensagemSucesso);
		} catch (err) {
			this.logger.exception(err);
			this.loaderDismiss();
			const mensagemErro = await this.translator.get("app.views.fale-conosco.messages.error", { classificacao });
			this.utils.presentToast(mensagemErro);
		}
	};
}
