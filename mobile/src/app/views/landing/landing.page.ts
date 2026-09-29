import { Component } from "@angular/core";

import { NavController, Events } from "ionic-angular";

import { AuthStore } from "../../commons/stores/auth.store";
import { LoginPage } from "../login/login.page";
import { AvaliacaoListPage } from "../avaliacao/avaliacao-list/avaliacao-list.page";
import { TOPIC } from "../../constants/topics";

@Component({
	templateUrl: "./landing.page.html",
})
export class LandingPage {
	constructor(private authStore: AuthStore, private navController: NavController, private events: Events) {}

	async ionViewWillEnter() {
		this.events.publish(TOPIC.menu.auth, this.authStore.isAuthenticated());
		if (this.authStore.isAuthenticated()) {
			this.navController.setRoot(AvaliacaoListPage);
		} else {
			this.navController.setRoot(LoginPage);
		}
	}
}
