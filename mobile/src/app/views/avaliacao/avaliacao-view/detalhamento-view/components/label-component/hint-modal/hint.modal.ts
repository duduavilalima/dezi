import { Component } from "@angular/core";
import { NavParams, ViewController } from "ionic-angular";

@Component({
	selector: "app-hint-modal",
	template: `
		<ion-content no-bounce class="bg-transparent" (click)="onDismiss()">
			<ion-card class="hint">{{ hint }}</ion-card>
		</ion-content>
	`,
})
export class HintModal {
	hint: string;
	constructor(public viewCtrl: ViewController, public navParams: NavParams) {
		this.hint = navParams.get("hint");
		console.log("entrou!");
	}

	onDismiss() {
		this.viewCtrl.dismiss();
	}
}
