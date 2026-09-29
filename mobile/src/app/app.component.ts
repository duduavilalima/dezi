import { Component, ViewChild } from "@angular/core";
import { Network } from "@ionic-native/network";
import { SplashScreen } from "@ionic-native/splash-screen";
import { StatusBar } from "@ionic-native/status-bar";
import { AlertController, Events, LoadingController, Nav, Platform } from "ionic-angular";
import { Translator } from "./commons/services/translate.service";
import { AuthStore } from "./commons/stores/auth.store";
import { EnvStore } from "./commons/stores/env.store";
import { TOPIC } from "./constants/topics";
import { FotoAvatarResource } from './model/services/resources/foto.resource';
import { isValidLoggedUserState, LoggedUserStore } from "./model/services/stores/logged-user.store";
import { isBoolean, ensureString } from "./utils/guards";
import { ApontamentosPage } from "./views/apontamentos/apontamentos.page";
import { AvaliacaoListPage } from "./views/avaliacao/avaliacao-list/avaliacao-list.page";
import { FaleConoscoPage } from "./views/fale-conosco/fale-conosco.page";
import { FeedforwardsRecebidosPage } from "./views/feedforwards-recebidos/feedforwards-recebidos.page";
import { FeedforwardsPage } from "./views/feedforwards/feedforwards.page";
import { LandingPage } from "./views/landing/landing.page";
import { LogoutPage } from "./views/logout/logout.component";

interface Item {
	title: string;
	icon?: string;
	component: any;
	authenticated?: boolean;
}

const dadosSidebar = {
	imagemSidebar: "https://gravatar.com/avatar/dba6bae8c566f9d4041fb9cd9ada7741?d=identicon&f=y",
	tituloSidebar: "Administrador",
	subtituloSidebar: "",
};

@Component({
	templateUrl: "app.html",
})
export class MyApp {
	@ViewChild(Nav) nav: Nav;

	rootPage: any = LandingPage;

	menu: Array<Item>;
	submenu: Array<Item>;
	activeMenu: string;

	imagemSidebar = "https://gravatar.com/avatar/dba6bae8c566f9d4041fb9cd9ada7741?d=identicon&f=y";
	tituloSidebar = "Administrador";
	subtituloSidebar = "";

	version = "0.0.0";
	environment = "Dev.";
	private mensagemErro = "Conexão perdida... Verifique a conexão com a internet e/ou aguarde a conexão voltar";

	constructor(
		public envStore: EnvStore,
		public platform: Platform,
		public statusBar: StatusBar,
		public splashScreen: SplashScreen,
		private authStore: AuthStore,
		private loggedUserStore: LoggedUserStore,
		private events: Events,
		private translator: Translator,
		private network: Network,
		private loadingController: LoadingController,
		private alertController: AlertController,
		private fotoAvatarResource: FotoAvatarResource,
	) {
		this.initializeApp();
		this.events.subscribe(TOPIC.menu.auth, authStatus => {
			if (authStatus) {
				this.activeMenu = "avaliacoes";
			} else {
				this.activeMenu = "";
			}
		});
		// used for an example of ngFor and navigation
		this.menu = [
			{
				title: "apontamentos",
				component: ApontamentosPage,
				authenticated: true,
			},
			{
				title: "feedforwards",
				component: FeedforwardsPage,
				icon: undefined,
				authenticated: true,
			},
			{
				title: "feedforwards-recebidos",
				component: FeedforwardsRecebidosPage,
				authenticated: true,
			},
			{
				title: "avaliacoes",
				component: AvaliacaoListPage,
				authenticated: true,
			},
		];

		this.submenu = [
			{
				title: "fale-conosco",
				component: FaleConoscoPage,
				icon: "mail",
				authenticated: true,
			},
			{
				title: "logout",
				component: LogoutPage,
				icon: "log-out",
				authenticated: true,
			},
		];
	}

	initializeApp() {
		this.platform.ready().then(() => {
			// Okay, so the platform is ready and our plugins are available.
			// Here you can do any higher level native things you might need.
			this.statusBar.styleLightContent();
			this.splashScreen.hide();
			this.loggedUserStore.subscribe({
				next: async state => {
					if (isValidLoggedUserState(state)) {
						const { usuario } = state;
						const { nome, cargo } = usuario;
						let avatar = dadosSidebar.imagemSidebar;
						try {
							avatar = ensureString((await this.fotoAvatarResource.getOne(usuario.id)).base64);
						} catch (e) {
							console.error("Erro ao obter avatar do usuário!", e);
						}
						this.imagemSidebar = avatar;
						this.tituloSidebar = nome || dadosSidebar.tituloSidebar;
						this.subtituloSidebar = (cargo || {}).nome || dadosSidebar.subtituloSidebar;
					} else {
						Object.assign(this, dadosSidebar);
					}
				},
			});
			this.envStore.get().then(async ENV => {
				const key = `app.env.${ENV.name}.label`;
				const environment = await this.translator.get(key);
				this.version = ENV.version;
				this.environment = environment === key ? ENV.name : environment;
			});
		});
		this.verificaConexao();
	}

	verificaConexao() {
		this.translator.get("app.conectionMsg").then(msg => (this.mensagemErro = msg));
		let networkLoader;

		this.network.onDisconnect().subscribe(() => {
			console.log("Por favor, verifique sua conexão com a internet!");
			networkLoader = this.loadingController.create({
				content: this.mensagemErro,
			});
			networkLoader.present();
		});

		this.network.onConnect().subscribe(() => {
			console.log("Network conectado!");
			if (networkLoader) {
				networkLoader.dismissAll();
			}
		});
	}

	openPage(page) {
		// Reset the content nav to have just this page
		// we wouldn't want the back button to show in this scenario
		if (page.title !== "logout") {
			this.activeMenu = page.title;
		}
		this.nav.setRoot(page.component);
	}

	shouldRenderItem(item: Item) {
		if (isBoolean(item.authenticated)) {
			const authenticated = this.authStore.isAuthenticated();
			return authenticated === item.authenticated;
		}
		return true;
	}

	showVersionInfo() {
		this.envStore.get().then(async ENV => {
			const { production, version, gitCommit, gitRef, name, buildDate } = ENV;
			let environmentConfig = '';
			if (production) {
				environmentConfig = `<br>envConfig:<br><pre><code>${JSON.stringify(ENV)}</code></pre>`;
			}
			const alert = this.alertController.create({
				title: 'Build info',
				message: `version: ${version}<br>`
					+ `gitCommit: ${gitCommit}<br>`
					+ `gitRef: ${gitRef}<br>`
					+ `name: ${name}<br>`
					+ `buildDate: ${buildDate}`
					+ environmentConfig,
				buttons: ['OK'],
			});
			alert.present();
		});
	}
}
