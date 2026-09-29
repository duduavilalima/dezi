import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { IonicModule } from "ionic-angular";
import { CommonsModule } from "../../commons/commons.module";
import { FaleConoscoPage } from "./fale-conosco.page";
import { FaleConoscoResource } from './fale-conosco.resource';

@NgModule({
	declarations: [FaleConoscoPage],
	imports: [
		CommonModule,
		CommonsModule,
		FormsModule,
		IonicModule,
	],
	entryComponents: [FaleConoscoPage],
	providers: [FaleConoscoResource],
})
export class FaleConoscoModule {}
