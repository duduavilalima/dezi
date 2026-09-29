import { Component, Input, ViewChild, Output, EventEmitter } from "@angular/core";

import { Slides } from "ionic-angular";
import { TipoCompetenciaTO } from "../../../model/transfer/tipoCompetencia.transfer";

interface LocalTipoCompetencia {
	id: number;
	nomeCurto?: string;
	nomeTipoCompetencia?: string;
}

@Component({
	selector: "app-tipo-competencia-slides",
	templateUrl: "tipo-competencia-slides.html",
})
export class TipoCompetenciaSlides {
	@ViewChild(Slides)
	slides: Slides;

	private _isUntouched = true;
	private _tipoCompetenciaList: LocalTipoCompetencia[] = [];
	private _activeId: number;
	@Output() selectedTipo = new EventEmitter<TipoCompetenciaTO>();

	constructor() { }


	ionViewWillEnter() {
		this.slides.centeredSlides = false;
	}

	get tipoCompetenciaList() {
		return this._tipoCompetenciaList;
	}

	@Input()
	set tipoCompetenciaList(value: LocalTipoCompetencia[]) {
		this._tipoCompetenciaList = value;
		this.onReloadHandler();
	}

	@Input()
	set activeId(value: number | undefined) {
		if (value) {
			this._activeId = value;
		} else {
			this._activeId = 0;
		}
	}

	get activeId() {
		return this._activeId;
	}

	get tipoCompetencia() {
		return this.findTipoCompetenciaById(this._tipoCompetenciaList, this._activeId);
	}

	set tipoCompetencia(value: LocalTipoCompetencia | undefined) {
		this.findTipoCompetenciaById(this._tipoCompetenciaList, this._activeId);
	}

	onChangeTipoCompetencia() {
		this._isUntouched = false;
		this.actionEmitter(this._tipoCompetenciaList);
	}

	private onReloadHandler() {
		if (!this._activeId && this._tipoCompetenciaList && this._tipoCompetenciaList.length > 0) {
			this.activeId = this._tipoCompetenciaList[0].id;
			this.selectedTipo.emit(this._tipoCompetenciaList[0]);
		} else {
			this.selectedTipo.emit(this.tipoCompetencia);
		}
	}

	private actionEmitter(list: LocalTipoCompetencia[]) {
		if (list && list.length > 0) {
			if (this.slides.getActiveIndex() < list.length) {
				const tipoCompetencia = this.findTipoCompetenciaByIndex(list, this.slides.getActiveIndex());
				this.activeId = tipoCompetencia ? tipoCompetencia.id : undefined;
				this.selectedTipo.emit(tipoCompetencia);
			}
		}
	}

	//FIXME: Alterar posteriormente nas pages de feedforwards recebidos 
	//e apontamentos para passar o Id para voltar para a competencia selecionada previamente
	private findTipoCompetenciaByIndex(list: LocalTipoCompetencia[], index: number | undefined) {
		if (list && list.length > 0) {
			return list.find((_, elIndex) => elIndex === index);
		}
		return undefined;
	}


	private findTipoCompetenciaById(list: LocalTipoCompetencia[], id: number | undefined) {
		if (list && list.length > 0) {
			return list.find((elem) => elem.id === id);
		}
		return undefined;
	}

	private findIndexByTipoCompetencia(list: LocalTipoCompetencia[], elem: LocalTipoCompetencia | undefined) {
		if (!!elem && list && list.length > 0) {
			const entry = list
				.map((tipoCompetencia, index) => ({ tipoCompetencia, index }))
				.find(entry => entry.tipoCompetencia.id === elem.id);
			if (entry) {
				return entry.index;
			}
		}
		return undefined;
	}
}
