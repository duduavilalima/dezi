import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CommonsModule } from "../../commons/commons.module";
import { FeedforwardsPage } from "./feedforwards.page";
import { NovoFeedforwardPage } from "./novo-feedforward/novo-feedforward.page";
import { FeedforwardResource } from './feedforwards.resource';

@NgModule({
	imports: [
		CommonModule,
		CommonsModule,
	],
	declarations: [
		FeedforwardsPage,
		NovoFeedforwardPage,
	],
	entryComponents: [
		FeedforwardsPage,
		NovoFeedforwardPage,
	],
	providers: [
		FeedforwardResource,
	],
})
export class FeedforwardsPageModule {}
