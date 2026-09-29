/* Angular */
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

/* Ionic */
import { IonicModule } from "ionic-angular";

/* Pacotes de terceiros */
import { TranslateModule } from "@ngx-translate/core";
import { NgCircleProgressModule } from "ng-circle-progress";

/* Serviços */
import { UtilsService } from "./utils.service";
import { EnvStore } from "./stores/env.store";
import { LocalStorage, AsyncLocalStorage } from "./services/local-storage.service";
import { AuthStore } from "./stores/auth.store";
import { AuthService } from "./services/auth.service";
import { Navigate } from "./services/navigate.service";

/* Componentes */
import { TipoCompetenciaSlides } from "./components/app-tipo-competencia/tipo-competencia-slides";
import { ProgressBar } from "./components/app-progress-bar/progress-bar";
import { AvatarSlide } from "./components/app-avatar-slide/app-avatar-slide";
import { HttpResourceService } from "./services/http-resource.service";
import { ApontamentosCard } from "./components/app-apontamentos-card/app-apontamentos-card";
import { IonicRatingModule } from "ionic-rating";
import { AutosizeDirective } from "./directives/autosize.directive";
import { SeparatorComponent } from "./components/app-separator/app-separator.component";
import { InfoAvaliadoComponent } from "./components/app-info-avaliado/app-info-avaliado.component";
import { RatingComponent } from "./components/app-rating/app-rating.component";
import { CompetenciaComboComponent } from "./components/app-competencia-combo/app-competencia-combo.component";
import { TextareaComponent } from "./components/app-textarea/app-textarea.component";
import { LoggerService } from "./services/logger.service";
import { PageIndicatorComponent } from "./components/app-slide-paginator/app-page-indicator.component";
import { Translator } from "./services/translate.service";

const MODULES = [
	CommonModule,
	IonicModule,
	FormsModule,
	ReactiveFormsModule,
	TranslateModule,
	NgCircleProgressModule,
	IonicRatingModule,
];

const SERVICES = [
	AsyncLocalStorage,
	UtilsService,
	LocalStorage,
	AuthService,
	Navigate,
	HttpResourceService,
	LoggerService,
	Translator,
];

const DIRECTIVES = [AutosizeDirective];

const PIPES = [];

const COMPONENTS = [
	TipoCompetenciaSlides,
	ProgressBar,
	AvatarSlide,
	ApontamentosCard,
	SeparatorComponent,
	InfoAvaliadoComponent,
	CompetenciaComboComponent,
	RatingComponent,
	TextareaComponent,
	PageIndicatorComponent
];

const STORES = [EnvStore, AuthStore];

@NgModule({
	declarations: [COMPONENTS, DIRECTIVES, PIPES],
	imports: [MODULES],
	exports: [MODULES, COMPONENTS, DIRECTIVES, PIPES],
	providers: [SERVICES, STORES],
})
export class CommonsModule { }
