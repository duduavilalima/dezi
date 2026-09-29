import { Component, Input } from "@angular/core";

@Component({
	selector: "app-progress-bar",
	templateUrl: "progress-bar.html",
})
export class ProgressBar {
	@Input() progress: number;

	constructor() {}
}
