import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { IonicModule } from "ionic-angular";

import { LandingPage } from "./landing.page";
import { CommonsModule } from "../../commons/commons.module";

@NgModule({
	imports: [
		CommonModule,
		CommonsModule,
		FormsModule,
		IonicModule,
	],
	declarations: [LandingPage],
	entryComponents: [LandingPage],
})
export class LandingPageModule {}
