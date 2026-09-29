import { LoadingController, Loading, ToastController } from "ionic-angular";

import { Injectable } from "@angular/core";

@Injectable()
export class UtilsService {
	private loader: Loading;

	constructor(
		public loadingController: LoadingController,
		public toastController: ToastController,
	) {}

	async loaderPresent(message: string) {
		this.loader = await this.loadingController.create({
			content: message,
		});
		return await this.loader.present();
	}

	loaderDismiss() {
		return this.loader.dismiss();
	}

	async presentToast(message: string) {
		const toast = await this.toastController.create({
			message: message,
			duration: 4000,
		});
		toast.present();
  }
  
  async presentAlertToast(message: string) {
		const toast = await this.toastController.create({
      message: message,
      cssClass: 'error',
			duration: 4000,
		});
		toast.present();
	}

	/* async presentToastWithOptions() {
    const toast = await this.toastController.create({
      header: 'Toast header',
      message: 'Click to Close',
      position: 'top',
      buttons: [
        {
          side: 'start',
          icon: 'star',
          text: 'Favorite',
          handler: () => {
            console.log('Favorite clicked');
          }
        }, {
          text: 'Done',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        }
      ]
    });
    toast.present();
  } */
	/*Comentado para documentação caso
precise usar alguma propriedade do loading

    async presentLoadingWithOptions() {
        const loading = await this.loadingController.create({
            spinner: null,
            duration: 5000,
            message: 'Please wait...',
            translucent: true,
            cssClass: 'custom-class custom-loading'
        });
        return await loading.present();
    } */
}
