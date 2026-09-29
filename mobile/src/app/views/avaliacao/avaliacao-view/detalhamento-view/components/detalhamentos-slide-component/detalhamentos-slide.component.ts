import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { Events, Slides } from "ionic-angular";
import { UtilsService } from "../../../../../../commons/utils.service";
import { TOPIC } from "../../../../../../constants/topics";
import { AvaliacaoDetalhamentoTO } from "../../../../avaliacao.transfer";

@Component({
	selector: "app-detalhamentos-slide",
	templateUrl: "detalhamentos-slide.component.html",
})
export class DetalhamentosSlideComponent {
	@ViewChild(Slides) slides: Slides;
	@Output() updatedDetalhamentoList = new EventEmitter<{ detalhamentoList: AvaliacaoDetalhamentoTO[], isLast: boolean }>();

	private _detalhamentoList: AvaliacaoDetalhamentoTO[];

	constructor(private events: Events, private utils: UtilsService) { }
	onTouchStart(event) {
		/* Workaround para resolver
		o issue do scroll de avatares
		que não estava funcionando */
		this.slides.direction = "vertical";
		this.slides.lockSwipes(true);
	}

	onTouchEnd(event) {
		this.slides.lockSwipes(false);
		this.slides.direction = "horizontal";
	}

	@Input()
	set detalhamentoList(value: AvaliacaoDetalhamentoTO[]) {
		this._detalhamentoList = value;
	}

	get detalhamentoList() {
		return this._detalhamentoList;
	}

	onClickPager(event) {
		this.slides.slideTo(event);
		const index = this.slides.realIndex;
		this.events.publish(TOPIC.preenchimento.avatar.list, this._detalhamentoList[index].listaPreenchimentos);
	}

	onChangeSlide(event) {
		const index = this.slides.realIndex;
		if (this._detalhamentoList[index]) {
			this.events.publish(TOPIC.preenchimento.avatar.list, this._detalhamentoList[index].listaPreenchimentos);
		}
	}

	onUpdateDetalhamento(detalhamento) {
		const lista = detalhamento.listaPreenchimentos;
		this.events.publish(TOPIC.preenchimento.avatar.list, lista);
	}

	onSaveDetalhamentos() {
		let isLast = false;
		const lengthIndex = this.detalhamentoList.length - 1;
		let isObrigatorioESemObs = false;
		let indiceDetalhamento;
		this._detalhamentoList.forEach(detalhamento => {
			if (detalhamento) {
				if (detalhamento.listaPreenchimentos) {
					detalhamento.listaPreenchimentos.forEach(preenchimento => {
						if (preenchimento.graduacao) {
							if (preenchimento.graduacao.comentarioObrigatorio && !preenchimento.observacao) {
								indiceDetalhamento = this._detalhamentoList.indexOf(detalhamento);
								isObrigatorioESemObs = true;
							}
						}
					});
				}
			}
		});
		if (isObrigatorioESemObs) {
			this.utils.presentAlertToast(`Comentário obrigatório na ${indiceDetalhamento + 1}ª pergunta!`);
			return;
		}
		if (this.slides.getActiveIndex() < lengthIndex) {
			this.slides.slideNext();
		} else {
			isLast = true;
		}
		this.updatedDetalhamentoList.emit({ detalhamentoList: this._detalhamentoList, isLast });
	}
}
