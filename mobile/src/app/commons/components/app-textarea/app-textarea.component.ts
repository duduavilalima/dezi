import { Component, Input } from "@angular/core";
import { FormGroup } from "@angular/forms";


@Component({
	selector: "app-textarea",
	templateUrl: "app-textarea.component.html",
})
export class TextareaComponent {
	@Input() isApontamento: boolean = false;

	/* private _apontamento: string;
	private _feedforward: string; */
	private _parentForm: FormGroup;

	constructor() { }

	/* get apontamento() {
		return this._apontamento;
	}

	@Input()
	set apontamento(value: number) {
		this._apontamento = value;
	} */

	get parentForm() {
		return this._parentForm;
	}

	@Input()
	set parentForm(value: FormGroup) {
		this._parentForm = value;
	}

}
