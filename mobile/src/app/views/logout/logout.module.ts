import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { LogoutPage } from "./logout.component";
import { IonicModule } from "ionic-angular";
import { CommonsModule } from "../../commons/commons.module";

@NgModule({
	imports: [
		CommonModule,
		CommonsModule,
		FormsModule,
		IonicModule,
	],
	declarations: [LogoutPage],
	entryComponents: [LogoutPage],
})
export class LogoutPageModule {}
