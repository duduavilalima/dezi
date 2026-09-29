import { Component, Input } from "@angular/core";
import { FuncionarioTO } from "../../../model/transfer/funcionario.transfer";


@Component({
	selector: "app-info-avaliado",
	templateUrl: "app-info-avaliado.component.html",
})
export class InfoAvaliadoComponent {

	private _avaliado: FuncionarioTO;

	constructor() { }

	get avaliado() {
		return this._avaliado;
	}

	@Input()
	set avaliado(value: FuncionarioTO) {
		this._avaliado = value;
	}

}
