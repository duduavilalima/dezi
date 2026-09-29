import { Component, EventEmitter, Input, Output } from "@angular/core";
import { AvaliacaoDetalhamentoTO } from "../../../../avaliacao.transfer";

@Component({
	selector: "app-detalhamento",
	templateUrl: "detalhamento.component.html",
})
export class DetalhamentoComponent {
	private _detalhamento: AvaliacaoDetalhamentoTO;
	@Output() updatedDetalhamento = new EventEmitter<AvaliacaoDetalhamentoTO>();

	constructor() {}

	@Input()
	set detalhamento(value: AvaliacaoDetalhamentoTO) {
		this._detalhamento = value;
	}

	get detalhamento() {
		return this._detalhamento;
	}

	onUpdatePreenchimento() {
		this.updatedDetalhamento.emit(this.detalhamento);
	}
}
