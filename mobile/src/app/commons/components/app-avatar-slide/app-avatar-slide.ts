import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { Slides } from "ionic-angular";
import { FuncionarioTO } from "../../../model/transfer/funcionario.transfer";
import { ensureNumber, ensureSomething } from "../../../utils/guards";


@Component({
	selector: "app-avatar-slide",
	templateUrl: "app-avatar-slide.html",
})
export class AvatarSlide {
	@ViewChild(Slides) slides: Slides;
	private _avaliados: FuncionarioTO[] = [];
	@Output() selectedAvaliado = new EventEmitter<FuncionarioTO>();

	/* Variavel para aplicação de estilo */
	public activeId: number;
	public isDisabledPrev: boolean = true;
	public isDisabledNext: boolean = false;
	constructor() { }


	@Input()
	set avaliados(value: FuncionarioTO[]) {
		this._avaliados = value;
		if (this._avaliados && this._avaliados.length > 0) {
			this.activeId = ensureNumber(this._avaliados[0].id);
			this.selectedAvaliado.emit(this._avaliados[0]);
		}
		this.checkSlides();
	}

	get avaliados() {
		return this._avaliados;
	}

	onChangeAvaliado() {
		const avaliado = ensureSomething<FuncionarioTO>(
			this._avaliados.find((_, index) => index === this.slides.realIndex)
		);
		this.activeId = ensureNumber(avaliado.id);
		this.selectedAvaliado.emit(avaliado);
		this.checkSlides();
	}

	checkSlides() {
		const isSingleElement = this.avaliados.length <= 1;
		const atLastElement = this.slides.realIndex === this.avaliados.length - 1;
		const atFirstElement = this.slides.realIndex === 0;

		if (isSingleElement) {
			this.isDisabledNext = true;
			this.isDisabledPrev = true;

		} else if (atLastElement) {
			this.isDisabledNext = true;
			this.isDisabledPrev = false;

		} else if (atFirstElement) {
			this.isDisabledNext = false;
			this.isDisabledPrev = true;

		} else {
			this.isDisabledNext = false;
			this.isDisabledPrev = false;
		}

		this.slides.lockSwipeToNext(this.isDisabledNext);
		this.slides.lockSwipeToPrev(this.isDisabledPrev);
	}

	prevSlide() {
		this.checkSlides();
		if (!this.isDisabledPrev) {
			this.slides.slidePrev();
		}
	}

	nextSlide() {
		this.checkSlides();
		if (!this.isDisabledNext) {
			this.slides.slideNext();
		}
	}
}
