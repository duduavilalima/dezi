import {Component} from '@angular/core';
import {Platform} from 'ionic-angular';

@Component({
	selector: "app-separator",
	templateUrl: "app-separator.component.html",
})

export class SeparatorComponent {

	platHeight: number;

  constructor(platform: Platform) {
    platform.ready().then(() => {
      console.log('Width: ' + platform.width());
	  console.log('Height: ' + platform.height());
	  this.platHeight = platform.height();
    });
  }

}
