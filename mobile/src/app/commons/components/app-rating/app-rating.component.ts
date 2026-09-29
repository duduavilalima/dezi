import { Component, Input } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Keyboard } from "@ionic-native/keyboard";


@Component({
	selector: "app-rating",
	templateUrl: "app-rating.component.html",
	providers: [Keyboard]
})
export class RatingComponent {

	private _nota: number;
	private _parentForm: FormGroup;

	constructor() { }

	get nota() {
		return this._nota;
	}

	@Input()
	set nota(value: number) {
		this._nota = value;
	}

	get parentForm() {
		return this._parentForm;
	}

	@Input()
	set parentForm(value: FormGroup) {
		this._parentForm = value;
	}
}
