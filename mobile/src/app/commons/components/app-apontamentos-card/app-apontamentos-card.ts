import { Component, Input } from "@angular/core";

export interface ApontamentosCardData {
	data?: Date;
	dataApontamento?: Date;
	feedforward?: string;
	apontamento?: string;
	registroCritico?: string;
	graduacaoVisual?: number;
	nota?: number;
	foto?: string;
	fotoBase64?: string;
	competencia: {
		descricao?: string;
		descricaoAbreviada?: string;
	};
	avaliado?: {
		fotoBase64?: string;
		nome?: string;
	}
	avaliador?: {
		fotoBase64?: string;
		nome?: string;
	}
}

@Component({
	selector: "app-apontamentos-card",
	templateUrl: "app-apontamentos-card.html"
})
export class ApontamentosCard {
	private _apontamentosList: ApontamentosCardData[];

	constructor() {}

	get apontamentosList() {
		return this._apontamentosList;
	}

	@Input()
	set apontamentosList(value: ApontamentosCardData[]) {
		this._apontamentosList = value;
	}

	hasFoto(apontamento: ApontamentosCardData) {
		return !!this.getFoto(apontamento);
	}

	hasNomeFuncionario(apontamento: ApontamentosCardData) {
		return !!this.getNomeFuncionario(apontamento);
	}

	getFoto(apontamento: ApontamentosCardData) {
		return apontamento.foto
			|| apontamento.fotoBase64
			|| (apontamento.feedforward && (apontamento.avaliador || {}).fotoBase64)
			|| (apontamento.apontamento && (apontamento.avaliado || {}).fotoBase64)
			|| undefined;
	}

	getNomeCompetencia(apontamento: ApontamentosCardData) {
		return apontamento.competencia.descricaoAbreviada || apontamento.competencia.descricao;
	}

	getNomeFuncionario(apontamento: ApontamentosCardData) {
		return (apontamento.feedforward && (apontamento.avaliador || {}).nome)
			|| (apontamento.apontamento && (apontamento.avaliado || {}).nome)
			|| undefined;
	}

	getData(apontamento: ApontamentosCardData) {
		return apontamento.dataApontamento || apontamento.data;
	}

	getDescricao(apontamento: ApontamentosCardData) {
		return apontamento.feedforward || apontamento.apontamento || apontamento.registroCritico;
	}

	getNota(apontamento: ApontamentosCardData) {
		return (apontamento.graduacaoVisual || apontamento.nota || 0) / 2;
	}
}
