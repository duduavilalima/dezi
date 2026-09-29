import { Component, Input } from "@angular/core";
import { ModalController } from "ionic-angular";
import { HintModal } from "./hint-modal/hint.modal";
import { AvaliacaoGraduacaoTO } from "../../../../avaliacao.transfer";

@Component({
	selector: 'app-label',
	templateUrl: 'label.component.html',
})
export class LabelComponent {
	private _graduacao: AvaliacaoGraduacaoTO;

	constructor(private modalCtrl: ModalController) {}

	@Input()
	set graduacao(value: AvaliacaoGraduacaoTO) {
		this._graduacao = value;
	}

	get graduacao() {
		return this._graduacao;
	}

	onOpenHint(hint: string) {
		let hintModal = this.modalCtrl.create(HintModal, {hint});
		hintModal.present();

	}

}

