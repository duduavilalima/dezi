import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { CommonsModule } from "../../commons/commons.module";
import { FeedforwardsRecebidosPage } from "./feedforwards-recebidos.page";
import { FeedforwardRecebidoResource } from './feedforwards-recebidos.resource';

@NgModule({
	imports: [
		CommonModule,
		CommonsModule,
	],
	providers: [FeedforwardRecebidoResource],
	declarations: [FeedforwardsRecebidosPage],
	entryComponents: [FeedforwardsRecebidosPage],
})
export class FeedforwardsRecebidosModule {}
