import { Component, OnInit } from "@angular/core";

import { NavController } from "ionic-angular";

import { AuthService } from "../../commons/services/auth.service";
import { LandingPage } from "../landing/landing.page";

@Component({
	template: "",
})
export class LogoutPage implements OnInit {
	constructor(private authService: AuthService, private navController: NavController) {}

	async ngOnInit() {
		await this.authService.logout();
		this.navController.push(LandingPage);
	}
}
