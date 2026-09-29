import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CommonsModule } from '../../commons/commons.module';
import { AvaliacaoListPage } from './avaliacao-list/avaliacao-list.page';
import { AvaliacaoViewPage } from './avaliacao-view/avaliacao-view.page';
import { CompetenciaProgress } from './avaliacao-view/competencia-progress/competencia-progress';
import { AvatarAvaliadosComponent } from './avaliacao-view/detalhamento-view/components/avatar-avaliados-component/avatar-avaliados.component';
import { DescricaoCompetenciaComponent } from './avaliacao-view/detalhamento-view/components/descricao-competencia-component/descricao-competencia.component';
import { DetalhamentoViewPage } from './avaliacao-view/detalhamento-view/detalhamento-view.page';
import { TipoCompetenciaInfo } from './avaliacao-view/tipo-competencia-info/tipo-competencia-info';
import { DetalhamentosSlideComponent } from './avaliacao-view/detalhamento-view/components/detalhamentos-slide-component/detalhamentos-slide.component';
import { PreenchimentosComponent } from './avaliacao-view/detalhamento-view/components/preenchimentos-component/preenchimentos.component';
import { HintModal } from './avaliacao-view/detalhamento-view/components/label-component/hint-modal/hint.modal';
import { LabelComponent } from './avaliacao-view/detalhamento-view/components/label-component/label.component';
import { DetalhamentoComponent } from './avaliacao-view/detalhamento-view/components/detalhamento-component/detalhamento.component';
import { AvaliacaoResource } from './avaliacao.resource';

@NgModule({
  declarations: [
    AvaliacaoViewPage,
	AvaliacaoListPage,
	DetalhamentoViewPage,
    TipoCompetenciaInfo,
	CompetenciaProgress,
	DescricaoCompetenciaComponent,
	AvatarAvaliadosComponent,
	DetalhamentosSlideComponent,
	PreenchimentosComponent,
	LabelComponent,
	DetalhamentoComponent,
	HintModal
  ],
  imports: [
    IonicPageModule.forChild(AvaliacaoListPage),
    CommonModule,
	CommonsModule,
  ],
  entryComponents: [
    AvaliacaoViewPage,
	AvaliacaoListPage,
	DetalhamentoViewPage,
	HintModal
  ],
  providers: [AvaliacaoResource]
})
export class AvaliacaoPageModule {}
