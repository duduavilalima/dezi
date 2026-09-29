import { NgModule } from "@angular/core";
import { ApontamentosPageModule } from "./apontamentos/apontamentos.module";
import { AvaliacaoPageModule } from "./avaliacao/avaliacao.module";
import { EsqueciSenhaPageModule } from "./esqueci-senha/esqueci-senha.module";
import { FaleConoscoModule } from "./fale-conosco/fale-conosco.module";
import { FeedforwardsRecebidosModule } from "./feedforwards-recebidos/feedforwards-recebidos.module";
import { FeedforwardsPageModule } from "./feedforwards/feedforwards.module";
import { LoginPageModule } from "./login/login.module";
import { LogoutPageModule } from "./logout/logout.module";


const MODULES = [];

const PAGE_MODULES = [
	EsqueciSenhaPageModule,
	AvaliacaoPageModule,
	LoginPageModule,
	LogoutPageModule,
	FaleConoscoModule,
	FeedforwardsPageModule,
	FeedforwardsRecebidosModule,
	ApontamentosPageModule,
];

@NgModule({
	imports: [MODULES, PAGE_MODULES],
	exports: [PAGE_MODULES],
})
export class ViewsPageModule {}
