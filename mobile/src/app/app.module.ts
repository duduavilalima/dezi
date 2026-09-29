import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { ErrorHandler, NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { SplashScreen } from "@ionic-native/splash-screen";
import { StatusBar } from "@ionic-native/status-bar";
import { TranslateLoader, TranslateModule, TranslateService, LangChangeEvent } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { IonicApp, IonicErrorHandler, IonicModule, Config, Platform } from "ionic-angular";
import { NgCircleProgressModule } from "ng-circle-progress";
import { MyApp } from "./app.component";
import { CommonsModule } from "./commons/commons.module";
import { HttpAuthErrorInterceptor } from "./commons/interceptors/auth-error.interceptor";
import { HttpAuthRequestInterceptor } from "./commons/interceptors/auth-req.interceptor";
import { BusinessModule } from "./model/business.module";
import { isValidLoggedUserState, LoggedUserStore } from "./model/services/stores/logged-user.store";
import { AvaliacaoPageModule } from "./views/avaliacao/avaliacao.module";
import { LandingPageModule } from "./views/landing/landing.module";
import { ViewsPageModule } from "./views/views.module";
import { Network } from '@ionic-native/network';

export function createTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, "./assets/i18n/", ".json");
}

@NgModule({
	declarations: [MyApp],
	imports: [
		BrowserModule,
		CommonsModule,
		BusinessModule,
		LandingPageModule,
		AvaliacaoPageModule,
		ViewsPageModule,
		IonicModule.forRoot(MyApp, { scrollAssist: false, autoFocusAssist: false }),
		HttpClientModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: createTranslateLoader,
				deps: [HttpClient],
			},
		}),
		NgCircleProgressModule.forRoot({
			// set defaults here
			radius: 50,
			//   space: -19,
			space: -14,
			titleFontSize: "20",
			unitsFontSize: "20",
			outerStrokeWidth: 14,
			subtitleFontSize: "14",
			showInnerStroke: true,
			//   innerStrokeWidth: 21,
			innerStrokeWidth: 14,
			outerStrokeColor: "#9ed44d",
			innerStrokeColor: "#e7e8ea",
			animation: true,
			animationDuration: 600,
			maxPercent: 100,
			renderOnClick: false,
			subtitle: "concluido",
			responsive: true,
		}),
	],
	bootstrap: [IonicApp],
	entryComponents: [MyApp],
	providers: [
		StatusBar,
		SplashScreen,
		Network,
		{ provide: ErrorHandler, useClass: IonicErrorHandler },
		{ provide: HTTP_INTERCEPTORS, useClass: HttpAuthRequestInterceptor, multi: true },
		{ provide: HTTP_INTERCEPTORS, useClass: HttpAuthErrorInterceptor, multi: true },
	],
})
export class AppModule {
	defaultLanguage = "pt_BR";

	constructor(
		private platform: Platform,
		private loggedUserStore: LoggedUserStore,
		private config: Config,
		public translate: TranslateService
	) {
		this.translate.onLangChange.subscribe((ev: LangChangeEvent) => {
			const { translations } = ev;
			const backButtonText = translations.app.components.back.button;
			this.config.set('ios', 'backButtonText', backButtonText);
		});
		const lang = this.platform.lang() || this.defaultLanguage;
		this.translate.setDefaultLang(this.defaultLanguage);
		this.translate.use(lang);
		this.loggedUserStore.subscribe({
			next: state => {
				if (isValidLoggedUserState(state)) {
					const { usuario } = state;
					const linguagem = usuario.configuracao.linguagem || this.defaultLanguage;
					this.translate.use(linguagem);
				} else {
					this.translate.use(this.defaultLanguage);
				}
			},
		});
	}
}
