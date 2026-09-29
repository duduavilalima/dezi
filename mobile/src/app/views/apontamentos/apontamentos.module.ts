import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { CommonsModule } from "../../commons/commons.module";
import { ApontamentosPage } from "./apontamentos.page";
import { NovoApontamentoPage } from "./novo-apontamento/novo-apontamento.page";
import { ApontamentoResource } from './apontamentos.resource';

@NgModule({
	imports: [CommonModule, CommonsModule],
	declarations: [ApontamentosPage, NovoApontamentoPage],
	entryComponents: [ApontamentosPage, NovoApontamentoPage],
	providers: [ApontamentoResource],
})
export class ApontamentosPageModule {}
