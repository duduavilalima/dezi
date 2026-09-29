import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { EsqueciSenhaPage } from "./esqueci-senha.page";
import { CommonsModule } from "../../commons/commons.module";
import { EsqueciSenhaResource } from './esqueci-senha.resource';

@NgModule({
	imports: [
		CommonModule,
		CommonsModule,
	],
	declarations: [EsqueciSenhaPage],
	entryComponents: [EsqueciSenhaPage],
	providers: [EsqueciSenhaResource],
})
export class EsqueciSenhaPageModule {}
