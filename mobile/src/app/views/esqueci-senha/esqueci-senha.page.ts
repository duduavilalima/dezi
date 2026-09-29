import { Component, Injector } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { SimplePage } from '../utils/page';
import { EsqueciSenhaCreateTO } from './esqueci-senha.transfer';
import { EsqueciSenhaResource } from './esqueci-senha.resource';

@Component({
	selector: "app-esqueci-senha",
	templateUrl: "./esqueci-senha.page.html",
})
export class EsqueciSenhaPage extends SimplePage {

	public form: FormGroup;

	private mensagemSucesso = "Sua senha foi redefinida com sucesso! Em pouco tempo, você receberá sua nova senha por e-mail.";
	/* Não tem mensagem de erro, apenas de sucesso */
	//private mensagemErro = "Este e-mail não pertence a nenhum usuário.";

	constructor(
		injector: Injector,
		private esqueciSenhaResource: EsqueciSenhaResource,
	) {
		super(injector);
		this.form = new FormGroup({
			emailFuncional: new FormControl(null, {
				validators: [Validators.required, Validators.email],
			}),
		});
	}

	async initialize() {
		await super.initialize();
		this.mensagemSucesso = await this.translator.get("app.views.esqueci-senha.messages.changed-success");
		//this.mensagemErro = await this.translator.get("app.views.esqueci-senha.messages.changed-error");
	}

	onBack = () => {
		this.navController.pop();
	};

	onSubmitForm = async () => {
		this.utils.loaderPresent(this.enterMessage!);
		const func: EsqueciSenhaCreateTO = this.form.value;
		try {
			await this.esqueciSenhaResource.forgotPassword(func);
			this.utils.presentToast(this.mensagemSucesso);
		} catch (err) {
			this.logger.exception(err);
			//this.utils.presentToast(this.mensagemErro);
		} finally {
			this.utils.loaderDismiss();
			this.navController.pop();
		}
	};
}
