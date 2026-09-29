import { Component, Input } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { AvaliacaoCompetenciaTO } from "../../../views/avaliacao/avaliacao.transfer";

@Component({
	selector: "app-competencia-combo",
	templateUrl: "app-competencia-combo.component.html",
})
export class CompetenciaComboComponent {
	private _competenciaList: AvaliacaoCompetenciaTO[];
	private _parentForm: FormGroup;

	constructor() {}

	get competenciaList() {
		return this._competenciaList;
	}

	@Input()
	set competenciaList(value: AvaliacaoCompetenciaTO[]) {
		this._competenciaList = value;
	}

	get parentForm() {
		return this._parentForm;
	}

	@Input()
	set parentForm(value: FormGroup) {
		this._parentForm = value;
	}
}
