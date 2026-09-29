import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { IonicModule } from "ionic-angular";

import { LoginPage } from "./login.page";
import { CommonsModule } from "../../commons/commons.module";

@NgModule({
	declarations: [LoginPage],
	imports: [
		CommonModule,
		CommonsModule,
		FormsModule,
		IonicModule,
	],
	entryComponents: [LoginPage],
})
export class LoginPageModule {}
